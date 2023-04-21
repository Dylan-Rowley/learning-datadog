# Purpose of Repository

Prior to joining Datadog, I wanted to experiment with their technology to understand if I was excited by it and in the end I was. 

# Application Overview 
A monolithic Spring Boot application that immitates the buying and selling of crypto currency trading pairs. 

The crypto currency trading pairs are obtained using IEX Cloud APIs. These are then persisted into a MySQL DB.

After a minute, a scheduled service queries a random trading pair and submits a buy/sell order to an SQS Queue (created using LocalStack).

Another service polls the messages in this SQS queue and just prints out the orders contained within it.

If you wish to see the logs/traces in your own Datadog Organisation, please generate an account and replace the API key in the docker-compose.yml 
with your personal one.

# Running the Application:
Clone the project

Build the jar: mvn clean install

Build the Dockefile: docker-compose build

Run the project: docker-compose up
