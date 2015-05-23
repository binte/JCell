
/**
 * @author Sergio Romero
 *
 * The problem must provide the following parameters:
 * 	  - variables: the number of variables of the problem
 *    - maxFitness: the optimum (or best known solution) of the problem
 *    - Target.maximize: set whether it is a maximization or minimization problem
 *    - minAllowedValues: Minimum allowed value for each gene  
 *    - maxAllowedValues: Maximum allowed value for each gene
 *
 * The problem: Onemax problem: Maximizes the number of ones in the chromosome
 * 
 */

package problems.Combinatorial;

import jcell.*; //Use jcell package

public class OneMax extends Problem
{
   // Overwrite eval method from Problem class.
   // Returns the fitness of Individual ind
	
	public static final int longitCrom = 500; // Length of chromosomes
	public static final double maxFitness = 500.0; // Maximum Fitness Value
	
	public OneMax()
	{
    	super();
		variables = longitCrom;
		super.maxFitness = maxFitness;
    	Target.maximize = true;
    }

	public Object eval(Individual ind)
   {
      BinaryIndividual bi = (BinaryIndividual)ind;
      int len    = bi.getLength();
      int totalOnes = 0;
      
      for (int i=0; i<len; i++)
         if (bi.getBooleanAllele(i))
            totalOnes++;
      
      return new Double(totalOnes); //Fitness is a double type value
   }
}
