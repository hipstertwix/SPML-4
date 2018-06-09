package nl.ru.ai.MartijnArnoldussenKoenNaarding.value_iteration;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import nl.ru.ai.vroon.mdp.Action;
import nl.ru.ai.vroon.mdp.Field;
import nl.ru.ai.vroon.mdp.MarkovDecisionProblem;

public class ValueIteration {

	
	private final MarkovDecisionProblem mdp;

	private final static Action[] ACTIONS = {Action.UP, Action.DOWN, Action.LEFT, Action.RIGHT};
	
	public ValueIteration(MarkovDecisionProblem mdp) {
		
		this.mdp = mdp;
	}


	public double[][] calculateValues(int iterationDepth) {

		int w = mdp.getWidth();
		int h = mdp.getHeight();

		double[][] valuesPrv = new double[w][h];
		double[][] valuesCur = new double[w][h];
				
		for(int k = 0; k < iterationDepth; k++) {
			
			for(int x = 0; x < w; x++) {
				for(int y = 0; y < h; y++) {
					
					
					Field field = mdp.getField(x, y);
					switch(field) {
					case EMPTY:
						f(valuesPrv, valuesCur, x, y);
						break;						
					case NEGREWARD:
					case REWARD:
						valuesCur[x][y] = mdp.getReward(field);
						break;
					case OBSTACLE:
						break;
					case OUTOFBOUNDS:
						default:
							throw new IllegalStateException();
					}
					
						
				}
			}
			valuesPrv = valuesCur;			
//			System.out.println(String.format("\n%s:\n", k));
//			printTable(valuesCur);
			valuesCur = new double[w][h];
		}
		
		return valuesPrv;
	}

	/**
	 * Function extraction for clarity, is only called at one point.
	 * @param valuesPrv
	 * @param valuesCur
	 * @param x
	 * @param y
	 * @throws Error
	 */
	private void f(double[][] valuesPrv, double[][] valuesCur, int x, int y) throws Error {
		double[] values = new double[ACTIONS.length];
		for(int a = 0; a < ACTIONS.length; a++) {
			
			Action action = ACTIONS[a];
			
			double success = mdp.getActionSuccess();
			double fail = 1 - success;
			
			double value;
			switch(action) {
			case UP:
				value = success * gridVal(valuesPrv, Action.UP, x, y, mdp) +
						fail/2 * gridVal(valuesPrv, Action.LEFT, x, y, mdp) +
						fail/2 * gridVal(valuesPrv, Action.RIGHT, x, y, mdp);
				break;
			case LEFT:
				value = success * gridVal(valuesPrv, Action.LEFT, x, y, mdp) +
						fail/2 * gridVal(valuesPrv, Action.DOWN, x, y, mdp) +
						fail/2 * gridVal(valuesPrv, Action.UP, x, y, mdp);
				break;
			case DOWN:
				value = success * gridVal(valuesPrv, Action.DOWN, x, y, mdp) +
						fail/2 * gridVal(valuesPrv, Action.RIGHT, x, y, mdp) +
						fail/2 * gridVal(valuesPrv, Action.LEFT, x, y, mdp);
				break;
			case RIGHT:
				value = success * gridVal(valuesPrv, Action.RIGHT, x, y, mdp) +
						fail/2 * gridVal(valuesPrv, Action.UP, x, y, mdp) +
						fail/2 * gridVal(valuesPrv, Action.DOWN, x, y, mdp);
				break;
			default:
				throw new Error();
			}
			values[a] = mdp.getReward(Field.EMPTY) + value;
		}
		valuesCur[x][y] = e(values);
		//System.out.println(String.format("%s,%s set to %s", x, y, valuesCur[x][y]));
	}
	
	/**
	 * Evaluates the results of all possible actions.
	 * @param vs
	 * @return
	 */
	private double e(double[] vs) {
		double d = Double.NEGATIVE_INFINITY;
		for(double v : vs) {
			d = v > d ? v : d;
		}
		if(d == Double.MIN_VALUE) {
			throw new Error();
		}
		return d;
	}
	
	/**
	 * Returns the grid value based on the given action and start state.
	 * @param values
	 * @param action
	 * @param x
	 * @param y
	 * @return
	 */
	private static double gridVal(double[][] values, Action action, int x, int y, MarkovDecisionProblem mdp) {
		int xn;
		int yn;
		switch(action) {
			case UP:
				xn = x;
				yn = y+1;
				break;
			case DOWN:
				xn = x;
				yn = y-1;
				break;
			case LEFT:
				xn = x-1;
				yn = y;
				break;
			case RIGHT:
				xn = x+1;
				yn = y;
				break;
			default:
				throw new Error();
			}
		if(mdp.getField(xn, yn) == Field.OUTOFBOUNDS || mdp.getField(xn, yn) == Field.OBSTACLE) {
			return values[x][y];
		} else {
			return values[xn][yn];
		}
	}
	
	public static void printTable(double[][] table) {
		NumberFormat formatter = new DecimalFormat("#0.000"); 
		StringBuilder sb = new StringBuilder("Table:");
		for(int y = table[0].length-1; y >= 0; y--) {
			sb.append("\n");
			for(int i = 0; i < table.length; i++) {
				sb.append("-----------");
			}
			sb.append("\n");
			for(int x = 0; x < table.length; x++) {
				sb.append(String.format("%s%s | ", table[x][y] >= 0 ? " " : "", formatter.format(table[x][y])));
			}
		}
		System.out.println(sb);
	}

	public static void printActionTable(Action[][] table, boolean pretty) {
		StringBuilder sb = new StringBuilder("Table:");
		Map<Action, Character> prettyMap = new HashMap<>();
		prettyMap.put(Action.UP, '^');
		prettyMap.put(Action.RIGHT, '>');
		prettyMap.put(Action.DOWN, 'v');
		prettyMap.put(Action.LEFT, '<');
		prettyMap.put(null, ' ');
		for(int y = table[0].length-1; y >= 0; y--) {
			sb.append("\n");
			for(int i = 0; i < table.length; i++) {
			}
			sb.append("\n");
			for(int x = 0; x < table.length; x++) {
				Action action = table[x][y];
				sb.append(String.format("%s | ", pretty ? prettyMap.get(action) : action));
			}
		}
		System.out.println(sb);
	}

	/**
	 * Returns a table of the best Action for each square in the grid. When multiple Actions are equally good, only
	 * one will be shown. Fields that are not empty will have null as their Action.
	 * @param table
	 * @param mdp
	 * @return
	 */
	public static Action[][] getActionTable(double[][] table, MarkovDecisionProblem mdp) {
		Action[][] actionTable = new Action[table.length][table[0].length];

		double success = mdp.getActionSuccess();
		double fail = 1 - success;

		for(int x = 0; x < table.length; x++) {
			for(int y = 0; y < table[0].length; y++) {
				Action bestAction = null;
				if(mdp.getField(x, y) == Field.EMPTY) {
					double bestScore = Double.NEGATIVE_INFINITY;

					for (Action action : ACTIONS) {

						double score;

						switch (action) {
							case UP:
								score = success * gridVal(table, Action.UP, x, y, mdp) +
										fail / 2 * gridVal(table, Action.LEFT, x, y, mdp) +
										fail / 2 * gridVal(table, Action.RIGHT, x, y, mdp);
								break;
							case LEFT:
								score = success * gridVal(table, Action.LEFT, x, y, mdp) +
										fail / 2 * gridVal(table, Action.DOWN, x, y, mdp) +
										fail / 2 * gridVal(table, Action.UP, x, y, mdp);
								break;
							case DOWN:
								score = success * gridVal(table, Action.DOWN, x, y, mdp) +
										fail / 2 * gridVal(table, Action.RIGHT, x, y, mdp) +
										fail / 2 * gridVal(table, Action.LEFT, x, y, mdp);
								break;
							case RIGHT:
								score = success * gridVal(table, Action.RIGHT, x, y, mdp) +
										fail / 2 * gridVal(table, Action.UP, x, y, mdp) +
										fail / 2 * gridVal(table, Action.DOWN, x, y, mdp);
								break;
							default:
								throw new Error();
						}

						if (score > bestScore) {
							bestScore = score;
							bestAction = action;
						}
					}
				}
				actionTable[x][y] = bestAction;
			}
		}

		return actionTable;
	}
}

