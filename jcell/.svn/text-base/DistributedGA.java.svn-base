/**
 * @author Bernabe Dorronsoro
 * 
 * Base class for the islands GA (only the sequential version is implemented)
 * 
 */

package jcell;

import java.util.Random;

public class DistributedGA extends EvolutionaryAlg
{

	public int[] bestInds ;
   public int[] worstInds;
   
   public DistributedGA(Random r)
   {
   	  super(r);
      
   }
   
   // Returns the islands composing the population
   public PopIsland getPopulation()
   {
   	return (PopIsland)population;
   }
   
   // Run generationLimit generations (or evaluationLimit evaluations) of a cEA
   public void experiment()
   {
	   PopIsland population = (PopIsland) super.population;
   		
	   //	 Are we executing a Multi-Target Problem?
	   boolean multiobjective = problem.numberOfObjectives() > 1;
      
	   double optimum; // Best fitness in the population
      Operator oper;
      Individual iv[] = new Individual[2]; // Used for recombination
	  Integer ind[] = new Integer[2];      // Used for avoiding to select the same individual twice as parent 
      
	  bestInds = new int[population.getNumberIslands()];
	  worstInds = new int[population.getNumberIslands()];
	  
	  problem.reset(); // Set evaluations to 0
      problem.evaluatePopulation(population);
      int islands = population.getNumberIslands();
      int islandSize = population.getSizeIslands();
      
      // Get the positions of the best and worst individuals for each sub-population
      for (int i=0; i<islands; i++)
      {
    	  // Initially the first individual of each population is considered to be the best and the worst one
    	  worstInds[i] = 0;
    	  bestInds[i] = 0;
    	  for(int j=1; j<islandSize; j++)
    	  {
    		  if (Target.isBetter(population.getIndividual(i, j), population.getIndividual(i, bestInds[i])))
    			  bestInds[i] = j;
    		  if (Target.isWorse(population.getIndividual(i, j), population.getIndividual(i, worstInds[i])))
    			  worstInds[i] = j;
    	  }
      }

      if (multiobjective)
        	paretoFront.initialize(population);
      else
      {
        	statistic.calculate(population);
        
        	if (Target.maximize)
        		optimum = ((Double)statistic.getStat(SimpleStats.MAX_FIT_VALUE)).doubleValue();
        	else optimum = ((Double)statistic.getStat(SimpleStats.MIN_FIT_VALUE)).doubleValue();
        	if (Target.isBetterOrEqual(optimum, targetFitness))
           return; // Stop if the best solution is found
        }
   
      generationNumber = 0;
      listener.generation(this);
       
      while ((problem.getNEvals() < evaluationLimit) && (generationNumber < generationLimit))
      {
    	    // Apply the breeding loop in every island (elitist ssGAs run in the islands)
      		for (int i=0; i<islands; i++) // Generate a new individual in each sub-population
      		{
	    	    //First father selection
				oper = (Operator)operators.get("selection1");
				ind[0] = (Integer)oper.execute(population.getIndividuals(i));
				
				iv[0] = (Individual)population.getIndividual(i,ind[0].intValue()).clone();
	
				//Second parent selection
				oper = (Operator)operators.get("selection2");
				if (oper != null)
			    {
					ind[1] = (Integer)oper.execute(population.getIndividuals(i));
					while (ind[0].intValue() == ind[1].intValue())
					{
					    // It is not allowed the same parent to be selected twice
					    // with this population size
					    ind[1] = (Integer)oper.execute(population.getIndividuals(i));
					}
					iv[1] = (Individual)population.getIndividual(i,ind[1].intValue()).clone();
			    }
	            
	            //Recombination
	            oper = (Operator)operators.get("crossover");
	            if (oper != null)
	               if (r.nextDouble() < crossoverProb)
	                  iv[0] = (Individual)oper.execute(iv);
	            
	            //Mutation
	            oper = (Operator)operators.get("mutation");
	            if (oper != null)
	               if (r.nextDouble() < mutationProb)
	                  iv[0] = (Individual)oper.execute(iv[0]);
	
	            // Local search
	            oper = (Operator)operators.get("local");
	            if (oper != null)
	               if (r.nextDouble() < localSearchProb)
	                  iv[0] = (Individual)oper.execute(iv[0]);
	               else problem.evaluate(iv[0]);
	            else problem.evaluate(iv[0]);
	            
	            oper = (Operator)operators.get("replacement");
	            iv[1] = population.getIndividual(i, worstInds[i]); // Replace the worst individual in pop. i
	            iv[0] = (Individual)oper.execute(iv);
	            
	            // if we are in the multiobjective case, insert the new solution into the archive
	            // if it dominates or it is non-dominated with respect to the worst individual in pop. i
	            if (multiobjective && (iv[0].dominanceTest(iv[1]) >= 0)) paretoFront.Insert((Individual)iv[0].clone());
	            
	            if (iv[0] != iv[1]) // The individual in the population has to be replaced
	            {
	            	population.setIndividual(i,worstInds[i], iv[0]);
	            	// update best and worst individuals for this population
	            	for(int j=0; j<islandSize; j++)
		          	  {
		          		  if (Target.isBetter(population.getIndividual(i, j), population.getIndividual(i, bestInds[i])))
		          			  bestInds[i] = j;
		          		  if (Target.isWorse(population.getIndividual(i, j), population.getIndividual(i, worstInds[i])))
		          			  worstInds[i] = j;
		          	  }
	            }
	            
      		}
      		
      		// Migrate individuals
      		if (problem.getNEvals() % migrationFreq == 0)
      		{
      			int i = 0;
      			for (i=0; i<population.getNumberIslands();i++)
      			{
      				int nextPop = (i+1)%population.getNumberIslands();
      				population.setIndividual(nextPop, worstInds[nextPop], population.getIndividual(i, bestInds[i]));
      				
      				// Recompute the best and worst individuals in nextPop
      				for(int j=0; j<islandSize; j++)
		          	{
		          		  if (Target.isBetter(population.getIndividual(nextPop, j), population.getIndividual(nextPop, bestInds[nextPop])))
		          			  bestInds[nextPop] = j;
		          		  if (Target.isWorse(population.getIndividual(nextPop, j), population.getIndividual(nextPop, worstInds[nextPop])))
		          			  worstInds[nextPop] = j;
		          	}
      			}
      		}
      		
            if (!multiobjective)
            {
            	statistic.calculate(population);
	   	         if (Target.maximize)
	   	         	optimum = ((Double)statistic.getStat(SimpleStats.MAX_FIT_VALUE)).doubleValue();
	   	         else optimum = ((Double)statistic.getStat(SimpleStats.MIN_FIT_VALUE)).doubleValue();
	   	         if (Target.isBetterOrEqual(optimum, targetFitness))
	   	            return; // Stop if the best solution is found
            }
 
         if (problem.getNEvals()%population.getPopSize() == 0)
         {
         	generationNumber++;
         	listener.generation(this);
         }
      }
   }

}
