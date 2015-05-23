/**
 * 
 * @author Bernabe Dorronsoro 
 * January, 22th, 2006
 * 
 * Description
 * Implements a self-adaptive population which is able to
 * change its grid shape during the search in order to improve
 * explotation or exploration as it is necessary. The criterion
 *  followed to decide whether changing the pop shape or not
 *  is based on both the average fitness of the individuals and the 
 *  phenotipic entropy
 *  
 */

package adaptiveCGA;

import jcell.*;

public class AFPH extends AdaptivePop {

	public AFPH(CellularGA cea)
	{
		super(cea);
	}
	
	// Evaluates whether or not changing the shape of the grid, and
	// makes that change (if necessary)
	public double evalChange()
	{

		double currentFitnessAvg = ((Double)stats.getStat(ComplexStats.AVG_FIT)).doubleValue();
		double currentPhenotipicEntropy = ((Double)stats.getStat(ComplexStats.ENTROPY_PHEN)).doubleValue();
		double ratio = -1.0;
		
		// the first time that evalChange is called the criterion cannot be evaluated
		
		if (lastValue < 0.0)
		{
			lastValue = currentFitnessAvg;
			lastValueP = currentPhenotipicEntropy;
			return -1.0;
		}
		
		// the second time that evalChange is called the criterion cannot be evaluated
		if (lastIncrement < 0.0) 
		{
			lastIncrement = Math.abs(currentFitnessAvg - lastValue);
			lastValue = currentFitnessAvg;
			lastIncrementP = Math.abs(currentPhenotipicEntropy - lastValueP);
			lastValueP = currentPhenotipicEntropy;
			return -1.0;
		}
				
		double increment = Math.abs(currentFitnessAvg - lastValue);
 		double incrementP = Math.abs(currentPhenotipicEntropy - lastValueP);
 		if ((increment < ((1+epsilon) * lastIncrement)) && (incrementP < ((1+epsilon) * lastIncrementP))) 		// exploit
		{
			Shape sh = lsh.moveSq();
			ratio = sh.getRatio();
			changeGridShape(sh);
		}
		else if ((increment > ((2-epsilon) * lastIncrement)) && (incrementP > ((2-epsilon) * lastIncrementP))) 	// explore
		{
			Shape sh = lsh.moveRect();
			ratio = sh.getRatio();
			changeGridShape(sh);
		}
		
		// Actualize values
		lastIncrement = increment;
		lastValue = currentFitnessAvg; 
		lastIncrementP = incrementP;
		lastValueP = currentPhenotipicEntropy; 
		
		return ratio;
	}

}
