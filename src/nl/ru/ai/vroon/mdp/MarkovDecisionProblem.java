package nl.ru.ai.vroon.mdp;
import javax.swing.*;
import java.util.Random;

/**
 * Basic class that contains and displays a Markov Decision Problem with grid positions in a landscape as states. 
 * Most stuff can be set manually to create the MDP desired.
 * 
 * Also contains and updates an agent that can roam around in the MDP.
 * @author Jered Vroon
 *
 */
public class MarkovDecisionProblem {
	/////////////////////////////////////////////////////////
	/// FIELDS
	/////////////////////////////////////////////////////////
	
	// The collection of grid positions that can be visited:
	private Field[][] landscape;
	private int width = 4,
				height = 3;
	
	// The current position of the agent
	private int xPosition = 0,
				yPosition = 0;
	
	// The positions of the agent in state 0:
	private int initXPos = 0,
				initYPos = 0;
	
	
	// Boolean determining if Actions are performed deterministically or not
	private boolean deterministic = false;

	// Random number generator for doing the Actions stochastically:
	private static Random rand = new Random();
	// ... and the probabilities for each (mis)interpretation of each Action:
	private double 	pPerform = 0.8, 	// probability of action being executed as planned 
					pSidestep = 0.2,	// probability of a sidestep being executed
					pBackstep = 0,		// probability of the inverse action being executed
					pNoStep = 0;		// probability of no action being executed 
						// These four probabilities should add up to 1
	
	// The rewards given for each state:
	private double 	posReward = 1,		// reward for positive end state
					negReward = -1,		// reward for negative end state
					noReward = -0.04;	// reward for the other states
	
	// Boolean maintaining if an end state has been reached:
	private boolean terminated = false;
	
	// The DrawFrame this MDP uses to draw itself
	private DrawFrame frame = null;
	// The time that is waited between drawing each action performed:
	private int waittime = 500;
	private boolean showProgress = true;
	
	// Counts the number of actions that has been performed
	private int actionsCounter = 0;
	
	/////////////////////////////////////////////////////////
	/// FUNCTIONS
	/////////////////////////////////////////////////////////
	
	/**
	 * Constructor.
	 * Constructs a basic MDP
	 * (the one described in Chapter 17 of Russell & Norvig)
	 */
	public MarkovDecisionProblem(){
		defaultSettings();
		
		width = 4;
		height = 3;
		
		// Make and fill the fields:
		landscape = new Field[width][height];
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				landscape[j][i] = Field.EMPTY;
		setField(1,1,Field.OBSTACLE);
		setField(3,1,Field.NEGREWARD);
		setField(3,2,Field.REWARD);
		
		// Draw yourself:
		pDrawMDP();
	}
	
	/**
	 * Constructs a basic MDP with the given width and height.
	 * All fields are set to Field.EMPTY.
	 * All other settings are the same as in the MDP described in Chapter 17 of Russell & Norvig
	 * @param width
	 * @param height
	 */
	public MarkovDecisionProblem(int width, int height) {
		defaultSettings();
		
		this.width = width;
		this.height = height;
		
		// Make and fill the fields:
		landscape = new Field[this.width][this.height];
		for (int i = 0; i < this.height; i++)
			for (int j = 0; j < this.width; j++)
				landscape[j][i] = Field.EMPTY;
		pDrawMDP();
	}
	
	/**
	 * Sets most parameters (except for the landscape, its width and height) to their default value
	 */
	public void defaultSettings(){
		xPosition = 0;
		yPosition = 0;
		
		initXPos = 0;
		initYPos = 0;
		
		deterministic = false;

		pPerform = 0.8;
		pSidestep = 0.2;
		pBackstep = 0;
		pNoStep = 0;

		posReward = 1;
		negReward = -1;	
		noReward = -0.04;

		terminated = false;
		
		waittime = 0;
		showProgress = true;
		
		actionsCounter = 0;
	}
	
	public double getActionSuccess() {
		return pPerform;
	}
	
	/**
	 * Performs the given action and returns the reward that action yielded.
	 * 
	 * However, keep in mind that, if this MDP is non-deterministic, the given action
	 * need not be executed - another action could be executed as well.
	 * @param action, the Action that is _intended_ to be executed
	 * @return the reward the agent gains at its new state
	 */
	public double performAction(Action action){
		// If we are working deterministic, the action is performed
		if (deterministic)
			doAction(action);
		else {
			double prob = rand.nextDouble();
			if (prob < pPerform)
				doAction(action);
			else if (prob < pPerform + pSidestep /2)
				doAction(Action.previousAction(action));
			else if (prob < pPerform + pSidestep)
				doAction(Action.nextAction(action));
			else if (prob < pPerform + pSidestep + pBackstep)
				doAction(Action.backAction(action));
			// else: do nothing (i.e. stay where you are)
		}
		actionsCounter++;
		pDrawMDP();
		return getReward();
	}
	
	/**
	 * Executes the given action as is
	 * (i.e. translates Action to an actual function being performed)
	 * @param action
	 */
	private void doAction(Action action) {
		switch (action){
		case UP:
			moveUp();
			break;
		case DOWN:
			moveDown();
			break;
		case LEFT:
			moveLeft();
			break;
		case RIGHT:
			moveRight();
			break;
		}
	}

	/**
	 * Moves the agent up (if possible).
	 */
	private void moveUp(){
		if (yPosition < (height-1) && landscape[xPosition][yPosition+1] != Field.OBSTACLE)
			yPosition++;
	}
	
	/**
	 * Moves the agent down (if possible).
	 */
	private void moveDown(){
		if (yPosition > 0 && landscape[xPosition][yPosition-1] != Field.OBSTACLE)
			yPosition--;
	}
	
	/**
	 * Moves the agent left (if possible).
	 */
	private void moveLeft(){
		if (xPosition > 0 && landscape[xPosition-1][yPosition] != Field.OBSTACLE)
			xPosition--;
	}
	
	/**
	 * Moves the agent right (if possible).
	 */
	private void moveRight(){
		if (xPosition < (width-1) && landscape[xPosition+1][yPosition] != Field.OBSTACLE)
			xPosition++;
	}
	
	/**
	 * sets the agent back to its default state and sets terminated to false.
	 */
	public void restart(){
		terminated = false;
		xPosition = initXPos;
		yPosition = initYPos;
		actionsCounter = 0;
		pDrawMDP();
	}
	
	/**
	 * Returns the reward the field in which the agent currently is yields
	 * @return a double (can be negative)
	 */
	public double getReward(){
		// If we are terminated, no rewards can be gained anymore (i.e. every action is futile):
		if (terminated) return 0;
		
		switch(landscape[xPosition][yPosition]){
		case EMPTY:
			return noReward;
		case REWARD:
			terminated = true;
			return posReward;
		case NEGREWARD:
			terminated = true;
			return negReward;
		}
		
		// If something went wrong:
		System.err.println("ERROR: MDP: getReward(): agent is not in an empty, reward or negreward field...");
		return 0;
	}
	
	public double getReward(Field field) {
		switch(field) {
		case REWARD:
			return posReward;
		case NEGREWARD:
			return negReward;
		case EMPTY:
			return noReward;
		}
		return 5463456; // this has meaning
	}
	
	/////////////////////////////////////////////////////////
	/// SETTERS
	/////////////////////////////////////////////////////////
	
	
	/**
	 * Sets the field with the given x and y coordinate to the given field.
	 * Updates the visual display.
	 * @param xpos
	 * @param ypos
	 * @param field
	 */
	public void setField(int xpos, int ypos, Field field){
		if (xpos >= 0 && xpos < width && ypos >= 0 && ypos < height)
			landscape[xpos][ypos] = field;
		pDrawMDP();
	}
	
	/**
	 * Moves the agent to the given state (x and y coordinate)
	 * @param xpos
	 * @param ypos
	 */
	public void setState(int xpos, int ypos){
		xPosition = xpos;
		yPosition = ypos;
		pDrawMDP();
	}
	
	/**
	 * sets the default state for the agent (used in restart() )
	 * @param xpos
	 * @param ypos
	 */
	public void setInitialState(int xpos, int ypos){
		initXPos = xpos;
		initYPos = ypos;
	}
	
	/**
	 * makes this MDP deterministic
	 * (i.e. actions have certain outcomes)
	 */
	public void setDeterministic() {
		deterministic = true;
	}
	
	/**
	 * makes this MDP stochastic
	 * (i.e. actions do not have certain outcomes)
	 */
	public void setStochastic() {
		deterministic = false;
	}

	/**
	 * Setter to set the probabilities for all (mis)interpretations of a to-be-performed action.
	 * The given probabilities should add up to 1.
	 * @param pPerform, the probability an action is performed as is (e.g. UP is executed as UP)
	 * @param pSidestep, the probability a sidestep is performed (e.g. UP is executed as LEFT or RIGHT)
	 * @param pBackstep, the probability a backstep is performed (e.g. UP is executed as DOWN)
	 * @param pNoStep, the probability an action is not performed at all (e.g. UP is not executed)
	 */
	public void setProbsStep(double pPerform, double pSidestep, double pBackstep, double pNoStep) {
		double total = pPerform + pSidestep + pBackstep + pNoStep;
		if (total == 1.0)
			System.err.println("ERROR: MDP: setProbsStep: the given probabilities do not add up to 1. I will normalize to compensate.");
		this.pPerform = pPerform / total;
		this.pSidestep = pSidestep / total;
		this.pBackstep = pBackstep / total;
		this.pNoStep = pNoStep / total;
	}

	/**
	 * Setter to set the reward given when a Field.REWARD is reached
	 * @param posReward
	 */
	public void setPosReward(double posReward) {
		this.posReward = posReward;
	}

	/**
	 * Setter to set the reward given when a Field.NEGREWARD is reached
	 */
	public void setNegReward(double negReward) {
		this.negReward = negReward;
	}

	/**
	 * Setter to set the reward given when a Field.EMPTY is reached
	 */
	public void setNoReward(double noReward) {
		this.noReward = noReward;
	}
	
	/////////////////////////////////////////////////////////
	/// GETTERS
	/////////////////////////////////////////////////////////
	
	/**
	 * Returns the x-position of the current state
	 * @return a number between 0 and width
	 */
	public int getStateXPosition(){
		return xPosition;
	}

	/**
	 * Returns the y-position of the current state
	 * @return a number between 1 and height
	 */
	public int getStateYPostion(){
		return yPosition;
	}
	
	/**
	 * Returns if the MDP has been terminated (i.e. a final state has been reached)
	 * @return
	 */
	public boolean isTerminated(){
		return terminated;
	}

	/**
	 * Returns the width of the landscape
	 * @return
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of the landscape
	 * @return
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Returns if this MDP is determinstic
	 * @return
	 */
	public boolean isDeterministic(){
		return deterministic;
	}
	
	/**
	 * Returns the number of actions that has been performed since the last (re)start.
	 * @return
	 */
	public int getActionsCounter(){
		return actionsCounter;
	}

	/**
	 * Returns the field with the given x and y coordinates
	 * @param xpos, should fall within the landscape
	 * @param ypos, should fall within the landscape
	 * @return
	 */
	public Field getField(int xpos, int ypos) {
		if (xpos >= 0 && xpos < width && ypos >= 0 && ypos < height)
			return landscape[xpos][ypos];
		else{
			//System.err.println("ERROR:MDP:getField:you request a field that does not exist!"); <- this is dumb
			return Field.OUTOFBOUNDS;
		}
	}
	
	/////////////////////////////////////////////////////////
	/// DISPLAY STUFF
	/////////////////////////////////////////////////////////
	
	/**
	 * Private method used to have this MDP draw itself only if it should show its progress.
	 */
	private void pDrawMDP(){
		if (showProgress)
			drawMDP();
	}
	
	/**
	 * Draws this MDP. If showProgress is set to true called by MDP every time something changes.
	 * In that case also waits the waittime.
	 */
	public void drawMDP() {
		// (1) sleep
		if (showProgress ){
			Thread.currentThread();
			try {
				Thread.sleep(waittime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// (2) repaint
		if ( frame == null ) {
			frame = new DrawFrame(this);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		} else { 
			frame.drawContent();
			frame.repaint();
		}
	}
	
	/**
	 * Setter to set the speed with which the display is updated at maximum
	 * @param waittime in ms
	 */
	public void setWaittime(int waittime){
		if (waittime >= 0){
			this.waittime = waittime;
		}
		else
			System.err.println("ERROR:MDP:setWaittime: no negative waittime alowed.");
	}
	
	/**
	 * Setter to enable/disable the showing of the progress on the display
	 * @param show
	 */
	public void setShowProgress(boolean show){
		showProgress = show;
	}
	
}
