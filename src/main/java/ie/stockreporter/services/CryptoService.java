package ie.stockreporter.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.stockreporter.entities.CryptoTradingPair;
import ie.stockreporter.model.BidAsk;
import ie.stockreporter.model.Order;
import ie.stockreporter.repositories.CryptoTradingPairsRepository;
import ie.stockreporter.secretsmanager.SecretsManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CryptoService {

    @Autowired
    private CryptoTradingPairsRepository cryptoTradingPairsRepository;

    @Autowired
    private SecretsManager secretsManager;

    @Autowired
    private WebClient webClient;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger log = LogManager.getLogger("CONSOLE_JSON_APPENDER");

    public CryptoTradingPair getRandomTradingPair() {

        log.info("Getting a random trading pair");
        List<CryptoTradingPair> cryptoTradingPairs;
        CryptoTradingPair randomCryptoTradingPair;

        try {
            cryptoTradingPairs = this.getAllTradingPairs();
            Random random = new Random();
            int randomTradingPairIndex = random.nextInt(cryptoTradingPairs.size());
            randomCryptoTradingPair = cryptoTradingPairs.get(randomTradingPairIndex);
        } catch (Exception e) {
            log.error("Could not get retrieve a random trading pair");
            e.printStackTrace();
            randomCryptoTradingPair = CryptoTradingPair.builder().symbol("BTCUSD").build();
        }

        return randomCryptoTradingPair;
    }

    public List<CryptoTradingPair> getAllTradingPairs() throws Exception {

        log.info("Querying database for all available trading pairs.");
        List<CryptoTradingPair> cryptoTradingPairs = cryptoTradingPairsRepository.findAll();

        /*
        * We have not queried the API yet.
        * Call it and get all trading pairs.
        */
        if(cryptoTradingPairs.size() == 0) {
            log.info("No trading pairs present in database. Retrieving from API.");
            cryptoTradingPairs = this.getAllCryptoTradingPairsFromApi();
        }

        return cryptoTradingPairs;

    }

    private List<CryptoTradingPair> getAllCryptoTradingPairsFromApi() throws Exception {

        log.info("Retrieving API key from Secrets Manager");
        String iexCloudApiKey = secretsManager.getSecret("iex-cloud-api-key", "http://localstack:4566", "us-east-1");

        List<CryptoTradingPair> cryptoTradingPairs;

        if (iexCloudApiKey != null) {

            log.info("Calling API to retrieve all trading pairs.");
            Mono<Object[]> responseFromApi = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/ref-data/crypto/symbols")
                            .queryParam("token", iexCloudApiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(Object[].class).log();

            Object[] objects = responseFromApi.block();

            log.info("Retrieved trading pairs from API.");
             cryptoTradingPairs = Arrays.stream(objects)
                    .map(object -> objectMapper.convertValue(object, CryptoTradingPair.class))
                    .collect(Collectors.toList());

             log.info("Saving all trading pairs to database");
             this.saveCryptoTradingPairs(cryptoTradingPairs);


        } else {
            log.error("Could not retrieve API key.");
            throw new Exception("NO API KEY - DID LOCALSTACK RUN CORRECTLY??");
        }

        return cryptoTradingPairs;
    }

    public Order getOrderFor(CryptoTradingPair tradingPair) throws Exception {

        log.info("Generating random order for trading pair: {}", tradingPair);

        Random random = new Random();

        Order order = Order.builder()
                .tradingPair(tradingPair.getSymbol())
                .price(random.nextDouble() * random.nextInt(10000))
                .size(random.nextDouble() * random.nextInt(10000))
                .timestamp(LocalDateTime.now())
                .build();

        return order;
    }

    private void saveCryptoTradingPairs(List<CryptoTradingPair> cryptoTradingPairs) {
        this.cryptoTradingPairsRepository.saveAll(cryptoTradingPairs);
    }
}
