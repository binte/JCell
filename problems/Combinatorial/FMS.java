/**
 * @author Mario Giacobini
 * 
 * Modified by Bernabe Dorronsoro
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

package problems.Combinatorial;

import jcell.*; //Use jcell package

import java.lang.Math;



public class FMS extends Problem
{
	public static final int longitCrom = 192; // Length of chromosomes
	public static final double maxFitness = 0.01; // Maximum Fitness Value Error = 10^-2
	
    double theta = (2.0*Math.PI)/100.0;

	double[] target = new double[101];

	public FMS()
	{
		super();
		variables = longitCrom;
		super.maxFitness = maxFitness;
		
		for (int i = 0; i<101; i++)
				target[i] = 1.0*Math.sin((5.0*theta*i)-(1.5*Math.sin((4.8*theta*i)+(2.0*Math.sin(4.9*theta*i)))));
		
	    //Target.maximize = true;
		Target.maximize = false;
	}
   // Overwrite eval method from Problem class.
   // Returns the fitness of Individual ind
   public Object eval(Individual ind)
   {
       BinaryIndividual bi = (BinaryIndividual)ind;
       long a1_int, w1_int, a2_int, w2_int, a3_int, w3_int;   // integer values of the representation
       double a1,w1,a2,w2,a3,w3;   // real parameters of the individual to be evaluated
       double[] individual = new double[101];  // values of the individual in the 101 fitness cases
       double partialFitness = 0.0;
       double fitness = 0.0;
       double distance = 0.0;

       // initialize parameters
       a1_int = 0;
       a2_int = 0;
       w1_int = 0;
       w2_int = 0;
       a3_int = 0;
       w3_int = 0;

       // calculate parameter a1_int
       for (int i=0; i<32; i++){
	   if (bi.getBooleanAllele(i))
	       a1_int++;
	   a1_int<<=1;
       }
       a1_int>>=1;


       // calculate parameter w1_int
       for (int i=32; i<64; i++){
	   if (bi.getBooleanAllele(i))
	       w1_int++;
	   w1_int<<=1;
       }
       w1_int>>=1;


       // calculate parameter a2_int
       for (int i=64; i<96; i++){
	   if (bi.getBooleanAllele(i))
	       a2_int++;
	   a2_int<<=1;
       }
       a2_int>>=1;


       // calculate parameter w2_int
       for (int i=96; i<128; i++){
	   if (bi.getBooleanAllele(i))
	       w2_int++;
	   w2_int<<=1;
       }
       w2_int>>=1;


       // calculate parameter a3_int
       for (int i=128; i<160; i++){
	   if (bi.getBooleanAllele(i))
	       a3_int++;
	   a3_int<<=1;
       }
       a3_int>>=1;


       // calculate parameter w3_int
       for (int i=160; i<192; i++){
	   if (bi.getBooleanAllele(i))
	       w3_int++;
	   w3_int<<=1;
       }
       w3_int>>=1;


       // calculate double a1,w1,a2,w2,a3,w3
       a1 = -6.4+(12.75*(((double)a1_int)/4294967295.0));
       w1 = -6.4+(12.75*(((double)w1_int)/4294967295.0));
       a2 = -6.4+(12.75*(((double)a2_int)/4294967295.0));
       w2 = -6.4+(12.75*(((double)w2_int)/4294967295.0));
       a3 = -6.4+(12.75*(((double)a3_int)/4294967295.0));
       w3 = -6.4+(12.75*(((double)w3_int)/4294967295.0));

	   // filling the vector individual with the values of ind on the 101 fitness cases
       for (int i=0; i<101; i++){
	      individual[i] = a1*Math.sin((w1*theta*i)+(a2*Math.sin((w2*theta*i)+(a3*Math.sin(w3*theta*i)))));	
       }


       // calculate the partial fitness of the individual
	   for (int i=0; i<101; i++){
	      distance = target[i] - individual[i];
	      partialFitness = partialFitness + (distance*distance);
       }
	   fitness = partialFitness;

      return new Double(fitness);
   }
}
