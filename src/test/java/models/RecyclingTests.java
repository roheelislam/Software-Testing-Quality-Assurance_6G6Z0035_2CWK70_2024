package models;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Concrete subclass for testing Recycling
class ConcreteRecycling extends Recycling {
    private final String generation;
    private final List<Double> rates;

    public ConcreteRecycling(Location location, int yearsActive, String generation, List<Double> rates) {
        super(location, yearsActive);
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }
        if (yearsActive < 0) {
            throw new IllegalArgumentException("Years active cannot be negative");
        }
        if (generation == null || generation.trim().isEmpty()) {
            throw new IllegalArgumentException("Generation cannot be null or empty");
        }
        if (rates == null || rates.isEmpty()) {
            throw new IllegalArgumentException("Rates cannot be null or empty");
        }
        if (rates.stream().anyMatch(rate -> rate < 0)) {
            throw new IllegalArgumentException("Rates cannot contain negative values");
        }
        this.generation = generation;
        this.rates = rates;
    }

    @Override
    public String getGeneration() {
        return generation;
    }

    @Override
    public List<Double> getRates() {
        return rates;
    }
}

// JUnit 5 Test Cases
class RecyclingTests {

    // Positive Test Cases
    @Test
    void testValidYearsActive() {
        // Arrange
        Recycling recycling = new ConcreteRecycling(Location.A, 5, "Alpha", Arrays.asList(1.0, 1.5, 2.0));

        // Act
        int yearsActive = recycling.getYearsActive();

        // Assert
        assertEquals(5, yearsActive, "Years active should match the initialized value");
    }

    @Test
    void testValidGeneration() {
        // Arrange
        Recycling recycling = new ConcreteRecycling(Location.B, 8, "Beta", Arrays.asList(1.5, 1.5, 1.5));

        // Act
        String generation = recycling.getGeneration();

        // Assert
        assertEquals("Beta", generation, "Generation should match the initialized value");
    }

    @Test
    void testValidRates() {
        // Arrange
        List<Double> expectedRates = Arrays.asList(1.0, 2.0, 3.0);
        Recycling recycling = new ConcreteRecycling(Location.C, 15, "Gamma", expectedRates);

        // Act
        List<Double> rates = recycling.getRates();

        // Assert
        assertEquals(expectedRates, rates, "Rates should match the initialized values");
    }

    // Negative Test Cases
    @Test
    void testNullLocation() {
        // Arrange, Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new ConcreteRecycling(null, 5, "Alpha", Arrays.asList(1.0, 1.0, 1.0)));

        // Assert
        assertEquals("Location cannot be null", exception.getMessage());
    }

    @Test
    void testNegativeYearsActive() {
        // Arrange, Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new ConcreteRecycling(Location.A, -1, "Alpha", Arrays.asList(1.0, 1.0, 1.0)));

        // Assert
        assertEquals("Years active cannot be negative", exception.getMessage());
    }

    @Test
    void testNullGeneration() {
        // Arrange, Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new ConcreteRecycling(Location.B, 5, null, Arrays.asList(1.0, 1.0, 1.0)));

        // Assert
        assertEquals("Generation cannot be null or empty", exception.getMessage());
    }

    @Test
    void testEmptyGeneration() {
        // Arrange, Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new ConcreteRecycling(Location.C, 7, "", Arrays.asList(1.0, 1.5, 2.0)));

        // Assert
        assertEquals("Generation cannot be null or empty", exception.getMessage());
    }

    @Test
    void testNullRates() {
        // Arrange, Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new ConcreteRecycling(Location.B, 3, "Beta", null));

        // Assert
        assertEquals("Rates cannot be null or empty", exception.getMessage());
    }

    @Test
    void testEmptyRates() {
        // Arrange, Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new ConcreteRecycling(Location.C, 9, "Gamma", Collections.emptyList()));

        // Assert
        assertEquals("Rates cannot be null or empty", exception.getMessage());
    }

    @Test
    void testNegativeRateValue() {
        // Arrange, Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new ConcreteRecycling(Location.A, 10, "Alpha", Arrays.asList(-1.0, 1.5, 2.0)));

        // Assert
        assertEquals("Rates cannot contain negative values", exception.getMessage());
    }

    // Edge Case Test Cases
    @Test
    void testLargeYearsActive() {
        // Arrange
        int largeYearsActive = Integer.MAX_VALUE;
        Recycling recycling = new ConcreteRecycling(Location.C, largeYearsActive, "Gamma", Arrays.asList(1.0, 1.5, 2.0));

        // Act
        int yearsActive = recycling.getYearsActive();

        // Assert
        assertEquals(largeYearsActive, yearsActive, "Years active should handle large values correctly");
    }

    @Test
    void testSpecialCharactersInGeneration() {
        // Arrange
        String specialGeneration = "@Gamma!";
        Recycling recycling = new ConcreteRecycling(Location.A, 7, specialGeneration, Arrays.asList(1.0, 1.5, 2.0));

        // Act
        String generation = recycling.getGeneration();

        // Assert
        assertEquals(specialGeneration, generation, "Generation should handle special characters correctly");
    }

    @Test
    void testZeroRates() {
        // Arrange
        List<Double> zeroRates = Arrays.asList(0.0, 0.0, 0.0);
        Recycling recycling = new ConcreteRecycling(Location.B, 5, "Beta", zeroRates);

        // Act
        List<Double> rates = recycling.getRates();

        // Assert
        assertEquals(zeroRates, rates, "Rates should allow zero values");
    }
}
