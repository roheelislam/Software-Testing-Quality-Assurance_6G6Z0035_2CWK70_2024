import models.Alpha;
import models.Historic;
import models.Location;
import models.Recycling;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EstimateWasteSplitUnitTests {
    @Test
    public void EdgeCase_TC_001() {
        Location zoneA = Location.A;
        double wasteVolume = 1250;
        Historic historic = new Historic(zoneA, wasteVolume);
        Recycling recyclingCenter = new Alpha(zoneA, 10);
        var recyclingCenterList = new ArrayList<Recycling>();
        recyclingCenterList.add(recyclingCenter);
        // Assert the correct distribution of waste
        assertEquals(historic.getPaper(), 625, "Paper waste should be 625 m³ (50%).");
        assertEquals(historic.getPlasticGlass(), 625, "Plastic/Glass waste should be 625 m³ (50%).");
        assertEquals(historic.getMetallic(), 0, "Metallic waste should be 0 m³ (0%).");
    }

    @Test
    public void EstimateWasteSplit_TC_001() {
        double wasteVolume = 1000;
        Historic historic = new Historic(Location.B, wasteVolume);
        assertEquals(500.0, historic.getPlasticGlass(), "Plastic/Glass waste should be 50% of total.");
        assertEquals(500.0, historic.getPaper(), "Paper waste should be 50% of total.");
    }

    @Test
    public void EstimateWasteSplit_TC_002() {
        double wasteVolume = 5000;
        Historic historic = new Historic(Location.B, wasteVolume);
        assertEquals(2500.0, historic.getPaper(), "Paper waste should be 50% of total.");
        assertEquals(1500.0, historic.getPlasticGlass(), "Plastic/Glass waste should be 30% of total.");
        assertEquals(1000.0, historic.getMetallic(), "Metallic waste should be 20% of total.");
    }


    @Test
    public void EstimateWasteSplit_TC_003() {
        double wasteVolume = 1250;
        Historic historic = new Historic(Location.B, wasteVolume);
        assertEquals(historic.getPlasticGlass(), 625, "Plastic/Glass waste should be 625 m³ (50%).");
        assertEquals(historic.getPaper(), 625, "Paper waste should be 625 m³ (50%).");
        assertEquals(historic.getMetallic(), 0, "Metallic waste should be 0 m³ (0%).");
    }

    @Test
    public void Validation_TC_001() {
        double initialWaste = 1250;
        Historic historic = new Historic(Location.A, initialWaste);
        assertEquals(625, historic.getPlasticGlass(), 0.1, "Plastic/Glass waste should be 625 cubic meters.");
        assertEquals(625, historic.getPaper(), 0.1, "Paper waste should be 625 cubic meters.");
    }

    @Test
    public void Math_TC_001() {
        double totalWaste = 5000;
        Historic historic = new Historic(Location.A, totalWaste);
        assertEquals(2500, historic.getPaper(), 0.1, "Paper waste should be 50% of total (2500 m³).");
        assertEquals(1500, historic.getPlasticGlass(), 0.1, "Plastic/Glass waste should be 30% of total (1500 m³).");
        assertEquals(1000, historic.getMetallic(), 0.1, "Metallic waste should be 20% of total (1000 m³).");
    }

}
