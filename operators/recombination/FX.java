   
/**
 * @author Bernabe Dorronsoro
 *
 * Flat crossover operator for real genotype (Radcliffe, 91).
 * 
 */

package operators.recombination;
import java.util.Random;
import java.util.Vector;

import jcell.Individual;
import jcell.Operator;
import jcell.RealIndividual;

public class FX implements Operator
{
	
   Random r;
   
   public FX(Random r)
   {
      this.r = r;
   }
   
   
   public Object execute(Object o)
   {
   	double c_max, c_min;
   	double allele1, allele2, newAllele;
   	
   	Individual iv[] = (Individual[])o;
    
   	RealIndividual ind[] = new RealIndividual[2];
        
    ind[0] = (RealIndividual) iv[0];
    ind[1] = (RealIndividual) iv[1];
    
   	//RealIndividual ind[] = (RealIndividual)o;
   	int len = ind[0].getLength();
   	Vector minAlleleValue = ind[0].getMinMaxAlleleValue(true);
   	Vector maxAlleleValue = ind[0].getMinMaxAlleleValue(false);
   	RealIndividual child = new RealIndividual(len, minAlleleValue, maxAlleleValue);
   	  	
   	for (int i = 0; i<len; i++)
   	{
   		allele1 = ind[0].getRealAllele(i);
   		allele2 = ind[1].getRealAllele(i);
   		// compute c_max and c_min values
   		if (allele2 > allele1)
   		{
   			c_max = allele2;
   			c_min = allele1;
   		}
   		else
   		{
   			c_max = allele1; 
   			c_min = allele2;  
   		}
   		
   		// random value in the range [c_max, cmin]
   		newAllele = c_min + r.nextDouble() * (c_max - c_min);
   		
   		// Set the new value to the child
   		child.setRealAllele(i,newAllele);
   	}
   	return child;
   }
}

