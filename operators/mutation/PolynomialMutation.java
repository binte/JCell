
/**
 * @author Bernabe Dorronsoro
 *
 * Define a general polynomial mutation operator for real individuals
 * 
 */

package operators.mutation;

import java.util.Random;

import jcell.*;

public class PolynomialMutation implements Operator
{
    private Random r;
    private EvolutionaryAlg ea;
    private double allele;
    private double distributionIndex = 20.0;
    private double lowerBound, upperBound;
   
    public PolynomialMutation(Random r, EvolutionaryAlg ea)
    {
       this.r = r;
       this.ea = ea;
       allele = r.nextDouble() * (Double.MAX_VALUE - Double.MIN_VALUE) + Double.MIN_VALUE;
    }
    
    public void setDistributionIndex(double di)
    {
    	this.distributionIndex = di;
    }
    
    public double getDistributionIndex()
    {
    	return distributionIndex;
    }
    
    public PolynomialMutation(Random r, EvolutionaryAlg ea, double di)
    {
       this.r = r;
       this.ea = ea;
       this.distributionIndex = di;
       allele = r.nextDouble() * (Double.MAX_VALUE - Double.MIN_VALUE) + Double.MIN_VALUE;
    }
   
   public PolynomialMutation(Random r, double min, double max, EvolutionaryAlg ea)
   {
      this.r = r;
      this.ea = ea;
      
      if (min == max) 
      {
      	System.err.println("PolynomialMutation. Constructor: minimum and maximum bounds are the same value"); 
      	return;
      }
      
      allele = (min < max) ? 
      	r.nextDouble() * (max - min) + min :
      	r.nextDouble() * (min - max) + max ;
   }
   
   // Parameter Individual, returns Individual
   public Object execute(Object o)
   {
   	   double temp, rnd, delta, deltaq, mu;
   	   int mutations = 0;
       Object o2;

       RealIndividual ri = (RealIndividual)o;
       int len = ri.getLength(); //Length of individual --> Number of elements in the chromosome
	   
       double prob = ((Double)ea.getParam(CellularGA.PARAM_ALLELE_MUTATION_PROB)).doubleValue();
	   
	   for (int i=0; i<len; i++)
	   {
	   	   if ((ri.getMinMaxAlleleValue(false).size() == 1) && (ri.getMinMaxAlleleValue(true).size() == 1))
	   	   	{
	   	   		upperBound = ((Double)ri.getMinMaxAlleleValue(false).firstElement()).doubleValue();
	   	   		lowerBound = ((Double)ri.getMinMaxAlleleValue(true).firstElement()).doubleValue();
	   	   	}
	   	   else
	   	   {
	   	   	upperBound = ((Double)ri.getMinMaxAlleleValue(false).get(i)).doubleValue();
	   	   	lowerBound = ((Double)ri.getMinMaxAlleleValue(true).get(i)).doubleValue();
	   	   }
		   
	       if (r.nextDouble() <= prob)
	       {
	       	
	       	   allele = ((Double)ri.getAllele(i)).doubleValue();
		       if (allele > lowerBound)
		       {
		       		if ((allele - lowerBound) < (upperBound - allele))
		       			delta = (allele - lowerBound) / (upperBound - allele);
		       		else
		       			delta = (upperBound - allele) / (upperBound - lowerBound);
		       			
		       		mu = 1.0 / (distributionIndex + 1.0);
		       		
		       		rnd = r.nextDouble();
		       		if (rnd <= 0.5)
		       		{
		       			double xy = 1.0 - delta;
		       			temp = 2 * rnd + (1-2*rnd) * Math.pow(xy, distributionIndex + 1.0);
		       			deltaq = Math.pow(temp, mu) - 1.0;
		       		}
		       		else
		       		{
		       			double xy = 1.0 - delta;
		       			temp = 2.0 * (1.0 - rnd) + 2.0 * (rnd-0.5) * Math.pow(xy, distributionIndex + 1.0);
		       			deltaq = 1.0 - Math.pow(temp, mu);
		       		}
		       		
		       		allele += deltaq * (upperBound - lowerBound);
		       		if (allele < lowerBound)
		       			allele = lowerBound;
		       		if (allele > upperBound)
		       			allele = upperBound;
		       }
		       else
		       {
		       		rnd  = r.nextDouble();
		       		allele = rnd * (upperBound - lowerBound) + lowerBound;
		       }
		       
		       ri.setRealAllele(i, allele);
		    }
	   }      
       return ri;
   }
}
