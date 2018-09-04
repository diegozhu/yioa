var map, myIcon, app = angular.module("myApp", []), $scope;
var $inputLocation;
var myGeo, city, marker;

app.controller("myCtrl", function($scope) {
    window.$scope = $scope;
    $scope.continue = function (){
        go('market_order.html?district=' + $scope.address.addressComponents.district + '&name=' +  $scope.name + '&lng=' + $scope.address.point.lng+ '&lat=' + $scope.address.point.lat);
    };
});

$(function () {
    map = new BMap.Map('allmap');
    city = UrlParam.city || "盐城市";
    map.centerAndZoom(city, 12);
    map.enableScrollWheelZoom(true);
    myIcon = new BMap.Icon("../images/locate_small.png", new BMap.Size(20, 28));
    map.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_SMALL}));
    myGeo = new BMap.Geocoder();

    myGeo.getPoint(city, function(point){
        marker = new BMap.Marker(point);
        marker.addEventListener("dragend", markerMoved);
        map.addOverlay(marker);
        marker.enableDragging();
        marker.setPosition(point);
        map.centerAndZoom(point, 12);
        markerMoved();
    }, city);
});

function markerMoved() {
    var point = marker.getPosition();
    myGeo.getLocation(point, function(rs){
        console.log(rs.point.lat + ' ' + rs.point.lng);
        $scope.address = rs;
        if(rs.surroundingPois && rs.surroundingPois.length > 0) {
            $scope.name = rs.surroundingPois[0].title;
        } else {
            $scope.name = rs.business || '未知位置';
        }
        $scope.$digest();
    });
}