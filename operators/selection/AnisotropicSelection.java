/**
 * @author Bernabe Dorronsoro
 *
 * Anisotropic selection operator. It considers different probabilities for choosing the NS and EW
 * individuals from the neighborhood. It only can be used with NEWS (or Linbear5) neighborhood
 * 
 */
   

package operators.selection;

import jcell.*;
import java.util.Random;

public class AnisotropicSelection implements Operator
{
   // The input parameter is an array of individuals
   // It returns the selected individual
	
	private double alpha = 0.0;
	
	private Random r;
	   
	public AnisotropicSelection(Random r)
	{
	   this.r = r;
	}
	
	public AnisotropicSelection(Random r, double val)
	{
	   this.r = r;
	   alpha = val;
	}
	
	// Once we decided whether to select in the column (NS) or row (EW), one of the neighbors in this column/row is randomly selected
   public Object execute(Object o)
   {
      Individual iv[] = (Individual[])o;
      Integer ind = new Integer(0);
      double randValue;
      
      // The order of individuals in the neighborhood list iv[] is: NWSE
      
      randValue = r.nextDouble();
      
      if (randValue > 1.0/iv.length) // if randValue <= 0.2 return the center individual, else:
      {
    	  if (randValue <= 2*(0.2*(1+alpha))+0.2) // individual at north or south
    	  {
    		  if (r.nextDouble() <= 0.5)
    			  ind = new Integer(1); // individual at east position
    		  else
    			  ind = new Integer(3); // Individual at west position
    	  }
    	  else
    	  { 
    		  if (r.nextDouble() <= 0.5)
    			  ind = new Integer(2); // Individual at north position
    		  else
    			  ind = new Integer(4); // Individual at south position
    	  }    	  
      }

     return ind;
   }
   
   public double getAlpha()
   {
	   return alpha;
   }
   
   public void setAlpha(double val)
   {
	   alpha = val;
   }
}
