
/**
 * @author Bernabe Dorronsoro
 *
 * The problem must provide the following parameters:
 * 	  - variables: the number of variables of the problem
 *    - maxFitness: the optimum (or best known solution) of the problem
 *    - Target.maximize: set whether it is a maximization or minimization problem
 *    - minAllowedValues: Minimum allowed value for each gene  
 *    - maxAllowedValues: Maximum allowed value for each gene
 *
 * The problem: EF10 problem
 * 
 */

package problems.Continuous;

import jcell.Individual;
import jcell.Target;
import jcell.Problem;
import jcell.RealIndividual;
import java.util.Vector;


public class EF10 extends Problem {

	private int vars = 10;
	
	/** Creates new EF10 */
	  public EF10() {
	      super() ;
	      
	      Target.maximize = false;
	      variables = 10; 
	      maxFitness = 0.0;

	      minAllowedValues = new Vector (numberOfVariables());
	      maxAllowedValues = new Vector (numberOfVariables());
	      
	      for (int i=0; i< numberOfVariables(); i++)
	      {
	  		minAllowedValues.add(new Double(-100.0));
	  		maxAllowedValues.add(new Double(100.0));
	      }
	      
	  } // EF10 constructor

	  
	  public Object eval(Individual individual) {
	  	vars = individual.getLength();
	    return new Double(fEF10((RealIndividual)individual));
	  } // Evaluate
	  
	  private double fEF10(RealIndividual individual)
	  {
	  	double fitness = 0.0;
	  	for (int i=0; i<vars-1; i++)
	  	{
	  		fitness += f10(individual.getRealAllele(i),individual.getRealAllele(i+1)); 
	  	}
	  	fitness += f10(individual.getRealAllele(vars-1),individual.getRealAllele(0));
	  		
	  	return fitness;
	  }
	  
	  private double f10(double x, double y)
	  {
	  	double res = 0.0;
	  	double a = Math.pow(x*x + y*y,0.25);
	  	double b = Math.pow(Math.sin(50*Math.pow(x*x + y*y,0.1)),2);
	  	res = Math.pow(x*x + y*y,0.25) * (Math.pow(Math.sin(50*Math.pow(x*x + y*y,0.1)),2)+1);
	  	return res;
	  }
}
