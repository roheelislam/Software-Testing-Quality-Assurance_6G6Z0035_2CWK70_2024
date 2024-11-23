import models.Location;
import models.Transport;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class TransportandLocationIntegrationTests {


    //Positive Test Case: Calculate Travel Time Between Locations
    @Test
    void testTransportTravelTimeBetweenLocations() { // same as LocationTests
        // Arrange
        Transport transport = new Transport(Location.A, Location.B);

        // Act
        double travelTime = transport.getTravelTime();

        // Assert
        assertEquals(2.0, travelTime, "Travel time between Location A and Location B should be 2.0 hours");
    }


    //Negative Test Case: Invalid Travel Time Calculation
    @Test
    void testInvalidTravelTime() {
        // Arrange
        Transport transport = new Transport(Location.A, Location.A);

        // Act
        double travelTime = transport.getTravelTime();

        // Assert
        assertEquals(1.0, travelTime, "Travel time within the same location should default to 1.0 hour");
    }


    //Edge Case: Transport Across Maximum Locations
    @Test
    void testTransportMaxTravelTime() { //same as locationTest
        // Arrange
        Transport transport = new Transport(Location.A, Location.C);

        // Act
        double travelTime = transport.getTravelTime();

        // Assert
        assertEquals(4.0, travelTime, "Travel time between the farthest locations A and C should be 4.0 hours");
    }


}