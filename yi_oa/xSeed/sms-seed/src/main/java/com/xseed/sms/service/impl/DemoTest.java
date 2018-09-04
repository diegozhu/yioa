package com.xseed.sms.service.impl;

import com.linkage.netmsg.NetMsgclient;
import com.linkage.netmsg.server.ReceiveMsg;
import com.xseed.sms.common.ReceiveDemo;

/**
 * Created by liangpengcheng on 2018/6/11.
 */
public class DemoTest {
    public static void main(String[] args){
        NetMsgclient client   = new NetMsgclient();
        ReceiveMsg receiveMsg = new ReceiveDemo();
        client = client.initParameters("202.102.41.101", 9005, "0515C00159683", "yc88350555", receiveMsg);
        try {
            /*登录认证*/
            boolean isLogin = client.anthenMsg(client);
            if(isLogin) {
                client.sendMsg(client, 0, "15695210606", "你好 测试", 1);
                System.out.println("login sucess");
	            /*发送下行短信*/
                System.out.println(client.sendMsg(client, 0, "13218065876", "test thread ", 1));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
