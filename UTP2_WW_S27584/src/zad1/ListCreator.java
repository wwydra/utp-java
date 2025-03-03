package zad1;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class ListCreator<T> { // Uwaga: klasa musi być sparametrtyzowana
    private List<T> list;

    public ListCreator(List<T> list) {
        this.list = list;
    }

    public static <T> ListCreator<T> collectFrom(List<T> list){
        return new ListCreator<>(list);
    }

    public ListCreator<T> when(Predicate<T> predicate){
        List<T> selected = new ArrayList<>();
        for (T t : list) {
            if (predicate.test(t)) {
                selected.add(t);
            }
        }
        return new ListCreator<>(selected);
    }

    public <Y> List<Y> mapEvery(Function<T, Y> function){
        List<Y> mapped = new ArrayList<>();
        for (T t : list) {
            mapped.add(function.apply(t));
        }
        return mapped;
    }
}