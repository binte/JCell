/**
 * @author Bernabe Dorronsoro
 *
 * Define a general mutation operator for real genotype individuals
 * for JCell, the muhlenbein mutation (Muhlenbein et al., 93)
 * 
 */


package operators.mutation;   
import java.util.Random;
import java.util.Vector;
import jcell.CellularGA;
import jcell.EvolutionaryAlg;
import jcell.Operator;
import jcell.RealIndividual;

public class MuhlenbeinMutation implements Operator
{
    private Random r;
    private EvolutionaryAlg ea;
    private double allele;
    private Vector lowerBound, upperBound;
   
   public MuhlenbeinMutation(Random r, EvolutionaryAlg ea)
   {
      this.r = r;
      this.ea = ea;
   }
   
   // Parameter Individual, returns Individual
   public Object execute(Object o)
   {

       Object o2;
       double alpha = 0.0;

       RealIndividual ri = (RealIndividual)o;
       int len = ri.getLength(); //Length of individual
       
       upperBound = ri.getMinMaxAlleleValue(false);
	   lowerBound = ri.getMinMaxAlleleValue(true);
	   
       int gn; // Number of generation
       int gnLimit; // Number of generation (the limit)
       double prob; //Define probability of mutation
      
       o2 = ea.getParam(CellularGA.PARAM_GENERATION_NUMBER);
       gn = ((Integer)o2).intValue();

       o2 = ea.getParam(CellularGA.PARAM_GENERATION_LIMIT);
       gnLimit = ((Integer)o2).intValue();

       //prob = ((Double)cea.getParam(CellularGA.PARAM_MUTATION_PROB)).doubleValue();
	prob = ((Double)ea.getParam(CellularGA.PARAM_ALLELE_MUTATION_PROB)).doubleValue();

       double allele, rang, gamma;
      for (int i=0; i<len; i++)
         if (r.nextDouble() < prob)
         {
         	allele = ri.getRealAllele(i);
         	if ((upperBound.size() == 1) && (lowerBound.size() == 1))
         		rang = 0.1 * (((Double)upperBound.firstElement()).doubleValue() - 
         				((Double)lowerBound.firstElement()).doubleValue());
         	else
         		rang = 0.1 * (((Double)upperBound.elementAt(i)).doubleValue() - 
         				((Double)lowerBound.elementAt(i)).doubleValue());
         	
         	gamma = 0.0;
         	for (int k=0;k<16;k++) 
         	{
         		if (r.nextDouble() <= 1/16) alpha = 1.0;
         		else alpha = 0.0;
       			gamma+= alpha * Math.pow(2.0,-k);
         	}
         	if (r.nextBoolean()) allele+=rang*gamma;
         	else allele-=rang*gamma;
         	if ((upperBound.size() == 1) && (lowerBound.size() == 1))
         	{
         		if (allele > ((Double)upperBound.firstElement()).doubleValue())
         			allele = ((Double)upperBound.firstElement()).doubleValue();
         		else if (allele < ((Double)lowerBound.firstElement()).doubleValue())
         			allele = ((Double)lowerBound.firstElement()).doubleValue();
         	}
         	else
         	{
         		if (allele > ((Double)upperBound.elementAt(i)).doubleValue())
         			allele = ((Double)upperBound.elementAt(i)).doubleValue();
         		else if (allele < ((Double)lowerBound.elementAt(i)).doubleValue())
         			allele = ((Double)lowerBound.elementAt(i)).doubleValue();
         	}
         	
         	ri.setRealAllele(i,allele);
         }
      
      return ri;
   }
}
