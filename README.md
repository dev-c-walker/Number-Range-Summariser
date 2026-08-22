# Number Range Summarizer

A Java 8 implementation of the `NumberRangeSummarizer` interface that parses comma-delimited numeric strings and summarizes sequential numbers into compact hyphenated range representations.

---

## Example

* **Input:** `"1, 3, 6, 7, 8, 12, 13, 14, 15, 21, 22, 23, 24, 31"`
* **Output:** `"1, 3, 6-8, 12-15, 21-24, 31"`

---

## Architectural & Design Decisions

* **Java 8 Idiomatic Features:** Leveraged Java 8 Streams (`filter`, `map`, `distinct`, `sorted`, `collect`), method references (`String::trim`, `this::parseToken`), and the Collections API for declarative and readable data transformations.
* **Encapsulation & Named Constants:** Delimiters (`","`, `", "`, `"-"`) are defined as `private static final` constants at the class level for immutability, thread safety, and maintainability.
* **Separation of Concerns:** Token parsing, error handling, and individual range formatting are encapsulated into private helper methods (`parseToken`, `formatRange`).
* **Algorithmic Complexity:**
  * **Parsing (`collect`):** $O(N)$ linear time where $N$ is the number of tokens.
  * **Summarization (`summarizeCollection`):** $O(N \log N)$ to sort the sanitized collection, followed by a single-pass $O(N)$ sliding window iteration to build ranges using `StringBuilder` via `String.join`. Overall Space Complexity is $O(N)$.

---

## Assumptions & Edge Cases Handled

The following assumptions were made upfront and are verified in the unit test suite:

1. **Null & Blank Safety:** Passing `null`, an empty string `""`, or whitespace `"   "` to `collect()` returns `Collections.emptyList()`. Passing `null` or an empty collection to `summarizeCollection()` returns an empty string `""`.
2. **Whitespace & Consecutive Delimiters:** Arbitrary whitespace around numbers (`" 1 ,  3 , 6 "`) and empty tokens from consecutive commas (`"1,,,2,3"`) are sanitised automatically.
3. **Validation & Errors:** Any non-numeric or decimal token (e.g., `"abc"`, `"3.14"`) throws a descriptive `IllegalArgumentException` explaining the invalid value.
4. **Ordering:** Input collections do not need to be pre-sorted. The summarizer sorts elements in ascending order prior to grouping.
5. **Duplicates:** Duplicate numbers (e.g., `[1, 1, 2, 2, 3]`) are deduplicated so boundary pointers remain accurate, producing `"1-3"`.
6. **Null Elements in Collections:** Null elements inside input collections (`[1, null, 2, 3]`) are filtered out gracefully.
7. **Negative Integers:** Ranges spanning negative numbers and crossing zero (e.g., `[-5, -4, -3, -1, 0, 1, 3]` $\rightarrow$ `"-5--3, -1-1, 3"`) are fully supported.
8. **Consecutive Pairs:** Two-element consecutive sequences (e.g., `[1, 2, 5, 6]`) are summarized as ranges (`"1-2, 5-6"`).

---

## Project Structure

```text
number-range-summarizer/
├── pom.xml
├── README.md
└── src/
    ├── main/java/numberrangesummarizer/
    │   ├── NumberRangeSummarizer.java       # Provided Interface
    │   └── NumberRangeSummarizerImpl.java   # Solution Implementation
    └── test/java/numberrangesummarizer/
        └── NumberRangeSummarizerTest.java   # 18 JUnit 5 Unit Tests
```

---

## Build and Test Instructions 

### Prerequisites
* **Java Development Kit (JDK):** Version 8 or higher
* **Apache Maven:** Version 3.6+

### Run Unit Tests
Execute the comprehensive test suite with:
```
mvn test
```








