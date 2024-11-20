//package models;
//
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//public class SiteSpec {
//    @Test
//    public void testValidLocationInitialization() {
//        // Arrange
//        Location expectedLocation = Location.A;
//        Site testSite = new TestSite(expectedLocation);
//
//        // Act
//        Location actualLocation = testSite.getLocation();
//
//        // Assert
//        assertEquals("The location should be correctly initialized to Location.A.", expectedLocation, actualLocation);
//    }
//
//    @Test
//    public void testNullLocationInitialization() {
//        // Arrange
//        Location expectedLocation = null;
//        Site testSite = new TestSite(expectedLocation);
//
//        // Act
//        Location actualLocation = testSite.getLocation();
//
//        // Assert
//        assertEquals("The location should be correctly initialized to null.", expectedLocation, actualLocation);
//    }
//
//    @Test
//    public void testEqualityOfSitesWithSameLocation() {
//        // Arrange
//        Location location = Location.B;
//        Site site1 = new TestSite(location);
//        Site site2 = new TestSite(location);
//
//        // Act
//        boolean areEqual = site1.getLocation().equals(site2.getLocation());
//
//        // Assert
//        assertTrue("Two sites with the same location should be considered equivalent.", areEqual);
//    }
//
//    @Test
//    public void testInequalityOfSitesWithDifferentLocations() {
//        // Arrange
//        Site site1 = new TestSite(Location.A);
//        Site site2 = new TestSite(Location.C);
//
//        // Act
//        boolean areNotEqual = !site1.getLocation().equals(site2.getLocation());
//
//        // Assert
//        assertTrue("Two sites with different locations should not be considered equivalent.", areNotEqual);
//    }
//
//    @Test
//    public void testLocationConsistency() {
//        // Arrange
//        Location expectedLocation = Location.B;
//        Site testSite = new TestSite(expectedLocation);
//
//        // Act
//        Location locationCall1 = testSite.getLocation();
//        Location locationCall2 = testSite.getLocation();
//
//        // Assert
//        assertEquals("The getLocation method should return the same value across " +
//                     "multiple calls.", locationCall1, locationCall2);
//        assertEquals("The returned location should match the expected location.", expectedLocation, locationCall1);
//    }
//
//    @Test
//    public void testLocationImmutability() {
//        // Arrange
//        Location initialLocation = Location.A;
//        Site testSite = new TestSite(initialLocation);
//
//        // Act
//        Location locationBefore = testSite.getLocation();
//        Location locationAfter = testSite.getLocation(); // No setter, so location remains unchanged
//
//        // Assert
//        assertEquals("The location property should be immutable and remain unchanged.", locationBefore, locationAfter);
//    }
//
//    @Test
//    public void testNullSafetyInGetLocation() {
//        // Arrange
//        Site testSite = new TestSite(null);
//
//        // Act
//        Location actualLocation = testSite.getLocation();
//
//        // Assert
//        assertNull("The getLocation method should safely return null if the location was " +
//                   "initialized as null.", actualLocation);
//    }
//
//    static class TestSite extends Site {
//        TestSite(Location location) {
//            super(location);
//        }
//    }
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
