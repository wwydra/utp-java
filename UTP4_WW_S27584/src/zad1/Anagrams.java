/**
 *
 *  @author Wydra Weronika S27584
 *
 */

package zad1;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Anagrams {

    private final String allWords; //ścieżka do pliku
    private List<List<String>> anagrams;

    public Anagrams(String allWords) {
        this.allWords = allWords;
    }

    public List<List<String>> getSortedByAnQty(){
        List<String> wordsList = new ArrayList<>();

        try{
            Scanner scanner = new Scanner(new File(allWords));

            while (scanner.hasNext()){
                wordsList.add(scanner.next());
            }

            scanner.close();
        }catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        }

        Map<String, List<String>> anagramMap = new HashMap<>();
        for (String word: wordsList){
            char[] chars = word.toCharArray();
            Arrays.sort(chars);

            String key = new String(chars);
            if (!anagramMap.containsKey(key)){
                anagramMap.put(key, new ArrayList<>());
            }
            anagramMap.get(key).add(word);
        }

        this.anagrams = new ArrayList<>(anagramMap.values());
        this.anagrams = this.anagrams.stream().sorted((o2, o1) -> {
            if (o1.size() != o2.size()){
                return Integer.compare(o1.size(), o2.size());
            }else{
                return o1.get(0).compareTo(o2.get(0));
            }
        }).collect(Collectors.toList());

        return this.anagrams;
    }

    public String getAnagramsFor(String word){
        List<String> forWord = new ArrayList<>();
        boolean found = false;

        for (int i = 0; i < this.anagrams.size() && !found; i++){
            forWord = this.anagrams.get(i);
            if (forWord.contains(word)){
                found = true;
            }
        }

        forWord.remove(word);
        if (found) {
            return word + ": " + forWord;
        }else{
            return word + ": null";
        }
    }
}
