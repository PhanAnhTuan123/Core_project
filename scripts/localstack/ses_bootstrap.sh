#!/bin/bash

set -euo pipefail

endpoint_url="http://localhost.localstack.cloud:4566"
region="us-west-2"
email="phantuan7a5@gmail.com"

awslocal --endpoint-url="$endpoint_url" ses verify-email-identity --email-address "$email" --region "$region"
awslocal ses list-identity --region "$region"