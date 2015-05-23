
/**
 * @author Bernabe Dorronsoro
 *
 * Parent centric recombination PBX (García and Lozano 05).
 * 
 */

package operators.recombination;
import java.util.Random;
import java.util.Vector;
import jcell.Individual;
import jcell.Operator;
import jcell.RealIndividual;

public class PBX implements Operator
{
	
   Random r;
   Vector minAlleleValue;
   Vector maxAlleleValue;
   private double alpha = 0.8;
   
   public PBX(Random r)
   {
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
   	Individual iv[] = (Individual[])o;
    
   	RealIndividual ind[] = new RealIndividual[2];
        
    ind[0] = (RealIndividual) iv[0];
    ind[1] = (RealIndividual) iv[1];
    
   	int len = ind[0].getLength();
   	minAlleleValue = ind[0].getMinMaxAlleleValue(true);
   	maxAlleleValue = ind[0].getMinMaxAlleleValue(false);
   	RealIndividual child = new RealIndividual(len, minAlleleValue, maxAlleleValue);
   	
   	double l, u, I, allele;

   	for (int i=0; i<len; i++)
   	{
   		// ind[0] is suposed to be the considered one (the one in the center of the neighborhood)
   		I = Math.abs(ind[0].getRealAllele(i) - ind[1].getRealAllele(i));
   		if ((maxAlleleValue.size() == 1) && (minAlleleValue.size() == 1))
   		{
   			l = Math.max(((Double)minAlleleValue.firstElement()).doubleValue(), ind[1].getRealAllele(i)-I*alpha);
   			u = Math.min(((Double)maxAlleleValue.firstElement()).doubleValue(), ind[1].getRealAllele(i)+I*alpha);
   		}
   		else
   		{
   			l = Math.max(((Double)minAlleleValue.elementAt(i)).doubleValue(), ind[1].getRealAllele(i)-I*alpha);
			u = Math.min(((Double)maxAlleleValue.elementAt(i)).doubleValue(), ind[1].getRealAllele(i)+I*alpha);
   		}
   		
   		allele = u + r.nextDouble() * (l-u);
   		
   		// check that the allele values are within the ranges
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
   		child.setRealAllele(i,allele);
   	}
   	return child;
   }
   
}