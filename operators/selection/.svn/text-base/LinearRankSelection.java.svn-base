
/**
 * @author Sergio Romero
 *
 * Modified by Bernabe Dorronsoro
 *
 * Linear ranking selection operator. It returns an individual from the neighborhood
 * chosen by linear ranking using: Min + (Max - Min)*rank/(n-1) with Max=2 and Min=0
 * 
 */
   
package operators.selection;

import jcell.*;
import java.util.*;

public class LinearRankSelection implements Operator
{
   private Random r;
   
   public LinearRankSelection(Random r)
   {
      this.r = r;
   }
   
   // The parameter is an array of individuals, and it returns only one of them, selected by Linear Ranking
   public Object execute(Object o)
   {
      Individual iv[] = (Individual[])o;
      Individual iCopy[] = new Individual[iv.length];
      int i, len = iv.length;
      double acFit, randValue;
      
      for (i=0; i<iv.length; i++)
         iCopy[i] = iv[i];
         
      Arrays.sort(iCopy,new Comparator() // Order the array in terms of the fitness value
      {
         // Compare the individuals in terms of their fitness values
         public int compare(Object o1, Object o2)
         {
            if (Target.isWorse((Individual)o1, (Individual)o2))
               return -1;
            else
               if (Target.isBetter((Individual)o1, (Individual)o2))
                  return 1;
               else
                  return 0;
         }
      });

      i=0;
      acFit = 0.0;
      randValue = r.nextDouble();
      while (acFit < randValue)
      {
         i++;
         acFit += ((double)2*i)/(len*(len-1));
      }
      
      if (i == iv.length)
      	i = r.nextInt(iv.length);
      
      int x = 0;
      while ((x<iv.length) && (iCopy[i] != iv[x]))
      	x++;
      
      return new Integer(x);
   }
}
