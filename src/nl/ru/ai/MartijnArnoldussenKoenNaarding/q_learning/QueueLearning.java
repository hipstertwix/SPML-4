package nl.ru.ai.MartijnArnoldussenKoenNaarding.q_learning;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import nl.ru.ai.vroon.mdp.Action;
import nl.ru.ai.vroon.mdp.Field;
import nl.ru.ai.vroon.mdp.MarkovDecisionProblem;

public class QueueLearning {

	
	private final MarkovDecisionProblem mdp;
	private final static Action[] ACTIONS = {Action.UP, Action.DOWN, Action.LEFT, Action.RIGHT};
	private final double[][] values;
	private final Map<Coord, Integer> map;
	
	private final Random rnd;
	
	
	public QueueLearning(MarkovDecisionProblem mdp, long seed) {
		
		this.mdp = mdp;
		this.values = new double[mdp.getWidth()][mdp.getHeight()];
		
		this.map = new HashMap<>();
		
		
		this.rnd = new Random(seed);
		
		for(int x=0; x < mdp.getWidth(); x++) {
			
			for(int y=0; y < mdp.getHeight(); y++) {
				
				if(mdp.getField(x, y) == Field.REWARD || mdp.getField(x, y) == Field.NEGREWARD) {
					values[x][y] = mdp.getReward(mdp.getField(x, y));
				}
			}
		}
	}
	
	public void step(boolean random) {
		
		
	}
	
	
}
