#!/usr/bin/env bash

JAVA_OPTS="-Xmx1G"

MAC=
if [[ "$OSTYPE" == "darwin"* ]]; then
  MAC="-Xdock:name=\"PDF Forms Designer\""
fi

# execute the jar
java $JAVA_OPTS "$MAC" -jar target/pdf-forms-designer.jar
