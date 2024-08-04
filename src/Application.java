import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Application {

  public static void main(String[] args) {
    try {
      File file = new File("inputText.txt");
      Scanner scanner = new Scanner(file);
      String text = scanner.nextLine();
      SummaryModel summaryModel = new SummaryModel();
      System.out.println(summaryModel.summarize(text));
      System.out.println(summaryModel.summarize(text, 5));
      scanner.close();
    } catch (FileNotFoundException exception) {
      System.out.println("Unable to find the file.");
    }
  }
}
