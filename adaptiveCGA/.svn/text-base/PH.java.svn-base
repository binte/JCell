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
 *  is based on the phenotipic entropy
 *  
 */

package adaptiveCGA;

import jcell.*;

public class PH extends AdaptivePop {

	public PH(CellularGA cea)
	{
		super(cea);
	}
	
	// Evaluates whether or not changing the shape of the grid, and
	// makes that change (if necessary)
	public double evalChange()
	{

		double currentPhenotipicEntropy = ((Double)stats.getStat(ComplexStats.ENTROPY_PHEN)).doubleValue();
		double ratio = -1.0;
		
		// the first time that evalChange is called the criterion cannot be evaluated
		
		if (lastValue < 0.0)
		{
			lastValue = currentPhenotipicEntropy;
			return -1.0;
		}
		
		// the second time that evalChange is called the criterion cannot be evaluated
		if (lastIncrement < 0.0) 
		{
			lastIncrement = Math.abs(currentPhenotipicEntropy - lastValue);
			lastValue = currentPhenotipicEntropy;
			return -1.0;
		}
				
 		double increment = Math.abs(currentPhenotipicEntropy - lastValue);
 		
 		
		if (increment < ((1+epsilon) * lastIncrement)) 			// exploit
		{
			Shape sh = lsh.moveSq();
			ratio = sh.getRatio();
			changeGridShape(sh);
		}
		else if (increment > ((2-epsilon) * lastIncrement)) 	// explore
		{
			Shape sh = lsh.moveRect();
			ratio = sh.getRatio();
			changeGridShape(sh);
		}
		
		// Update values
		lastIncrement = increment;
		lastValue = currentPhenotipicEntropy; 
		
		return ratio;
	}

}
