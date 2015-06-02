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
 * The problem: Rosenbrock problem
 * 
 */

package problems.Continuous;
import jcell.*;
import java.util.Vector;
import java.lang.Math;

public class Rosenbrock extends Problem {
  
  public Rosenbrock(String dataFile) {
	  
      super(dataFile);
      
      Target.maximize = false;
      variables = 25; 
      maxFitness = 0.0;

      minAllowedValues = new Vector (this.getVariables());
      maxAllowedValues = new Vector (this.getVariables());
      
      for (int i=0; i< this.getVariables(); i++)
      {
  		minAllowedValues.add(new Double(-5.12));
  		maxAllowedValues.add(new Double(5.12));
      }
      
  } // ProblemRosenbrock constructor
  
  public Object eval(Individual individual) {
    return new Double(fRosenbrock(individual));
  } // Evaluate
  
  // Count the number of 1's in the string
  private double fRosenbrock(Individual individual) {
    double fitness = 0.0;
    double sumando1,sumando2;
    RealIndividual ri = (RealIndividual) individual;
    int len = ri.getLength();
    
    for(int i = 0; i < len - 1; i++) {
      sumando1 = ri.getRealAllele(i+1) - 
                 Math.pow(ri.getRealAllele(i), 2.0);
      sumando1 = 100.0 * Math.pow(sumando1,2.0);
      sumando2 = ri.getRealAllele(i) - 1.0 ;
      sumando2 = Math.pow(sumando2,2.0);
      fitness += (sumando1 + sumando2) ;
    } //for
    
    return fitness;
  } // fRosenbrock
} // class Rosenbrock
