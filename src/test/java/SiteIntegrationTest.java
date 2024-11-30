import models.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SiteIntegrationTest {
    private final static int INITIAL_WASTE = 5000;

    @Test
    void testOptimalCenterSelectionWithEqualDistance() {
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
    @Test
    void testNoViableCenters() { // Test Case ID: testNoViableCenters
        Historic historic = new Historic(Location.A, 5000);
        Recycling center1 = new Alpha(Location.B, 10); // 4 hours away
        Recycling center2 = new Beta(Location.C, 15); // 5 hours away
        List<Recycling> recyclingCenters = new ArrayList<>();
        recyclingCenters.add(center1);
        recyclingCenters.add(center2);
        List<Recycling> viableCenters = Utils.findViableCentres(historic, recyclingCenters);
        assertTrue(viableCenters.isEmpty(), "No centers should be viable as all are beyond 3 hours.");
    }
    @Test
    void testAllCentersInSameZone() { // Test Case ID: testAllCentersInSameZone
        Historic historic = new Historic(Location.A, 5000);
        Recycling alphaCenter = new Alpha(Location.A, 10);
        Recycling betaCenter = new Beta(Location.A, 8);
        Recycling gammaCenter = new Gamma(Location.A, 5);
        List<Recycling> recyclingCenters = Arrays.asList(alphaCenter, betaCenter, gammaCenter);

        List<Recycling> viableCenters = Utils.findViableCentres(historic, recyclingCenters);
        assertTrue(viableCenters.contains(alphaCenter), "Alpha should be viable in the same zone.");
        assertTrue(viableCenters.contains(betaCenter), "Beta should be viable in the same zone.");
        assertTrue(viableCenters.contains(gammaCenter), "Gamma should be viable in the same zone.");

        Recycling optimalCenter = Utils.findOptimalCentre(historic, viableCenters);
        assertEquals("Gamma", optimalCenter.getGeneration(), "Gamma should be selected as the optimal center.");
    }
    @Test
    void testIdenticalCenters() { // Test Case ID: testIdenticalCenters
        Historic historic = new Historic(Location.A, 5000);
        Recycling gamma1 = new Gamma(Location.A, 5);
        Recycling gamma2 = new Gamma(Location.A, 5);
        Recycling gamma3 = new Gamma(Location.A, 5);
        List<Recycling> recyclingCenters = Arrays.asList(gamma1, gamma2, gamma3);

        List<Recycling> viableCenters = Utils.findViableCentres(historic, recyclingCenters);
        assertEquals(3, viableCenters.size(), "All identical Gamma centers should be viable.");

        Recycling optimalCenter = Utils.findOptimalCentre(historic, viableCenters);
        assertNotNull(optimalCenter, "An optimal center should still be selected among identical centers.");
        assertEquals("Gamma", optimalCenter.getGeneration(), "Optimal center should be one of the Gamma centers.");
    }
}
