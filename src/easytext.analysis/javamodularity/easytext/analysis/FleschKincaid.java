package javamodularity.easytext.analysis;

import java.util.ArrayList;
import java.util.List;

import static javamodularity.easytext.analysis.internal.SyllableCounter.countSyllables;

public class FleschKincaid {

   public double analyze(List<List<String>> sentences) {
      float totalsentences = sentences.size();
      float totalwords = sentences.stream().mapToInt(sentence -> sentence.size()).sum();
      float totalsyllables = sentences.stream()
         .flatMapToInt(sentence ->
            sentence.stream().mapToInt(word -> countSyllables(word)))
         .sum();
      return 206.835 - 1.015 * (totalwords / totalsentences) - 84.6 * (totalsyllables / totalwords);
   }


}
