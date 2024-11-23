import models.Location;
import models.Recycling;
import models.Alpha;
import models.Gamma;
import models.Beta;
import models.Historic;
import models.Transport;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ModelsAdditionalIntegrationTests {


    //Positive Test Cases

    //Calculate Total Waste in Transport
    @Test
    void testTotalWasteInTransport() {
        //Arrange: Create a Transport with paperWaste=10, plasticGlassWaste=5, and metallicWaste=5.
        Transport transport = new Transport(Location.A, Location.B);
        transport.setPaperWaste(10);
        transport.setPlasticGlassWaste(5);
        transport.setMetallicWaste(5);

        //Act: Retrieve the total waste.
        double totalWaste = transport.getTotalWaste();

        //Assert: Validate the total.
        assertEquals(20.0, totalWaste, "Total waste should equal the sum of all waste types");
    }


    //Verify Interaction Between Historic and Transport
    @Test
    void testHistoricWasteWithTransport() {
        //Arrange: Create a Historic site with 1500 cubic meters of waste and a Transport object.
        Historic historic = new Historic(Location.A, 1500.0);
        Transport transport = new Transport(Location.A, Location.B);

        transport.setPaperWaste(historic.getPaper());
        transport.setPlasticGlassWaste(historic.getPlasticGlass());
        transport.setMetallicWaste(historic.getMetallic());

        //Act: Set Transport waste values and validate the total waste.
        double totalWaste = transport.getTotalWaste();

        //Assert: Total waste equals the sum of all types.
        assertEquals(1500.0, totalWaste, "Total waste should match the initial waste of the historic site");
    }


    //Verify Recycling Generation and Rates
    @Test
    void testRecyclingGenerationAndRates() {
        //Arrange: Create instances of Alpha, Beta, and Gamma recycling centers.
        Recycling alpha = new Alpha(Location.A, 10);
        Recycling beta = new Beta(Location.B, 5);
        Recycling gamma = new Gamma(Location.C, 15);

        //Act: Retrieve their generation and rates.
        //Assert: Validate the generation and processing rates.
        assertEquals("Alpha", alpha.getGeneration(), "Alpha generation is incorrect");
        assertEquals(List.of(1.0, 1.0, 1.0), alpha.getRates(), "Alpha rates are incorrect");

        assertEquals("Beta", beta.getGeneration(), "Beta generation is incorrect");
        assertEquals(List.of(1.5, 1.5, 1.5), beta.getRates(), "Beta rates are incorrect");

        assertEquals("Gamma", gamma.getGeneration(), "Gamma generation is incorrect");
        assertEquals(List.of(1.5, 2.0, 3.0), gamma.getRates(), "Gamma rates are incorrect");
    }


    //Validate Location Travel Times
    @Test
    void testLocationTravelTimes() {
        //Arrange: Create Transport objects between various Location pairs.
        //Act: Retrieve travel times.
        //Assert: Validate the correct travel times.
        assertEquals(2.0, new Transport(Location.A, Location.B).getTravelTime(), "Travel time A to B is incorrect");
        assertEquals(4.0, new Transport(Location.A, Location.C).getTravelTime(), "Travel time A to C is incorrect");
        assertEquals(3.0, new Transport(Location.B, Location.C).getTravelTime(), "Travel time B to C is incorrect");
        assertEquals(1.0, new Transport(Location.A, Location.A).getTravelTime(), "Travel time A to A is incorrect");
    }


    //Validate Waste Processing Duration
    @Test
    void testWasteProcessingDuration() {
        //Arrange: Create a Historic site and a Gamma recycling center.
        Historic historic = new Historic(Location.A, 1500.0);
        Recycling gamma = new Gamma(Location.B, 10);

        //Act: Calculate the processing duration using Utils.calculateProcessDuration.
        double processDuration = Utils.calculateProcessDuration(historic, gamma);

        //Assert: Validate the duration is calculated correctly.
        double expected = (historic.getPlasticGlass() / 1.5) + (historic.getMetallic() / 2.0) + (historic.getPaper() / 3.0);
        assertEquals(expected, processDuration, 0.01, "Process duration calculation is incorrect");
    }


    //Negative Test Cases

    //Zero Waste Transport
    @Test
    void testZeroWasteTransport() {
        //Arrange: Create a Transport with no waste.
        Transport transport = new Transport(Location.A, Location.B);

        //Act: Retrieve the total waste.
        double totalWaste = transport.getTotalWaste();

        //Assert: Total waste should be 0.0.
        assertEquals(0.0, totalWaste, "Total waste should be 0.0 when no waste is set");
    }


    //Waste Distribution Below Threshold
    @Test
    void testHistoricWasteSplitBelowThreshold() {
        //Arrange: Create Historic with initial waste of 1000 cubic meters.
        Historic historic = new Historic(Location.A, 1000.0);

        //Act: Retrieve the waste split.
        double paper = historic.getPaper();
        double plasticGlass = historic.getPlasticGlass();
        double metallic = historic.getMetallic();

        //Assert: Metallic waste should be 0.0.
        assertEquals(500.0, paper, "Paper waste should be 50% of total waste");
        assertEquals(500.0, plasticGlass, "Plastic/Glass waste should be 50% of total waste");
        assertEquals(0.0, metallic, "Metallic waste should be 0.0 below threshold");
    }


    //Invalid Waste Distribution for Historic
    @Test
    void testHistoricZeroWaste() {
        //Arrange: Create a Historic site with zero waste.
        Historic historic = new Historic(Location.A, 0.0);

        //Act: Retrieve waste distribution.
        double paper = historic.getPaper();
        double plasticGlass = historic.getPlasticGlass();
        double metallic = historic.getMetallic();

        //Assert: All waste values should be zero.
        assertEquals(0.0, paper, "Paper waste should be zero");
        assertEquals(0.0, plasticGlass, "Plastic/Glass waste should be zero");
        assertEquals(0.0, metallic, "Metallic waste should be zero");
    }


    //Exceeding Waste Capacity in Transport
    @Test
    void testTransportExceedingCapacity() {
        //Arrange: Create a Transport with waste exceeding the transport capacity.
        Transport transport = new Transport(Location.A, Location.B);
        transport.setPaperWaste(15.0);
        transport.setPlasticGlassWaste(10.0);
        transport.setMetallicWaste(5.0);

        //Act: Retrieve the total waste.
        double totalWaste = transport.getTotalWaste();

        //Assert: Validate that the total waste does not exceed capacity.
        assertTrue(totalWaste > 20.0, "Total waste should exceed transport capacity of 20 cubic meters");
    }


    //Edge Case Tests

    //Maximum Waste Capacity in Transport Location A & B
    @Test
    void testTransportMaxCapacity() {
        //Arrange: Create a Transport with waste set to capacity (20 cubic meters).
        Transport transport = new Transport(Location.A, Location.B);
        transport.setPaperWaste(10);
        transport.setPlasticGlassWaste(5);
        transport.setMetallicWaste(5);

        //Act: Retrieve the total waste.
        double totalWaste = transport.getTotalWaste();

        //Assert: Validate waste does not exceed capacity.
        assertTrue(totalWaste <= 20.0, "Total waste should not exceed transport capacity of 20 cubic meters");
    }


    //Negative Waste in Transport
    @Test
    void testNegativeWasteInTransport() {
        //Arrange: Create a Transport with negative waste values.
        Transport transport = new Transport(Location.A, Location.B);
        transport.setPaperWaste(-10);
        transport.setPlasticGlassWaste(-5);
        transport.setMetallicWaste(-5);

        //Act: Retrieve the total waste.
        double totalWaste = transport.getTotalWaste();

        //Assert: Total waste should not be negative.
        assertTrue(totalWaste >= 0.0, "Total waste should not be negative");
    }


}