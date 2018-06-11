package nl.ru.ai.MartijnArnoldussenKoenNaarding.q_learning;

public class Coord {

	
	private final int x;
	private final int y;
	
	
	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
	public boolean equals(int a, int b) {
		return x == a && y == b;
	}
	
}
