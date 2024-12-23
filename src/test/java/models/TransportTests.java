package models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;


class TransportTests {

    // Positive Test Cases
    @Test
    @DisplayName("Constructor: Validate Locations Using Reflection")
    void testConstructorWithValidLocationsUsingReflection_TC_001() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        Location start = Location.A;
        Location end = Location.B;

        // Act
        Transport transport = new Transport(start, end);

        // Access private fields using reflection
        Field startField = Transport.class.getDeclaredField("start");
        Field endField = Transport.class.getDeclaredField("end");
        startField.setAccessible(true);
        endField.setAccessible(true);

        Location actualStart = (Location) startField.get(transport);
        Location actualEnd = (Location) endField.get(transport);

        // Assert
        assertEquals(start, actualStart, "Start location should match the initialized value");
        assertEquals(end, actualEnd, "End location should match the initialized value");
    }


    @Test
    @DisplayName("Set and Get Paper Waste: Validate Functionality")
    void testSetAndGetPaperWaste_TC_001() {
        // Arrange
        Transport transport = new Transport(Location.A, Location.C);

        // Act
        transport.setPaperWaste(500.0);
        double paperWaste = transport.getPaperWaste();

        // Assert
        assertEquals(500.0, paperWaste, "Paper waste should be set and retrieved correctly");
    }

    @Test
    @DisplayName("Set and Get Plastic/Glass Waste: Validate Functionality")
    void testSetAndGetPlasticGlassWaste_TC_001() {
        // Arrange
        Transport transport = new Transport(Location.B, Location.C);

        // Act
        transport.setPlasticGlassWaste(200.0);
        double plasticGlassWaste = transport.getPlasticGlassWaste();

        // Assert
        assertEquals(200.0, plasticGlassWaste, "Plastic/Glass waste should be set and retrieved correctly");
    }

    @Test
    @DisplayName("Set and Get Metallic Waste: Validate Functionality")
    void testSetAndGetMetallicWaste_TC_001() {
        // Arrange
        Transport transport = new Transport(Location.C, Location.A);

        // Act
        transport.setMetallicWaste(300.0);
        double metallicWaste = transport.getMetallicWaste();

        // Assert
        assertEquals(300.0, metallicWaste, "Metallic waste should be set and retrieved correctly");
    }

    @Test
    @DisplayName("Calculate Total Waste: Validate Sum of All Waste Types")
    void testGetTotalWaste_TC_001() {
        // Arrange
        Transport transport = new Transport(Location.A, Location.C);

        // Act
        transport.setPaperWaste(100.0);
        transport.setPlasticGlassWaste(200.0);
        transport.setMetallicWaste(300.0);
        double totalWaste = transport.getTotalWaste();

        // Assert
        assertEquals(600.0, totalWaste, "Total waste should equal the sum of all waste types");
    }

    @Test
    @DisplayName("Travel Time: Validate Between A and B")
    void testTravelTimeBetweenAAndB_TC_001() {
        // Arrange
        Transport transport = new Transport(Location.A, Location.B);

        // Act
        double travelTime = transport.getTravelTime();

        // Assert
        assertEquals(2.0, travelTime, "Travel time between A and B should be 2.0 hours");
    }

    @Test
    @DisplayName("Travel Time: Validate Within Same Location")
    void testTravelTimeWithinSameLocation_TC_002() {
        // Arrange
        Transport transport = new Transport(Location.A, Location.A);

        // Act
        double travelTime = transport.getTravelTime();

        // Assert
        assertEquals(1.0, travelTime, "Travel time within the same location should be 1.0 hour");
    }

    // Negative Test Cases
    @Test
    @DisplayName("Constructor: Validate Null Start Location Throws Exception")
    void testConstructorWithNullStartLocation_TC_001() {
        // Arrange
        Location start = null;
        Location end = Location.B;

        // Act & Assert
        Exception exception = assertThrows(NullPointerException.class, () -> new Transport(start, end));
        assertNotNull(exception, "Constructor should throw NullPointerException for null start location");
    }

    @Test
    @DisplayName("Constructor: Validate Null End Location Throws Exception")
    void testConstructorWithNullEndLocation_TC_001() {
        // Arrange
        Location start = Location.A;
        Location end = null;

        // Act & Assert
        Exception exception = assertThrows(NullPointerException.class, () -> new Transport(start, end));
        assertNotNull(exception, "Constructor should throw NullPointerException for null end location");
    }

    @Test
    @DisplayName("Set Paper Waste: Validate Negative Value Throws Exception")
    void testSetNegativePaperWaste_TC_001() {
        // Arrange
        Transport transport = new Transport(Location.A, Location.B);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> transport.setPaperWaste(-50.0));
        assertNotNull(exception, "Setting negative paper waste should throw IllegalArgumentException");
    }

    @Test
    @DisplayName("Set Plastic/Glass Waste: Validate Negative Value Throws Exception")
    void testSetNegativePlasticGlassWaste_TC_001() {
        // Arrange
        Transport transport = new Transport(Location.B, Location.C);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> transport.setPlasticGlassWaste(-30.0));
        assertNotNull(exception, "Setting negative plastic/glass waste should throw IllegalArgumentException");
    }

    @Test
    @DisplayName("Set Metallic Waste: Validate Negative Value Throws Exception")
    void testSetNegativeMetallicWaste_TC_001() {
        // Arrange
        Transport transport = new Transport(Location.C, Location.A);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> transport.setMetallicWaste(-20.0));
        assertNotNull(exception, "Setting negative metallic waste should throw IllegalArgumentException");
    }

    // Edge Case Test Cases
    @Test
    @DisplayName("Travel Time: Validate Between B and C")
    void testTravelTimeBetweenBAndC_TC_001() {
        // Arrange
        Transport transport = new Transport(Location.B, Location.C);

        // Act
        double travelTime = transport.getTravelTime();

        // Assert
        assertEquals(3.0, travelTime, "Travel time between B and C should be 3.0 hours");
    }

    @Test
    @DisplayName("Travel Time: Validate Between A and C")
    void testTravelTimeBetweenAAndC_TC_001() {
        // Arrange
        Transport transport = new Transport(Location.A, Location.C);

        // Act
        double travelTime = transport.getTravelTime();

        // Assert
        assertEquals(4.0, travelTime, "Travel time between A and C should be 4.0 hours");
    }

    @Test
    @DisplayName("Mock Transport: Validate Overloaded Status")
    void testMockTransportOverloaded_TC_001() {
        // Arrange
        MockTransport mockTransport = new MockTransport(Location.A, Location.B);
        mockTransport.setPaperWaste(500.0);
        mockTransport.setPlasticGlassWaste(700.0);
        mockTransport.setMetallicWaste(300.0);

        // Act
        boolean overloaded = mockTransport.isOverloaded(1000.0);

        // Assert
        assertTrue(overloaded, "Transport should be overloaded if total waste exceeds max capacity");
    }

    // Subclass for Additional Testing
    static class MockTransport extends Transport {
        public MockTransport(Location start, Location end) {
            super(start, end);
        }

        public boolean isOverloaded(double maxCapacity) {
            return getTotalWaste() > maxCapacity;
        }
    }
}
