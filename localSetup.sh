#!/bin/bash

echo "Starting to create AWS Services"

echo "Creating new secret 'iex-cloud-api-key' "
awslocal secretsmanager create-secret --name iex-cloud-api-key --secret-string Tsk_852b9aad89504dae95cefb487bb4cd9f

echo "Creating Queue"
awslocal create-queue --queue-name orders

echo "END OF SCRIPT"


