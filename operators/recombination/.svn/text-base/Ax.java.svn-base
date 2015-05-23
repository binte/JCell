
/**
 * @author Sergio romero
 *
 * Aritmetic crossover for double-type genotype individuals (Michalewicz, 92)
 * 
 */

package operators.recombination;

import jcell.*;

public class Ax implements Operator
{
   protected double pBias; //Inclinacion al mejor individuo
   
   public Ax()
   {
      this(0.5);
   }
   
   public Ax(double pBias)
   {
      this.pBias = pBias;
   }
   
   public void setpBias(double pBias)
   {
   	this.pBias = pBias;
   }
   
   public double getpBias()
   {
   	return pBias;
   }
   
   // Parametro array de individuos devuelve individuo
   public Object execute(Object o)
   {
      Individual iv[] = (Individual[])o;
      RealIndividual i1, i2, auxInd, newInd;
      int len = iv[0].getLength();
      
      i1 = (RealIndividual)iv[0];
      i2 = (RealIndividual)iv[1];
      newInd = (RealIndividual)i1.clone();
      if (Target.isBetterOrEqual(i1, i2))
      {
         auxInd = i1;
         i1 = i2;
         i2 = auxInd;
      }
      // i1 apunta al mejor individuo
      for (int i=0; i<len; i++)
         newInd.setRealAllele(i,
            i1.getRealAllele(i)*pBias +
            i2.getRealAllele(i)*(1 - pBias));
            
      return newInd;
   }
}
