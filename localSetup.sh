#!/bin/bash

echo "Starting to create AWS Services"
#wget https://s3.amazonaws.com/aws-cli/awscli-bundle.zip
#unzip awscli-bundle.zip
#./awscli-bundle/install -i /usr/local/aws -b /usr/local/bin/aws
#
#aws configure set aws_access_key_id 0000
#aws configure set aws_secret_access_key 0000
#aws configure set region us-west-1

echo "Creating new secret 'iex-cloud-api-key' "
awslocal secretsmanager create-secret --name iex-cloud-api-key --secret-string Tsk_852b9aad89504dae95cefb487bb4cd9f

echo "Creating Queue"
awslocal sqs create-queue --queue-name orders

echo "END OF SCRIPT"


