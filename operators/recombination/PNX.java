/**
 * @author Bernabe Dorronsoro
 *
 * Parent centric normal recombination (Ballester et al. 04).
 * 
 */

package operators.recombination;
import java.util.Random;
import java.util.Vector;
import jcell.Individual;
import jcell.Operator;
import jcell.RealIndividual;

public class PNX implements Operator
{
	
   Random r;
   Vector minAlleleValue;
   Vector maxAlleleValue;
   private double lambda = 0.35;
   
   private double ro = 2.0;
      
   public PNX(Random r)
   {
      this.r = r;
   }
   
   public void setLambda(double lambda)
   {
   	this.lambda = lambda;
   }
   
   public double getLambda()
   {
   	return lambda;
   }
   
   public void setRo(double ro)
   {
   	this.ro = ro;
   }
   
   public double getRo()
   {
   	return ro;
   }
   
   private double nextGaussian(double center, double deviation) 
   {
   		double v1, v2, s;
   		do {
	    	v1 = 2 * r.nextDouble() - 1.0;
	        v2 = 2 * r.nextDouble() - 1.0;
	        s = v1 * v1 + v2 * v2;
	    } while (s >= 1.0);
	    double multiplier = Math.sqrt(-2 * Math.log(s)/s);
	    return center + deviation * v1 * multiplier;
 }
   
   // Parametro array de individuos devuelve individuo
   public Object execute(Object o)
   {
   	Individual iv[] = (Individual[])o;
    
   	RealIndividual ind[] = new RealIndividual[2];
        
    ind[0] = (RealIndividual) iv[0];
    ind[1] = (RealIndividual) iv[1];
    
   	//RealIndividual ind[] = (RealIndividual)o;
   	int len = ind[0].getLength();
   	minAlleleValue = ind[0].getMinMaxAlleleValue(true);
   	maxAlleleValue = ind[0].getMinMaxAlleleValue(false);
   	RealIndividual child = new RealIndividual(len, minAlleleValue, maxAlleleValue);

   	for (int i=0; i<len; i++)
   	{
   		if (r.nextDouble() < 0.5)
   	   		child.setRealAllele(i,nextGaussian(ind[0].getRealAllele(i), (Math.abs(ind[1].getRealAllele(i)-ind[0].getRealAllele(i))/ro)));
   		else
   			child.setRealAllele(i,nextGaussian(ind[1].getRealAllele(i), (Math.abs(ind[1].getRealAllele(i)-ind[0].getRealAllele(i))/ro)));
   		
   		// check that the limits on the allele values were not exceeded
   		double allele = child.getRealAllele(i);
   		if ((maxAlleleValue.size() == 1) && (minAlleleValue.size() == 1))
   		{
   			if (allele > ((Double)maxAlleleValue.firstElement()).doubleValue())
   				allele = ((Double)maxAlleleValue.firstElement()).doubleValue();
   			else if (allele < ((Double)minAlleleValue.firstElement()).doubleValue())
   				allele = ((Double)minAlleleValue.firstElement()).doubleValue();
   		}
   		else
   		{
   			if (allele > ((Double)maxAlleleValue.elementAt(i)).doubleValue())
   				allele = ((Double)maxAlleleValue.elementAt(i)).doubleValue();
   			else if (allele < ((Double)minAlleleValue.elementAt(i)).doubleValue())
   				allele = ((Double)minAlleleValue.elementAt(i)).doubleValue();
   		}
   		
   	}
   	return child;
   }
   
}