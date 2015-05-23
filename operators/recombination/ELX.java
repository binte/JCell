/**
 * @author Bernabe Dorronsoro
 *
 * Extended line crossover operator for real genotype (Mühlenbein, 93).
 * 
 */

package operators.recombination;
import java.util.Random;
import java.util.Vector;

import jcell.Individual;
import jcell.Operator;
import jcell.RealIndividual;

public class ELX implements Operator
{
	
   Random r;
   
   public ELX(Random r)
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
   	Vector minAlleleValue = ind[0].getMinMaxAlleleValue(true);
   	Vector maxAlleleValue = ind[0].getMinMaxAlleleValue(false);
   	RealIndividual child = new RealIndividual(len, minAlleleValue, maxAlleleValue);
   	double alpha = 0.0;
   	  	
   	for (int i = 0; i<len; i++)
   	{
   		alpha  = -0.25 + r.nextDouble() * 1.50; 
   		// Set the new value to the child
   		double allele = ind[0].getRealAllele(i)+alpha*(ind[1].getRealAllele(i)-ind[0].getRealAllele(i));
   		if (minAlleleValue.size() == 1)
   			if (allele < ((Double)minAlleleValue.firstElement()).doubleValue())
   				allele = ((Double)minAlleleValue.firstElement()).doubleValue();
   			else if (allele > ((Double)maxAlleleValue.firstElement()).doubleValue())
   				allele = ((Double)maxAlleleValue.firstElement()).doubleValue();

   		else
   			if (allele < ((Double)minAlleleValue.elementAt(i)).doubleValue())
   				allele = ((Double)minAlleleValue.firstElement()).doubleValue();
   			else if (allele > ((Double)maxAlleleValue.elementAt(i)).doubleValue())
   				allele = ((Double)maxAlleleValue.firstElement()).doubleValue();
   			
   		child.setRealAllele(i,allele);
   			
   	}
  	return child;
   }
}