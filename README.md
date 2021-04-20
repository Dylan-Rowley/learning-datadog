# Datadog Hiring Exercise
A monolithic Spring Boot application that periodically,
through scheduled tasks, retrieves a random crypto currency trading pair
and submits an order for this pair to a SQS queue.

The orders are then pulled from this queue periodically and simply printed
to mimick the execution of an order. 

The cryptocurrency trading pairs are retrieved using APIs from IEX Cloud.
See here: https://iexcloud.io/.

Localstack is used to model AWS Simple Queue Service and AWS Secrets Manager.
See here: https://github.com/localstack/localstack

To Run the Application:

1. Create the network the agent and application container will share:
   docker network create datadog-network
   
2. Run the Datadog Agent:
   docker run -d --name datadog-agent \
   	--network datadog-network \
      -e DD_API_KEY=b84b544d6ae68812ccfef6600dafb92c \
      -e DD_LOGS_ENABLED=true \
      -e DD_LOGS_CONFIG_CONTAINER_COLLECT_ALL=true \
      -e DD_CONTAINER_EXCLUDE_LOGS="name:datadog-agent" \
      -e DD_APM_ENABLED=true \
      -e DD_APM_NON_LOCAL_TRAFFIC=true \
      -e DD_SITE="datadoghq.eu"  \
      -v /var/run/docker.sock:/var/run/docker.sock:ro \
      -v /proc/:/host/proc/:ro \
      -v /opt/datadog-agent/run:/opt/datadog-agent/run:rw \
      -v /sys/fs/cgroup/:/host/sys/fs/cgroup:ro \
      datadog/agent:latest

3. Finally, in the root directory of the project /stock-reports, run:
   docker-compose up
   
4. The application will now run. Please read on for more information.
   
Using the Dockerfile, the image is built for the Spring Boot application. Using system
properties defined by ENTRYPOINT in the Dockerfile, the Datadog agent is configured to
instrument the application.

A script, ./localSetup.sh, is bind mounted into the localstack container. This 
stores an api key called iex-cloud-api-key in AWS Secrets Manager. It also creates
and SQS queue called 'orders'. The API key is for a sandbox account, so no errors or limits should be
encountered.

Another script, ./init.sql, is also bindmounted into the MySQL container. This will create 
a single table to store all the cryptocurrency trading pairs.

After 1 minute, the scheduled tasks begin to run. This simulates the querying of cryptocurrency prices 
and execution of orders. On the first execution of the application, the IEX Cloud API will be called
due to no Cryptocurrency trading pairs being present in the DB. These are stored in a volume for future 
use. 


