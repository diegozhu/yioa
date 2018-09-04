//load todolist
$(function () {
    var g_co = "";
    var currentUser = {};
    var initDate = function () {
        $("#milestoneDate").calendar();
        $("#reqCompleteDate").calendar();
        $.getJSON(makeAjaxUrl("/sys/staff/xx"), function (data, textStatus, jqXHR) {
            currentUser = data;
            console.log('currentUser', currentUser);
            $("#senderName").val(currentUser.name ||currentUser.loginName);
        });
    }
    initDate();

    var initEvent = function () {

        $.Mustache.addFromDom('organiserTpl');
        // $.Mustache.addFromDom('coOrganiserTpl');
        var removeTag = function (tag) {

        };

        $("#organiserName,#coOrganiserName,#chooseName").tagsinput({
            itemValue: 'id',
            itemText: 'text'
        });
        $(".bootstrap-tagsinput input").attr("readOnly", "true").css("width", "1px");

        $(".bootstrap-tagsinput").click(function (event) {
            event.preventDefault();
            event.stopPropagation();
            return false;
        });

        $(".organiser-a").click(function () {
            $("#organiserContainer").html("");
            g_co = "organiser";
            $("#organiserChoose").popup();
        });
        $(".coOrganiser-a").click(function () {
            $("#organiserContainer").html("");
            g_co = "coOrganiser";
            $("#organiserChoose").popup();
        });

        // var $span = $('pre.yioa-pre span');
        // bindTextChange('.yioa-textarea', function () {
        //     $span.html($(this).val());
        // });


        //取消时候，重新搜索
        $("#searchCancel").on("click", function () {
            listStaff("#organiserContainer", "organiserTpl", "#" + g_co + "Name");
        });

    }
    initEvent();
    // var initOrder = function () {
    //     var orderId = getUrlParameter("orderid");
    //     $.ajax({
    //         async: false,
    //         dataType: "json",
    //         url: makeAjaxUrl("/oa/workorder/detail/" + orderId),
    //         data: {},
    //         success: function (data, textStatus, jqXHR) {
    //             for (var key in data) {
    //                 $("#" + key).val(data[key]);
    //                 if (key == "attachFile") {
    //                     appendAttachFile(data[key]);
    //                 }
    //             }
    //             $('#organiserName').tagsinput('removeAll');
    //             $('#coOrganiserName').tagsinput('removeAll');
    //             $('#organiserName').tagsinput('add', {id: data["organiser"], text: data["organiserName"]});
    //             var tArr = data["coOrganiser"].split(",");
    //             var tNameArr = data["coOrganiserName"].split(",");
    //             for(var i=0;i<tArr.length;i++){
    //                 $('#coOrganiserName').tagsinput('add', {id: tArr[i], text: tNameArr[i]});
    //             }

    //         },
    //         error: function (jqXHR, textStatus, errorThrown) {
    //             console.log(jqXHR);
    //         }
    //     });
    // };
    // initOrder();
    

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
        initSearch($("#searchInput"), 'organiserTpl', $("#organiserContainer"));
    })();

    var addTag = function (tagInput, tagValue) {
        tagInput.addTag(tagValue);
    }

    var removeTag = function (tagInput, tagValue) {
        tagInput.removeTag(tagValue);
    }


    $("#organiserContainer").on("click", ".weui-check__label", function () {
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

    var hasTagStr = function (input, str) {
        var arr = input.val().split(",");
        var bool = false;
        $.each(arr, function (index, ele) {
            bool = str == ele ? true : false;
            if (bool) {
                return false;
            }
        })
        return bool;
    }

    var removeTagStr = function (input, str) {
        var arr = input.val().split(",");
        var bool = false;
        var tArr = $.grep(arr, function (ele, index) {
            return ele != str;
        });
        input.val(tArr.join(","));
    }


    var listStaff = function (container, tpl, tagId) {
        var exStr = $(tagId).val();
        exStr = !exStr ? "xx" : exStr;
        $.getJSON(makeAjaxUrl("/sys/staff/list/" + exStr), function (data, textStatus, jqXHR) {
            var staffObj = {"data": data.records};
            $(container).mustache(tpl, staffObj, {method: 'html'});
        });
    }


    var handlerList = function (container) {
        var tInput = $("#" + g_co + "Name");
        var items = $("#chooseName").tagsinput('items');
        $.each(items, function (inedex, ele) {
            tInput.tagsinput('add', ele);
        })
    }

    $(document).on("open", ".organiser-popup", function (e) {

        $("#organiserContainer").html("");
        $("#searchInput").val("");

        $('#chooseName').tagsinput('removeAll');
        window.setTimeout(function () {
            listStaff("#organiserContainer", "organiserTpl", "#" + g_co + "Name");
        }, 200);

    });
    // .on("close", ".organiser-popup", function () {
    //
    //     handlerList("#organiserContainer");
    // });

    $(".yioa-popup-ok").on("click", function () {
        handlerList("#organiserContainer");
    });


    var validFun = function () {
        var keyArr = ["organiserName", "reqCompleteDate", "workOrderContent",
            "workOrderSubject", "workOrderType","emergencyLevel"];
        for (var i = 0; i < keyArr.length; i++) {
            var val = $("#" + keyArr[i]).val();
            if (!val || $.trim(val) == "") {
                $.toptip('该值不能为空，请输入', 'warning');
                var pDiv = $("#" + keyArr[i]).parents("div.weui-cell,a.weui-cell");
                pDiv.addClass("yioa-valid");
                setTimeout(function () {
                    pDiv.removeClass("yioa-valid");
                }, 5000);
                return false;
            }
        }
        return true;
    }

    var submitFun = function (actionUrl) {
        if (!validFun()) {
            return;
        }

        var coOrganiserNameTxt = $.map($("#coOrganiserName").tagsinput('items'), function (ele, index) {
            return ele["text"];
        }).join(",");

        var organiserNameTxt = $.map($("#organiserName").tagsinput('items'), function (ele, index) {
            return ele["text"];
        }).join(",");

        var attachFile = getUpFiles();

        $.post(makeAjaxUrl("/oa/workorder/"+actionUrl), {
            "workOrderId": $("#workOrderId").val(),
            "workOrderCode": $("#workOrderCode").val(),
            "coOrganiser": $("#coOrganiserName").val(),
            "coOrganiserName": coOrganiserNameTxt,
            "organiser": $("#organiserName").val(),
            "organiserName": organiserNameTxt,
            "attachFile": attachFile,
            // "milestoneDate": JSON.stringify(new Date($("#milestoneDate").val())),
            // "reqCompleteDate": JSON.stringify(new Date($("#reqCompleteDate").val())),
            "milestoneDate": $("#milestoneDate").val(),
            "reqCompleteDate": $("#reqCompleteDate").val(),
            "workOrderContent": $("#workOrderContent").val(),
            "workOrderSubject": $("#workOrderSubject").val(),
            "workOrderType": $("#workOrderType").val(),
            "emergencyLevel":$("#emergencyLevel").val()
        }, function (data) {
            $.toast("提交成功", function () {
                window.location.href = g_home_url;
            });
        }, "json");
    };

    $("#btn-submit").click(function () {
        submitFun("submit");
    });

    $("#btn-save").click(function () {
        submitFun("saveOrder");
    });

});