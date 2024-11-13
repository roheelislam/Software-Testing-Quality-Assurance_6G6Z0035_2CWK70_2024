import models.*;
import org.junit.jupiter.api.Test;

import java.util.*;

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

//    @Test
//    public void FindViableCentres_TC_002_2() {
//        double wasteVolume = 3000; // Including metallic waste
//        Historic historic = new Historic(Location.B, wasteVolume);
//        Recycling recyclingCenter1 = new Alpha(Location.B, 3); // Within 3 hours
//        Recycling recyclingCenter2 = new Beta(Location.C, 5); // Beyond 3 hours
//        Recycling recyclingCenter3 = new Gamma(Location.B, 2); // Within 3 hours
//        var recyclingCenterList = new ArrayList<Recycling>();
//        recyclingCenterList.add(recyclingCenter1);
//        recyclingCenterList.add(recyclingCenter2);
//        recyclingCenterList.add(recyclingCenter3);
//        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenterList);
//        List<Recycling> viableCentres = Utils.findViableCentres(scenarioConfiguration.getHistoric(), scenarioConfiguration.getRecycling());
//        assertTrue(viableCentres.contains(recyclingCenter1), "Alpha recycling center should be viable.");
//        assertTrue(viableCentres.contains(recyclingCenter3), "Gamma recycling center should be viable.");
//        assertTrue(!viableCentres.contains(recyclingCenter2), "Beta recycling center should not be " +
//                                                              "viable due to distance.");
//    }

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

        // Processing duration for Beta center is 1000 / 1.5 = 666.67 hours
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
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenterList);
        Main.runScenario(scenarioConfiguration);
        assertNotNull(scenarioConfiguration.getHistoric(), "Scenario finished");
    }

    @Test
    public void EstimateWasteSplit_TC_003() {
        double wasteVolume = 1250;
        Historic historic = new Historic(Location.B, wasteVolume);
        historic.estimateWasteSplit(wasteVolume);
        assertEquals(historic.getPlasticGlass(), 625, "Plastic/Glass waste should be 625 m³ (50%).");
        assertEquals(historic.getPaper(), 625, "Paper waste should be 625 m³ (50%).");
        assertEquals(historic.getMetallic(), 0, "Metallic waste should be 0 m³ (0%).");
    }

    @Test
    public void FindViableCentres_TC_004() {
        Recycling center1 = new Alpha(Location.B, 5);
        Recycling center2 = new Alpha(Location.B, 5);
        Recycling center3 = new Alpha(Location.B, 5);
        var recyclingCenterList = Arrays.asList(center1, center2, center3);
        Historic historic = new Historic(Location.B, 2000);
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenterList);
        List<Recycling> viableCenters = Utils.findViableCentres(scenarioConfiguration.getHistoric(), scenarioConfiguration.getRecycling());
        assertEquals(3, viableCenters.size(), "All centers should be marked as viable.");
        assertTrue(viableCenters.containsAll(recyclingCenterList), "All centers in Zone B should be in the viable list.");
    }

    @Test
    public void FindOptimalCentre_TC_002() {
        Recycling centerWithFewerYears = new Alpha(Location.A, 5);
        Recycling centerWithMoreYears = new Alpha(Location.A, 10);
        var recyclingCenterList = Arrays.asList(centerWithFewerYears, centerWithMoreYears);
        Historic historic = new Historic(Location.A, 2000);
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenterList);
        Recycling optimalCenter = Utils.findOptimalCentre(scenarioConfiguration.getHistoric(), scenarioConfiguration.getRecycling());
        assertEquals(centerWithFewerYears, optimalCenter, "The optimal center should be the one with the fewest active years.");
    }

    @Test
    public void CalculateTravelDuration_TC_004() {
        Historic historicSite = new Historic(Location.A, 30.0);
        Recycling recyclingCenter = new Alpha(Location.B, 5);
        double travelDuration = Utils.calculateTravelDuration(historicSite, recyclingCenter);
        double expectedDuration = 4.0; // 2 trips * 2 hours per trip
        assertEquals(expectedDuration, travelDuration, "Travel duration should be 4 hours for 30 m³ waste with 20 m³ truck capacity");
    }

    @Test
    public void CalculateProcessDuration_TC_003() {
        Recycling gammaCenter = new Gamma(Location.C, 5);
        Historic historic = new Historic(Location.C, 10);
        var recyclingCenterList = Collections.singletonList(gammaCenter);
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenterList);
        double processDuration = Utils.calculateProcessDuration(scenarioConfiguration.getHistoric(), gammaCenter);

        assertEquals(5, processDuration, "Process duration should be 5 hours for metallic waste at 2 m³/hr.");
    }

    @Test
    public void Boundary_TC_001() {
        Historic historic = new Historic(Location.A, 1000);
        Recycling boundaryCenter = new Alpha(Location.B, 3); // Center exactly 3 hours

        var recyclingCenterList = Arrays.asList(boundaryCenter);
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenterList);
        List<Recycling> viableCenters = Utils.findViableCentres(scenarioConfiguration.getHistoric(), scenarioConfiguration.getRecycling());
        assertTrue(viableCenters.contains(boundaryCenter), "Center at the exact 3-hour limit should be marked as viable.");
    }

    @Test
    public void API_ScenarioCreate_TC_003() {
        //Send POST request to /Scenario and capture response
    }

    @Test
    public void API_ScenarioDelete_TC_002() {
        //Send DELETE request to delete the scenario

    }

    @Test
    public void API_ResponseFormat_TC_001() {
        //No API IMPLEMENTATION FOUND

    }

    @Test
    public void Auth_API_Access_TC_001() {
        //No API IMPLEMENTATION FOUND FOR AUTHENTICATION
    }

    @Test
    public void ErrorHandling_TC_001() {
        double initialWaste = 10;
        Historic historic = new Historic(Location.A, 100);
//        historic.setWasteType("Hazardous"); //can't set , Move to defect
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            historic.estimateWasteSplit(10);
        });
        assertTrue(exception.getMessage().contains("unsupported waste type"), "Error message should indicate unsupported type.");
    }

    @Test
    public void Performance_TC_001() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            Historic landfill = new Historic(Location.A, 2000.0);
            Recycling center = new Beta(Location.B, 5);
            Utils.calculateTravelDuration(landfill, center);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        assertTrue(duration < 5000, "System response time should be within acceptable limits.");
    }

    @Test
    public void DataConsistency_TC_001() {
        Historic landfill1 = new Historic(Location.A, 5000.0);
        Historic landfill2 = new Historic(Location.A, 5000.0);
        Recycling center = new Alpha(Location.A, 10);

        double travelDuration1 = Utils.calculateTravelDuration(landfill1, center);
        double processDuration1 = Utils.calculateProcessDuration(landfill1, center);

        double travelDuration2 = Utils.calculateTravelDuration(landfill2, center);
        double processDuration2 = Utils.calculateProcessDuration(landfill2, center);

        assertEquals(travelDuration1, travelDuration2, 0.1, "Travel durations should match for identical scenarios.");
        assertEquals(processDuration1, processDuration2, 0.1, "Process durations should match for identical scenarios.");
    }

    @Test
    public void EdgeCase_TC_002() {
        Historic landfill = new Historic(Location.A, 1000.0);
        landfill.setMetallic(1000.0); // Only metallic waste
        landfill.setPlasticGlass(0);
        landfill.setPaper(0);

        Recycling center = new Gamma(Location.C, 10);
        var centerList = new ArrayList<Recycling>();
        centerList.add(center);
        List<Recycling> viableCenters = Utils.findViableCentres(landfill,centerList );

        // Assert that the Gamma center is considered viable due to metallic waste presence
        assertTrue(viableCenters.contains(center), "Gamma center should be viable for metallic-only waste.");
    }

    @Test
    public void Failover_TC_001() {
        Historic landfill = new Historic(Location.A, 5000.0);
        Recycling center = new Alpha(Location.A, 10);
        try {
            Utils.calculateTravelDuration(landfill, center);
            throw new RuntimeException("Simulated system interruption.");
        } catch (Exception e) {
            System.out.println("System interrupted, resuming...");
            var centerList = new ArrayList<Recycling>();
            centerList.add(center);
            double result = runScenario(new ScenarioConfiguration(landfill, centerList));
            boolean resume = Objects.nonNull(result) && result > 0;
            assertTrue(resume, "System should resume or restart after an interruption.");
        }
    }

    @Test
    public void Persistence_TC_001() {
        Historic landfill = new Historic(Location.A, 2000.0);
        Recycling center = new Alpha(Location.B, 5);
        ScenarioConfiguration scenario = new ScenarioConfiguration(landfill, List.of(center));
//        Utils.saveScenario(scenario, "testScenario.dat");

//        ScenarioConfiguration loadedScenario = Utils.loadScenario("testScenario.dat");
//
//        assertEquals(scenario.getHistoric().getRemainingWaste(), loadedScenario.getHistoric().getRemainingWaste(),
//                "Remaining waste should match after reload.");
//        assertEquals(scenario.getRecycling().size(), loadedScenario.getRecycling().size(),
//                "Recycling centers count should match after reload.");
    }

    @Test
    public void Validation_TC_001() {
        double initialWaste = 1250;
        Historic historic = new Historic(Location.A, initialWaste);
        historic.estimateWasteSplit(initialWaste);
        assertEquals(625, historic.getPlasticGlass(), 0.1, "Plastic/Glass waste should be 625 cubic meters.");
        assertEquals(625, historic.getPaper(), 0.1, "Paper waste should be 625 cubic meters.");
    }

    @Test
    public void Validation_TC_002() {
        Recycling gammaCenter = new Gamma(Location.B, 8);
        List<Double> rates = gammaCenter.getRates();
        double plasticVolume = 300;
        double metallicVolume = 400;
        double paperVolume = 500;
        double totalWaste = plasticVolume + metallicVolume + paperVolume;

        double expectedPlasticDuration = plasticVolume / rates.get(0);   // plastic
        double expectedMetallicDuration = metallicVolume / rates.get(1); // metallic
        double expectedPaperDuration = paperVolume / rates.get(2);       // paper
        double expectedTotalDuration = expectedPlasticDuration + expectedMetallicDuration + expectedPaperDuration;
        Historic historic = new Historic(Location.A, totalWaste);
        double actualTotalDuration = Utils.calculateProcessDuration(historic, gammaCenter);
        assertEquals(expectedTotalDuration, actualTotalDuration, "Gamma center processing duration should match expected calculation.");
    }

    @Test
    public void Validation_TC_003() {
        Recycling alphaCenter = new Alpha(Location.A, 5);
        Recycling betaCenter = new Beta(Location.B, 7);
        Recycling gammaCenter = new Gamma(Location.B, 8);
        Historic landfill = new Historic(Location.A, 1000);
        var centerList = new ArrayList<Recycling>();
        centerList.add(alphaCenter);
        List<Recycling> viableCenters = Utils.findViableCentres(landfill, centerList);
        assertTrue(viableCenters.contains(alphaCenter), "Alpha should be viable for plastic waste.");
        assertTrue(viableCenters.contains(betaCenter), "Beta should be viable for plastic waste.");
        assertFalse(viableCenters.contains(gammaCenter), "Gamma should not be viable for plastic waste only.");
    }

    @Test
    public void Validation_TC_004() {
        Historic historic = new Historic(Location.A, 2000);
//        historic.setWasteType("Hazardous");  // Unsupported type

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            historic.estimateWasteSplit(2000);
        });

        assertEquals("Unsupported waste type: Hazardous", exception.getMessage());
    }

    @Test
    public void Validation_TC_005() {
        ScenarioConfiguration scenarioConfig = new ScenarioConfiguration();
        Historic historic = new Historic(Location.A, 1500);
        scenarioConfig.setHistoric(historic);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
//            scenarioConfig.finalizeScenario();
            //Implementaion not found for the finalize .
        });
        assertEquals("Scenario cannot be finalized without complete data.", exception.getMessage());
    }

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
    public void Math_TC_001() {
        double totalWaste = 5000;
        Historic historic = new Historic(Location.A, totalWaste);
        historic.estimateWasteSplit(totalWaste);
        assertEquals(2500, historic.getPaper(), 0.1, "Paper waste should be 50% of total (2500 m³).");
        assertEquals(1500, historic.getPlasticGlass(), 0.1, "Plastic/Glass waste should be 30% of total (1500 m³).");
        assertEquals(1000, historic.getMetallic(), 0.1, "Metallic waste should be 20% of total (1000 m³).");
    }

    @Test
    public void Math_TC_002() {
        Historic landfill = new Historic(Location.A, 5000);
        double truckCapacity = 20;
        double roundTripTime = 0.5;
        double expectedTrips = Math.ceil(5000 / truckCapacity);
        double expectedTotalTravelDuration = expectedTrips * roundTripTime;
        double actualTravelDuration = Utils.calculateTravelDuration(landfill, new Gamma(Location.A, 5000));
        assertEquals(expectedTotalTravelDuration, actualTravelDuration, 0.1, "Total travel duration should match expected calculation.");
    }

    @Test
    public void Math_TC_003() {
        Recycling alphaCenter = new Alpha(Location.A, 5);
        double totalWaste = 3000;
        double processingRate = 1;
        double expectedProcessDuration = totalWaste / processingRate;
        Historic historic = new Historic(Location.A, totalWaste);
        double actualProcessDuration = Utils.calculateProcessDuration(historic, alphaCenter);
        assertEquals(expectedProcessDuration, actualProcessDuration, "Alpha center process duration should match expected calculation.");
    }

    @Test
    public void Math_TC_004() {
        ScenarioConfiguration scenario = new ScenarioConfiguration();
        Historic historic = new Historic(Location.A, 1000);
        Recycling alphaCenter = new Alpha(Location.A, 5);
        Recycling betaCenter = new Beta(Location.B, 7);
        Recycling gammaCenter = new Gamma(Location.C, 10);

        scenario.setHistoric(historic);
        scenario.addRecycling(alphaCenter);
        scenario.addRecycling(betaCenter);
        scenario.addRecycling(gammaCenter);

        double expectedTotalDuration = Utils.calculateProcessDuration(historic, alphaCenter);
        double actualTotalDuration = runScenario(scenario);
        assertEquals(expectedTotalDuration, actualTotalDuration, "Total duration should be correctly calculated for all waste types.");
    }

    @Test
    public void Math_TC_005() {
        ScenarioConfiguration scenario1 = new ScenarioConfiguration();
        ScenarioConfiguration scenario2 = new ScenarioConfiguration();
        Historic historic = new Historic(Location.A, 2000);
        Recycling alphaCenter = new Alpha(Location.A, 5);

        scenario1.setHistoric(historic);
        scenario1.addRecycling(alphaCenter);

        scenario2.setHistoric(historic);
        scenario2.addRecycling(alphaCenter);

        double duration1 = runScenario(scenario1);
        double duration2 = runScenario(scenario2);

        assertEquals(duration1, duration2, "Identical scenarios should yield consistent durations.");
    }

    @Test
    public void Math_TC_006() {
        Recycling alphaCenter = new Alpha(Location.B, 5);  // Alpha with 5 years active
        Recycling betaCenter = new Beta(Location.B, 3);    // Beta with 3 years active
        Historic landfill = new Historic(Location.B, 1500);
        var centerList = new ArrayList<Recycling>();
        centerList.add(alphaCenter);
        centerList.add(betaCenter);
        Recycling optimalCenter = Utils.findOptimalCentre(landfill, centerList);
        assertEquals(betaCenter, optimalCenter, "Beta should be selected as the optimal center due " +
                                                "to higher generation and fewer years active.");
    }

    private double runScenario(ScenarioConfiguration scenarioConfiguration) {
        List<Recycling> viableCentres = Utils.findViableCentres(scenarioConfiguration.getHistoric(),
                scenarioConfiguration.getRecycling());
        Recycling optimalCentre = Utils.findOptimalCentre(scenarioConfiguration.getHistoric(), viableCentres);
        double travelDuration = Utils.calculateTravelDuration(scenarioConfiguration.getHistoric(), optimalCentre);
        double processDuration = Utils.calculateProcessDuration(scenarioConfiguration.getHistoric(), optimalCentre);
        return travelDuration + processDuration;
    }

    private double calculateExpectedTravelDuration(double wasteVolume) {
        double truckCapacity = 20;
        double tripsNeeded = Math.ceil(wasteVolume / truckCapacity);
        double roundTripTime = 1;
        return tripsNeeded * roundTripTime;
    }
}

