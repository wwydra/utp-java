package zad3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProgLang {

    private final Map<String, List<String>> langsMap = new LinkedHashMap<>();
    private final Map<String, List<String>> progsMap = new LinkedHashMap<>();

    public ProgLang(String nazwaPliku) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(nazwaPliku));

            String line;
            while ((line = br.readLine()) != null){
                String[] split = line.split("\t");
                if (split.length > 0){
                    String key = split[0];
                    if (!this.langsMap.containsKey(key)){
                        this.langsMap.put(key, new ArrayList<>());
                    }

                    List<String> prog = new ArrayList<>();
                    for (int i = 1; i < split.length; i++) {
                        if (!prog.contains(split[i])){
                            prog.add(split[i]);
                        }
                    }
                    this.langsMap.put(key, prog);
                }
            }

            br.close();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public Map<String, List<String>> getLangsMap(){

        return this.langsMap;
    }

    public Map<String, List<String>> getProgsMap(){
        Set<String> keySet = this.langsMap.keySet();

        for (String s : keySet){
            List<String> tmp = this.langsMap.get(s);
            for (String prog : tmp){
                if (!this.progsMap.containsKey(prog)){
                    this.progsMap.put(prog, new ArrayList<>());
                }
                List<String> langs = this.progsMap.get(prog);
                if (!langs.contains(s)) {
                    langs.add(s);
                }
            }
        }

        return this.progsMap;
    }

    public Map<String, List<String>> getLangsMapSortedByNumOfProgs(){
        return this.langsMap.entrySet().stream().sorted((o2, o1) -> {
            if (o1.getValue().size() != o2.getValue().size()){
                return Integer.compare(o1.getValue().size(), o2.getValue().size());
            }else{
                return o1.getKey().compareTo(o2.getKey());
            }
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

    }

    public Map<String, List<String>> getProgsMapSortedByNumOfLangs(){
        return this.progsMap.entrySet().stream().sorted((o2, o1) -> {
            if (o1.getValue().size() != o2.getValue().size()){
                return Integer.compare(o1.getValue().size(), o2.getValue().size());
            }else{
                return o2.getKey().compareTo(o1.getKey());
            }
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public Map<String, List<String>> getProgsMapForNumOfLangsGreaterThan(int n){
        Map<String, List<String>> nProgsMap = new LinkedHashMap<>();

        Set<String> keySet = this.progsMap.keySet();
        for (String key : keySet) {
            int count = 0;

            List<String> list = this.progsMap.get(key);
            for (String lang : list){
                count++;
            }

            if (count > n){
                nProgsMap.put(key, list);
            }
        }

        return nProgsMap;
    }

    public <K, V> Map<K, V> sorted(Map<K, V> map, Comparator<Map.Entry<K, V>> comparator){
        return map.entrySet().stream().sorted(comparator).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public <K, V> Map<K, V> filtered(Map<K, V> map, Predicate<Map.Entry<K, V>> predicate) {
        return map.entrySet().stream().filter(predicate).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
