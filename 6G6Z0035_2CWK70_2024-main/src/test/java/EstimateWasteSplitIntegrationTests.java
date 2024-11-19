import models.Historic;
import models.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EstimateWasteSplitIntegrationTests {
    @Test
    public void testWasteSplitBelowThreshold() {
        Historic historic = new Historic(Location.A, 1000); //less than 1250cm3

        double expectedPlasticGlass = 500.0; // 50%
        double expectedPaper = 500.0; // 50%
        double expectedMetallic = 0.0;

        assertEquals(expectedPlasticGlass, historic.getPlasticGlass(), "Plastic/Glass split mismatch");
        assertEquals(expectedPaper, historic.getPaper(), "Paper split mismatch");
        assertEquals(expectedMetallic, historic.getMetallic(), "Metallic should be zero below threshold");
    }
}
