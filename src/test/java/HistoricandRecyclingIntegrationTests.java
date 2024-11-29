import models.Location;
import models.Recycling;
import models.Gamma;
import models.Alpha;
import models.Historic;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoricandRecyclingIntegrationTests {


    //Positive Test Case: Waste Distribution Across Recycling Centers
    @Test
    void testWasteDistributionToRecycling() {
        // Arrange
        Historic historic = new Historic(Location.A, 5000.0);
        Recycling recycling = new Gamma(Location.B, 10);

        // Act
        double plasticRate = historic.getPlasticGlass();
        double metallicRate = historic.getMetallic();

        // Assert
        assertEquals(1500.0, plasticRate, "Plastic waste split should be 30% of the total for waste above 1250 cubic meters");
        assertEquals(1000.0, metallicRate, "Metallic waste split should be 20% of the total for waste above 1250 cubic meters");
    }


    @Test
    void testAssignNonViableCenter() {
        // Arrange
        Historic historic = new Historic(Location.A, 500.0); // No metallic waste
        List<Recycling> centers = List.of(
                new Gamma(Location.B, 10), // Non-viable due to generation
                new Alpha(Location.C, 5)  // Viable
        );

        // Act
        List<Recycling> viableCenters = Utils.findViableCentres(historic, new ArrayList<>(centers));

        // Assert
        assertFalse(viableCenters.contains(centers.get(0)), "Gamma center should not be viable when no metallic waste is present");
    }


}