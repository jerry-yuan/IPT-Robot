#!/bin/bash

# add this script to crontab and run it every 5 minutes.

HOST_LIST='/var/lib/tomcat9/webapps/ROOT/host-list'

export TZ=Asia/Shanghai
export PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/local/games:/snap/bin


echo "" > $HOST_LIST

echo '<p>' >> $HOST_LIST
echo '<a href="../">&lt;&lt; GO BACK TO HOME</a>' >> $HOST_LIST
echo '</p>' >> $HOST_LIST

echo "<p>The host scan started at `date` and will cost about 1 min to retrieve list...</p>" >> $HOST_LIST
echo '<p><b>**PLEASE WAIT IF THERE IS NO ANY RESULT AND REFRESH THE PAGE IN 1 MINUTE**</b></p>' >> $HOST_LIST
echo '<pre>' >> $HOST_LIST
ARP_RESULT=`arp-scan --interface=br-static -x 180.201.128.0:255.255.192.0 -g | grep -v "HUAWEI"`
echo -e "${ARP_RESULT}">> $HOST_LIST

curl -X POST \
	http://localhost:8080/ipt \
	-d "$ARP_RESULT"

echo '</pre>' >> $HOST_LIST
echo "<p>The host scan ends at `date`</p>" >> $HOST_LIST