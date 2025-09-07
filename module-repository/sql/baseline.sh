#!/bin/sh

FILE=baseline.sql
TEMP_FILE=baseline.tmp.sql
sed '/^--/d' $FILE > $TEMP_FILE
sed -i 's/^/ /' $TEMP_FILE
BASELINE_SQL=$(tr -d "\n\r" < $TEMP_FILE)

flyway  -url=jdbc:postgresql://$DB_URL:5432/$DB_NAME \
        -user=$DB_USERNAME \
        -password=$DB_PASSWORD \
        -initSql="$BASELINE_SQL" \
        migrate
