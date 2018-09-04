var app = angular.module("myApp", []);
var $scope;

app.controller("myCtrl", function($scope) {
    window.$scope =  $scope;
    $scope.status = getUrlParameter('status') || 'all';
    $scope.search = getUrlParameter('search') || '';
    $scope.searchFunc = window.queryFunc;

    $scope.initialTab = getUrlParameter('swiper_tab') || 0;

    $scope.tabs = [
        {name: '待处理工单', id:'todo-container', selected: $scope.initialTab == 0, url: '/oa/workorder/todo/', type:'oa_detail'},
        {name: '关注工单', id:'watch-container', selected: $scope.initialTab == 1, url: '/oa/core/watchlist/oa/', type: 'oa_detail'},
        {name: '派出工单', id:'mine-container', selected: $scope.initialTab == 2, url: '/oa/workorder/mine/', type: 'market_detail'},
    ];
    
    $scope.currentTab = $scope.tabs[$scope.initialTab];

    $scope.choseStatus = function() {
        propose('选择状态', $scope.status, [{k:'所有', v:'all'}, {k:'已派单'}, {k:'已审核'}, {k:'阶段回单'}, {k:'已回单'}, {k:'已归档'}], function(val) {
            $scope.status = val;
            queryFunc();
        });
    }

    $scope.goToEdit = function(order) {
        go('oa_order.html?orderid={{order.workOrderId}}');
    }

    $scope.orderClick = function(order){
        if(order.orderType == "1"){
            go("oa_detail.html?workorderid=" + order.workOrderId);
        }else if(order.orderType == "2"){
            go("market_detail.html?workorderid=" + order.workOrderId);
        }else if(order.orderType == "3"){
            go("car_detail.html?r=1&orderid=" + order.workOrderId);
        }
    }

    $scope.checkInOut = function(order){
        $.post(makeAjaxUrl("/oa/workorder/receiveorder/oa/" + order.workOrderId + "/" + order.flowInfoId + '/' + (order.checkOut ? 0 : 1)), {}, function () {
            $.toptip('提交成功', 'success');
            queryFunc();
        }, "json");
    }

    $scope.watchUnwatch = function(order) {
        var url = "/oa/core/" + (order.watched ? 'un' : '') + "watch/" + order.workOrderId;
        $.post(makeAjaxUrl(url), {}, function () {
            $.toptip('提交成功', 'success');
            queryFunc();
        }, "json");
    };
});

$(document).on("click", '.swiper-title .swiper-slide', function () {
    $(this).parents('.swiper-title').data('swiper').slideTo($(this).index(), 500, false);
    updateUrl({'swiper_tab': $(this).index()});
    slideOk($(this).parents('.swiper-title').parent(), $(this).index());
});

function slideOk($parent, index, keepData){
    window.scrollTo(0,0);
    $scope.currentTab.selected = false;
    $scope.tabs[index].selected = true;
    $scope.currentTab = $scope.tabs[index];
    if(!keepData) {
        $scope.search = '';
    }
    $scope.$digest();
    queryFunc();
}

$(function(){
    FastClick.attach(document.body);
    $('.swiper-content').each(function(idx, ele) {
        var swiper = new Swiper(ele, {
            direction: 'horizontal',
            width: getDocument().body.clientWidth,
            height: getDocument().body.clientHeight,
            loop: false,
            onSlideChangeEnd: function (swiper) {
                slideOk($(swiper.container).parent(), swiper.activeIndex);
            }
        });
        var $parent = $(ele).parent();
        $parent.find('.swiper-title').data('swiper', swiper);
        slideOk($parent, $scope.initialTab, true);
    }); 
    $('.swiper-title').data('swiper').slideTo($scope.initialTab, 500, false);
});

function queryFunc() {
    updateUrl({status: $scope.status, search: $scope.search});
    var url = $scope.currentTab.url + $scope.status + "?size=300&current=1&keyword=" + ($scope.search || '');
    $.getJSON(makeAjaxUrl(url), function (data) {
        $scope.currentTab.orders = data.records;
        $scope.$digest();
    });
}
