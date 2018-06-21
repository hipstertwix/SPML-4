package nl.ru.ai.vroon.mdp;

import nl.ru.ai.MartijnArnoldussenKoenNaarding.q_learning.QueueLearning;

/**
 * This main is for testing purposes (and to show you how to use the MDP class).
 * 
 * @author Jered Vroon
 *
 */
public class Main {

	/**
	 * @param args, not used
	 */
	public static void main(String[] args) {
		MarkovDecisionProblem mdp = new MarkovDecisionProblem();
		mdp.setInitialState(0, 0);
		mdp.restart();

		QueueLearning kju = new QueueLearning(mdp, /*12345*/ 55, false, .9, .009,1);



		for(int i = 0; i < 11200; i++) {
			if(i%2000==0) {
				System.out.println(i);
			}
			kju.step(i%10==0);
		}

		kju.printActions();

		kju.printGridValues();
//		Map<Tuple<Coord, Action>, Integer> map = kju.getCountMap();
//		for(Tuple<Coord, Action> key : map.keySet()) {
//            System.out.println(String.format("%s <- map[%s]", map.get(key), key));
//        }

//
//		while(!mdp.isTerminated()) {
//
//			mdp.performAction(actions[mdp.getStateXPosition()][mdp.getStateYPostion()]);
//		}
//
//		MarkovDecisionProblem mdp2 = new MarkovDecisionProblem(10, 10);
//
//		vi = new ValueIteration(mdp2);
//
//		mdp2.setField(5, 5, Field.REWARD);
//
//		values = vi.calculateValues(1000);
//		ValueIteration.printActionTable(actions, true);
//
//		actions = ValueIteration.getActionTable(values, mdp2);
//
//		ValueIteration.printActionTable(actions, true);
//		ValueIteration.printTable(values);
//
//		while(!mdp2.isTerminated()) {
//
//			mdp2.performAction(actions[mdp2.getStateXPosition()][mdp2.getStateYPostion()]);
//		}
	}
}
