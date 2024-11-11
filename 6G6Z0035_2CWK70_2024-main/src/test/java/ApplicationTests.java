import models.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationTests {
    @Test
    public void RunModel_TC_001() {
        Location zoneB = Location.B;
        double wasteVolume = 2000;
        Historic historic = new Historic(zoneB, wasteVolume);
        // Create a Beta Recycling Center located in Zone B with 5 years of activity
        Recycling recyclingCenter = new Beta(zoneB, 5);
        var recyclingCenterList = new ArrayList<Recycling>();
        recyclingCenterList.add(recyclingCenter);
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenterList);
        List<Recycling> viableCentres = Utils.findViableCentres(scenarioConfiguration.getHistoric(), scenarioConfiguration.getRecycling());
        Recycling optimalCentre = Utils.findOptimalCentre(scenarioConfiguration.getHistoric(), viableCentres);

        double travelDuration = Utils.calculateTravelDuration(scenarioConfiguration.getHistoric(), optimalCentre);
        double processDuration = Utils.calculateProcessDuration(scenarioConfiguration.getHistoric(), optimalCentre);
        double totalDuration = travelDuration + processDuration;

        assertNotNull(travelDuration, "Travel duration should not be null.");
        assertNotNull(processDuration, "Process duration should not be null.");
        assertTrue(totalDuration > 0, "Total duration should be greater than 0.");
    }
    @Test
    public void EstimateWasteSplit_TC_001() {
        double wasteVolume = 1000;
        Historic historic = new Historic(Location.B, wasteVolume);
        historic.estimateWasteSplit(wasteVolume);
        assertEquals(500.0, historic.getPlasticGlass(), "Plastic/Glass waste should be 50% of total.");
        assertEquals(500.0, historic.getPaper(), "Paper waste should be 50% of total.");
    }
    @Test
    public void EstimateWasteSplit_TC_002() {
        double wasteVolume = 5000;
        Historic historic = new Historic(Location.B, wasteVolume);

        historic.estimateWasteSplit(wasteVolume);
        assertEquals(2500.0, historic.getPaper(), "Paper waste should be 50% of total.");
        assertEquals(1500.0, historic.getPlasticGlass(), "Plastic/Glass waste should be 30% of total.");
        assertEquals(1000.0, historic.getMetallic(), "Metallic waste should be 20% of total.");
    }
    @Test
    public void FindViableCentres_TC_001() {
        double wasteVolume = 2000; // Including metallic waste
        Historic historic = new Historic(Location.B, wasteVolume);
        Recycling recyclingCenter1 = new Beta(Location.B, 5);
        Recycling recyclingCenter2 = new Alpha(Location.C, 3);
        var recyclingCenterList = new ArrayList<Recycling>();
        recyclingCenterList.add(recyclingCenter1);
        recyclingCenterList.add(recyclingCenter2);

        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenterList);
        List<Recycling> viableCentres = Utils.findViableCentres(scenarioConfiguration.getHistoric(), scenarioConfiguration.getRecycling());

        assertTrue(viableCentres.size() > 0, "At least one viable center should be found.");
    }
    @Test
    public void FindViableCentres_TC_002() {
        double wasteVolume = 1000; // No metallic waste
        Historic historic = new Historic(Location.B, wasteVolume);
        Recycling recyclingCenter1 = new Alpha(Location.B, 3);
        Recycling recyclingCenter2 = new Beta(Location.C, 2);
        var recyclingCenterList = new ArrayList<Recycling>();
        recyclingCenterList.add(recyclingCenter1);
        recyclingCenterList.add(recyclingCenter2);
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenterList);
        List<Recycling> viableCentres = Utils.findViableCentres(scenarioConfiguration.getHistoric(), scenarioConfiguration.getRecycling());

        // Assert that only Alpha and Beta centers are viable
        assertTrue(viableCentres.stream().anyMatch(center -> center instanceof Alpha), "Alpha centers should be viable.");
        assertTrue(viableCentres.stream().anyMatch(center -> center instanceof Beta), "Beta centers should be viable.");
    }
    @Test
    public void FindViableCentres_TC_002_2() {
        double wasteVolume = 3000; // Including metallic waste
        Historic historic = new Historic(Location.B, wasteVolume);
        Recycling recyclingCenter1 = new Alpha(Location.B, 3); // Within 3 hours
        Recycling recyclingCenter2 = new Beta(Location.C, 5); // Beyond 3 hours
        Recycling recyclingCenter3 = new Gamma(Location.B, 2); // Within 3 hours
        var recyclingCenterList = new ArrayList<Recycling>();
        recyclingCenterList.add(recyclingCenter1);
        recyclingCenterList.add(recyclingCenter2);
        recyclingCenterList.add(recyclingCenter3);
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenterList);
        List<Recycling> viableCentres = Utils.findViableCentres(scenarioConfiguration.getHistoric(), scenarioConfiguration.getRecycling());
        assertTrue(viableCentres.contains(recyclingCenter1), "Alpha recycling center should be viable.");
        assertTrue(viableCentres.contains(recyclingCenter3), "Gamma recycling center should be viable.");
        assertTrue(!viableCentres.contains(recyclingCenter2), "Beta recycling center should not be " +
                                                              "viable due to distance.");
    }
    @Test
    public void CalculateTravelDuration_TC_001() {
        double wasteVolume = 20;
        Historic historic = new Historic(Location.A, wasteVolume);
        Recycling recyclingCenter = new Beta(Location.A, 3);
        var recyclingCenterList = new ArrayList<Recycling>();
        recyclingCenterList.add(recyclingCenter);
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenterList);
        double travelDuration = Utils.calculateTravelDuration(scenarioConfiguration.getHistoric(), recyclingCenter);

        assertEquals(1.0, travelDuration, "Travel duration should be 1 hour for a round trip within the same zone.");
    }
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
    @Test
    public void CalculateTravelDuration_TC_003() {
        double wasteVolume = 20;
        Historic historic = new Historic(Location.A, wasteVolume);
        Recycling recyclingCenter = new Alpha(Location.A, 3);
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, List.of(recyclingCenter));
        double travelDuration = Utils.calculateTravelDuration(scenarioConfiguration.getHistoric(), recyclingCenter);
        assertTrue(travelDuration == 1, "Travel duration should be 1 hour.");
    }
    @Test
    public void CalculateProcessDuration_TC_001() {
        double plasticWaste = 1.5;
        double metallicWaste = 2.0;
        double paperWaste = 3.0;
        double wasteVolume = plasticWaste + metallicWaste + paperWaste;
        Recycling gammaRecyclingCenter = new Gamma(Location.B, 12);
        Historic historic = new Historic(Location.B, wasteVolume);
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, List.of(gammaRecyclingCenter));
        double processDuration = Utils.calculateProcessDuration(scenarioConfiguration.getHistoric(), gammaRecyclingCenter);
        assertTrue(processDuration == (plasticWaste + metallicWaste + paperWaste), "Process duration should match expected values.");
    }
    @Test
    public void CalculateProcessDuration_TC_002() {
        double wasteVolume = 1000;
        Recycling betaRecyclingCenter = new Beta(Location.B, 5);
        Historic historic = new Historic(Location.B, wasteVolume);
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, List.of(betaRecyclingCenter));
        double processDuration = Utils.calculateProcessDuration(scenarioConfiguration.getHistoric(), betaRecyclingCenter);

        // Assert: Processing duration for Beta center is 1000 / 1.5 = 666.67 hours
        assertTrue(processDuration == 666.67, "Processing duration should be 666.67 hours.");
    }
    @Test
    public void API_ScenarioCreate_TC_001() {
        // No API TO SEND POST
    }
    @Test
    public void FindOptimalCentre_TC_001() {
        Historic historic = new Historic(Location.A, 3000);
        Recycling centerAlpha = new Alpha(Location.A, 10);
        Recycling centerBeta = new Beta(Location.B, 8);
        Recycling centerGamma = new Gamma(Location.C, 12);
        var recyclingCenterList = new ArrayList<Recycling>();
        recyclingCenterList.add(centerAlpha);
        recyclingCenterList.add(centerBeta);
        recyclingCenterList.add(centerGamma);

        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenterList);
        Recycling optimalCenter = Utils.findOptimalCentre(scenarioConfiguration.getHistoric(), scenarioConfiguration.getRecycling());

        // Assert: Optimal center should be the nearest one with the highest years active, which is Alpha in Zone A
        assertTrue(optimalCenter == centerAlpha, "The optimal center should be the Alpha generation in Zone A with 10 years active.");
    }
    @Test
    public void API_ScenarioDelete_TC_001() {
        // Send a DELETE request to remove the scenario
    }
    @Test
    public void EdgeCase_TC_001() {
        Location zoneA = Location.A;
        double wasteVolume = 1250;
        Historic historic = new Historic(zoneA, wasteVolume);
        Recycling recyclingCenter = new Alpha(zoneA, 10);
        var recyclingCenterList = new ArrayList<Recycling>();
        recyclingCenterList.add(recyclingCenter);
        // Assert the correct distribution of waste
        assertEquals(historic.getPaper(), 625, "Paper waste should be 625 m³ (50%).");
        assertEquals(historic.getPlasticGlass(), 625, "Plastic/Glass waste should be 625 m³ (50%).");
        assertEquals(historic.getMetallic(), 0, "Metallic waste should be 0 m³ (0%).");
    }
    @Test
    public void RunModel_TC_002() {
        Location zoneA = Location.A;
        double wasteVolume = 3000;
        Historic historic = new Historic(zoneA, wasteVolume);

        Recycling center1 = new Beta(zoneA, 8);
        Recycling center2 = new Gamma(zoneA, 12);
        var recyclingCenterList = new ArrayList<Recycling>();
        recyclingCenterList.add(center1);
        recyclingCenterList.add(center2);
        Main main = new Main();

        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenterList);
        main.runScenario(scenarioConfiguration);

    }


}

