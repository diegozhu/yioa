package com.xseed.sms.common;


import com.linkage.netmsg.NetMsgclient;
import com.linkage.netmsg.server.ReceiveMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by tao on 2017-08-19.
 */
public class TelecomSmsUtil {

    private final static Logger logger = LoggerFactory.getLogger(TelecomSmsUtil.class);
    private static NetMsgclient client = null;

    /*ReceiveMsgImpl为ReceiveMsg类的子类，构造时，构造自己继承的子类就行*/
    private static ReceiveMsg receiveMsg = new ReceiveDemo();

    static {
        /*初始化参数*/
        init(0);
    }

    public static void init(int i) {
//        client = new NetMsgclient();
//        client = client.initParameters("202.102.41.101", 9005, "0515C00159683", "yc88350555", receiveMsg);
//        boolean isLogin = false;
//        try {
//            isLogin = client.anthenMsg(client);
//        } catch (IOException e) {
//            logger.info(e.toString(), e);
//            if (client != null) {
//                client.closeConn();
//            }
//            try {
//                TimeUnit.SECONDS.sleep(90);
//            } catch (InterruptedException ee) {
//                logger.error(ee.toString(), ee);
//            }
//            if (i < 3) {
//                init(++i);
//            } else {
//                logger.error("############ fatal error!!!!! sms client init failed for more than 3 times,plz check env");
//            }
//        }
//        logger.info("###### login end     {}", isLogin);
    }

    public static boolean sendSms(String num, String msg) {
        if (client == null) {
            client = new NetMsgclient();
            client = client.initParameters("202.102.41.101", 9005, "0515C00159683", "yc88350555", receiveMsg);
            try{
                boolean isLogin = client.anthenMsg(client);
                if(isLogin){
                    client.sendMsg(client, 0, num, msg, 1);
                }

            }catch (Exception e){
                   if (client != null) {
                        client.closeConn();
                    }
            }
//            isLogin = client.anthenMsg(client);
//            init(0);
        }else{
            client.sendMsg(client, 0, num, msg, 1);
        }
//        String str = client.sendMsg(client, 0, num, msg, 1);
        logger.info("#####send msg to {},msg is : {}", num, msg);
        return true;
    }
}
