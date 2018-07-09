# ImageDownloader
When [Conversations](https://conversations.im) uploads an encrypted image with [XEP-0363: HTTP Upload](http://xmpp.org/extensions/xep-0363.html) it appends the encryption key as an anchor to the URL.

The anchor is made of 96 characters which represent 48 bytes in HEX. The first 16 bytes are the IV the last 32 bytes are the key.

The encryption mode is ```aes-gcm```. The authentcation tag of 16 bytes is appended to the file.

This Java project provides sample code on how to download and decrypt a file. Output is to stdout. Redirect to file or pipe to image viewer.

Compile with ```mvn package```

Find a compiled version [here](https://gultsch.de/ImageDownloader-0.1.jar)

Usage: ```java -jar ImageDownloader.jar http://host.tld/path/to/file.jpg#theivandkey```

If you change your browser in gajim to something like this script it will automatically open image links in your image viewer if you click on them.
```bash
#!/bin/bash
URL=$1
BROWSER=firefox
JAVA=/usr/lib/jvm/java-8-openjdk/bin/java
IMAGE_DOWNLOADER=/home/daniel/Projects/ImageDownloader/t
if [ ${URL: -97:1} == "#" ]; then
	$JAVA -jar $IMAGE_DOWNLOADER $URL | feh -. -
else
   $BROWSER $URL
fi
```
