/**
 * @author Bernabe Dorronsoro
 *
 * The problem must provide the following parameters:
 * 	  - variables: the number of variables of the problem
 *    - maxFitness: the optimum (or best known solution) of the problem
 *    - Target.maximize: set whether it is a maximization or minimization problem
 *    - minAllowedValues: Minimum allowed value for each gene  
 *    - maxAllowedValues: Maximum allowed value for each gene
 *
 * The problem: Simple test problem. Sums the values of the genes
 * 
 */

package problems;

import java.util.Vector;

import jcell.*;

public class Sum extends Problem
{
	private double maxValue;
	
	public Sum() {
		
		Target.maximize = true;
	    variables = 25; // length of the individual's chromosome
	    maxValue = Integer.MAX_VALUE; // Maximum fitness value
	    super.maxFitness = Integer.MAX_VALUE;

	    minAllowedValues = null; // Maximum and minimum allowed values for each gene
	    maxAllowedValues = null;
	    
	}
	
   // Overwrite eval method from Problem class.
   // Returns the fitness of Individual ind
   public Object eval(Individual ind)
   {
   		double sum = 0.0;
   		for (int i=0; i<ind.getLength(); i++)
   			sum = sum+((Integer)ind.getAllele(i)).intValue();
   		return new Double(sum/ind.getLength());
   }
}
