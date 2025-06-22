# PatientSearchService Edge Case Handling Report

## 1. Service Description

`PatientSearchService` provides search functionality for patients by:
- Birth date (as string, parsed to LocalDate)
- First name (case-insensitive, partial match)
- Last name (case-insensitive, partial match)
- Medical record number (exact match)

All methods now robustly handle a wide range of edge cases and provide clear error handling and documentation.

---

## 2. Edge Cases Handled (per method)

### `searchByBirthDate(String dobString)`
- Null input: throws `IllegalArgumentException`
- Empty or whitespace input: throws `IllegalArgumentException`
- Input too long: throws `IllegalArgumentException`
- Invalid date format: throws `IllegalArgumentException`
- Non-existent date (e.g., 2023-02-30): throws `IllegalArgumentException`
- Future date: logs a warning, still queries
- Unusually old date (before 1900-01-01): logs a warning, still queries
- Repository/database error: throws `PatientSearchException`
- No results found: returns empty list

### `findByFirstNameContainingIgnoreCase(String name)`
- Null input: throws `IllegalArgumentException`
- Empty or whitespace input: throws `IllegalArgumentException`
- Input too long: throws `IllegalArgumentException`
- Special characters: queries as-is
- Repository/database error: throws `PatientSearchException`
- No results found: returns empty list

### `findByLastNameContainingIgnoreCase(String name)`
- Null input: throws `IllegalArgumentException`
- Empty or whitespace input: throws `IllegalArgumentException`
- Input too long: throws `IllegalArgumentException`
- Special characters: queries as-is
- Repository/database error: throws `PatientSearchException`
- No results found: returns empty list

### `findByMedicalRecordNumber(String mrn)`
- Null input: throws `IllegalArgumentException`
- Empty or whitespace input: throws `IllegalArgumentException`
- Input too long: throws `IllegalArgumentException`
- Special characters: queries as-is
- Repository/database error: throws `PatientSearchException`
- No results found: returns empty list

---

## 3. Example Test Cases

```java
// searchByBirthDate
assertThrows(IllegalArgumentException.class, () -> service.searchByBirthDate(null));
assertThrows(IllegalArgumentException.class, () -> service.searchByBirthDate("   "));
assertThrows(IllegalArgumentException.class, () -> service.searchByBirthDate("not-a-date"));
assertThrows(IllegalArgumentException.class, () -> service.searchByBirthDate("2023-02-30"));
assertThrows(PatientSearchException.class, () -> service.searchByBirthDate("2000-01-01")); // if DB error

// findByFirstNameContainingIgnoreCase
assertThrows(IllegalArgumentException.class, () -> service.findByFirstNameContainingIgnoreCase(null));
assertThrows(IllegalArgumentException.class, () -> service.findByFirstNameContainingIgnoreCase("   "));
assertThrows(IllegalArgumentException.class, () -> service.findByFirstNameContainingIgnoreCase("a".repeat(101)));
assertThrows(PatientSearchException.class, () -> service.findByFirstNameContainingIgnoreCase("John")); // if DB error

// findByMedicalRecordNumber
assertThrows(IllegalArgumentException.class, () -> service.findByMedicalRecordNumber(null));
assertThrows(IllegalArgumentException.class, () -> service.findByMedicalRecordNumber("   "));
assertThrows(IllegalArgumentException.class, () -> service.findByMedicalRecordNumber("c".repeat(101)));
assertThrows(PatientSearchException.class, () -> service.findByMedicalRecordNumber("MRN-1")); // if DB error
```

---

## 4. Error Handling and Robustness Improvements

- All input is validated for null, empty, whitespace, and excessive length.
- Date parsing is robust and provides clear error messages.
- Repository/database errors are caught and wrapped in a custom `PatientSearchException`.
- All methods return empty lists if no results are found, never null.
- Special and unusual input is handled gracefully.
- Extensive unit tests cover all major edge cases.

---

**This ensures the PatientSearchService is robust, secure, and maintainable, with clear documentation and test coverage for all critical edge cases.** 