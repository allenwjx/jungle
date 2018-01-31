#!/bin/sh
# Copyright (c) 2001, 2016 LY.com Inc.
#
# Java application sh script.  Suitable for starting and stopping
# wrapped Java applications on UNIX platforms.
#
#
# Get the fully qualified path to the script
case $0 in
/*)
SCRIPT="$0"
;;
*)
PWD=`pwd`
SCRIPT="$PWD/$0"
;;
esac

# Resolve the true real path without any sym links.
CHANGED=true
while [ "X$CHANGED" != "X" ]
do
# Change spaces to ":" so the tokens can be parsed.
SAFESCRIPT=`echo $SCRIPT | sed -e 's; ;:;g'`
# Get the real path to this script, resolving any symbolic links
TOKENS=`echo $SAFESCRIPT | sed -e 's;/; ;g'`
REALPATH=
for C in $TOKENS; do
# Change any ":" in the token back to a space.
C=`echo $C | sed -e 's;:; ;g'`
REALPATH="$REALPATH/$C"
# If REALPATH is a sym link, resolve it.  Loop for nested links.
while [ -h "$REALPATH" ] ; do
LS="`ls -ld "$REALPATH"`"
LINK="`expr "$LS" : '.*-> \(.*\)$'`"
if expr "$LINK" : '/.*' > /dev/null; then
# LINK is absolute.
REALPATH="$LINK"
else
# LINK is relative.
REALPATH="`dirname "$REALPATH"`""/$LINK"
fi
done
done

if [ "$REALPATH" = "$SCRIPT" ]
then
CHANGED=""
else
SCRIPT="$REALPATH"
fi
done

# Save the startup directory
STARTUP_DIR=`pwd`

# Change the current directory to the location of the script
cd "`dirname "$REALPATH"`"
REALDIR=`pwd`
APP_HOME="`dirname "${REALDIR}"`"

if [ "X$JAVA_HOME" = "X" ]
then
JAVA_HOME="/usr"
fi

# Configure remote Java debugging options here
# Setting suspend=y will wait for you to connect before proceeding
JPDA_OPTS="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
#JAVA_OPTS="-server -Xms1800m -Xmx1800m -Xmn680m -Xss256k -XX:PermSize=340m -XX:MaxPermSize=340m -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:CMSFullGCsBeforeCompaction=5 -XX:+UseCMSCompactAtFullCollection -XX:+CMSClassUnloadingEnabled -XX:+DisableExplicitGC -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Dcom.sun.management.jmxremote.port=9981 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dfile.encoding=${__output_encoding}"
JAVA_OPTS=`cat java_opts |awk '{print $0}'`

# Configure message server system properties
APP_ARGS="-Ddubbo.properties.file=$APP_HOME/conf/dubbo.properties -Dlog4j.configurationFile=$APP_HOME/conf/log4j.xml"

# Classpath
APP_CORE=$APP_HOME/core/*
APP_LIB=$APP_HOME/lib/*
APP_CFG=$APP_HOME/conf/
CLASSPATH=$CLASSPATH:$APP_CORE:$APP_LIB:$APP_CFG

# Application name
APP_NAME=JungleContainer

# Application main class
APP_MAIN=com.zeh.jungle.core.boot.DefaultBootstrap

# Location of the pid file.
PIDDIR="."

# If the PIDDIR is relative, set its value relative to the full REALPATH to avoid problems if
# the working directory is later changed.
FIRST_CHAR=`echo $PIDDIR | cut -c1,1`
if [ "$FIRST_CHAR" != "/" ]
then
PIDDIR=$REALDIR/$PIDDIR
fi

# Process ID
ANCHORFILE="$PIDDIR/$APP_NAME.anchor"

# Patched for Jungle container server
PIDFILE="$PIDDIR/.$APP_NAME.pid"
LOCKDIR="/var/lock/subsys"
LOCKFILE="$LOCKDIR/$APP_NAME"
pid=""

# Resolve the location of the 'ps' command
PSEXE="/usr/ucb/ps"
if [ ! -x "$PSEXE" ]
then
PSEXE="/usr/bin/ps"
if [ ! -x "$PSEXE" ]
then
PSEXE="/bin/ps"
if [ ! -x "$PSEXE" ]
then
echo "Unable to locate 'ps'."
echo "Please report this message along with the location of the command on your system."
exit 1
fi
fi
fi
PSKEYWORD="args"

# Resolve the os
DIST_OS=`uname -s | tr [:upper:] [:lower:] | tr -d [:blank:]`
case "$DIST_OS" in
'sunos')
DIST_OS="solaris"
;;
'hp-ux' | 'hp-ux64')
DIST_OS="hpux"
;;
'darwin')
DIST_OS="macosx"
PSKEYWORD="command"
;;
'unix_sv')
DIST_OS="unixware"
;;
'aix')
DIST_OS="aix"
;;
esac

# Resolve the architecture
UNAME_PROC_OPTION="-m"
if [ "$DIST_OS" = "aix" ]; then
UNAME_PROC_OPTION="-p"
fi

PROC_ARCH=`uname $UNAME_PROC_OPTION | tr [:upper:] [:lower:] | tr -d [:blank:]`

# The previous approach was this:
# If a 32-bit wrapper binary exists then it will work on 32 or 64 bit
# platforms, if the 64-bit binary exists then the distribution most
# likely wants to use long names.
DIST_BITS="32"

while :
do
case "$PROC_ARCH" in
'amd64' | 'athlon' | 'i386' | 'i486' | 'i586' | 'i686' | 'i86pc')
DIST_ARCH="x86"
break;;
'x86_64')
DIST_ARCH="x86"
# on 64 bit Linuxes the 32 bit subsystem may not be installed so
# let's use the 64 bit wrapper
DIST_BITS="64"
break;;
'ia32' | 'ia64')
DIST_ARCH="ia"
break;;
'ip27')
DIST_ARCH="mips"
break;;
'power' | 'powerpc' | 'power_pc' | 'ppc64' | 'powermacintosh')
DIST_ARCH="ppc"
break;;
'pa_risc' | 'pa-risc')
DIST_ARCH="parisc"
break;;
'sun4u' | 'sun4us' | 'sun4v' | 'sparcv9' | 'sparc')
DIST_ARCH="sparc"
break;;
'9000/800')
DIST_ARCH="parisc"
break;;
*)
echo "Your machine's hardware type (uname $UNAME_PROC_OPTION) was not recognized by the Service Wrapper as a supported platform."
echo "Please report this message along with your machine's processor/architecture."
exit 1
;;
esac
done
######################################################################

outputFile() {
if [ -f "$1" ]
then
echo "  $1 (Found but not executable.)";
else
echo "  $1"
fi
}

# Build the nice clause
if [ "X$PRIORITY" = "X" ]
then
CMDNICE=""
else
CMDNICE="nice -$PRIORITY"
fi

# Build the anchor file clause.
if [ "X$IGNORE_SIGNALS" = "X" ]
then
ANCHORPROP=
IGNOREPROP=
else
ANCHORPROP=wrapper.anchorfile=\"$ANCHORFILE\"
IGNOREPROP=wrapper.ignore_signals=TRUE
fi

# Build the lock file clause.  Only create a lock file if the lock directory exists on this platform.
LOCKPROP=
if [ -d $LOCKDIR ]
then
if [ -w $LOCKDIR ]
then
LOCKPROP=wrapper.lockfile=\"$LOCKFILE\"
fi
fi

checkUser() {
# $1 touchLock flag
# $2 command

# Check the configured user.  If necessary rerun this script as the desired user.
if [ "X$RUN_AS_USER" != "X" ]
then
# Resolve the location of the 'id' command
IDEXE="/usr/xpg4/bin/id"
if [ ! -x "$IDEXE" ]
then
IDEXE="/usr/bin/id"
if [ ! -x "$IDEXE" ]
then
echo "Unable to locate 'id'."
echo "Please report this message along with the location of the command on your system."
exit 1
fi
fi

if [ "`$IDEXE -u -n`" = "$RUN_AS_USER" ]
then
# Already running as the configured user.  Avoid password prompts by not calling su.
RUN_AS_USER=""
fi
fi
if [ "X$RUN_AS_USER" != "X" ]
then
# If LOCKPROP and $RUN_AS_USER are defined then the new user will most likely not be
# able to create the lock file.  The Wrapper will be able to update this file once it
# is created but will not be able to delete it on shutdown.  If $2 is defined then
# the lock file should be created for the current command
if [ "X$LOCKPROP" != "X" ]
then
if [ "X$1" != "X" ]
then
# Resolve the primary group
RUN_AS_GROUP=`groups $RUN_AS_USER | awk '{print $3}' | tail -1`
if [ "X$RUN_AS_GROUP" = "X" ]
then
RUN_AS_GROUP=$RUN_AS_USER
fi
touch $LOCKFILE
chown $RUN_AS_USER:$RUN_AS_GROUP $LOCKFILE
fi
fi

# Still want to change users, recurse.  This means that the user will only be
#  prompted for a password once. Variables shifted by 1
su -m $RUN_AS_USER -c "\"$REALPATH\" $2"

# Now that we are the original user again, we may need to clean up the lock file.
if [ "X$LOCKPROP" != "X" ]
then
getpid
if [ "X$pid" = "X" ]
then
# Wrapper is not running so make sure the lock file is deleted.
if [ -f "$LOCKFILE" ]
then
rm "$LOCKFILE"
fi
fi
fi

exit 0
fi
}

######################################################################

getpid() {
javaps=`$PSEXE -ef|grep $APP_MAIN |grep -v grep|awk '{print $2}'`
#javaps=`$JAVA_HOME/bin/jps -l | grep $APP_MAIN`
if [ -n "$javaps" ]; then
pid=${javaps}
else
pid=""
fi
}

testpid() {
if [ "$DIST_OS" = 'solaris' ]
then
# on Solaris, both /usr/ucb/ps and /usr/bin/ps accept the ww option.
pid=`$PSEXE ww $pid | grep $pid | grep -v grep | awk '{print $1}' | tail -1`
else
pid=`$PSEXE -p $pid | grep $pid | grep -v grep | awk '{print $1}' | tail -1`
fi

if [ "X$pid" = "X" ]
then
# Process is gone so remove the pid file.
rm -f "$PIDFILE"
pid=""
fi
}

testlogdir() {
if [ ! -d $APP_HOME/logs ]
then
mkdir $APP_HOME/logs
fi
}

console() {
# $1 debug command
# $2 jprofiler command

echo "###################################################################################"
echo "Running $APP_NAME..."
getpid
echo $pid
if [ "X$pid" = "X" ]
then
# The string passed to eval must handle spaces in paths correctly.
DEBUG=""

if [ "$1" = "-debug" ]
then
DEBUG=${DEBUG}" "${JPDA_OPTS}
fi

if [ "$1" = "-jprofiler" ]
then
DEBUG=${DEBUG}" "${JPROFILER}
fi

if [ "$2" = "-debug" ]
then
DEBUG=${DEBUG}" "${JPDA_OPTS}
fi

if [ "$2" = "-jprofiler" ]
then
DEBUG=${DEBUG}" "${JPROFILER}
fi

$JAVA_HOME/bin/java $JAVA_OPTS $DEBUG $APP_ARGS -cp $CLASSPATH $APP_MAIN
else
echo "$APP_NAME is already running."
exit 1
fi
}

start() {
# $1 debug command
# $2 jprofiler command

echo "###################################################################################"
echo "Running $APP_NAME..."
getpid
if [ "X$pid" = "X" ]
then
testlogdir

# The string passed to eval must handles spaces in paths correctly.
DEBUG=""

if [ "$1" = "-debug" ]
then
DEBUG=${DEBUG}" "${JPDA_OPTS}
fi

if [ "$1" = "-jprofiler" ]
then
DEBUG=${DEBUG}" "${JPROFILER}
fi

if [ "$2" = "-debug" ]
then
DEBUG=${DEBUG}" "${JPDA_OPTS}
fi

if [ "$2" = "-jprofiler" ]
then
DEBUG=${DEBUG}" "${JPROFILER}
fi

$JAVA_HOME/bin/java $JAVA_OPTS $DEBUG $APP_ARGS -cp $CLASSPATH $APP_MAIN >/dev/null 2>&1 & getpid
else
echo "$APP_NAME is already running."
exit 1
fi
}

stopit() {
echo "Stopping $APP_NAME..."
getpid
echo $pid
if [ "X$pid" = "X" ]
then
echo "$APP_NAME was not running."
else
if [ "X$IGNORE_SIGNALS" = "X" ]
then
# Running so try to stop it.
kill $pid
if [ $? -ne 0 ]
then
# An explanation for the failure should have been given
echo "Unable to stop $APP_NAME."
exit 1
fi
else
rm -f "$ANCHORFILE"
if [ -f "$ANCHORFILE" ]
then
# An explanation for the failure should have been given
echo "Unable to stop $APP_NAME."
exit 1
fi
fi

# We can not predict how long it will take for the wrapper to
#  actually stop as it depends on settings in wrapper.conf.
#  Loop until it does.
savepid=$pid
CNT=0
TOTCNT=0
while [ "X$pid" != "X" ]
do
# Show a waiting message every 5 seconds.
if [ "$CNT" -lt "5" ]
then
CNT=`expr $CNT + 1`
else
echo "Waiting for $APP_NAME to exit..."
CNT=0
fi
TOTCNT=`expr $TOTCNT + 1`

sleep 1

testpid
done

pid=$savepid
testpid
if [ "X$pid" != "X" ]
then
echo "Failed to stop $APP_NAME."
exit 1
else
echo "Stopped $APP_NAME."
fi
fi
}

status() {
getpid
if [ "X$pid" = "X" ]
then
echo "$APP_NAME is not running."
exit 1
else
echo "$APP_NAME is running ($pid)."
exit 0
fi
}

case "$1" in

'console')
checkUser touchlock $1
console $2 $3
;;

'start')
checkUser touchlock $1
start $2 $3
;;

'stop')
checkUser "" $1
stopit
;;

'restart')
checkUser touchlock $1
stopit
start
;;

'status')
checkUser "" $1
status
;;

*)
######################################################################
# Customized for Jungle Container Server
######################################################################
echo "Running in console (foreground) mode by default, use Ctrl-C to exit..."

# Change back to the original startup directory.
cd "$STARTUP_DIR"

# Call this script recursively with the "console" parameter.
"$REALPATH" console $*
######################################################################
;;
esac

exit 0
