import models.Location;
import models.Transport;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


public class TransportandLocationIntegrationTests {
    @Test
    void testTransportTravelTimeBetweenLocations() {
        // Arrange
        Transport transport = new Transport(Location.A, Location.B);

        // Act
        double travelTime = transport.getTravelTime();

        // Assert
        assertEquals(2.0, travelTime, "Travel time between Location A and Location B should be 2.0 hours");
    }


    @Test
    void testInvalidTravelTime() {
        // Arrange
        Transport transport = new Transport(Location.A, Location.A);

        // Act
        double travelTime = transport.getTravelTime();

        // Assert
        assertEquals(1.0, travelTime, "Travel time within the same location should default to 1.0 hour");
    }


    @Test
    void testTransportMaxTravelTime() {
        // Arrange
        Transport transport = new Transport(Location.A, Location.C);

        // Act
        double travelTime = transport.getTravelTime();

        // Assert
        assertEquals(4.0, travelTime, "Travel time between the farthest locations A and C should be 4.0 hours");
    }
















}