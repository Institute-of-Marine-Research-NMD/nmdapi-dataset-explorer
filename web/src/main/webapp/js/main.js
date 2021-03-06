var app = {};
$(document).ready(function () {


    app.baseCruiseNames={"Forskningsfartøy":true,"Leiefartøy":true};
    
    app.cruiseDatasetNames = [];
    
  

    app.baseURL = "../";
 
    // unblock when ajax activity stops 
    $(document).ajaxStop($.unblockUI);

    //Init tree
    $('#browseTree').jstree({
        "core": {
            "animation": 0,
            'check_callback': true},
        "types": {
            "#": {
                "icon": "jstree-file"
            },
            "root": {
                "icon": "jstree-file",
                "valid_children": ["default"]
            },
            "default": {
                "valid_children": ["default", "file"]
            },
            "file": {
                "icon": "jstree-file"
            }
        },
        "plugins": ["types"]
    });

    //Keep reference for calls
    var browseTree = $('#browseTree').jstree(true);


    var callRest = function (path, success,param)
    {
        //  $.blockUI({ message: $('#waitMessage') }); 
        $.ajax({
            url: app.baseURL + path,
            data:param,
            dataType: "json"
        }).done(function (data) {
            success(data);
        }).fail(function () {

            $("<div>").html("Unable to retrieve data").dialog({
                modal: true,
                buttons: {
                    "Ok": function ()
                    {
                        $(this).dialog("close");
                    }
                }
            });
        });
    };
    
    //Hidden cache reloader
    $("#s2dlogo").click(function(e) {
         if(e.ctrlKey) {
             console.log("Shhhh lets Refresh");
             $("<div>").html("Cache reload queued").dialog({
                modal: true,
                buttons: {
                    "Ok": function ()
                    {
                        $(this).dialog("close");
                    }
                }}) ;

             
                 $.ajax({
            url: app.baseURL + "cacheReload"
        }).done(function (data) {
           console.log("Boom!");
                   $("<div>").html("Cache reload complete").dialog({
                modal: true,
                buttons: {
                    "Ok": function ()
                    {
                        $(this).dialog("close");
                    }
                }
            });

        });
             
         }
      });

    callRest("listCruiseDatsetTypes", function (data) {
        app.cruiseDatasetNames = data.filter(function(s) {return s != "CRUISE"});
    });

    //Setup expand node handling
    $("#browseTree").bind(
            "select_node.jstree", function (evt, data) {
                var par = data.node.id;
                var orgNode = browseTree.get_node(data.node.id).original;
                if (orgNode.nop) {
                    return;
                }

                //Check if already expanded
                if ((data.node.children.length > 0) && (!orgNode.prepop))
                {
                    browseTree.open_node(par);
                    return;
                }

                var path = orgNode.fullPath;
   
                if (orgNode.cruiseSeries) {
                    switch (data.node.parents.length) {
                        case 1:
                            addCruiseSeries(data.node.id, path);
                            break;
                        case 2:
                            addDatasetSummary(data.node.id, "CruiseSeries/summary", path);
                            addCruiseSeries(data.node.id, path);
                            break;
                        case 3:
                            addDatasetSummary(data.node.id, "CruiseSeries/summary", path);
                            addCruiseSeriesCruises(data.node.id, path);
                            break;
                        case 4:
                            addCruiseDatasets(data.node.id, orgNode.cruiseCode,orgNode.shipName);
                            break;
                        case 5:
                    }
                } else if (orgNode.timeSeries) {
                    switch (data.node.parents.length) {
                        case 1:
                            addTimeSeries(data.node.id, path);
                            break;
                        case 2:
                            addTimeSeriesSamples(data.node.id, path);
                            break;
                        case 3:
                            addTimeSeriesDatasets(data.node.id, path);
                            break;
                        case 4:
                            addCruiseDatasets(data.node.id, orgNode.cruiseCode,orgNode.shipName);
                            break;
                    }
                } else {
                    //Use switch in case we need special handling later for specific levels
                    switch (data.node.parents.length) {
                        case 3:
                            addDatasetSummary(data.node.id, "statusByCruise", path);
                            addChildrenWithCount(data.node.id, path);
                            break;
                    case 1:
    	break;
                        case 2:
                        case 4:
                            addChildrenWithCount(data.node.id, path);
                            break;
                        case 5:
                            addDatasets(data.node.id, path);
                            break;
                    }



                }


            }
    );

    var initData = callRest("count", function (data) {

	var  identified=0;
	var  loaded=0;
	var  missionCount=0

	var baseCruises={identified:0,loaded:0};
	var otherCruises={identified:0,loaded:0}; 
	var cruises;

	var cruiseRoot = browseTree.create_node("#", {fullPath: "/"}, "first");
	var otherCruiseRoot = browseTree.create_node("#", {fullPath: "/"}, "last");
	
	for (var i = 0; i < data.length; i++) {
	    if (app.baseCruiseNames[data[i].name]) {
		cruises=baseCruises;
		browseTree.create_node(cruiseRoot, {text: formatCount(data[i].name, data[i].count),
						    fullPath: data[i].parentPath + data[i].name});

	    } else {
		cruises=otherCruises;
		browseTree.create_node(otherCruiseRoot, {text: formatCount(data[i].name, data[i].count),
						    fullPath: data[i].parentPath + data[i].name});
	    }
	    
	    identified += data[i].count.identified;
	    cruises.identified += data[i].count.identified;
	    loaded += data[i].count.loaded;
	    cruises.loaded += data[i].count.loaded;
	    missionCount += data[i].count.missionCount;
	    cruises.missionCount += data[i].count.missionCount;
	}

	browseTree.rename_node(cruiseRoot,formatCount("Cruise", baseCruises));
	browseTree.rename_node(otherCruiseRoot,formatCount("Commercial sampling", otherCruises));
	
        browseTree.create_node("#", {text: "Cruise series", cruiseSeries: true,
            fullPath: "/"},
        "last");
        browseTree.create_node("#", {text: "Survey Timeseries", timeSeries: true,
            fullPath: "/"},
        "last");

        var statPercent = Math.round(loaded / identified * 100.0);
        $('<div/>', {
            "data-type": "half",
            "data-fgcolor": "#61a9dc",
            "data-fill": "#ddd",
            "data-total": identified,
            "data-part": loaded,
            "data-text": statPercent + "%",
            "data-info": "Datasets loaded",
            id: "allStatDial"
        }).appendTo('#totalStatus');
        $('#allStatDial').circliful();

        $('#totalStatusMessage1').html("Datasets loaded: " + loaded);
        $('#totalStatusMessage2').html("Datasets identified: " + identified);
        $('#totalStatusMessage3').html("Cruise count: " + missionCount);


    });


    var addDatasetSummary = function (par, url, path) {
        browseTree.create_node(par, {"type": "file",
            text: "<a class='summaryLink' href='#' onClick='javascript:app.showSummary(\"" + url + "\",\"" + path + "\")'>Summary</a>",
            nop: true}
        , 'last');
    };





    var addChildrenWithCount = function (par, path)
    {
        var countText;
        callRest("count" + path, function (data) {
            for (var i = 0; i < data.length; i++)
            {
                countText = formatCount(data[i].name, data[i].count);
                browseTree.create_node(par, {text: countText,
                    fullPath: data[i].parentPath + data[i].name}
                , 'last');
            }
            browseTree.open_node(par);
        });
    };

    var addCruiseSeries = function (par, path)
    {
        callRest("CruiseSeries/list" + path, function (data) {
            for (var i = 0; i < data.length; i++)
            {
                browseTree.create_node(par, {text: data[i], cruiseSeries: true,
                    fullPath: path + "/" + data[i]}
                , 'last');
            }
            browseTree.open_node(par);
        });
    }


    var addCruiseSeriesCruises = function (par, path)
    {
        var countText;
        callRest("CruiseSeries/list" + path, function (data) {
            for (var i = 0; i < data.length; i++)
            {
                countText = data[i].cruiseCode;
                browseTree.create_node(par, {text: countText, cruiseSeries: true,
                    cruiseCode: data[i].cruiseCode,shipName: data[i].shipName}
                , 'last');
            }
            browseTree.open_node(par);
        });
    }



    var addCruiseDatasets = function (par, cruiseCode,shipName)
    {
        callRest("Cruise/find/", function (cruiseData) {

            callRest("list" + cruiseData, function (data) {
                var dataSetText;
                jQuery.each(data, function (name, value) {
                    if (value != "N/A")
                    {
                        dataSetText = createProxyDownloadLink(name,value);
       
       //                 dataSetText = createDataLink(name,name+"_"+cruiseCode+".xml", value);
                    }
                    else
                    {
                        dataSetText = name + " Not loaded";
                    }
                    browseTree.create_node(par, {"type": "file", cruiseSeries: true,
                        text: dataSetText
                    }
                    , 'last');
                });
                browseTree.open_node(par);

            });
        },{cruiseCode:cruiseCode,shipName:shipName});
    };



    var addTimeSeries = function (par, path)
    {

        var countText;
        callRest("SurveyTimeSeries/list" + path, function (data) {
            for (var i = 0; i < data.length; i++)
            {
                countText = data[i];
                browseTree.create_node(par, {text: countText, timeSeries: true,
                    fullPath: path + "/" + data[i]}
                , 'last');
            }
            browseTree.open_node(par);
        });
    }

    var addTimeSeriesSamples = function (par, path)
    {
     addDatasetSummary(par, "SurveyTimeSeries/summary", path);
   
        callRest("SurveyTimeSeries/listSamples" + path, function (data) {
            for (var i = 0; i < data.length; i++)
            {
                countText = data[i].sampleTime;
                var newNode = browseTree.create_node(par,
                        {text: countText,
                            timeSeries: true,
                            fullPath: path + "/" + data[i].sampleTime,
                            prepop: true}
                , 'last');

                addDatasetSummary(newNode, "SurveyTimeSeries/summary", path + "/" + data[i].sampleTime);
                browseTree.create_node(newNode, {"type": "file", timeSeries: true,
                    text: createProxyDownloadLink("Stox", data[i].stoxURL), nop: true}
                , 'last');

                browseTree.create_node(newNode, {"type": "file", timeSeries: true,
                    text: createDownloadLink("Zip ", data[i].zipURL), nop: true}
                , 'last');


            }
            browseTree.open_node(par);
        });
    }



//Ugly hack need to refactor so easier to read
    var addTimeSeriesDatasets = function (par, path)
    {

        callRest("SurveyTimeSeries/listCruise" + path, function (cruiseList) {
            for (var i = 0; i < cruiseList.length; i++) {
                cruiseNR = cruiseList[i].cruiseCode;
                browseTree.create_node(par, {text: cruiseNR, cruiseSeries: true,
                    cruiseCode:cruiseList[i].cruiseCode,shipName:cruiseList[i].shipName}
                , 'last');

            }
            browseTree.open_node(par);

        });
    }





    var addDatasets = function (par, path)
    {

        callRest("list" + path, function (data) {
            var dataSetText;
            var pathList;
            jQuery.each(data, function (name, value) {
                if (value != "N/A")
                {
                   dataSetText = createProxyDownloadLink(name,value);
                }
                else
                {
                    dataSetText = name + " Not loaded";
                }
                browseTree.create_node(par, {"type": "file",
                    text: dataSetText
                }
                , 'last');
            });
            browseTree.open_node(par);

        });
    };

    var createDownloadLink = function (name, value)
    {
        return   name + " <a class='downloadLink' href='" + value + "'  onClick='javascript:app.downloadFile(event)'  >Download</a>";
    };


    var createProxyDownloadLink = function (name, value)
    {
        var proxyURL =     app.baseURL + "SimpleFetch?src="+encodeURIComponent(value);
         return   name + " <a class='dataLink' href='" + proxyURL + "' title='"+name+"' onClick='javascript:app.showData(event)'>Link</a>";
    };

    app.showSummary = function (url, path) {

        callRest(url + path, function (data) {
            var sumTable = $("<table class='summaryTable'>");
            var head = $("<tr class='summaryHeader' >");
            head.append("<th class='summaryHead' >Cruise Code</th>");
            head.append("<th class='summaryHead' >Stop date</th>");
            for (var i = 0; i < app.cruiseDatasetNames.length; i++) {
                head.append("<th class='summaryHead' >" +
                        app.cruiseDatasetNames[i].charAt(0) +
                        app.cruiseDatasetNames[i].substring(1).toLowerCase()+
                        "</th>");
            }
            sumTable.append(head);
 

            jQuery.each(data, function (index, value) {
                var row = $("<tr class='summaryRow' >");
                var cellClass;
                row.append("<td class=' cruisecode' >" + value.delivery + " " + value.platform + "</td>");
                row.append("<td class='stopDate' >" + value.stopDate+ "</td>");

                for (var i = 0; i < app.cruiseDatasetNames.length; i++) {
                    if (value.loaded[app.cruiseDatasetNames[i]]) {  // Do we know anything about dataset
                        cellClass = value.loaded[app.cruiseDatasetNames[i]] + "_" + value.exists[app.cruiseDatasetNames[i]];
                        row.append("<td class='summaryCell " + cellClass + "' >");
                    } else {
                        row.append("<td class='summaryCell UNKNOWN' >");
                    }
                }

                sumTable.append(row);
            });
            var sumDialog = $("<div>");
            sumDialog.append(sumTable);

            sumDialog.dialog({
                autoOpen: true,
                modal: true,
                dialogClass: 'fixed-dialog',
                height: 625,
                maxHeight: 700,
                width: 650,
                position: {
                    my: "left top",
                    at: "left+200 top+100",
                    of: window,
                    collision: "none"
                },
                title: "Summary for " + path.replace(/\\/, ''),
                buttons: {
                    "Ok": function ()
                    {
                        $(this).dialog("close");
                    }}
            });
            sumDialog.dialog('open');
        });
    }

    app.downloadFile = function (ev) {
        window.location.href = ev.srcElement.href;
    }

    app.showData = function (ev) {
        var dataUrl = ev.srcElement.href;
        var dialogTitle = ev.srcElement.title;
        $.ajax({
            url: dataUrl.replace(/Simple/,"Partial")+"&length=5000",
            dataType: "text"
        }).done(function (data) {
            //Indent xml
            data = vkbeautify.xml(data);
            //Then escape xml
            data = data.replace(/&/g, '&amp;')
                    .replace(/</g, '&lt;')
                    .replace(/>/g, '&gt;')
                    .replace(/"/g, '&quot;')
                    .replace(/'/g, '&apos;');
            
            
            //Create dialog
            var dialog = $("<div>").
                    html("<a href = '" + dataUrl + "' >Download xml</a>" +
                            "<pre class='prettyprint'>"
                            + data + "</pre>")
                    .dialog({
                        autoOpen: false,
                        modal: true,
                        dialogClass: 'fixed-dialog',
                        height: 625,
                        width: 650,
                        position: {
                            my: "left top",
                            at: "left+200 top+100",
                            of: window,
                            collision: "none"
                        },
                        title: dialogTitle,
                        buttons: {
                            "Ok": function ()
                            {
                                $(this).dialog("close");
                            }}
                    });
            //Format xml       
            prettyPrint();
            dialog.dialog('open');



        }).fail(function () {

            $("<div>").html("Unable to retrieve xml data").dialog({
                modal: true,
                buttons: {
                    "Ok": function ()
                    {
                        $(this).dialog("close");
                    }
                }
            });
        });


        return false;
    }

    var formatCount = function (name, data)
    {
        return name + "   (" + data.loaded + "/" + data.identified + ")  ";//+data.missionCount+" missions";
    }
});
