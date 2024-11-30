import models.*;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProcessDurationUnitTests {
    @Test
    void CalculateProcessDuration_TC_001() {
        double plasticWaste = 1.5;
        double metallicWaste = 2.0;
        double paperWaste = 3.0;
        double wasteVolume = plasticWaste + metallicWaste + paperWaste;
        Recycling gammaRecyclingCenter = new Gamma(Location.B, 1);
        Historic historic = new Historic(Location.B, wasteVolume);
        historic.setPlasticGlass(plasticWaste);
        historic.setMetallic(metallicWaste);
        historic.setPaper(paperWaste);
        List<Double> gammaRates = gammaRecyclingCenter.getRates();
        double expectedDuration = (plasticWaste / gammaRates.get(0))  // Plastic duration
                                  + (metallicWaste / gammaRates.get(1)) // Metallic duration
                                  + (paperWaste / gammaRates.get(2));  // Paper duration
        double processDuration = Utils.calculateProcessDuration(historic, gammaRecyclingCenter);
        assertEquals(expectedDuration, processDuration, 0.1, "Process duration should match expected values.");
    }

    @Test
    void CalculateProcessDuration_TC_002() {
        double wasteVolume = 1000;
        Recycling betaRecyclingCenter = new Beta(Location.B, 5);
        Historic historic = new Historic(Location.B, wasteVolume);
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, List.of(betaRecyclingCenter));
        double processDuration = Utils.calculateProcessDuration(scenarioConfiguration.getHistoric(), betaRecyclingCenter);
        processDuration = Math.round(processDuration * 100.0) / 100.0;
        // Processing duration for Beta center is 1000 / 1.5 = 666.67 hours
        assertTrue(processDuration == 666.67, "Processing duration should be 666.67 hours.");
    }

    @Test
    void CalculateProcessDuration_TC_003() {
        Recycling gammaCenter = new Gamma(Location.C, 5);
        Historic historic = new Historic(Location.C, 10);
        var recyclingCenterList = Collections.singletonList(gammaCenter);
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenterList);
        double processDuration = Utils.calculateProcessDuration(scenarioConfiguration.getHistoric(), gammaCenter);

        assertEquals(5, processDuration, "Process duration should be 5 hours for metallic waste at 2 m³/hr.");
    }

    @Test
    void Math_TC_004() {
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
    void Math_TC_005() {
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
    void Math_TC_003() {
        Recycling alphaCenter = new Alpha(Location.A, 5);
        double totalWaste = 3000;
        double processingRate = 1;
        double expectedProcessDuration = totalWaste / processingRate;
        Historic historic = new Historic(Location.A, totalWaste);
        double actualProcessDuration = Utils.calculateProcessDuration(historic, alphaCenter);
        assertEquals(expectedProcessDuration, actualProcessDuration, "Alpha center process duration should match expected calculation.");
    }

    private double runScenario(ScenarioConfiguration scenarioConfiguration) {
        List<Recycling> viableCentres = Utils.findViableCentres(scenarioConfiguration.getHistoric(),
                scenarioConfiguration.getRecycling());
        Recycling optimalCentre = Utils.findOptimalCentre(scenarioConfiguration.getHistoric(), viableCentres);
        double travelDuration = Utils.calculateTravelDuration(scenarioConfiguration.getHistoric(), optimalCentre);
        double processDuration = Utils.calculateProcessDuration(scenarioConfiguration.getHistoric(), optimalCentre);
        return travelDuration + processDuration;
    }
}
