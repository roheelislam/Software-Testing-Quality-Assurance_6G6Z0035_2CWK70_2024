import models.Location;
import models.Recycling;
import models.Alpha;
import models.Gamma;
import models.Beta;
import models.Historic;
import models.Transport;
import org.junit.jupiter.api.Test;


import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class MainScenarioConfigUtilsIntegrationTests {


    //Positive Test Cases

    //Test Scenario Configuration with Valid Inputs
    @Test
    void testScenarioConfiguration_ValidInputs() {
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
    void testRunScenarioWithValidInputs() {
        // Arrange
        Historic historic = new Historic(Location.A, 1000); // Valid initial waste
        Recycling recycling1 = new Alpha(Location.B, 5); // Valid Recycling center
        Recycling recycling2 = new Beta(Location.C, 3);
        List<Recycling> recyclingCenters = List.of(recycling1, recycling2);
        ScenarioConfiguration scenario = new ScenarioConfiguration(historic, recyclingCenters);

        // Act
        List<Recycling> viableCenters = Utils.findViableCentres(historic, recyclingCenters);
        Recycling optimalCenter = Utils.findOptimalCentre(historic, viableCenters);
        double travelDuration = Utils.calculateTravelDuration(historic, optimalCenter);
        double processDuration = Utils.calculateProcessDuration(historic, optimalCenter);

        // Assert
        assertNotNull(viableCenters, "Viable centers should not be null");
        assertTrue(viableCenters.size() > 0, "There should be at least one viable center");
        assertNotNull(optimalCenter, "Optimal center should be determined");
        assertTrue(travelDuration > 0, "Travel duration should be greater than 0");
        assertTrue(processDuration > 0, "Process duration should be greater than 0");
    }


    @Test
    void testScenarioWithMultipleGenerations() {
        // Arrange
        Historic historic = new Historic(Location.B, 2000); // Above metallic threshold
        Recycling alpha = new Alpha(Location.A, 8);
        Recycling beta = new Beta(Location.C, 3);
        Recycling gamma = new Gamma(Location.A, 2); // Gamma expected to be excluded
        List<Recycling> recyclingCenters = List.of(alpha, beta, gamma);

        // Act
        List<Recycling> viableCenters = Utils.findViableCentres(historic, recyclingCenters);

        // Assert
        assertEquals(2, viableCenters.size(), "Gamma centers should be excluded due to metallic conditions");
    }


    //Test Running a Scenario with Viable Recycling Centers
    @Test
    void testRunScenario_WithValidViableCenters() {
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
    void testCalculateDurations_ValidInputs() {
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
    void testScenarioConfiguration_NullHistoric() {
        //Arrange: Use a null Historic site.
        List<Recycling> recycling = List.of(new Alpha(Location.B, 5));

        //Act: Attempt to create a ScenarioConfiguration.
        //Assert: Ensure NullPointerException or appropriate error is thrown.
        assertThrows(NullPointerException.class, () -> {
            new ScenarioConfiguration(null, recycling);
        });
    }


    @Test
    void testScenarioConfigurationWithEmptyInputs() {
        // Arrange
        ScenarioConfiguration scenario = new ScenarioConfiguration();

        // Act & Assert
        assertNull(scenario.getHistoric(), "Historic site should initially be null");
        assertEquals(0, scenario.getRecycling().size(), "Recycling centers should initially be empty");
    }


    //Test Find Viable Centers with Empty List
    @Test
    void testFindViableCenters_EmptyList() {
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
    void testCalculateTravelDuration_InsufficientWaste() {
        //Arrange: Create a Historic site with waste less than transport capacity.
        Historic historic = new Historic(Location.A, 10.0);
        Recycling alpha = new Alpha(Location.B, 10);

        //Act: Call Utils.calculateTravelDuration().
        double travelTime = Utils.calculateTravelDuration(historic, alpha);

        //Assert: Ensure the result is -1.0.
        assertEquals(-1.0, travelTime);
    }


    @Test
    void testTravelDurationWithInsufficientTransportCapacity() {
        // Arrange
        Historic historic = new Historic(Location.A, 10); // Below transport capacity
        Recycling recycling = new Beta(Location.B, 5);

        // Act
        double travelDuration = Utils.calculateTravelDuration(historic, recycling);

        // Assert
        assertEquals(-1.0, travelDuration, "Travel duration should return -1 for insufficient capacity");
    }


    @Test
    void testRunScenarioWithInvalidWasteDistribution() {
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
    void testFindOptimalCenter_NoViableCenters() {
        //Arrange: Use a Historic site with waste but no valid Recycling centers.
        Historic historic = new Historic(Location.A, 2000.0);
        List<Recycling> recycling = new ArrayList<>();

        //Act: Call Utils.findOptimalCentre().
        Recycling optimalCenter = Utils.findOptimalCentre(historic, recycling);

        //Assert: Ensure it handles the empty list gracefully.
        assertNull(optimalCenter);
    }


    //Test Process Duration with Zero Rates
    @Test
    void testProcessDurationEdgeCaseWithZeroRates() {
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
        List<Recycling> recyclingCenters = List.of(zeroRateRecycling);

        // Act & Assert
        assertThrows(ArithmeticException.class, () -> {
            Utils.calculateProcessDuration(historic, zeroRateRecycling);
        }, "Division by zero should throw an exception");
    }


}