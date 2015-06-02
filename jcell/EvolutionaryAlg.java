
/**
 * @author Bernabe Dorronsoro
 *
 * Base class for an Evolutionary Algorithm
 * 
 */

package jcell;

import java.util.*;
import jcell.neighborhoods.*;
import java.awt.Point;
import HcGA.*;
import gui.*;
import MO.*;
import adaptiveCGA.*;
import java.util.Random;

public abstract class EvolutionaryAlg
{
   public static final int PARAM_POPULATION        = 0; //Population
   public static final int PARAM_STATISTIC         = 1; //Statistics
   public static final int PARAM_NEIGHBOURHOOD     = 2; //Neighborhood
   public static final int PARAM_CELL_UPDATE       = 3; //Updating policy
   public static final int PARAM_LISTENER          = 4; //Generation listener (used for monitoring the search)
   public static final int PARAM_PROBLEM           = 5; //Problem
   public static final int PARAM_MUTATION_PROB     = 6; //Individual mutation probability (once we decide to mutate an individual we then use other probability to mutate its genes)
   public static final int PARAM_CROSSOVER_PROB    = 7; //Recombination probability
   public static final int PARAM_LOCAL_SEARCH_PROB = 8; //Local search probability
   public static final int PARAM_LOCAL_SEARCH_STEPS= 9; // Maximum number of steps of the Local search
   public static final int PARAM_TARGET_FITNESS    = 10; //objective fitness value
   public static final int PARAM_GENERATION_NUMBER = 11; //Generation number
   public static final int PARAM_SELECTED_CELL     = 12; //Current cell
   public static final int PARAM_GENERATION_LIMIT  = 13; //Generations Limit
   
   public static final int PARAM_ALLELE_MUTATION_PROB = 14; // Gene mutation probability (applied after selecting to mutate a given individual)
   public static final int PARAM_EVALUATION_LIMIT	  = 15; // Max. number of evaluations
   
   public static final int PARAM_ANISOTROPIC_ADAPTATION = 16; // For the adaptation of the anisotropic selection

   public static final int PARAM_MIGRATION_FREQUENCY  = 17; // Migration frequency for the islands model
   
   public static final int PARAM_NEWS     = 21; //NEWS neighborhhod
   
   // For the multi-objective case
   public static final int PARAM_SOLUTION_FRONT		= 23; // The solution front
   public static final int PARAM_UPPER_LIMIT		= 25; // Upper limit for the alleles values
   public static final int PARAM_LOWER_LIMIT		= 26; // Upper limit for the alleles values
   public static final int PARAM_FEEDBACK		    = 27; // Number of solutions to be inserted in the neighborhood from the Pareto front
   // For the multi-objective case
   
   // For the gui
   public static final int PARAM_DISPLAY       = 28;
   public static final int PARAM_DISPLAY2      = 29;
   public static final int PARAM_DISPLAY_STEPS = 30;
   // For the gui
   
   // For the hierarchical model
   public static final int PARAM_HIERARCHY  	= 31;
   public static final int PARAM_ASYNC_SWAP		= 32;
   public static final int PARAM_SWAP_FREQ		= 33;
   public static final int PARAM_SWAP_IF_STATIC	= 34;
   public static final int PARAM_MOVES_SWAP		= 35;
   public static final int PARAM_SWAP_PROB		= 36;
   // For the hierarchical model
   
   // For the adaptive population 
   public static final int PARAM_POP_ADAPTATION	= 37;
   // For the adaptive population
   
   public static final int PARAM_VERBOSE		= 38;
   public static final int PARAM_SYNCHR_UPDATE	= 39;

   public static final int PARAM_INTEGER		= 1001;
   public static final int PARAM_DOUBLE			= 1002;
   
   
   protected Population   population;
   protected Statistic    statistic;
   protected Neighborhood neighborhood;
   protected CellUpdate   cellUpdate;
   protected GenerationListener listener;
   protected Problem      problem;
   protected Point        selectedCell;
   protected boolean	  synchronousUpdate;
   protected double       mutationProb;
   protected double       crossoverProb;
   protected double       localSearchProb;
   protected double       targetFitness;
   protected int          generationNumber;
   protected int          generationLimit;
   protected int          evaluationLimit;
   protected int 		  numFeedbackSols = 0;
   protected int		  max_steps = 0;
   
   protected int 		  migrationFreq = 10000;
   
   protected Map          operators;
   protected Random       r;
   
   protected int neighType=0; // indicates the neighborhood used
   
   protected double alleleMut;
   
   protected boolean verbose;
   
   // For the hierarchical model
   protected Hierarchy hierarchy = null;
   protected LineCenterLRSweep lrSweep = null;
   protected LineCenterUDSweep udSweep = null;
   protected Boolean asyncSwap = new Boolean("False");
   protected int swapFreq = 1;
   protected boolean swapIfStatic = false;
   protected int movesForSwapping = 10;
   protected double swapProb = 1.0;
   // For the hierarchical model
   
   // For the gui
   protected CGADisplay display, display2;
   protected Integer displaySteps;
   // For the gui
   
   // For the multi-objective case
   protected Archive 		   paretoFront;
   protected Vector 		   upperLimit;
   protected Vector			   lowerLimit;
   // For the multi-objective case
   
   // For the adaptive population
   protected AdaptivePop  adaptation = null;
   // For the adaptive population
   
   protected AdaptivePop ANadaptation = null; // For the Anisotropic selection

   public EvolutionaryAlg(Random r, int genLimit)
   {
      this.r = r;
      operators = new HashMap(); //Build a HashMap for storing the operators 
      
      // set default values
      population = new Population(100);
      statistic = null;
      neighborhood = new Linear5(10,10); // new Linear5(population.x,population.y);
      cellUpdate = null;
      listener = null;
      problem = null;
      selectedCell = null;
      mutationProb = 1.0;
      crossoverProb = 1.0;
      localSearchProb = 0.0;
      targetFitness = Double.MAX_VALUE;
      generationNumber = 0;
      generationLimit = genLimit;
      evaluationLimit = Integer.MAX_VALUE;
      alleleMut = 0.001;
      verbose = true;
      synchronousUpdate = true;
      
      // For the hierarchical model
      hierarchy = null;
      // For the hierarchical model

      // For the gui
      display = null;
      display2 = null;
      displaySteps = null;
      // For the gui
      
      // For the multi-objective case
      paretoFront = null;
      upperLimit = null;
      lowerLimit = null;
      // For the multi-objective case
   }

   // Returns any parameter of the algorithm (specified as 'keyValue') as an Object
   public Object getParam(int keyValue)
   {
      switch (keyValue)
      {
         case PARAM_POPULATION:				return population;
         case PARAM_STATISTIC:				return statistic;
         case PARAM_NEIGHBOURHOOD:			return neighborhood;
         case PARAM_CELL_UPDATE:			return cellUpdate;
         case PARAM_LISTENER:				return listener;
         case PARAM_PROBLEM:				return problem;
         case PARAM_MUTATION_PROB:			return new Double(mutationProb);
         case PARAM_CROSSOVER_PROB:			return new Double(crossoverProb);
         case PARAM_LOCAL_SEARCH_PROB:		return new Double(localSearchProb);
         case PARAM_TARGET_FITNESS:			return new Double(targetFitness);
         case PARAM_GENERATION_NUMBER:		return new Integer(generationNumber);
         case PARAM_SELECTED_CELL:			return selectedCell;	
         case PARAM_GENERATION_LIMIT: 		return new Integer(generationLimit);
         case PARAM_ALLELE_MUTATION_PROB:	return new Double(alleleMut);
         // For the multi-objective case
         case PARAM_SOLUTION_FRONT:			return paretoFront;
         case PARAM_EVALUATION_LIMIT:		return new Integer(evaluationLimit);
         case PARAM_UPPER_LIMIT:			return upperLimit;
         case PARAM_LOWER_LIMIT:			return lowerLimit;
         case PARAM_FEEDBACK:				return new Integer(numFeedbackSols);
         // For the multi-objective case
         // For the gui
         case PARAM_DISPLAY:				return display;
         case PARAM_DISPLAY2:				return display2;
         case PARAM_DISPLAY_STEPS:			return displaySteps;
         // For the gui
         // For the hierarchical model
         case PARAM_HIERARCHY:				return hierarchy;
         case PARAM_ASYNC_SWAP:				return asyncSwap;
         case PARAM_SWAP_FREQ:				return new Integer(swapFreq);
         case PARAM_SWAP_IF_STATIC:			return new Boolean(swapIfStatic);
         case PARAM_MOVES_SWAP:				return new Integer(movesForSwapping);
         case PARAM_SWAP_PROB:				return new Double(swapProb);
         // For the hierarchical model
         // For the adaptive population
         case PARAM_POP_ADAPTATION:			return adaptation;
         // For the adaptive population
         case PARAM_VERBOSE:				return new Boolean(verbose);
         case PARAM_SYNCHR_UPDATE:			return new Boolean(synchronousUpdate);
         case PARAM_ANISOTROPIC_ADAPTATION: return ANadaptation;
         case PARAM_MIGRATION_FREQUENCY:	return new Integer(migrationFreq);
         case PARAM_LOCAL_SEARCH_STEPS:		return new Integer(max_steps);
         default: return null;
      }
   }

   // The operators are identified with String objects
   public Object getParam(String keyName)
   {
      return operators.get(keyName);
   }

   // Set the parameter of the algorithm specified as 'keyValue' to the value in 'param'
   public void setParam(int keyValue, Object param)
   {
      switch (keyValue)
      {
         case PARAM_POPULATION:
            population = (Population)param;
            break;
         case PARAM_STATISTIC:
            statistic = (Statistic)param;
            break;
         case PARAM_NEIGHBOURHOOD:
            neighborhood = (Neighborhood)param;
            neighType = PARAM_NEWS;
            break;
         case PARAM_CELL_UPDATE:
            cellUpdate = (CellUpdate)param;
            break;
         case PARAM_LISTENER:
            listener = (GenerationListener)param;
            break;
         case PARAM_PROBLEM:
            problem = (Problem)param;
            break;
         case PARAM_MUTATION_PROB:
            mutationProb = ((Double)param).doubleValue();
            break;
         case PARAM_CROSSOVER_PROB:
            crossoverProb = ((Double)param).doubleValue();
            break;
         case PARAM_LOCAL_SEARCH_PROB:
            localSearchProb = ((Double)param).doubleValue();
            break;
         case PARAM_TARGET_FITNESS:
            targetFitness = ((Double)param).doubleValue();
            break;
         case PARAM_GENERATION_NUMBER:
            generationNumber = ((Integer)param).intValue();
            break;
         case PARAM_GENERATION_LIMIT:
            generationLimit = ((Integer)param).intValue();
            break;
         case PARAM_ALLELE_MUTATION_PROB:
            alleleMut = ((Double)param).doubleValue();
            break;
         // For the multi-objective case
         case PARAM_SOLUTION_FRONT:
         	paretoFront = (Archive)param;
         	break;
         case PARAM_EVALUATION_LIMIT:
         	evaluationLimit = ((Integer) param).intValue();
         	break;
         case PARAM_UPPER_LIMIT:
         	upperLimit = (Vector) param;
         	break;
         case PARAM_LOWER_LIMIT:
         	lowerLimit = (Vector) param;
         	break;
         case PARAM_FEEDBACK:
         	numFeedbackSols = ((Integer) param).intValue();
         	break;
         // For the multi-objective case
         // For the gui
         case PARAM_DISPLAY_STEPS: 
         	displaySteps = (Integer) param;
         	break;
         case PARAM_DISPLAY: 
         	display = (CGADisplay) param;
         	break;
         case PARAM_DISPLAY2: 
         	display2 = (CGADisplay) param;
         	break;
         // For the gui
         // For the hierarchical model
         case PARAM_HIERARCHY: 
         	hierarchy = (Hierarchy) param;
         	break;
         case PARAM_ASYNC_SWAP:
         	asyncSwap = (Boolean) param;
         	break;
         case PARAM_SWAP_FREQ:
         	swapFreq = ((Integer) param).intValue();
         	break;
         case PARAM_SWAP_IF_STATIC:
         	swapIfStatic = ((Boolean) param).booleanValue();
         	break;
         case PARAM_MOVES_SWAP:
         	movesForSwapping = ((Integer) param).intValue();
         	break;
         case PARAM_SWAP_PROB:
         	swapProb = ((Double) param).doubleValue();
         	break;
         // For the hierarchical model
         // For the adaptive population
         case PARAM_POP_ADAPTATION:
         	adaptation = (AdaptivePop) param;
         	break;
         // For the adaptive population
         case PARAM_VERBOSE:
         	verbose = ((Boolean) param).booleanValue();
         	break;
         case PARAM_SYNCHR_UPDATE:
         	synchronousUpdate = ((Boolean) param).booleanValue();
         	break;
         case PARAM_ANISOTROPIC_ADAPTATION:
        	 ANadaptation = (AdaptivePop) param;
        	 break;
         case PARAM_MIGRATION_FREQUENCY:
        	 migrationFreq = ((Integer) param).intValue();
        	 break;
         case PARAM_LOCAL_SEARCH_STEPS:
        	 max_steps = ((Integer) param).intValue();
        	 break;
         default:
			 System.err.println("no proper PARAM selected");
		 	 System.exit(-1);
      }
   }
   
   public void setParam(String keyName, Object param)
   {
      operators.put(keyName,param);
   }
   
   //Run generationLimit generations (or evaluationLimit evaluations) of an Evolutionary Algorithm
   public abstract void experiment();
}
