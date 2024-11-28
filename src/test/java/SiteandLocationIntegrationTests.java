//import models.Location;
//import models.Recycling;
//import models.Gamma;
//import models.Alpha;
//import models.Historic;
//import models.Location;
//import models.Site;
//import org.junit.jupiter.api.Test;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class SiteandLocationIntegrationTests {
//
//
//    //Positive Test Case: Validate Site Location
//    @Test
//    void testSiteLocation() {
//        // Arrange
//        Site site = new Historic(Location.C, 100.0);
//
//        // Act
//        Location location = site.getLocation();
//
//        // Assert
//        assertEquals(Location.C, location, "Site location should be correctly assigned to Location C");
//    }
//
//
//    //Negative Test Case: Null Location
//    @Test
//    void testNullSiteLocation() {
//        // Arrange
//        Site site = new Historic(null, 100.0);
//
//        // Act & Assert
//        assertThrows(NullPointerException.class, site::getLocation, "Null location should throw an exception");
//    }
//
//}
