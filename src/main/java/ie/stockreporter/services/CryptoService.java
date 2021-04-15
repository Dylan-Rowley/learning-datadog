package ie.stockreporter.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.stockreporter.entities.CryptoTradingPair;
import ie.stockreporter.model.BidAsk;
import ie.stockreporter.repositories.CryptoTradingPairsRepository;
import ie.stockreporter.secretsmanager.SecretsManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CryptoService {

    @Autowired
    private CryptoTradingPairsRepository cryptoTradingPairsRepository;

    @Autowired
    private SecretsManager secretsManager;

    @Autowired
    private WebClient webClient;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public CryptoTradingPair getRandomTradingPair() {

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

        List<CryptoTradingPair> cryptoTradingPairs = cryptoTradingPairsRepository.findAll();

        /*
        * We have not queried the API yet.
        * Call it and get all trading pairs.
        */
        if(cryptoTradingPairs.size() == 0) {
            cryptoTradingPairs = this.getAllCryptoTradingPairsFromApi();
        }

        return cryptoTradingPairs;

    }

    private List<CryptoTradingPair> getAllCryptoTradingPairsFromApi() throws Exception {

        String iexCloudApiKey = secretsManager.getSecret("iex-cloud-api-key", "http://localhost:4566", "us-east-1");

        List<CryptoTradingPair> cryptoTradingPairs;

        if (iexCloudApiKey != null) {

            Mono<Object[]> responseFromApi = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/ref-data/crypto/symbols")
                            .queryParam("token", iexCloudApiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(Object[].class).log();

            Object[] objects = responseFromApi.block();

             cryptoTradingPairs = Arrays.stream(objects)
                    .map(object -> objectMapper.convertValue(object, CryptoTradingPair.class))
                    .collect(Collectors.toList());

             this.saveCryptoTradingPairs(cryptoTradingPairs);


        } else {
            throw new Exception("NO API KEY - DID LOCALSTACK RUN CORRECTLY??");
        }

        return cryptoTradingPairs;
    }

    public BidAsk getBidAndAskForTradingPair(CryptoTradingPair tradingPair) throws Exception {

        String iexCloudApiKey = secretsManager.getSecret("iex-cloud-api-key", "http://localhost:4566", "us-east-1");

        String endpoint = String.format("/crypto/%s/book", tradingPair.getSymbol());

        BidAsk bidAndAsk;

        if (iexCloudApiKey != null) {

            Mono<Object> responseFromApi = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path(endpoint)
                            .queryParam("token", iexCloudApiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(Object.class).log();

            Object object = responseFromApi.block();

            bidAndAsk = objectMapper.convertValue(object, BidAsk.class);

        } else {
            throw new Exception("NO API KEY - DID LOCALSTACK RUN CORRECTLY??");
        }

        return bidAndAsk;
    }

    private void saveCryptoTradingPairs(List<CryptoTradingPair> cryptoTradingPairs) {
        this.cryptoTradingPairsRepository.saveAll(cryptoTradingPairs);
    }
}
