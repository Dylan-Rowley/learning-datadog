package ie.stockreporter.services;

import ie.stockreporter.entities.CryptoTradingPair;
import ie.stockreporter.secretsmanager.SecretsManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import ie.stockreporter.model.TimeSeries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class IEXCloudService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private SecretsManager secretsManager;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger log = LogManager.getLogger("CONSOLE_JSON_APPENDER");


    public ResponseEntity<Object> getAllTimeSeries() {

        String iexCloudApiKey = secretsManager.getSecret("iex-cloud-api-key", "http://localstack:4566", "us-east-1");

        if (iexCloudApiKey != null) {

            Mono<Object[]> response = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/time-series")
                            .queryParam("token", iexCloudApiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(Object[].class).log();

            Object[] objects = response.block();

            List<TimeSeries> timeSeriesList = Arrays.stream(objects)
                    .map(object -> objectMapper.convertValue(object, TimeSeries.class))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(timeSeriesList);
        } else {
            return ResponseEntity.noContent().build();
        }

    }


    public ResponseEntity<Object> getAllCryptoSymbols() {

        log.info("Getting all trading pairs available from API");
        String iexCloudApiKey = secretsManager.getSecret("iex-cloud-api-key", "http://localstack:4566", "us-east-1");

        ResponseEntity<Object> responseEntity;

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

            List<CryptoTradingPair> cryptoSymbolsList = Arrays.stream(objects)
                    .map(object -> objectMapper.convertValue(object, CryptoTradingPair.class))
                    .collect(Collectors.toList());

            responseEntity = ResponseEntity.ok(cryptoSymbolsList);
        } else {
            responseEntity = new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }



        return responseEntity;
    }
}
