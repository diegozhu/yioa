$(function () {
    $("#btn-login").click(function(){
       var username = $(".login_name").val();
       var password = $(".pass_word").val();
        $.getJSON(makeAjaxUrl("/dologin/" + username + "/" + password), function (data, textStatus, jqXHR) {
            if (data.login_flag == "0") {
                $.toast("登录成功，欢迎来到起而行之", function () {
                    if(data.roleList[0] != null){
                        window.location.href = "me.html"
                    }else{
                        window.location.href = "bindAccount.html"
                    }
                });
            }else{
                $.toast("账号或密码错误，请重新输入", function () {

                });
            }
        });
    });
});