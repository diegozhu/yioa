//load todolist
$(function () {
    var g_co = "";

    var urlMap = {"diver": "/car/diver/list/", "car": "", "passenger": "", "feedback": ""};

    $("#planLeaveDate").calendar();
    $("#planBackDate").calendar();
    $("#marketAreaName").val(UrlParam.district);
    $("#destination").val(UrlParam.name);

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

        $(".passenger-a").click(function () {
            staffPopup("passenger");
        });
        $(".driver-a").click(function () {
            staffPopup("driver");
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
        if(!orderId) {
            return ;
        }
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



    var initSearch = function (searchInput, tpl, container) {

        var timeOut = "";
        searchInput.keyup(function () {
            window.clearTimeout(timeOut);

            timeOut = window.setTimeout(function () {
                var searchKey = searchInput.val();
                var exStr = $("#" + g_co + "Name").val();
                exStr = !exStr ? "xx" : exStr;

                if (jQuery.trim(searchKey) == "") {
                    return;
                }
                $.getJSON(makeAjaxUrl("/sys/staff/q/" + searchKey + "/" + exStr + "/100"), function (data, textStatus, jqXHR) {
                    var staffObj = {"data": data};
                    container.mustache(tpl, staffObj, {method: 'html'});
                });
            }, 500);

        });
    };
    (function () {
        initSearch($("#searchInput"), 'staffTpl', $("#staffContainer"));
    })();

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
        var tQueryKey = g_co == "feedback" ? "/sys/staff" : "/car/" + g_co;
        var exStr = $("#" + g_co + "Name").val();
        exStr = !exStr ? "xx" : exStr;

        $.getJSON(makeAjaxUrl(tQueryKey + "/list/" + exStr + "?current=1&size=20"), function (data, textStatus, jqXHR) {
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



    var getShowName = function (tagsinput) {
        var str = $.map(tagsinput.tagsinput('items'), function (ele, index) {
            return ele["text"];
        }).join(",");
        return str;
    }


    var validFun = function () {
        var keyArr = ["passengerName","driverName","destination","marketAreaName","marketTargetName","planLeaveDate","planBackDate"
            ,"carSendOrderContent"];
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
        var passengerName = getShowName($("#passengerName"));
        var passenger =  $("#passengerName").val();
        var co_organiser_name = getShowName($("#driverName"));
        var co_organiser =  $("#driverName").val();
        var destination = $("#destination").val();
        var marketAreaName = $("#marketAreaName").val();
        var marketTargetName = $("#marketTargetName").val();
        var planLeaveDate = $("#planLeaveDate").val();
        var planBackDate = $("#planBackDate").val();
        var wo_order_content = $("#carSendOrderContent").val();
        var attachFile = getUpFiles();

        $.post(makeAjaxUrl("/t_market_order/"+actionUrl), {
            "workOrderId": $("#workOrderId").val(),
            "workOrderCode": $("#workOrderCode").val(),
            "passenger": passenger,
            "lat": (+getUrlParameter('lat')).toFixed(4),
            "lng": (+getUrlParameter('lng')).toFixed(4),
            "passenger_name": passengerName,
            "co_organiser": co_organiser,
            "co_organiser_name": co_organiser_name,
            "destination": destination,
            "market_area_ame": marketAreaName,
            "work_order_subject": destination + "营销",
            "market_targer_name": marketTargetName,
            "plan_leave_date": planLeaveDate,
            "plan_back_date": planBackDate,
            "wo_order_content": wo_order_content,
            "attachFile": attachFile
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

    $("#marketAreaName").click(function() {
        propose('选择区域', $("#marketAreaName").val(), CITY[UrlParam.city] || CITY["盐城市"], function(val) {
            $("#marketAreaName").val(val);
        });
    });
});