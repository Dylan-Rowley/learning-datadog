package ie.stockreporter.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.stockreporter.model.TimeSeries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IEXCloudService {

    @Value("${iex.cloud.api-key}")
    private String iexCloudApiKey;

    @Autowired
    private WebClient webClient;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public ResponseEntity<Object> getAllTimeSeries() {

        Mono<Object[]> response =  webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                .path("/time-series")
                .queryParam("token", this.iexCloudApiKey)
                .build())
                .retrieve()
                .bodyToMono(Object[].class).log();

        Object[] objects = response.block();

        List<TimeSeries> timeSeriesList = Arrays.stream(objects)
                .map(object -> objectMapper.convertValue(object, TimeSeries.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(timeSeriesList);
    }


}
