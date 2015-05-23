/**
 * @author Bernabe Dorronsoro
 *
 * Define a cauchy mutation operator for double-type genotypes
 * 
 */

package operators.mutation;
import java.util.Random;

import jcell.*;

public class CauchyMutation implements Operator
{
    private Random r;
    private EvolutionaryAlg ea;
   
    private double deviation = 1.0;
    
   public CauchyMutation(Random r, EvolutionaryAlg ea)
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
   
   private double nextCauchy(double center, double deviation) 
   {
   	double v1, v2;
    do {
    	v1 = 2.0 * r.nextDouble() - Math.abs(deviation);   // between -deviation and deviation
        v2 = 2.0 * r.nextDouble() - Math.abs(deviation);   // between -deviation and deviation
        //s = v1 * v1 + v2 * v2;
    } while (((v1*v1+v2*v2) > deviation) || ((v1==0.0) && (v2==0.0)));
    
    if (v1!=0.0) 
    	return center + deviation * (v2/v1);
    else
    	return center;
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
	       ri.setRealAllele(i,ri.getRealAllele(i)+nextCauchy(0.0,deviation)); //Mutate gene i
      
      return ri;
   }
}
