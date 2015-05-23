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
 * The problem: Schwefel problem
 * 
 */

package problems.Continuous;
import jcell.*;
import java.util.Vector;
import java.lang.Math;

public class Schwefel extends Problem{
  
  public Schwefel() {
      super() ;
      
      Target.maximize = false;
      maxFitness = 0.0;
      variables = 25;

      minAllowedValues = new Vector (numberOfVariables());
      maxAllowedValues = new Vector (numberOfVariables());
      
      for (int i=0; i< numberOfVariables(); i++)
      {
  		minAllowedValues.add(new Double(-65.536));
  		maxAllowedValues.add(new Double(65.536));
      }
  } // ProblemSchwefel constructor
  
  public Object eval(Individual individual) {
    return new Double(fSchwefel(individual));
  } // Evaluate
  
  private double fSchwefel(Individual individual) {
    double fitness = 0.0;
    double fitnessAux = 0.0;
    RealIndividual ri = (RealIndividual) individual;
    int len = ri.getLength();
    for (int i = 0; i < len; i++) {
       fitnessAux = 0.0;
       for (int j = 0; j < i; j ++)
           fitnessAux += ri.getRealAllele(i);
       fitness += Math.pow(fitnessAux, 2.0);
    } //for
    return fitness;
  } // fSchwefel
} // class ProblemSchwefel
