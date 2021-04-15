package ie.stockreporter.services;

import ie.stockreporter.entities.CryptoTradingPair;
import ie.stockreporter.model.BidAsk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledBuyService {

    @Autowired
    private CryptoService cryptoService;

    /*
    * Query the DB (or api if DB empty) for the available crypto trading pairs.
    * Pick a random trading pair.
    * Call API to get latest Bid/Ask of that trading pair.
    * Push buy order for this crypto to order queue.
    */
    @Scheduled(fixedRate = 1000)
    public void createBuyOrder() throws Exception {

        CryptoTradingPair randomTradingPair = this.cryptoService.getRandomTradingPair();

        BidAsk bidAsk = this.cryptoService.getBidAndAskForTradingPair(randomTradingPair);

        System.out.println("Random Trading Pair is: " + randomTradingPair);
        System.out.println("Bid for Trading Pair: " + bidAsk.getBids());
        System.out.println("Ask for Trading Pair " + bidAsk.getAsks());
    }
}
