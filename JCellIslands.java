/**
 * @author Bernabe Dorronsoro 
 * 
 * Description
 * Example of using JCell without config file.
 * Here, the MTTP problem is solved with an island GA
 * 
 * For more information:
 * https://gforge.uni.lu
 * http://neo.lcc.uma.es/Software/JCell/index.htm
 * 
 */


import gui.CGADisplay;
import jcell.*;
import MO.*;
import adaptiveCGA.*;
import java.util.Random;
import java.util.Date;
import operators.mutation.*;
import operators.recombination.*;
import operators.selection.*;
import operators.replacement.*;
import problems.Combinatorial.*;


public class JCellIslands implements GenerationListener
{

    static int islands = 5;
    static int islandSize = 80;
    
    static int longitCrom      ;
    static int numberOfFuncts  ;
    
    // Default maximum number of function evaluations
    static int evaluationsLimit = 1000000;
    
    //private static boolean showDisplay = false;
   
    private boolean verbose = false;
    
    public static void main (String args[]) throws Exception
    {
    	
		Random r = new Random(); // seed for the random number generator
		
		long start, end; // starting and ending time
	
		JCellIslands sel = new JCellIslands();
		
		EvolutionaryAlg ea = new DistributedGA(r);
		
		//Problem prob = new MMDP();
		Problem prob = new MTTP();
		ea.setParam(CellularGA.PARAM_PROBLEM, prob);
		longitCrom = prob.numberOfVariables();
		numberOfFuncts = prob.numberOfObjectives();
		
		Population pop = new PopIsland(islands, islandSize);
		Individual ind = new BinaryIndividual(longitCrom);
		
		pop.setRandomPop(r, ind);
		
		Double mutac = new Double(1.0); // probability of individual mutation
		Double cross = new Double(1.0); // crossover probability
		
		// Set parameters of CEA
		ea.setParam(EvolutionaryAlg.PARAM_MIGRATION_FREQUENCY, new Integer(1000));
	    ea.setParam(CellularGA.PARAM_POPULATION, pop);
	    ea.setParam(CellularGA.PARAM_STATISTIC, new ComplexStats());
	    ea.setParam(CellularGA.PARAM_LISTENER,sel);
	    ea.setParam(CellularGA.PARAM_MUTATION_PROB, mutac);
	    ea.setParam(CellularGA.PARAM_CROSSOVER_PROB, cross);
	    ea.setParam(CellularGA.PARAM_EVALUATION_LIMIT, new Integer(evaluationsLimit));
	    ea.setParam(CellularGA.PARAM_TARGET_FITNESS, (Double) new Double(prob.getMaxFitness()));
		ea.setParam("selection1", new TournamentSelection(r)); // selection of one parent
	    ea.setParam("selection2", new TournamentSelection(r)); // selection of one parent
	    ea.setParam("crossover", new Dpx(r));
	    ea.setParam("mutation", new BinaryMutation(r,ea));
	    ea.setParam("replacement", new ReplaceIfBetter());
			
		start = (new Date()).getTime();
		// generation cycles
		ea.experiment();
		end = (new Date()).getTime();
				
		Double best = (Double) ((Statistic) ea.getParam(CellularGA.PARAM_STATISTIC)).getStat(0);
		int evals = ((Problem) (ea.getParam(CellularGA.PARAM_PROBLEM))).getNEvals();
		// Writes: best found solution, number of generations, elapsed time (mseconds)
		System.out.println("Solution: Best  Generations  Evaluations  Time (ms)  Problem");
		System.out.println(best + " " + (Integer) ea.getParam(CellularGA.PARAM_GENERATION_NUMBER)+" "+ evals +" "+(end-start) + " " 
				+ ((Problem) ea.getParam(CellularGA.PARAM_PROBLEM)));
    }

    private void writeLine(String line)
	{
		if (verbose)
			System.out.println(line);
	}
    
    public void generation(EvolutionaryAlg ea)
    {   
    	//CellularGA cea = (CellularGA) ea;
    	verbose = ((Boolean) ea.getParam(CellularGA.PARAM_VERBOSE)).booleanValue();
    	
    	//writeLine("Generation: " + (Integer) ea.getParam(CellularGA.PARAM_GENERATION_NUMBER));
		
		// Migration occurs here:
    	int evals = ((Problem) (ea.getParam(CellularGA.PARAM_PROBLEM))).getNEvals();
    	
    	PopIsland pop = (PopIsland)(ea.getParam(CellularGA.PARAM_POPULATION));
    	
		//if (evals >= ea.getParam(ea.PARAM_MIGRATION_FREQUENCY))
    	Individual ind0, ind1;
    	int j;
    	int migrFreq = ((Integer)ea.getParam(EvolutionaryAlg.PARAM_MIGRATION_FREQUENCY)).intValue();
    	if (((double)evals/migrFreq >= 1) && (evals%(migrFreq*islands)==0))
		{
    		System.out.println("Evaluaciones: " + evals);
    		for (int i=0; i<islands; i++)
    		{
    			ind0 = pop.getIndividual(i,((DistributedGA)ea).bestInds[i]);
    			if (i==islands-1)
    				j = 0;
    			else
    				j = i+1;
    			
    			ind1 = pop.getIndividual(j,((DistributedGA)ea).worstInds[j]);
    			if (Target.isBetter(ind0, ind1))
				{
					pop.setIndividual(j,((DistributedGA)ea).worstInds[j],ind0);
					
					if (Target.isBetter(ind0, pop.getIndividual(j, ((DistributedGA)ea).bestInds[j])))
						((DistributedGA)ea).bestInds[j] = ((DistributedGA)ea).worstInds[j];
					
					for(int k=0; k<islandSize; k++)
	          		  if (Target.isWorse(pop.getIndividual(j,k), pop.getIndividual(j, ((DistributedGA)ea).worstInds[j])))
		          			((DistributedGA)ea).worstInds[j] = k;
				}
    		}
		}
		   
    }
}
