#!/bin/bash

bin_path=$(cd `dirname $0`;pwd)
cd $bin_path

nohup ./command.sh start > /dev/null 2>&1 &
#tail -f logs/info.log
