package csv;

import org.junit.jupiter.api.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class CsvServiceTest {

    private static final String TEST_CSV = "test_products.csv";

    @BeforeEach
    public void setup() throws Exception {
        List<String> lines = Arrays.asList(
            "name,description,price,quantity",
            "Apple,Fruit,1.5,10",
            "Banana,Fruit,0.5,20"
        );
        Files.write(Paths.get(TEST_CSV), lines);
    }

    @AfterEach
    public void cleanup() throws Exception {
        Files.deleteIfExists(Paths.get(TEST_CSV));
    }

    @Test
    public void testReadCsvSuccess() throws Exception {
        List<CsvService.Product> products = CsvService.readCsv(TEST_CSV);
        assertEquals(2, products.size());
        assertEquals("Apple", products.get(0).name);
        assertEquals(1.5, products.get(0).price, 0.001);
        assertEquals(10, products.get(0).quantity);
    }

    @Test
    public void testFileNotFound() {
        Exception exception = assertThrows(CsvService.CSVFileNotFoundException.class, () -> {
            CsvService.readCsv("nonexistent.csv");
        });
        assertTrue(exception.getMessage().contains("does not exist"));
    }

    @Test
    public void testValidationError() throws Exception {
        String badCsv = "bad.csv";
        List<String> lines = Arrays.asList(
            "name,price,quantity",
            "Orange,notanumber,5"
        );
        Files.write(Paths.get(badCsv), lines);
        Exception exception = assertThrows(CsvService.CSVValidationException.class, () -> {
            CsvService.readCsv(badCsv);
        });
        assertTrue(exception.getMessage().contains("Invalid numeric values"));
        Files.deleteIfExists(Paths.get(badCsv));
    }
}
