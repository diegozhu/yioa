$(function () {
    var tel = getUrlParameter("tel");
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

    var initEvent = function () {
        //获取验证码
        $(".btn-code").click(function () {

            var num = $(".input-tel").val();
            $.getJSON(makeAjaxUrl("/oa/sms/send/" + num + "/" + num), function (data, textStatus, jqXHR) {
                $.toptip('发送成功，请输入收到的验证码进行验证，两分钟有效', 'success', function () {
                    tVal = setTimeout(function () {
                        showTime(120);
                    }, 1000);
                });
            });
        });
        //确定
        $("#btn-submit").click(function(){
            var inputTel = $(".input-tel").val();
            var repeatPhone = $(".repeat-phone").val();
            var btnCode = $(".btn-code").val();
            if(inputTel == "" || repeatPhone ==""){
                alert("输入的手机号码不能为空");
                return;
            }else if(inputTel != repeatPhone){
                alert("两次输入的手机号码不一致");
                return;
            }else if(btnCode == ""){
                alert("请输入验证码");
            }else{
                $.getJSON(makeAjaxUrl("/oa/sms/changePhone/" + btnCode + "/" + tel + "/" + inputTel), function (data, textStatus, jqXHR) {
                    if (data == "1") {
                        //跳转到欢迎页面
                        $.toast("手机号码修改成功", function () {
                            window.location.href = "home.html";
                        });
                    }
                });
            }
            // var username = $(".name").val();
            // var no = $(".no").val();
            // var companyId = $("#branchOffice").val();
            // var officeId = $("#department").val();
            // $.getJSON(makeAjaxUrl("/sys/bindAccount/" + username + "/" + no + "/" +  companyId + "/" + officeId ), function (data, textStatus, jqXHR) {
            //     window.location.href = "me.html"
            //     // if(data.name != null){
            //     //
            //     // }
            //     // var staffObj = {"data": data};
            //     // alert(staffObj);
            // });
            // alert("姓名是" + username + "手机号码是" + no + "所属公司" + companyId + "部门" + officeId);
        });
    }
    initEvent();
});