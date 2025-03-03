/**
 *
 *  @author Wydra Weronika S27584
 *
 */

package zad2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*<--
 *  niezbędne importy
 */
public class Main {
  public static void main(String[] args) {
    /*<--
     *  definicja operacji w postaci lambda-wyrażeń:
     *  - flines - zwraca listę wierszy z pliku tekstowego
     *  - join - łączy napisy z listy (zwraca napis połączonych ze sobą elementów listy napisów)
     *  - collectInts - zwraca listę liczb całkowitych zawartych w napisie
     *  - sum - zwraca sumę elmentów listy liczb całkowitych
     */
    Function<String, List<String>> flines = (fname) -> {
      try{
        return Files.readAllLines(Paths.get(fname));
      }catch (IOException e){
        e.printStackTrace();
        return Collections.emptyList();
      }
    };

    Function<List<String>, String> join = (list) -> {
      StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s);
        }
        return sb.toString();
    };

    Function<String, List<Integer>> collectInts = (s) -> {
      List<Integer> ints = new ArrayList<>();
      Pattern pattern = Pattern.compile("\\d+");
      Matcher matcher = pattern.matcher(s);

      while (matcher.find()){
        ints.add(Integer.parseInt(matcher.group()));
      }
      return ints;
    };

    Function<List<Integer>, Integer> sum = (list) -> {
      int res = 0;
        for (int val : list){
          res += val;
        }
        return res;
    };

    String fname = System.getProperty("user.home") + "/LamComFile.txt";
    InputConverter<String> fileConv = new InputConverter<>(fname);
    List<String> lines = fileConv.convertBy(flines);
    String text = fileConv.convertBy(flines, join);
    List<Integer> ints = fileConv.convertBy(flines, join, collectInts);
    Integer sumints = fileConv.convertBy(flines, join, collectInts, sum);

    System.out.println(lines);
    System.out.println(text);
    System.out.println(ints);
    System.out.println(sumints);

    List<String> arglist = Arrays.asList(args);
    InputConverter<List<String>> slistConv = new InputConverter<>(arglist);
    sumints = slistConv.convertBy(join, collectInts, sum);
    System.out.println(sumints);

  }
}
