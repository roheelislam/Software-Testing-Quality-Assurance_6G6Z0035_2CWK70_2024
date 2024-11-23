import models.Location;
import models.Recycling;
import models.Gamma;
import models.Alpha;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RecyclingandTransportIntegrationTests {


    //Positive Test Case: Validate Recycling Rates
    @Test
    void testRecyclingRates() { //same as testRatesMatchExpectedOrder om the GammaTests
        // Arrange
        Recycling recycling = new Gamma(Location.B, 5);

        // Act
        List<Double> rates = recycling.getRates();

        // Assert
        assertEquals(List.of(1.5, 2.0, 3.0), rates, "Gamma recycling rates should match the expected values");
    }


    //Negative Test Case: Invalid Recycling Center Generation
    @Test
    void testInvalidRecyclingGeneration() {
        // Arrange
        Recycling recycling = new Alpha(Location.A, 5);

        // Act
        String generation = recycling.getGeneration();

        // Assert
        assertNotEquals("Beta", generation, "Alpha recycling center should not have a Beta generation");
    }


}