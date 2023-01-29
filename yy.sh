#!/bin/bash

yourname=123

bin_path=`cd $(dirname $0); pwd`

b=`echo ${bin_path}${yourname}`

echo `cd $(dirname $0); pwd`
