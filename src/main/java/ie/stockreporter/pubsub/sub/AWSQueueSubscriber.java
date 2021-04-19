package ie.stockreporter.pubsub.sub;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import ie.stockreporter.pubsub.Subscriber;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AWSQueueSubscriber extends Subscriber {

    private static final Logger log = LogManager.getLogger("CONSOLE_JSON_APPENDER");

    @Override
    @Scheduled(initialDelay = 60000, fixedRate = 1250)
    public void subscribe() {

        log.info("Building SQS Client");
        AmazonSQS sqs = AmazonSQSClientBuilder.standard().withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration("http://localstack:4566", "us-east-1"))
                .build();

        log.info("Retrieving queue url for queue name orders");
        String queueUrl = sqs.getQueueUrl("orders").getQueueUrl();

        log.info("Retrieving messages from queue orders");
        List<Message> messages = sqs.receiveMessage(queueUrl).getMessages();

        messages.forEach(System.out::println);
    }
}
