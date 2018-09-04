$(function () {
    var initOffice = function () {
        $.getJSON(makeAjaxUrl("/sys/qryOffice/xx"), function (data, textStatus, jqXHR) {
            var arr = jQuery.map(data, function (ele, i) {
                $("#branchOffice").append("<option value='"+ele["id"]+"'>"+ele["name"]+"</option>");
            });
        });
    };
    var initDepart = function() {
        $.getJSON(makeAjaxUrl("/sys/qryDepart/xx"), function (data, textStatus, jqXHR) {
            var arr = jQuery.map(data, function (ele, i) {
                $("#department").append("<option value='"+ele["id"]+"'>"+ele["name"]+"</option>");
            });
        });
    }
    initOffice();
    initDepart();


    $("#btn_confirm").click(function(){
        var username = $(".name").val();
        var no = $(".no").val();
        var companyId = $("#branchOffice").val();
        var officeId = $("#department").val();
        $.getJSON(makeAjaxUrl("/sys/bindAccount/" + username + "/" + no + "/" +  companyId + "/" + officeId ), function (data, textStatus, jqXHR) {
            window.location.href = "me.html"
            // if(data.name != null){
            //
            // }
            // var staffObj = {"data": data};
            // alert(staffObj);
        });
        alert("姓名是" + username + "手机号码是" + no + "所属公司" + companyId + "部门" + officeId);
    });
});