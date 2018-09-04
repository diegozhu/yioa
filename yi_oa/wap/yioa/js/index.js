$(function () {
    $.getJSON(makeAjaxUrl("/oa/workorder/todo/all?size=120&current=1"), function (data) {
        $("#orderCnt").html(data.total)
    });

    $.getJSON(makeAjaxUrl("/car/order/todo/all?size=120&current=1"), function (data) {
        $("#carOrderCnt").html(data.total)
    });

    $("#orderAction").click(function () {
        propose('派单类型', '', [
            {k:'我的草稿', v:'order_save.html'},
            {k:'新事务单', v:'oa_order.html'},
            {k:'新营销单', v:'select_location.html'}
        ], function(res) {
            go(res);
        });
    });

    $("#carOrderAction").click(function () {
        go('car_order.html');
        //propose('选则', '', [{k:'我的草稿', v:'order_save.html?orderType=car'}, {k:'新发起', v:'car_order.html'}], go);
    });
});