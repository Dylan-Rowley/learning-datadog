package ie.stockreporter.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@ToString
@Getter
@Setter
public class Order {
    private String tradingPair;
    private Double price;
    private Double size;
    private LocalDateTime timestamp;
}
