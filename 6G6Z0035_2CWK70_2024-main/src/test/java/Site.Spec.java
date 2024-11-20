import models.Location;
import models.Site;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Concrete subclass of Site for testing
class TestSite extends Site {
    TestSite(Location location) {
        super(location);
    }
}

public class SiteTest {

    @Test
    public void testGetLocation() {
        // Arrange: Set up the location and create an instance of the test subclass
        Location expectedLocation = Location.B;
        Site testSite = new TestSite(expectedLocation);

        // Act: Call the getLocation method
        Location actualLocation = testSite.getLocation();

        // Assert: Verify that the returned location matches the expected location
        assertEquals(expectedLocation, actualLocation, "The getLocation method should return the correct location.");
    }
}
