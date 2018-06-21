package nl.ru.ai.MartijnArnoldussenKoenNaarding.q_learning;

import java.util.Objects;

public class Tuple<T, U> {
	
	private final T t;
	private final U u;
	
	public Tuple(T t, U u) {
	    this.t = t;
	    this.u = u;
    }

    public T getFirst() {
	    return this.t;
    }

    public U getSecond() {
        return this.u;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple<?, ?> tuple = (Tuple<?, ?>) o;
        return Objects.equals(t, tuple.t) &&
                Objects.equals(u, tuple.u);
    }

    @Override
    public int hashCode() {

        return Objects.hash(t, u);
    }

    public String toString() {
	    return String.format("(%s, %s)", t, u);
    }
}