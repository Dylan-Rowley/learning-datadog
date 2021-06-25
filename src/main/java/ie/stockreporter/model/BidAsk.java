package ie.stockreporter.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BidAsk {
    private List<Order> bids;
    private List<Order> asks;
}
