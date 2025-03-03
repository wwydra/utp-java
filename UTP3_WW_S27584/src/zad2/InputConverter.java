package zad2;

import java.util.function.Function;

public class InputConverter<T> {

    private T val;

    public InputConverter(T val) {
        this.val = val;
    }

    public <R> R convertBy(Function... fun){
        R res = null;
        T data = val;
        for (Function<T, R> f : fun){
            res = f.apply(data);
            data = (T) res;
        }
        return res;
    }
}
