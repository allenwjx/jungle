#!/bin/sh
# chkconfig 2345 on

if [ -f /etc/init.d/functions ]; then
. /etc/init.d/functions
elif [ -f /etc/rc.d/init.d/functions ] ; then
. /etc/rc.d/init.d/functions
else
exit 0
fi

# Source networking configuration.
. /etc/sysconfig/network

# Check that networking is up.
[ ${NETWORKING} = "no" ] && exit 0

STARTUP_DIR=`pwd`

exec="$STARTUP_DIR/snailms.sh"

start() {
daemon $exec start
if [ $? -ne 0 ]; then
echo "Error configuration file"
return 1
fi
echo -n "Starting server ..."
RETVAL=$?
echo
[ $RETVAL -eq 0 ] && touch /var/lock/subsys/jungle
return $RETVAL
}

stop() {
echo -n "Shutdown server ..."
$exec stop
RETVAL=$?
echo
[ $RETVAL -eq 0 ] && rm -rf /var/lock/subsys/jungle
return $RETVAL
}

restart() {
$exec restart
if [ $? -ne 0 ]; then
echo "Error configuration file"
return 1
fi
}

rhstatus() {
$exec status
}

# See how we were called.
case "$1" in
start)
start
;;
stop)
stop
;;
restart)
restart
;;
status)
rhstatus
;;
*)

echo $"Usage: deploy.sh {start|stop|restart|status}"
RETVAL=1
esac
exit $RETVAL
