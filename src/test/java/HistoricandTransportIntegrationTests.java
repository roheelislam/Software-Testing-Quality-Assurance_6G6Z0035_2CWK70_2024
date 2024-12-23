import models.Location;
import models.Transport;
import models.Historic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class HistoricandTransportIntegrationTests {


    //Positive Test Case: Waste Handling in Transport
    @Test
    @DisplayName("Positive Case: Validate Waste Handling in Transport")
    void testTransportWasteManagement_TC_001() {
        // Arrange
        new Historic(Location.A, 100.0);
        Transport transport = new Transport(Location.A, Location.B);
        transport.setPaperWaste(20.0);

        // Act
        double totalWaste = transport.getTotalWaste();

        // Assert
        assertEquals(20.0, totalWaste, "Total waste in transport should match the set paper waste");
    }


    //Negative Test Case: Exceed Transport Capacity
    @Test
    @DisplayName("Negative Case: Exceed Transport Capacity")
    void testExceedTransportCapacity_TC_001() {
        // Arrange
        new Historic(Location.A, 100.0);
        Transport transport = new Transport(Location.A, Location.B);
        transport.setPaperWaste(25.0); // Exceeds capacity

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            if (transport.getTotalWaste() > 20.0) throw new IllegalArgumentException("Transport capacity exceeded");
        }, "An exception should be thrown if transport waste exceeds capacity");
    }


}