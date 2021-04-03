package ie.stockreporter.services;

import ie.stockreporter.secretsmanager.SecretsManager;
import ie.stockreporter.secretsmanager.aws.AWSSecretsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.awt.desktop.SystemSleepEvent;

@Service
public class IEXCloudService {

    @Value("${iex.cloud.api-key}")
    private String iexCloudApiKey;

    @Autowired
    private WebClient webClient;

    @Autowired
    private SecretsManager secretsManager;

    public ResponseEntity<Object> getAllTimeSeries() {

        String iexCloudApiKey = secretsManager.getSecret("iex-cloud-api-key", "", "us-east-1");

        String apiResponse =  webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                .path("/time-series")
                .queryParam("token", this.iexCloudApiKey)
                .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return ResponseEntity.ok(apiResponse);
    }


}
