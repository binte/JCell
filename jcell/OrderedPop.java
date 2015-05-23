
/**
 * @author Bernabe Dorronsoro
 *
 * 
 * Population with individuals ordered in terms of their fitness values 
 * 
 */


package jcell;

import java.util.Random;
import tools.QuickSort;

public class OrderedPop extends Population
{

	public OrderedPop(int popSize)
   {
	  super(popSize);
   }
   
   public void orderPopulation()
   {
	   QuickSort.quickSort(population, population.length);
   }
   
   private void ReorderPopulation()
   {
	   Individual auxInd = population[0];
	   for (int i=1; i<popSize; i++)
		   if (Target.isWorse(population[i], auxInd))
	    	  population[i-1] = population[i];
	      else
	      {
	    	  population[i] = auxInd;
	    	  break;
	      }
   }
   
   public void setIndividual(int pos, Individual ind)
   {
      population[0] = ind;
      ReorderPopulation();
   }
   
   // Set a random population
   public void setRandomPop(Random r, Individual ind)
   {
      super.setRandomPop(r, ind);
   }
   
   public void setRandomPop(Random r, Individual ind, double maxFitness)
   {
	   super.setRandomPop(r, ind, maxFitness);
   }
}
