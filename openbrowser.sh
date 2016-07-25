#!/bin/bash
URL=$1
BROWSER=firefox
JAVA=/usr/lib/jvm/java-8-openjdk/bin/java
IMAGE_DOWNLOADER=/home/daniel/Projects/ImageDownloader/target/ImageDownloader-0.1.jar
if [ ${URL: -97:1} == "#" ]; then
	$JAVA -jar $IMAGE_DOWNLOADER $URL | feh -. -
elif $(echo ${URL##*.} | tr '[:upper:]' '[:lower:]' | grep 'png\|jpg' &>/dev/null); then
    feh $URL
else
   $BROWSER $URL
fi
