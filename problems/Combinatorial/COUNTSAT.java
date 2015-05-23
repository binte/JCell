/**
 * @author Mario Giacobini
 *
 * The problem must provide the following parameters:
 * 	  - variables: the number of variables of the problem
 *    - maxFitness: the optimum (or best known solution) of the problem
 *    - Target.maximize: set whether it is a maximization or minimization problem
 *    - minAllowedValues: Minimum allowed value for each gene  
 *    - maxAllowedValues: Maximum allowed value for each gene
 *
 * The problem: COUNTSAT problem
 * 
 */

package problems.Combinatorial;

import jcell.*; //Use jcell package



public class COUNTSAT extends Problem
{
	public static final int longitCrom = 20; // Length of chromosomes
	public static final double maxFitness = 6860.0; // Maximum Fitness Value
	

    public COUNTSAT()
    {
    	super();
		variables = longitCrom;
		super.maxFitness = maxFitness;
    	Target.maximize = true;
    }

   // Overwrite eval method from Problem class.
   // Returns the fitness of Individual ind
   public Object eval(Individual ind)
   {
      BinaryIndividual bi = (BinaryIndividual)ind;
      int totalOnes = 0;
      double fitness = 0.0;

      for (int i=0; i<variables; i++)
	  if (bi.getBooleanAllele(i))
		  totalOnes++;
     
      fitness = totalOnes + variables*(variables-1)*(variables-2) - (variables-2)*totalOnes*(totalOnes-1) + totalOnes*(totalOnes-1)*(totalOnes-2);

      return new Double(fitness);
   }
}
