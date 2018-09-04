package com.xseed.sms.service.impl;

/**
 * Created by tao on 2017-07-28.
 */



import com.linkage.netmsg.NetMsgclient;
import com.linkage.netmsg.server.AnswerBean;

import com.linkage.netmsg.server.ReceiveMsg;
import com.xseed.sms.common.ReceiveDemo;

public class Demo {
    public static void main(String[] args) {
        NetMsgclient client   = new NetMsgclient();
        /*ReceiveMsgImpl为ReceiveMsg类的子类，构造时，构造自己继承的子类就行*/
        ReceiveMsg receiveMsg = new ReceiveDemo();
        /*初始化参数*/
        client = client.initParameters("202.102.41.101", 9005, "0515C00159683", "yc88350555", receiveMsg);
        try {

            /*登录认证*/
            boolean isLogin = client.anthenMsg(client);
            System.out.println("###### login end     " + isLogin);
            if(isLogin) {
                System.out.println("login sucess");
	            /*发送下行短信*/
                String msg = client.sendMsg(client, 0, "15695210606", "this is a test ggggggggggg ", 1);
                System.out.println("################ msg: " + msg);
//                AnswerBean answerBean = new AnswerBean();
//                receiveMsg.getAnswer(answerBean);
//                client.closeConn();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
