# Landfill Labs Project Repository

Welcome to the **Landfill Labs Project Repository**. This repository contains the implementation, testing, and continuous integration setup for the Landfill Labs system, a strategic planning tool designed to help councils manage and reallocate waste from historic landfill sites to recycling plants and other facilities.

## Repository Structure

The repository is organized as follows:

```
.github/workflows/
    tests.yml            # CI configuration file for automated testing (GitHub Actions)

src/
    main/java/           # Source code for the Landfill Labs system
    test/java/           # Test files for integration, and system tests
        models/          # Tests for individual models and components

pom.xml                 # Maven project configuration file
README.md               # Project documentation
TechMemo.md             # Technical memorandum for project implementation
```

### Key Components

#### Source Code (`src/main/java`)
The main Java source files for the Landfill Labs system, implementing functionality for:
- Scenario configuration
- Recycling center management
- Waste allocation and distribution logic
- Travel time calculation
- Optimal recycling center selection

#### Test Files (Located in `src/test/java` and `src/test/java/models`)
This folder contains a comprehensive suite of test cases, organized into:

- **Unit Tests:**
    - Validate the functionality of individual components (e.g. `AlphaTests.java`, `BetaTests.java`).
    - Cover positive, negative, edge cases, such as null inputs, boundary conditions, and invalid data.

- **Integration Tests:**
    - Ensure different components interact correctly (e.g. `HistoricandTransportIntegrationTests.java`, `RecyclingandTransportIntegrationTests.java`).
    - Simulate real-world workflows involving multiple modules/components.

- **System Tests:**
    - Test the entire system workflow (e.g. `EndToEndFlowIntegrationTest.java`).
    - Verify high-level functionality and expected outcomes.

- **Advanced Features in Testing:**
    - **Reflection:** Used to access and test private fields and methods (e.g. in `TransportTests.java`).
    - **Mock Classes:** Extended and mock subclasses are used to simulate behaviors of abstract and final classes (e.g. `MockTransport` in `TransportTests.java`, `MockHistoric` in `HistoricTests.java`).
    - **Parameterized Tests:** Applied for validating a range of input scenarios efficiently and parameters validation such as location, yearsActive, generation.
    - **Overrides and Extended Classes:** Methods like `getGeneration` and `getRates` are overridden to ensure controlled access and extended class behavior validation.
    - **System Performance Testing:** The `System.currentTimeMillis()` method measures system response times for efficiency evaluation.
    - **Capacity Validation:** The `isOverloaded` method checks if total waste exceeds a specified capacity, ensuring accurate modeling.
    - **Helper Methods:** Provide sample `Historic` and `RecyclingCenter` objects, validating scenarios like insufficient waste and other edge cases.
    - **Empty List Validation:** Tests system behavior when no viable options exist, ensuring robustness.
    - **List Operations:** Used to define multiple recycling centers and evaluate viability based on predefined rules, such as excluding Gamma centers when no metallic waste is present.
    - **Exception Handling:** Try-catch blocks are utilized to capture runtime errors, verify system resumption, and ensure robust testing.
    - **@BeforeEach and @AfterEach:** Redirect and restore system input/output streams for capturing console output during tests.
    - **Exception Validation:** Overridden methods simulate invalid recycling center generation and rates, validating exceptions like `ArithmeticException` using `assertThrows`.
    - **Utility Methods:** Calculate viable recycling centers, optimal center selection, travel, and processing durations.
    - **Waste Volume Management:** The `Math.ceil` method handles scenarios with multiple trips, ensuring correct calculations for waste exceeding truck capacity.

These tests ensure the correctness, robustness, and scalability of the system under various scenarios and conditions.

#### Continuous Integration (CI) Setup (`.github/workflows/tests.yml`)
This repository uses GitHub Actions for Continuous Integration. The `tests.yml` file is configured to:
- Provide immediate feedback on the status of tests, ensuring code quality and functionality.
- Automatically execute unit, integration, and system tests on each push and pull request.
- Ensure code quality and functionality are maintained.

### Highlights of the Test Suite

1. **Comprehensive Coverage:**
    - All core functionalities are tested, including waste split estimation, optimal recycling center selection, and transport logistics.
    - Includes tests for edge cases, such as negative inputs, zero waste scenarios, positive scenarios, error handling for core system functionalities and handling of large datasets.

2. **Advanced Testing Techniques:**
    - Reflection is used to test private fields and methods, ensuring full coverage of encapsulated logic.
    - Mock and extended subclasses simulate behavior for abstract and final classes, enabling thorough validation.
    - Tests for immutability and consistency across multiple calls (e.g., `AlphaTests.java`, `GammaTests.java`).

3. **CI Integration:**
    - Automated testing through GitHub Actions ensures seamless validation of code changes and development workflow.
    - Provides quick feedback on test results, reducing potential bugs and improving code reliability.

4. **Scalability and Maintainability:**
    - Parameterized tests allow for efficient testing of multiple scenarios.
    - Clear organization of tests into unit, integration, and system levels for ease of maintenance.

### How to Use This Repository

#### Prerequisites
- **Java Development Kit (JDK)** version 18 or above
- **Maven** for building and running tests
- Access to a **GitHub repository** for CI execution

#### Running Tests Locally
1. Clone this repository:
    ```bash
    git clone https://github.com/repo-url/landfill-labs.git
    ```
2. Navigate to the project directory:
    ```bash
    cd landfill-labs
    ```
3. Run tests using Maven:
    ```bash
    mvn test
    ```

#### Continuous Integration
- CI is automatically triggered on each push or pull request via the `tests.yml` workflow file.
- To view the status of tests, navigate to the **Actions** tab in the GitHub repository.

## Key Features of the Landfill Labs System
- **Scenario Configuration:** Configure landfill site attributes and recycling center properties.
- **Waste Allocation:** Calculate optimal distribution of waste to recycling centers.
- **Travel Time Calculation:** Estimate transport times based on locations.
- **System Modeling:** Analyze the impact of adding or modifying facilities.
- **Performance Validation:** Measure system response times and identify bottlenecks.
- **Robust Error Handling:** Capture and validate runtime exceptions for stability assurance.