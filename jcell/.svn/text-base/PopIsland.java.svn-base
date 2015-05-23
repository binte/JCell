/**
 * @author Bernabe Dorronsoro
 *
 * Represents a population structured in islands
 * 
 */

package jcell;

public class PopIsland extends Population
{
   private int sizeIslands; 
   private int numberIslands;

   // Creates a new population composed by 'numberIslands' islands 
   // of size 'sizeIslands'
   // The populations of every islands are in the same vector one after the other
   public PopIsland(int numberIslands, int sizeIslands)
   {
      super(numberIslands*sizeIslands);
      this.numberIslands = numberIslands;
      this.sizeIslands = sizeIslands;      
   }

   // Returns the number of islands in the population
   public int getNumberIslands()
   {
      return numberIslands;
   }

   // Change the number of islands in the population
   public int getSizeIslands()
   {
      return sizeIslands;
   }

   // islands are numbered from 0 to sizeIslands-1
   public Individual getIndividual(int island, int location)
   {
      return population[island*sizeIslands+location];
   }

   public void setIndividual(int island, int location, Individual ind)
   {
      setIndividual(island*sizeIslands+location,ind);
   }
   
   // Get the population of island 'island'
   public Individual[] getIndividuals(int island)
   {
	   Individual[] list = new Individual[sizeIslands];
	   for (int i=0; i<sizeIslands; i++)
		   list[i] = this.getIndividual(island, i);
	   
      return list;
   }
   
   // Copies the contents of pop in this population
   public void copyPop(PopIsland pop)
   {
   	super.copyPop(pop);
   	this.numberIslands = pop.numberIslands;
   	this.sizeIslands = pop.sizeIslands;
   }
}
