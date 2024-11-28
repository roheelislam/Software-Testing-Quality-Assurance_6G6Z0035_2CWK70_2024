import models.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationUnitTests {
    @Test
    public void API_ScenarioCreate_TC_001() {
        // No API TO SEND POST
    }

    @Test
    public void API_ScenarioDelete_TC_001() {
        // Send a DELETE request to remove the scenario
    }

    @Test
    public void API_ScenarioCreate_TC_003() {
        //Send POST request to /Scenario and capture response
    }

    @Test
    public void API_ScenarioDelete_TC_002() {
        //Send DELETE request to delete the scenario

    }

    @Test
    public void API_ResponseFormat_TC_001() {
        //No API IMPLEMENTATION FOUND

    }

    @Test
    public void Auth_API_Access_TC_001() {
        //No API IMPLEMENTATION FOUND FOR AUTHENTICATION
    }

    @Test
    public void ErrorHandling_TC_001() {
        double initialWaste = 10;
        Historic historic = new Historic(Location.A, 100);
//        historic.setWasteType("Hazardous"); //can't set , Move to defect
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
        });
        assertTrue(exception.getMessage().contains("unsupported waste type"), "Error message should indicate unsupported type.");
    }

    @Test
    public void Performance_TC_001() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            Historic landfill = new Historic(Location.A, 2000.0);
            Recycling center = new Beta(Location.B, 5);
            Utils.calculateTravelDuration(landfill, center);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        assertTrue(duration < 5000, "System response time should be within acceptable limits.");
    }

    @Test
    public void Persistence_TC_001() {
        Historic landfill = new Historic(Location.A, 2000.0);
        Recycling center = new Alpha(Location.B, 5);
        ScenarioConfiguration scenario = new ScenarioConfiguration(landfill, List.of(center));
//        Utils.saveScenario(scenario, "testScenario.dat");

//        ScenarioConfiguration loadedScenario = Utils.loadScenario("testScenario.dat"); //supposing loadScenario
//        will load by name
//
//        assertEquals(scenario.getHistoric().getRemainingWaste(), loadedScenario.getHistoric().getRemainingWaste(),
//                "Remaining waste should match after reload.");
//        assertEquals(scenario.getRecycling().size(), loadedScenario.getRecycling().size(),
//                "Recycling centers count should match after reload.");
    }

    @Test
    public void Validation_TC_004() {
        Historic historic = new Historic(Location.A, 2000);
//        historic.setWasteType("Hazardous");  // Unsupported type

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        });

        assertEquals("Unsupported waste type: Hazardous", exception.getMessage());
    }

    @Test
    public void Validation_TC_005() {
        ScenarioConfiguration scenarioConfig = new ScenarioConfiguration();
        Historic historic = new Historic(Location.A, 1500);
        scenarioConfig.setHistoric(historic);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
//            scenarioConfig.finalizeScenario();
            //Implementaion not found for the finalize .
        });
        assertEquals("Scenario cannot be finalized without complete data.", exception.getMessage());
    }
}

