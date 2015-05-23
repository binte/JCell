/**
 * @author Bernabe Dorronsoro
 *
 * Order preserving crossover for integer genotype indiviudals
 * 
 */

package operators.recombination;

import java.util.Random;
import jcell.Target;
import jcell.Individual;
import jcell.Operator;
import jcell.IntegerIndividual;

public class OxInt implements Operator
{
   private boolean boolv[]; 
   private Random r;
   
   public OxInt(Random r)
   {
      this.r = r;
   }
   

   public Object execute(Object o)
   {
      Individual iv[] = (Individual[])o;
      IntegerIndividual i1, i2, auxInd, newInd;
      int j, i, cut1, cut2, len = iv[0].getLength();
      
      newInd = (IntegerIndividual)iv[0].clone();
      if (boolv == null || boolv.length != len)
         boolv = new boolean[((Integer)((IntegerIndividual)iv[0]).getMinMaxAlleleValue(false).elementAt(0)).intValue()+1];
      for (i=0; i<len; i++)
         boolv[i] = false;
         
      i1 = (IntegerIndividual)iv[0];
      i2 = (IntegerIndividual)iv[1];
      cut1 = r.nextInt(len);
      do{
    	  cut2 = r.nextInt(len);  
      } while (cut1==cut2);
      
      
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
         boolv[i2.getIntegerAllele(i)] = true; 
      }
      i = (cut2+1) % len;
      j = i;

      while (i != (cut1+1)%len)
      {
         if (boolv[i1.getIntegerAllele(j)])
            j = (j+1) % len;
         else
         {
            newInd.setIntegerAllele(i,i1.getIntegerAllele(j));
            j = (j+1) % len;
            i = (i+1) % len;
         }
      }
      
      return newInd;
   }
}
