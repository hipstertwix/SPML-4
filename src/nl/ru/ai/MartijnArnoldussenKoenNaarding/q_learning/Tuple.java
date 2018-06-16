package nl.ru.ai.MartijnArnoldussenKoenNaarding.q_learning;

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
    public boolean equals(Object obj) {
	    if(!(obj instanceof Tuple)) {
	        return false;
        }
        Tuple tup = (Tuple) obj;
        return this.t.equals(tup.t) && this.u.equals(tup.u);
    }

    public String toString() {
	    return String.format("(%s, %s)", t, u);
    }
}