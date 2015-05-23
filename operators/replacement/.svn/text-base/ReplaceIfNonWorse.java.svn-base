
/**
 * @author Bernabe Dorronsoro
 *
 * Replacement operator. Replaces the current individual in 
 * the population if the individual to insert is not worse 
 * 
 */


package operators.replacement;

import jcell.*;

public class ReplaceIfNonWorse implements Operator
{
   // Parametro array de individuos, devuelve individuo
   public Object execute(Object o)
   {
      Individual iv[] = (Individual[])o;
      
      if (Target.isBetterOrEqual(iv[0], iv[1]))
         return iv[0];
      else
         return iv[1];
   }
}
