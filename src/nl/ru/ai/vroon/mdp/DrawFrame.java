package nl.ru.ai.vroon.mdp;

import java.awt.Container;
import javax.swing.JFrame;

/**
 * Draws the given MDP.
 * @author Sjoerd Lagarde + some adaptations by Jered Vroon
 *
 */
public class DrawFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private int width = 750;
	private int height = 800;
	private MarkovDecisionProblem mdp;

	/**
	 * Constructor.
	 * @param mdp
	 */
	public DrawFrame(MarkovDecisionProblem mdp) {
		this.mdp = mdp;
		width = mdp.getWidth() * 50;
		height = mdp.getHeight() * 50;
		setSize(width+20, height+100);
		setTitle("MDP Visualization");
		
		drawContent();
	}
	
	/**
	 * Adds the content to the frame:
	 */
	public void drawContent() {
		DrawPanel panel = new DrawPanel(mdp, width, height);
		Container contentPane = getContentPane();
		contentPane.add(panel);		
	}
	
}
