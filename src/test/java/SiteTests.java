//import models.Site;
//import models.Location;
//
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class SiteTests {
//
//    // Positive Test Cases
//    @Test
//    void testConstructorWithValidLocationA() {
//        // Arrange
//        Location location = Location.A;
//
//        // Act
//        Site site = new TestSite(location);
//
//        // Assert
//        assertEquals(location, site.getLocation(), "The location should be set to Location A");
//    }
//
//    @Test
//    void testConstructorWithValidLocationB() {
//        // Arrange
//        Location location = Location.B;
//
//        // Act
//        Site site = new TestSite(location);
//
//        // Assert
//        assertEquals(location, site.getLocation(), "The location should be set to Location B");
//    }
//
//    @Test
//    void testConstructorWithValidLocationC() {
//        // Arrange
//        Location location = Location.C;
//
//        // Act
//        Site site = new TestSite(location);
//
//        // Assert
//        assertEquals(location, site.getLocation(), "The location should be set to Location C");
//    }
//
//    // Negative Test Cases
//    @Test
//    void testConstructorWithNullLocation() {
//        // Arrange
//        Location location = null;
//
//        // Act & Assert
//        Exception exception = assertThrows(NullPointerException.class, () -> new TestSite(location));
//        assertNotNull(exception, "Constructor should throw NullPointerException when location is null");
//    }
//
//    @Test
//    void testGetLocationForUninitializedSite() {
//        // Arrange
//        Site site = null;
//
//        // Act & Assert
//        Exception exception = assertThrows(NullPointerException.class, () -> site.getLocation());
//        assertNotNull(exception, "Accessing getLocation on a null Site should throw NullPointerException");
//    }
//
//    // Edge Case Test Cases
//    @Test
//    void testImmutableLocation() {
//        // Arrange
//        Location location = Location.A;
//        Site site = new TestSite(location);
//
//        // Act
//        Location retrievedLocation = site.getLocation();
//
//        // Assert
//        assertEquals(location, retrievedLocation, "The location should remain immutable and match the original value");
//    }
//
//    @Test
//    void testSubclassInstanceBehavior() {
//        // Arrange
//        Location location = Location.C;
//
//        // Act
//        Site site = new TestSite(location);
//
//        // Assert
//        assertInstanceOf(TestSite.class, site, "The instance should be recognized as a subclass of Site");
//    }
//
//    // Subclass for Testing
//    static class TestSite extends Site {
//        public TestSite(Location location) {
//            super(location);
//        }
//    }
//}
