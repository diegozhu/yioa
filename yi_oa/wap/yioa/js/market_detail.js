//load todolist
var statusActionMapping = {
    "开始": "开始",
    "待阶段回单": "已派单",
    "待回单": "已阶段回单",
    "待审批归档": "已回单",
    "已归档": "已归档" 
};

$(function () {
    var workOrderId = getUrlParameter("workorderid");

    $("#watchHeartIcon").on('click', function(){
        $.post(makeAjaxUrl("/oa/core/watch/" + workOrderId), function (data, textStatus, jqXHR) {
            $("#watchHeartIcon").hide();
            $("#unwatchHeartIcon").show();
        });
        return false;
    });
    $("#unwatchHeartIcon").on('click', function(){
        $.post(makeAjaxUrl("/oa/core/unwatch/" + workOrderId), function (data, textStatus, jqXHR) {
            $("#watchHeartIcon").show();
            $("#unwatchHeartIcon").hide();
        });
        return false;
    });
    
    var initFlow = function () {
        var workOrderId = getUrlParameter("workorderid");
        $.getJSON(makeAjaxUrl("/oa/workorder/flow/" + workOrderId), function (data, textStatus, jqXHR) {
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
            //如果流程结束了，追加一个归档，否则追加一个进行中
            $(".time-line-container").mustache("flowTpl", dataObj, {method: "html"});
            $.each(dataObj.data, function(index,obj){
                if(obj.longitude != null){
                    var map = new BMap.Map(obj.flowInfoId);
                    var point = new BMap.Point(118.791588, 32.094211);
                    map.centerAndZoom(point, 12);
                    var mk = new BMap.Marker(point);
                    map.addOverlay(mk);//标出所在地
                }
            });
            if (g_flowStage == "FLOW_DONE") {
                $(".time-line-container").prepend(g_tArr2.join(""));
            }
            $(".time-line-container").append(g_tArr.join(""));
            showFileFun();
        });
    }

    var initOrder = function () {
        var workOrderId = getUrlParameter("workorderid");
        $.ajax({
            async: false,
            dataType: "json",
            url: makeAjaxUrl("/t_market_order/detail/" + workOrderId),
            data: {},
            success: function (data, textStatus, jqXHR) {
                var oneDay = 24 * 60 * 60 * 1000;
                var leftDate = parseInt(((new Date(data.plan_leave_date)).getTime() - (new Date()).getTime()) / oneDay);
                if(leftDate > 5){
                    data.leftDate = '剩余' + leftDate + '天';
                    $('[name="leftDate"]').css('border', 'green 1px solid').css('color', 'green');
                }else if(leftDate > 0 && leftDate <= 5){
                    data.leftDate = '剩余' + leftDate + '天';
                    $('[name="leftDate"]').css('border', 'orange 1px solid').css('color', 'orange');
                }else if(leftDate == 0){
                    data.leftDate = '今天';
                    $('[name="leftDate"]').css('border', 'red 1px solid').css('color', 'red');
                }else{
                    data.leftDate = '已过' + ( 0 - leftDate) + '天';
                    $('[name="leftDate"]').css('border', 'red 1px solid').css('color', 'red');
                }
                if(data.status == '已归档'){
                    data.leftDate = '完成' + ( 0 - leftDate) + '天';
                    $('[name="leftDate"]').css('border', 'grey 1px solid').css('color', 'grey');
                }
                if(data.watched){
                    $("#watchHeartIcon").show();
                }else{
                    $("#unwatchHeartIcon").show();
                }
                for (var key in data) {
                    $('[name="'+key+'"]').html(data[key]);
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
    }

    /**
     * 根据工单id，查询可以支持的操作
     */
    var initOperation = function () {
        var workOrderId = getUrlParameter("workorderid");
        var opArr = getOperationByFlowStage(g_flowStage, workOrderId, "t_market_order");
        var actionsArr = [];

        //把回单的模板加上，这里可以用通用的函数，把标题和event传递进去，然后就可以回单了。
        //审核页面需要单独处理，可以考虑提取出去，或者简单起见，做car的时候再多拷贝一次
        $.each(opArr, function (i, ele) {
            actionsArr.push({
                text: ele["title"],
                className: "color-primary",
                onClick: function () {
                    if (ele["event"] == "watch" || ele["event"] == "unwatch") {
                        watchOrderFun(workOrderId, ele.restUrl);
                        return;
                    }else if(ele["event"] == "signIn"){
                        var workOrderId = getUrlParameter("workorderid");
                        window.location.href = "sign_in.html?workorderid=" + workOrderId+"&g_flowStage=" + g_flowStage;
                    }else{
                        var dataObj = {"data": ele};
                        //渲染模板显示
                        $(".handler-container").mustache("handlerTpl", dataObj, {method: "html"});
                        if(bindUpFile){
                            bindUpFile();
                        }
                        if ($(".yioa-textarea").length > 0) {
                            makeExpandingArea($(".yioa-textarea")[0]);
                        }
                        // AlloyLever.entry('#entry222');
                        $(".handler_popup").popup();
                    }



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
            var workOrderId = getUrlParameter("workorderid");


            restUrl = restUrl.replace('{workOrderId}', workOrderId).replace('{event}', event).replace('{orderType}', "market");

            var attachFile = getUpFiles();

            $.post(makeAjaxUrl(restUrl), {
                "workOrderId": workOrderId,
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


});