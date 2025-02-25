import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MainScenarioConfigUtilsIntegrationTests {


    //Positive Test Cases

    //Test Scenario Configuration with Valid Inputs
    @Test
    @DisplayName("Scenario Configuration: Valid Inputs")
    void testScenarioConfiguration_ValidInputs_TC_001() {
        //Arrange:
        //Create a valid Historic site with initial waste (3000 meters cubed) at Location.A.
        //Create a list of Recycling centers: 1 Alpha, 1 Beta, and 1 Gamma, all at different locations with valid years active.
        Historic historic = new Historic(Location.A, 3000.0);
        List<Recycling> recycling = List.of(
                new Alpha(Location.B, 5),
                new Beta(Location.C, 10),
                new Gamma(Location.A, 15)
        );

        //Act: Call configureScenario() to build the ScenarioConfiguration.
        ScenarioConfiguration config = new ScenarioConfiguration(historic, recycling);

        //Assert: Ensure the ScenarioConfiguration object contains the Historic site and the list of Recycling centers.
        assertNotNull(config.getHistoric());
        assertEquals(3, config.getRecycling().size());
    }


    @Test
    @DisplayName("Run Scenario: Valid Inputs")
    void testRunScenarioWithValidInputs_TC_002() {
        // Arrange
        Historic historic = new Historic(Location.A, 1000); // Valid initial waste
        Recycling recycling1 = new Alpha(Location.B, 5); // Valid Recycling center
        Recycling recycling2 = new Beta(Location.C, 3);
        List<Recycling> recyclingCenters = new ArrayList<>(List.of(recycling1, recycling2));

        // Act
        List<Recycling> viableCenters = Utils.findViableCentres(historic, recyclingCenters);
        Recycling optimalCenter = Utils.findOptimalCentre(historic, viableCenters);
        double travelDuration = Utils.calculateTravelDuration(historic, optimalCenter);
        double processDuration = Utils.calculateProcessDuration(historic, optimalCenter);

        // Assert
        assertNotNull(viableCenters, "Viable centers should not be null");
        assertFalse(viableCenters.isEmpty(), "There should be at least one viable center");
        assertNotNull(optimalCenter, "Optimal center should be determined");
        assertTrue(travelDuration > 0, "Travel duration should be greater than 0");
        assertTrue(processDuration > 0, "Process duration should be greater than 0");
    }


    @Test
    @DisplayName("Scenario with Multiple Generations")
    void testScenarioWithMultipleGenerations_TC_001() {
        // Arrange
        Historic historic = new Historic(Location.B, 2000); // Above metallic threshold
        Recycling alpha = new Alpha(Location.A, 8);
        Recycling beta = new Beta(Location.C, 3);
        Recycling gamma = new Gamma(Location.A, 2); // Gamma expected to be excluded
        List<Recycling> recyclingCenters = new ArrayList<>(List.of(alpha, beta, gamma));

        // Act
        List<Recycling> viableCenters = Utils.findViableCentres(historic, recyclingCenters);

        // Assert
        assertEquals(2, viableCenters.size(), "Gamma centers should be excluded due to metallic conditions");
    }


    //Test Running a Scenario with Viable Recycling Centers
    @Test
    @DisplayName("Run Scenario: Viable Recycling Centers")
    void testRunScenario_WithValidViableCenters_TC_001() {
        //Arrange:
        //Set up a Historic site with enough waste for transportation.
        //Add a mix of Alpha, Beta, and Gamma recycling centers.
        Historic historic = new Historic(Location.A, 1500.0);
        List<Recycling> recycling = List.of(
                new Alpha(Location.B, 5),
                new Beta(Location.C, 2),
                new Gamma(Location.A, 8)
        );

        //Act: Call Utils.findViableCentres() and Utils.findOptimalCentre().
        List<Recycling> viableCenters = Utils.findViableCentres(historic, new ArrayList<>(recycling));
        Recycling optimalCenter = Utils.findOptimalCentre(historic, viableCenters);

        //Assert:
        //Ensure viable centers include only those within travel constraints.
        //Ensure the optimal center is selected correctly.
        assertEquals(2, viableCenters.size()); // Gamma excluded if no metallic waste
        assertNotNull(optimalCenter);
        assertEquals("Alpha", optimalCenter.getGeneration());
    }


    //Test Travel and Process Duration Calculations
    @Test
    @DisplayName("Travel and Process Duration Calculations")
    void testCalculateDurations_ValidInputs_TC_003() {
        //Arrange: Set up a valid Historic site and an Alpha recycling center.
        Historic historic = new Historic(Location.A, 2000.0);
        Recycling alpha = new Alpha(Location.B, 10);

        //Act: Call Utils.calculateTravelDuration() and Utils.calculateProcessDuration().
        double travelTime = Utils.calculateTravelDuration(historic, alpha);
        double processTime = Utils.calculateProcessDuration(historic, alpha);

        //Assert: Validate travel time and process duration.
        assertTrue(travelTime > 0);
        assertTrue(processTime > 0);
        assertEquals(2.0, travelTime, 0.01); // Location A -> B
    }


    //Negative Test Cases

    //Test Scenario Configuration with Null Historic Site
    @Test
    @DisplayName("Scenario Configuration: Null Historic Site")
    void testScenarioConfiguration_NullHistoric_TC_001() {
        //Arrange: Use a null Historic site.
        List<Recycling> recycling = List.of(new Alpha(Location.B, 5));

        //Act: Attempt to create a ScenarioConfiguration.
        //Assert: Ensure NullPointerException or appropriate error is thrown.
        assertThrows(NullPointerException.class, () -> {
            new ScenarioConfiguration(null, recycling);
        });
    }


    @Test
    @DisplayName("Scenario Configuration: Empty Inputs")
    void testScenarioConfigurationWithEmptyInputs_TC_001() {
        // Arrange
        ScenarioConfiguration scenario = new ScenarioConfiguration();

        // Act & Assert
        assertNull(scenario.getHistoric(), "Historic site should initially be null");
        assertEquals(0, scenario.getRecycling().size(), "Recycling centers should initially be empty");
    }


    //Test Find Viable Centers with Empty List
    @Test
    @DisplayName("Find Viable Centers: Empty List")
    void testFindViableCenters_EmptyList_TC_001() {
        //Arrange: Use a Historic site but an empty list of Recycling centers.
        Historic historic = new Historic(Location.A, 1000.0);
        List<Recycling> recycling = new ArrayList<>();

        //Act: Call Utils.findViableCentres().
        List<Recycling> viableCenters = Utils.findViableCentres(historic, recycling);

        //Assert: Ensure the result is an empty list.
        assertTrue(viableCenters.isEmpty());
    }


    //Test Travel Duration with Insufficient Waste
    @Test
    @DisplayName("Travel Duration: Insufficient Waste")
    void testCalculateTravelDuration_InsufficientWaste_TC_001() {
        //Arrange: Create a Historic site with waste less than transport capacity.
        Historic historic = new Historic(Location.A, 10.0);
        Recycling alpha = new Alpha(Location.B, 10);

        //Act: Call Utils.calculateTravelDuration().
        double travelTime = Utils.calculateTravelDuration(historic, alpha);

        //Assert: Ensure the result is -1.0.
        assertEquals(-1.0, travelTime);
    }


    @Test
    @DisplayName("Travel Duration: Insufficient Transport Capacity")
    void testTravelDurationWithInsufficientTransportCapacity_TC_001() {
        // Arrange
        Historic historic = new Historic(Location.A, 10); // Below transport capacity
        Recycling recycling = new Beta(Location.B, 5);

        // Act
        double travelDuration = Utils.calculateTravelDuration(historic, recycling);

        // Assert
        assertEquals(-1.0, travelDuration, "Travel duration should return -1 for insufficient capacity");
    }


    @Test
    @DisplayName("Run Scenario: Invalid Waste Distribution")
    void testRunScenarioWithInvalidWasteDistribution_TC_002() {
        // Arrange
        Historic historic = new Historic(Location.A, -100); // Invalid negative waste
        Recycling recycling = new Alpha(Location.B, 5);
        List<Recycling> recyclingCenters = List.of(recycling);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            Utils.findViableCentres(historic, recyclingCenters);
        }, "Negative waste should throw an exception");
    }


//    //Invalid Location for Recycling Center
//    @Test
//    public void testInvalidLocationForRecyclingCenter() {
//        //Arrange: Use invalid location input (e.g., "D") when collecting a location for recycling.
//
//        ByteArrayInputStream in = new ByteArrayInputStream("D\nA".getBytes()); // Invalid then valid
//        System.setIn(in);
//        Scanner scanner = new Scanner(System.in);
//
//        //Act: Simulate user input in collectLocation().
//        Location location = Main.collectLocation(scanner);
//
//        //Assert: Validate the rejection and that no invalid location is returned.
//        assertEquals(Location.A, location); // Should accept the second valid input
//    }


    //Test Optimal Center with No Viable Centers
    @Test
    @DisplayName("Optimal Center: No Viable Centers")
    void testFindOptimalCenter_NoViableCenters_TC_001() {
        //Arrange: Use a Historic site with waste but no valid Recycling centers.
        Historic historic = new Historic(Location.A, 2000.0);
        List<Recycling> recyclingCenters = new ArrayList<>(); // Empty list of recycling centers

        //Act: Call Utils.findOptimalCentre() and handle potential exceptions.
        Recycling optimalCenter = null;
        try {
            optimalCenter = Utils.findOptimalCentre(historic, recyclingCenters);
        } catch (Exception e) {

            // Assert: Fail the test if an exception is thrown.
            fail("Exception occurred while finding optimal center: " + e.getMessage());
        }
        //Assert: Ensure it handles the empty list gracefully.
        assertNull(optimalCenter, "Optimal center should be null when no viable centers exist.");
    }


    //Test Process Duration with Zero Rates
    @Test
    @DisplayName("Process Duration: Edge Case with Zero Rates")
    void testProcessDurationEdgeCaseWithZeroRates_TC_001() {
        // Arrange
        Historic historic = new Historic(Location.A, 500);
        Recycling zeroRateRecycling = new Recycling(Location.B, 5) {
            @Override
            public String getGeneration() {
                return "Custom";
            }

            @Override
            public List<Double> getRates() {
                return List.of(0.0, 0.0, 0.0); // Invalid rates
            }
        };

        // Act & Assert
        assertThrows(ArithmeticException.class, () -> {
            Utils.calculateProcessDuration(historic, zeroRateRecycling);
        }, "Division by zero should throw an exception");
    }


}