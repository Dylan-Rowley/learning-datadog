package ie.stockreporter.pubsub.pub;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import ie.stockreporter.pubsub.Publisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class AWSQueuePublisher extends Publisher {

    @Override
    public void publish(String message, String queueName) {

        log.info("Retrieving queue url for queue name {}", queueName);
        AmazonSQS sqs = AmazonSQSClientBuilder.standard().withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration("http://localstack:4566", "us-east-1"))
                .build();

        String queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
        log.info("Queue url is {}", queueName);

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(message);

        log.info("Sending message to queue");
        sqs.sendMessage(send_msg_request);

    }
}
