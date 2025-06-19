package csv;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.*;

public class CsvService {
    private static final Logger logger = Logger.getLogger(CsvService.class.getName());

    // Custom exception classes
    public static class CSVFileNotFoundException extends FileNotFoundException {
        public CSVFileNotFoundException(String message) { super(message); }
    }
    public static class CSVValidationException extends Exception {
        public CSVValidationException(String message) { super(message); }
    }
    public static class CSVError extends Exception {
        public CSVError(String message) { super(message); }
    }

    // Product data class
    public static class Product {
        public final String name;
        public final String description;
        public final double price;
        public final int quantity;

        public Product(String name, String description, double price, int quantity) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.quantity = quantity;
        }
    }

    /**
     * Reads and validates data from a CSV file.
     *
     * @param filePath Path to the CSV file
     * @return List of Product objects
     * @throws CSVFileNotFoundException If file doesn't exist
     * @throws CSVValidationException If data validation fails
     * @throws CSVError For other CSV errors
     */
    public static List<Product> readCsv(String filePath)
        throws CSVFileNotFoundException, CSVValidationException, CSVError {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            logger.severe("CSV file not found: " + filePath);
            throw new CSVFileNotFoundException("File " + filePath + " does not exist");
        }

        List<Product> products = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                logger.warning("CSV file is empty");
                throw new CSVValidationException("CSV file is empty");
            }
            String[] headers = headerLine.split(",");
            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                headerMap.put(headers[i].trim().toLowerCase(), i);
            }
            if (!headerMap.containsKey("name") || !headerMap.containsKey("price") || !headerMap.containsKey("quantity")) {
                throw new CSVValidationException("Missing required fields in CSV");
            }

            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",", -1);
                try {
                    String name = fields[headerMap.get("name")].trim();
                    String description = headerMap.containsKey("description")
                        ? fields[headerMap.get("description")].trim() : "";
                    double price;
                    int quantity;
                    try {
                        price = Double.parseDouble(fields[headerMap.get("price")].trim());
                        quantity = Integer.parseInt(fields[headerMap.get("quantity")].trim());
                        if (price < 0 || quantity < 0) {
                            throw new CSVValidationException("Price and quantity must be non-negative");
                        }
                    } catch (NumberFormatException e) {
                        throw new CSVValidationException("Invalid numeric values in CSV");
                    }
                    products.add(new Product(name, description, price, quantity));
                } catch (CSVValidationException e) {
                    logger.severe("Validation error in row: " + e.getMessage());
                    throw e;
                } catch (Exception e) {
                    logger.severe("Unexpected error in row: " + e.getMessage());
                    throw new CSVError("Failed to parse row: " + e.getMessage());
                }
            }
            if (products.isEmpty()) {
                logger.warning("No valid products found in CSV file");
                throw new CSVValidationException("No valid products found in CSV file");
            }
            logger.info("Successfully read " + products.size() + " products from CSV");
            return products;
        } catch (IOException e) {
            logger.severe("CSV parsing error: " + e.getMessage());
            throw new CSVError("Failed to parse CSV file: " + e.getMessage());
        }
    }
}
