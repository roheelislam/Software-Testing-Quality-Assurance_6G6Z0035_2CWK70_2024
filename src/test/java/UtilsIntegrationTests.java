import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UtilsIntegrationTests {

    // Positive Test Case: Valid generations
    @Test
    @DisplayName("Find Highest Generations: Valid Input")
    void testFindHighestGenerations_ValidInput_TC_001() {
        // Arrange
        List<Recycling> candidateCentres = new ArrayList<>();
        candidateCentres.add(new Alpha(Location.A, 5)); // Alpha
        candidateCentres.add(new Beta(Location.B, 3));  // Beta
        candidateCentres.add(new Gamma(Location.C, 7)); // Gamma

        // Act
        List<Recycling> highestGenerations = Utils.findHighestGenerations(candidateCentres);

        // Assert
        assertEquals(1, highestGenerations.size(), "Should return only Gamma centers.");
        assertEquals("Gamma", highestGenerations.get(0).getGeneration(), "The highest generation should be Gamma.");
    }

    // Negative Test Case: Empty list
    @Test
    @DisplayName("Find Highest Generations: Empty List")
    void testFindHighestGenerations_EmptyList_TC_002() {
        // Arrange
        List<Recycling> candidateCentres = new ArrayList<>();

        // Act
        List<Recycling> highestGenerations = Utils.findHighestGenerations(candidateCentres);

        // Assert
        assertTrue(highestGenerations.isEmpty(), "Should return an empty list for no candidate centres.");
    }

    // Negative Test Case: Null input
    @Test
    @DisplayName("Find Highest Generations: Null Input")
    void testFindHighestGenerations_NullInput_TC_001() {
        // Arrange
        List<Recycling> candidateCentres = null;

        // Act
        List<Recycling> highestGenerations = Utils.findHighestGenerations(candidateCentres);

        // Assert
        assertTrue(highestGenerations.isEmpty(), "Should return an empty list for null input.");
    }


    // Positive Test Case: Valid years active
    @Test
    @DisplayName("Find Least Years Active: Valid Input")
    void testFindLeastYearsActive_ValidInput_TC_001() {
        // Arrange
        List<Recycling> candidateCentres = new ArrayList<>();
        candidateCentres.add(new Alpha(Location.A, 5)); // 5 years active
        candidateCentres.add(new Beta(Location.B, 3));  // 3 years active
        candidateCentres.add(new Gamma(Location.C, 7)); // 7 years active

        // Act
        List<Recycling> leastYearsActive = Utils.findLeastYearsActive(candidateCentres);

        // Assert
        assertEquals(1, leastYearsActive.size(), "Should return the center with least years active.");
        assertEquals(3, leastYearsActive.get(0).getYearsActive(), "The least years active should be 3.");
    }

    // Negative Test Case: Empty list
    @Test
    @DisplayName("Find Least Years Active: Empty List")
    void testFindLeastYearsActive_EmptyList_TC_003() {
        // Arrange
        List<Recycling> candidateCentres = new ArrayList<>();

        // Act
        List<Recycling> leastYearsActive = Utils.findLeastYearsActive(candidateCentres);

        // Assert
        assertTrue(leastYearsActive.isEmpty(), "Should return an empty list for no candidate centres.");
    }

    // Negative Test Case: Null input
    @Test
    @DisplayName("Find Least Years Active: Null Input")
    void testFindLeastYearsActive_NullInput_TC_002() {
        // Arrange
        List<Recycling> candidateCentres = null;

        // Act
        List<Recycling> leastYearsActive = Utils.findLeastYearsActive(candidateCentres);

        // Assert
        assertTrue(leastYearsActive.isEmpty(), "Should return an empty list for null input.");
    }


    // Positive Test Case: Valid transport simulation
    @Test
    @DisplayName("Calculate Travel Duration: Valid Input")
    void testCalculateTravelDuration_ValidInput_TC_001() {
        // Arrange
        Historic historic = new Historic(Location.A, 50.0); // Waste > TRANSPORT_CAPACITY
        Recycling gamma = new Gamma(Location.B, 5); // Gamma center

        // Act
        double travelDuration = Utils.calculateTravelDuration(historic, gamma);

        // Assert
        assertTrue(travelDuration > 0, "Travel duration should be greater than 0 for valid inputs.");
    }

    // Negative Test Case: Waste below transport capacity
    @Test
    @DisplayName("Calculate Travel Duration: Insufficient Waste")
    void testCalculateTravelDuration_InsufficientWaste_TC_002() {
        // Arrange
        Historic historic = new Historic(Location.A, 10.0); // Waste < TRANSPORT_CAPACITY
        Recycling beta = new Beta(Location.B, 5); // Beta center

        // Act
        double travelDuration = Utils.calculateTravelDuration(historic, beta);

        // Assert
        assertEquals(-1.0, travelDuration, "Should return -1.0 for waste below transport capacity.");
    }

    // Negative Test Case: Null inputs
    @Test
    @DisplayName("Calculate Travel Duration: Null Inputs")
    void testCalculateTravelDuration_NullInputs_TC_003() {
        // Arrange
        Historic historic = null;
        Recycling beta = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> Utils.calculateTravelDuration(historic, beta),
                "Should throw NullPointerException for null inputs.");
    }

}
