var app={};
$(document).ready(function() {

    app.baseURL ="../"; 
    
    //Init tree
    $('#browseTree').jstree({
	"core" : {
	    "animation" : 0,
	    'check_callback': true},
	"types" : {
	    "#" : {
		"icon" : "jstree-file",
		"max_children" : 1, 
	    },
	    "root" : {
		"icon" : "jstree-file",
		"valid_children" : ["default"]
	    },
	    "default" : {
		"valid_children" : ["default","file"]
	    },
	    "file" : {
		"icon" : "jstree-file",
	    }
	},
	"plugins" : ["types"]
    });

    //Keep reference for calls
    var browseTree = $('#browseTree').jstree(true);
    
      
    var callRest = function(path,success)
    {
	$.ajax({
	    url: app.baseURL+path,
	    dataType: "json",
	}).done(function (data) {
	    console.log(data);
	    success(data);
	}).fail(function() {
	    
	      $("<div>").html("Unable to retrieve data").dialog({
		  modal: true,
		  buttons: {
		      "Ok": function() 
		      {
			  $( this ).dialog( "close" );
		      }
		  }
	      });
	});
    }
    
    //Setup expand node handling
    $("#browseTree").bind(
        "select_node.jstree", function(evt, data){
 	    var par = data.node.id;
	    //Check if already expanded
	    if (data.node.children.length > 0)
	    {
		return;
	    }

	    var path = browseTree.get_node(data.node.id).original.fullPath;
	    //Use switch in case we need special handling later for specific levels
	    switch (data.node.parents.length) {
	    case 1:
	    case 2:
	    case 3:
	    case 4:
		addChildrenWithCount(data.node.id,path);
		break;
	    case 5:
		addDatasets(data.node.id,path);
		break;
	    case 6:
		console.log("data type");
	    }
        }
    );

    var initData = callRest("countAll",function(data){
	browseTree.create_node("#",{text:formatCount("All",data),
				    fullPath:"/"},
			       "first");
	var statPercent = Math.round(data.actual/data.total*100.0);
	$('<div/>', {
	    "data-type":"half" ,
	    "data-fgcolor":"#61a9dc",
	    "data-fill":"#ddd",
	    "data-total":data.total,
	    "data-part":data.actual,
	    "data-text":statPercent+"%",
	    "data-info":"Datasets loaded",
	    id:"allStatDial"
	}).appendTo('#totalStatus');
	$('#allStatDial').circliful();
	
	$('#totalStatusMessage1').html("Datasets loaded: "+data.actual);
	$('#totalStatusMessage2').html("Datasets identified: "+data.total);
        	$('#totalStatusMessage3').html("Mission count: "+data.missionCount);
        

    });

    


    

   var addChildrenWithCount=function(par,path)
    {
	var countText;
	callRest("count"+path,function (data) {
	    for (var i=0;i<data.length;i++)
	    {
		countText=formatCount(data[i].name,data[i].count);
		browseTree.create_node(par,{text:countText,
					    fullPath:data[i].parentPath+data[i].name}
				       ,'last');
	    }
	    browseTree.open_node(par);
	});
    }

    var addDatasets=function(par,path)
    {
	callRest("list"+path,function (data) {
	    var dataSetText;
	    jQuery.each(data, function (name, value) {
		if (value != "N/A")
		{
		    dataSetText = name+" <a class='dataLink' href='"+value+"' >Link</a>";
		}
		else
		{
		    dataSetText = name+" Not loaded"
		}
		browseTree.create_node(par,{"type":"file",
					    text:dataSetText
					   }
				       ,'last');
	    });
	    browseTree.open_node(par);
	    
	});
    }
    
    var formatCount = function(name,data)
    {
	return name+"   ("+data.actual+"/"+data.total+")  "+data.missionCount+" missions";
    }
});
