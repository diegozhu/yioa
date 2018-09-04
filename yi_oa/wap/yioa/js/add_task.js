var app = angular.module("myApp", []);
var $scope;
var alerts = ['提前15分钟', '提前30分钟','提前一小时','提前两小时','提前三小时','提前一天'];
var alertsMapping = {'提前15分钟': 16, '提前30分钟':31, '提前一小时':61, '提前两小时':121, '提前三小时':181, '提前一天':24 * 60 + 1};

app.controller("myCtrl", function($scope) {
    var taskId = getUrlParameter('taskId');
    if(taskId) {
        $.get(makeAjaxUrl("/oa/task/" + taskId), function (data, textStatus, jqXHR) {
            $scope.task = data;
            $scope.$digest();
        });
    }
    window.$scope =  $scope;

    var now = new Date().getTime();
    $scope.task = {
        taskLevel: TaskLevels[0],
        alert: alerts[0],
        taskContent: '',
        startTime: new Date(new Date().getTime() + 60 *60 * 1000).format('YYYY-MM-DD HH:mm'),
        endTime: new Date(new Date().getTime() + 60 *60 * 1000).offsetDay(7).format('YYYY-MM-DD HH:mm')
    };
    
    $scope.saveTask = function(){
        if(!$scope.task.taskName){
            return $.toast("任务名不能为空！", "text");
        }
        $scope.task.alertTime = (new Date($scope.task.startTime.toDate().getTime() - 60 * 1000 * alertsMapping[$scope.task.alert])).format('YYYY-MM-DD HH:mm');
        $.post(makeAjaxUrl("/oa/task/saveOrUpdate"), $scope.task, function (data, textStatus, jqXHR) {
           $.toast("提交成功", function () {
                window.location.href = 'home.html#tab1';
            });
        });
    };

    $scope.selectOrder = function(order){
        $scope.task.refOrder = order.workOrderSubject || '无名称';
        $scope.task.refOrderId = order.workOrderId;
        $.closePopup();
    };

    $scope.delete = function() {
        $.confirm('确认删除？', '任务： ' + $scope.task.taskName, function(){
            $.post(makeAjaxUrl("/oa/task/delete/" + $scope.task.taskId), function (data, textStatus, jqXHR) {
                $.toast("已删除", function () {
                     window.location.href = 'home.html#tab1';
                 });
             });
        });
    };
});

$(function(){

    $(".datetimepicker").datetimePicker();

    $("#taskLevel").click(function () {
        var actionsArr = [];
        for(var i in TaskLevels) {
            (function(level, idx){
                actionsArr.push({
                    text: level,
                    className: "color-primary",
                    onClick: function () {
                        $scope.task.taskLevel = level;
                        $scope.$digest();
                    }
                });
            })(TaskLevels[i], i);
        }
        $.actions({ onClose: function () {}, actions: actionsArr});
    });

    
    $("#taskAlert").click(function () {
        var actionsArr = [];
        for(var i in alerts) {
            (function(alert){
                actionsArr.push({
                    text: alert,
                    className: "color-primary",
                    onClick: function () {
                        console.log(alert);
                        $scope.task.alert = alert;
                        $scope.$digest();
                    }
                });
            })(alerts[i]);
        }
        $.actions({ onClose: function () {}, actions: actionsArr});
    });

    $(".order-popup").click(function () {
        var actionsArr = [];
        actionsArr.push({ text: '营销单', className: "color-primary", onClick: function () { $("#orderChose").popup(); loadOrders('market'); }});
        actionsArr.push({ text: '用车单', className: "color-primary", onClick: function () { $("#orderChose").popup(); loadOrders('car'); }});
        actionsArr.push({ text: '事务单', className: "color-primary", onClick: function () { $("#orderChose").popup(); loadOrders('work'); }});

        if($scope.task.refOrderId && $scope.task.refOrderType) {
            actionsArr.push({ text: '查看关联' + $scope.task.refOrderType + ': ' + $scope.task.refOrder, className: "color-primary", onClick: function () {
                var url;
                if($scope.task.refOrderType == '用车单') {
                    url = "car_detail.html?orderid=";
                }else if($scope.task.refOrderType == '营销单') {
                    url = "market_detail.html?workorderid=";
                } else { //任务单
                    url = "oa_detail.html?workorderid=";
                }
                window.location.href = url + $scope.task.refOrderId;
            }});
        }
        actionsArr.push({ text: '新建工单', className: "color-primary", onClick: function () {
            window.location.href = 'oa_order.html';
        }});

        $.actions({ onClose: function () {}, actions: actionsArr});
    });
    
    $("#searchInput").on('keydown', loadOrders);
});

var lastOrderType;
function loadOrders(orderType){
    orderType = orderType || lastOrderType;
    lastOrderType = orderType;
    $scope.task.refOrderType = {'market':'营销单', 'car':'用车单', 'work':'任务单'}[orderType];
    $.get(makeAjaxUrl("/oa/workorder/todo/" + orderType + "/all?&keyword=" + $('#searchInput').val()), function (data, textStatus, jqXHR) {
        $scope.orders = data;
        $scope.$digest();
    });
}

document.addEventListener('YixinJSBridgeReady', function onBridgeReady() {
    YixinJSBridge.call('hideOptionMenu');
});