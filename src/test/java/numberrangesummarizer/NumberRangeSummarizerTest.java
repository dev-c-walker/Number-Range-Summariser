package numberrangesummarizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName; 
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals; 
import static org.junit.jupiter.api.Assertions.assertThrows; 
import static org.junit.jupiter.api.Assertions.assertTrue; 

class NumberRangeSummarizerTest {
   
   private NumberRangeSummarizer summarizer;
   
   @BeforeEach
   void setup() {
      summarizer = new NumberRangeSummarizerImpl();
   }
   
   @Nested
   @DisplayName ("collect() Method Tests")
   class CollectTests {
      
      @Test
      @DisplayName ("Should successfully parse standard comma-delimited input")
      void testCollectStandardInput() {
         String input = "1, 3, 6, 7, 8, 12, 13, 14, 15, 21, 22, 23, 24, 31";
         Collection<Integer> result = summarizer.collect(input);
         List<Integer> expected = Arrays.asList(1, 3, 6, 7, 8, 12, 13, 14, 15, 21, 22, 23, 24, 31);
         
         assertEquals(expected, result);
      }
      
      @Test
      @DisplayName ("Should handle irregular whitespace around numbers")
      void testCollectWithIrregularWhitespace() {
         String input = "1 , 3,    6, 7 ,8 ";
         Collection<Integer> result = summarizer.collect(input);
         List<Integer> expected = Arrays.asList(1, 3, 6, 7, 8);
         
         assertEquals(expected, result);
      }
      
      @Test
      @DisplayName ("Should parse negative numbers correctly")
      void testCollectWithNegativeNumbers() {
         String input = "-3, -2, -1, 0, 1, 2";
         Collection<Integer> result = summarizer.collect(input);
         List<Integer> expected = Arrays.asList(-3, -2, -1, 0, 1, 2);
         
         assertEquals(expected, result);
      }
      
      @Test
      @DisplayName ("Should return an empty collection when input null or blank")
      void testCollectWithNullAndEmptyInputs() {
         assertTrue(summarizer.collect(null).isEmpty(), "Null input should return empty collection");
         assertTrue(summarizer.collect("").isEmpty(), "Empty string should return empty collection");
         assertTrue(summarizer.collect("   ").isEmpty(), "Blank string should return empty collection");
      }
      
      @Test
      @DisplayName ("Should ignore extra delimiters and consecutive commas")
      void testCollectWithConsecutiveDelimiters(){
         String input = "1,,,2,3";
         Collection<Integer> result = summarizer.collect(input);
         List<Integer> expected = Arrays.asList(1, 2, 3);
         
         assertEquals(expected, result);

      }
      
      
      @Test
      @DisplayName ("Should throw IllegelArgumentException on non-numeric input")
      void testCollectInvalidNonNumericInput() {
         String invalidInput = "1, 2, abc, 4, 5";
         IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> summarizer.collect(invalidInput));
         assertTrue(ex.getMessage().contains("abc"));
      }
      
      @Test
      @DisplayName ("Should throw IllegelArgumentException on decimal values")
      void testCollectInvalidDecimalInput() {
         String invalidInput = "1, 2, 3.14, 4";
         IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> summarizer.collect(invalidInput));
         assertTrue(ex.getMessage().contains("3.14"));
      }
      
   }
   
   @Nested
   @DisplayName("summarizeCollection() Method Tests")
   class SummarizeCollectionTests{
      
      @Test
      @DisplayName ("Should summarize standard mixed sequential and non-sequential input")
      void testSummarizeStandardInput() {
         List<Integer> input = Arrays.asList(1, 3, 6, 7, 8, 12, 13, 14, 15, 21, 22, 23, 24, 31);
         String result = summarizer.summarizeCollection(input);
         
         assertEquals("1, 3, 6-8, 12-15, 21-24, 31", result);
      }
      
      @Test
      @DisplayName ("Should summarize single continous range")
      void testSummarizeSingleContinousRange() {
         List<Integer> input = Arrays.asList(1, 2, 3, 4, 5);
         String result = summarizer.summarizeCollection(input);
         
         assertEquals("1-5", result);
      }
      
      @Test
      @DisplayName ("Should summarize all non-sequential numbers without ranges")
      void testSummarizeNoConsecutiveNumbers() {
         List<Integer> input = Arrays.asList(1, 3, 5, 7, 9);
         String result = summarizer.summarizeCollection(input);
         
         assertEquals("1, 3, 5, 7, 9", result);
      }
      
      @Test
      @DisplayName ("Should handle single element collection")
      void testSummarizeSingleElement() {
         List<Integer> input = Arrays.asList(42);
         String result = summarizer.summarizeCollection(input);
         
         assertEquals("42", result);
      }
      
      @Test
      @DisplayName ("Should return empty string for null or empty collections")
      void testSummarizeNullAndEmpty() {
         assertEquals("", summarizer.summarizeCollection(null));
         assertEquals("", summarizer.summarizeCollection(Collections.emptyList()));
      }
      
      @Test
      @DisplayName("Should sort unsorted input before summarizing")
      void testSummarizeUnsortedInput() {
         List<Integer> input = Arrays.asList(8, 1, 7, 3, 6);
         String result = summarizer.summarizeCollection(input);

         assertEquals("1, 3, 6-8", result);
      }

      @Test
      @DisplayName("Should deduplicate repeated elements")
      void testSummarizeWithDuplicates() {
         List<Integer> input = Arrays.asList(1, 1, 2, 2, 3, 3, 5, 5);
         String result = summarizer.summarizeCollection(input);

         assertEquals("1-3, 5", result);
      }

      @Test
      @DisplayName("Should ignore null elements inside collection")
      void testSummarizeWithNullElements() {
         List<Integer> input = Arrays.asList(1, null, 2, 3, null, 7);
         String result = summarizer.summarizeCollection(input);

         assertEquals("1-3, 7", result);
      }

      @Test
      @DisplayName("Should summarize negative number sequences spanning across zero")
      void testSummarizeNegativeRanges() {
         List<Integer> input = Arrays.asList(-5, -4, -3, -1, 0, 1, 3);
         String result = summarizer.summarizeCollection(input);

         assertEquals("-5--3, -1-1, 3", result);
      }

      @Test
      @DisplayName("Should summarize pairs of consecutive numbers as two-element ranges")
      void testSummarizeTwoElementRanges() {
         List<Integer> input = Arrays.asList(1, 2, 5, 6);
         String result = summarizer.summarizeCollection(input);

         assertEquals("1-2, 5-6", result);
      }
   }

   @Nested
   @DisplayName("End-to-End Pipeline Tests")
   class EndToEndPipelineTests {

      @Test
      @DisplayName("Should correctly pipe collect() directly into summarizeCollection()")
      void testFullPipelineSample() {
         String input = "1, 3, 6, 7, 8, 12, 13, 14, 15, 21, 22, 23, 24, 31";
         Collection<Integer> collected = summarizer.collect(input);
         String summarized = summarizer.summarizeCollection(collected);

         assertEquals("1, 3, 6-8, 12-15, 21-24, 31", summarized);
      }
   }
}