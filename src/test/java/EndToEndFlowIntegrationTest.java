import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import models.Location;
import models.Recycling;
import models.Alpha;
import models.Gamma;
import models.Beta;
import models.Historic;


import java.io.ByteArrayInputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class EndToEndFlowIntegrationTest {

    // Helper method to create a sample Historic site
    private Historic createSampleHistoric(Location location, double initialWaste) {
        return new Historic(location, initialWaste);
    }

    // Helper method to create sample Recycling centers
    private List<Recycling> createSampleRecyclingCenters() {
        List<Recycling> recyclingCenters = new ArrayList<>();
        recyclingCenters.add(new Alpha(Location.A, 10));
        recyclingCenters.add(new Beta(Location.B, 5));
        recyclingCenters.add(new Gamma(Location.C, 2));
        return recyclingCenters;
    }


    @Test
    void testScenarioWithInsufficientWaste() {
        // Arrange
        Historic historic = createSampleHistoric(Location.A, 10.0); // Insufficient waste
        List<Recycling> recyclingCenters = createSampleRecyclingCenters();

        // Act
        List<Recycling> viableCenters = Utils.findViableCentres(historic, recyclingCenters);
        Recycling optimalCenter = null;
        double travelDuration = -1;
        double processDuration = 0;

        if (!viableCenters.isEmpty()) {
            optimalCenter = Utils.findOptimalCentre(historic, viableCenters);
            travelDuration = Utils.calculateTravelDuration(historic, optimalCenter);
            processDuration = Utils.calculateProcessDuration(historic, optimalCenter);
        }

        // Assert
        assertNotNull(viableCenters);
        assertTrue(viableCenters.isEmpty());
        assertNull(optimalCenter);
        assertEquals(-1, travelDuration);
        assertEquals(0, processDuration);
    }

    @Test
    void testScenarioWithNoMetallicWaste() {
        // Arrange
        Historic historic = createSampleHistoric(Location.A, 1200.0); // No metallic waste
        List<Recycling> recyclingCenters = createSampleRecyclingCenters();

        // Act
        List<Recycling> viableCenters = Utils.findViableCentres(historic, recyclingCenters);
        Recycling optimalCenter = Utils.findOptimalCentre(historic, viableCenters);
        double travelDuration = Utils.calculateTravelDuration(historic, optimalCenter);
        double processDuration = Utils.calculateProcessDuration(historic, optimalCenter);

        // Assert
        assertNotNull(viableCenters);
        assertTrue(viableCenters.stream().noneMatch(c -> c.getGeneration().equals("Gamma")));
        assertNotNull(optimalCenter);
        assertTrue(travelDuration > 0);
        assertTrue(processDuration > 0);
    }

    @Test
    void testScenarioWithExcessiveTravelTime() {
        // Arrange
        Historic historic = createSampleHistoric(Location.C, 2000.0); // Far location
        List<Recycling> recyclingCenters = createSampleRecyclingCenters();

        // Act
        List<Recycling> viableCenters = Utils.findViableCentres(historic, recyclingCenters);
        Recycling optimalCenter = null;

        if (!viableCenters.isEmpty()) {
            optimalCenter = Utils.findOptimalCentre(historic, viableCenters);
        }

        // Assert
        assertNotNull(viableCenters);
        assertTrue(viableCenters.isEmpty()); // All centers filtered out due to travel time
        assertNull(optimalCenter);
    }

    @Test
    void testScenarioWithEdgeCaseWasteSplit() {
        // Arrange
        Historic historic = createSampleHistoric(Location.B, 1250.0); // On the metallic threshold
        createSampleRecyclingCenters();

        // Act
        double metallicWaste = historic.getMetallic();
        double paperWaste = historic.getPaper();
        double plasticGlassWaste = historic.getPlasticGlass();

        // Assert
        assertEquals(250.0, metallicWaste);
        assertEquals(625.0, paperWaste);
        assertEquals(375.0, plasticGlassWaste);
    }

    @Test
    void testOptimalCenterSelection() {
        // Arrange
        Historic historic = createSampleHistoric(Location.A, 2000.0);
        List<Recycling> recyclingCenters = createSampleRecyclingCenters();

        // Act
        List<Recycling> viableCenters = Utils.findViableCentres(historic, recyclingCenters);
        Recycling optimalCenter = Utils.findOptimalCentre(historic, viableCenters);

        // Assert
        assertNotNull(optimalCenter);
        assertEquals("Gamma", optimalCenter.getGeneration()); // Highest generation
    }


    //Valid Scenario with Full Configuration
    @Test
    void testEndToEndValidScenario() {
        // Arrange
        Historic historic = new Historic(Location.A, 2000);
        Recycling alphaCenter = new Alpha(Location.B, 10);
        Recycling betaCenter = new Beta(Location.C, 5);
        List<Recycling> recyclingCenters = new ArrayList<>(List.of(alphaCenter, betaCenter));

        // Act
        List<Recycling> viableCenters = Utils.findViableCentres(historic, recyclingCenters);
        Recycling optimalCenter = Utils.findOptimalCentre(historic, viableCenters);
        double travelDuration = Utils.calculateTravelDuration(historic, optimalCenter);
        double processDuration = Utils.calculateProcessDuration(historic, optimalCenter);

        // Assert
        assertNotNull(viableCenters);
        assertNotNull(optimalCenter);
        assertTrue(travelDuration > 0);
        assertTrue(processDuration > 0);
        assertEquals(Location.B, optimalCenter.getLocation());
    }


    //No Viable Recycling Centers
    @Test
    void testEndToEndNoViableRecyclingCenters() {
        // Arrange
        Historic historic = new Historic(Location.A, 2000);
        Recycling gammaCenter = new Gamma(Location.C, 4); // Gamma center too far
        List<Recycling> recyclingCenters = new ArrayList<>(List.of(gammaCenter));

        // Act
        List<Recycling> viableCenters = Utils.findViableCentres(historic, recyclingCenters);

        // Assert
        assertTrue(viableCenters.isEmpty());
    }


    //Invalid Waste Distribution
    @Test
    void testInvalidWasteDistribution() {
        // Arrange
        Historic historic = new Historic(Location.A, 15); // Below transport capacity
        Recycling alphaCenter = new Alpha(Location.B, 10);
        List<Recycling> recyclingCenters = List.of(alphaCenter);

        // Act
        double travelDuration = Utils.calculateTravelDuration(historic, alphaCenter);

        // Assert
        assertEquals(-1.0, travelDuration, 0.01);
    }


    //Edge Case with Excessive Waste
    @Test
    void testExcessiveWaste() {
        // Arrange
        Historic historic = new Historic(Location.C, 50000); // Large amount of waste
        Recycling gammaCenter = new Gamma(Location.A, 2);
        List<Recycling> recyclingCenters = List.of(gammaCenter);

        // Act
        double travelDuration = Utils.calculateTravelDuration(historic, gammaCenter);
        double processDuration = Utils.calculateProcessDuration(historic, gammaCenter);

        // Assert
        assertTrue(travelDuration > 0);
        assertTrue(processDuration > 0);
    }


    //Fail Case with Invalid Location
    @Test
    void testInvalidLocation() throws Exception {
        // Arrange
        ByteArrayInputStream in = new ByteArrayInputStream("D\nC".getBytes()); // Invalid, then valid input
        System.setIn(in);

        // Use reflection to access the private method
        Method collectLocationMethod = Main.class.getDeclaredMethod("collectLocation");
        collectLocationMethod.setAccessible(true); // Allow access to the private method

        // Act
        Location location = (Location) collectLocationMethod.invoke(null); // Invoke the static private method

        // Assert
        assertEquals(Location.C, location); // Verify the valid input was finally accepted
    }


    // Empty Recycling Centers
    @Test
    void testEmptyRecyclingCenters() {
        // Arrange
        Historic historic = new Historic(Location.B, 1000);
        List<Recycling> recyclingCenters = new ArrayList<>(); // No centers provided

        // Act
        List<Recycling> viableCenters = Utils.findViableCentres(historic, recyclingCenters);

        // Assert
        assertTrue(viableCenters.isEmpty());
    }


    //Recycling Center Too Old to Be Optimal
    @Test
    void testRecyclingCenterTooOld() {
        // Arrange
        Historic historic = new Historic(Location.A, 3000);
        Recycling betaCenter = new Beta(Location.B, 50); // Very old center
        Recycling alphaCenter = new Alpha(Location.C, 2);
        List<Recycling> recyclingCenters = new ArrayList<>(List.of(betaCenter, alphaCenter));

        // Act
        Recycling optimalCenter = Utils.findOptimalCentre(historic, recyclingCenters);

        // Assert
        assertEquals("Alpha", optimalCenter.getGeneration());
    }


    //Fail Case with No Historic Site
    @Test
    void testNoHistoricSite() {
        // Arrange
        ScenarioConfiguration configuration = new ScenarioConfiguration(null, new ArrayList<>());

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            Utils.findViableCentres(configuration.getHistoric(), configuration.getRecycling());
        });
    }


}
