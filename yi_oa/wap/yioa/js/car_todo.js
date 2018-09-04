//load todolist
$(function () {

    var getQryUrl = function (container) {
        var tUrl = "/car/order/todo/";
        if (container.attr("id").indexOf("watch") > -1) {
            tUrl = "/oa/core/watchlist/car/";
        } else if (container.attr("id").indexOf("mine") > -1) {
            tUrl = "/car/order/mine/";
        }
        return tUrl;
    }

    window.queryFunc = function (status, isAppend, container) {
        var searchInput = $("#searchInput");
        var searchKey = searchInput.val();
        container = !container ? $(".swiper-content .swiper-slide-active") : container;
        container.html("");

        var tStatus = container.data("status");
        tStatus = !tStatus ? "all" : tStatus;
        status = !status ? tStatus : status;

        var method = !isAppend ? "html" : "append";
        var current = !isAppend ? 1 : (parseInt(container.data("current")) + 1);

        var pageParam = "?size=20&current=" + current + "&keyword=" + searchKey;
        var tUrl = getQryUrl(container);

        $.getJSON(makeAjaxUrl(tUrl + status + pageParam), function (data, textStatus, jqXHR) {
            var rePage = {"data": data.records};
            container.mustache("todoTpl", rePage, {method: method});
            //存储当前页码
            container.data("current", data["current"]);
            container.data("status", status);
        });
    }

    var initSearch = function (searchInput, tpl) {
        if (!tpl) {
            tpl = "todoTpl";
        }
        var timeOut = "";
        searchInput.keyup(function () {
            window.clearTimeout(timeOut);
            timeOut = window.setTimeout(queryFunc, 500);

        });
    };

    var initWatch = function () {
        $("#todo-container,#watch-container,#mine-container").on("click","a.yioa-unwatch ", function (event) {

            if (event.preventDefault) {
                event.preventDefault();
            }
            if (event.stopPropagation) {
                event.stopPropagation();
            }
            var order = $(this);
            watchOrderFun(order.attr("orderId"), "/oa/core/unwatch/{workOrderId}");
            order.removeClass("yioa-unwatch").addClass("yioa-watch").find("img").attr("src", "../images/watch.png");
            return false;
        });

        $("#todo-container,#watch-container,#mine-container").on("click","a.yioa-watch ",function () {

            if (event.preventDefault) {
                event.preventDefault();
            }
            if (event.stopPropagation) {
                event.stopPropagation();
            }
            var order = $(this);
            watchOrderFun(order.attr("orderId"), "/oa/core/watch/{workOrderId}");
            order.removeClass("yioa-watch").addClass("yioa-unwatch").find("img").attr("src", "../images/unwatch.png");
            return false;
        });
    };

    var initCheckOut = function () {
        $("#todo-container,#watch-container,#mine-container").on("click","a.yioa-checkin ", function (event) {
            if (event.preventDefault) {
                event.preventDefault();
            }
            if (event.stopPropagation) {
                event.stopPropagation();
            }
            var order = $(this);
            checkOutOrderFun("/oa/workorder/receiveorder/car/"+order.attr("orderId")+"/"+order.attr("flowInfoId")+"/0");
            order.removeClass("yioa-checkin").addClass("yioa-checkout").find("img").attr("src", "../images/checkout.png");
            return false;
        });

        $("#todo-container,#watch-container,#mine-container").on("click","a.yioa-checkout ",function () {
            if (event.preventDefault) {
                event.preventDefault();
            }
            if (event.stopPropagation) {
                event.stopPropagation();
            }
            var order = $(this);
            checkOutOrderFun("/oa/workorder/receiveorder/car/"+order.attr("orderId")+"/"+order.attr("flowInfoId")+"/1");
            order.removeClass("yioa-checkout").addClass("yioa-checkin").find("img").attr("src", "../images/checkin.png");
            return false;
        });
    };

    var initEdit = function () {
        $("#mine-container").on("click","a.yioa-editable ", function (event) {
            if (event.preventDefault) {
                event.preventDefault();
            }
            if (event.stopPropagation) {
                event.stopPropagation();
            }
            var orderId = $(this).attr("orderId");
            window.location.href = "car_order.html?orderid=" + orderId;
        });
    };


    var init = function () {
        $.Mustache.addFromDom('todoTpl');
        //初次加载 是替代
        queryFunc();
        queryFunc(false, false, $("#watch-container"));
        queryFunc(false, false, $("#mine-container"));

        initSearch($("#searchInput"));

        initWatch();
        initCheckOut();
        initEdit();
        $(".weui-tab .weui-navbar .weui-navbar__item").on("click", queryFunc);
        $("#searchCancel").on("click", function () {
            $("#searchInput").val("");
            queryFunc();
        });

        var actionsArr = [];
        var makeAction = function (textStr) {
            return {
                text: textStr,
                className: "color-primary",
                onClick: function () {
                    queryFunc(textStr == "处理中" ? "all" : textStr);
                }
            };
        }

        actionsArr.push(makeAction("处理中"));

        actionsArr.push(makeAction("已派单"));
        actionsArr.push(makeAction("已审核"));
        actionsArr.push(makeAction("已归档"));


        $("#show-actions").click(function () {
            $.actions({
                title: "选择状态",
                onClose: function () {
                    console.log("close");
                },
                actions: actionsArr
            });
        });
    }
    init();


    $("#todo-container").on("click", ".todo-item", function () {
        var orderid = $(this).find(".orderid-input").val();
        window.location.href = "car_detail.html?orderid=" + orderid;
    })

    $("#watch-container,#mine-container").on("click", ".todo-item", function () {
        var orderid = $(this).find(".orderid-input").val();
        window.location.href = "car_detail.html?r=1&orderid=" + orderid;
    })


})