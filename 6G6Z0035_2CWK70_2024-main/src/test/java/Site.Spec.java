import models.Location;
import models.Site;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Concrete subclass of Site for testing

@Test
public void testValidLocationInitialization() {
    // Arrange
    Location expectedLocation = Location.A;
    Site testSite = new TestSite(expectedLocation);

    // Act
    Location actualLocation = testSite.getLocation();

    // Assert
    assertEquals(expectedLocation, actualLocation, "The location should be correctly initialized to Location.A.");
}

@Test
public void testNullLocationInitialization() {
    // Arrange
    Location expectedLocation = null;
    Site testSite = new TestSite(expectedLocation);

    // Act
    Location actualLocation = testSite.getLocation();

    // Assert
    assertEquals(expectedLocation, actualLocation, "The location should be correctly initialized to null.");
}

@Test
public void testEqualityOfSitesWithSameLocation() {
    // Arrange
    Location location = Location.B;
    Site site1 = new TestSite(location);
    Site site2 = new TestSite(location);

    // Act
    boolean areEqual = site1.getLocation().equals(site2.getLocation());

    // Assert
    assertTrue(areEqual, "Two sites with the same location should be considered equivalent.");
}

@Test
public void testInequalityOfSitesWithDifferentLocations() {
    // Arrange
    Site site1 = new TestSite(Location.A);
    Site site2 = new TestSite(Location.C);

    // Act
    boolean areNotEqual = !site1.getLocation().equals(site2.getLocation());

    // Assert
    assertTrue(areNotEqual, "Two sites with different locations should not be considered equivalent.");
}

@Test
public void testLocationConsistency() {
    // Arrange
    Location expectedLocation = Location.B;
    Site testSite = new TestSite(expectedLocation);

    // Act
    Location locationCall1 = testSite.getLocation();
    Location locationCall2 = testSite.getLocation();

    // Assert
    assertEquals(locationCall1, locationCall2, "The getLocation method should return the same value across multiple calls.");
    assertEquals(expectedLocation, locationCall1, "The returned location should match the expected location.");
}

@Test
public void testLocationImmutability() {
    // Arrange
    Location initialLocation = Location.A;
    Site testSite = new TestSite(initialLocation);

    // Act
    Location locationBefore = testSite.getLocation();
    Location locationAfter = testSite.getLocation(); // No setter, so location remains unchanged

    // Assert
    assertEquals(locationBefore, locationAfter, "The location property should be immutable and remain unchanged.");
}

@Test
public void testNullSafetyInGetLocation() {
    // Arrange
    Site testSite = new TestSite(null);

    // Act
    Location actualLocation = testSite.getLocation();

    // Assert
    assertNull(actualLocation, "The getLocation method should safely return null if the location was initialized as null.");
}

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

























