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
        Alpha alphaCentre = new Alpha(Location.A, INITIAL_WASTE);
        Beta betaCentre = new Beta(Location.B, INITIAL_WASTE);
        Gamma gammaCentre = new Gamma(Location.C, INITIAL_WASTE);
        List<Recycling> recyclingCenters = new ArrayList<>(Arrays.asList(alphaCentre, betaCentre, gammaCentre));
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenters);
        scenarioConfiguration.addRecycling(betaCenterA);
        scenarioConfiguration.addRecycling(gammaCenterA);

        List<Recycling> viableCenters = Utils.findViableCentres(historic, scenarioConfiguration.getRecycling());
        Recycling optimalCenter = Utils.findOptimalCentre(historic, viableCenters);

        assertEquals("Gamma", optimalCenter.getGeneration(), "Optimal center should be Gamma with higher generation");
    }
}
