#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
URL=$1
BROWSER=xdg-open
JAVA=/usr/lib/jvm/java-default-runtime/bin/java
IMAGE_DOWNLOADER=${DIR}/target/ImageDownloader-0.1.jar

if [ "${URL: -97:1}" == "#" ]; then
	$JAVA -jar $IMAGE_DOWNLOADER $URL | feh -. -
elif $(echo ${URL##*.} | tr '[:upper:]' '[:lower:]' | grep 'png\|jpg' &>/dev/null); then
    feh $URL
else
   $BROWSER $URL
fi
