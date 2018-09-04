$(function () {

    var listStaff = function (container, tpl) {
        container = !container ? "#addressContainer" : container;
        tpl = !tpl ? "addressTpl" : tpl;

        $.getJSON(makeAjaxUrl("/sys/staff/list/xx"), function (data, textStatus, jqXHR) {
            var staffObj = {"data": data.records};
            $(container).mustache(tpl, staffObj, {method: 'html'});
        });
    };

    var initSearch = function (searchInput, tpl, container) {

        var timeOut = "";
        searchInput.keyup(function () {
            window.clearTimeout(timeOut);
            timeOut = window.setTimeout(function () {
                var searchKey = searchInput.val();
                if (jQuery.trim(searchKey) == "") {
                    return;
                }
                $.getJSON(makeAjaxUrl("/sys/staff/q/" + searchKey + "/xx/100"), function (data, textStatus, jqXHR) {
                    var staffObj = {"data": data};
                    container.mustache(tpl, staffObj, {method: 'html'});
                });
            }, 500);

        });
    };

    var initOrder = function () {
        $.Mustache.addFromDom('addressTpl');
        listStaff();
        initSearch($("#searchInput"), 'addressTpl', $("#addressContainer"));
    };
    initOrder();

    $("#addressContainer").on("click", ".person_item", function () {
        window.location.href = "oa_detail.html?workorderid=" + workOrderId;
    })

    $("#searchCancel").on("click", function() {listStaff();});

});
