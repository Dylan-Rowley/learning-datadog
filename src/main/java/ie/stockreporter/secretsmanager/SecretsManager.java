package ie.stockreporter.secretsmanager;

public abstract class SecretsManager {

    public abstract String getSecret(String secretName, String endpoint, String region);
}
