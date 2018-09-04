//load todolist
$(function () {

    $.getJSON(makeAjaxUrl("/sys/staff/xx"), function (data) {
        $.toast('您已绑定账号');
        setTimeout(function() {
            location.href = 'home.html';
        }, 500);
    });

    var tVal;

    function showTime(count) {
        $(".btn-count-down").html(count);
        if (count == 0) {
            window.clearTimeout(tVal);
        } else {
            count -= 1;
            setTimeout(function () {
                showTime(count);
            }, 1000);
        }

    }

    var initEvent = function () {
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

        $("#btn-submit").click(function () {
            var num = $(".input-tel").val();
            var code = $(".input-code").val();
            $.getJSON(makeAjaxUrl("/oa/sms/verify/" + code + "/" + num), function (data, textStatus, jqXHR) {
                if (data == "1") {
                    //跳转到欢迎页面
                    var bindredurl = getUrlParameter("bindredurl");
                    if (!bindredurl) {
                        bindredurl = "home.html"
                    }
                    $.toast("绑定成功，欢迎来到起而行之", function () {
                        window.location.href = bindredurl;
                    });
                }
            });
        });
    }
    initEvent();


});