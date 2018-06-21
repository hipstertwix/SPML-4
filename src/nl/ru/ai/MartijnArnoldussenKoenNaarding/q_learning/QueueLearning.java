package nl.ru.ai.MartijnArnoldussenKoenNaarding.q_learning;

import nl.ru.ai.vroon.mdp.Action;
import nl.ru.ai.vroon.mdp.Field;
import nl.ru.ai.vroon.mdp.MarkovDecisionProblem;

import java.util.*;

public class QueueLearning {

	
	private final MarkovDecisionProblem mdp;
	private Coord startPosition;
	private final static Action[] ACTIONS = {Action.UP, Action.DOWN, Action.LEFT, Action.RIGHT};
	private final static Double DEFAULT_VALUE = 0d;
	private Map<Tuple<Coord, Action>, Double> valuesMap;
	private Map<Tuple<Coord, Action>, Double> valuesNew;
	private final Map<Tuple<Coord, Action>, Integer> countsMap;
	
	private final Random seed;

	private final boolean random;
	private final double discountFactor;
	private final double learningRate;
	private final int maxDepth;


	public QueueLearning(MarkovDecisionProblem mdp, long seed, boolean random, double discountFactor,
						 double learningRate, int maxDepth) {
		
		this.mdp = mdp;
		this.valuesMap = new HashMap<>();
		this.valuesNew = new HashMap<>();
		this.countsMap = new HashMap<>();

		this.discountFactor = discountFactor;
		this.learningRate = learningRate;
		this. maxDepth = maxDepth;


		this.random = random;
		this.seed = new Random(seed);
	}

	private static void printf(Object o, Object... args) {
        System.out.println(String.format(o.toString(), args));
    }

	/**
	 * shallow copy
	 * @param map the map
	 * @return shallow copy of map
	 */
	private <T, U> Map copy(Map<T, U> map) {
		Map map_ = new HashMap<T, U>();
		for(T key : map.keySet()) {
			map_.put(key, map.get(key));
		}
		return map_;
	}

	public Map<Tuple<Coord, Action>, Integer> getCountMap() {
	    return countsMap;
    }

	public void step(boolean print) {
	    while(mdp.getField(mdp.getStateXPosition(), mdp.getStateYPostion()) != Field.EMPTY) {
	        mdp.setState(seed.nextInt(mdp.getWidth()), seed.nextInt(mdp.getHeight()));
        }


		Action action = chooseAction();
        this.startPosition = new Coord(mdp.getStateXPosition(), mdp.getStateYPostion());
        mdp.performAction(action);

		Tuple<Coord, Action> coordAction = new Tuple<>(startPosition, action);
		countsMap.put(coordAction, countsMap.getOrDefault(coordAction, 0)+1);

		double value = val(coordAction, 0);

		valuesNew.put(coordAction, value);

		valuesMap = copy(valuesNew);

	}



	private double val(Tuple<Coord, Action> coordAction, int depth) {
		Field f = mdp.getField(coordAction.getFirst().getX(), coordAction.getFirst().getY());
		if(f == Field.REWARD || f == Field.NEGREWARD) {
			return mdp.getReward(f);
		}

		Coord curCoord = move(coordAction);
		double oldValue = valuesMap.getOrDefault(coordAction, DEFAULT_VALUE);

		double reward = mdp.getReward(mdp.getField(mdp.getStateXPosition(), mdp.getStateYPostion()));//mdp.getReward(f);
        printf(String.format("tried %s from %s with reward %s for field %s", coordAction.getSecond(), coordAction.getFirst(), reward, mdp.getField(mdp.getStateXPosition(), mdp.getStateYPostion())));
		double discount = Math.pow(discountFactor, depth);
		double optimalFutureValue;
		if(depth >= maxDepth) {
			optimalFutureValue =  valuesMap.getOrDefault(coordAction, DEFAULT_VALUE);
		} else {
			List<Double> futureValues = new ArrayList<>();
			for (Action action : ACTIONS) {
				double val = val(new Tuple<>(curCoord, action), depth + 1);
				futureValues.add(val);
				Tuple<Coord, Action> tup = new Tuple<>(curCoord, action);
				//valuesNew.put(tup, val);
			}
			optimalFutureValue = Collections.max(futureValues);
		}
		double val  = (1 - learningRate) * oldValue + learningRate * (reward + discount * optimalFutureValue);
		return val;
	}

	/**
	 * Returns the Coord you get when moving from the given Coord in the given direction. Will take out of bounds
	 * and obstacles into account.
	 * @param coordAction
	 * @return
	 */
	private Coord move(Tuple<Coord, Action> coordAction) {
		Coord coord = coordAction.getFirst();
		int x = coord.getX();
		int y = coord.getY();
		Coord noChange = new Coord(x, y);

		Field newField;

		switch(coordAction.getSecond()) {
			case UP:
				newField = mdp.getField(x, y+1);
				if(newField.equals(Field.OBSTACLE) || newField.equals(Field.OUTOFBOUNDS)) {
					return noChange;
				}
				return  new Coord(x, y+1);
			case RIGHT:
				newField = mdp.getField(x+1, y);
				if(newField.equals(Field.OBSTACLE) || newField.equals(Field.OUTOFBOUNDS)) {
					return noChange;
				}
				return  new Coord(x+1, y);
			case DOWN:
				newField = mdp.getField(x, y-1);
				if(newField.equals(Field.OBSTACLE) || newField.equals(Field.OUTOFBOUNDS)) {
					return noChange;
				}
				return  new Coord(x, y-1);
			case LEFT:
				newField = mdp.getField(x-1, y);
				if(newField.equals(Field.OBSTACLE) || newField.equals(Field.OUTOFBOUNDS)) {
					return noChange;
				}
				return  new Coord(x-1, y);
			default:
				return noChange;
		}
	}

    /**
     * TODO: this is bad if its not random
     * @return
     */
	private Action chooseAction() {

		if(this.random) {
			return ACTIONS[seed.nextInt(4)];
		} else {
			Action leastFrequent = null;
			Integer leastFrequentCount = Integer.MAX_VALUE;
			for(Action a : ACTIONS) {
				Tuple<Coord, Action> coordAction = new Tuple<>(startPosition, a);
				if(countsMap.containsKey(coordAction)) {
					if(leastFrequent == null || countsMap.get(coordAction) < leastFrequentCount) {
						leastFrequent = a;
						leastFrequentCount = countsMap.get(coordAction);
					}
				} else {
					leastFrequent = a;
					break;
				}
			}
			return leastFrequent;
		}

	}

	public void printActions() {
		Map<Action, Character> prettyMap = new HashMap<>();
		prettyMap.put(Action.UP, '^');
		prettyMap.put(Action.RIGHT, '>');
		prettyMap.put(Action.DOWN, 'v');
		prettyMap.put(Action.LEFT, '<');
		prettyMap.put(null, ' ');
		for(int y = mdp.getHeight()-1; y >= 0; y--) {
			StringBuilder sb = new StringBuilder();
			for(int x = 0; x < mdp.getWidth(); x++) {

				Coord coord = new Coord(x, y);
				double bestScore = Double.NEGATIVE_INFINITY;
				Action bestAction = Action.UP;
				for(Action action : ACTIONS) {
					Tuple<Coord, Action> coordAction = new Tuple<>(coord, action);
					if(valuesMap.containsKey(coordAction) && valuesMap.get(coordAction) >  bestScore) {
						bestScore = valuesMap.get(coordAction);
						bestAction = action;
					}
				}

				sb.append(prettyMap.get(bestAction) + " ");
			}
			System.out.println(sb + "\n");
		}
	}


    public void printGridValues() {
        for(int y = mdp.getHeight()-1; y >= 0; y--) {
            StringBuilder sb = new StringBuilder();
            for(int x = 0; x < mdp.getWidth(); x++) {

                Coord coord = new Coord(x, y);
                double bestScore = Double.NEGATIVE_INFINITY;
                for(Action action : ACTIONS) {
                    Tuple<Coord, Action> coordAction = new Tuple<>(coord, action);
                    if(valuesMap.containsKey(coordAction) && valuesMap.get(coordAction) >  bestScore) {
                        bestScore = valuesMap.get(coordAction);
                    }
                }

                sb.append(bestScore + " ");
            }
            System.out.println(sb + "\n");
        }
    }
}
