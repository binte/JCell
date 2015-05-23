
/**
 * @author Sergio Romero
 * 
 * Modified by Bernabe Dorronsoro
 *
 * Define a general mutation operator for floating point for JCell
 * 
 * We execute a mutation on an Individual, we mutate each allele 
 * in the chromosome with non-uniform probability
 * 
 */

package operators.mutation;   
import java.util.Random;

import jcell.CellularGA;
import jcell.EvolutionaryAlg;
import jcell.Operator;
import jcell.RealIndividual;

public class FloatNonUniformMutation implements Operator
{
    private Random r;
    private EvolutionaryAlg ea;
   
   public FloatNonUniformMutation(Random r, EvolutionaryAlg ea)
   {
      this.r = r;
      this.ea = ea;
   }
   
   // Parameter Individual, returns Individual
   public Object execute(Object o)
   {

       Object o2;

       RealIndividual ri = (RealIndividual)o;
       int len = ri.getLength(); //Length of individual
       
       int gn; // Number of generation
       int gnLimit; // Number of generation (the limit)
       double prob; //Define probability of mutation
      
       o2 = ea.getParam(CellularGA.PARAM_GENERATION_NUMBER);
       gn = ((Integer)o2).intValue();

       o2 = ea.getParam(CellularGA.PARAM_GENERATION_LIMIT);
       gnLimit = ((Integer)o2).intValue();

       prob = ((Double)ea.getParam(CellularGA.PARAM_ALLELE_MUTATION_PROB)).doubleValue();

      for (int i=0; i<len; i++)
         if (r.nextDouble() < prob)
            ri.nuMutate(r,i,gn,gnLimit); //Mutate gene i with probability 1/len
      
      return ri;
   }
}
