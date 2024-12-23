import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTests {

    // Positive Test Cases

    @Test
    @DisplayName("Find Viable Centres: With Metallic Waste")
    void testFindViableCentresWithMetallicWaste_TC_001() {
        // Arrange
        Historic historic = new Historic(Location.A, 2000);
        historic.setMetallic(500);
        List<Recycling> centres = List.of(
                new Alpha(Location.B, 5),
                new Gamma(Location.C, 10)
        );

        // Act
        List<Recycling> viableCentres = Utils.findViableCentres(historic, new ArrayList<>(centres));

        // Assert
        assertEquals(2, viableCentres.size(), "Both centres should be viable with metallic waste present");
    }

    @Test
    @DisplayName("Find Viable Centres: Without Metallic Waste")
    void testFindViableCentresWithoutMetallicWaste_TC_001() {
        // Arrange
        Historic historic = new Historic(Location.A, 2000);
        historic.setMetallic(0);
        List<Recycling> centres = List.of(
                new Alpha(Location.B, 5),
                new Beta(Location.C, 7),
                new Gamma(Location.B, 8)
        );

        // Act
        List<Recycling> viableCentres = Utils.findViableCentres(historic, new ArrayList<>(centres));

        // Assert
        assertEquals(2, viableCentres.size(), "Only Alpha and Beta centres should remain without metallic waste");
        assertTrue(viableCentres.stream().noneMatch(c -> c.getGeneration().equals("Gamma")), "Gamma centres should not be viable");
    }

    @Test
    @DisplayName("Find Optimal Centre: Valid Centres")
    void testFindOptimalCentre_TC_003() {
        // Arrange
        Historic historic = new Historic(Location.A, 1500);
        List<Recycling> centres = List.of(
                new Beta(Location.B, 10),
                new Gamma(Location.C, 5)
        );

        // Act
        Recycling optimalCentre = Utils.findOptimalCentre(historic, centres);

        // Assert
        assertEquals("Gamma", optimalCentre.getGeneration(), "Gamma should be the optimal centre");
    }

    @Test
    @DisplayName("Calculate Travel Duration: Valid Centre")
    void testCalculateTravelDuration_TC_003() {
        // Arrange
        Historic historic = new Historic(Location.A, 2000);
        Recycling recycling = new Alpha(Location.B, 5);

        // Act
        double travelDuration = Utils.calculateTravelDuration(historic, recycling);

        // Assert
        assertTrue(travelDuration > 0, "Travel duration should be greater than 0");
    }

    @Test
    @DisplayName("Calculate Process Duration: Valid Centre")
    void testCalculateProcessDuration_TC_004() {
        // Arrange
        Historic historic = new Historic(Location.A, 2000);
        Recycling recycling = new Gamma(Location.B, 5);

        // Act
        double processDuration = Utils.calculateProcessDuration(historic, recycling);

        // Assert
        assertTrue(processDuration > 0, "Process duration should be greater than 0");
    }

    // Negative Test Cases

    @Test
    @DisplayName("Find Viable Centres: Empty List")
    void testFindViableCentresWithEmptyList_TC_001() {
        // Arrange
        Historic historic = new Historic(Location.A, 1500);
        List<Recycling> centres = new ArrayList<>();

        // Act
        List<Recycling> viableCentres = Utils.findViableCentres(historic, centres);

        // Assert
        assertEquals(0, viableCentres.size(), "No viable centres should be found with an empty list");
    }

    @Test
    @DisplayName("Find Optimal Centre: Empty List")
    void testFindOptimalCentreWithEmptyList_TC_001() {
        // Arrange
        Historic historic = new Historic(Location.A, 2000);
        List<Recycling> centres = new ArrayList<>();

        // Act & Assert
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () ->
                Utils.findOptimalCentre(historic, centres)
        );
        assertNotNull(exception, "Exception should be thrown when no optimal centre can be found");
    }

    @Test
    @DisplayName("Calculate Travel Duration: Insufficient Waste")
    void testCalculateTravelDurationWithInsufficientWaste_TC_001() {
        // Arrange
        Historic historic = new Historic(Location.A, 15); // Less than transport capacity
        Recycling recycling = new Alpha(Location.B, 5);

        // Act
        double travelDuration = Utils.calculateTravelDuration(historic, recycling);

        // Assert
        assertEquals(-1.0, travelDuration, "Travel duration should be -1 for insufficient waste");
    }

    @Test
    @DisplayName("Calculate Process Duration: Zero Rates")
    void testCalculateProcessDurationWithZeroRates_TC_001() {
        // Arrange
        Historic historic = new Historic(Location.A, 2000);
        Recycling recycling = new Recycling(Location.B, 5) {
            @Override
            public String getGeneration() {
                return "Custom";
            }

            @Override
            public List<Double> getRates() {
                return List.of(0.0, 0.0, 0.0);
            }
        };

        // Act & Assert
        Exception exception = assertThrows(ArithmeticException.class, () ->
                Utils.calculateProcessDuration(historic, recycling)
        );
        assertNotNull(exception, "Exception should be thrown when rates are zero");
    }

    // Edge Case Test Cases

    @Test
    @DisplayName("Find Viable Centres: Boundary Travel Time")
    void testFindViableCentresWithBoundaryTravelTime_TC_001() {
        // Arrange
        Historic historic = new Historic(Location.A, 1500);
        List<Recycling> centres = List.of(
                new Beta(Location.B, 10),  // Travel time 2 hours
                new Gamma(Location.C, 5)  // Travel time 4 hours
        );

        // Act
        List<Recycling> viableCentres = Utils.findViableCentres(historic, new ArrayList<>(centres));

        // Assert
        assertEquals(1, viableCentres.size(), "Only Beta centre should remain due to travel time constraint");
    }

    @Test
    @DisplayName("Find Highest Generations: Mixed Generations")
    void testFindHighestGenerationsWithMixedGenerations_TC_001() {
        // Arrange
        List<Recycling> centres = List.of(
                new Alpha(Location.A, 5),
                new Beta(Location.B, 10),
                new Gamma(Location.C, 8)
        );

        // Act
        List<Recycling> highestGenerations = Utils.findHighestGenerations(centres);

        // Assert
        assertEquals(1, highestGenerations.size(), "Only the highest generation (Gamma) should be selected");
        assertEquals("Gamma", highestGenerations.get(0).getGeneration(), "Gamma should be the highest generation");
    }
}
