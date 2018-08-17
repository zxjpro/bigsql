#!/bin/bash

bin_path=$(cd `dirname $0`;pwd)
cd $bin_path

./stop.sh
sleep 2
echo "start process..."
./start.sh
