/**
 * @author Sergio Romero
 * 
 * Modified by Bernabe Dorronsoro
 *
 * Defines a general mutation operator for integer chromosome.
 * 
 */

package operators.mutation;
   
import jcell.*; // uses jcell package

import java.awt.Point;
import java.util.Random;

public class IntegerMutation implements Operator
{
   private Random r;
   private EvolutionaryAlg ea;
   
   public IntegerMutation(Random r, EvolutionaryAlg ea)
   {
      this.r = r;
      this.ea = ea;
   }

	public Object execute(Object o) {
		
		return execute(o, null);
	}
	
   
   // Parameter Individual, returns Individual
	   public Object execute(Object o, Point p)
	   
	   {
	  IntegerIndividual ind = (IntegerIndividual)o;
      int len = ind.getLength(); //Length of individual
      double prob = ((Double)ea.getParam(CellularGA.PARAM_ALLELE_MUTATION_PROB)).doubleValue(); 
      
      for (int i=0; i<len; i++)
         if (r.nextDouble() <= prob)
            ind.mutate(r,i); //Mutate gene i with probability 1/len
      
      return ind;
   }
}
