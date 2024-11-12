import models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationIntegrationTest {
    private ScenarioConfiguration scenarioConfig;
    private Historic historic;
    private Recycling alphaCentre;
    private Recycling betaCentre;
    private Recycling gammaCentre;

    @BeforeEach
    public void setUp() {
        historic = new Historic(Location.A, 5000.0);
        alphaCentre = new Alpha(Location.A, 10);
        betaCentre = new Beta(Location.B, 8);
        gammaCentre = new Gamma(Location.C, 12);

        List<Recycling> recyclingCenters = new ArrayList<>(Arrays.asList(alphaCentre, betaCentre, gammaCentre));
        scenarioConfig = new ScenarioConfiguration(historic, recyclingCenters);
    }

    @Test
    public void testFullScenarioExecution() {
        double expectedPlasticGlass = 1500.0; // 30%
        double expectedPaper = 2500.0; // 50%
        double expectedMetallic = 1000.0; // 20%

        assertEquals(expectedPlasticGlass, historic.getPlasticGlass(), "Plastic/Glass split mismatch");
        assertEquals(expectedPaper, historic.getPaper(), "Paper split mismatch");
        assertEquals(expectedMetallic, historic.getMetallic(), "Metallic split mismatch");

        List<Recycling> viableCenters = Utils.findViableCentres(historic, scenarioConfig.getRecycling());
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
    public void testWasteSplitBelowThreshold() {
        historic.setRemainingWaste(1000.0);
        historic.estimateWasteSplit(1000.0);

        double expectedPlasticGlass = 500.0; // 50%
        double expectedPaper = 500.0; // 50%
        double expectedMetallic = 0.0;

        assertEquals(expectedPlasticGlass, historic.getPlasticGlass(), "Plastic/Glass split mismatch");
        assertEquals(expectedPaper, historic.getPaper(), "Paper split mismatch");
        assertEquals(expectedMetallic, historic.getMetallic(), "Metallic should be zero below threshold");
    }

    @Test
    public void testCalculateTravelAndProcessDurationWithOptimalCenter() {
        List<Recycling> viableCenters = Utils.findViableCentres(historic, scenarioConfig.getRecycling());
        Recycling optimalCenter = Utils.findOptimalCentre(historic, viableCenters);

        assertEquals("Alpha", optimalCenter.getGeneration(), "Optimal center should be Alpha in Zone A");
        
        double travelDuration = Utils.calculateTravelDuration(historic, optimalCenter);
        double processDuration = Utils.calculateProcessDuration(historic, optimalCenter);
        assertTrue(travelDuration > 0, "Travel duration should be positive");
        assertTrue(processDuration > 0, "Process duration should be positive");
        System.out.printf("Test passed: Travel Duration: %.2f, Process Duration: %.2f%n", travelDuration, processDuration);
    }

    @Test
    public void testOptimalCenterSelectionWithEqualDistance() {
        Recycling betaCenterA = new Beta(Location.A, 3);
        Recycling gammaCenterA = new Gamma(Location.A, 2);
        scenarioConfig.addRecycling(betaCenterA);
        scenarioConfig.addRecycling(gammaCenterA);

        List<Recycling> viableCenters = Utils.findViableCentres(historic, scenarioConfig.getRecycling());
        Recycling optimalCenter = Utils.findOptimalCentre(historic, viableCenters);

        assertEquals("Gamma", optimalCenter.getGeneration(), "Optimal center should be Gamma with higher generation");
    }
}
