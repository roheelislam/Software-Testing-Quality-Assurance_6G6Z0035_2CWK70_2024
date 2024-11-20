import models.Alpha;
import models.Location;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AlphaSpec {

    @Test
    public void testConstructorInitialization() {
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
    public void testGetGeneration() {
        // Arrange
        Alpha alphaRecycling = new Alpha(Location.B, 3);

        // Act
        String generation = alphaRecycling.getGeneration();

        // Assert
        assertEquals("Alpha", generation, "The generation should be 'Alpha'.");
    }

    @Test
    public void testGetRates() {
        // Arrange
        Alpha alphaRecycling = new Alpha(Location.C, 10);

        // Act
        List<Double> rates = alphaRecycling.getRates();

        // Assert
        List<Double> expectedRates = List.of(1.0, 1.0, 1.0);
        assertEquals(expectedRates, rates, "The rates should be [1.0, 1.0, 1.0].");
    }

    @Test
    public void testLocationConsistency() {
        // Arrange
        Location location = Location.B;
        Alpha alphaRecycling = new Alpha(location, 8);

        // Act
        Location retrievedLocation = alphaRecycling.getLocation();

        // Assert
        assertEquals(location, retrievedLocation, "The location should remain consistent across calls.");
    }
}
