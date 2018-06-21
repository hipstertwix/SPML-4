package nl.ru.ai.MartijnArnoldussenKoenNaarding.q_learning;

import java.util.Objects;

public class Coord {

	
	private final int x;
	private final int y;
	
	
	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Coord coord = (Coord) o;
		return x == coord.x &&
				y == coord.y;
	}

	@Override
	public int hashCode() {

		return Objects.hash(x, y);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public String toString() {
		return String.format("(%s, %s)", x, y);
	}
}
