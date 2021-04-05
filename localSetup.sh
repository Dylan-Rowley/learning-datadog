#!/bin/bash


doesSecretExist=$(aws secretsmanager describe-secret --secret-id iex-cloud-api-key)



# If it does not exist then create the secret using the API key
if [[ $doesSecretExist == "An error occurred (ResourceNotFoundException)"* ]]; then
  awslocal secretsmanager create-secret --name iex-cloud-api-key --secret-string Tsk_852b9aad89504dae95cefb487bb4cd9f
fi


if [[ $doesSecretExist == *"\"Name\": \"iex-cloud-api-key\""* ]]; then
  echo "Secret already exists"
fi
