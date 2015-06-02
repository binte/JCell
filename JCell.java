

/**
 * @author Bernabe Dorronsoro 
 * 
 * Description
 * This file contains the main process for running JCell
 * It requires a configuration file. Some examples are
 * given in cfg directory.
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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Date;

import ExceptionHandlers.*;


public class JCell implements GenerationListener
{
		
    // Default population shape
    static int x = 10;
    static int y = 10;
    
    static int longitCrom      ;
    static int numberOfFuncts  ;
    
    // Default maximum number of function evaluations
    static int evaluationsLimit = 30000;
    
    protected static boolean showDisplay = false;
   
    protected double lastRatio = -1.0;
    protected boolean verbose = true;
    private static EvolutionaryAlg ea;
    
    private static FileWriter fstream;
    private static BufferedWriter out;
    
    
    public static void main (String args[]) throws Exception
    {
    	if (args.length != 4)
        {
           System.out.println("Error. Try java JCell <ConfigFile> <DataFile> <GenerationLimit> <testFlag>");
           System.exit(-1);
        }
    	
		//Random r = new Random(3816L); // seed for the random number generator
    	Random r = new Random(); // seed for the random number generator
		
		long inicio, fin; // starting and ending time
	
		JCell sel = new JCell();
		
		// Read the configuration file
		ReadConf conf = new ReadConf(args[0], args[1], r);
		
		// Create and initialize ea with the parameters of the configuration
		//EvolutionaryAlg ea = conf.getParameters();
		ea = conf.getParameters(args[1], Integer.parseInt(args[2]));

		Problem prob = (Problem)ea.getParam(CellularGA.PARAM_PROBLEM);
		
		// se se tratar de uma execução de teste
		if( Boolean.parseBoolean((args[3])) ) // activar a flag respetiva
			prob.setTesting(Boolean.parseBoolean((args[3])));
		else // se não se tratar de uma execução de teste
    		System.out.println(prob.toString());
		
		Population popAux = (Population) ea.getParam(CellularGA.PARAM_POPULATION);
		popAux.setPopSize(prob.getVariables()*2);
		
		// Set the Individual
		String indiv = conf.getProperties().getProperty("Individual");

		if (indiv == null)
			throw new MissedPropertyException("Individual");
		
		Class c = Class.forName(indiv);
		Individual individual = null; // The individual is initialized here.
		individual = (Individual) c.newInstance();
		individual.setMinMaxAlleleValue(true, prob.getMinAllowedValues());
		individual.setMinMaxAlleleValue(false, prob.getMaxAllowedValues());
		individual.setLength(prob.getVariables());
		individual.setNumberOfFuncts(prob.numberOfObjectives());
	
		popAux.setTopPop(individual, prob.getVariables()); // initialization of the initial population

		
		ea.setParam(CellularGA.PARAM_POPULATION, popAux);
		
		
		longitCrom = prob.getVariables();
		numberOfFuncts = prob.numberOfObjectives();
		
		ea.setParam(CellularGA.PARAM_LISTENER,sel);
		ea.setParam(CellularGA.PARAM_FEEDBACK, new Integer(4));
		
		int displaySteps = ((Integer)ea.getParam(CellularGA.PARAM_DISPLAY_STEPS)).intValue();
	
		showDisplay = (ea.getParam(CellularGA.PARAM_DISPLAY) != null);

		if(((Boolean)ea.getParam(CellularGA.PARAM_VERBOSE)).booleanValue()) {

			// inicializar as variáveis para a escrita em ficheiro
	    	try{
	    		// Create file 
	    		fstream = new FileWriter("cromossomas.txt");
	    		out = new BufferedWriter(fstream);
	    	} 
	    	catch (Exception e) { //Catch exception if any
	    		  
	    		System.err.println("Error: " + e.getMessage());
	    		e.printStackTrace();
	    	}
		}
		

		inicio = (new Date()).getTime();
		
		// generation cycles are performed in the next method
		ea.experiment();
		
		if(((Boolean)ea.getParam(CellularGA.PARAM_VERBOSE)).booleanValue()) {

			try{
	    		fstream.close();
	    		out.close();
	    	} 
	    	catch (Exception e) { //Catch exception if any
	    		  
	    		System.err.println("Error: " + e.getMessage());
	    		e.printStackTrace();
	    	}
		}

		fin = (new Date()).getTime();
				
		// output best solution
	   if (showDisplay && (((Integer)ea.getParam(CellularGA.PARAM_GENERATION_NUMBER)).intValue()%displaySteps==0)) {
			  CGADisplay display1 = (CGADisplay) ea.getParam(CellularGA.PARAM_DISPLAY);
			  CGADisplay display2 = (CGADisplay) ea.getParam(CellularGA.PARAM_DISPLAY2);
			  if (ea.getParam(CellularGA.PARAM_DISPLAY) != null)
				  display1.step();
			  if (ea.getParam(CellularGA.PARAM_DISPLAY2) != null)
				  display2.step();
		   }
	   
	   //last step 
	   /*xfig SNAPSHOT
	   PopGrid tmppop = (PopGrid) cea.getPopulation();
	    for (int i = 0; i < x*y; i++) {
			Individual tmpind = tmppop.getIndividual(i);
			int fitVal = (int) (256*((tmpind.getFitness()-(maxFitness/4))/(3*maxFitness/4)));
			if (fitVal < 0) fitVal=0;
			String hex = Integer.toHexString(fitVal);
			if (fitVal < 16) hex = "0"+hex;
			System.out.println( "0 " + (i+32) + " #" + hex + hex + hex + " " + tmpind.getFitness());
		    }	  		   
	   */
	
		if (prob.numberOfObjectives()>1) // The multiobjective case
		{
			System.out.println("Evaluations: " + ((Problem)ea.getParam(CellularGA.PARAM_PROBLEM)).getNEvals() +" Time: "+(fin-inicio));
			System.out.println("Number of solutions in the Pareto front: " + ((Archive) ea.getParam(CellularGA.PARAM_SOLUTION_FRONT)).getNumStoredSols());
			((Archive) ea.getParam(CellularGA.PARAM_SOLUTION_FRONT)).printFile(prob.getClass().getName(),prob);
		}
		else
		{
			Double best = null;
			int contig = 0;
			if (Target.maximize)
				best = (Double) ((Statistic) ea.getParam(CellularGA.PARAM_STATISTIC)).getStat(SimpleStats.MAX_FIT_VALUE);
			else 
				best = (Double) ((Statistic) ea.getParam(CellularGA.PARAM_STATISTIC)).getStat(SimpleStats.MIN_FIT_VALUE);
			int evals = ((Problem) (ea.getParam(CellularGA.PARAM_PROBLEM))).getNEvals();
		    // Writes: best found solution, number of generations, elapsed time (mseconds)
			
			
			if(prob.getClass().getName().equalsIgnoreCase("problems.Combinatorial.DNAFragmentAssembling"))
			{
				// Get the best Individual
				int pos = ((Integer)((Statistic)ea.getParam(EvolutionaryAlg.PARAM_STATISTIC)).getStat(SimpleStats.MAX_FIT_POS)).intValue();
				Individual bestInd = ((Population) ea.getParam(EvolutionaryAlg.PARAM_POPULATION)).getIndividual(pos);
				// Evaluate it without weights
				problems.Combinatorial.DNAFragmentAssembling dna = (problems.Combinatorial.DNAFragmentAssembling) (ea.getParam(CellularGA.PARAM_PROBLEM));
				contig = dna.evalContigs(bestInd);
				//System.out.println("Number of contig: " + contig);
			}
			// In the case of SAT problem, we should compute the best fitness without weights:
			else if(prob.getClass().getName().equalsIgnoreCase("problems.Combinatorial.SAT"))
			{
				// Get the best Individual
				int pos = ((Integer)((Statistic)ea.getParam(EvolutionaryAlg.PARAM_STATISTIC)).getStat(SimpleStats.MAX_FIT_POS)).intValue();
				Individual bestInd = ((Population) ea.getParam(EvolutionaryAlg.PARAM_POPULATION)).getIndividual(pos);
				// Evaluate it without weights
				problems.Combinatorial.SAT sat = (problems.Combinatorial.SAT) (ea.getParam(CellularGA.PARAM_PROBLEM));
				best = new Double(sat.evalCount(bestInd));
			}
			
			if (((Boolean)ea.getParam(EvolutionaryAlg.PARAM_VERBOSE)).booleanValue())
				System.out.println("Solution: Best  Generations  Evaluations  Time (ms)  Problem");
			if(prob.getClass().getName().equalsIgnoreCase("problems.Combinatorial.DNAFragmentAssembling"))
			{
				System.out.println(best + " " + contig + " " + (Integer) ea.getParam(CellularGA.PARAM_GENERATION_NUMBER)+" "+ evals +" "+(fin-inicio) + " "
						+ ((Problem) ea.getParam(CellularGA.PARAM_PROBLEM)).getClass().getName());
				// Get the best Individual
				int pos = ((Integer)((Statistic)ea.getParam(EvolutionaryAlg.PARAM_STATISTIC)).getStat(SimpleStats.MAX_FIT_POS)).intValue();
				PermutationIndividual bestInd = (PermutationIndividual) ((Population) ea.getParam(EvolutionaryAlg.PARAM_POPULATION)).getIndividual(pos);
				int len = bestInd.getLength();
				for (int i=0; i<len; i++)
				{
					System.out.print(bestInd.getIntegerAllele(i) + " ");
				}
				System.out.println();
			}
			else
				if (!prob.testing()) // se não estiver a ser executada uma instância de teste
					System.out.println(best + " " + (Integer) ea.getParam(CellularGA.PARAM_GENERATION_NUMBER)+" "+ evals +" "+(fin-inicio) + " " 
						+ ((Problem) ea.getParam(CellularGA.PARAM_PROBLEM)).getClass().getName());
			
			
			if( prob.testing() & prob.getClass().getName().equalsIgnoreCase("problems.Combinatorial.TOP"))
				System.out.println(((problems.Combinatorial.TOP )prob).getCollected());
		}
    }
    
    private void writeLine(String line)
	{
		if (verbose) {
			
	    	try{
	    		out.write(line + "\n");
	    		out.flush();
	    	} 
	    	catch (Exception e) { //Catch exception if any
	    		  
	    		System.err.println("Error: " + e.getMessage());
	    		e.printStackTrace();
	    	}
		}
	}
    
    public void generation(EvolutionaryAlg ea)
    {   
    	//CellularGA cea = (CellularGA) ea;
    	verbose = ((Boolean) ea.getParam(CellularGA.PARAM_VERBOSE)).booleanValue();

    	if ((!ea.getParam(EvolutionaryAlg.PARAM_POPULATION).getClass().getName().equalsIgnoreCase("distributedGA")) &&
    			(((Population)ea.getParam(EvolutionaryAlg.PARAM_POPULATION)).getPopSize() != 1))
    	{
			// Get the best Individual
			int pos = ((Integer)((Statistic)ea.getParam(EvolutionaryAlg.PARAM_STATISTIC)).getStat(SimpleStats.MAX_FIT_POS)).intValue();
			Individual bestInd = ((Population) ea.getParam(EvolutionaryAlg.PARAM_POPULATION)).getIndividual(pos);
			Problem prob = (Problem)ea.getParam(EvolutionaryAlg.PARAM_PROBLEM);
			
			if (prob.numberOfObjectives() == 1) {	
				if( prob.getClass().getName().equals("problems.Combinatorial.TOP") )
					writeLine("Generation: " + (Integer) ea.getParam(CellularGA.PARAM_GENERATION_NUMBER) + "; Best individual: " + ((TopIndividual) bestInd).toString() + "\n");
				else
					writeLine("Generation: " + (Integer) ea.getParam(CellularGA.PARAM_GENERATION_NUMBER) + "; Best individual: " + ((BinaryIndividual) bestInd).toString() + "\n");
			}
			else
				writeLine("Generation: " + (Integer) ea.getParam(CellularGA.PARAM_GENERATION_NUMBER));
    	}
/*		Population population = (Population) cea.getParam(CellularGA.PARAM_POPULATION);
		for (int i=0; i<25; i++)
			writeLine(((Double[])population.getIndividual(i).getFitness())[0].doubleValue() + ", " + ((Double[])population.getIndividual(i).getFitness())[1].doubleValue());
		writeLine();*/
		
		// La realimentaciÛn aquÌ
		
		int displaySteps = ((Integer)ea.getParam(CellularGA.PARAM_DISPLAY_STEPS)).intValue();
		if ((showDisplay)&& (((Integer)ea.getParam(CellularGA.PARAM_GENERATION_NUMBER)).intValue()%displaySteps==0)) {
			  CGADisplay display = (CGADisplay) ea.getParam(CellularGA.PARAM_DISPLAY);
			  CGADisplay display2 = (CGADisplay) ea.getParam(CellularGA.PARAM_DISPLAY2);
			  if (display != null)
				  display.step();
			  if (display2 != null)
				  display2.step();
		   }
		   
		   /*xfig SNAPSHOT
		   if (((Integer)cea.getParam(CellularGA.PARAM_GENERATION_NUMBER)).intValue()%displaySteps==0) {
			   PopGrid tmppop = (PopGrid) cea.getPopulation();
			    for (int i = 0; i < x*y; i++) {
					Individual tmpind = tmppop.getIndividual(i);
					int fitVal = (int) (256*((tmpind.getFitness()-(maxFitness/4))/(3*maxFitness/4)));
					if (fitVal < 0) fitVal=0;
					String hex = Integer.toHexString(fitVal);
					if (fitVal < 16) hex = "0"+hex;
					System.out.println( "0 " + (i+32) + " #" + hex + hex + hex + " " + tmpind.getFitness());
				    }	  		   
		   }
		   */
		
		// For the adaptive population case
		int n = ((Integer)ea.getParam(CellularGA.PARAM_GENERATION_NUMBER)).intValue();
		//Problem problem = (Problem) ea.getParam(CellularGA.PARAM_PROBLEM);
		//Population pop = (Population) ea.getParam(CellularGA.PARAM_POPULATION);
		
		// Adaptive population
		if (((ea.getParam(CellularGA.PARAM_POP_ADAPTATION))!=null) && 
		   	 // each delta steps
		   	 ((n%AdaptivePop.delta) == 0))
		{
			double ratio = 0.0;
		   	// change the pop shape (if needed)
		   	if ((ratio = ((AdaptivePop)ea.getParam(CellularGA.PARAM_POP_ADAPTATION)).evalChange()) != -1.0)
		   	{
		   		if (ratio != lastRatio)
		   		{
		   			lastRatio = ratio;
		   			if (showDisplay) 
		   			{
		  			  CGADisplay display = (CGADisplay) ea.getParam(CellularGA.PARAM_DISPLAY);
		  			  CGADisplay display2 = (CGADisplay) ea.getParam(CellularGA.PARAM_DISPLAY2);
		  			  if (display != null)
		  			  {
		  				//display.resize(((PopGrid)pop).getDimX(), ((PopGrid)pop).getDimY());
		  			  	display.resize();
		  			    
		  			  display.step();
		  			  	//display = new CGADisplay(cea, problem.getMaxFitness(),CGADisplay.NO_TEXT + CGADisplay.DISPLAY_VALUE);
		  				//cea.setParam(CellularGA.PARAM_DISPLAY, display);
		  			  }
		  			  if (display2 != null)
		  			  {
		  			  	display2.resize();
		  			    
		  			  display2.step();
		  			  	//display2.resize(((PopGrid)pop).getDimX(), ((PopGrid)pop).getDimY());
		  			  	//display2 = new CGADisplay(cea, problem.numberOfVariables(),CGADisplay.NO_TEXT + CGADisplay.DISPLAY_BESTDISTANCE);
		  				//cea.setParam(CellularGA.PARAM_DISPLAY, display2);
		  			  }
		  		   }
		   			writeLine("New ratio: " + ratio);
		   		}
		   	}
		}
	    // For the adaptive population case
		
		// Adaptive anisotropic selection
		//if (((ea.getParam(CellularGA.PARAM_POP_ADAPTATION))!=null) && 
		//   	 // each delta steps
		//   	 ((n%AdaptivePop.delta) == 0))
		double alpha;
		
		// For the island distributed population
		// Migration occurs here:
		if (ea.getParam(EvolutionaryAlg.PARAM_POPULATION).getClass().getName().equalsIgnoreCase("distributedGA"))
		{
			int evals = ((Problem) (ea.getParam(CellularGA.PARAM_PROBLEM))).getNEvals();
			
			PopIsland pop = (PopIsland)(ea.getParam(CellularGA.PARAM_POPULATION));
			
			//if (evals >= ea.getParam(ea.PARAM_MIGRATION_FREQUENCY))
			Individual ind0, ind1;
			int j;
			int freq = ((Integer)ea.getParam(EvolutionaryAlg.PARAM_MIGRATION_FREQUENCY)).intValue();
			int islands = ((DistributedGA)ea).getPopulation().getNumberIslands();
			int islandSize = ((DistributedGA)ea).getPopulation().getSizeIslands();
			
			if (((double)evals/(double)freq >= 1) && (evals%(freq*islands)==0))
			{
				writeLine("Evaluaciones: " + evals);
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
    	// For the island distributed population
		
		Population pop = (Population)(ea.getParam(CellularGA.PARAM_POPULATION));
    }

    
    public EvolutionaryAlg getEA()
    {
    	return ea;
    }
    
    public void setEA(EvolutionaryAlg ea)
    {
    	this.ea = ea;
    }
}
