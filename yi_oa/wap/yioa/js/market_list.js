var map, allPoints = [], allMarkers = [], marketOrderByLocation = {}, heatmapOverlay, myGeo, markerClusterer, myIcon, app = angular.module("myApp", []), $scope;
var locationCounts = {};
var city = UrlParam.city || "盐城市";

app.controller("myCtrl", function($scope) {
    window.$scope = $scope;
    $scope.data = [];
    $scope.marketAreaName = UrlParam.marketAreaName || CITY[city][0];
    $scope.tips = '查询中...';

    $scope.selectRestirct = function() {
        propose('选择区域', $scope.marketAreaName, CITY[UrlParam.city] || CITY["盐城市"], function(val) {
            $scope.marketAreaName = val;
            $scope.tips = '查询中...';
            $scope.$digest();
            updateUrl({marketAreaName: val});
            setTimeout(initData, 200);
            setTimeout(function() {
                myGeo.getPoint($scope.marketAreaName, function(point) {
                    map.panTo(point);
                }, city);
            }, 200);
        });
    };
});

$(function () {
    $("#planLeaveDate").val(getUrlParameter("planLeaveDate") || new Date().offsetDay(-3).format('YYYY-MM-DD')).on('change', function(){setTimeout(initData, 200)}).calendar();
    $("#planBackDate").val(getUrlParameter("planBackDate")|| new Date().offsetDay(7).format('YYYY-MM-DD')).on('change', function(){setTimeout(initData, 200)}).calendar();
    
    FastClick.attach(document.body);
    myGeo = new BMap.Geocoder();
    myGeo.getPoint($scope.marketAreaName, initMap, city);
});

$(document).on("click", ".todo-item", function () {
    var orderid = $(this).find('.orderid-input').val();
    window.location.href = "market_detail.html?workorderid=" + orderid;
});

function initMap(point){
    map = new BMap.Map('allmap');
    map.centerAndZoom(point, +(getUrlParameter('zoom') || 13));
    map.enableScrollWheelZoom();
    myIcon = new BMap.Icon("../images/locate_small.png", new BMap.Size(20, 28));
    map.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_BOTTOM_RIGHT, type: BMAP_NAVIGATION_CONTROL_SMALL}));  
    map.addEventListener("zoomend", mapZoomed);
    map.addEventListener("moveend", mapMoved);
    if(getUrlParameter('lat') && getUrlParameter('lng')) {
        map.panTo(new BMap.Point(+getUrlParameter('lng'), +getUrlParameter('lat')));
    }
    initData();
}

var oneDay = 24 * 60 * 60 * 1000;
function leftDate(item) {
    var leftDate = parseInt(((new Date(item.plan_leave_date)).getTime() - (new Date()).getTime()) / oneDay);
    if(leftDate > 5){
        item.leftDate = '剩余' + leftDate + '天';
        item.leftDateCss = 'green';
    }else if(leftDate <= 5 && leftDate > 0){
        item.leftDate = '剩余' + leftDate + '天';
        item.leftDateCss = 'late';
    }else if(leftDate == 0){
        item.leftDate = '今天';
        item.leftDateCss = 'late';
    }else{
        item.leftDate = '已过' + ( 0 - leftDate) + '天';
        item.leftDateCss = 'overflow';
    }
}

function mapMoved(r) {
    var center = map.getCenter();
    console.log('mapMoved', center);
    updateUrl({lat: center.lat, lng: center.lng});
}

function mapZoomed() {
    var zoom = map.getZoom();
    console.log('zoom', zoom);
    updateUrl({zoom: zoom});
    if(zoom >= 14) {
        heatmapOverlay.hide();
        for(var i in allMarkers) {
            allMarkers[i].show();
        }
    } else {
        heatmapOverlay.show();
        for(var j in allMarkers) {
            allMarkers[j].hide();
        }
    }
}

function initData(){
    map.clearOverlays();
    allMarkers.length = 0;
    allPoints.length = 0;
    updateUrl({marketAreaName: $scope.marketAreaName, planLeaveDate:$("#planLeaveDate").val(), planBackDate: $("#planBackDate").val()});
    $.getJSON(makeAjaxUrl("/t_market_order/queryMarket/" + $scope.marketAreaName +"/"+ $("#planLeaveDate").val() +"/"+ $("#planBackDate").val()), function (data) {
        data.records = data.records || [];
        var step = 1;
        if(data.records.length <= 10) {
            step = 20;
        } else if (data.records.length > 10 && data.records.length <= 20) {
            step = 10;
        } else if (data.records.length > 20 && data.records.length < 50) {
            step = 5;
        }
        var item, i;
        console.log('step:', step);
        for(i in data.records){
            item = data.records[i];
            leftDate(item);
        }
        $scope.tips = data.total > 0 ? '共有 ' + data.total + ' 场营销活动' : '暂无营销活动';
        $scope.data = data.records;
        $scope.$digest();

        for(i in data.records){
            item = data.records[i];
            if(item.lng && item.lat) {
                var key = (Math.round((+item.lng * 100))/100).toFixed(2) + '/' + (Math.round((+item.lat * 100))/100).toFixed(2);
                var key2 = (Math.round((+item.lng * 1000))/1000).toFixed(3) + '/' + (Math.round((+item.lat * 1000))/1000).toFixed(3);
                marketOrderByLocation[key2] = marketOrderByLocation[key2] ? marketOrderByLocation[key2] : [];
                marketOrderByLocation[key2].push(item);
                locationCounts[key] = locationCounts[key] ? locationCounts[key] + step : step;
                var point = new BMap.Point(+item.lng, +item.lat);
                var label = new BMap.Label(item.work_order_subject || '未命名营销', {position : point, offset: new BMap.Size(10, -20)});
                var marker = new BMap.Marker(point, {icon: myIcon});
                marker.setLabel(label);
                marker.addEventListener("click", clickMarker);
                map.addOverlay(marker);
                allMarkers.push(marker);
            }
        }

        for(var j in locationCounts) {
            console.log(j + '/' + locationCounts[j]);
            allPoints.push({lng: j.split('/')[0], lat: j.split('/')[1], count: locationCounts[j]});
        }

        heatmapOverlay = new BMapLib.HeatmapOverlay({"radius": 20});
	    map.addOverlay(heatmapOverlay);
        heatmapOverlay.setDataSet({data:allPoints, max:100});
        mapZoomed();
        //markerClusterer = new BMapLib.MarkerClusterer(map, {markers: allPoints});
    });
}

function clickMarker(r) {
    var key = (Math.round((+r.target.point.lng * 1000))/1000).toFixed(3) + '/' + (Math.round((+r.target.point.lat * 1000))/1000).toFixed(3);
    var orders = marketOrderByLocation[key];
    var msg = [], order;
    console.log(orders);
    if(orders.length == 1) {
       return popupOrder(orders[0]);
    }
    if(map.getZoom() !== 19) { //max
        map.setZoom(19);
        map.panTo(new BMap.Point(r.target.point.lng, r.target.point.lat));
        return ;
    }
    var repeatPoints = [];
    for(var i in orders) {
        order = orders[i];
        if(order.lng == r.target.point.lng && order.lat == r.target.point.lat) {
            repeatPoints.push(order);
        }
    }
    if(repeatPoints.length == 1) {
        return popupOrder(repeatPoints[0]);
    }
    //overlap; exactly the same location;
    for(var j in repeatPoints) {
        order = repeatPoints[j];
        msg.push('<div style="margin-top: 5px;border-bottom: #ddd 1px solid; text-align: left; font-size: 15px; line-height: 20px;" onclick="goto(\'' +  order.market_send_order_id + '\')">');
        msg.push('<span style="color: #555;">' + order.work_order_subject + '</span><br />');
        msg.push('<span style="font-size: 12px; line-height: 18px;">(' + order.passenger_name + '&nbsp;/&nbsp;' + order.flowStageName + ')' + '<br />');
        msg.push(order.plan_back_date + '~' + order.plan_leave_date + ')</span>');
        msg.push('</div>');
    }
    $.confirm(msg.join(''), '此位置有 ' + orders.length + ' 个营销单, 点击查看详情');
    
    function popupOrder(order) {
        msg.push('创建人：' + order.createBy); 
        msg.push('负责人：' + order.passenger_name);
        msg.push('当前状态：' + order.flowStageName);
        msg.push(order.plan_back_date + '~' + order.plan_leave_date);
        $.confirm(msg.join('<br />'), '查看 ' + order.work_order_subject + ' 详情？', function(){
            go('market_detail.html?workorderid=' + order.market_send_order_id);
        });
    }
}

function goto(id) {
    go('market_detail.html?workorderid=' + id);
}