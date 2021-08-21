# Datadog Hiring Exercise
A monolithic Spring Boot application that immitates the buying and selling of crypto currency trading pairs. 

The crypto currency trading pairs are obtained using IEX Cloud APIs. These are then persisted into a MySQL DB.

After a minute, a scheduled service queries a random trading pair and submits a buy/sell order to an SQS Queue (created using LocalStack).

Another service polls the messages in this SQS queue and just prints out the orders contained within it.
