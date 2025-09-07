#!/bin/sh

# delete .sql files
find . -type f -regex '.*\/[0-9]\{1,3\}-[0-9a-zA-Z_-]*\.sql' -delete

# output config
CURRENT_DATE=$(date '+%Y_%m_%d')
SNAPSHOT_FILE="000-snapshot_$CURRENT_DATE.sql"

# baseline file
BASELINE_FILE="baseline.sql"

# pg connection config
PG_USER="${DB_USERNAME:-local}"
PG_PASSWORD="${DB_PASSWORD:-local}"
PG_HOST="${DB_URL:-localhost}"
PG_PORT="${DB_PORT:-5432}"
PG_DB="${DB_NAME:-local}"

# force password prompt (should happen automatically)
export PGPASSWORD="$PG_PASSWORD"

# Dump all schemas into a snapshot file
pg_dump --host=$PG_HOST --port=$PG_PORT --username=$PG_USER --dbname=$PG_DB --file=./"$SNAPSHOT_FILE" \
  --schema-only \
  --no-owner \
  --no-privileges \
  --exclude-table=schema_version
echo "Successfully dump only schema to $SNAPSHOT_FILE"

# run baseline: run before deploy/ or release.
# flyway -url=jdbc:postgresql://$PG_HOST:$PG_PORT/$PG_DB -user=$PG_USER -password=$PG_PASSWORD -initSql="$(cat $BASELINE_FILE)" migrate

# flyway migrate to verify
# flyway migrate
echo "Successfully consolidation!"
