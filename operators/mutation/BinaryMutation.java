 /**
 * @author Sergio romero
 * 
 * Modified by Bernabe Dorronsoro
 *
 * Define a general mutation operator for binary chromosome.
 * Uses the jcell interface Operator.
 * 
 */

 
package operators.mutation;
   
import jcell.*; // uses jcell package

import java.awt.Point;
import java.util.Random;

public class BinaryMutation implements Operator
{
   private Random r;
   private EvolutionaryAlg ea;
   
   public BinaryMutation(Random r, EvolutionaryAlg ea)
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

	  BinaryIndividual bi = (BinaryIndividual)o;
      int len = bi.getLength(); //Length of individual
      double prob = ((Double)ea.getParam(CellularGA.PARAM_ALLELE_MUTATION_PROB)).doubleValue(); 
      
      for (int i=0; i<len; i++)
         if (r.nextDouble() <= prob)
            bi.mutate(r,i); //Mutate gene i with probability 'prob'
      
      return bi;
   }
}
