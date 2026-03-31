package support;

import model.BookingRequest;

import java.util.concurrent.ThreadLocalRandom;

public class DataFactory {

    public BookingRequest createValidBookingRequest() {

        // Generate unique booking number to avoid conflicts in tests
        int uniqueBookingNumber = ThreadLocalRandom.current().nextInt(100000, 999999);

        return new BookingRequest(
                uniqueBookingNumber,
                "John",
                3
        );
    }
}