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

    @Autowired
    private WebClient webClient;

    @Autowired
    private SecretsManager secretsManager;

    public ResponseEntity<Object> getAllTimeSeries() {

        String iexCloudApiKey = secretsManager.getSecret("iex-cloud-api-key", "http://localhost:4566", "us-east-1");

        String apiResponse = "";
        if(iexCloudApiKey != null) {

            apiResponse =  webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/time-series")
                            .queryParam("token", iexCloudApiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        }
        
        return ResponseEntity.ok(apiResponse);
    }


}
