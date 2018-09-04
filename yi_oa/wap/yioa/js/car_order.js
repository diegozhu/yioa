//load todolist
var g_co = "";

$(function () {

    var urlMap = {"diver": "/car/diver/list/", "car": "", "passenger": "", "feedback": ""};

    var initDate = function () {
        $("#planLeaveDate").calendar();
        $("#planBackDate").calendar();
    }
    initDate();

    var initEvent = function () {

        $(".tagsinput").click(function (event) {
            event.preventDefault();
            event.stopPropagation();
            return false;
        });
        $.Mustache.addFromDom('staffTpl');
        $.Mustache.addFromDom('carTpl');

        var removeTag = function (tag) {
        };
        $("#driverName,#carNo,#passengerName,#feedbackName,#chooseName").tagsinput({
            itemValue: 'id',
            itemText: 'text'
        });
        $(".bootstrap-tagsinput input").attr("readOnly","true").css("width","1px");

        $(".bootstrap-tagsinput").click(function (event) {
            event.preventDefault();
            event.stopPropagation();
            return false;
        });

        var staffPopup = function (tag) {
            g_co = tag;
            $("#staffContainer").html("");
            $("#staffPopup").popup();
        }

        $(".driver-a").click(function () {
            staffPopup("driver");
        });
        $(".passenger-a").click(function () {
            staffPopup("passenger");
        });
        $(".feedback-a").click(function () {
            staffPopup("feedback");
        });


        $("#carContainer").on("click", "a.car-cell", function () {
            if($(this).find("div img").hasClass("car_occupied")){
                $.toptip('本车已经被占用', 'warning');
                return;
            }
            $("#carContainer a.car-cell").removeClass("car-selected");
            $(this).addClass("car-selected");
        });


        $("#staffContainer").on("click", ".weui-check__label", function () {
            var selInput = $(this).find(".weui-check");
            var bool = selInput[0].checked + "";
            var selId = selInput.attr("id");
            var selName = selInput.attr("name");

            var tagInputName = $("#chooseName");
            if (bool == "true") {
                //选中，add
                tagInputName.tagsinput('add', {id: selId, text: selName});
            } else {
                //取消选中，remove
                tagInputName.tagsinput('remove', {id: selId, text: selName});
            }
        });


        $(".car-a").click(function () {
            g_co = "car";
            var container = $("#carContainer");
            window.setTimeout(function () {
                $.getJSON(makeAjaxUrl("/car/car/list?current=1&size=20"), function (data, textStatus, jqXHR) {
                    var obj = {"data": data.records};
                    container.mustache("carTpl", obj, {method: 'html'});
                    $("#carPopup").popup();
                });
            }, 200);
        });

        // var $span = $('pre.yioa-pre span');
        // bindTextChange('.yioa-textarea', function() {
        //     $span.html($(this).val());
        // });
        // makeExpandingArea($(".yioa-textarea")[0]);

        $("#searchCancel").on("click",function () {
            listStaff("#staffContainer", "staffTpl");
        });

    }
    initEvent();

    var initOrder = function () {
        var orderId = getUrlParameter("orderid");
        if(!orderId) 
            return ;
        $.ajax({
            async: false,
            dataType: "json",
            url: makeAjaxUrl("/car/detail/" + orderId),
            data: {},
            success: function (data, textStatus, jqXHR) {
                for (var key in data) {
                    $("#" + key).val(data[key]);
                    if (key == "attachFile") {
                        appendAttachFile(data[key]);
                    }
                }
                $('#driverName').tagsinput('removeAll');
                $('#carNo').tagsinput('removeAll');
                $('#passengerName').tagsinput('removeAll');
                $('#feedbackName').tagsinput('removeAll');
                $('#driverName').tagsinput('add', {id: data["driverId"], text: data["driverName"]});
                $('#carNo').tagsinput('add', {id: data["carId"], text: data["carNo"]});
                $('#feedbackName').tagsinput('add', {id: data["feedbackId"], text: data["feedbackName"]});
                var tArr = data["passengerId"].split(",");
                var tNameArr = data["passengerName"].split(",");
                for(var i=0;i<tArr.length;i++){
                    $('#passengerName').tagsinput('add', {id: tArr[i], text: tNameArr[i]});
                }

            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log(jqXHR);
            }
        });
    };
    initOrder();

    var addTag = function (tagInput, tagValue) {
        tagInput.addTag(tagValue);
    }

    var removeTag = function (tagInput, tagValue) {
        tagInput.removeTag(tagValue);
    }


    $("#staffContainer").on("click", ".btn-ok", function () {
        console.log("organiserSubList:................");
    });


    var listStaff = function (container, tpl) {
        var exStr = $("#" + g_co + "Name").val();
        exStr = !exStr ? "xx" : exStr;

        $.getJSON(makeAjaxUrl("/sys/staff/list/" + exStr + "?current=1&size=20"), function (data, textStatus, jqXHR) {
            var staffObj = {"data": data.records};
            $(container).mustache(tpl, staffObj, {method: 'html'});
        });
    }

    var handlerList = function (container, tagInput) {
        var tInput = $("#" + g_co + "Name");
        var items = $("#chooseName").tagsinput('items');
        $.each(items, function (inedex, ele) {
            tInput.tagsinput('add', ele);
        })
    }

    $(document).on("open", "#staffPopup", function (e) {
        $("#staffContainer").html("");
        $('#chooseName').tagsinput('removeAll');
        window.setTimeout(function () {
            listStaff("#staffContainer", "staffTpl");
        }, 200);
    });
    // .on("close", "#staffPopup", function () {
    //     handlerList("#staffContainer");
    // });

    $(".yioa-popup-ok").on("click",function () {
        handlerList("#staffContainer");
    });

    $("#carPopup").on("click", "a.btn-ok", function () {

    });

    $(document).on("close", "#carPopup", function () {
        var selCar = $("#carContainer a.car-cell.car-selected");
        if (selCar.length > 0) {
            $('#carNo').tagsinput('removeAll');
            $("#carNo").tagsinput("add", {"id": selCar.attr("carId"), "text": selCar.attr("carNo")});
        }
    });


    var getShowName = function (tagsinput) {
        var str = $.map(tagsinput.tagsinput('items'), function (ele, index) {
            return ele["text"];
        }).join(",");
        return str;
    }


    var validFun = function () {
        var keyArr = ["driverName","carNo","passengerName","feedbackName","carSendOrderContent","carSendOrderSubject","destination"
        ,"planLeaveDate","planBackDate"];
        for(var i=0;i<keyArr.length;i++){
            var val = $("#"+keyArr[i]).val();
            if(!val || $.trim(val) == ""){
                $.toptip('该值不能为空，请输入', 'warning');
                var pDiv = $("#"+keyArr[i]).parents("div.weui-cell,a.weui-cell");
                pDiv.addClass("yioa-valid");
                setTimeout(function () {
                    pDiv.removeClass("yioa-valid");
                },5000);
                return false;
            }
        }
        return true;
    }

    var submitFun = function (actionUrl) {
        if(!validFun()){
            return;
        }
        var driverName = getShowName($("#driverName"));
        var carNo = getShowName($("#carNo"));
        var passengerName = getShowName($("#passengerName"));
        var feedbackName = getShowName($("#feedbackName"));
        var attachFile = getUpFiles();
        $.post(makeAjaxUrl("/car/order/"+actionUrl), {
            "carSendOrderId": $("#carSendOrderId").val(),
            "carSendOrderCode": $("#carSendOrderCode").val(),


            "carSendOrderContent": $("#carSendOrderContent").val(),
            "carSendOrderSubject": $("#carSendOrderSubject").val(),
            "driverName": driverName,
            "driverId": $("#driverName").val(),
            "carNo": carNo,
            "attachFile": attachFile,
            "carId": $("#carNo").val(),
            "passengerName": passengerName,
            "passengerId": $("#passengerName").val(),
            "feedbackName": feedbackName,
            "feedbackId": $("#feedbackName").val(),
            "destination": $("#destination").val(),
            "planLeaveDate": $("#planLeaveDate").val(),
            "planBackDate": $("#planBackDate").val()
        }, function (data) {
            if (data == "1") {
                $.toast("提交成功", function () {
                    window.location.href = g_home_url;
                });
            }
        }, "json");
    };

    $("#btn-save").click(function () {
        submitFun("saveOrder");
    });

    $("#btn-submit").click(function () {
        submitFun("submit");
    });

});


$(function () {
    var timeOut = "";
    $(".weui-search-bar__input").keyup(function (event) {
        window.clearTimeout(timeOut);
        var searchKey = event.target.value;
        timeOut = window.setTimeout(function () {
            var exStr = $("#" + g_co + "Name").val();
            exStr = !exStr ? "xx" : exStr;
            if (jQuery.trim(searchKey) == "") {
                return;
            }
            $.getJSON(makeAjaxUrl("/sys/staff/q/" + searchKey + "/" + exStr + "/100"), function (data, textStatus, jqXHR) {
                var staffObj = {"data": data};
                $("#staffContainer").mustache('staffTpl', staffObj, {method: 'html'});
            });
        }, 500);
    });
});