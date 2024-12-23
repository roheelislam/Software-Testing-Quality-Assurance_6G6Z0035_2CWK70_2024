import models.Historic;
import models.Location;
import models.Recycling;
import models.Beta;
import models.Alpha;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScenarioConfigurationTests {

    // Positive Test Cases

    @Test
    @DisplayName("Default Constructor: Validate Initialization")
    void testDefaultConstructor_TC_001() {
        // Arrange & Act
        ScenarioConfiguration config = new ScenarioConfiguration();

        // Assert
        assertNotNull(config.getRecycling(), "Recycling list should be initialized as empty");
        assertEquals(0, config.getRecycling().size(), "Recycling list should be empty by default");
        assertNull(config.getHistoric(), "Historic site should be null by default");
    }

    @Test
    @DisplayName("Parameterized Constructor: Validate Initialization with Inputs")
    void testParameterizedConstructor_TC_001() {
        // Arrange
        Historic historic = new Historic(Location.A, 1500);
        Recycling recycling = new Beta(Location.B, 10);
        List<Recycling> recyclingList = new ArrayList<>();
        recyclingList.add(recycling);

        // Act
        ScenarioConfiguration config = new ScenarioConfiguration(historic, recyclingList);

        // Assert
        assertEquals(historic, config.getHistoric(), "Historic site should match the provided historic object");
        assertEquals(1, config.getRecycling().size(), "Recycling list should contain one item");
        assertEquals(recycling, config.getRecycling().get(0), "Recycling item should match the provided object");
    }

    @Test
    @DisplayName("Set Historic Site: Validate Assignment")
    void testSetHistoric_TC_001() {
        // Arrange
        ScenarioConfiguration config = new ScenarioConfiguration();
        Historic historic = new Historic(Location.C, 2000);

        // Act
        config.setHistoric(historic);

        // Assert
        assertEquals(historic, config.getHistoric(), "Historic site should be set correctly");
    }

    @Test
    @DisplayName("Add Recycling Center: Single Item")
    void testAddRecycling_TC_001() {
        // Arrange
        ScenarioConfiguration config = new ScenarioConfiguration();
        Recycling recycling = new Alpha(Location.A, 5);

        // Act
        config.addRecycling(recycling);

        // Assert
        assertEquals(1, config.getRecycling().size(), "Recycling list should contain one item after adding");
        assertEquals(recycling, config.getRecycling().get(0), "Recycling item should match the added object");
    }

    @Test
    @DisplayName("Add Multiple Recycling Centers: Validate List Updates")
    void testAddMultipleRecyclingItems_TC_001() {
        // Arrange
        ScenarioConfiguration config = new ScenarioConfiguration();
        Recycling recycling1 = new Beta(Location.B, 7);
        Recycling recycling2 = new Alpha(Location.A, 5);

        // Act
        config.addRecycling(recycling1);
        config.addRecycling(recycling2);

        // Assert
        assertEquals(2, config.getRecycling().size(), "Recycling list should contain two items after adding");
        assertEquals(recycling1, config.getRecycling().get(0), "First recycling item should match the first added object");
        assertEquals(recycling2, config.getRecycling().get(1), "Second recycling item should match the second added object");
    }

    // Negative Test Cases

    @Test
    @DisplayName("Add Null Recycling Center: Validate Behavior")
    void testAddNullRecycling_TC_001() {
        // Arrange
        ScenarioConfiguration config = new ScenarioConfiguration();

        // Act
        config.addRecycling(null);

        // Assert
        assertEquals(1, config.getRecycling().size(), "Recycling list should contain one item (null)");
        assertNull(config.getRecycling().get(0), "Recycling item should be null");
    }

    @Test
    @DisplayName("Set Null Historic Site: Validate Behavior")
    void testSetNullHistoric_TC_001() {
        // Arrange
        ScenarioConfiguration config = new ScenarioConfiguration();

        // Act
        config.setHistoric(null);

        // Assert
        assertNull(config.getHistoric(), "Historic site should remain null after setting null");
    }

    @Test
    @DisplayName("Parameterized Constructor with Null Recycling List")
    void testNullRecyclingListInParameterizedConstructor_TC_001() {
        // Arrange
        Historic historic = new Historic(Location.A, 2000);

        // Act
        ScenarioConfiguration config = new ScenarioConfiguration(historic, null);

        // Assert
        assertEquals(historic, config.getHistoric(), "Historic site should be set correctly");
        assertNull(config.getRecycling(), "Recycling list should remain null if provided as null");
    }

    @Test
    @DisplayName("Modify Recycling List After Initialization")
    void testRecyclingModificationAfterInitialization_TC_001() {
        // Arrange
        Historic historic = new Historic(Location.B, 1000);
        List<Recycling> recyclingList = new ArrayList<>();
        recyclingList.add(new Alpha(Location.A, 5));

        ScenarioConfiguration config = new ScenarioConfiguration(historic, recyclingList);

        // Act
        recyclingList.add(new Beta(Location.B, 10));

        // Assert
        assertEquals(2, config.getRecycling().size(), "Recycling list in configuration should reflect external modifications");
    }

    // Edge Case Test Cases

    @Test
    @DisplayName("Recycling List with Empty Initialization")
    void testRecyclingWithEmptyList_TC_001() {
        // Arrange
        Historic historic = new Historic(Location.A, 500);
        ScenarioConfiguration config = new ScenarioConfiguration(historic, new ArrayList<>());

        // Assert
        assertEquals(0, config.getRecycling().size(), "Recycling list should be empty when initialized with an empty list");
    }

    @Test
    @DisplayName("Add Recycling to Null Recycling List")
    void testAddRecyclingToNullRecyclingList_TC_001() {
        // Arrange
        Historic historic = new Historic(Location.A, 1500);
        ScenarioConfiguration config = new ScenarioConfiguration(historic, null);
        Recycling recycling = new Beta(Location.C, 8);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> config.addRecycling(recycling),
                "Adding to a null recycling list should throw a NullPointerException");
    }

    @Test
    @DisplayName("Empty Constructor with Null Additions")
    void testEmptyConstructorWithNullAdditions_TC_001() {
        // Arrange
        ScenarioConfiguration config = new ScenarioConfiguration();

        // Act
        config.setHistoric(null);
        config.addRecycling(null);

        // Assert
        assertNull(config.getHistoric(), "Historic site should remain null after setting null");
        assertEquals(1, config.getRecycling().size(), "Recycling list should contain one item after adding null");
        assertNull(config.getRecycling().get(0), "Recycling item should be null");
    }
}
