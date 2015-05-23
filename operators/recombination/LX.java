
/**
 * @author Bernabe Dorronsoro
 *
 * Linear crossover operator for real genotype (Wright, 91).
 * Returns one of the three generated offspring. It is
 * chosen randomly.  
 * 
 */

package operators.recombination;
import java.util.Random;
import java.util.Vector;
import jcell.Individual;
import jcell.Operator;
import jcell.RealIndividual;

public class LX implements Operator
{
	
   Random r;
   
   public LX(Random r)
   {
      this.r = r;
   }
   
   public Object execute(Object o)
   {
   	double allele1, allele2, newAllele;
   	double maxRange, minRange;
   	
   	Individual iv[] = (Individual[])o;
    
   	RealIndividual ind[] = new RealIndividual[2];
        
    ind[0] = (RealIndividual) iv[0];
    ind[1] = (RealIndividual) iv[1];
    
   	//RealIndividual ind[] = (RealIndividual)o;
   	int len = ind[0].getLength();
   	Vector minAlleleValue = ind[0].getMinMaxAlleleValue(true);
   	Vector maxAlleleValue = ind[0].getMinMaxAlleleValue(false);
   	RealIndividual child1 = new RealIndividual(len, minAlleleValue, maxAlleleValue);
   	RealIndividual child2 = new RealIndividual(len, minAlleleValue, maxAlleleValue);
   	RealIndividual child3 = new RealIndividual(len, minAlleleValue, maxAlleleValue);
   	  	
   	for (int i = 0; i<len; i++)
   	{
   		allele1 = ind[0].getRealAllele(i);
   		allele2 = ind[1].getRealAllele(i);
   		   		
   		// Set the new value to the child
   		child1.setRealAllele(i,0.5*allele1 + 0.5*allele2);
   		child2.setRealAllele(i,3*allele1/2 - 0.5*allele2);
   		child3.setRealAllele(i,-0.5*allele1 + 3*allele2/2);
   	}
   	
   	double rand = r.nextDouble();
   	if (rand<=0.33)
   	{
   		child1.checkAlleleRanges();
   		return child1;
   	}
   	else if (rand<=0.66)
   	{
   		child2.checkAlleleRanges();
   		return child2;
   	}
   	else
   	{
   		child3.checkAlleleRanges();
   		return child3;
   	}
   }
}

