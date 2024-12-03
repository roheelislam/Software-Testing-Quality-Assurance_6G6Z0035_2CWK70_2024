package models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HistoricTests {

    // Positive Test Cases
    @Test
    void testConstructorWithValidInputs() {
        // Arrange
        Location location = Location.A;
        double initialWaste = 2000.0;

        // Act
        Historic historic = new Historic(location, initialWaste);

        // Assert
        assertEquals(location, historic.getLocation(), "Location should match the provided value");
        assertEquals(initialWaste, historic.getRemainingWaste(), "Remaining waste should match the initial waste");
        assertEquals(600.0, historic.getPlasticGlass(), "Plastic/Glass waste should be 30% of the initial waste");
        assertEquals(1000.0, historic.getPaper(), "Paper waste should be 50% of the initial waste");
        assertEquals(400.0, historic.getMetallic(), "Metallic waste should be 20% of the initial waste");
    }

    @Test
    void testConstructorWithWasteBelowThreshold() {
        // Arrange
        double initialWaste = 1000.0;

        // Act
        Historic historic = new Historic(Location.B, initialWaste);

        // Assert
        assertEquals(1000.0, historic.getRemainingWaste(), "Remaining waste should match the initial waste");
        assertEquals(500.0, historic.getPlasticGlass(), "Plastic/Glass waste should be 50% of the initial waste below the threshold");
        assertEquals(500.0, historic.getPaper(), "Paper waste should be 50% of the initial waste");
        assertEquals(0.0, historic.getMetallic(), "Metallic waste should be 0 below the threshold");
    }

    @Test
    void testSetRemainingWaste() {
        // Arrange
        Historic historic = new Historic(Location.C, 3000.0);

        // Act
        historic.setRemainingWaste(2500.0);

        // Assert
        assertEquals(2500.0, historic.getRemainingWaste(), "Remaining waste should be updated to the new value");
    }

    // Negative Test Cases
    @Test
    void testConstructorWithNegativeWaste() {
        // Arrange
        Location location = Location.A;
        double initialWaste = -200.0;

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Historic(location, initialWaste));
        assertEquals("Initial waste cannot be negative", exception.getMessage(), "Exception message should match");
    }

    @Test
    void testSetNegativeRemainingWaste() {
        // Arrange
        Historic historic = new Historic(Location.B, 2000.0);

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> historic.setRemainingWaste(-500.0));
        assertEquals("Remaining waste cannot be negative", exception.getMessage(), "Exception message should match");
    }

    @Test
    void testConstructorWithNullLocation() {
        // Arrange
        double initialWaste = 5000.0;

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Historic(null, initialWaste));
        assertEquals("Location cannot be null", exception.getMessage(), "Exception message should match");
    }

    // Edge Case Test Cases
    @Test
    void testConstructorWithZeroWaste() {
        // Arrange
        double initialWaste = 0.0;

        // Act
        Historic historic = new Historic(Location.C, initialWaste);

        // Assert
        assertEquals(0.0, historic.getRemainingWaste(), "Remaining waste should be zero");
        assertEquals(0.0, historic.getPlasticGlass(), "Plastic/Glass waste should be zero");
        assertEquals(0.0, historic.getPaper(), "Paper waste should be zero");
        assertEquals(0.0, historic.getMetallic(), "Metallic waste should be zero");
    }

    @Test
    void testSetMetallicValue() {
        // Arrange
        Historic historic = new Historic(Location.C, 5000.0);

        // Act
        historic.setMetallic(800.0);

        // Assert
        assertEquals(800.0, historic.getMetallic(), "Metallic waste should be updated to the new value");
    }

    // Subclass for Additional Testing
    static class MockHistoric extends Historic {

        public MockHistoric(Location location, double initialWaste) {
            super(location, initialWaste);
        }

        public double calculateTotalWaste() {
            return getPlasticGlass() + getPaper() + getMetallic();
        }
    }

    @Test
    void testMockHistoricTotalWasteCalculation() {
        // Arrange
        MockHistoric mockHistoric = new MockHistoric(Location.A, 3000.0);

        // Act
        double totalWaste = mockHistoric.calculateTotalWaste();

        // Assert
        assertEquals(3000.0, totalWaste, "Total waste should equal the sum of all waste types");
    }
}
