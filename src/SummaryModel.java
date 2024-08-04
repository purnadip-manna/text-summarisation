import java.util.*;
import java.util.Map.Entry;

public class SummaryModel {
  private String[] sentences = null;
  private Float[] sentencesRelevanceScore = null;
  private String summarizedText;

  private Map<String, Integer> wordList = new HashMap<>();
  private Map<String, Float> wordRelevanceScores = new HashMap<>();
  private Map<Float, String> rsSentence = new HashMap<>();

  private void setUp(String actualText) {
    sentences = actualText.split("\\.");
    sentencesRelevanceScore = new Float[sentences.length];
    wordList = new HashMap<>();
    wordRelevanceScores = new HashMap<>();
    rsSentence = new HashMap<>();
    summarizedText = "";
  }

  public String summarize(String actualText) {
    setUp(actualText);
    return summarize(actualText, sentences.length / 2);
  }

  public String summarize(String actualText, int noOfSentences) {
    if (sentences == null) {
      setUp(actualText);
    }
    String processedText = removePunctuations(actualText.toLowerCase());
    prepareWordList(processedText);
    int maxOccurrenceWordCount = getMaxOccurrenceWordCount();
    prepareWordRelevanceScores(maxOccurrenceWordCount);
    prepareSentenceRelevanceScores();
    Arrays.sort(sentencesRelevanceScore, Collections.reverseOrder());
    Arrays.sort(sentencesRelevanceScore, 0, noOfSentences);
    for (int i = 0; i < noOfSentences; i++) {
      summarizedText += rsSentence.get(sentencesRelevanceScore[i]) + ".";
    }
    sentences = null;
    return summarizedText.trim();
  }

  private String removePunctuations(String text) {
    String regex = "\\p{P}";
    return text.replaceAll(regex, "");
  }

  private void prepareWordList(String text) {
    Scanner scanner = new Scanner(text);
    while (scanner.hasNext()) {
      String word = scanner.next();
      if (!wordList.containsKey(word)) {
        wordList.put(word, 1);
      } else {
        wordList.put(word, wordList.get(word) + 1);
      }
    }
    scanner.close();
  }

  private int getMaxOccurrenceWordCount() {
    int max = 0;
    for (Entry<String, Integer> entry : wordList.entrySet()) {
      if (entry.getValue() > max) max = entry.getValue();
    }
    return max;
  }

  private void prepareWordRelevanceScores(int maxOccurrenceWordCount) {
    for (Entry<String, Integer> entry : wordList.entrySet()) {
      wordRelevanceScores.put(entry.getKey(), (float) entry.getValue() / maxOccurrenceWordCount);
    }
  }

  private void prepareSentenceRelevanceScores() {
    int i = 0;
    for (String sentence : sentences) {
      float totalScore = 0.0F;
      String processedSentence = removePunctuations(sentence.toLowerCase()).trim();
      for (String word : processedSentence.split(" ")) {
        totalScore += wordRelevanceScores.get(word);
      }
      rsSentence.put(totalScore, sentence);
      sentencesRelevanceScore[i++] = totalScore;
    }
  }
}
