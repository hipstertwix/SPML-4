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
		
//		ValueIteration vi = new ValueIteration(mdp);
//
//		double[][] values = vi.calculateValues(30);
//		ValueIteration.printTable(values);
		
//		mdp.setNoReward(-1.7);
//		System.out.println("-1.7:\n");
//		values = vi.calculateValues(3000);
//		ValueIteration.printTable(values);
//		ValueIteration.printActionTable(ValueIteration.getActionTable(values, mdp), true);
//		
//		mdp.setNoReward(-0.3);
//		System.out.println("-0.3:\n");
//		values = vi.calculateValues(30);
//		ValueIteration.printTable(values);
//		ValueIteration.printActionTable(ValueIteration.getActionTable(values, mdp), false);
//		
//		mdp.setNoReward(-0.01);
//		System.out.println("-0.01:\n");
//		values = vi.calculateValues(30);
//		ValueIteration.printTable(values);
//		ValueIteration.printActionTable(ValueIteration.getActionTable(values, mdp), true);
//		
//		mdp.setNoReward(0.5);
//		System.out.println("0.5:\n");
//		values = vi.calculateValues(30);
//		ValueIteration.printTable(values);
//		ValueIteration.printActionTable(ValueIteration.getActionTable(values, mdp), false);
		
//		Action[][] actions = ValueIteration.getActionTable(values, mdp);

		QueueLearning kju = new QueueLearning(mdp, 12345, false, 1, 0.1,7);



		for(int i = 0; i < 100; i++) {
			if(i%2000==0) {
				System.out.println(i);
			}
			kju.step(i%10==0);
		}

		kju.printActions();
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
