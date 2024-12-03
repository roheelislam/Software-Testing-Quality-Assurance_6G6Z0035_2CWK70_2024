package models;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlphaTests {

    @Test
    void testConstructorInitialization() {
        // Arrange
        Location expectedLocation = Location.A;
        int expectedYearsActive = 5;

        // Act
        Alpha alphaRecycling = new Alpha(expectedLocation, expectedYearsActive);

        // Assert
        assertEquals(expectedLocation, alphaRecycling.getLocation(), "The location should be correctly initialized.");
        assertEquals(expectedYearsActive, alphaRecycling.getYearsActive(), "The years active should be correctly initialized.");
    }

    @Test
    void testGetGeneration() {
        // Arrange
        Alpha alphaRecycling = new Alpha(Location.B, 3);

        // Act
        String generation = alphaRecycling.getGeneration();

        // Assert
        assertEquals("Alpha", generation, "The generation should be 'Alpha'.");
    }

    @Test
    void testGetRates() {
        // Arrange
        Alpha alphaRecycling = new Alpha(Location.C, 10);

        // Act
        List<Double> rates = alphaRecycling.getRates();

        // Assert
        List<Double> expectedRates = List.of(1.0, 1.0, 1.0);
        assertEquals(expectedRates, rates, "The rates should be [1.0, 1.0, 1.0].");
    }

    @Test
    void testLocationConsistency() {
        // Arrange
        Location location = Location.B;
        Alpha alphaRecycling = new Alpha(location, 8);

        // Act
        Location retrievedLocation = alphaRecycling.getLocation();

        // Assert
        assertEquals(location, retrievedLocation, "The location should remain consistent across calls.");
    }

    
    // Edge Cases and Additional Tests

    @Test
    void testConstructorWithZeroYearsActive() {
        // Arrange
        int yearsActive = 0;

        // Act
        Alpha alphaRecycling = new Alpha(Location.A, yearsActive);

        // Assert
        assertEquals(yearsActive, alphaRecycling.getYearsActive(), "The years active should be initialized to 0.");
    }

    @Test
    void testConstructorWithNegativeYearsActive() {
        // Arrange
        int yearsActive = -5;

        // Act
        Alpha alphaRecycling = new Alpha(Location.B, yearsActive);

        // Assert
        assertEquals(yearsActive, alphaRecycling.getYearsActive(), "The years active should be initialized to the negative value.");
    }

    @Test
    void testRatesImmutability() {
        // Arrange
        Alpha alphaRecycling = new Alpha(Location.A, 5);

        // Act
        List<Double> rates = alphaRecycling.getRates();

        // Assert
        assertThrows(UnsupportedOperationException.class, () -> rates.add(2.0), "Rates list should be immutable.");
    }

    @Test
    void testLargeYearsActive() {
        // Arrange
        int yearsActive = Integer.MAX_VALUE;

        // Act
        Alpha alphaRecycling = new Alpha(Location.C, yearsActive);

        // Assert
        assertEquals(yearsActive, alphaRecycling.getYearsActive(), "The years active should handle large values correctly.");
    }

    @Test
    void testNullLocationInitialization() {
        // Arrange
        Location location = null;

        // Act
        Alpha alphaRecycling = new Alpha(location, 3);

        // Assert
        assertNull(alphaRecycling.getLocation(), "The location should be null when initialized with null.");
    }

    @Test
    void testGetGenerationConsistency() {
        // Arrange
        Alpha alphaRecycling = new Alpha(Location.B, 8);

        // Act
        String generation1 = alphaRecycling.getGeneration();
        String generation2 = alphaRecycling.getGeneration();

        // Assert
        assertEquals(generation1, generation2, "The generation value should be consistent across multiple calls.");
    }

    @Test
    void testMultipleInstancesIndependence() {
        // Arrange
        Alpha alpha1 = new Alpha(Location.A, 5);
        Alpha alpha2 = new Alpha(Location.B, 10);

        // Act
        Location location1 = alpha1.getLocation();
        Location location2 = alpha2.getLocation();

        // Assert
        assertNotEquals(location1, location2, "Different Alpha instances should have independent locations.");
    }

    @Test
    void testRatesContent() {
        // Arrange
        Alpha alphaRecycling = new Alpha(Location.C, 5);

        // Act
        List<Double> rates = alphaRecycling.getRates();

        // Assert
        assertAll(
                () -> assertEquals(1.0, rates.get(0), "The first rate should be 1.0."),
                () -> assertEquals(1.0, rates.get(1), "The second rate should be 1.0."),
                () -> assertEquals(1.0, rates.get(2), "The third rate should be 1.0."),
                () -> assertEquals(3, rates.size(), "The rates list should contain exactly 3 elements.")
        );
    }


}
