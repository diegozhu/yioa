package com.yioa.oa.test;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.yioa.oa.domain.WorkOrderVo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by tao on 2017-05-24.
 */
public class GuavaMain {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
    public static void main(String[] args) {

        System.out.println(sdf.format(new Date()));


//        WorkOrderVo a = new WorkOrderVo();
//        a.setWorkOrderId("aaa");
//
//        WorkOrderVo b = new WorkOrderVo();
//        b.setWorkOrderId("bbb");
//
//        WorkOrderVo c = new WorkOrderVo();
//        c.setWorkOrderId("ccc");
//
//        List<WorkOrderVo> workOrderVoList = Lists.newArrayList(a,b,c);
//
//        List<String> list = Lists.transform(workOrderVoList, new Function<WorkOrderVo, String>() {
//
//            @Override
//            public String apply(WorkOrderVo workOrder) {
//                return workOrder.getWorkOrderId();
//            }
//        });
//
//        System.out.println(list);

    }
}
