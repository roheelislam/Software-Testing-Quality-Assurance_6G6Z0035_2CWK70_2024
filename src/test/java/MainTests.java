import models.*;
import org.junit.jupiter.api.*;
import java.io.*;
import java.lang.reflect.Method;
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
    void testShowAbout() throws Exception {
        // Act
        invokePrivateMethod("showAbout");

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("This is a prototype of the Landfill Labs Worker Service."));
        assertTrue(output.contains("To run multiple scenarios, you should run the application multiple times."));
    }

    @Test
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
    void testInvalidLocationInput() throws Exception {
        // Arrange
        provideInput("X\nA\n");

        // Act
        Location location = (Location) invokePrivateMethod("collectLocation");

        // Assert
        assertEquals(Location.A, location, "Location should default to A after invalid input");
    }

    @Test
    void testInvalidGenerationInput() throws Exception {
        // Arrange
        provideInput("InvalidGen\nBeta\n");

        // Act
        String generation = (String) invokePrivateMethod("collectGeneration");

        // Assert
        assertEquals("Beta", generation, "Generation should default to Beta after invalid input");
    }

    @Test
    void testRunScenarioWithoutHistoricSite() {
        // Arrange
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(null, List.of());

        // Act & Assert
        Exception exception = assertThrows(NullPointerException.class, () ->
                invokePrivateMethod("runScenario", scenarioConfiguration)
        );
        assertNotNull(exception, "Scenario run should fail if no historic site is configured");
    }

    // Edge Case Test Cases

    @Test
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
    void testRunScenarioWithValidConfiguration() throws Exception {
        // Arrange
        Historic historic = new Historic(Location.A, 1000);
        Recycling recycling = new Alpha(Location.B, 5);
        ScenarioConfiguration scenarioConfiguration = new ScenarioConfiguration(historic, List.of(recycling));

        // Act
        invokePrivateMethod("runScenario", scenarioConfiguration);

        // Assert
        String output = outputStream.toString();
        assertTrue(output.contains("Scenario successfully completed"), "Scenario should complete successfully");
        assertTrue(output.contains("Time to fill recycling centre:"), "Output should include travel time");
        assertTrue(output.contains("Time to process the waste after delivery:"), "Output should include processing time");
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
