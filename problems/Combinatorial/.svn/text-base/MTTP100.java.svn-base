
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
 * The problem: Minimum tardy task problem. Instance of 100 tasks
 * 
 */

package problems.Combinatorial;


import jcell.*; //Use jcell package

public class MTTP100 extends Problem

//max Fitness 0.005 
{
	public static final int longitCrom = 100; // Length of chromosomes
	public static final double maxFitness = 0.005; // Maximum Fitness Value

	
    static int[] lengths = {3, 6, 9, 12, 15, 3, 6, 9, 12, 15, 3, 6, 9, 12, 15, 3, 6, 9, 12, 15, 3, 6, 9, 12, 15, 3, 6, 9, 12, 15, 3, 6, 9, 12, 15, 3, 6, 9, 12, 15, 3, 6, 9, 12, 15, 3, 6, 9, 12, 15, 3, 6, 9, 12, 15, 3, 6, 9, 12, 15, 3, 6, 9, 12, 15, 3, 6, 9, 12, 15, 3, 6, 9, 12, 15, 3, 6, 9, 12, 15, 3, 6, 9, 12, 15, 3, 6, 9, 12, 15, 3, 6, 9, 12, 15, 3, 6, 9, 12, 15};
    static int[] deadlines = {5, 10, 15, 20, 25, 29, 34, 39, 44, 49, 53, 58, 63, 68, 73, 77, 82, 87, 92, 97, 101, 106, 111, 116, 121, 125, 130, 135, 140, 145, 149, 154, 159, 164, 169, 173, 178, 183, 188, 193, 197, 202, 207, 212, 217, 221, 226, 231, 236, 241, 245, 250, 255, 260, 265, 269, 274, 279, 284, 289, 293, 298, 303, 308, 313, 317, 322, 327, 332, 337, 341, 346, 351, 356, 361, 365, 370, 375, 380, 385, 389, 394, 399, 404, 409, 413, 418, 423, 428, 433, 437, 442, 447, 452, 457, 461, 466, 471, 476, 481};
    static double[] weights = {60.0, 40.0, 7.0, 3.0, 50.0, 120.0, 80.0, 7.0, 3.0, 100.0, 180.0, 120.0, 7.0, 3.0, 150.0, 240.0, 160.0, 7.0, 3.0, 200.0, 300.0, 200.0, 7.0, 3.0, 250.0, 360.0, 240.0, 7.0, 3.0, 300.0, 420.0, 280.0, 7.0, 3.0, 350.0, 480.0, 320.0, 7.0, 3.0, 400.0, 540.0, 360.0, 7.0, 3.0, 450.0, 600.0, 400.0, 7.0, 3.0, 500.0, 660.0, 440.0, 7.0, 3.0, 550.0, 720.0, 480.0, 7.0, 3.0, 600.0, 780.0, 520.0, 7.0, 3.0, 650.0, 840.0, 560.0, 7.0, 3.0, 700.0, 900.0, 600.0, 7.0, 3.0, 750.0, 960.0, 640.0, 7.0, 3.0, 800.0, 1020.0, 680.0, 7.0, 3.0, 850.0, 1080.0, 720.0, 7.0, 3.0, 900.0, 1140.0, 760.0, 7.0, 3.0, 950.0, 1200.0, 800.0, 7.0, 3.0, 1000.0};

    int tasks = 100;


    public MTTP100()
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
	double first = 0.0;
	double second = 0.0;


	for (int i = 0; i < tasks; i++) {

	    if (bi.getBooleanAllele(i)) {
		if (time+lengths[i] > deadlines[i]) {      
		    feasible = false;
		    penalty += weights[i];
		} else {
		    time += lengths[i];
		}
	    }

	    if (!bi.getBooleanAllele(i)) {
		first += weights[i];
	    } 

	    second += weights[i];

	}

	if (feasible) {
	    fitness = 1.0 / (first + penalty);
	} else {
	    fitness = 1.0 / (first + second + penalty);
	}

	return new Double(fitness);
    }
}
