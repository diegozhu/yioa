//load todolist
var statusActionMapping = {
    "开始": "开始",
    "待阶段回单": "已派单",
    "待回单": "已回单",
    "待审批归档": "已回单",
    "待阶段回单":"已阶段回单",
    "已归档": "已归档" 
};

$(function () {
    var tArr = [];
    tArr.push('<div class="time-line"><ul><li><div class="time-line-circle"></div><div class="time-line-info">');
    tArr.push('<p class="time-line-title">已归档</p>');
    tArr.push('</div></li></ul></div>');


    var initFlow = function () {
        var orderid = getUrlParameter("orderid");
        $.getJSON(makeAjaxUrl("/oa/workorder/flow/" + orderid), function (data, textStatus, jqXHR) {
            //-----------------------begin
            var tmp = [];
            for(var i = 0; i < data.length; i++) {
                if(!data[i].completeDate) {
                    tmp.push(data[i]);
                    data.splice(i, 1);
                }
            }
            for(i in tmp) {
                data.unshift(tmp[i]);
            }
            //-----------------------------end 為了排序

            $.each(data,function (index,flowInfo) {
                flowInfo.flowStageName = statusActionMapping[flowInfo.flowStageName] || flowInfo.flowStageName;
                if(flowInfo.attachFile){
                    var fileArr = flowInfo.attachFile.split(",");
                    var attatchFileArr = $.map(fileArr,function (ele, index) {
                        return {"src": ele};
                    })
                    flowInfo.attatchFileArr = attatchFileArr;
                }
            });

            var dataObj = {"data": data};
            $(".time-line-container").mustache("flowTpl", dataObj, {method: "html"});

            if (g_flowStage == "FLOW_DONE") {
                if (data.length < 3) {
                    $(".time-line-container").prepend(g_tArr2.join("").replace("已归档", "已归档(审核不通过)"));
                } else {
                    $(".time-line-container").prepend(g_tArr2.join(""));
                }
            }
            $(".time-line-container").append(g_tArr.join(""));
            showFileFun();
        });
    }

    var initOrder = function () {
        var orderid = getUrlParameter("orderid");
        $.ajax({
            async: false,
            dataType: "json",
            url: makeAjaxUrl("/car/detail/" + orderid),
            data: {},
            success: function (data, textStatus, jqXHR) {
                for (var key in data) {
                    $("#" + key).html(data[key]);
                    if (key == "flowStage") {
                        g_flowStage = data[key];
                    } else if (key == "attachFile") {
                        showAttachFile(data[key]);
                    }
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {

            }
        });

        //回填审核人
        $.getJSON(makeAjaxUrl("/oa/workorder/flowinfo/" + orderid + "/CAR_FLOW_AUDIT"), function (data, textStatus, jqXHR) {
            var flowObj = data.length > 0 ? data[0] : {};
            if (flowObj["completeUserName"]) {
                $("#auditer").html(flowObj["completeUserName"]);
            }
        });

    }


    /**
     * 根据工单id，查询可以支持的操作
     */
    var initOperation = function () {
        var orderid = getUrlParameter("orderid");
        var opArr = getOperationByFlowStage(g_flowStage, orderid, "car");
        var actionsArr = [];

        //把回单的模板加上，这里可以用通用的函数，把标题和event传递进去，然后就可以回单了。
        //审核页面需要单独处理，可以考虑提取出去，或者简单起见，做car的时候再多拷贝一次
        $.each(opArr, function (i, ele) {
            actionsArr.push({
                text: ele["title"],
                className: "color-primary",
                onClick: function () {
                    if (ele["event"] == "watch" || ele["event"] == "unwatch") {
                        watchOrderFun(orderid, ele.restUrl);
                        return;
                    }

                    var dataObj = {"data": ele};
                    //渲染模板显示
                    $(".handler-container").mustache("handlerTpl", dataObj, {method: "html"});
                    if (bindUpFile) {
                        bindUpFile();
                    }
                    if ($(".yioa-textarea").length > 0) {
                        makeExpandingArea($(".yioa-textarea")[0]);
                    }
                    $(".handler_popup").popup();
                    // var $span = $('pre.yioa-pre span');
                    // bindTextChange('.yioa-textarea', function() {
                    //     $span.html($(this).val());
                    // });
                }
            });
        });

        $("#show-actions").click(function () {
            $.actions({
                title: "处理",
                onClose: function () {
                },
                actions: actionsArr
            });
        });

        $(".handler_popup").on("click", "#btn-cancel", function () {
            $.closePopup();
        });

        $(".handler_popup").on("change", "#auditSel", function () {
            $("#btn-submit").attr("subEvent", $(this).val());
        });


        $(".handler_popup").on("click", "#btn-submit", function () {
            var restUrl = $("#restUrl").val();
            var event = $(this).attr("subEvent");
            var orderid = getUrlParameter("orderid");
            restUrl = restUrl.replace('{workOrderId}', orderid).replace('{event}', event).replace('{orderType}', "car");
            var attachFile = getUpFiles();
            $.post(makeAjaxUrl(restUrl), {
                "workOrderId": orderid,
                "attachFile": attachFile,
                "flowStage": g_flowStage,
                "notes": $("#notes").val()
            }, function (data) {
                $.toast("提交成功", function () {
                    $.closePopup();
                    window.location.href = g_home_url;
                });
            }, "json");
        });
    }


    var init = function () {
        $.Mustache.addFromDom('flowTpl');
        //回单的模板
        $.Mustache.load('common_handler.html');

        initOrder();
        initFlow();
        initOperation();
    }
    init();


})