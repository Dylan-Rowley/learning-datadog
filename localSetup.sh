#!/bin/bash


# Recreate the secret
awslocal secretsmanager create-secret --name iex-cloud-api-key --secret-string Tsk_852b9aad89504dae95cefb487bb4cd9f
