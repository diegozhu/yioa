var app = angular.module("myApp", []);
var $scope;
var alerts = ['提前一小时','提前两小时','提前三小时','提前一天','提前天'];

app.controller("myCtrl", function($scope) {
    $scope.projectName = getUrlParameter('projectName');
    $scope.date = getUrlParameter('date');
    var url = "/oa/task/list?project_name=" +  $scope.projectName + '&start=' + $scope.date.format('YYYY-MM-DD 00:00:00') + '&end=' + $scope.date.format('YYYY-MM-DD 23:59:59');
    $.get(makeAjaxUrl(url), function (data, textStatus, jqXHR) {
        $scope.tasks = data.records;
        var count  = 0;
        for(var i in data.records) {
            count += data.records[i].status == '已完成' ? 1 : 0;
        }
        $scope.finished = count;
        $scope.$digest();
    });
    window.$scope =  $scope;

    $scope.gotoref = function(task) {
        var url;
        if(task.refOrderType == '用车单') {
            url = "car_detail.html?orderid=";
        }else if(task.refOrderType == '营销单') {
            url = "market_detail.html?workorderid=";
        } else { //任务单
            url = "oa_detail.html?workorderid=";
        }
        window.location.href = url + task.refOrderId;
    }
    $scope.clickTask = function(task) {
        $.modal({
            text: '开始时间：' + task.startTime + '<br> 结束时间：' + task.endTime + '<br>' +task.taskContent,
            title: task.taskName,
            buttons: [
            { text: '取消', className: "default"},
            {
            text: task.status == '已完成' ? '未完成' : '完成',
            className: "default",
            onClick: function() {
                $.post(makeAjaxUrl((task.status == '已完成' ? "/oa/task/undone/" : '/oa/task/done/') + task.taskId), function(){
                    $scope.finished += task.status == '已完成' ?  -1 : +1;
                    task.status = task.status == '已完成' ? '未完成' : '已完成';
                    $scope.$digest();
                });
            }
            },
            {
            text: '详情',
            className: "primary",
            onClick: function() {
                window.location.href = 'add_task.html?taskId=' + task.taskId;
            }
            }]
        });
    }
});

document.addEventListener('YixinJSBridgeReady', function onBridgeReady() {
    YixinJSBridge.call('hideOptionMenu');
});