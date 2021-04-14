package ie.stockreporter.repositories;

import ie.stockreporter.entities.CryptoTradingPairs;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CryptoTradingPairsRepository extends CrudRepository<CryptoTradingPairs, Long> {

    List<CryptoTradingPairs> findAll();
}
