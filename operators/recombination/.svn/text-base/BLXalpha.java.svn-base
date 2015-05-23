/**
 * @author Bernabe Dorronsoro
 *
 * BLX-alpha crossover for double-type genotype individuals
 * 
 */

package operators.recombination;

import java.util.Random;
import java.util.Vector;
import jcell.Individual;
import jcell.Operator;
import jcell.RealIndividual;

public class BLXalpha implements Operator
{
	
   double alpha;
   Random r;
   
   public BLXalpha(Random r)
   {
      this(0.5, r);
      this.r = r;
   }
   
   public BLXalpha(double alpha, Random r)
   {
      this.alpha = alpha;
      this.r = r;
   }
   
   public void setAlpha(double alpha)
   {
   	this.alpha = alpha;
   }
   
   public double getAlpha()
   {
   	return alpha;
   }
   
   public Object execute(Object o)
   {
   	double c_max, c_min, I;
   	double allele1, allele2, newAllele;
   	double maxRange, minRange;
   	
   	Individual iv[] = (Individual[])o;
    
   	RealIndividual ind[] = new RealIndividual[2];
   	
   	ind[0] = (RealIndividual) iv[0];
    ind[1] = (RealIndividual) iv[1];
    
   	int len = ind[0].getLength();
   	Vector minAlleleValue = ind[0].getMinMaxAlleleValue(true);
   	Vector maxAlleleValue = ind[0].getMinMaxAlleleValue(false);
   	RealIndividual child = new RealIndividual(len, ind[0].getNumberOfFuncts(), minAlleleValue, maxAlleleValue);
   	  	
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
   		
   		// Compute I value
   		I = c_max - c_min;
   		
   		// Ranges of the new allele
   		minRange = c_min - I*alpha;
   		maxRange = c_max + I*alpha;
   		
   		// random value in the previous ranges
   		newAllele = minRange + r.nextDouble() * (maxRange - minRange);
   		
   		// Set the new value to the child
   		child.setRealAllele(i,newAllele);
   	}
   	
   	return child;
   	
   }
}

