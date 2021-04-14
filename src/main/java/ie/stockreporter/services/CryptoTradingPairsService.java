package ie.stockreporter.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.stockreporter.entities.CryptoTradingPairs;
import ie.stockreporter.repositories.CryptoTradingPairsRepository;
import ie.stockreporter.secretsmanager.SecretsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CryptoTradingPairsService {

    @Autowired
    private CryptoTradingPairsRepository cryptoTradingPairsRepository;

    @Autowired
    private SecretsManager secretsManager;

    @Autowired
    private WebClient webClient;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public List<CryptoTradingPairs> getAllTradingPairs() throws Exception {

        List<CryptoTradingPairs> cryptoTradingPairs = cryptoTradingPairsRepository.findAll();

        /*
        * We have not queried the API yet.
        * Call it and get all trading pairs.
        */
        if(cryptoTradingPairs.size() == 0) {
            cryptoTradingPairs = this.getAllCryptoTradingPairsFromApi();
        }

        return cryptoTradingPairs;

    }

    private List<CryptoTradingPairs> getAllCryptoTradingPairsFromApi() throws Exception {

        String iexCloudApiKey = secretsManager.getSecret("iex-cloud-api-key", "http://localhost:4566", "us-east-1");

        List<CryptoTradingPairs> cryptoTradingPairs;

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
                    .map(object -> objectMapper.convertValue(object, CryptoTradingPairs.class))
                    .collect(Collectors.toList());

             this.saveCryptoTradingPairs(cryptoTradingPairs);


        } else {
            throw new Exception("NO API KEY - DID LOCALSTACK RUN CORRECTLY??");
        }

        return cryptoTradingPairs;

    }

    private void saveCryptoTradingPairs(List<CryptoTradingPairs> cryptoTradingPairs) {
        this.cryptoTradingPairsRepository.saveAll(cryptoTradingPairs);
    }
}
