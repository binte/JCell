
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
 * The problem: Maassively multimodal deceptive problem
 * 
 */

package problems.Combinatorial;

import jcell.*; //Use jcell package

public class MMDP extends Problem
{
	public static final int longitCrom = 240; // Length of chromosomes
	public static final double maxFitness = 40.0; // Maximum Fitness Value

    int subproblemsLength = 6;
    int subproblemsNumber = 40;

    public MMDP(String dataFile)
    {
    	super(dataFile);
    	
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
      double partialFitness = 0.0;
      double fitness = 0.0;

      for (int i=0; i<subproblemsNumber; i++){
	  totalOnes = 0;
	  for (int j=0; j<subproblemsLength; j++)
	      if (bi.getBooleanAllele(i*subproblemsLength+j))
		  totalOnes++;

	  switch (totalOnes){
	  case 0: case 6: partialFitness = 1.0;
	      break;
	  case 1: case 5: partialFitness = 0.0;
	      break;
	  case 2: case 4: partialFitness = 0.360384;
	      break;
	  case 3: partialFitness = 0.640576;
	      break;
	  }

	  fitness = fitness+partialFitness;

      }


      return new Double(fitness);
   }
}
