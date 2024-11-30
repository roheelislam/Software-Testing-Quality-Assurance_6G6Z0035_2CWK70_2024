import models.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EstimateWasteSplitIntegrationTests {
    @Test
    void testWasteSplitBelowThreshold() {
        Historic historic = new Historic(Location.A, 1000); //less than 1250cm3

        double expectedPlasticGlass = 500.0; // 50%
        double expectedPaper = 500.0; // 50%
        double expectedMetallic = 0.0;

        assertEquals(expectedPlasticGlass, historic.getPlasticGlass(), "Plastic/Glass split mismatch");
        assertEquals(expectedPaper, historic.getPaper(), "Paper split mismatch");
        assertEquals(expectedMetallic, historic.getMetallic(), "Metallic should be zero below threshold");
    }
    @Test
    void testExtremeWasteVolume() { // Test Case ID: testExtremeWasteVolume
        Historic historic = new Historic(Location.A, 100000);
        Recycling alphaCenter = new Alpha(Location.A, 10);
        Recycling betaCenter = new Beta(Location.B, 8);
        Recycling gammaCenter = new Gamma(Location.C, 5);
        List<Recycling> recyclingCenters = new ArrayList<>();
        recyclingCenters.add(alphaCenter);
        recyclingCenters.add(betaCenter);
        recyclingCenters.add(gammaCenter);

        List<Recycling> viableCenters = Utils.findViableCentres(historic, recyclingCenters);
        assertFalse(viableCenters.isEmpty(), "Centers should still be viable despite the high waste volume.");

        Recycling optimalCenter = Utils.findOptimalCentre(historic, viableCenters);
        double travelDuration = Utils.calculateTravelDuration(historic, optimalCenter);
        double processDuration = Utils.calculateProcessDuration(historic, optimalCenter);

        assertTrue(travelDuration > 0, "Travel duration should be positive for large waste volumes.");
        assertTrue(processDuration > 0, "Process duration should be positive for large waste volumes.");
    }
    @Test
    void testEmptyWasteConfiguration() { // Test Case ID: testEmptyWasteConfiguration
        Historic historic = new Historic(Location.A, 0);
        Recycling alphaCenter = new Alpha(Location.A, 10);
        Recycling betaCenter = new Beta(Location.B, 8);
        Recycling gammaCenter = new Gamma(Location.C, 5);
        List<Recycling> recyclingCenters = Arrays.asList(alphaCenter, betaCenter, gammaCenter);

        double travelDuration = Utils.calculateTravelDuration(historic, alphaCenter);
        double processDuration = Utils.calculateProcessDuration(historic, alphaCenter);

        assertEquals(0, travelDuration, "Travel duration should be 0 for 0 waste.");
        assertEquals(0, processDuration, "Process duration should be 0 for 0 waste.");
    }
}
