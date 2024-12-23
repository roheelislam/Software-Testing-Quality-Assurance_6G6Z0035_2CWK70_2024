import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RunModelIntegrationTest {
    private final static int INITIAL_WASTE = 5000;

    @Test
    @DisplayName("Full Scenario Execution: Validate Waste Split and Optimal Center")
    void testFullScenarioExecution_TC_001() {
        // Arrange
        Historic historic = new Historic(Location.A, INITIAL_WASTE);
        Alpha alphaCentre = new Alpha(Location.A, INITIAL_WASTE);
        Beta betaCentre = new Beta(Location.B, INITIAL_WASTE);
        Gamma gammaCentre = new Gamma(Location.C, INITIAL_WASTE);
        List<Recycling> recyclingCenters = new ArrayList<>(Arrays.asList(alphaCentre, betaCentre, gammaCentre));
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenters);
        double expectedPlasticGlass = 1500.0; // 30%
        double expectedPaper = 2500.0; // 50%
        double expectedMetallic = 1000.0; // 20%

        assertEquals(expectedPlasticGlass, historic.getPlasticGlass(), "Plastic/Glass split mismatch");
        assertEquals(expectedPaper, historic.getPaper(), "Paper split mismatch");
        assertEquals(expectedMetallic, historic.getMetallic(), "Metallic split mismatch");

        // Act & Assert Viable Centers
        List<Recycling> viableCenters = Utils.findViableCentres(historic, scenarioConfiguration.getRecycling());
        assertTrue(viableCenters.contains(alphaCentre));
        assertTrue(viableCenters.contains(betaCentre));
        assertFalse(viableCenters.contains(gammaCentre));

        // Act & Assert Optimal Center
        Recycling optimalCenter = Utils.findOptimalCentre(historic, viableCenters);
        assertEquals("Alpha", optimalCenter.getGeneration(), "Optimal center should be Alpha as it's closest");

        // Act & Assert Travel and Process Durations
        double travelDuration = Utils.calculateTravelDuration(historic, optimalCenter);
        double processDuration = Utils.calculateProcessDuration(historic, optimalCenter);

        assertTrue(travelDuration > 0, "Travel duration should be greater than 0");
        assertTrue(processDuration > 0, "Process duration should be greater than 0");
        System.out.printf("Travel Duration: %.2f hours, Process Duration: %.2f hours, Total Duration: %.2f hours%n",
                travelDuration, processDuration, travelDuration + processDuration);
    }

    @Test
    @DisplayName("Calculate Travel and Process Duration with Optimal Center")
    void testCalculateTravelAndProcessDurationWithOptimalCenter_TC_001() { //Site
        // Arrange
        Historic historic = new Historic(Location.A, 1000);
        Alpha alphaCentre = new Alpha(Location.A, INITIAL_WASTE);
        Beta betaCentre = new Beta(Location.B, INITIAL_WASTE);
        Gamma gammaCentre = new Gamma(Location.C, INITIAL_WASTE);
        // Act & Assert Viable and Optimal Centers
        List<Recycling> recyclingCenters = new ArrayList<>(Arrays.asList(alphaCentre, betaCentre, gammaCentre));
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenters);
        List<Recycling> viableCenters = Utils.findViableCentres(historic, scenarioConfiguration.getRecycling());
        Recycling optimalCenter = Utils.findOptimalCentre(historic, viableCenters);

        assertEquals("Alpha", optimalCenter.getGeneration(), "Optimal center should be Alpha in Zone A");

        // Act & Assert Travel and Process Durations
        double travelDuration = Utils.calculateTravelDuration(historic, optimalCenter);
        double processDuration = Utils.calculateProcessDuration(historic, optimalCenter);
        assertTrue(travelDuration > 0, "Travel duration should be positive");
        assertTrue(processDuration > 0, "Process duration should be positive");
        System.out.printf("Test passed: Travel Duration: %.2f, Process Duration: %.2f%n", travelDuration, processDuration);
    }
}
