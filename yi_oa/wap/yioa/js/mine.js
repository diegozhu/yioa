var user;
$(function () {
    var initOrder = function () {
        $.getJSON(makeAjaxUrl("/sys/staff/1456c33018ba403abf93544a767a9ca2"), function (data, textStatus, jqXHR) {
            user = data;
            if(user.officeId){
                $.getJSON('/a/sys/qryOffice/' + user.officeId, function(data){
                    var company = [];
                    for(var i in data){
                        company.push(data[i].name);
                    }
                    user.company = company.join(',');
                    $('#compnay').html(user.company);
                }, function(){
                    $('#compnay').html('暂无办公室信息');
                });
            }else{
                $('#compnay').html('暂无办公室信息');
            }
            for (var key in data) {
                if (key == "roleList") {
                    var arr = jQuery.map(data[key], function (ele, i) {
                        return ele["name"];
                    });
                    $("#roleStr").html(arr.join(", "));
                } else {
                    $("#" + key).html(data[key]);
                }
            }
        });
    };
    initOrder();
    $("#login_out").click(function(){
        $.getJSON(makeAjaxUrl("/loginout"), function (data, textStatus, jqXHR) {
            if(data.login_flag == "1"){
                window.location.href = "login.html"
            }
        });
    });
    $("#changePhone").click(function () {
        window.location.href="change_phone.html";
    });
});