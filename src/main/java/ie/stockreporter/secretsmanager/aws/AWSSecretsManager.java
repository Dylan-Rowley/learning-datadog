package ie.stockreporter.secretsmanager.aws;

import ie.stockreporter.secretsmanager.SecretsManager;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class AWSSecretsManager extends SecretsManager {

    @Override
    public String getSecret(String secretName, String endpoint, String region){

        Region awsRegion = Region.of(region);

        SecretsManagerClient secretsClient = null;
        try {
            secretsClient = SecretsManagerClient.builder()
                    .endpointOverride(new URI(endpoint))
                    .region(awsRegion)
                    .build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }

        GetSecretValueRequest valueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse valueResponse = secretsClient.getSecretValue(valueRequest);

        String secret = valueResponse.secretString();

        return secret;
    }

}
