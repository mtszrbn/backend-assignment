package steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import model.BookingDocument;
import model.BookingRequest;
import service.BookingTestFlow;
import support.KafkaPublisher;
import support.MongoBookingRepository;
import support.TestConfig;
import support.TestContext;
import support.DataFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookingStepDef {

    private TestContext context;
    private BookingTestFlow bookingTestFlow;
    private KafkaPublisher kafkaPublisher;
    private MongoBookingRepository mongoRepository;

    @Before
    public void setUp() {
        TestConfig config = new TestConfig();

        context = new TestContext();
        kafkaPublisher = new KafkaPublisher(config.getKafkaBootstrapServers());
        mongoRepository = new MongoBookingRepository(
                config.getMongoUri(),
                config.getMongoDatabase(),
                config.getMongoCollection()
        );

        bookingTestFlow = new BookingTestFlow(
                context,
                new DataFactory(),
                kafkaPublisher,
                mongoRepository,
                new ObjectMapper()
        );
    }

    @After
    public void tearDown() {
        bookingTestFlow.cleanup();
        kafkaPublisher.close();
        mongoRepository.close();
    }

    @Given("valid booking request is prepared")
    public void validBookingRequestIsPrepared() {
        bookingTestFlow.prepareBookingRequest();
    }

    @When("booking request is published to Kafka topic {string}")
    public void bookingRequestIsPublishedToKafkaTopic(String topicName) {
        bookingTestFlow.publishBookingRequest(topicName);
    }

    @Then("booking request should eventually be stored in MongoDB database {string} collection {string}")
    public void bookingRequestShouldEventuallyBeStoredInMongoDB(String databaseName, String collectionName) {
        bookingTestFlow.waitForBookingToBeStored();
    }

    @Then("stored booking request should match the published request")
    public void storedBookingRequestShouldMatchThePublishedRequest() {
        BookingRequest expected = context.getPublishedRequest();
        BookingDocument actual = context.getStoredDocument();

        assertNotNull(expected, "Expected booking request is missing");
        assertNotNull(actual, "Stored Mongo document is missing");

        assertEquals(expected.getBookingnumber(), actual.getBookingnumber());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getItems(), actual.getItems());
    }
}