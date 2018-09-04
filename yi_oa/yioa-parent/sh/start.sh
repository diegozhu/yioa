#!/bin/bash

echo ${JAVA_HOME}
dir_base=/var/lib/jenkins/workspace/yi_oa_parent
cd ${dir_base}
echo 'deploying...'
#if [ -r 'deploy.tar.gz'  ];then
#  tar -zxvf deploy.tar.gz
#fi
#rm -rf *.gz
#rm -rf ${dir_base}/logs
jar_name='hi-1.0.jar'
if [ $(pgrep -f ${jar_name} | wc -l) -gt 0 ]; then
  pkill -9 -f ${jar_name}
fi

src_jar=/var/lib/jenkins/workspace/yi_oa_parent/yioa-parent/hi/target/hi-1.0.jar
tag_jar=/home/ubuntu/demo/exec_jar/hi-1.0.jar
rm ${tag_jar}
cp ${src_jar} /home/ubuntu/demo/exec_jar/
echo ${tag_jar}

#nohup ${JAVA_HOME}/bin/java -jar ${tag_jar} >> /var/log/yi_oa/hi.log 2>&1 &
nohup ${JAVA_HOME}/bin/java -jar ${tag_jar} >> /var/log/yi_oa/hi.log &

sleep 2
echo 'ok!'
