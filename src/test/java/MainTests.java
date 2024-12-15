import models.Alpha;
import models.Historic;
import models.Location;
import models.Recycling;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class MainTests {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final InputStream originalSystemIn = System.in;
    private final PrintStream originalSystemOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalSystemOut);
        System.setIn(originalSystemIn);
    }

    // Positive Test Cases

    @Test
    @DisplayName("Display Main Menu Options")
    void testShowOptions() throws Exception {
        // Act
        invokePrivateMethod("showOptions");

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("1. Configure/Run Scenario."));
        assertTrue(output.contains("2. About."));
        assertTrue(output.contains("3. Exit."));
    }

    @Test
    @DisplayName("Handle Invalid Menu Option")
    void testShowOptionsAndInvalidChoice() {
        // Simulate invalid choice for the main menu
        String input = "4\n3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Main.main(null);

        // Verify the output
        assertTrue(outputStream.toString().contains("Invalid choice. Please try again."),
                "Output should indicate invalid choice.");
    }

    @Test
    @DisplayName("Display About Information")
    void testShowAbout() throws Exception {
        // Act
        invokePrivateMethod("showAbout");

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("This is a prototype of the Landfill Labs Worker Service."));
        assertTrue(output.contains("To run multiple scenarios, you should run the application multiple times."));
    }

    @Test
    @DisplayName("Create Historic Site with User Input")
    void testCreateHistoric() throws Exception {
        // Arrange
        provideInput("A\n1000\n");

        // Act
        Historic historic = (Historic) invokePrivateMethod("createHistoric");

        // Assert
        assertNotNull(historic);
        assertEquals(Location.A, historic.getLocation(), "Historic site should have location A");
        assertEquals(1000.0, historic.getRemainingWaste(), "Historic site should have 1000 cubic meters of waste");
    }

    @Test
    @DisplayName("Create Recycling Center with User Input")
    void testCreateRecyclingWithSingleCentre() throws Exception {
        // Arrange
        provideInput("A\n5\nAlpha\nn\n");

        // Act
        List<Recycling> recyclingCenters = (List<Recycling>) invokePrivateMethod("createRecycling");

        // Assert
        assertEquals(1, recyclingCenters.size(), "One recycling centre should be created");
        assertEquals("Alpha", recyclingCenters.get(0).getGeneration(), "Recycling centre generation should be Alpha");
    }

    @Test
    @DisplayName("Collect Location with Valid Input")
    void testCollectLocationWithValidInput() throws Exception {
        // Arrange
        provideInput("B\n");

        // Act
        Location location = (Location) invokePrivateMethod("collectLocation");

        // Assert
        assertEquals(Location.B, location, "Location should be set to B");
    }

    // Negative Test Cases

    @Test
    @DisplayName("Handle Invalid Location Input")
    void testInvalidLocationInput() throws Exception {
        // Arrange
        provideInput("X\nA\n");

        // Act
        Location location = (Location) invokePrivateMethod("collectLocation");

        // Assert
        assertEquals(Location.A, location, "Location should default to A after invalid input");
    }

    @Test
    @DisplayName("Handle Invalid Recycling Generation Input")
    void testInvalidGenerationInput() throws Exception {
        // Arrange
        provideInput("InvalidGen\nBeta\n");

        // Act
        String generation = (String) invokePrivateMethod("collectGeneration");

        // Assert
        assertEquals("Beta", generation, "Generation should default to Beta after invalid input");
    }

    @Test
    @DisplayName("Run Scenario Without Historic Site")
    void testRunScenarioWithoutHistoricSite() {
        // Arrange
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(null, List.of());

        // Act & Assert
        Exception exception = assertThrows(InvocationTargetException.class, () ->
                invokePrivateMethod("runScenario", scenarioConfiguration)
        );
        assertNotNull(exception.getCause(), "Exception cause should not be null");
        assertInstanceOf(NullPointerException.class, exception.getCause(), "Cause of exception should be NullPointerException");
        assertEquals("Cannot invoke \"models.Historic.getMetallic()\" because \"historic\" is null",
                exception.getCause().getMessage(),
                "Exception message should match the expected null pointer message");
    }

    // Edge Case Test Cases

    @Test
    @DisplayName("Exit Application with User Input")
    void testExitOption() {
        // Arrange
        provideInput("3\n");

        // Act
        Main.main(new String[]{});

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Exiting..."), "Application should exit cleanly when option 3 is selected");
    }

    @Test
    @DisplayName("Run Scenario with Valid Configuration")
    void testRunScenarioWithValidConfiguration() throws Exception {
        // Arrange
        Historic historic = new Historic(Location.A, 1000);
        Recycling recycling = new Alpha(Location.B, 5);
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, new ArrayList<>(List.of(recycling)));

        // Act
        invokePrivateMethod("runScenario", scenarioConfiguration);

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Scenario successfully completed"), "Scenario should complete successfully");
        assertTrue(output.contains("Time to fill recycling centre:"), "Output should include travel time");
        assertTrue(output.contains("Time to process the waste after delivery:"), "Output should include processing time");
    }

    @Test
    @DisplayName("Configure Scenario and Run with Valid Inputs")
    void testConfigureScenarioAddHistoricAndRun() throws Exception {
        // Simulate user input for creating a historic site and running a scenario
        String input = "1\nA\n1000\n3\n"; // Add Historic, Location A, Waste 1000, Exit
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // Capture the console output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Use reflection to access the private configureScenario method
        Method method = Main.class.getDeclaredMethod("configureScenario");
        method.setAccessible(true); // Make the private method accessible

        try {
            // Invoke the private method and get the ScenarioConfiguration object
            ScenarioConfiguration scenario = (ScenarioConfiguration) method.invoke(null);

            // Assertions for Historic creation
            assertNotNull(scenario.getHistoric(), "Historic should not be null.");
            assertEquals(Location.A, scenario.getHistoric().getLocation(), "Location should be A.");
            assertEquals(1000.0, scenario.getHistoric().getRemainingWaste(), "Remaining waste should be 1000.");

            // Assertions for recycling centers
            assertNull(scenario.getRecycling(), "Recycling list should be null when not created.");
        } catch (Exception e) {
            fail("Exception occurred during test execution: " + e.getMessage());
        }
    }

    // Utility Methods

    private void provideInput(String data) {
        InputStream inputStream = new ByteArrayInputStream(data.getBytes());
        System.setIn(inputStream);
    }

    private Object invokePrivateMethod(String methodName, Object... args) throws Exception {
        Method method = findMethod(Main.class, methodName, args);
        method.setAccessible(true);
        return method.invoke(null, args);
    }

    private Method findMethod(Class<?> clazz, String methodName, Object... args) throws NoSuchMethodException {
        Class<?>[] parameterTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        return clazz.getDeclaredMethod(methodName, parameterTypes);
    }
}
