package bogus.util;

import bogus.func.*;

public interface Eachable<T>{
    void each(Cons<? super T> cons);
}