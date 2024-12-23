package models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BetaTests {

    // Positive Test Cases
    @Test
    @DisplayName("Constructor: Validate Initialization with Valid Input")
    void testConstructorWithValidInput_TC_001() {
        // Arrange
        Beta beta = new Beta(Location.A, 5);

        // Act
        Location location = beta.getLocation();
        int yearsActive = beta.getYearsActive();
        String generation = beta.getGeneration();
        List<Double> rates = beta.getRates();

        // Assert
        assertEquals(Location.A, location, "Location should match the provided value");
        assertEquals(5, yearsActive, "Years active should match the provided value");
        assertEquals("Beta", generation, "Generation should be 'Beta'");
        assertEquals(List.of(1.5, 1.5, 1.5), rates, "Rates should match the default Beta rates");
    }

    @Test
    @DisplayName("Constructor: Validate Zero Years Active")
    void testConstructorWithZeroYearsActive_TC_002() {
        // Arrange
        Beta beta = new Beta(Location.B, 0);

        // Act
        int yearsActive = beta.getYearsActive();

        // Assert
        assertEquals(0, yearsActive, "Years active should be zero");
    }

    @Test
    @DisplayName("Constructor: Validate Large Years Active")
    void testConstructorWithLargeYearsActive_TC_002() {
        // Arrange
        int largeYearsActive = Integer.MAX_VALUE;
        Beta beta = new Beta(Location.C, largeYearsActive);

        // Act
        int yearsActive = beta.getYearsActive();

        // Assert
        assertEquals(largeYearsActive, yearsActive, "Years active should match the maximum integer value");
    }

    @Test
    @DisplayName("Rates Immutability: Validate Rates List Cannot Be Modified")
    void testRatesAreImmutable_TC_001() {
        // Arrange
        Beta beta = new Beta(Location.C, 10);

        // Act & Assert
        Exception exception = assertThrows(UnsupportedOperationException.class, () ->
                beta.getRates().add(2.0));

        assertNotNull(exception, "Rates list should be immutable");
    }

    // Negative Test Cases
    @Test
    @DisplayName("Constructor: Validate Null Location")
    void testConstructorWithNullLocation_TC_001() {
        // Arrange, Act & Assert
        Exception exception = assertThrows(NullPointerException.class, () ->
                new Beta(null, 5));

        assertNotNull(exception, "Constructor should throw NullPointerException for null location");
    }

    @Test
    @DisplayName("Constructor: Validate Negative Years Active")
    void testConstructorWithNegativeYearsActive_TC_002() {
        // Arrange, Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Beta(Location.A, -5));

        assertNotNull(exception, "Constructor should throw IllegalArgumentException for negative yearsActive");
    }

    // Edge Case Test Cases
    @Test
    @DisplayName("Constructor: Validate Boundary Years Active")
    void testConstructorWithBoundaryYearsActive_TC_001() {
        // Arrange
        int boundaryYearsActive = 1;
        Beta beta = new Beta(Location.B, boundaryYearsActive);

        // Act
        int yearsActive = beta.getYearsActive();

        // Assert
        assertEquals(boundaryYearsActive, yearsActive, "Years active should match the smallest positive integer");
    }

    @Test
    @DisplayName("Generation: Validate Fixed Value 'Beta'")
    void testGenerationWithSpecialCharacters_TC_001() {
        // Arrange
        Beta beta = new Beta(Location.C, 10);

        // Act
        String generation = beta.getGeneration();

        // Assert
        assertEquals("Beta", generation, "Generation should always be 'Beta'");
    }

    @Test
    @DisplayName("Constructor: Validate Extreme Years Active")
    void testConstructorWithExtremeYearsActive_TC_001() {
        // Arrange
        Beta beta = new Beta(Location.A, Integer.MAX_VALUE);

        // Act
        int yearsActive = beta.getYearsActive();

        // Assert
        assertEquals(Integer.MAX_VALUE, yearsActive, "Years active should handle extreme large values correctly");
    }
}
