
/**
 * @author Bernabe Dorronsoro
 *
 * Wright's heuristic crossover operator for real genotype (Wright, 90).
 * 
 */

package operators.recombination;
import java.util.Random;
import java.util.Vector;
import jcell.Individual;
import jcell.Target;
import jcell.Operator;
import jcell.RealIndividual;

public class WHX implements Operator
{
	
   Random r;
   
   public WHX(Random r)
   {
      this.r = r;
   }
   
   public Object execute(Object o)
   {
   	Individual iv[] = (Individual[])o;
    
   	RealIndividual ind[] = new RealIndividual[2];
        
    ind[0] = (RealIndividual) iv[0];
    ind[1] = (RealIndividual) iv[1];
    
   	int len = ind[0].getLength();
   	Vector minAlleleValue = ind[0].getMinMaxAlleleValue(true);
   	Vector maxAlleleValue = ind[0].getMinMaxAlleleValue(false);
   	RealIndividual child = new RealIndividual(len, minAlleleValue, maxAlleleValue);
   	double alpha = 0.0;
   	
   	// en ind[0] debe estar el mejor padre
   	if (Target.isBetterOrEqual(ind[1], ind[0]))
   	{
   		RealIndividual aux = ind[0];
   		ind[0] = ind[1];
   		ind[1] = aux;
   	}
   	
   	for (int i = 0; i<len; i++)
   	{
   		alpha  = -0.25 + r.nextDouble() * 1.50; 
   		// Set the new value to the child
   		child.setRealAllele(i,r.nextDouble()*(ind[0].getRealAllele(i)-ind[1].getRealAllele(i))+ind[0].getRealAllele(i));
   	}
   	child.checkAlleleRanges();
   	
  	return child;
   }
}