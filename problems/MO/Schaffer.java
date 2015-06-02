
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
 * The problem: Schaffer multi-objective problem
 * 
 */

package problems.MO;

import jcell.*; //Use jcell package
import java.util.Vector;

public class Schaffer extends Problem
{

    public  final static int    longitCrom 		= 1 ; // Number of variables
    public  final static int    numberOfFuncts	= 2 ;
    
	public Schaffer(String dataFile, EvolutionaryAlg cmoea)
	{
		super(dataFile); 
		
		variables = longitCrom;
		functions = numberOfFuncts;
		
		maxAllowedValues = new Vector(variables);
		maxAllowedValues.add(0,new Double(10^5));
		cmoea.setParam(CellularGA.PARAM_UPPER_LIMIT, maxAllowedValues);
		
		minAllowedValues = new Vector(variables);
		minAllowedValues.add(0,new Double(-10^5));
		cmoea.setParam(CellularGA.PARAM_LOWER_LIMIT, minAllowedValues);
		
		Target.maximize = false;
		
	}
   // Overwrite eval method from Problem class.
   // Returns the fitness of Individual ind
   public Object eval(Individual ind)
   {
   	  //RealIndividualCMoEA bi = (RealIndividualCMoEA)ind;
   	RealIndividual bi = (RealIndividual)ind;
      
      Double [] fitness = new Double[2];
      
      //fitness[0] = fitness[1] = new Double(0.0);

      // First function
      double result;
      
      fitness[0] =  new Double(bi.getRealAllele(0) * bi.getRealAllele(0));
  	  
  	  // Second function	
  	  
  	  fitness[1] =  new Double((bi.getRealAllele(0) - 2) * (bi.getRealAllele(0) - 2));
  	  
      return fitness;
   }

   public int computeNumberOfViolatedConstraints(Individual indiv)
   {
		// No restrictions
		return 0;
   }
}