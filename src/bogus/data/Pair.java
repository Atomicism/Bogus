package bogus.data;

public class Pair<T, E> {
    public T first;
    public E second;

    public Pair(T v1, E v2){
        first = v1;
        second = v2;
    }

    public String toString(){
        return String.format("(%s, %s)", first, second);
    }
}
