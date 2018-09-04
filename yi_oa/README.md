# README #

yi_oa 内容不多说了

### What is this repository for? ###

*

dev:
bypass oauth : http://localhost:8011/login4test/fenglei

本地后端打包jar: 
1. goto xSeed
    mvn clean && mvn install -Dmaven.test.skip=true
    mvn package -Dmaven.test.skip=true

2. goto yioa-parent 
    mvn clean && mvn install -Dmaven.test.skip=true
    mvn package -Dmaven.test.skip=true

打包完看到 hi/target/hi-1.0.jar , 上移两层路径，提交svn


服务器端升级：
1. 前端，在 /root/yixin/yi_oa/exin/ 执行svn update . 就好了
2. 后端，在 /root/yixin/yi_oa/exin/ 执行svn update . 后，在 yi_oa/yioa-parent 路径下 执行nohup java -jar hi-1.0.jar 


