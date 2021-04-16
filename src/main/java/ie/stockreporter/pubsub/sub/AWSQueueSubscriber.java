package ie.stockreporter.pubsub.sub;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import ie.stockreporter.pubsub.Subscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class AWSQueueSubscriber extends Subscriber {

    @Override
    @Scheduled(fixedRate = 1250)
    public void subscribe() {

        log.info("Building SQS Client");
        AmazonSQS sqs = AmazonSQSClientBuilder.standard().withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration("http://localhost:4566", "us-east-1"))
                .build();

        log.info("Retrieving queue url for queue name orders");
        String queueUrl = sqs.getQueueUrl("orders").getQueueUrl();

        log.info("Retrieving messages from queue orders");
        List<Message> messages = sqs.receiveMessage(queueUrl).getMessages();

        messages.forEach(System.out::println);
    }
}
