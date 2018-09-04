$(function(){
    var getQryUrl = function (container){
        var tUrl = "/t_market_order/todo/";
        return tUrl;
    }
    window.queryFunc = function(status, isAppend, container){
        container = !container ? $(".swiper-content .swiper-slide-active") : container;
        container.html("");
        var tStatus = container.data("status");
        tStatus = !tStatus ? "all" : tStatus;
        status = !status ? tStatus : status;
        var method = !isAppend ? "html" : "append";
        var current = !isAppend ? 1 : (parseInt(container.data("current")) + 1);

        var pageParam = "?size=20&current=" + current + "&keyword=" + "";
        var tUrl = getQryUrl(container);

        $.getJSON(makeAjaxUrl(tUrl + status + pageParam), function (data, textStatus, jqXHR) {
            var rePage = {"data": data.records};
            container.mustache("todoTpl", rePage, {method: method});
            //存储当前页码
            container.data("current", data["current"]);
            container.data("status", status);
        });
    }


    var init = function(){
        $.Mustache.addFromDom('todoTpl');
        queryFunc();
    }
    init();

    $("#todo-container").on("click", ".todo-item", function () {
        var workOrderId = $(this).find(".orderid-input").val();
        window.location.href = "market_detail.html?workorderid=" + workOrderId;
    })
})