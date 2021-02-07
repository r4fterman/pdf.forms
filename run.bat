@echo off

JAVA_OPTS="-Xmx1G"

# execute the jar
java %JAVA_OPTS% -jar target/pdf-forms-designer.jar
