
/**
 * @author Bernabe Dorronsoro
 *
 * Simulated binary crossover. Returns the best offspring 
 * 
 */

package operators.recombination;

import java.util.Random;
import jcell.Individual;
import jcell.Operator;
import jcell.RealIndividual;

public class SBX implements Operator
{
	
   Random r;
   double distributionIndex;
   
   public SBX(Random r)
   {
      this(20.0, r);
      this.r = r;
   }
   
   public SBX(double distributionIndex, Random r)
   {
      this.distributionIndex = distributionIndex;
      this.r = r;
   }
   
   public void setDistributionIndex(double distributionIndex)
   {
   	this.distributionIndex = distributionIndex;
   }
   
   public double getDistributionIndex()
   {
   	return distributionIndex;
   }
   
   public Object execute(Object o)
   {
   	
   	  double upperValue, lowerValue, valueY1, valueY2, valueX1, valueX2, beta, alpha, aux;
   	  double betaq, random;
   	  
      Individual iv[] = (Individual[])o;
      RealIndividual parent1, parent2, child1, child2, newInd;
      int len = iv[0].getLength();

      parent1 = (RealIndividual)iv[0];
      parent2 = (RealIndividual)iv[1];
      child1 = (RealIndividual)parent1.clone();
      child2 = (RealIndividual)parent2.clone();

      for (int i=0; i<len; i++)
      {
      	if ((child1.getMinMaxAlleleValue(false).size() == 1) && (child1.getMinMaxAlleleValue(true).size() == 1))
   	   	{
   	   		upperValue = ((Double)child1.getMinMaxAlleleValue(false).firstElement()).doubleValue();
   	   		lowerValue = ((Double)child1.getMinMaxAlleleValue(true).firstElement()).doubleValue();
   	   	}
   	   	else
   	   	{
   	   		upperValue = ((Double)child1.getMinMaxAlleleValue(false).get(i)).doubleValue();
   	   		lowerValue = ((Double)child1.getMinMaxAlleleValue(true).get(i)).doubleValue();
   	   	}

      	random = r.nextDouble();
      	valueX1 = child1.getRealAllele(i);
      	valueX2 = child2.getRealAllele(i);
      	
        // A variable is selected with a probability less than 0.5
        if (random <= 0.5)
        {
      
          if (valueX1 < valueX2) {
            valueY1 = valueX1 ;
            valueY2 = valueX2 ;
          } // if
          else {
            valueY1 = valueX2 ;
            valueY2 = valueX1 ;
          } // else
      
          if (Math.abs(valueX1 - valueX2) > 1.0e-14) {  
            random = r.nextDouble();
            beta = 1.0 + (2.0*(valueY1-lowerValue)/(valueY2-valueY1)) ;
            alpha = 2.0 - Math.pow(beta, -(distributionIndex + 1.0)) ;
            if (random <= 1.0/alpha)
              betaq = Math.pow((random*alpha), (1.0/(distributionIndex + 1.0))) ;
            else
              betaq = Math.pow((1.0/(2.0-random*alpha)), (1.0/(distributionIndex + 1.0))) ;
            aux = 0.5*((valueY1 + valueY2) - betaq*(valueY2 - valueY1)) ;
            
            if (aux < lowerValue) 
                aux = lowerValue;
            if (aux > upperValue)
                aux = upperValue;
              
            child1.setRealAllele(i, aux) ;
    
            beta = 1.0 + (2.0*(upperValue-valueY2)/(valueY2-valueY1)) ;
            alpha = 2.0 - Math.pow(beta, -(distributionIndex + 1.0)) ;
            if (random <= 1.0/alpha)
              betaq = Math.pow((random*alpha), (1.0/(distributionIndex + 1.0))) ;
            else
              betaq = Math.pow((1.0/(2.0-random*alpha)), (1.0/(distributionIndex + 1.0))) ;

            aux = 0.5*((valueY1 + valueY2) + betaq*(valueY2 - valueY1)) ;
            
            if (aux < lowerValue) 
                aux = lowerValue;
            if (aux > upperValue)
                aux = upperValue;
            
            child2.setRealAllele(i, aux) ;
     
          }
      	
        }
      }
      	
      // Select one child randomly
      if (r.nextDouble() <= 0.5)
      	newInd = child1;
      else  
		newInd = child2;
            
      return newInd;
   }   
}