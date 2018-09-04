var app = angular.module("myApp", []);
var $scope;

app.controller("myCtrl", function($scope) {
    
    var index = UrlParam.MainTab || 4;

    window.$scope =  $scope;
    $scope.user = {};
    $scope.tabs = [
        {idx:1, name:'任务', icon: 'task', id:'taskImg'},
        {idx:2, name:'工单', icon: 'order', id:'orderImg'},
        {idx:3, name:'通讯录', icon: 'phonebook', id:'bookImg'},
        {idx:4, name:'我的', icon: 'me', id:'selImg'},
    ];

    $scope.currentTab = $scope.tabs[index - 1];
    $scope.currentTab.selected = true;

    $scope.date = (new Date()).format('YYYY-MM-DD');
    $scope.zhouji = ['日','一','二','三','四','五','六'][$scope.date.toDate().getDay()];
    $scope.taskLeves = TaskLevels;

    $scope.firstDayOfThisWeek =  getUrlParameter('firstDayOfThisWeek') || $scope.date.toDate().offsetDay( 1 - $scope.date.toDate().getDay()).format('YYYY-MM-DD');
    $scope.lastDayOfThisWeek =  getUrlParameter('lastDayOfThisWeek') || $scope.date.toDate().offsetDay( 7 - $scope.date.toDate().getDay()).format('YYYY-MM-DD');
    
    $scope.xiangXian = [
        { cells: [ {id:0}, {id:1}]},
        { cells: [ {id:2}, {id:3}]}
    ];

    var timeouthandler;

    $scope.daysOffset = function(day){
        $scope.date =  $scope.date.toDate().offsetDay(day).format('YYYY-MM-DD');
        $scope.zhouji = ['日','一','二','三','四','五','六'][$scope.date.toDate().getDay()];
        $scope.firstDayOfThisWeek =  $scope.date.toDate().offsetDay( 1 - $scope.date.toDate().getDay()).format('YYYY-MM-DD');
        $scope.lastDayOfThisWeek =  $scope.date.toDate().offsetDay( 7 - $scope.date.toDate().getDay()).format('YYYY-MM-DD');
        updateUrl({firstDayOfThisWeek: $scope.firstDayOfThisWeek, lastDayOfThisWeek: $scope.lastDayOfThisWeek});
        clearTimeout(timeouthandler);
        timeouthandler = setTimeout(function(){
            loadTab4Xiangxian();
            loadTabTimeline();
            loadTabProject();
        }, 500);        
        $scope.tasks = [];
        $scope.projects = [];
    };

    $scope.gotoAddTask = function(){
        window.location.href = 'add_task.html';
    };
    
    $scope.goToListByProject = function(project){
        window.location.href = "list_task.html?projectName="+project.name + "&date=" + $scope.date;
    };

    $scope.clickTask = function(task){
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
    };

    $scope.clickTab = function(tab) {
        $scope.currentTab.selected = false;
        $scope.currentTab = tab;
        tab.selected = true;
        updateUrl({'MainTab': tab.idx});
    };

    var qryUrl = "/oa/workorder/todo/all?size=20&current=1";
    $.getJSON(makeAjaxUrl(qryUrl), function (data, textStatus, jqXHR) {
        $("#orderCnt").html(data.total)
    });
});

function loadTab4Xiangxian(index, $dom) {
    $.getJSON(makeAjaxUrl("/oa/task/list?sidx=create_date&size=100&order=desc&start=" + $scope.firstDayOfThisWeek.format('YYYY-MM-DD 00:00') + '&end=' + $scope.lastDayOfThisWeek.format('YYYY-MM-DD 23:59')), function (data, textStatus, jqXHR) {
        $scope.tasks = data.records;
        $scope.$digest();
    });
}


function _getKey(date, offset) {
    return date.toDate().offsetDay(offset).format('MM-DD') + ' 周' + ['日','一','二','三','四','五','六'][date.toDate().offsetDay(offset).getDay()];
}
function loadTabTimeline(index, $dom) {
    $.getJSON(makeAjaxUrl("/oa/task/list-by-start-date?size=100&sidx=create_date&order=desc&start=" + $scope.firstDayOfThisWeek + '&end=' + $scope.lastDayOfThisWeek.format('YYYY-MM-DD 23:59')), function (data, textStatus, jqXHR) {
        var tmp = [];
        tmp.push({ date: _getKey( $scope.firstDayOfThisWeek, 0), tasks: []});
        tmp.push({ date: _getKey( $scope.firstDayOfThisWeek, 1), tasks: []});
        tmp.push({ date: _getKey( $scope.firstDayOfThisWeek, 2), tasks: []});
        tmp.push({ date: _getKey( $scope.firstDayOfThisWeek, 3), tasks: []});
        tmp.push({ date: _getKey( $scope.firstDayOfThisWeek, 4), tasks: []});
        tmp.push({ date: _getKey( $scope.firstDayOfThisWeek, 5), tasks: []});
        tmp.push({ date: _getKey( $scope.firstDayOfThisWeek, 6), tasks: []});
        for(var i in data.records) {
            var task = data.records[i];
            var day = task.startTime.toDate().getDay() == 0 ? 6 : task.startTime.toDate().getDay() - 1;
            task.startTimeShort = task.startTime.format('hh:mm')
            task.endTimeShort = task.endTime.format('hh:mm')
            tmp[day].tasks.push(task);
        }
        $scope.tasksByTimeline = tmp;
        $scope.$digest();
    });}

function loadTabProject(index, $dom) {
    $.getJSON(makeAjaxUrl("/oa/task/list/by-project?size=100&start=" + $scope.firstDayOfThisWeek.format('YYYY-MM-DD 00:00') + '&end=' + $scope.lastDayOfThisWeek.format('YYYY-MM-DD 23:59') ), function (data, textStatus, jqXHR) {
        var projects = {};
        for(var i in data) {
            var project = data[i];
            if(!projects[project.project_name || '未标记']) {
                projects[project.project_name || '未标记'] = { done : 0, undone : 0 };
            }
            if(project.status == '已完成') {
                projects[project.project_name || '未标记'].done = project.count;
            } else {
                projects[project.project_name || '未标记'].undone = project.count;
            }
        }
        var tmp = [];
        for(var i in projects) {
            tmp.push({
                name: i,
                undone: projects[i].undone,
                done: projects[i].done,
                finish: Math.round(projects[i].done * 100 / (projects[i].done + projects[i].undone))
            });
        }
        $scope.projects = tmp;
        $scope.$digest();
    });
}


$(function () {
    FastClick.attach(document.body);
    $.getJSON(makeAjaxUrl("/sys/staff/xx"), function (data) {
        $scope.user = data;
        $scope.$digest();
    });
    loadTab4Xiangxian();
    loadTabTimeline();
    loadTabProject();
    $('.cell .content').css('min-height', ((document.body.clientHeight - 150) / 2) + 'px');
});

document.addEventListener('YixinJSBridgeReady', function onBridgeReady() {
    YixinJSBridge.call('hideOptionMenu');
});