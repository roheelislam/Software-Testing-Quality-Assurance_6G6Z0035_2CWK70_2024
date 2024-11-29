import models.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TravelDurationUnitTests {
//    @Test
//    public void CalculateTravelDuration_TC_001() {
//        double wasteVolume = 20;
//        Historic historic = new Historic(Location.A, wasteVolume);
//        Recycling recyclingCenter = new Beta(Location.B, 3);
//        var recyclingCenterList = new ArrayList<Recycling>();
//        recyclingCenterList.add(recyclingCenter);
//        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenterList);
//        double travelDuration = Utils.calculateTravelDuration(scenarioConfiguration.getHistoric(), recyclingCenter);
//
//        assertEquals(1.0, travelDuration, "Travel duration should be 1 hour for a round trip within the same zone.");
//    }

    @Test
    public void CalculateTravelDuration_TC_002() {
        double wasteVolume = 200;
        Historic historic = new Historic(Location.A, wasteVolume);
        Recycling recyclingCenter = new Beta(Location.B, 5);
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, List.of(recyclingCenter));
        double travelTime = Utils.calculateTravelDuration(scenarioConfiguration.getHistoric(), recyclingCenter);
        int trips = (int) Math.ceil(wasteVolume / 20.0);
        double totalTravelDuration = trips * travelTime;
        assertTrue(totalTravelDuration == 20, "Total travel duration should be 20 hours.");
    }

//    @Test
//    public void CalculateTravelDuration_TC_003() {
//        double wasteVolume = 20;
//        Historic historic = new Historic(Location.A, wasteVolume);
//        Recycling recyclingCenter = new Alpha(Location.A, 3);
//        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, List.of(recyclingCenter));
//        double travelDuration = Utils.calculateTravelDuration(scenarioConfiguration.getHistoric(), recyclingCenter);
//        assertTrue(travelDuration == 1, "Travel duration should be 1 hour.");
//    }

//    @Test
//    public void CalculateTravelDuration_TC_004() {
//        Historic historicSite = new Historic(Location.A, 30.0);
//        Recycling recyclingCenter = new Alpha(Location.B, 5);
//        double travelDuration = Utils.calculateTravelDuration(historicSite, recyclingCenter);
//        double expectedDuration = 4.0; // 2 trips * 2 hours per trip
//        assertEquals(expectedDuration, travelDuration, "Travel duration should be 4 hours for 30 m³ waste with 20 m³ truck capacity");
//    }

    @Test
    public void Validation_TC_006() {
        double wasteVolume = 2000;
        Location location = Location.A;
        Historic landfill = new Historic(location, wasteVolume);
        Recycling center = new Alpha(location, 5);
        double expectedTravelDuration = calculateExpectedTravelDuration(wasteVolume);
        double actualTravelDuration = Utils.calculateTravelDuration(landfill, center);
        assertEquals(expectedTravelDuration, actualTravelDuration, 0.1, "Travel duration should be " +
                                                                        "1 hour per round trip within the same zone.");
    }

    @Test
    public void Math_TC_002() {
        Historic landfill = new Historic(Location.A, 5000);
        double truckCapacity = 20;
        double roundTripTime = 1.0;
        double expectedTrips = Math.ceil(5000 / truckCapacity);
        double expectedTotalTravelDuration = expectedTrips * roundTripTime;
        double actualTravelDuration = Utils.calculateTravelDuration(landfill, new Gamma(Location.A, 5000));
        assertEquals(expectedTotalTravelDuration, actualTravelDuration, 0.1, "Total travel duration should match expected calculation.");
    }

    private double calculateExpectedTravelDuration(double wasteVolume) {
        double truckCapacity = 20;
        double tripsNeeded = Math.ceil(wasteVolume / truckCapacity);
        double roundTripTime = 1;
        return tripsNeeded * roundTripTime;
    }
}
