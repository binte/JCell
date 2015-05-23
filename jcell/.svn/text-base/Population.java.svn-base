
/**
 * @author Sergio Romero Leiva
 * 
 * Modified by Bernabe Dorronsoro
 *
 * Represents a population of individuals
 * 
 */

package jcell;

import java.util.Random;

public class Population
{
   protected Individual population[]; // Individuals in the population
   protected int popSize; // Number of individuals in the population
   
   // Create a new population of size 'popSize'
   public Population(int popSize)
   {
      population = new Individual[popSize];
      this.popSize = popSize;
   }
   
   // returns the individual placed in 'pos' in the list
   public Individual getIndividual(int pos)
   {
      return population[pos];
   }
   
   // Get the list of all the individuals composing the population
   public Individual[] getIndividuals()
   {
      return population;
   }
   
   // Returns the size of the population
   public int getPopSize()
   {
      return popSize;
   }
   
   // Change the size of the population
   public void setPopSize(int popSize)
   {
      population = new Individual[popSize];
      this.popSize = popSize;
   }
   
   // Place individual 'ind' in location 'pos'
   public void setIndividual(int pos, Individual ind)
   {
      population[pos] = ind;
   }
   
   // Build a random population of individuals with the same genotype as 'ind'
   public void setRandomPop(Random r, Individual ind)
   {
      for (int i=0; i<popSize; i++)
      {
         population[i] = (Individual)ind.clone();
         population[i].setRandomValues(r);
      }
   }
   
   public void setRandomPop(Random r, Individual ind, double maxFitness)
   {

      for (int i=0; i<popSize; i++)
      {
         population[i] = (Individual)ind.clone();
         do
         {
            population[i].setRandomValues(r);
         } while ((double) ((BinaryIndividual) population[i]).binaryToDecimal(0,10) == maxFitness);
      }
      
      int indiv = r.nextInt(popSize-1);
      for (int j=0; j<ind.len;j++)
         population[indiv].setAllele(j,new Boolean(true));
   }

   // Copies the contents of pop in this population
   public void copyPop(Population pop)
   {
   	  setPopSize(pop.getPopSize());
      for (int i=0; i<popSize; i++)
         setIndividual(i,(Individual)pop.getIndividual(i).clone());
   }
}
