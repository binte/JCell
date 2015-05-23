/**
 * @author Stefan Janson
 *
 * Performs a swap between two individuals in the population
 *  
 */
   
package HcGA;

import jcell.*;
import java.awt.Point;

public class Swap implements Operator
{

	public Object execute(Object o) {
		return execute(o, null);
	}
	
	// Parameter array of 2 individuals and a position
   public Object execute(Object o, Point p)
   {
      Individual iv[] = (Individual[])o;
	  
	  //the decision whether to swap is taken outside
      Individual tmp = iv[0];
	  iv[0]=iv[1];
	  iv[1]=tmp;
      
	  return iv;
   }
}
