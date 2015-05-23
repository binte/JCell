/**
 * @author Bernabe Dorronsoro
 *
 * Suitable for permutations
 * 
 * Inserts an allele into a different location
 * 
 */

package operators.mutation;
import java.util.Random;

import jcell.*;

public class InsertionMutation implements Operator
{
    private Random r;
    private EvolutionaryAlg ea;
   
    public InsertionMutation(Random r, EvolutionaryAlg ea)
    {
       this.r = r;
       this.ea = ea;
    }
    
   // Parameter Individual, returns Individual
   public Object execute(Object o)
   {
   	   PermutationIndividual ind = (PermutationIndividual)o;
       int len = ind.getLength(); //Length of individual --> Number of elements in the chromosome
	   
       double prob = ((Double)ea.getParam(CellularGA.PARAM_ALLELE_MUTATION_PROB)).doubleValue();
	   
       for (int i=0; i<len; i++)
    	   if (r.nextDouble() <= prob)
    	   {
	
    		   int r1 = i;
		       int r2 = 0;
		       do
		       {
		    	   r2 = r.nextInt(len);
		       } while (r1==r2);
		
		       ind.relocate(r1, r2);
	       }
  
       return ind;
   }
}
