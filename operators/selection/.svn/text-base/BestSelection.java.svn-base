/**
 * @author Bernabe Dorronsoro
 *
 * Best selection operator. It returns the best individual in the neighborhood
 * 
 */
   
package operators.selection;

import jcell.*;

public class BestSelection implements Operator
{
   // Asume como parametro array de Individuos
   // Devuelve individuo
   public Object execute(Object o)
   {
      Individual iv[] = (Individual[])o;
      Individual ind = iv[0];
      
      for (int i=1; i<iv.length; i++)
         if (Target.isBetterOrEqual(iv[i], ind))
            ind = iv[i];
      
      return ind;
   }
}
