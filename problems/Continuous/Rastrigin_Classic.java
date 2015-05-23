
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
 * The problem: Rastrigin problem
 * 
 */

package problems.Continuous;

import jcell.*;
import java.util.Vector;
import java.lang.Math;

public class Rastrigin_Classic extends Problem{
  
  /** Creates new Rastrigin */
  public Rastrigin_Classic() {
      super() ;

      Target.maximize = false;
      variables = 25; 
      maxFitness = 0.0;

      minAllowedValues = new Vector (numberOfVariables());
      maxAllowedValues = new Vector (numberOfVariables());
      
      for (int i=0; i< numberOfVariables(); i++)
      {
  		minAllowedValues.add(new Double(-5.12));
  		maxAllowedValues.add(new Double(5.12));
      }
      
  } // Rastrigin constructor
  
  public Object eval(Individual individual) {
    return new Double(fRastrigin((RealIndividual)individual));
  } // Evaluate
  
  // Count the number of 1's in the string
  private double fRastrigin(RealIndividual individual) {
    double fitness = 0.0;
    double a = 10.0;
    double w = 2.0 * Math.PI;
    double gene ;
    double aux = 0.0;
    int length = individual.getLength();
    fitness = a * length;
    for(int i = 0; i < length; i++) {
      gene = individual.getRealAllele(i);
      
      fitness += Math.pow(gene,2.0) 
                 -
                 ( a * Math.cos(w * gene)) ;
    } //for
    
    return fitness;    
  } // fRastrigin
} // class Rastrigin
