package ie.stockreporter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StockReporterApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockReporterApplication.class, args);
	}

}
