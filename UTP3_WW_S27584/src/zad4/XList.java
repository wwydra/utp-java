package zad4;

import java.util.*;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class XList<T> extends ArrayList<T>{

    public XList(Collection<T> collection) {
        super(collection);
    }

    public XList(T... args){
        super(Arrays.asList(args));
    }

    public static <T> XList<T> of(T... args){
        return new XList<>(args);
    }

    public static <T> XList<T> ofColl(Collection<T> collection){
        return new XList<>(collection);
    }

    public static XList<String > ofChars(String napis){
        return new XList<>(napis.split(""));
    }

    public static XList<String> ofTokens(String napis, String... sep){
        List<String> tl = new ArrayList<>();
        String separator = (sep.length > 0) ? sep[0] : "\\s+";
        StringTokenizer tokenizer = new StringTokenizer(napis, separator);

        while (tokenizer.hasMoreTokens()){
            tl.add(tokenizer.nextToken());
        }
        return new XList<>(tl);
    }

    public XList<T> union(Collection<T> collection){
        List<T> newList = new ArrayList<>(this);
        newList.addAll(collection);
        return new XList<>(newList);
    }

    public <E> XList<T> diff(Collection<E> collection) {
        List<T> res = new ArrayList<>(this);
        res.removeAll(collection);
        return new XList<>(res);
    }

    public XList<T> unique(){
        List<T> u = new ArrayList<>();

        for (T t : this){
            if (!u.contains(t)){
                u.add(t);
            }
        }
        return new XList<>(u);
    }

    public XList<List<T>> combine(){
        XList<List<T>> combinations = new XList<>();
        combinations.add(new ArrayList<>());

        for (T t : this) {
            if (t instanceof List) {
                List<T> curr = (List<T>) t;
                XList<List<T>> newRes = new XList<>();

                for (T item : curr) {
                    for (List<T> prev : combinations) {
                        List<T> newComb = new ArrayList<>(prev);
                        newComb.add(item);
                        newRes.add(newComb);
                    }
                }

                combinations = newRes;
            }
        }

        return combinations;
    }

    public <R> XList<R> collect(Function<T, R> function) {
        List<R> res = new ArrayList<>();
        for (T t : this) {
            R result = function.apply(t);
            if (result instanceof XList) {
                res.addAll((XList<R>) result);
            } else {
                res.add(result);
            }
        }
        return new XList<>(res);
    }

    public String join(String... sep){
        String separator = (sep.length > 0) ? sep[0] : "";
        return this.stream().map(s -> s.toString()).collect(Collectors.joining(separator));
    }

    public void forEachWithIndex(BiConsumer<T, Integer> consumer){
        if (!this.isEmpty()) {
            for (int i = 0; i < this.size(); i++) {
                consumer.accept(get(i), i);
            }
        }
    }
}
