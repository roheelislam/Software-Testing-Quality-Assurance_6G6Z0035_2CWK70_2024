package models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

    // Positive Test Cases
    @Test
    void testTravelTimeFromAToB() {
        // Arrange
        Location from = Location.A;
        Location to = Location.B;

        // Act
        double travelTime = from.travelTime(to);

        // Assert
        assertEquals(2.0, travelTime, "Travel time between A and B should be 2.0 hours");
    }

    @Test
    void testTravelTimeFromBToC() {
        // Arrange
        Location from = Location.B;
        Location to = Location.C;

        // Act
        double travelTime = from.travelTime(to);

        // Assert
        assertEquals(3.0, travelTime, "Travel time between B and C should be 3.0 hours");
    }

    @Test
    void testTravelTimeFromAToC() {
        // Arrange
        Location from = Location.A;
        Location to = Location.C;

        // Act
        double travelTime = from.travelTime(to);

        // Assert
        assertEquals(4.0, travelTime, "Travel time between A and C should be 4.0 hours");
    }

    @Test
    void testTravelTimeWithinSameLocation() {
        // Arrange
        Location from = Location.C;
        Location to = Location.C;

        // Act
        double travelTime = from.travelTime(to);

        // Assert
        assertEquals(0.0, travelTime, "Travel time within the same location should be 0.0 hours");
    }

    // Negative Test Cases
    @Test
    void testTravelTimeFromNullLocation() {
        // Arrange
        Location from = null;
        Location to = Location.A;

        // Act & Assert
        Exception exception = assertThrows(NullPointerException.class, () -> from.travelTime(to));
        assertNotNull(exception, "Travel time from a null location should throw NullPointerException");
    }

    @Test
    void testTravelTimeToNullLocation() {
        // Arrange
        Location from = Location.B;
        Location to = null;

        // Act & Assert
        Exception exception = assertThrows(NullPointerException.class, () -> from.travelTime(to));
        assertNotNull(exception, "Travel time to a null location should throw NullPointerException");
    }

    // Edge Case Test Cases
    @Test
    void testBidirectionalTravelTimeEqualityAB() {
        // Arrange
        Location from = Location.A;
        Location to = Location.B;

        // Act
        double timeFromTo = from.travelTime(to);
        double timeToFrom = to.travelTime(from);

        // Assert
        assertEquals(timeFromTo, timeToFrom, "Travel time from A to B should equal travel time from B to A");
    }

    @Test
    void testBidirectionalTravelTimeEqualityAC() {
        // Arrange
        Location from = Location.A;
        Location to = Location.C;

        // Act
        double timeFromTo = from.travelTime(to);
        double timeToFrom = to.travelTime(from);

        // Assert
        assertEquals(timeFromTo, timeToFrom, "Travel time from A to C should equal travel time from C to A");
    }

    @Test
    void testBidirectionalTravelTimeEqualityBC() {
        // Arrange
        Location from = Location.B;
        Location to = Location.C;

        // Act
        double timeFromTo = from.travelTime(to);
        double timeToFrom = to.travelTime(from);

        // Assert
        assertEquals(timeFromTo, timeToFrom, "Travel time from B to C should equal travel time from C to B");
    }

    @Test
    void testMockLocationRoundTripTime() {
        // Arrange
        MockLocation mockLocation = new MockLocation(Location.A);
        Location destination = Location.B;

        // Act
        double roundTripTime = mockLocation.getRoundTripTime(destination);

        // Assert
        assertEquals(4.0, roundTripTime, "Round trip time between A and B should be 4.0 hours");
    }

    // Subclass for Additional Testing
    static class MockLocation {
        private final Location location;

        public MockLocation(Location location) {
            this.location = location;
        }

        public double getRoundTripTime(Location destination) {
            return location.travelTime(destination) * 2;
        }
    }
}
