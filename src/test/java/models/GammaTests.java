package models;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Test Suite for Gamma Class
class GammaTests {

    // Positive Test Cases
    @Test
    void testValidConstructor() {
        // Arrange
        Gamma gamma = new Gamma(Location.A, 5);

        // Act
        Location location = gamma.getLocation();
        int yearsActive = gamma.getYearsActive();
        String generation = gamma.getGeneration();
        List<Double> rates = gamma.getRates();

        // Assert
        assertEquals(Location.A, location, "Location should match the provided value");
        assertEquals(5, yearsActive, "Years active should match the provided value");
        assertEquals("Gamma", generation, "Generation should be 'Gamma'");
        assertEquals(List.of(1.5, 2.0, 3.0), rates, "Rates should match the default Gamma rates");
    }

    @Test
    void testZeroYearsActive() {
        // Arrange
        Gamma gamma = new Gamma(Location.B, 0);

        // Act
        int yearsActive = gamma.getYearsActive();

        // Assert
        assertEquals(0, yearsActive, "Years active should be zero");
    }

    @Test
    void testLargeYearsActive() {
        // Arrange
        int largeYearsActive = Integer.MAX_VALUE;
        Gamma gamma = new Gamma(Location.C, largeYearsActive);

        // Act
        int yearsActive = gamma.getYearsActive();

        // Assert
        assertEquals(largeYearsActive, yearsActive, "Years active should handle large values correctly");
    }

    @Test
    void testRatesAreImmutable() {
        // Arrange
        Gamma gamma = new Gamma(Location.A, 5);

        // Act & Assert
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            gamma.getRates().add(4.0); // Attempt to modify the immutable rates list
        });

        assertNotNull(exception, "Rates list should be immutable");
    }

    // Negative Test Cases
    @Test
    void testConstructorWithNullLocation() {
        // Arrange
        Gamma gamma = new Gamma(null, 5);

        // Act
        Location location = gamma.getLocation();

        // Assert
        assertNull(location, "Location should be null when null is passed to the constructor");
    }

    @Test
    void testConstructorWithNegativeYearsActive() {
        // Arrange
        Gamma gamma = new Gamma(Location.A, -5);

        // Act
        int yearsActive = gamma.getYearsActive();

        // Assert
        assertEquals(-5, yearsActive, "Years active should be negative if no validation exists");
    }

    // Edge Case Test Cases
    @Test
    void testConstructorWithBoundaryYearsActive() {
        // Arrange
        Gamma gamma = new Gamma(Location.C, 1);

        // Act
        int yearsActive = gamma.getYearsActive();

        // Assert
        assertEquals(1, yearsActive, "Years active should handle the smallest positive integer correctly");
    }

    @Test
    void testGenerationConsistency() {
        // Arrange
        Gamma gamma = new Gamma(Location.B, 15);

        // Act
        String generation = gamma.getGeneration();

        // Assert
        assertEquals("Gamma", generation, "Generation should always return 'Gamma'");
    }

    @Test
    void testRatesMatchExpectedOrder() {
        // Arrange
        Gamma gamma = new Gamma(Location.A, 10);

        // Act
        List<Double> rates = gamma.getRates();

        // Assert
        assertEquals(List.of(1.5, 2.0, 3.0), rates, "Rates should match [plastic, metallic, paper] in the correct order");
    }


}
