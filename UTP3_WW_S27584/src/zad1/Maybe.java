package zad1;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Maybe<T> {

    private T val;

    public Maybe(T val) {
        this.val = val;
    }
    public Maybe() {
    }

    public static <T> Maybe<T> of(T x){
        return new Maybe<>(x);
    }

    public void ifPresent(Consumer<T> cons){
        if (val != null){
            cons.accept(val);
        }
    }

    public <R> Maybe<R> map(Function<T, R> func){
        if (val != null){
            R res = func.apply(val);
            if (res != null) {
                return new Maybe<>(res);
            }
        }
        return new Maybe<>();
    }

    public T get(){
        if (val != null){
            return val;
        }else{
            throw new NoSuchElementException("maybe is empty");
        }
    }

    public boolean isPresent(){
        return val != null;
    }

    public T orElse(T defVal){
        if (val != null){
            return val;
        }else{
            return defVal;
        }
    }

    public Maybe<T> filter(Predicate<T> pred){
        if (val == null){
            return this;
        }else if(pred.test(val)){
            return new Maybe<>();
        }else{
            return this;
        }
    }

    @Override
    public String toString() {
        if (val != null) {
            return "Maybe has value " + val;
        }else{
            return "Maybe is empty";
        }
    }
}
