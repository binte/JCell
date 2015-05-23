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
 * The problem: Satisfiability problem
 * 
 */

package problems.Combinatorial;

import jcell.*; //Use jcell package

import java.io.*;
import java.util.*;

public class SAT extends Problem
{

	public static int longitCrom = 0;
	public static double maxFitness = 430.0;
	
	private int numclause;
	private int lenclause;
	private int numvar;
	private Vector clause = new Vector();
	private double[] weights;
	private double[] v;
	private double r;
	private int generation;
		
	private EvolutionaryAlg ea;
	
	private static final int COUNT = 0;
	private static final int SAW = 1;
	private static final int REF = 2;
	private static final int REFPLUS = 3;
	
	private static int fitnessFunction = SAW; 
	
	public SAT()
	{
		super();
		super.maxFitness = maxFitness;
	}
	
	public SAT(String filename, EvolutionaryAlg ea)
	{
		this.ea = ea;
		readInst(filename);
		
		if (fitnessFunction != COUNT)
		{
			weights = new double[numclause];		// initialize weights
			for (int i = 0; i < numclause; i++)
				weights[i] = 1;
		}
		
		// using REF or REF+ adaptive fitness functions?
		if ((fitnessFunction == REF) || (fitnessFunction == REFPLUS))
		{
			v = new double[numvar];
			for (int j = 0; j< numvar ; j++)
	      	{
	      		v[j] = 0.0;
	      	}
	      	r = 0;
	      	generation = 0;
		}
		
		variables = longitCrom;
		super.maxFitness = maxFitness;
		Target.maximize = true;
	}
	
	public void readInst(String filename)
	{
		
		BufferedReader br = null;
      	StringTokenizer st;
      	String token = new String();
    
		try
      	{
        	br = new BufferedReader(new FileReader(filename));
        	st = new StringTokenizer(br.readLine());
        	
        	int numTokens = st.countTokens();
        	int t = 0;
        	
        	while (t < numTokens) 
        	{
        		t++;
        		token = st.nextToken();
        		//System.out.println(token);
        	} 
        	
        	
        	lenclause = Integer.parseInt(token);
        	
        	st = new StringTokenizer(br.readLine());
        	st = new StringTokenizer(br.readLine());
        	st = new StringTokenizer(br.readLine());
        	
        	st.nextToken();
        	st.nextToken();
        	
        	numvar    = Integer.parseInt(st.nextToken());
        	numclause = Integer.parseInt(st.nextToken());
        	
        	
        	for (int i = 0; i < numclause ; i++)   // Read all clauses
        	{
        		st = new StringTokenizer(br.readLine());
        		Integer[] cl = new Integer[lenclause];
        		
        		for (int j = 0; j < lenclause; j++)			 // Read clause
        			cl[j] = new Integer(st.nextToken());
        		
        		clause.add(cl);
        	}
        	
        	br.close();
        	
        	longitCrom = numvar;
        	maxFitness = numclause;
        }
        catch (Exception e)
      	{
        	System.err.println("ERROR "+e);
      	}
	}

   // Overwrite eval method from Problem class.
   // Returns the fitness of Individual ind
   public Object eval(Individual ind)
   {
	   double result= 0.0;
	  switch (fitnessFunction)
	  {
	  	case COUNT:		result = evalCount(ind); break;
	  	case SAW:		result = evalSAW(ind); break;
	  	case REF:		result = evalREF(ind); break;	
	  	case REFPLUS:	result = evalREF(ind); break;
	  }

      return new Double(result);

   }
   
   // Eval fitness using SAW
   public double evalSAW(Individual ind)
   {
      BinaryIndividual bi = (BinaryIndividual)ind;
	  double fitness = 0.0;
      //int acum = 0;
     
      int clauseValue[] = new int[numclause];
      for (int i = 0; i < numclause; i++)
      {
      	Integer rl[] = (Integer[]) clause.get(i);
      	boolean a = (rl[0].intValue() < 0) ? (!bi.getBooleanAllele(Math.abs(rl[0].intValue())-1)) : 
      								 		 (bi.getBooleanAllele(Math.abs(rl[0].intValue())-1));
      	boolean b = (rl[1].intValue() < 0) ? (!bi.getBooleanAllele(Math.abs(rl[1].intValue())-1)) : 
      								 		 (bi.getBooleanAllele(Math.abs(rl[1].intValue())-1));
      	boolean c = (rl[2].intValue() < 0) ? (!bi.getBooleanAllele(Math.abs(rl[2].intValue())-1)) : 
      								 		 (bi.getBooleanAllele(Math.abs(rl[2].intValue())-1));
      	clauseValue[i] = ((a|b|c) ? 1 : 0) ;
      }
      
       // En generaciones pares recalcular los pesos
      if ((((Integer) ea.getParam(EvolutionaryAlg.PARAM_GENERATION_NUMBER)).intValue() != 0) 
       && (((Integer) ea.getParam(EvolutionaryAlg.PARAM_GENERATION_NUMBER)).intValue() % 2 == 0))
      {
      	BinaryIndividual bestInd = new BinaryIndividual();
      	int bestPos = ((Integer)((ComplexStats) ea.getParam(EvolutionaryAlg.PARAM_STATISTIC)).getStat(ComplexStats.MAX_FIT_POS)).intValue();
      	bestInd = (BinaryIndividual) ((Population) ea.getParam(EvolutionaryAlg.PARAM_POPULATION)).getIndividual(bestPos);
      	
      	double maxfitness = 0.0; // Al cambiar los pesos hay que cambiar el fitness
      	
      	for (int i = 0; i < numclause; i++)
      	{
      		Integer rl[] = (Integer[]) clause.get(i);
      		boolean a = (rl[0].intValue() < 0) ? (!bestInd.getBooleanAllele(Math.abs(rl[0].intValue())-1)) : 
      								 		 	 (bestInd.getBooleanAllele(Math.abs(rl[0].intValue())-1));
      		boolean b = (rl[1].intValue() < 0) ? (!bestInd.getBooleanAllele(Math.abs(rl[1].intValue())-1)) : 
      									 		 (bestInd.getBooleanAllele(Math.abs(rl[1].intValue())-1));
      		boolean c = (rl[2].intValue() < 0) ? (!bestInd.getBooleanAllele(Math.abs(rl[2].intValue())-1)) : 
      									 		 (bestInd.getBooleanAllele(Math.abs(rl[2].intValue())-1));
      		weights[i] += 1 - ((a|b|c) ? 1 : 0) ;
      		
      		maxfitness += weights[i];   // voy actualizando maxfitness
      	}
      	ea.setParam(EvolutionaryAlg.PARAM_TARGET_FITNESS,new Double(maxfitness)); // Cambio el fitness del algoritmo
      }
	  
      fitness = 0.0;
      for (int i = 0; i < numclause; i++)
      	fitness += weights[i]*clauseValue[i];
   	  
	  return fitness;
   }
   
   // Calcula el fitness sumando el número de cláusulas que se satisfacen
   public double evalCount(Individual ind)
   {
   	  double fitness = 0.0;
   	  int acum;
	  
	  BinaryIndividual bi = (BinaryIndividual) ind;
	  for(int i = 0; i < numclause; i++)
	  {
	  	Integer rl[] = (Integer[]) clause.get(i);
	    acum = 0;
	    for(int j = 0; (j < lenclause) && (acum != 1);j++)
	    {
	    	if( ((rl[j].intValue() < 0) && (!bi.getBooleanAllele(Math.abs(rl[j].intValue())-1)))
			 || ((rl[j].intValue() > 0) && (bi.getBooleanAllele(rl[j].intValue()-1))))
		 		acum = 1;
	    }
		fitness += acum;
	  }
	  return fitness;
	}
   
   // Eval fitness using Refining Functions
   public double evalREF(Individual ind)
   {
   	   
   	  double fitness = 0.0;
   	     	
   	  final double alpha = 0.5;
   	  
   	  int clauseValue[] = new int[numclause];
   	  
   	  BinaryIndividual bi = (BinaryIndividual) ind;
      for (int i = 0; i < numclause; i++)
      {
      	Integer rl[] = (Integer[]) clause.get(i);
      	boolean a = (rl[0].intValue() < 0) ? (!bi.getBooleanAllele(Math.abs(rl[0].intValue())-1)) : 
      								 		 (bi.getBooleanAllele(Math.abs(rl[0].intValue())-1));
      	boolean b = (rl[1].intValue() < 0) ? (!bi.getBooleanAllele(Math.abs(rl[1].intValue())-1)) : 
      								 		 (bi.getBooleanAllele(Math.abs(rl[1].intValue())-1));
      	boolean c = (rl[2].intValue() < 0) ? (!bi.getBooleanAllele(Math.abs(rl[2].intValue())-1)) : 
      								 		 (bi.getBooleanAllele(Math.abs(rl[2].intValue())-1));
      	clauseValue[i] = ((a|b|c) ? 1 : 0) ;
      	
      	fitness += clauseValue[i];
      }	
      
      BinaryIndividual bestInd = ((BinaryIndividual) ((Population)ea.getParam(EvolutionaryAlg.PARAM_POPULATION)).getIndividual(((Integer)((Statistic) ea.getParam(EvolutionaryAlg.PARAM_STATISTIC)).getStat(ComplexStats.MAX_FIT_POS)).intValue()));
      
      if (((Integer) ea.getParam(EvolutionaryAlg.PARAM_GENERATION_NUMBER)).intValue() == generation)
      {
	      generation++;
	      	      
	      double num = 0;
	      double den = 0;
	      
	      for (int j = 0; j < numvar; j++)
	      {
	      	v[j] -= K(bestInd.getBooleanAllele(j)) * U(j, bestInd); // update v[]
	      	num += K(((BinaryIndividual) ind).getBooleanAllele(j)) * v[j];
	      	den = Math.abs(v[j]);
	      }
	      
	      r = 0.5 * (1 + (num/(1+den)));
	      
	      double maxfitness = numclause + alpha * r;
      	  ea.setParam(EvolutionaryAlg.PARAM_TARGET_FITNESS,new Double(maxfitness)); // Cambio el fitness del algoritmo
      	  //recalculate population
      	  evaluatePopulation((Population)ea.getParam(EvolutionaryAlg.PARAM_POPULATION));
	  }
      
      fitness += alpha * r;
      
      return fitness;
   }
   
   // For EvalREF. K(true) = 1; and K(false) = -1
   private int K(boolean k)
   {
   		return k ? 1 : -1;
   }
   
   // For EvalREF. Returns the cardinality of the set of unsatisfied 
   // clauses having variable u
   private double U(int u, BinaryIndividual bi)
   {
   		u = Math.abs(u);
   		double res = 0.0;
   		
   		for (int i = 0; i < numclause; i++)
   		{
   			Integer rl[] = (Integer[]) clause.get(i);
   			
   			boolean a = (rl[0].intValue() < 0) ? (!bi.getBooleanAllele(Math.abs(rl[0].intValue())-1)) : 
	      								 		 (bi.getBooleanAllele(Math.abs(rl[0].intValue())-1));
	      	boolean b = (rl[1].intValue() < 0) ? (!bi.getBooleanAllele(Math.abs(rl[1].intValue())-1)) : 
	      								 		 (bi.getBooleanAllele(Math.abs(rl[1].intValue())-1));
	      	boolean c = (rl[2].intValue() < 0) ? (!bi.getBooleanAllele(Math.abs(rl[2].intValue())-1)) : 
	      								 		 (bi.getBooleanAllele(Math.abs(rl[2].intValue())-1));
	      
		    // Actualize weights (only for REF+)
		    if ((fitnessFunction==REFPLUS) && (((Integer) ea.getParam(EvolutionaryAlg.PARAM_GENERATION_NUMBER)).intValue() != 0) && 
		   	    (((Integer) ea.getParam(EvolutionaryAlg.PARAM_GENERATION_NUMBER)).intValue() % 2 == 0))
					weights[i] += 1 - ((a|b|c) ? 1 : 0) ;
   			
   			if ((Math.abs(rl[0].intValue() - 1) == u) || 
   			    (Math.abs(rl[1].intValue() - 1) == u) || 
   			    (Math.abs(rl[2].intValue() - 1) == u))
		      		if (!(a|b|c)) res+=weights[i];
   		}
   		return res;
   }
   
   // Evaluate the hole population using fitness function without weights
   // These evaluations do not affect to the total number of evaluations of the algorithm
   public void evalPopNoSaw(Population pop)
   {
   	  double fitness = 0.0;
   	  int acum;
	   	for (int p = 0; p < pop.getPopSize(); p++)
	   	{
	   	  BinaryIndividual bi = (BinaryIndividual)pop.getIndividual(p);
	   	  for(int i = 0; i < numclause; i++)
	      {
	      	Integer rl[] = (Integer[]) clause.get(i);
	      	acum = 0;
	      	for(int j = 0; (j < lenclause) && (acum != 1);j++)
	      	{
	      		if( ((rl[j].intValue() < 0) && (!bi.getBooleanAllele(Math.abs(rl[j].intValue())-1)))
				 || ((rl[j].intValue() > 0) && (bi.getBooleanAllele(rl[j].intValue()-1))))
			 		acum = 1;
	        }
			fitness += acum;
	      }
	      bi.setFitness(new Double(fitness));
	      
	      fitness = 0.0;
	 	}
   }
}
