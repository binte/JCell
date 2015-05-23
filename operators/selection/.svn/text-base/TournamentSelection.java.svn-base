/**
 * @author Bernabe Dorronsoro
 *
 * Tournament selection operator 
 * 
 */


package operators.selection;

import jcell.*;
import java.util.Random;

public class TournamentSelection implements Operator
{
   private Random r;
   
   public TournamentSelection(Random r)
   {
      this.r = r;
   }
   
   // Parametro array de individuos, devuelve individuo
   public Object execute(Object o)
   {
      Integer ind1, ind2;
      Individual iv[] = (Individual[])o;
	   ind1 = new Integer(r.nextInt(iv.length));
      Individual i1 = iv[ind1.intValue()];
	   ind2 = new Integer(r.nextInt(iv.length));
      Individual i2 = iv[ind2.intValue()];
      
      if (Target.isBetterOrEqual(i1, i2))
         return ind1;
      else
         return ind2;
   }
}
