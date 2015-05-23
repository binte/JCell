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
 * The problem: Fonseca multi-objective problem
 * 
 */

package problems.MO;

import jcell.*; //Use jcell package
import java.util.Vector;

public class Fonseca extends Problem
{

    public  final static int    longitCrom 		= 3; // Number of variables of the Fonseca function
    public  final static int    numberOfFuncts	= 2; // Number of functions of the Fonseca function
    

	public Fonseca(CellularGA cmoea)
	{
		super();
		
		variables = longitCrom; // Number of variables of the Fonseca function
	    functions = numberOfFuncts; // Number of functions of the Fonseca function
	    
		maxAllowedValues = new Vector(variables);
		maxAllowedValues.add(0,new Double(4.0));
		maxAllowedValues.add(1,new Double(4.0));
		maxAllowedValues.add(2,new Double(4.0));
		cmoea.setParam(CellularGA.PARAM_UPPER_LIMIT, maxAllowedValues);
		minAllowedValues = new Vector(variables);
		minAllowedValues.add(0,new Double(-4.0));
		minAllowedValues.add(1,new Double(-4.0));
		minAllowedValues.add(2,new Double(-4.0));
		cmoea.setParam(CellularGA.PARAM_LOWER_LIMIT, minAllowedValues);
		
		Target.maximize = false; // minimize the two functions
	}
   // Overwrite eval method from Problem class.
   // Returns the fitness of Individual ind
   public Object eval(Individual ind)
   {
      RealIndividual bi = (RealIndividual)ind;
      
      Double [] fitness = new Double[functions];
      
      // First function
      double result = 0.0;
      
      for (int i = 0; i < variables; i++) 
      	result += Math.pow(bi.getRealAllele(i) - (1.0 / Math.sqrt((double)variables)), 2);
  	  
  	  result = 1 - Math.exp(-result);
  	  fitness[0] = new Double(result);
  	  
  	  // Second function	
  	  result = 0.0;
  	  for (int i = 0; i < variables ; i++) 
		result += Math.pow(bi.getRealAllele(i) + (1.0 / Math.sqrt((double)variables)), 2);
	
	  result = 1 - Math.exp(-result);
	  fitness[1] = new Double(result);
	  
      return fitness;
   }

   public int computeNumberOfViolatedConstraints(Individual indiv)
   {
		// No constraints
		return 0;
   }
}