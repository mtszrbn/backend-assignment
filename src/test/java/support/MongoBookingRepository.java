package support;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import model.BookingDocument;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class MongoBookingRepository {

    private final MongoClient mongoClient;
    private final String databaseName;
    private final String collectionName;

    public MongoBookingRepository(String mongoUri, String databaseName, String collectionName) {
        this.mongoClient = MongoClients.create(mongoUri);
        this.databaseName = databaseName;
        this.collectionName = collectionName;
    }

    // Retrieves booking document by booking number
    public BookingDocument findByBookingNumber(int bookingNumber) {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> collection = database.getCollection(collectionName);
        Document doc = collection.find(eq("bookingnumber", bookingNumber)).first();
        if (doc == null) {
            return null;
        }

        // Map MongoDB document to domain object
        return new BookingDocument(
                doc.getInteger("bookingnumber"),
                doc.getString("name"),
                doc.getInteger("items")
        );
    }

    public void deleteByBookingNumber(int bookingNumber) {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> collection = database.getCollection(collectionName);

        collection.deleteMany(eq("bookingnumber", bookingNumber));
    }

    public void close() {
        mongoClient.close();
    }
}