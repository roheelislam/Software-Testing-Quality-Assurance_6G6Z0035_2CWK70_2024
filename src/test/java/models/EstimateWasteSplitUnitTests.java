package models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EstimateWasteSplitUnitTests {
    @Test
    @DisplayName("Edge Case: Validate Waste Split at Threshold (1250 m³)")
    void EdgeCase_TC_001() {
        // Arrange
        Location zoneA = Location.A;
        double wasteVolume = 1250;
        Historic historic = new Historic(zoneA, wasteVolume);
        Recycling recyclingCenter = new Alpha(zoneA, 10);
        var recyclingCenterList = new ArrayList<Recycling>();
        recyclingCenterList.add(recyclingCenter);
        // Assert the correct distribution of waste
        // Assert
        assertEquals(625, historic.getPaper(), "Paper waste should be 625 m³ (50%).");
        assertEquals(625, historic.getPlasticGlass(), "Plastic/Glass waste should be 625 m³ (50%).");
        assertEquals(0, historic.getMetallic(), "Metallic waste should be 0 m³ (0%).");
    }

    @Test
    @DisplayName("Estimate Waste Split: Validate Distribution for 1000 m³ Waste")
    void EstimateWasteSplit_TC_001() {
        // Arrange
        double wasteVolume = 1000;
        Historic historic = new Historic(Location.B, wasteVolume);
        // Assert
        assertEquals(500.0, historic.getPlasticGlass(), "Plastic/Glass waste should be 50% of total.");
        assertEquals(500.0, historic.getPaper(), "Paper waste should be 50% of total.");
    }

    @Test
    @DisplayName("Estimate Waste Split: Validate Distribution for 5000 m³ Waste")
    void EstimateWasteSplit_TC_002() {
        // Arrange
        double wasteVolume = 5000;
        Historic historic = new Historic(Location.B, wasteVolume);
        // Assert
        assertEquals(2500.0, historic.getPaper(), "Paper waste should be 50% of total.");
        assertEquals(1500.0, historic.getPlasticGlass(), "Plastic/Glass waste should be 30% of total.");
        assertEquals(1000.0, historic.getMetallic(), "Metallic waste should be 20% of total.");
    }


    @Test
    @DisplayName("Estimate Waste Split: Validate Distribution for 1250 m³ Waste")
    void EstimateWasteSplit_TC_003() {
        // Arrange
        double wasteVolume = 1250;
        Historic historic = new Historic(Location.B, wasteVolume);
        // Assert
        assertEquals(625, historic.getPlasticGlass(), "Plastic/Glass waste should be 625 m³ (50%).");
        assertEquals(625, historic.getPaper(), "Paper waste should be 625 m³ (50%).");
        assertEquals(0, historic.getMetallic(), "Metallic waste should be 0 m³ (0%).");
    }

    @Test
    @DisplayName("Validation: Validate Waste Split Calculation for Threshold Waste")
    void Validation_TC_001() {
        // Arrange
        double initialWaste = 1250;
        Historic historic = new Historic(Location.A, initialWaste);
        // Assert
        assertEquals(625, historic.getPlasticGlass(), 0.1, "Plastic/Glass waste should be 625 cubic meters.");
        assertEquals(625, historic.getPaper(), 0.1, "Paper waste should be 625 cubic meters.");
    }

    @Test
    @DisplayName("Math Validation: Validate Distribution for 5000 m³ Waste")
    void Math_TC_001() {
        // Arrange
        double totalWaste = 5000;
        Historic historic = new Historic(Location.A, totalWaste);
        // Assert
        assertEquals(2500, historic.getPaper(), 0.1, "Paper waste should be 50% of total (2500 m³).");
        assertEquals(1500, historic.getPlasticGlass(), 0.1, "Plastic/Glass waste should be 30% of total (1500 m³).");
        assertEquals(1000, historic.getMetallic(), 0.1, "Metallic waste should be 20% of total (1000 m³).");
    }

}
