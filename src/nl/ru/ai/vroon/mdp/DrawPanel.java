package nl.ru.ai.vroon.mdp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * Creates visual content in accordance with the given MDP
 * @author Sjoerd Lagarde + some adaptations by Jered Vroon
 *
 */
public class DrawPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private int screenWidth;
	private int screenHeight;
	private MarkovDecisionProblem mdp;
	
	/**
	 * Constructor
	 * @param mdp
	 * @param screenWidth
	 * @param screenHeight
	 */
	public DrawPanel(MarkovDecisionProblem mdp, int screenWidth, int screenHeight) {
		this.mdp = mdp;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		setBackground(new Color(255, 255, 255)); 	// White background
		super.paintComponent(g);
		
		int stepSizeX = screenWidth/mdp.getWidth();
		int stepSizeY = screenHeight/mdp.getHeight();
		
		Graphics2D g2 = (Graphics2D)g;
		for ( int i=0; i<mdp.getWidth(); i++ ) {
			for ( int j=0; j<mdp.getHeight(); j++ ) {				
				Field f = mdp.getField(i, j);

				g2.setPaint(Color.WHITE);
				if ( f.equals(Field.REWARD) ) {
					g2.setPaint(Color.GREEN);
				} else if ( f.equals(Field.NEGREWARD) ) {
					g2.setPaint(Color.RED);
				} else if ( f.equals(Field.OBSTACLE) ) {
					g2.setPaint(Color.GRAY);
				} 
				g2.fillRect(stepSizeX*i, screenHeight - stepSizeY*(j+1), stepSizeX,stepSizeY);
				
				if ( mdp.getStateXPosition() == i && mdp.getStateYPostion() == j ) {
					g2.setPaint(Color.BLUE);
					g2.fillOval(stepSizeX*i+stepSizeX/4, screenHeight - stepSizeY*(j+1)+stepSizeY/4, stepSizeX/2, stepSizeY/2);
				}
				
				g2.setPaint(Color.BLACK);
				g2.drawRect(stepSizeX*i, screenHeight - stepSizeY*(j+1), stepSizeX,stepSizeY);
			}			
		}
		g2.drawString("Reward: \t\t"+mdp.getReward(), 30, screenHeight+25);
		g2.drawString("#Actions: \t\t"+mdp.getActionsCounter(), 30, screenHeight+40);
	}

}
