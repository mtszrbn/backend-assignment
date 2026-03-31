package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.BookingDocument;
import model.BookingRequest;
import org.awaitility.Awaitility;
import support.KafkaPublisher;
import support.MongoBookingRepository;
import support.TestContext;
import support.DataFactory;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookingTestFlow {

    private final TestContext context;
    private final DataFactory testDataFactory;
    private final KafkaPublisher kafkaPublisher;
    private final MongoBookingRepository mongoRepository;
    private final ObjectMapper objectMapper;

    public BookingTestFlow(TestContext context,
                           DataFactory testDataFactory,
                           KafkaPublisher kafkaPublisher,
                           MongoBookingRepository mongoRepository,
                           ObjectMapper objectMapper) {
        this.context = context;
        this.testDataFactory = testDataFactory;
        this.kafkaPublisher = kafkaPublisher;
        this.mongoRepository = mongoRepository;
        this.objectMapper = objectMapper;
    }

    // Prepare valid booking request and convert it to JSON payload
    public void prepareBookingRequest() {
        try {
            // Create a valid booking request object using test data factory
            BookingRequest request = testDataFactory.createValidBookingRequest();

            // Serialize request object to JSON string
            String payload = objectMapper.writeValueAsString(request);

            // Store both object and JSON payload in shared test context
            context.setPublishedRequest(request);
            context.setPublishedPayload(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to prepare booking request payload", e);
        }
    }

    // Publish prepared booking request to Kafka
    public void publishBookingRequest(String topicName) {
        // Retrieve request from context
        BookingRequest request = context.getPublishedRequest();

        // Ensure request was prepared before publishing
        assertNotNull(request, "Booking request must be prepared before publishing");

        // Use booking number as Kafka message key
        String key = String.valueOf(request.getBookingnumber());

        // Send message to Kafka topic
        kafkaPublisher.publish(topicName, key, context.getPublishedPayload());
    }

    // Wait until booking is stored in MongoDB
    public void waitForBookingToBeStored() {
        BookingRequest request = context.getPublishedRequest();

        // Ensure request exists before waiting
        assertNotNull(request, "Published booking request is missing");

        // Use Awaitility to poll MongoDB until the booking appears
            Awaitility.await()
                .atMost(Duration.ofSeconds(20))
                .pollInterval(Duration.ofMillis(500))
                .ignoreExceptions()
                .untilAsserted(() -> {
                    // Try to fetch booking document from MongoDB
                    BookingDocument stored = mongoRepository
                            .findByBookingNumber(request.getBookingnumber());

                    // Assert that booking is now stored
                    assertNotNull(stored, "Booking request was not stored in MongoDB yet");

                    // Save retrieved document in context for further assertions
                    context.setStoredDocument(stored);
                });
    }

    // Cleanup test data from MongoDB after test execution
    public void cleanup() {
        BookingRequest request = context.getPublishedRequest();
        if (request != null) {
            mongoRepository.deleteByBookingNumber(request.getBookingnumber());
        }
    }
}