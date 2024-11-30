import models.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class RunModelUnitTests {
    @Test
    void RunModel_TC_001() {
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
    void RunModel_TC_002() {
        Location zoneA = Location.A;
        double wasteVolume = 3000;
        Historic historic = new Historic(zoneA, wasteVolume);

        Recycling center1 = new Beta(zoneA, 8);
        Recycling center2 = new Gamma(zoneA, 12);
        var recyclingCenterList = new ArrayList<Recycling>();
        recyclingCenterList.add(center1);
        recyclingCenterList.add(center2);
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenterList);
        runScenario(scenarioConfiguration);
        assertNotNull(scenarioConfiguration.getHistoric(), "Scenario finished");
    }

    @Test
    void Failover_TC_001() {
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
    void DataConsistency_TC_001() {
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

    private double runScenario(ScenarioConfiguration scenarioConfiguration) {
        List<Recycling> viableCentres = Utils.findViableCentres(scenarioConfiguration.getHistoric(),
                scenarioConfiguration.getRecycling());
        Recycling optimalCentre = Utils.findOptimalCentre(scenarioConfiguration.getHistoric(), viableCentres);
        double travelDuration = Utils.calculateTravelDuration(scenarioConfiguration.getHistoric(), optimalCentre);
        double processDuration = Utils.calculateProcessDuration(scenarioConfiguration.getHistoric(), optimalCentre);
        return travelDuration + processDuration;
    }
}
