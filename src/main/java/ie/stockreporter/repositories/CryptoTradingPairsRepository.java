package ie.stockreporter.repositories;

import ie.stockreporter.entities.CryptoTradingPair;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CryptoTradingPairsRepository extends CrudRepository<CryptoTradingPair, Long> {

    List<CryptoTradingPair> findAll();

}
