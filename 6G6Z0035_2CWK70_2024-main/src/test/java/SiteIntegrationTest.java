import models.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SiteIntegrationTest {
    private final static int INITIAL_WASTE = 5000;

    @Test
    public void testOptimalCenterSelectionWithEqualDistance() {
        Historic historic = new Historic(Location.A, 1000);
        Recycling betaCenterA = new Beta(Location.A, 3);
        Recycling gammaCenterA = new Gamma(Location.A, 2);
        List<Recycling> recyclingCenters = new ArrayList<>(Arrays.asList(betaCenterA, gammaCenterA));
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenters);

        List<Recycling> viableCenters = Utils.findViableCentres(historic, scenarioConfiguration.getRecycling());

        // Identify the optimal center
        Recycling optimalCenter = Utils.findOptimalCentre(historic, viableCenters);

        assertEquals("Gamma", optimalCenter.getGeneration(), "Optimal center should be Gamma with higher generation and younger age.");
    }
}
