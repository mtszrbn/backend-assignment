Feature: Booking request from Kafka to MongoDB

  I want to verify that a booking message consumed from Kafka
  is correctly stored in MongoDB
  So I can ensure the booking microservice processes messages end-to-end

  @test
  Scenario: Validate whether consumed message is stored in MongoDB or not
    Given valid booking request is prepared
    When booking request is published to Kafka topic "TEST.BOOKINGREQUEST.TOPIC"
    Then booking request should eventually be stored in MongoDB database "Testdb" collection "bookingrequest"
    And stored booking request should match the published request