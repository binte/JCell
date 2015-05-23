
/**
 * @author Bernabe Dorronsoro
 *
 * Discrete crossover operator for real genotype (Mühlenbein, 93).
 * 
 */

package operators.recombination;
import java.util.Random;
import java.util.Vector;

import jcell.Individual;
import jcell.Operator;
import jcell.RealIndividual;

public class DX implements Operator
{
	
   Random r;
   
   public DX(Random r)
   {
      this.r = r;
   }
   
   public Object execute(Object o)
   {
   	Individual iv[] = (Individual[])o;
    
   	RealIndividual ind[] = new RealIndividual[2];
        
    ind[0] = (RealIndividual) iv[0];
    ind[1] = (RealIndividual) iv[1];
    
   	//RealIndividual ind[] = (RealIndividual)o;
   	int len = ind[0].getLength();
   	Vector minAlleleValues = ind[0].getMinMaxAlleleValue(true);
   	Vector maxAlleleValues = ind[0].getMinMaxAlleleValue(false);
   	RealIndividual child = new RealIndividual(len, minAlleleValues, maxAlleleValues);
   	
   	for (int i = 0; i<len; i++)
   	{
   		// Set the new value to the child
   		if (r.nextBoolean())
   			child.setRealAllele(i,ind[0].getRealAllele(i));
   		else 
   			child.setRealAllele(i,ind[1].getRealAllele(i));
   	}
  	return child;
   }
}

