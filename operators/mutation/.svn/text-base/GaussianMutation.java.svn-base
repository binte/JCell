/**
 * @author Bernabe Dorronsoro
 *
 * Define a gaussian mutation operator for floating point
 * 
 */

package operators.mutation;
import java.util.Random;
import java.util.Vector;

import jcell.CellularGA;
import jcell.EvolutionaryAlg;
import jcell.Operator;
import jcell.RealIndividual;

public class GaussianMutation implements Operator
{
    private Random r;
    private EvolutionaryAlg ea;
   
    private static double nextNextGaussian = 0.0;
    private static boolean hasNextNextGaussian = false;
    private double deviation = 1.0;
    
   public GaussianMutation(Random r,EvolutionaryAlg ea)
   {
      this.r = r;
      this.ea = ea;
   }
   
   public void setDeviation(double dev)
   {
   	this.deviation = dev;
   }
   
   public double getDeviation()
   {
   	return deviation;
   }
   
   private double nextGaussian(double center, double deviation) 
   {
   	if (hasNextNextGaussian) {
        hasNextNextGaussian = false;
        return nextNextGaussian;
   	} 
   	else 
   	{
   		double v1, v2, s;
	    do {
	    	v1 = 2 * r.nextDouble() - Math.abs(deviation);   // between -deviation and deviation
	        v2 = 2 * r.nextDouble() - Math.abs(deviation);   // between -deviation and deviation
	        s = v1 * v1 + v2 * v2;
	    } while (s >= deviation || s == 0.0);
	    double multiplier = Math.sqrt(-2 * Math.log(s)/s);
	    double nextNextGaussian = center + v2 * multiplier;
	    return center + v1 * multiplier;
    }
 }
   
   // Parameter Individual, returns Individual
   public Object execute(Object o)
   {
       RealIndividual ri = (RealIndividual)o;
       int len = ri.getLength(); //Length of individual --> Number of elements in the chromosome

       double prob; //Define probability of mutation

       prob = ((Double)ea.getParam(CellularGA.PARAM_ALLELE_MUTATION_PROB)).doubleValue();
             
       for (int i=0; i<len; i++)
	   if (r.nextDouble() < prob)
	       ri.setRealAllele(i,ri.getRealAllele(i)+nextGaussian(0.0,deviation)); //Mutate gene i
	   
	   // nextGaussian(0.0,deviation) hace lo mismo que 0.0 + deviation*r.nextGaussian()
      
	   Vector minAlleleValue = ri.getMinMaxAlleleValue(true);
	   Vector maxAlleleValue = ri.getMinMaxAlleleValue(false);
	   double allele;
	   // Check that the maximum and minimum allele values are not exceeded
	   for (int i=0; i<len; i++)
	   {
	   	allele = ri.getRealAllele(i);
	   	if (minAlleleValue.size() == 1)
	   	{
   			if (allele < ((Double)minAlleleValue.firstElement()).doubleValue())
   			{
   				allele = ((Double)minAlleleValue.firstElement()).doubleValue();
   				ri.setRealAllele(i,allele);
   			}
   			else if (allele > ((Double)maxAlleleValue.firstElement()).doubleValue())
   			{
   				allele = ((Double)maxAlleleValue.firstElement()).doubleValue();
   				ri.setRealAllele(i,allele);
   			}
	   	}

   		else
   		{
   			if (allele < ((Double)minAlleleValue.elementAt(i)).doubleValue())
   			{
   				allele = ((Double)minAlleleValue.firstElement()).doubleValue();
   				ri.setRealAllele(i,allele);
   			}
   			else if (allele > ((Double)maxAlleleValue.elementAt(i)).doubleValue())
   			{
   				allele = ((Double)maxAlleleValue.firstElement()).doubleValue();
   				ri.setRealAllele(i,allele);
   			}
   		}
	   }

      return ri;
   }
}
