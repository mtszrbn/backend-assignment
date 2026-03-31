package support;

import model.BookingDocument;
import model.BookingRequest;

public class TestContext {

    private BookingRequest publishedRequest;
    private String publishedPayload;
    private BookingDocument storedDocument;

    public BookingRequest getPublishedRequest() {
        return publishedRequest;
    }

    public void setPublishedRequest(BookingRequest publishedRequest) {
        this.publishedRequest = publishedRequest;
    }

    public String getPublishedPayload() {
        return publishedPayload;
    }

    public void setPublishedPayload(String publishedPayload) {
        this.publishedPayload = publishedPayload;
    }

    public BookingDocument getStoredDocument() {
        return storedDocument;
    }

    public void setStoredDocument(BookingDocument storedDocument) {
        this.storedDocument = storedDocument;
    }
}