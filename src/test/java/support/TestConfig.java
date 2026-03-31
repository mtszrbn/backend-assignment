package support;

public class TestConfig {

    // Kafka broker address
    private final String kafkaBootstrapServers = "localhost:9092";

    // MongoDB connection URI
    private final String mongoUri = "mongodb://localhost:27017";

    // MongoDB database name
    private final String mongoDatabase = "Testdb";

    // MongoDB collection name
    private final String mongoCollection = "bookingrequest";

    public String getKafkaBootstrapServers() {
        return kafkaBootstrapServers;
    }

    public String getMongoUri() {
        return mongoUri;
    }

    public String getMongoDatabase() {
        return mongoDatabase;
    }

    public String getMongoCollection() {
        return mongoCollection;
    }
}