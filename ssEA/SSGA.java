/**
 * @author Bernabe Dorronsoro
 *
 * Implementation of a steady-state GA in JCell
 * 
 */

package ssEA;

import java.util.Random;

import jcell.*;

public class SSGA extends EvolutionaryAlg
{
   public SSGA(Random r, int genLimit)
   {
      super(r, genLimit);
   }
   
   public void experiment()
   {
	   boolean multiobjective = problem.numberOfObjectives() > 1;
	   
	   double optimum = 0.0; 
      Operator oper;
      Individual iv[] = new Individual[2]; // used for recombination
  	  Integer ind[] = new Integer[2];      // For avoiding the same individual to be selected twice
 
      
      this.problem.reset(); // Set evaluations to 0
      
      this.problem.evaluatePopulation(population);
            
      int worst = 0;
      if (multiobjective)
      {
    	  paretoFront.initialize(population);
	      // Get the position of the worst individual in the population
	      for (int i=1; i<population.getPopSize(); i++)
	    	if (Target.isWorse(population.getIndividual(i), population.getIndividual(worst)))
	   			  worst = i;
      }
	  else
	  {
      	statistic.calculate(population);
      
	  	if (Target.maximize)
	  		optimum = ((Double)statistic.getStat(SimpleStats.MAX_FIT_VALUE)).doubleValue();
	  	else optimum = ((Double)statistic.getStat(SimpleStats.MIN_FIT_VALUE)).doubleValue();
	  	if (Target.isBetterOrEqual(optimum, targetFitness))
	    return; 
	  }

      generationNumber = 0;
      listener.generation(this);
      
      while ((problem.getNEvals() < evaluationLimit) && (generationNumber < generationLimit))
      {

      		//First parent selection
			oper = (Operator)operators.get("selection1");
			ind[0] = (Integer)oper.execute(population.getIndividuals());
			
			iv[0] = (Individual)population.getIndividual(ind[0].intValue()).clone();

			//Second parent selection
			oper = (Operator)operators.get("selection2");
			if (oper != null)
		    {
				ind[1] = (Integer)oper.execute(population.getIndividuals());
				while (ind[0].intValue() == ind[1].intValue())
				{
				    // It is not allowed the same parent to be selected twice
				    // with this population size
				    ind[1] = (Integer)oper.execute(population.getIndividuals());
				}
				iv[1] = (Individual)population.getIndividual(ind[1].intValue()).clone();
		    }
            
            //recombination
            oper = (Operator)operators.get("crossover");
            if (oper != null)
               if (r.nextDouble() < crossoverProb)
                  iv[0] = (Individual)oper.execute(iv);
            
            //Mutation
            oper = (Operator)operators.get("mutation");
            if (oper != null)
               if (r.nextDouble() < mutationProb)
                  iv[0] = (Individual)oper.execute(iv[0]);

            //Local search
            oper = (Operator)operators.get("local");
            if (oper != null)
               if (r.nextDouble() < localSearchProb)
                  iv[0] = (Individual)oper.execute(iv[0]);
               else problem.evaluate(iv[0]);
            else problem.evaluate(iv[0]);
            
            // Replace the worst individual with the new one
            oper = (Operator)operators.get("replacement");
            if ((!multiobjective) && (Target.maximize))
            	worst = ((Integer) statistic.getStat(SimpleStats.MIN_FIT_POS)).intValue();
            else if ((!multiobjective) && (!Target.maximize))
            	worst = ((Integer) statistic.getStat(SimpleStats.MAX_FIT_POS)).intValue();
            
            iv[1] =  population.getIndividual(worst);
            iv[0] = (Individual)oper.execute(iv);
            
            population.setIndividual(worst, iv[0]);            
            
            // if we are in the multiobjective case, insert the new solution into the archive
            // if it dominates or it is non-dominated with respect to the worst individual in pop. i
            if (multiobjective && (iv[0].dominanceTest(iv[1]) >= 0)) paretoFront.Insert((Individual)iv[0].clone());
            
            if (multiobjective && (iv[0] != iv[1])) // Replaced individual
            {
            	// update worst individual in the population
            	for(int j=0; j<population.getPopSize(); j++)
          		  if (Target.isWorse(population.getIndividual(j), population.getIndividual(worst)))
	          		  worst = j;
            }

            if (!multiobjective)
            {
            	statistic.calculate(population);
            	if (Target.maximize)
    	         	optimum = ((Double)statistic.getStat(SimpleStats.MAX_FIT_VALUE)).doubleValue();
    	         else optimum = ((Double)statistic.getStat(SimpleStats.MIN_FIT_VALUE)).doubleValue();
    	         if (Target.isBetterOrEqual(optimum, targetFitness))
    	            return; 
            }
 
         //if (verbose)
        //	 System.out.println("Evaluaciones: " + problem.getNEvals() + " Best fitness: " + optimum + " Worst fitness: " + ((Double)statistic.getStat(SimpleStats.MIN_FIT_VALUE)).doubleValue());
         
            if (problem.getNEvals()%population.getPopSize() == 0)
            {
            	generationNumber++;
                listener.generation(this);
            }
      }
   }
}
