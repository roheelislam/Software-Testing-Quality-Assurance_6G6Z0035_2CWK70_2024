import models.Location;
import models.Recycling;
import models.Alpha;
import models.Beta;
import models.Historic;
import models.Transport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MiscIntegrationTests {


//Positive Test Cases

    //Verify Travel Time Between Locations
    @Test
    @DisplayName("Travel Time: Between Locations A and B")
    void testTravelTimeBetweenLocations_AB() {
        //Arrange: Created a Transport object with Location.A as the start and Location.B as the end.
        Transport transport = new Transport(Location.A, Location.B);

        //Act: Invoke the getTravelTime() method.
        double travelTime = transport.getTravelTime();

        //Assert: Verify the travel time is 2.0 hours.
        assertEquals(2.0, travelTime, "Travel time between A and B should be 2.0 hours.");
    }


    //Validate Recycling Center Creation (Alpha)
    @Test
    @DisplayName("Recycling Center Creation: Alpha Generation Validation")
    void testAlphaRecyclingCreation() {
        //Arrange: Create an Alpha recycling center at Location.A with 5 years of activity.
        Recycling alpha = new Alpha(Location.A, 5);

        //Act: Fetch the generation and rates.
        assertEquals("Alpha", alpha.getGeneration(), "Generation should be Alpha.");

        //Assert: Verify the generation is "Alpha" and rates are [1.0, 1.0, 1.0].
        assertEquals(List.of(1.0, 1.0, 1.0), alpha.getRates(), "Rates for Alpha should match [1.0, 1.0, 1.0].");
    }


    //Calculate Waste Split in Historic Site
    @Test
    @DisplayName("Historic Site: Validate Waste Split")
    void testHistoricWasteSplit() {
        //Arrange: Create a Historic site at Location.B with 1500 cubic meters of waste.
        Historic historic = new Historic(Location.B, 1500);

        //Act: Fetch plasticGlass, paper, and metallic waste.
        //Assert: Verify the waste split matches 30% plasticGlass, 50% paper, and 20% metallic.
        assertEquals(750, historic.getPaper(), 0.01, "Paper waste should be 750 cubic meters.");
        assertEquals(450, historic.getPlasticGlass(), 0.01, "PlasticGlass waste should be 450 cubic meters.");
        assertEquals(300, historic.getMetallic(), 0.01, "Metallic waste should be 300 cubic meters.");
    }


    //Calculate Travel Duration for Valid Configuration
    @Test
    @DisplayName("Travel Duration: Valid Configuration")
    void testCalculateTravelDuration() {
        //Arrange: Create a Historic site at Location.A with 50 cubic meters of waste. Add a Beta recycling center at Location.B.
        Historic historic = new Historic(Location.A, 50);

        //Act: Use Utils.calculateTravelDuration() to compute travel time.
        Recycling beta = new Beta(Location.B, 3);
        double duration = Utils.calculateTravelDuration(historic, beta);

        //Assert: Verify the travel duration is correctly computed.
        assertTrue(duration > 0, "Travel duration should be positive.");
    }


//Negative Test Cases

    // Invalid Location Input
    @Test
    @DisplayName("Invalid Location Input: Exception Handling")
    void testInvalidLocationInput() {
        //Arrange: Create a Location object and try invalid input "D".
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Location location = Location.valueOf("D");
        });

        //Act & Assert: Expect an exception.
        assertNotNull(exception, "Invalid location should throw an exception.");
    }


    // Invalid Recycling Center Generation
    @Test
    @DisplayName("Invalid Recycling Center Generation: Exception Handling")
    void testInvalidRecyclingGeneration() {
        //Arrange & Act: Attempt to create a recycling center with generation "Zeta".
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Recycling(Location.C, 3) {
                @Override
                public String getGeneration() {
                    return "InvalidGen";
                }

                @Override
                public List<Double> getRates() {
                    return List.of(0.0, 0.0, 0.0);
                }
            };
        });

        //Assert: Expect an exception or fallback behavior.
        assertNotNull(exception, "Invalid generation should throw an exception.");
    }


    //Transport Waste Capacity Exceeded Location A & C
    @Test
    @DisplayName("Transport Waste Exceeds Capacity")
    void testTransportWasteExceedCapacity() {
        //Arrange: Create a Transport object and set waste values exceeding the capacity.
        Transport transport = new Transport(Location.A, Location.C);
        transport.setPaperWaste(15);
        transport.setPlasticGlassWaste(10);

        //Act: Invoke getTotalWaste().
        double totalWaste = transport.getTotalWaste();

        //Assert: Verify it does not exceed the transport capacity.
        assertTrue(totalWaste <= 20, "Total waste should not exceed transport capacity of 20.");
    }


//Edge Cases


    //Travel Time Within Same Location
    @Test
    @DisplayName("Travel Time: Within Same Location")
    void testTravelTimeSameLocation() {
        //Arrange: Create a Transport object where the start and end locations are the same.
        Transport transport = new Transport(Location.B, Location.B);

        //Act: Compute travel time.
        double travelTime = transport.getTravelTime();

        //Assert: Verify travel time is 1.0.
        assertEquals(1.0, travelTime, "Travel time within the same location should be 1.0 hour.");
    }


    //No Recycling Centers Found
    @Test
    @DisplayName("Recycling Centers: None Found")
    void testNoRecyclingCenters() {
        //Arrange: Pass an empty list of candidate recycling centers to Utils.findViableCentres().
        Historic historic = new Historic(Location.A, 1000);
        List<Recycling> emptyList = new ArrayList<>();

        //Act: Fetch viable centers.
        List<Recycling> viableCenters = Utils.findViableCentres(historic, emptyList);

        //Assert: Verify the result is an empty list.
        assertTrue(viableCenters.isEmpty(), "No viable recycling centers should be found.");
    }


    //Waste Below Transport Capacity
    @Test
    @DisplayName("Transport Capacity: Waste Below Threshold")
    void testWasteBelowTransportCapacity() {
        //Arrange: Create a Historic site with waste below transport capacity.
        Historic historic = new Historic(Location.C, 10); // Below transport capacity
        Recycling alpha = new Alpha(Location.B, 2);

        //Act: Use Utils.calculateTravelDuration().
        double duration = Utils.calculateTravelDuration(historic, alpha);

        //Assert: Verify the result is -1.0.
        assertEquals(-1.0, duration, "Travel duration should return -1.0 for waste below capacity.");
    }


}
