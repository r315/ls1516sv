package utils;

public class Pair<T,U> {
    public T value1;
    public U value2;

    public Pair(){
        this.value1 = null;
        this.value2 = null;
    }

    public Pair(T value1, U value2) {
        this.value1 = value1;
        this.value2 = value2;
    }
}