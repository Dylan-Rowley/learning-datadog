package ie.stockreporter.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BeanConfiguration {

    @Bean
    public WebClient getWebClientForIEXCloud() {
        return WebClient.create("https://sandbox.iexapis.com/stable/");
    }
}
