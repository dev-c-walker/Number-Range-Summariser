package numberrangesummarizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Caleb Walker
 *
 * Implementation of the NumberRangeSummarizer interface.
 * 
 * Provides functionality to:
 * 1. Parse comma-delimited number strings into a typed collection.
 * 2. Summarize sequential integers into compact hyphenated ranges.
 */
public class NumberRangeSummarizerImpl implements NumberRangeSummarizer {

   private static final String COMMA_DELIMITER = ",";
   private static final String OUTPUT_DELIMITER = ", ";
   private static final String RANGE_SEPARATOR = "-";
   
   /**
    * Takes a comma-delimited string and returns it as a Collection of Integers
    *
    * @param input Raw comma-separated string 
    * @return Collection of parsed Integer values or empty list if input is empty/null
    * @throws IllegalArgumentException if any token is not a valid integer
    */
   @Override
   public Collection<Integer> collect(String input){
      
      // check if empty or null
      if (input == null || input.trim().isEmpty()){
         return Collections.emptyList();
      }
         
      return Arrays.stream(input.split(COMMA_DELIMITER)) // split into string tokens and convert to stream
                   .map(String::trim) // remove leading or trailing spaces 
                   .filter(token -> !token.isEmpty()) // filter out any blank tokens
                   .map(this::parseToken) // covert string into integer
                   .collect(Collectors.toList()); // bundle stream into list of integers
   }
   
   
   /**
    * Parses a single token to an Integer, handling the failure in a clearer  
    * exception than the raw NumberFormatException 
    */
   private Integer parseToken(String token){
      try{
         return Integer.valueOf(token);
      } catch (NumberFormatException e){
         throw new IllegalArgumentException("Invalid number in input: '" + token + "'", e);
      }
   } 
   
   
   /**
    * Groups consecutive integers in a collection into sequential range strings 
    *
    * @param input A collection of Integers
    * @return A comma-separated summarized string 
    */
   @Override
   public String summarizeCollection(Collection<Integer> input){
      // check if empty or null
      if (input == null || input.isEmpty()){
         return "";
      }
      
      // sanitise the input 
      List<Integer> sortedNumbers = input.stream() 
                                    .filter(num -> num != null) // remove any null
                                    .distinct() // remove duplicates 
                                    .sorted() // arrange in ascending order
                                    .collect(Collectors.toList());
                                    
      // ensure list is not empty
      if (sortedNumbers.isEmpty()){
         return "";
      }
      
      List<String> formattedRanges = new ArrayList<>();
      
      int rangeStart = sortedNumbers.get(0);
      int rangeEnd = rangeStart;
      
      // single pass over the elements 
      for (int i = 1; i < sortedNumbers.size(); i++){ // i = 1 because we already know what is at i = 0
         
         int currentNum = sortedNumbers.get(i); // get the current number 
         
         if (currentNum == rangeEnd + 1){
            rangeEnd = currentNum; // number is consecutive, therefore extend range
         } else {
            // sequence broken, therefore add range
            formattedRanges.add(formatRange(rangeStart,rangeEnd));
            
            // reset pointers 
            rangeStart = currentNum;
            rangeEnd = currentNum;
         }
      }
      
      formattedRanges.add(formatRange(rangeStart,rangeEnd));
      
      return String.join(OUTPUT_DELIMITER, formattedRanges);
   }
   
   
   /**
    * Method to assist with the format of individual range intervals 
    *
    * if start == end then its a single element 
    * if start < end then its multi-element range  
    */
   private static String formatRange(int start, int end){
      if (start == end){
         return String.valueOf(start);
      } 
      
      return start + RANGE_SEPARATOR + end;
   }
   
   


}