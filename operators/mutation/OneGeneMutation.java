/**
 * @author Sergio Romero
 *
 * Applies the default mutation of the individual
 * 
 */

package operators.mutation;

import java.util.Random;

import jcell.Individual;
import jcell.Operator;

public class OneGeneMutation implements Operator
{
   private Random r;
   
   public OneGeneMutation(Random r)
   {
      this.r = r;
   }
   
   public Object execute(Object o)
   {
      Individual ind = (Individual)o;
      
      ind.mutate(r,r.nextInt(ind.getLength())); // Mutate one random gene
      return ind;
   }
}
