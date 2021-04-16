package ie.stockreporter.model;

import lombok.Builder;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@ToString
public class Order {
    private String tradingPair;
    private Double price;
    private Double size;
    private LocalDateTime timestamp;
}
