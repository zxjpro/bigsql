#!/bin/bash


bin_path=$(cd `dirname $0`;pwd)

base_path=$(cd $bin_path;cd ..;pwd)

app_name=bigsql-server.jar

JAVA_OPTIONS="-Xms2048m -Xmx2048m -Xmn1792m -XX:+HeapDumpOnOutOfMemoryError"

PARAM="-Dbigsql.conf=$base_path/conf/ -Dfile.encoding=UTF-8"
CMD="java -jar $JAVA_OPTIONS $PARAM $base_path/lib/$app_name"


function start(){
	echo "start $app_name"
	$CMD
}

function stop(){
	echo "stop $app_name"
	find_pid_cmd="ps -ef | grep $app_name | grep -v grep | grep -v $0 | awk '{print \$2}'"
	#echo $find_pid_cmd
	
	pid=`eval $find_pid_cmd`
	if [ $pid ]
	then
		echo $pid
		kill -9 $pid
		echo ""
		echo "============================================================="
		echo "  stop $app_name success! "
		echo "============================================================="
	else
		echo ""
		echo "=========================="
		echo "=   process not found!   ="
		echo "=========================="
		
	fi
	
	
	
}

function restart(){
	echo "restart $app_name"
	stop
	sleep 2
	start
}

case "$1" in
	start)
		start;;
	stop)
		stop;;
	restart)
		restart;;
esac