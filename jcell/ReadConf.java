
/**
 * @author Bernabe Dorronsoro
 * August, 13th, 1006
 * 
 * This class reads a configuration 
 * file and sets the values in that file to the 
 * evolutionary algorithm
 * 
 */

package jcell;

import gui.CGADisplay;

import java.util.Properties;
import java.util.HashMap;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;

import jcell.neighborhoods.*;
import MO.*;
import HcGA.*;
//import operators.*;
import adaptiveCGA.*;
import genEA.*;
import ssEA.*;
import java.util.Random;

import operators.mutation.*;
import operators.recombination.*;
import ExceptionHandlers.*;


public class ReadConf {

	/**
	 *  
	 */

	protected String filename;
	protected String dataFile;
	protected EvolutionaryAlg ea;
	protected StoreProperties properties;
	protected Random r;
	protected Boolean verbose = new Boolean(true);
	protected Double proportion;
	
	private final static int evaluationsLimitDf		= 1000000;
	private final static String popSizeDf			= "(10, 10)";
	private final static String updateDf			= "Synchronous";
	private final static String paretoDf			= "CMoEA.Crowding";
	private final static double mutationDf			= 1.0;
	private final static double crossoverDf			= 1.0;
	private final static String selectionP1Df		= "jcell.CenterSelection";
	private final static String selectionP2Df		= "jcell.TournamentSelection";
	private final static String replacementDf		= "jcell.ReplaceIfNonWorse";
	private final static String displayStepsDf		= "1";
	private final static String swapFreqDf			= "1";
	private final static String swapProbDf			= "1.0";
	private final static String asyncSwapDf			= "True";
	private final static String swapIfNoMovesDf		= "False";
	private final static String movesForSwappingDf	= "10"; 
	private final static String adaptivePopDf		= "False"; // The use of self-adaptive population is false by default
	private final static String adaptiveAnisotrDf	= "False"; // The use of self-adaptive anisotropic neighborhood is false by default
	private final static String adaptiveAnisotrPolicyDf	= "adaptiveCGA.AFAnisotropic"; // The default policy for the adaptive anisotropic neighborhood is AF
	private final static String adaptivePolicyDf	= "adaptiveCGA.AF"; // The default adaptive policy is AF
	private final static String verboseStrDf		= "False"; 	// Verbose is false by default
	private final static String algDf				= "CellularGA"; // Default algorithm
	private final static String islandsDf			= "5"; // Default number of islands
	private final static String migrationFreqDf		= "10000"; // Default value for the migration frequency
	private final static int maxStepsDf				= 100; // Default number of steps for the local search
	private final static double lsProbDf			= 0.0; // Default value for the probability of applying local search
	private final static int BypassLinksClustersDf  = 1; // Default number of clusters in the network of the problem
	private final static boolean synchronousUpdateDf= true; // Algorithms are synchronous by default


	public class StoreProperties {

		private HashMap properties;
		{

			try {
				FileInputStream f = new FileInputStream(filename);

				Properties tempProperties = new Properties();
				tempProperties.load(f);
				f.close();

				properties = new HashMap(tempProperties);

				/*
				 * Print the input parameters: key = value
				 */
				String verboseStr = (String) properties.get("Verbose");
				if (verboseStr == null)
					verboseStr = verboseStrDf;
				
				verbose = new Boolean(verboseStr);
				
				if (verbose.booleanValue())
					System.out.println(properties);

			} catch (Exception e) {
				System.err
						.println("Error when reading the configuration file: "
								+ e);
			}
		}

		private StoreProperties() {
		}

		public String getProperty(String nombre)
				throws MissedPropertyException {

			String valor = (String) properties.get(nombre);

			return valor;
		}
	}
	
	
	public Double getProportion() {
		
		return this.proportion;
	}
	

	public ReadConf(String filename, String dataFile, Random r) {
		
		super();
		this.filename = filename;
		this.dataFile = dataFile;
		this.r = r;
		this.properties = new StoreProperties();
		this.proportion = 0.0;
	}

	public void SetConfFile(String filename) {
		
		this.filename = filename;
	}

	public StoreProperties getProperties()
	{
		return properties;
	}

	private void writeLine(String line)
	{
		if (verbose.booleanValue())
			System.out.println(line);
	}
	
	public EvolutionaryAlg getParameters(String dataFile, int genLimit) {

		try
		{
			// Write some information during the run?
			String verboseStr = properties.getProperty("Verbose");
			if (verboseStr == null)
				verboseStr = verboseStrDf;
			
			verbose = new Boolean(verboseStr);
			
			// Set the limit of evaluations
			String evals = properties.getProperty("EvaluationsLimit");
			Integer evaluationsLimit = new Integer(evaluationsLimitDf);
			if (evals == null)
				writeLine("Limit of evaluations not provided. Default value: " + evaluationsLimitDf);
			else
				evaluationsLimit = new Integer(evals);

			Class c;
			// cellular, generational, steady-state, or distributed GA
			String alg = properties.getProperty("Algorithm");
			String islands = properties.getProperty("islandsNumber");
			
			if ((islands == null) && (alg.equalsIgnoreCase("distributed")))
			{
				writeLine("Number of islands not provided. Default number of islands: " + islandsDf);
				islands = islandsDf;
			}

			Population pop = null;
			
			if (alg.equalsIgnoreCase("cellular"))
				ea = new CellularGA(r, genLimit);
			else if (alg.equalsIgnoreCase("generational"))
				ea = new GenGA(r, genLimit);
			else if (alg.equalsIgnoreCase("steady-state"))
				//ea = new SSGAEfficient(r);
				ea = new SSGA(r, genLimit);
			else if (alg.equalsIgnoreCase("distributed"))
				ea = new DistributedGA(r, genLimit);
			else
			{
				if (alg.equalsIgnoreCase("cellular"))
					throw new MissedPropertyException("The population must be supplied like (x, y) in the cellular case");
				
				writeLine("Algorithm model not provided. Default value: " + algDf);
				
				c = Class.forName(algDf);
				Constructor[] cons = c.getDeclaredConstructors();
				
				int l = cons.length;
				// Call the constructor. 
				// Problem Constructors having more than one arguments are not allowed.
				// Problem constructors with one argument are preferred with respect to those having no arguments
				for (int i = l-1; i>=0; i--)
				{
					if (cons[i].getParameterTypes().length == 1)
					{
						Random[] aux = new Random[1];
						aux[0] = r;
						ea = (EvolutionaryAlg) cons[i].newInstance(aux); // Constructor called
						break;
					}
				}
			}
		
			int begin = 0, end = 0;
			
			// Set the population shape
			String popSize = properties.getProperty("Population");
			if ((popSize == null) && (!alg.equalsIgnoreCase("ils")))
			{
				System.err.println("Population shape not provided.");
				System.exit(-1);
			}
			
			if (popSize != null)
				begin = popSize.indexOf("(") + 1;
			
			if(begin == 0)
				begin = popSize.indexOf("[") + 1;
			
			int x=0, y=0;
			
			if ((begin == 0) && (alg.equalsIgnoreCase("cellular")))
				throw new MissedPropertyException("The population must be supplied like (x, y) in the cellular case");

			else if (alg.equalsIgnoreCase("distributed"))
			{
				int isl = (new Integer(islands)).intValue();
				int totalPopSize = (new Integer(popSize)).intValue();
				pop = new PopIsland(isl,totalPopSize/isl);
			}
			else if (begin != 0 && !alg.equalsIgnoreCase("generational"))
			{
				end   = popSize.indexOf(",");
				x = (new Integer(popSize.substring(begin,end).trim())).intValue();
				begin = end + 1;
				end   = popSize.indexOf(")");
				y = (new Integer(popSize.substring(begin,end).trim())).intValue();
				
				// Create the population
				pop = new PopGrid(x,y);
			}
			
			else if(alg.equalsIgnoreCase("steady-state"))
			{
				pop = new Population((new Integer(popSize)).intValue());
				// pop = new OrderedPop((new Integer(popSize)).intValue());
			}
			else // generational algorithms
				if (popSize != null)
					if (begin != 0) {
						
						begin = popSize.indexOf("[") + 1;
						end = popSize.indexOf("]");
						
						this.proportion = (new Double(popSize.substring(begin, end).trim()));
						
						pop = new Population(10);
					}
					else
						pop = new Population((new Integer(popSize)).intValue());
		
			// Use of hierarchycal population?
			String hierarchyName = properties.getProperty("HierarchycalPop");
			Boolean useHierarchy = null;
			Hierarchy hierarchy = null;
			String asyncSwap = asyncSwapDf;
			String swapFreq = swapFreqDf;
			String swapProb = swapProbDf;
			String swapIfNoMoves = swapIfNoMovesDf;
			String movesForSwapping = movesForSwappingDf;
			if (hierarchyName == null)
				useHierarchy = new Boolean("False");
			else
			{
				useHierarchy = new Boolean(hierarchyName);
				if (useHierarchy.booleanValue())
				{
					hierarchy = new Hierarchy(Hierarchy.HIERARCHY_TYPE_PYRAMID, Hierarchy.HIERARCHY_SWAP_FITNESS,(CellularGA)ea);
					// synchronous or asynchronous swap?
					asyncSwap = properties.getProperty("AsynchSwap");
					if (asyncSwap==null)
					{
						writeLine("Swap synchronicity not provided. Default value: " + asyncSwapDf + " (false=synchronous; true=asynchronous)");
						asyncSwap = asyncSwapDf;
					}
						
					// Set the swapping frequency
					swapFreq = properties.getProperty("SwapFreq");
					if (swapFreq == null)
					{
						writeLine("Swapping frequency not provided. Default value: " + swapFreqDf + " generation");
						swapFreq = swapFreqDf;
					}
					
					// Set the swapping probability
					swapProb = properties.getProperty("SwapProb");
					if (swapProb == null)
					{
						writeLine("Swapping probability not provided. Default value: " + swapProbDf);
						swapProb = swapProbDf;
					}
					
					// swap an individual if it has been in that position for too long?
					swapIfNoMoves = properties.getProperty("SwapIfNoMoves");
					
					if (swapIfNoMoves == null)
					{
						writeLine("Force swap for too static individuals parameter not provided. Default value: " + swapIfNoMovesDf);
						swapIfNoMoves = swapIfNoMovesDf;
					}
					if (new Boolean(swapIfNoMoves).booleanValue())
					{
						movesForSwapping = properties.getProperty("MovesForSwapping");
						if (movesForSwapping == null)
						{
							writeLine("The number of unsuccessful swapping tries for considering an individuals too static is not provided. Default value: " + movesForSwappingDf);
							movesForSwapping = movesForSwappingDf;
					}
				}
				}
			}

			// Set the update policy
			String update = properties.getProperty("UpdatePolicy");
			if ((update == null) && (alg.equalsIgnoreCase("cellular")))
			{
				writeLine("Update policy not provided. Default value: " + updateDf);
				update = updateDf;
			}
			else if (update == null) 
				update = updateDf; // update won't be used. This is made only for avoiding null pointing errors
			
			String migrationFreq = properties.getProperty("MigrationFreq");//.toLowerCase();
			if (migrationFreq == null)
				migrationFreq = migrationFreqDf;
			ea.setParam(EvolutionaryAlg.PARAM_MIGRATION_FREQUENCY, new Integer(migrationFreq));
			
			String updateLC = update.toLowerCase();
			
			boolean synchronousUpdate = synchronousUpdateDf; 
			if ((update!=null) && updateLC.contains("asynchronous"))
				synchronousUpdate = false;
			
			else if (update == null)
				writeLine("Update synchronousity not provided. Default value: " + synchronousUpdateDf);
				
			ea.setParam(CellularGA.PARAM_SYNCHR_UPDATE, new Boolean(synchronousUpdate));
			ea.setParam(CellularGA.PARAM_STATISTIC, new ComplexStats());
			
			// Set the problem to solve
			String prob = properties.getProperty("Problem");
			if (prob == null)
				throw new MissedPropertyException("Problem");
						
			c = Class.forName(prob);
			Constructor[] cons = c.getDeclaredConstructors();
			
			Problem problem = null; // The problem is initialized here.
			
			int l = cons.length;
			// Call the constructor. 
			// Problem Constructors having more than one arguments are not allowed.
			// Problem constructors with one argument are preferred with respect to those having no arguments
			for (int i = l-1; i>=0; i--)
			{
				if (cons[i].getParameterTypes().length > 4)
					throw new MissedPropertyException("Problem Constructors can only have 0, 1 or 2 arguments");
				else if (cons[i].getParameterTypes().length == 4)
				{
					// The file wherein the instance to solve is 
					Object[] aux = new Object[4];
					for (int j=0; j<4; j++)
					{
						if (cons[i].getParameterTypes()[j].getName().equalsIgnoreCase(Random.class.getName()))
							// if the parameter is a Random object 
							aux[j] = r;
						else if (cons[i].getParameterTypes()[j].getName().equalsIgnoreCase(EvolutionaryAlg.class.getName()))
		                //	if the parameter is an EvolutionaryAlg object (required for MO problems)
							aux[j] = (EvolutionaryAlg)ea;
						else if (cons[i].getParameterTypes()[j].getName().equalsIgnoreCase(String.class.getName()))
						{
							String indiv = properties.getProperty("Individual");
							//	if the parameter is a String object (for problems needing an instance file to read)
							aux[j] = indiv;
						}
						else if (cons[i].getParameterTypes()[j].getName().equalsIgnoreCase(Integer.class.getName()))
						{
				            //	if the parameter is an Integer object (for problems needing an instance file to read)
							int BypassLinksClusters;
							String clusters = properties.getProperty("BypassLinksClusters");
							if (clusters == null)
								BypassLinksClusters = BypassLinksClustersDf;
							else
								BypassLinksClusters = Integer.parseInt(clusters);

							aux[j] = new Integer(BypassLinksClusters);
						}
					}
					problem = (Problem) cons[i].newInstance(aux); // Constructor called
					break;
				}
				else if (cons[i].getParameterTypes().length == 0)
				{ 
					problem = (Problem) c.newInstance(); // Constructor called
					break;
				}
				else if (cons[i].getParameterTypes().length == 2) // When the problem needs to read the instance from a file
				{
					// The file wherein the instance to solve is 
					String instance = this.dataFile;
					Object[] aux = new Object[2];
					for (int j=0; j<2; j++)
					{
						if (cons[i].getParameterTypes()[j].getName().equalsIgnoreCase(Random.class.getName()))
							// if the parameter is a Random object 
							aux[j] = r;
						else if ((cons[i].getParameterTypes()[j].getName().equalsIgnoreCase(EvolutionaryAlg.class.getName())) ||
								(cons[i].getParameterTypes()[j].getSuperclass().getName().equalsIgnoreCase(EvolutionaryAlg.class.getName())))
		                //	if the parameter is an EvolutionaryAlg object (required for MO problems)
							aux[j] = (EvolutionaryAlg)ea;
						else if (cons[i].getParameterTypes()[j].getName().equalsIgnoreCase(CellularGA.class.getName()))
			                //	if the parameter is an EvolutionaryAlg object (required for MO problems)
								aux[j] = (EvolutionaryAlg)ea;
						else if (cons[i].getParameterTypes()[j].getName().equalsIgnoreCase(String.class.getName()))
			            //	if the parameter is a String object (for problems needing an instance file to read)
							aux[j] = instance;
						else if (cons[i].getParameterTypes()[j].getName().equalsIgnoreCase(Integer.class.getName()))
						{
				            //	if the parameter is an Integer object (for problems needing an instance file to read)
							int BypassLinksClusters;
							String clusters = properties.getProperty("BypassLinksClusters");
							if (clusters == null)
								BypassLinksClusters = BypassLinksClustersDf;
							else
								BypassLinksClusters = Integer.parseInt(clusters);
								aux[j] = new Integer(BypassLinksClusters);
						}
					}
					problem = (Problem) cons[i].newInstance(aux); // Constructor called
					break;
				}
				else if (cons[i].getParameterTypes().length == 3)
				{
					// The file wherein the instance to solve is 
					Object[] aux = new Object[3];
					for (int j=0; j<3; j++)
					{
						if (cons[i].getParameterTypes()[j].getName().equalsIgnoreCase(Random.class.getName()))
							// if the parameter is a Random object 
							aux[j] = r;
						else if (cons[i].getParameterTypes()[j].getName().equalsIgnoreCase(EvolutionaryAlg.class.getName()))
		                //	if the parameter is an EvolutionaryAlg object (required for MO problems)
							aux[j] = (EvolutionaryAlg)ea;
						else if (cons[i].getParameterTypes()[j].getName().equalsIgnoreCase(String.class.getName()))
						{
							String indiv = properties.getProperty("Individual");
							//	if the parameter is a String object (for problems needing an instance file to read)
							aux[j] = indiv;
						}
						else if (cons[i].getParameterTypes()[j].getName().equalsIgnoreCase(Integer.class.getName()))
						{
				            //	if the parameter is an Integer object (for problems needing an instance file to read)
							int BypassLinksClusters;
							String clusters = properties.getProperty("BypassLinksClusters");
							if (clusters == null)
								BypassLinksClusters = BypassLinksClustersDf;
							else
								BypassLinksClusters = Integer.parseInt(clusters);

							aux[j] = new Integer(BypassLinksClusters);
						}
					}
					problem = (Problem) cons[i].newInstance(aux); // Constructor called
					break;
				}
				else
				{
					if (cons[i].getParameterTypes()[0].getName().equalsIgnoreCase(Random.class.getName()))
					// if the parameter is a Random object 
					{
						Random[] aux = new Random[1];
						aux[0] = r;
						problem = (Problem) cons[i].newInstance(aux); // Constructor called
						break;
					}
					else if (cons[i].getParameterTypes()[0].getName().equalsIgnoreCase(EvolutionaryAlg.class.getName()))
                    //	if the parameter is an EvolutionaryAlg object (required for MO problems)
					{
						EvolutionaryAlg[] aux = new EvolutionaryAlg[1];
						aux[0] = (EvolutionaryAlg)ea;
						problem = (Problem) cons[i].newInstance(aux); // Constructor called
						break;
					}
					else if (cons[i].getParameterTypes()[0].getName().equalsIgnoreCase(String.class.getName()))
	                    //	if the parameter is an EvolutionaryAlg object (required for MO problems)
						{
//						    // The file wherein the instance to solve is 
							String instance = this.dataFile;
							String[] aux = new String[1];
							aux[0] = (String)instance;
							problem = (Problem) cons[i].newInstance(aux); // Constructor called
							break;
						}
				}
			}
/*
			// Set the Individual
			String indiv = properties.getProperty("Individual");

			if (indiv == null)
				throw new MissedPropertyException("Individual");
			
			c = Class.forName(indiv);
			cons = c.getDeclaredConstructors();
			Individual individual = null; // The individual is initialized here.
			individual = (Individual) c.newInstance();
			individual.setMinMaxAlleleValue(true, problem.getMinAllowedValues());
			individual.setMinMaxAlleleValue(false, problem.getMaxAllowedValues());
			individual.setLength(problem.getVariables());
			individual.setNumberOfFuncts(problem.numberOfObjectives());
*/
			/*
			 * A primeira linha de c—digo parece-me escusada. 
			 * 
			 * Estas duas linhas devem ser comentadas caso o problema que se pretende estudar seja o TOP, 
			 * sendo descomentada a seguinte.
			
			//individual.setRandomValues(r); Faz duas vezes (em baixo o c—digo executado Ž o mesmo)
			
			//pop.setRandomPop(r,individual); // initialization of the random initial population
	
			*/
			
	//		pop.setTopPop(individual, problem.variables); // initialization of the initial population
			
			CellUpdate cu = null;
			
			if (alg.contains("cellular"))
			{
				if (updateLC.contains("asynchronous ls"))
					cu = new LineSweep((PopGrid)pop);				
				else if (updateLC.contains("asynchronous frs"))
					cu = new FixedRandomSweep(r,(PopGrid)pop);
				else if (updateLC.contains("asynchronous nrs"))
					cu = new NewRandomSweep(r,(PopGrid)pop);
				else if (updateLC.contains("asynchronous uc"))
					cu = new UniformChoice(r,(PopGrid)pop);
				else if (updateLC.contains("asynchronous ss"))
					cu = new SpiralSweep((PopGrid)pop);
			}
			
			// Set the Pareto parameters
			String pareto = properties.getProperty("ArchiveManagement");
			if ((pareto == null) && (problem.numberOfObjectives()>1))
			{
				writeLine("Archive management policy not provided. Default value: " + paretoDf);
				pareto = paretoDf;
			}	
			
			String depth = null;
			Archive archive = null;
			
			if (pareto!=null)
			{
				c = Class.forName(pareto);
				archive = (Archive) c.newInstance(); // Constructor called
				archive.setNumberOfFunctions(problem.numberOfObjectives());
						
				// Set the Depth parameter (for the AdaptiveGrid case)
				depth = properties.getProperty("Depth");
				if ((depth == null) && (pareto.equalsIgnoreCase("adaptivegrid")))
					writeLine("The 'Depth' parameter of the adaptive grid policy is not provided. Default value: " + ((AdaptiveGrid)archive).depth);
				else if ((depth != null) && (pareto.equalsIgnoreCase("adaptivegrid")))
					((AdaptiveGrid)archive).depth = new Integer(depth).intValue();
			}
			
			// Set the mutation probability
			String mutValue = properties.getProperty("MutationProb");
			Double mutation = new Double(mutationDf);
			if (mutValue==null)
				writeLine("Mutation probability not provided. Default value: " + mutationDf);
			else
				mutation = new Double(mutValue);
			
			
			Boolean alleleMutation = false;
			String allelelMutValue = properties.getProperty("AlleleMutationProb");
			Double alleleMutationProb = 0.001;
			if (allelelMutValue == null)
				writeLine("Mutation probability not provided. Default value is one over the number of genes");
			else
			{
				alleleMutation = true;
				alleleMutationProb = new Double(allelelMutValue);
			}
			
			// Set the crossover probability
			Double crossover = new Double(crossoverDf);
			String crossValue = properties.getProperty("CrossoverProb");
			if (crossValue == null)
				writeLine("Crossover probability not provided. Default value: " + crossoverDf);
			else
				crossover = new Double(crossValue);
			
			// Set the neighborhood
			String neigh = properties.getProperty("Neighborhood");
			Neighborhood neighborhood = new Linear5(x,y);
			
			if ((neigh == null) && (alg.equalsIgnoreCase("cellular")))
				writeLine("Neighborhood not provided. Default neighborhood: " + neighborhood.getClass().getName());
			
			if ((neigh!=null) && (!neigh.toLowerCase().contains("linear5")))
			{
				c = Class.forName(neigh);
				neighborhood = (Neighborhood) c.newInstance(); // Constructor called
			}
			
			// Set the selection policy for parent 1
			String selectionP1 = properties.getProperty("SelectionParent1");
			if (selectionP1 == null)
			{
				writeLine("Selection of first parent not provided. Default selection: " + selectionP1Df);
				selectionP1 = selectionP1Df;
			}
			Operator sel1 = null;
			c = Class.forName(selectionP1);
			cons = c.getConstructors();
			Random[] aux = new Random[1];
			aux[0] = r;
			l = cons.length;
			// Call the constructor. 
			// Operator Constructors having more than one arguments are not allowed.
			// Operator constructors with one argument are preferred with respect to those having no arguments
			for (int i = l-1; i>=0; i--)
			{
				if (cons[i].getParameterTypes().length == 1)
				{
					sel1 = (Operator) cons[i].newInstance(aux); // Constructor called
					break;
				}
			}
			
			// Set the selection policy for parent 2
			String selectionP2 = properties.getProperty("SelectionParent2");
			if (selectionP2 == null)
			{
				writeLine("Selection of second parent not provided. Default selection: " + selectionP2Df);
				selectionP2 = selectionP2Df;
			}
			Operator sel2 = null;
			c = Class.forName(selectionP2);
			cons = c.getConstructors();
			l = cons.length;
			// Call the constructor. 
			// Operator Constructors having more than one arguments are not allowed.
			// Operator constructors with one argument are preferred with respect to those having no arguments
			for (int i = l-1; i>=0; i--)
			{
				if (cons[i].getParameterTypes().length == 1)
				{
					sel2 = (Operator) cons[i].newInstance(aux); // Constructor called
					break;
				}
			} 
						
			// Set the recombination operator
			String recombName = properties.getProperty("Crossover");
			if (recombName == null)
				throw new MissedPropertyException("Crossover");
			c = Class.forName(recombName);
			Operator recomb = null;
			cons = c.getConstructors();
			if (cons[0].getParameterTypes().length == 0) 
			// if there is a constructor with no arguments
				recomb = (Operator) c.newInstance();
			else
			{
				l = cons.length;
				// Call the constructor. 
				// Operator Constructors having more than one arguments are not allowed.
				// Operator constructors with one argument are preferred with respect to those having no arguments
				for (int i = l-1; i>=0; i--)
				{
					if (cons[i].getParameterTypes().length == 1)
					{
						aux[0] = r;
						recomb = (Operator) cons[i].newInstance(aux); // Constructor called
						break;
					}
					else if ((cons[i].getParameterTypes().length == 2) && cons[i].getParameterTypes()[0] == Random.class)
					{

						Object[] aux2 = new Object[2];
						aux2[0] = r;
						aux2[1] = problem;
						//l = cons.length;
						// Call the constructor. 
						// Operator Constructors having more than one arguments are not allowed.
						// Operator constructors with one argument are preferred with respect to those having no arguments
								recomb = (Operator) cons[i].newInstance(aux2); // Constructor called
								break;
					}
					else if ((cons[i].getParameterTypes().length == 2) && cons[i].getParameterTypes()[0] == Double.class)
					{
						// The first parameter is a recombination specific parameter, and it will be set later
						Object aux2 = problem;

						recomb = (Operator) cons[i].newInstance(aux2); // Constructor called
						break;
					}
				}
			}
			
			// Special parameters for the recombination operators
			// Set the pBias parameter (for the Ax recombination)
			String pBias = properties.getProperty("pBiasAX");
			if ((pBias == null) && (recombName.equalsIgnoreCase("AX")))
				writeLine("pBias parameter not provided for AX. Default value: " + ((Ax)recomb).getpBias());
			else if ((pBias != null) && (recombName.equalsIgnoreCase("AX")))
				((Ax)recomb).setpBias(new Double(pBias).doubleValue());
			
			// Set the alpha parameter (for the BLXalpha recombination)
			String alpha = properties.getProperty("AlphaBLX");
			if ((alpha == null) && (recombName.equalsIgnoreCase("BLX")))
				writeLine("alpha parameter not provided for BLX. Default value: " + ((BLXalpha)recomb).getAlpha());
			else if ((alpha != null) && (recombName.equalsIgnoreCase("BLX")))
				((BLXalpha)recomb).setAlpha(new Double(alpha).doubleValue());
			
			// Set the lambda parameter (for the FCBX recombination)
			String lambda = properties.getProperty("LambdaFCBX");
			if ((lambda == null) && (recombName.equalsIgnoreCase("FCBX")))
				writeLine("lambda parameter not provided for FCBX. Default value: " + ((FCBX)recomb).getLambda());
			else if ((lambda != null) && (recombName.equalsIgnoreCase("FCBX")))
				((FCBX)recomb).setLambda(new Double(lambda).doubleValue());
			
			// Set the alpha parameter (for the PBX recombination)
			String alphaPBX = properties.getProperty("AlphaPBX");
			if ((alphaPBX == null) && (recombName.equalsIgnoreCase("PBX")))
				writeLine("alpha parameter not provided for PBX. Default value: " + ((PBX)recomb).getAlpha());
			else if ((alphaPBX != null) && (recombName.equalsIgnoreCase("PBX")))
				((PBX)recomb).setAlpha(new Double(alphaPBX).doubleValue());
			
			// Set the lambda and ro parameters (for the PNX recombination)
			String lambdaPNX = properties.getProperty("LambdaPNX");
			String roPNX = properties.getProperty("RoPNX");
			if ((lambdaPNX == null) && (recombName.equalsIgnoreCase("PNX")))
				writeLine("lambda parameter not provided for PNX. Default value: " + ((PNX)recomb).getLambda());
			if ((roPNX == null) && (recombName.equalsIgnoreCase("PNX")))
				writeLine("ro parameter not provided for PNX. Default value: " + ((PNX)recomb).getRo());
			if ((lambdaPNX != null) && (roPNX != null) && (recombName.equalsIgnoreCase("PNX")))
			{
				((PNX)recomb).setLambda(new Double(lambdaPNX).doubleValue());
				((PNX)recomb).setRo(new Double(roPNX).doubleValue());
			}
			
			// Set the pBias parameter (for the Px recombination)
			String pBiasPX = properties.getProperty("pBiasPX");
			if ((pBiasPX == null) && (recombName.equalsIgnoreCase("Px")))
				writeLine("pBias parameter not provided for Px. Default value: " + ((Px)recomb).getpBias());
			else if ((pBiasPX != null) && (recombName.equalsIgnoreCase("Px")))
				((Px)recomb).setpBias(new Double(pBiasPX).doubleValue());
			
			// Set the distributionIndex parameter (for the SBX recombination)
			String distributionIndex = properties.getProperty("distributionIndexSBX");
			if ((distributionIndex == null) && (recombName.equalsIgnoreCase("SBX")))
				writeLine("distributionIndex parameter not provided for SBX. Default value: " + ((SBX)recomb).getDistributionIndex());
			else if ((distributionIndex != null) && (recombName.equalsIgnoreCase("SBX")))
				((SBX)recomb).setDistributionIndex(new Double(distributionIndex).doubleValue());
			// Special parameters for the recombination operators
			
			// Set the mutation operator
			String mutName = properties.getProperty("Mutation");
			if (mutName == null)
				throw new MissedPropertyException("Mutation");
			c = Class.forName(mutName);
			Operator mut = null;
			cons = c.getConstructors();
			if (cons[0].getParameterTypes().length == 2) 
			// The constructor for every mutation needs 2 variables
			{
				Object[] auxMut = new Object[2];
				auxMut[0] = r;
				auxMut[1] = ea;
				mut = (Operator) cons[0].newInstance(auxMut); // Constructor called
			}
			
			// Special parameters for the recombination operators
			// Set the distributionIndex parameter (for the polynomial mutation)
			String distributionIndexPM = properties.getProperty("distributionIndexPM");
			if ((distributionIndexPM == null) && (mutName.equalsIgnoreCase("polynomialMutation")))
				writeLine("distributionIndex parameter not provided for PolynomialMutation. Default value: " + ((PolynomialMutation)mut).getDistributionIndex());
			else if ((distributionIndexPM != null) && (mutName.equalsIgnoreCase("polynomialMutation")))
				((PolynomialMutation)mut).setDistributionIndex(new Double(distributionIndexPM).doubleValue());
			
			// Set the deviation parameter (for the Gaussian mutation)
			String deviationGM = properties.getProperty("deviationGM");
			if ((deviationGM == null) && (mutName.equalsIgnoreCase("GaussianMutation")))
				writeLine("deviation parameter not provided for GaussianMutation. Default value: " + ((GaussianMutation)mut).getDeviation());
			else if ((deviationGM != null) && (mutName.equalsIgnoreCase("GaussianMutation")))
				((GaussianMutation)mut).setDeviation(new Double(deviationGM).doubleValue());
			
			// Set the deviation parameter (for the Cauchy mutation)
			String deviationCM = properties.getProperty("deviationCM");
			if ((deviationCM == null) && (mutName.equalsIgnoreCase("CauchyMutation")))
				writeLine("deviation parameter not provided for CauchyMutation. Default value: " + ((CauchyMutation)mut).getDeviation());
			else if ((deviationCM != null) && (mutName.equalsIgnoreCase("CauchyMutation")))
				((CauchyMutation)mut).setDeviation(new Double(deviationCM).doubleValue());
			// Special parameters for the recombination operators
			
			// Set the replacement policy
			String replName = properties.getProperty("Replacement");
			if (replName == null)
			{
				writeLine("Replacement policy not provided. Default selection: " + replacementDf);
				replName = replacementDf;
			}
			c = Class.forName(replName);
			Operator replacement = null;
			replacement = (Operator) c.newInstance();
			
			String displayStepsName = properties.getProperty("DisplaySteps");
			if (displayStepsName == null)
			{
				writeLine("The display steps are not provided. Default value: " + displayStepsDf);
				displayStepsName = displayStepsDf;
			}
			Integer displaySteps = new Integer(displayStepsName);
			
			// Set parameters of CEA
			ea.setParam(CellularGA.PARAM_POPULATION, pop);
			ea.setParam(CellularGA.PARAM_VERBOSE, verbose);
			ea.setParam(CellularGA.PARAM_SOLUTION_FRONT, archive);
			ea.setParam(CellularGA.PARAM_NEIGHBOURHOOD, neighborhood);
			ea.setParam(CellularGA.PARAM_DISPLAY_STEPS, displaySteps);
			ea.setParam(CellularGA.PARAM_CELL_UPDATE, cu);
			ea.setParam(CellularGA.PARAM_PROBLEM, problem);
			ea.setParam(CellularGA.PARAM_EVALUATION_LIMIT, evaluationsLimit);
			ea.setParam(CellularGA.PARAM_MUTATION_PROB, mutation);
			if (alleleMutation)
				ea.setParam(CellularGA.PARAM_ALLELE_MUTATION_PROB, new Double(alleleMutationProb)); // probability of allele mutation
			else if (problem.getVariables() > 1)
				ea.setParam(CellularGA.PARAM_ALLELE_MUTATION_PROB, new Double(1.0/(double)problem.getVariables())); // probability of allele mutation
			else
				ea.setParam(CellularGA.PARAM_ALLELE_MUTATION_PROB, new Double(0.5)); // probability of allele mutation
			ea.setParam(CellularGA.PARAM_CROSSOVER_PROB, crossover);
			ea.setParam(CellularGA.PARAM_TARGET_FITNESS, (Double) new Double(problem.getMaxFitness()));
			// For the hierarchical algorithm
			ea.setParam(CellularGA.PARAM_ASYNC_SWAP, new Boolean(asyncSwap));
			ea.setParam(CellularGA.PARAM_SWAP_FREQ, new Integer(swapFreq));
			ea.setParam(CellularGA.PARAM_SWAP_PROB, new Double(swapProb));
			ea.setParam(CellularGA.PARAM_SWAP_IF_STATIC, new Boolean(swapIfNoMoves));
			ea.setParam(CellularGA.PARAM_MOVES_SWAP, new Integer(movesForSwapping));
			// For the hierarchical algorithm
			// For the adaptive population
			
			// For the adaptive population
			ea.setParam("selection1", sel1);
			ea.setParam("selection2", sel2);
			ea.setParam("crossover", recomb);
			ea.setParam("mutation", mut);
			ea.setParam("replacement", replacement);
			ea.setParam(CellularGA.PARAM_HIERARCHY, hierarchy);
			ea.setParam("swap", new Swap());
			
			if ((hierarchy != null) && (alg.contains("cellular"))) 
				((PopGrid) pop).initializeGridPop(hierarchy.getHierarchyType());
		
			// Adaptive population shape?
			String adaptivePop = properties.getProperty("AdaptivePop");
			String adaptivePolicyName = adaptivePolicyDf;
			AdaptivePop adaptivePolicy = null;
			if (adaptivePop == null)
			{
				writeLine("Adaptive populaiton = " + adaptivePopDf);
				adaptivePop = adaptivePopDf;
			}
			if ((new Boolean(adaptivePop).booleanValue()) && (alg.contains("cellular")))
			{
				adaptivePolicyName = properties.getProperty("AdaptivePopPolicy");
				if (adaptivePolicyName == null)
				{
					writeLine("Policy of the adative population not defined. Default value: " + adaptivePolicyDf);
					adaptivePolicyName = adaptivePolicyDf;
				}
				c = Class.forName(adaptivePolicyName);
				cons = c.getDeclaredConstructors();
				Object[] aux2 = new Object[1];
				aux2[0] = ea;
				adaptivePolicy = (AdaptivePop) cons[0].newInstance(aux2);
			}
			
			ea.setParam(CellularGA.PARAM_POP_ADAPTATION, adaptivePolicy);
			
			// For the adaptive population case
			if (ea.getParam(CellularGA.PARAM_POP_ADAPTATION) != null)
			{
				// change initial population (if we would like it to be that with a medium ratio)
				Shapes listSh =((AdaptivePop) ea.getParam(CellularGA.PARAM_POP_ADAPTATION)).lsh;
				int initialShape = listSh.getSize() / 2;	// the initial shape is a compromise among exploration and exploitation
				x = listSh.get(initialShape).getX();		// sets x value for the first population
				y = listSh.get(initialShape).getY();		// sets y value for the first population
				((AdaptivePop) ea.getParam(CellularGA.PARAM_POP_ADAPTATION)).changeGridShape(listSh.get(initialShape));
				listSh.setIndex(initialShape);
			}
			// For the adaptive population case
			
//			 Set whether the population will be displayed or not
			String showDispl = properties.getProperty("ShowDisplay");
			if (showDispl == null)
			{
				writeLine("The population won't be displayed.");
				showDispl = "False";
			}
			
			Boolean display = new Boolean(showDispl);
			if (display.booleanValue())
			{
				if (!alg.equalsIgnoreCase("cellular"))
					System.out.println("The graphic interface is only available for the CellularGA. It won't be activated");
				else
				{
					ea.setParam(CellularGA.PARAM_DISPLAY, new CGADisplay((CellularGA)ea, problem.getMaxFitness(),CGADisplay.NO_TEXT + CGADisplay.DISPLAY_VALUE));
					ea.setParam(CellularGA.PARAM_DISPLAY2, new CGADisplay((CellularGA)ea, problem.getVariables(),CGADisplay.NO_TEXT + CGADisplay.DISPLAY_BESTDISTANCE));		  
				}
			}
			
			// Adaptive anisotropic selection?
			if ((selectionP1!=null) && (selectionP2!=null) && ((selectionP1.toUpperCase().contains("Anisotropic".toUpperCase())) ||
					(selectionP2.toUpperCase().contains("Anisotropic".toUpperCase()))))
			{
				if (!alg.equalsIgnoreCase("cellular"))
					throw new MissedPropertyException("Anisotropic selection can only be used with cellular populations");

				String adaptiveAnisotr = properties.getProperty("AdaptiveAnisotropic");
				String adaptiveAnisotrPolicyName = adaptiveAnisotrPolicyDf;
				AdaptivePop adaptiveAnisotrPolicy = null;
				if (adaptiveAnisotr == null)
				{
					writeLine("Adaptive Anisotropic Selection = " + adaptiveAnisotrDf);
					adaptiveAnisotr = adaptiveAnisotrDf;
				}
				if ((new Boolean(adaptiveAnisotr).booleanValue()) && (alg.contains("cellular")))
				{
					adaptiveAnisotrPolicyName = properties.getProperty("AdaptiveAnisotrPolicy");
					if (adaptiveAnisotrPolicyName == null)
					{
						writeLine("Policy of the adative anisotropic selection not defined. Default value: " + adaptiveAnisotrPolicyDf);
						adaptiveAnisotrPolicyName = adaptiveAnisotrPolicyDf;
					}
					c = Class.forName(adaptiveAnisotrPolicyName);
					cons = c.getDeclaredConstructors();
					Object[] aux2 = new Object[1];
					aux2[0] = ea;
					adaptiveAnisotrPolicy = (AdaptivePop) cons[0].newInstance(aux2);
				}
				
				ea.setParam(CellularGA.PARAM_ANISOTROPIC_ADAPTATION, adaptiveAnisotrPolicy);
			}
			
			// For the adaptive anisotropic selection case
			
			// Local Search
			String msteps = properties.getProperty("MaxSteps");
			Integer maxSteps = null;
			if (msteps == null) 
				maxSteps = new Integer(maxStepsDf);
			else maxSteps = new Integer(msteps);
			
			ea.setParam(EvolutionaryAlg.PARAM_LOCAL_SEARCH_STEPS, maxSteps);
			
			String lsp = properties.getProperty("LocalSearchProb");
			Double lsProb = null;
			if (lsp == null)
				lsProb = new Double(lsProbDf);
			else lsProb = new Double(lsp);
			
			ea.setParam(EvolutionaryAlg.PARAM_LOCAL_SEARCH_PROB, lsProb);

			String LocalSearch = properties.getProperty("LocalSearch");
			Operator LS = null;
			if (LocalSearch != null)
			{
				c = Class.forName(LocalSearch);
				cons = c.getConstructors();
				
				for (int i=0; i<cons.length; i++)
				{
					if (cons[0].getParameterTypes().length == 2)
					// The constructor for every local search needs 2 variables
					{
						Object[] aux2 = new Object[2];
						aux2[0] = r;
						aux2[1] = ea;
						LS = (Operator) cons[0].newInstance(aux2); // Constructor called
					}
				}
				
			}
			ea.setParam("local", LS);
		}
		catch (Exception e)
		{
			System.out.println("Error ocured while reading the configuration file: " + e);
			System.exit(-1);
		}
		
		return ea;
	}
}
