
/**
 * @author Bernabe Dorronsoro
 *
 * Single point crossover. It keeps the largest part of the best parent
 * 
 */

package operators.recombination;

import java.util.Random;
import jcell.Target;
import jcell.Individual;
import jcell.Operator;

public class Spx implements Operator
{
   private Random r;
   
   public Spx(Random r)
   {
      this.r = r;
   }
   
   public Object execute(Object o)
   {
      Individual iv[] = (Individual[])o;
      Individual i1, i2, auxInd, newInd;
      int i, cut, len = iv[0].getLength();
      
      newInd = (Individual)iv[0].clone();
      i1 = iv[0];
      i2 = iv[1];
      cut = r.nextInt(len);
      
      if (!Target.isBetterOrEqual(i1, i2))
      {
         auxInd = i1;
         i1 = i2;
         i2 = auxInd;
      }
      if (cut < len/2)
      {
         auxInd = i1;
         i1 = i2;
         i2 = auxInd;
      }
      
      for (i=0; i<cut+1; i++)
         newInd.setAllele(i,i1.getAllele(i));
      for (i=cut+1; i<len; i++)
         newInd.setAllele(i,i2.getAllele(i));
      
      return newInd;
   }
}
