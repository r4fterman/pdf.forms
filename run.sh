#!/usr/bin/env bash

mvn clean install
if [[ $? -ne 0 ]]; then
    exit 1
fi

java -jar target/pdfforms.jar
