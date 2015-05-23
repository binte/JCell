
/**
 * @author Bernabe Dorronsoro
 *
 * Random selection operator. Randomly selects one individual from the neighborhood
 * 
 */
   
package operators.selection;   

import jcell.*;
import java.util.Random;

public class RandomSelection implements Operator
{
   private Random r;
   
   public RandomSelection(Random r)
   {
      this.r = r;
   }
   
   public Object execute(Object o)
   {
      Individual iv[] = (Individual[])o;
      
	  return new Integer(r.nextInt(iv.length));
   }
}
