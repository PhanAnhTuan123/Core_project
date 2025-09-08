#!/bin/bash

set -euo pipefail

echo "Creating S3 buckets..."

buckets=("public-core-project-buckets")

endpoint_url="http://localhost:4566"
region="us-west-2"

for bucket_name in "${buckets[@]}"; do
  awslocal --endpoint-url="$endpoint_url" \
    s3api create-bucket \
    --bucket "$bucket_name" \
    --create-bucket-configuration LocationConstraint="$region" \
    --region "$region"

  awslocal --endpoint-url="$endpoint_url" \
    s3api put-bucket-cors \
    --bucket "$bucket_name" \
    --cors-configuration file://cors-config.json \
    --region "$region"

  echo "Bucket $bucket_name created."
done