
/**
 * @author Sergio Romero
 *
 * Replacement operator. Replaces the current individual in 
 * the population if the individual to insert is better 
 * 
 */

package operators.replacement;

import jcell.*;

public class ReplaceIfBetter implements Operator
{

   public Object execute(Object o)
   {
      Individual iv[] = (Individual[])o;
      
      if (Target.isBetter(iv[0], iv[1]))
         return iv[0];
      else
         return iv[1];
   }
}
