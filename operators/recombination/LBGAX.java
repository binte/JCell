
/**
 * @author Sergio Romero
 * 
 * Modified by Bernabe Dorronsoro
 *
 * Linear BGA crossover operator for real genotype (Schlierkamp-Voosen, 94).
 * 
 */

package operators.recombination;
import java.util.Random;
import java.util.Vector;
import jcell.Target;
import jcell.Individual;
import jcell.Operator;
import jcell.RealIndividual;

public class LBGAX implements Operator
{
	
   Random r;
   
   public LBGAX(Random r)
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
   	double Lambda, gamma;
   	
   	if (Target.isBetterOrEqual(ind[1], ind[0]))
   	{
   		RealIndividual aux = ind[0];
   		ind[0] = ind[1];
   		ind[1] = aux;
   	}
   	
   	double dist = 0.0;
   	
   	// compute distance among parents
   	for (int i = 0; i<len; i++)
   	{
   		dist += Math.abs((ind[0].getRealAllele(i)-ind[1].getRealAllele(i)));
   	}
   	
   	for (int i = 0; i<len; i++)
   	{
   		Lambda = (ind[1].getRealAllele(i)-ind[0].getRealAllele(i)) / dist;
   		gamma = 0.0;
   		for (int k=0;k<16;k++)
   		{
   			alpha = 0.0;
   			if (r.nextDouble() <= 1/16) alpha = 1.0;
   			else alpha = 0.0;
   			gamma+= alpha * Math.pow(2.0,-k);
   		}
   		// Set the new value to the child
   		if (r.nextDouble() <= 0.9)
   			if ((maxAlleleValue.size() == 1) && (minAlleleValue.size() == 1))
   				child.setRealAllele(i,ind[0].getRealAllele(i)-(((Double)maxAlleleValue.firstElement()).doubleValue()
   					-((Double)minAlleleValue.firstElement()).doubleValue())*gamma*Lambda);
   			else
   				child.setRealAllele(i,ind[0].getRealAllele(i)-(((Double)maxAlleleValue.elementAt(i)).doubleValue()
   	   					-((Double)minAlleleValue.elementAt(i)).doubleValue())*gamma*Lambda);
   	}
   	
   	   //  Check that the maximum and minimum allele values are not exceeded
	   child.checkAlleleRanges();

  	return child;
   }
}