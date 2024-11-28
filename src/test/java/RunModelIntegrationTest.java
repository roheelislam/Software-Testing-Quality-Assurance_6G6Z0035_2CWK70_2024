import models.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RunModelIntegrationTest {
    private final static int INITIAL_WASTE = 5000;

    @Test
    public void testFullScenarioExecution() {
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

        List<Recycling> viableCenters = Utils.findViableCentres(historic, scenarioConfiguration.getRecycling());
        assertTrue(viableCenters.contains(alphaCentre));
        assertTrue(viableCenters.contains(betaCentre));
        assertFalse(viableCenters.contains(gammaCentre));

        Recycling optimalCenter = Utils.findOptimalCentre(historic, viableCenters);
        assertEquals("Alpha", optimalCenter.getGeneration(), "Optimal center should be Alpha as it's closest");

        double travelDuration = Utils.calculateTravelDuration(historic, optimalCenter);
        double processDuration = Utils.calculateProcessDuration(historic, optimalCenter);

        assertTrue(travelDuration > 0, "Travel duration should be greater than 0");
        assertTrue(processDuration > 0, "Process duration should be greater than 0");
        System.out.printf("Travel Duration: %.2f hours, Process Duration: %.2f hours, Total Duration: %.2f hours%n",
                travelDuration, processDuration, travelDuration + processDuration);
    }

    @Test
    public void testCalculateTravelAndProcessDurationWithOptimalCenter() { //Site
        Historic historic = new Historic(Location.A, 1000);
        Alpha alphaCentre = new Alpha(Location.A, INITIAL_WASTE);
        Beta betaCentre = new Beta(Location.B, INITIAL_WASTE);
        Gamma gammaCentre = new Gamma(Location.C, INITIAL_WASTE);
        List<Recycling> recyclingCenters = new ArrayList<>(Arrays.asList(alphaCentre, betaCentre, gammaCentre));
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenters);
        List<Recycling> viableCenters = Utils.findViableCentres(historic, scenarioConfiguration.getRecycling());
        Recycling optimalCenter = Utils.findOptimalCentre(historic, viableCenters);

        assertEquals("Alpha", optimalCenter.getGeneration(), "Optimal center should be Alpha in Zone A");

        double travelDuration = Utils.calculateTravelDuration(historic, optimalCenter);
        double processDuration = Utils.calculateProcessDuration(historic, optimalCenter);
        assertTrue(travelDuration > 0, "Travel duration should be positive");
        assertTrue(processDuration > 0, "Process duration should be positive");
        System.out.printf("Test passed: Travel Duration: %.2f, Process Duration: %.2f%n", travelDuration, processDuration);
    }
}
