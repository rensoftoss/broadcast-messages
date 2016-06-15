#!/bin/bash

DIRNAME=`dirname "$0"`
PROGNAME=`basename "$0"`

while [ "$#" -gt 0 ]
do
    case "$1" in
      --)
          shift
          break;;
      *)
          SERVER_OPTS="$SERVER_OPTS -D$1"
          ;;
    esac
    shift
done

# Specify options to pass to the Java VM.
if [ "x$JAVA_OPTS" = "x" ]; then
   JAVA_OPTS="-Xms64m -Xmx200m -XX:MaxMetaspaceSize=256m"
else
   echo "JAVA_OPTS already set in environment; overriding default settings with values: $JAVA_OPTS"
fi

# Setup the JVM
if [ "x$JAVA" = "x" ]; then
    if [ "x$JAVA_HOME" != "x" ]; then
        JAVA="$JAVA_HOME/bin/java"
    else
        JAVA="java"
    fi
fi

if [[ "$JAVA" ]]; then
    JAVA_VERSION=$("$JAVA" -version 2>&1 | awk -F '"' '/version/ {print $2}')
    if [[ "$JAVA_VERSION" < "1.8" ]]; then
        echo "Please use Java 1.8 or later !"
        exit
    fi
fi

# Setup the service directory
if [ "x$SERVICEDIR" == "x" ]; then
 SERVICEDIR=/opt/broadcast-messages
fi

SERVICE_JAR="$SERVICEDIR/broadcast-messages.jar"

TIMESTAMP=$(date +%s)

# Execute the JVM in the background
if [ "x$DEBUG_MODE" != "x" ]; then
 echo "eval \"$JAVA\" $SERVER_OPTS $JAVA_OPTS -cp \"$CLASSPATH\" -jar \"$SERVICE_JAR\" > /tmp/broadcast-messages-\"$TIMESTAMP\".log & "
fi

eval \"$JAVA\" $SERVER_OPTS $JAVA_OPTS -jar \"$SERVICE_JAR\" \> /tmp/broadcast-messages-\"$TIMESTAMP\".log &

