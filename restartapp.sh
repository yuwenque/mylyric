#!/bin/bash
git pull
mvn package
kill `lsof -t -i:9595`
nohup java -jar /usr/local/src/mylyric/target/lyric-0.0.1-SNAPSHOT.jar params1 > nohup.out 2>&1 & 
