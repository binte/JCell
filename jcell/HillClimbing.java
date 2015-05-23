/**
 * @author Sergio Romero Leiva
 *
 * Implements a hill climbing generic operator
 * 
 */

package jcell;

// It uses a given problem, and an operator that provides a list of solutions
// which are neighbors in the solution space of the current solution
public class HillClimbing implements Operator
{
   private Problem prob; // Problem
   private Operator local; // Operator that returns some neighbor solutions (in the solutions space)
   private int steps; // Maximum number of steps
   
   public HillClimbing(Problem prob, Operator local, int steps)
   {
      this.prob = prob;
      this.local = local;
      this.steps = steps;
   }
   
   // Evaluates and returns the best individual in iv[]
   private Individual bestIndividual(Individual iv[])
   {
      Individual best;
      
      prob.evaluate(iv[0]);
      best = iv[0];
      for (int i=1; i<iv.length; i++)
      {
         prob.evaluate(iv[i]);
         if (Target.isBetterOrEqual(iv[i].getFitness(), best.getFitness()))
            best = iv[i];
      }
      
      return best;
   }
   
   // Perform the hill climbing method
   // The parameter and the returned values are individuals
   public Object execute(Object o)
   {
      Individual current, best;
      Individual []iv;
      
      current = (Individual)o;
      prob.evaluate(current);
      for (int i=0; i<steps; i++)
      {
         iv = (Individual[])local.execute(current); // get neighbor solutions
         best = bestIndividual(iv); // Get the best neighbor
         if (Target.isBetterOrEqual(best.getFitness(), current.getFitness()))
            current = best; // accept the solution if it is better 
         else
            break; // Stop if the local optimum is found
      }
      
      return current;
   }
}
