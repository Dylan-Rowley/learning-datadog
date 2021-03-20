package ie.stockreporter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class IEXCloudService {


    @Autowired
    private WebClient webClient;

    public ResponseEntity<Object> getAllTimeSeries() {

        String apiResponse =  webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                .path("/time-series")
                .queryParam("token", "FAKE-KEY")
                .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("The Response from the API is: ");
        System.out.println(apiResponse);
        return ResponseEntity.ok(apiResponse);
    }


}
