var app = {};
$(document).ready(function () {

   app.baseURL = "../";
    var resultTable =  $('#resultTable').DataTable({
        columns: [
            {data: "missionType", title: "Mission Type"},
            {data: "year", title: "Year"},
            {data: "platform", title: "Platform"},
            {data: "delivery", title: "Delivery"},
            {data: "dataset", title: "Data Type"},
        ]
    });
    
    var callRest = function (path, success,param)
    {
        $.ajax({
            url: app.baseURL + path,
            data:param,
            dataType: "json"
        }).done(function (data) {
            success(data);
        }).fail(function () {
            console.log("Fail");
         
        });
    };
    
    $("#searchBtn").click(function(e) {
      callRest("findDataset", function (data) {
         console.log("Got ",data);
         resultTable.clear();
         resultTable.rows.add(data);
         resultTable.draw();
         
        },{year:$("#year").val()});
      });


   
});
