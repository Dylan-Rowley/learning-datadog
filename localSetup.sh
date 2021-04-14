#!/bin/bash

echo "Starting to create AWS Services"

echo "Creating new secret 'iex-cloud-api-key' "
awslocal secretsmanager create-secret --name iex-cloud-api-key --secret-string Tsk_852b9aad89504dae95cefb487bb4cd9f
echo "Secret created"
echo "END OF SCRIPT"


#doesSecretExist="$(awslocal secretsmanager describe-secret --secret-id api-key --output json)"
#
## If it does not exist then c
## Create the secret using the API key
#if [[ $doesSecretExist == *"An error occurred (ResourceNotFoundException)"* ]]; then
#  echo "in if"
#fi


#if [[ $doesSecretExist == *"\"Name\": \"iex-cloud-api-key\""* ]]; then
#echo "Secret already exists. Continuing..."
#fi

