var app = {};
$(document).ready(function () {

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


    var callRest = function (path, success)
    {
        //  $.blockUI({ message: $('#waitMessage') }); 
        $.ajax({
            url: app.baseURL + path,
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

    //Setup expand node handling
    $("#browseTree").bind(
            "select_node.jstree", function (evt, data) {
                var par = data.node.id;
                //Check if already expanded
                if (data.node.children.length > 0)
                {
                    return;
                }
               var orgNode = browseTree.get_node(data.node.id).original;

                if (orgNode.nop) {
                   return;
                }
                var path = orgNode.fullPath;
                //    console.log(orgNode);
                if (orgNode.cruiseSeries) {
                    console.log("Cruise series");
                    switch (data.node.parents.length) {
                        case 1:
                        case 2:
                        case 3:
                            addCruiseSeries(data.node.id, path);
                            break;
                        case 4:
                            addCruiseDatasets(data.node.id, orgNode.text);
                            break;
                        case 5:
                            console.log("data type");
                    }
                } else if (orgNode.timeSeries) {
                    console.log("Time series");

                    switch (data.node.parents.length) {
                        case 1:
                        case 2:
                            addTimeSeries(data.node.id, path);
                            break;
                        case 3:
                            addTimeSeriesDatasets(data.node.id, path);
                            break;
                        case 4:
                            console.log("data type");
                    }
                } else {
                    console.log("Dataset ");

                    //Use switch in case we need special handling later for specific levels
                    switch (data.node.parents.length) {
                        case 3:
                            addDatasetSummary(data.node.id, path);
                            addChildrenWithCount(data.node.id, path);
                            break;
                        case 1:
                        case 2:
                        case 4:
                            addChildrenWithCount(data.node.id, path);
                            break;
                        case 5:
                            addDatasets(data.node.id, path);
                            break;
                        case 6:
                            console.log("data type");
                    }



                }


            }
    );

    var initData = callRest("countAll", function (data) {


        browseTree.create_node("#", {text: formatCount("All by Cruise", data),
            fullPath: "/"},
        "first");
        browseTree.create_node("#", {text: "All by Cruise series", cruiseSeries: true,
            fullPath: "/"},
        "last");
        browseTree.create_node("#", {text: "All by TimeSeries", timeSeries: true,
            fullPath: "/"},
        "last");


        var statPercent = Math.round(data.actual / data.total * 100.0);
        $('<div/>', {
            "data-type": "half",
            "data-fgcolor": "#61a9dc",
            "data-fill": "#ddd",
            "data-total": data.total,
            "data-part": data.actual,
            "data-text": statPercent + "%",
            "data-info": "Datasets loaded",
            id: "allStatDial"
        }).appendTo('#totalStatus');
        $('#allStatDial').circliful();

        $('#totalStatusMessage1').html("Datasets loaded: " + data.actual);
        $('#totalStatusMessage2').html("Datasets identified: " + data.total);
        $('#totalStatusMessage3').html("Cruise count: " + data.missionCount);


    });


    var addDatasetSummary = function(par, path){
         browseTree.create_node(par, {"type": "file",
             text: "<a class='summaryLink' href='#' onClick='javascript:app.showSummary(\""+path+"\")'>Summary</a>",
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

        var countText;
        callRest("CruiseSeries/list" + path, function (data) {
            for (var i = 0; i < data.length; i++)
            {
                countText = data[i];
                browseTree.create_node(par, {text: countText, cruiseSeries: true,
                    fullPath: path + "/" + data[i]}
                , 'last');
            }
            browseTree.open_node(par);
        });
    }



    var addCruiseDatasets = function (par, cruiseNR)
    {
        callRest("Cruise/mapByNR/" + cruiseNR, function (cruiseData) {

            callRest("list" + cruiseData, function (data) {
                var dataSetText;
                jQuery.each(data, function (name, value) {
                    if (value != "N/A")
                    {
                        dataSetText = createDataLink(name, value);

                        //                      dataSetText = name + " <a class='dataLink' href='" + value + "' target='_blank'>Link</a>";
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
        });
    };


    var addTimeSeries = function (par, path)
    {

        var countText;
        callRest("TimeSeries/list" + path, function (data) {
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


//Ugly hack need to refactor so easier to read
    var addTimeSeriesDatasets = function (par, path)
    {
        browseTree.create_node(par, {"type": "file", timeSeries: true,
            text: "Stox not loaded"}
                            , 'last');
        
        callRest("TimeSeries/list" + path, function (cruiseList) {
                     console.log(" cl " + cruiseList);

            for (var i = 0; i < cruiseList.length; i++)  {
                cruiseNR = cruiseList[i];
                console.log(" nr " + cruiseNR);
       
                callRest("Cruise/mapByNR/" + cruiseNR, function (cruiseData) {

                    callRest("list" + cruiseData, function (data) {
                        var dataSetText;
                        jQuery.each(data, function (name, value) {
                            if (value != "N/A")
                            {
                                dataSetText = createDataLink(cruiseNR + "_" + name, value);
                            }
                            else
                            {
                                dataSetText =cruiseNR+"_"+ name + " Not loaded";
                            }
                            browseTree.create_node(par, {"type": "file", timeSeries: true,
                                text: dataSetText
                            }
                            , 'last');
                        });
                        browseTree.open_node(par);

                    });
                });
            }
        });
    }





    var addDatasets = function (par, path)
    {

        callRest("list" + path, function (data) {
            var dataSetText;
            jQuery.each(data, function (name, value) {
                if (value != "N/A")
                {
                    dataSetText = createDataLink(name, value);
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

    var createDataLink = function (name, value)
    {
        console.log(name + " " + value);
        var result = name + " <a class='dataLink'  download='data.xml' href='" + value + "' target='_blank'>Link</a>";
        //    if (name == "cruise"){

        result = name + " <a class='dataLink' href='" + value + "' onClick='javascript:app.showData(event)'>Link</a>"

        //  }
        return  result;
    };
    
    app.showSummary = function (path) {
        console.log("show summary");
        console.log(path);
          callRest("summarizeByCruise" + path, function (data) {
         console.log(data);
         var sumTable =  $("<table class='summaryTable'>");
         var head=  $("<tr class='summaryHeader' >");
         head.append("<th class='summaryHead' >Cruise Code</th>");
         head.append("<th class='summaryHead' >Cruise</th>");
         head.append("<th class='summaryHead' >Biotic</th>");
         head.append("<th class='summaryHead' >Echosounder</th>");
         head.append("<th class='summaryHead' >Physics</th>");
         head.append("<th class='summaryHead' >Chemistry</th>");
         head.append("<th class='summaryHead' >Eventlogger</th>");
     
         
         sumTable.append(head);        
              
               
                jQuery.each(data, function (name, value) {
                    var row =  $("<tr class='summaryRow' >");
                    row.append("<td class='summaryCell' >"+name+"</td>");
                    if  (value.cruise) {
                    row.append("<td class='summaryCell loaded' >");
                    }else {
                    row.append("<td class='summaryCell missing' >");
                    }
                    if  (value.biotic) {
                    row.append("<td class='summaryCell loaded' >");
                    }else {
                    row.append("<td class='summaryCell missing' >");
                    }
              
                 if  (value.echosounder) {
                    row.append("<td class='summaryCell loaded' >");
                    }else {
                    row.append("<td class='summaryCell missing' >");
                    }
              
               if  (value.physics) {
                    row.append("<td class='summaryCell loaded' >");
                    }else {
                    row.append("<td class='summaryCell missing' >");
                    }
              
               if  (value.chemistryr) {
                    row.append("<td class='summaryCell loaded' >");
                    }else {
                    row.append("<td class='summaryCell missing' >");
                    }
              
               if  (value.eventlogger) {
                    row.append("<td class='summaryCell loaded' >");
                    }else {
                    row.append("<td class='summaryCell missing' >");
                    }
              
              
              
                    sumTable.append(row);
                });
                 var sumDialog =  $("<div>");
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
                        title: "Summary for "+path,
                        buttons: {
                            "Ok": function ()
                            {
                                $(this).dialog("close");
                            }}
                    });
                  sumDialog.dialog('open');
    });
    }
    

    app.showData = function (ev) {
        var dataUrl = ev.srcElement.href;
        console.log("show data");
        //   window.open(dataUrl);

        $.ajax({
            url: dataUrl,
            dataType: "text"
        }).done(function (data) {
            //Truncate xml if too big for display
            if (data.length > 50000) {
                data = data.substring(0, 50000)
            }
            ;
            //Then indent xml
            data = vkbeautify.xml(data);
            //Then escape xml
            data = data.replace(/&/g, '&amp;')
                    .replace(/</g, '&lt;')
                    .replace(/>/g, '&gt;')
                    .replace(/"/g, '&quot;')
                    .replace(/'/g, '&apos;');
            //Create dialog
            var dialog = $("<div>").
                    html("<a href = '" + dataUrl + "' download = 'data.xml' >Download xml</a>" +
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
                        title: "Data.xml",
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
        return name + "   (" + data.actual + "/" + data.total + ")  ";//+data.missionCount+" missions";
    }
});
