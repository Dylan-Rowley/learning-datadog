package ie.stockreporter.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.stockreporter.entities.CryptoTradingPair;
import ie.stockreporter.model.Order;
import ie.stockreporter.pubsub.pub.AWSQueuePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduledBuyService {

    @Autowired
    private CryptoService cryptoService;

    @Autowired
    private AWSQueuePublisher awsQueuePublisher;

    private static final ObjectMapper objectMapper = new ObjectMapper();



    /*
    * Query the DB (or api if DB empty) for the available crypto trading pairs.
    * Pick a random trading pair.
    * Call API to get latest Bid/Ask of that trading pair.
    * Push buy order for this crypto to order queue.
    */
    @Scheduled(initialDelay = 60000, fixedRate = 1000)
    public void createBuyOrder() throws Exception {

        CryptoTradingPair randomTradingPair = this.cryptoService.getRandomTradingPair();

        Order order = this.cryptoService.getOrderFor(randomTradingPair);

        log.info("Sending Order to Queue: {}",objectMapper.writeValueAsString(order));

        awsQueuePublisher.publish(objectMapper.writeValueAsString(order), "orders");


    }
}
