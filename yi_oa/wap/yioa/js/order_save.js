$(function () {
    $.Mustache.addFromDom('orderTpl');
    $.Mustache.addFromDom('carOrderTpl');

    //初次加载 是替代
    var orderType = getUrlParameter("orderType");
    queryFunc(orderType);
});

function queryFunc(orderType) {
    var urlStr = orderType == "car" ?  "/car/order/listCarOrderSave" : "/oa/workorder/listOrderSave";
    var tpl = orderType == "car" ?  "carOrderTpl" : "orderTpl";
    var container = $("#save-container");
    $.getJSON(makeAjaxUrl(urlStr), function (data, textStatus, jqXHR) {
        var rePage = {"data": data.records};
        container.mustache(tpl, rePage, {method: "html"});
        //存储当前页码
        container.data("current", data["current"]);
    });
}

$(document).on("click", ".todo-item", function () {
    var orderId = $(this).find(".orderid-input").val();
    var orderType = getUrlParameter("orderType");
    var tUrl = orderType == "car" ?  "car_order" : "oa_order";
    window.location.href = tUrl+".html?orderid=" + orderId;
})