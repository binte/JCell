
/**
 * @author Sergio Romero
 * 
 * Modified by Bernabe Dorronsoro
 *
 * Define a general mutation operator for floating point for JCell
 * 
 * We execute a mutation on an Individual, we mutate each allele 
 * in the chromosome with an equal probability
 * 
 */

package operators.mutation;
import java.util.Random;

import jcell.CellularGA;
import jcell.EvolutionaryAlg;
import jcell.Operator;
import jcell.RealIndividual;

public class FloatUniformMutation implements Operator
{
    private Random r;
    private EvolutionaryAlg ea;
   
   public FloatUniformMutation(Random r,EvolutionaryAlg ea)
   {
      this.r = r;
      this.ea = ea;
   }
   
   // Parameter Individual, returns Individual
   public Object execute(Object o)
   {
       RealIndividual ri = (RealIndividual)o;
       int len = ri.getLength(); //Length of individual --> Number of elements in the chromosome

       double prob; //Define probability of mutation

       prob = ((Double)ea.getParam(CellularGA.PARAM_ALLELE_MUTATION_PROB)).doubleValue();
             
       for (int i=0; i<len; i++)
	   if (r.nextDouble() < prob)
	       ri.mutate(r,i); //Mutate gene i
      
      return ri;
   }
}
