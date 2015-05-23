
/**
 * @author Bernabe Dorronsoro
 *
 * Probabilistic crossover. Takes the best parent gene 
 * with probability 'pBias'
 * 
 */

package operators.recombination;

import java.util.Random;
import jcell.Target;
import jcell.Individual;
import jcell.Operator;

public class Px implements Operator
{
   private double pBias;
   private Random r;
   
   public Px(Random r)
   {
      this(r,0.5);
   }
   
   public Px(Random r, double pBias)
   {
      this.pBias = pBias;
      this.r = r;
   }
   
   public void setpBias(double pBias)
   {
   	this.pBias = pBias;
   }
   
   public double getpBias()
   {
   	return pBias;
   }
      
   public Object execute(Object o)
   {
      Individual iv[] = (Individual[])o;
      Individual i1, i2, auxInd, newInd;
      int i, len = iv[0].getLength();
      
      newInd = (Individual)iv[0].clone();
      i1 = iv[0];
      i2 = iv[1];
      
      if (!Target.isBetterOrEqual(i1, i2))
      {
         auxInd = i1;
         i1 = i2;
         i2 = auxInd;
      }
      
      for (i=0; i<len; i++)
         if (r.nextDouble() < pBias)
            newInd.setAllele(i,i1.getAllele(i));
         else
            newInd.setAllele(i,i2.getAllele(i));
      
      return newInd;
   }
}
