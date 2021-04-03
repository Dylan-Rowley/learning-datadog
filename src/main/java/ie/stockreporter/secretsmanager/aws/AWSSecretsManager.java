package ie.stockreporter.secretsmanager.aws;

import ie.stockreporter.secretsmanager.SecretsManager;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

public class AWSSecretsManager extends SecretsManager {

    @Override
    public String getSecret(String secretName, String endpoint, String region) {

        Region awsRegion = Region.US_EAST_1;

        SecretsManagerClient secretsClient = SecretsManagerClient.builder()
                .region(awsRegion)
                .build();

        GetSecretValueRequest valueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse valueResponse = secretsClient.getSecretValue(valueRequest);
        String secret = valueResponse.secretString();
        return secret;
    }

}
