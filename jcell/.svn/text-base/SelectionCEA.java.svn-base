
/**
 * @author Bernabe Dorronsoro
 *
 * cGA for measuring the takeover time. It does not use operators (only selection) and only works with TakeoverProblem 
 * 
 */

package jcell;

import problems.TakeoverProblem;
import java.util.Random;
import java.awt.Point;

public class SelectionCEA extends CellularGA
{
   private double maxFitness;

   public SelectionCEA(Random r)
   {
      super(r);
   }

   public void setMaxFitness(double maxFitness)
   {
      this.maxFitness = maxFitness;
   }

   public double getMaxFitness()
   {
      return maxFitness;
   }


   public int getNumBest()
   {
      int i, mejIndivs = 0;
      for (i=0; i<population.getPopSize(); i++)   
         if (((Double)population.getIndividual(i).getFitness()).doubleValue() == maxFitness)
            mejIndivs++;

      return mejIndivs;
   }

   public void experiment()
   {
      double optimum; // Best fitness value in the population
      Point neighPoints[]; // Points in the neighborhood
      Operator oper;
      Individual iv[] = new Individual[2];
      Individual neighIndivs[] = new Individual[neighborhood.getNeighSize()];
      PopGrid auxPop = new PopGrid(((PopGrid)population).getDimX(),((PopGrid)population).getDimY());
      LineSweep ls = new LineSweep((PopGrid)population);
      
      problem.reset();
      problem.evaluatePopulation(population);
      statistic.calculate(population);
      
      iv[0] = population.getIndividual(((Integer)statistic.getStat(SimpleStats.MAX_FIT_POS)).intValue());
      iv[0].setFitness(((Double)iv[0].getFitness()).doubleValue()+1.0); // Set only one best fitness value 

      generationNumber = 0;
      listener.generation(this);
      
      while ((getNumBest()<population.getPopSize()) && (generationNumber < generationLimit))
      {
         for (int k=0; k<population.getPopSize(); k++)
         {
            selectedCell = ls.nextCell(); // next cell
            neighPoints = neighborhood.getNeighbors(selectedCell);
            ((PopGrid) population).getFromPoints(neighPoints,neighIndivs);
            
            // First parent selection
            oper = (Operator)operators.get("selection1");
            iv[0] = (Individual)oper.execute(neighIndivs);
            
            // Second Parent selection
            oper = (Operator)operators.get("selection2");
            if (oper != null)
            {
               neighPoints = neighborhood.getNeighbors(selectedCell);
               ((PopGrid)population).getFromPoints(neighPoints,neighIndivs);
               iv[1] = (Individual)oper.execute(neighIndivs);
            }
            
            // No Recombination
            
            // No Mutation
            
            // No local search
            
            // Put the best parent in iv[0]
            if (Target.isBetter(iv[1], iv[0]))
            	iv[0] = iv[1];
            
            // Put the selected cell in iv[1]
            iv[1] = ((PopGrid)population).getIndividual(selectedCell);
            
            // Replacement
            oper = (Operator)operators.get("replacement");
            iv[0] = (Individual)oper.execute(iv);
            
           if (synchronousUpdate)
				auxPop.setIndividual(selectedCell,iv[0]);
			else
			{
				((PopGrid)population).setIndividual(selectedCell,iv[0]);
			}
         }
         
         population.copyPop(auxPop);
         statistic.calculate(population);
         generationNumber++;
         listener.generation(this);
      }
   }
}
