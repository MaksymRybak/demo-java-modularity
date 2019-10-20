package javamodularity.easytext.analysis.kincaid;

import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import javamodularity.easytext.analysis.api.Analyzer;

import javamodularity.easytext.analysis.kincaid.internal.SyllableCounter;

public class KincaidAnalyzer implements Analyzer {

   public String getName() {
      return "Flesch-Kincaid";
   }

   public double analyze(List<List<String>> sentences) {
      float totalsentences = sentences.size();
      float totalwords = sentences.stream().mapToInt(sentence -> sentence.size()).sum();
      float totalsyllables = sentences.stream()
         .flatMapToInt(sentence ->
            sentence.stream().mapToInt(word -> SyllableCounter.countSyllables(word)))
         .sum();
      return 206.835 - 1.015 * (totalwords / totalsentences) - 84.6 * (totalsyllables / totalwords);
   }

}
