/**
 * @author Bernabe Dorronsoro 
 * January, 22th, 2006
 * 
 * Description
 * Implements an self-adaptive population which is able to
 * change its grid shape during the search in order to improve
 * explotation or exploration as it is necessary
 */


package adaptiveCGA;

import jcell.*;


public abstract class AdaptivePop {
	
	// epsilon is the maximum allowed evolution in the population
	// It is a parameter of the self-adaptive method, and it was tuned to 0.05 
	//	after an intensive tuning process.
	public final static double epsilon = 0.05;
	
	// Delta is the frequency (in number of generations) for evaluating 
	//	a possible change in the population shape
	public final static int delta = 10;
	
	// lsh is a list with all the possible shapes that a population of a given size can have
	public Shapes lsh;
	
	// This are variables used by the self-adaptive method
	protected static double lastValue;
	protected static double lastIncrement;
	protected static double lastValueP;
	protected static double lastIncrementP;
	
	// stats keeps some statistics of the population. It is used for measuring the evolution of the population
	protected ComplexStats stats;
	
	// pop is the population of the cGA
	protected PopGrid pop;
	
	public AdaptivePop(CellularGA cea)
	{
		// Initialize the variable values
		lastValue = -1.0; 
		lastIncrement = -1.0;
		lastValueP = -1.0; 
		lastIncrementP = -1.0;
		this.stats = (ComplexStats) cea.getParam(CellularGA.PARAM_STATISTIC);
		this.pop = (PopGrid) cea.getParam(CellularGA.PARAM_POPULATION);
		this.lsh = new Shapes(pop);
	}
	
	// changes the population shape to the shape specified by 'sh'
	public void changeGridShape(Shape sh)
	{
		pop.setDimension(sh.getX(),sh.getY());
	}
   
	// evaluate whether a change has to be made or not in terms of some measure of the pop. evolution
   public abstract double evalChange();
}
