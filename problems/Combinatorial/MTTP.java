/* ------------------------------------------
   File: MTTP.java
   Author: Mario Giacobini
   Description
   It defines a specific subclass of Problem.
   The Maximum Tardy Task Problem
   ------------------------------------------*/

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
 * The problem: Minimum tardy task problem. Instance of 20 tasks
 * 
 */
 
package problems.Combinatorial;


import jcell.*; //Use jcell package

public class MTTP extends Problem
{

	public static final int longitCrom = 20; // Length of chromosomes
	public static final double maxFitness = 0.02439; // Maximum Fitness Value
	
	
    static int[] lengths = {2, 4, 1, 7, 4, 3, 5, 2, 4, 7, 2, 9, 8, 6, 1, 4, 9, 7, 8, 2};
    static int[] deadlines = {300, 500, 600, 800, 1000, 1500, 1600, 2000, 2500, 2900, 3000, 3600, 4900, 5900, 8000, 8100, 8900, 9700, 10000, 10500};
    static double[] weights = {15.000000, 20.000000, 16.000000, 19.000000, 10.000000, 25.000000, 17.000000, 18.000000, 21.000000, 17.000000, 31.000000, 2.000000, 26.000000, 42.000000, 50.000000, 19.000000, 17.000000, 21.000000, 22.000000, 13.000000};

    int tasks = 20;

    public MTTP()
    {
    	super();
		variables = longitCrom;
		super.maxFitness = maxFitness;
    	Target.maximize = true;
    }

    public Object eval(Individual ind)
    {
	BinaryIndividual bi = (BinaryIndividual)ind;
	double fitness = 0.0;
	boolean feasible = true;
	int time = 0;
	double penalty = 0.0;
	double first = 0.0; // soma dos pesos das tarefas com gene = 0
	double second = 0.0; // soma dos pesos de todas as tarefas (tasks)


	for (int i = 0; i < tasks; i++) {

	    if (bi.getBooleanAllele(i)) { // se gene na posição 'i' = 1
	    	
			if (time+lengths[i] > deadlines[i]) { // se o plano for inadmissível
			    feasible = false;
			    penalty += weights[i];
			} else { // se o plano for admissível
			    time += lengths[i]; // atualiza o timing do plano
			}
	    }

	    if (!bi.getBooleanAllele(i)) // se gene na posição 'i' = 0
	    	first += weights[i];

	    second += weights[i];
	}

	if (feasible) { // se o plano tiver sido classificado como admissível		
	    fitness = 1.0 / first;
	} else { // caso contrário
	    fitness = 1.0 / (first + second + penalty);
	}

	return new Double(fitness);
    }
}
