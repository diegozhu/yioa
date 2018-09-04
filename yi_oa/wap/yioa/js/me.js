$(function () {
    $.getJSON(makeAjaxUrl("/sys/staff/xx"), function (data, textStatus, jqXHR) {
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
    $("#login_out").click(function(){
        $.getJSON(makeAjaxUrl("/loginout"), function (data, textStatus, jqXHR) {
            if(data.login_flag == "1"){
                window.location.href = "login.html"
            }
        });
    });
    $("#changePhone").click(function () {
        window.location.href="change_phone.html?tel=" + $("#mobile").html();
    });
});