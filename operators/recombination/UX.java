
/**
 * @author Bernabe Dorronsoro
 *
 * Uniform crossover
 * 
 */

package operators.recombination;

import java.util.Random;

import jcell.*;

public class UX implements Operator
{
private Random r;

public UX(Random r)
{
   this.r = r;
}

public Object execute(Object o) 
{
   Individual iv[] = (Individual[])o;
   Individual newInd;
   int i, len = iv[0].getLength();
   
   newInd = (Individual)iv[0].clone();
   
   for (i=0; i<len; i++)
	   if (r.nextBoolean())
		   newInd.setAllele(i,iv[1].getAllele(i));
   
   return newInd;
}
}