package ie.stockreporter.services;

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

    public ResponseEntity<Object> getAllTimeSeries() {

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
