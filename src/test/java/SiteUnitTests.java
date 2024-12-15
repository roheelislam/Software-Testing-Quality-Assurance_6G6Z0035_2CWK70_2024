import models.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SiteUnitTests {
    @Test
    @DisplayName("Find Viable Centers: Including Metallic Waste")
    void FindViableCentres_TC_001() {
        // Arrange
        double wasteVolume = 2000; // Including metallic waste
        Historic historic = new Historic(Location.B, wasteVolume);
        Recycling recyclingCenter1 = new Beta(Location.B, 5);
        Recycling recyclingCenter2 = new Alpha(Location.C, 3);
        var recyclingCenterList = new ArrayList<Recycling>();
        recyclingCenterList.add(recyclingCenter1);
        recyclingCenterList.add(recyclingCenter2);

        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenterList);
        // Act
        List<Recycling> viableCentres = Utils.findViableCentres(scenarioConfiguration.getHistoric(), scenarioConfiguration.getRecycling());

        // Assert
        assertTrue(viableCentres.size() > 0, "At least one viable center should be found.");
    }

    @Test
    @DisplayName("Find Viable Centers: Without Metallic Waste")
    void FindViableCentres_TC_002() {
        // Arrange
        double wasteVolume = 1000; // No metallic waste
        Historic historic = new Historic(Location.B, wasteVolume);
        Recycling recyclingCenter1 = new Alpha(Location.B, 3);
        Recycling recyclingCenter2 = new Beta(Location.C, 2);
        var recyclingCenterList = new ArrayList<Recycling>();
        recyclingCenterList.add(recyclingCenter1);
        recyclingCenterList.add(recyclingCenter2);
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenterList);
        // Act
        List<Recycling> viableCentres = Utils.findViableCentres(scenarioConfiguration.getHistoric(), scenarioConfiguration.getRecycling());

        // Assert that only Alpha and Beta centers are viable
        // Assert
        assertTrue(viableCentres.stream().anyMatch(center -> center instanceof Alpha), "Alpha centers should be viable.");
        assertTrue(viableCentres.stream().anyMatch(center -> center instanceof Beta), "Beta centers should be viable.");
    }

    @Test
    @DisplayName("Find Optimal Center: Nearest and Most Active")
    void FindOptimalCentre_TC_001() {
        // Arrange
        Historic historic = new Historic(Location.A, 3000);
        Recycling centerAlpha = new Alpha(Location.A, 10);
        Recycling centerBeta = new Beta(Location.B, 8);
        Recycling centerGamma = new Gamma(Location.C, 12);
        var recyclingCenterList = new ArrayList<Recycling>();
        recyclingCenterList.add(centerAlpha);
        recyclingCenterList.add(centerBeta);
        recyclingCenterList.add(centerGamma);

        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenterList);
        // Act
        Recycling optimalCenter = Utils.findOptimalCentre(scenarioConfiguration.getHistoric(), scenarioConfiguration.getRecycling());

        // Assert: Optimal center should be the nearest one with the highest years active, which is Alpha in Zone A
        // Assert
        assertTrue(optimalCenter == centerAlpha, "The optimal center should be the Alpha generation in Zone A with 10 years active.");
    }

    @Test
    @DisplayName("Find Viable Centers: All Centers in Zone B")
    void FindViableCentres_TC_004() {
        // Arrange
        Recycling center1 = new Alpha(Location.B, 5);
        Recycling center2 = new Alpha(Location.B, 5);
        Recycling center3 = new Alpha(Location.B, 5);
        var recyclingCenterList = Arrays.asList(center1, center2, center3);
        Historic historic = new Historic(Location.B, 2000);
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenterList);
        // Act
        List<Recycling> viableCenters = Utils.findViableCentres(scenarioConfiguration.getHistoric(), scenarioConfiguration.getRecycling());
        // Assert
        assertEquals(3, viableCenters.size(), "All centers should be marked as viable.");
        assertTrue(viableCenters.containsAll(recyclingCenterList), "All centers in Zone B should be in the viable list.");
    }

    @Test
    @DisplayName("Find Optimal Center: Fewest Active Years")
    void FindOptimalCentre_TC_002() {
        // Arrange
        Recycling centerWithFewerYears = new Alpha(Location.A, 5);
        Recycling centerWithMoreYears = new Alpha(Location.A, 10);
        var recyclingCenterList = Arrays.asList(centerWithFewerYears, centerWithMoreYears);
        Historic historic = new Historic(Location.A, 2000);
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenterList);
        // Act
        Recycling optimalCenter = Utils.findOptimalCentre(scenarioConfiguration.getHistoric(), scenarioConfiguration.getRecycling());
        // Assert
        assertEquals(centerWithFewerYears, optimalCenter, "The optimal center should be the one with the fewest active years.");
    }

    @Test
    @DisplayName("Boundary Case: Viable Center at 3-Hour Travel Limit")
    void Boundary_TC_001() {
        // Arrange
        Historic historic = new Historic(Location.A, 1000);
        Recycling boundaryCenter = new Alpha(Location.B, 3); // Center exactly 3 hours

        var recyclingCenterList = Arrays.asList(boundaryCenter);
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, recyclingCenterList);
        // Act
        List<Recycling> viableCenters = Utils.findViableCentres(scenarioConfiguration.getHistoric(), scenarioConfiguration.getRecycling());
        // Assert
        assertTrue(viableCenters.contains(boundaryCenter), "Center at the exact 3-hour limit should be marked as viable.");
    }

    @Test
    @DisplayName("Edge Case: Viable Center with Only Metallic Waste")
    void EdgeCase_TC_002() {
        // Arrange
        Historic landfill = new Historic(Location.A, 1000.0);
        landfill.setMetallic(1000.0); // Only metallic waste
        landfill.setPlasticGlass(0);
        landfill.setPaper(0);

        Recycling center = new Gamma(Location.C, 10);
        var centerList = new ArrayList<Recycling>();
        centerList.add(center);
        // Act
        List<Recycling> viableCenters = Utils.findViableCentres(landfill, centerList);

        // Assert that the Gamma center is considered viable due to metallic waste presence
        // Assert
        assertTrue(viableCenters.contains(center), "Gamma center should be viable for metallic-only waste.");
    }

    @Test
    @DisplayName("Validation: Process Duration Calculation for Gamma Center")
    void Validation_TC_002() {
        // Arrange
        Recycling gammaCenter = new Gamma(Location.A, 8);
        List<Double> rates = gammaCenter.getRates();
        double plasticVolume = 300;
        double metallicVolume = 400;
        double paperVolume = 500;
        double totalWaste = plasticVolume + metallicVolume + paperVolume;


        Historic historic = new Historic(Location.A, totalWaste);
        historic.setPlasticGlass(plasticVolume);
        historic.setMetallic(metallicVolume);
        historic.setPaper(paperVolume);
        // Act
        double actualTotalDuration = Utils.calculateProcessDuration(historic, gammaCenter);
        // Assert
        double expectedPlasticDuration = plasticVolume / rates.get(0);   // plastic
        double expectedMetallicDuration = metallicVolume / rates.get(1); // metallic
        double expectedPaperDuration = paperVolume / rates.get(2);       // paper
        double expectedTotalDuration = expectedPlasticDuration + expectedMetallicDuration + expectedPaperDuration;
        assertEquals(expectedTotalDuration, actualTotalDuration, "Gamma center processing duration should match expected calculation.");
    }

    @Test
    @DisplayName("Validation: Viability with Plastic-Only Waste")
    void Validation_TC_003() {
        // Arrange
        Recycling alphaCenter = new Alpha(Location.A, 5);
        Recycling betaCenter = new Beta(Location.B, 7);
        Recycling gammaCenter = new Gamma(Location.B, 8);

        Historic landfill = new Historic(Location.A, 1000);
        landfill.setPlasticGlass(1000); // Plastic-only waste
        landfill.setMetallic(0);        // No metallic waste
        landfill.setPaper(0);           // No paper waste
        var centerList = new ArrayList<Recycling>();
        centerList.add(alphaCenter);
        centerList.add(betaCenter);
        centerList.add(gammaCenter);
        // Act
        List<Recycling> viableCenters = Utils.findViableCentres(landfill, centerList);
        // Assert
        assertTrue(viableCenters.contains(alphaCenter), "Alpha should be viable for plastic waste.");
        assertTrue(viableCenters.contains(betaCenter), "Beta should be viable for plastic waste.");
        assertFalse(viableCenters.contains(gammaCenter), "Gamma should not be viable for plastic waste only.");
    }

    @Test
    @DisplayName("Math Validation: Optimal Center Based on Generation and Active Years")
    void Math_TC_006() {
        // Arrange
        Recycling alphaCenter = new Alpha(Location.B, 5);  // Alpha with 5 years active
        Recycling betaCenter = new Beta(Location.B, 3);    // Beta with 3 years active
        Historic landfill = new Historic(Location.B, 1500);
        var centerList = new ArrayList<Recycling>();
        centerList.add(alphaCenter);
        centerList.add(betaCenter);
        // Act
        Recycling optimalCenter = Utils.findOptimalCentre(landfill, centerList);
        // Assert
        assertEquals(betaCenter, optimalCenter, "Beta should be selected as the optimal center due " +
                                                "to higher generation and fewer years active.");
    }
}
