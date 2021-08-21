#!/bin/bash
echo "Hello welcome to Brian's code"

FILE=./work/BrianLewis.jar
if test -f "$FILE"; then
    echo "$FILE exists."
  else
    echo "no file building jar"
    sbt assembly
    mkdir "work"
    cp ./target/scala-2.13/BrianLewis.jar ./work/BrianLewis.jar
fi

: ${HTTP_PORT?"Need to set HTTP_PORT"}
: ${CSCARDS_ENDPOINT?"Need to set CSCARDS_ENDPOINT"}
: ${SCOREDCARDS_ENDPOINT?"Need to set SCOREDCARDS_ENDPOINT"}

java -jar ./work/BrianLewis.jar