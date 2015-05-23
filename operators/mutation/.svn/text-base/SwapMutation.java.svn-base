
/**
 * @author Bernabe Dorronsoro
 *
 * Mutation for permutation representation. Swaps the place of two genes
 * 
 */
   
package operators.mutation;
import java.util.Random;

import jcell.*;

public class SwapMutation implements Operator
{
    private Random r;
    private EvolutionaryAlg ea;
   
    public SwapMutation(Random r, EvolutionaryAlg ea)
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
		
		       int r1 = r.nextInt(len);
		       int r2 = 0;
		       do
		       {
		    	   r2 = r.nextInt(len);
		       } while (r1==r2);
		
		       ind.swap(r1, r2);
	       }

       return ind;
   }
}
