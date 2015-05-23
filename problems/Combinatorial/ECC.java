
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
 * The problem: Error correcting codes design problem. It optimizes half of the code and 
 * then the other half is composed by the complementary words
 * 
 */

package problems.Combinatorial;

import jcell.*; //Use jcell package

public class ECC extends Problem {

	public static final int longitCrom = 144; // Length of chromosomes
	public static final double maxFitness = 0.0674; // Maximum Fitness Value

    int individualLength = 12;
    int halfCode = 12;

    public ECC()
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
      double partialFitness = 0.0;
      int hamming = 0;
      double fitness = 0.0;

      for (int i=0; i<halfCode; i++) {
	  partialFitness += 1.0/(individualLength*individualLength);           // distance from its complementary
	  for (int j=0; j<halfCode; j++) {
	      if (j!=i) {
		  hamming = 0;
		  for (int k=0; k<individualLength; k++){
		      if (bi.getBooleanAllele(i*individualLength+k)^bi.getBooleanAllele(j*individualLength+k)) {  
			  hamming++;
		      }
		  }
		  partialFitness += 1.0/(hamming*hamming) + 1.0/((individualLength-hamming)*(individualLength-hamming));
	      }
	  }
      }
      fitness = 1.0/(2*partialFitness);                   
      return new Double(fitness);
   }
}
