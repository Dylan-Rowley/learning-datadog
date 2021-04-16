package ie.stockreporter.pubsub;

public abstract class Publisher {

    public abstract void publish(String message, String queueName);

}
