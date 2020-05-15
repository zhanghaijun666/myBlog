#!/bin/bash

export JAVA_HOME=`dirname  ${JAVA_HOME}`
export JRE_HOME=${JAVA_HOME}/jre

APP_HOME=./
APP_NAME=blog-server

# 检查程序是否在运行
is_exist(){
        PID=$(ps -ef |grep ${APP_NAME} | grep -v $0 |grep -v grep |awk '{print $2}')
        if [ -z "${PID}" ]; then
                return 1
        else
                return 0
        fi
}

# 定义启动程序函数
start(){
        is_exist
        if [ $? -eq "0" ]; then
                echo "${APP_NAME} is already running, PID=${PID}"
        else    
                #nohup ${JRE_HOME}/bin/java -jar ${APP_HOME}/${APP_NAME} >/dev/null 2>&1 &
                nohup mvn spring-boot:run  >/dev/null 2>&1 &
                PID=$(echo $!)
                echo "${APP_NAME} start success, PID=$!"
        fi
}

# 停止进程函数
stop(){
        is_exist
        if [ $? -eq "0" ]; then
                kill -9 ${PID}
                echo "${APP_NAME} process stop, PID=${PID}"
        else    
                echo "There is not the process of ${APP_NAME}"
        fi
}

# 查看进程状态
status(){
        is_exist
        if [ $? -eq "0" ]; then
                echo "${APP_NAME} is running, PID=${PID}"
        else    
                echo "There is not the process of ${APP_NAME}"
        fi
}

cd ../
mvn clean compile

case $1 in
"start")
        start
        ;;
"stop")
        stop
        ;;
"restart")
        stop
        start
        ;;
"status")
       status
        ;;
	*)
    echo "Usage: blog.sh start|stop|restart|reload|force-reload|status"
	;;
esac
exit 0