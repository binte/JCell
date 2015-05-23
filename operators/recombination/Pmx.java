/**
 * @author Bernabe Dorronsoro
 *
 * PMX recombination, for integer genotype individuals
 * 
 */

package operators.recombination;

import java.util.Random;
import jcell.Target;
import jcell.Individual;
import jcell.Operator;
import jcell.PermutationIndividual;

public class Pmx implements Operator
{
   private int intv[]; 
   private Random r;
   
   public Pmx(Random r)
   {
      this.r = r;
   }
   
   public Object execute(Object o)
   {
      Individual iv[] = (Individual[])o;
      PermutationIndividual i1, i2, auxInd, newInd;
      int n, m, i, cut1, cut2, len = iv[0].getLength();
      
      newInd = (PermutationIndividual)iv[0].clone();
      if (intv == null || intv.length != len)
         intv = new int[len]; 
      for (i=0; i<len; i++)
         intv[i] = -1;
         
      i1 = (PermutationIndividual)iv[0];
      i2 = (PermutationIndividual)iv[1];
      cut1 = r.nextInt(len);
      cut2 = r.nextInt(len);
      
      if (cut1 > cut2)
      {
         i = cut1;
         cut1 = cut2;
         cut2 = i;
      }
      
      if (!Target.isBetterOrEqual(i1, i2))
      {
         auxInd = i1;
         i1 = i2;
         i2 = auxInd;
      }
      if ((cut2 - cut1) > len/2)
      {
         auxInd = i1;
         i1 = i2;
         i2 = auxInd;
      }
      for (i=cut1+1; i<cut2+1; i++)
      {
         newInd.setIntegerAllele(i,i2.getIntegerAllele(i));
         intv[i2.getIntegerAllele(i)] = i1.getIntegerAllele(i);
      }
      for (i=0; i<len; i++)
      {
         if (i>cut1 && i<cut2+1)
            continue;
         n = i1.getIntegerAllele(i);
         m = intv[n];
         while (m != -1)
         {
            n = m;
            m = intv[m];
         }
         newInd.setIntegerAllele(i,n);
      }
      
      return newInd;
   }
}
