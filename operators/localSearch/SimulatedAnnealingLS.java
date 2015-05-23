
/**
 * @author Sergio Romero
 * 
 * Modified by Bernabe Dorronsoro
 *
 * Defines a Simulated Annealing local search operator for JCell
 * 
 */

package operators.localSearch; 

import jcell.*;
import java.util.Random;



public class SimulatedAnnealingLS implements Operator
{
   private Random r;
   private Problem prob; //Problem
   private Operator suc; // this operator returns a neighbor solution
   private int steps; // for modifying the temperature value
   private double tmax, tmin, coolingRate; //temp max, min and cooling rate
   
   public SimulatedAnnealingLS(Random r, Problem prob, Operator suc, int steps, double tmax,
      double tmin, double coolingRate)
   {
      this.r = r;
      this.prob = prob;
      this.suc = suc;
      this.steps = steps;
      this.tmax = tmax;
      this.tmin = tmin;
      this.coolingRate = coolingRate;
   }
   
   public Object execute(Object o)
   {
      Individual current, next;
      double temp, diff;
      
      current = (Individual)o;
      prob.evaluate(current);
      temp = tmax;
      while (temp > tmin)
      {
         for (int k=0; k<steps; k++)
         {
            next = (Individual)suc.execute(current); // get a neighbor solution
            prob.evaluate(next); // evaluate it
            if (Target.isBetterOrEqual(next.getFitness(), current.getFitness()))
               current = next; // Accept it if it is better solution
            else
            {
               
               if (Target.maximize)
               	diff = ((Double)current.getFitness()).doubleValue() - ((Double)next.getFitness()).doubleValue();
               else 
               	diff = ((Double)next.getFitness()).doubleValue() - ((Double)current.getFitness()).doubleValue();
               
               diff /= temp;
               if (r.nextDouble() < Math.exp(diff))
                  current = next; // if it is worse, it is accepted with a given probability 
            }
         }
         temp *= coolingRate;
      }
      
      return current;
   }
}
