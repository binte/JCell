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
 * The problem: The P-Peaks problem
 * 
 */

package problems.Combinatorial;


import jcell.*; //Use jcell package
//import java.lang.Math;
import java.util.Random;



public class PEAK extends Problem
{

	public static final int longitCrom = 100; // Length of chromosomes
	public static final double maxFitness = 1.0; // Maximum Fitness Value
	
	
	int problemLength = longitCrom;
	int numberOfPeaks = 100;

	static Individual target[];

	private Random r;

	public PEAK()
	{
		super();
		r = null;
		super.maxFitness = maxFitness;
	}
	
	public PEAK(Random r)
	{
		super();
		variables = longitCrom;
		super.maxFitness = maxFitness;
		
		this.r = r;

		target = new BinaryIndividual[numberOfPeaks];

		for (int x = 0; x<numberOfPeaks; x++)
			for (int y = 0; y<problemLength; y++)
			{
				target[x] = new BinaryIndividual(problemLength);
				target[x].setRandomValues(r);
			}
		
		Target.maximize = true;
	}

   // Overwrite eval method from Problem class.
   // Returns the fitness of Individual ind
   public Object eval(Individual ind)
   {
       BinaryIndividual bi = (BinaryIndividual)ind;
       int partialFitness = 0;
       int distance = 0;
       double fitness = 0.0;

	   for (int i=0; i<100; i++){

	   distance = 0;

	   for (int j=0; j<100; j++){
	       if (!((BinaryIndividual)target[i]).getBooleanAllele(j)^bi.getBooleanAllele(j)) { 
		   distance++;
	       }
     
	   if (distance > partialFitness) {
	       partialFitness = distance;
	   }
	   }
       }

       fitness = (double)partialFitness/100.0;
       return new Double(fitness);
       

   }
}

