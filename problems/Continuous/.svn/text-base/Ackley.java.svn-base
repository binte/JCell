
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
 * The problem: Ackley problem
 * 
 */

package problems.Continuous;

import jcell.*;
import java.util.Vector;
import java.lang.Math;

public class Ackley extends Problem{
  
  /** Creates new Ackley */
  public Ackley() {
      super() ;

      Target.maximize = false;
      variables = 25; 
      maxFitness = 0.0; 

      minAllowedValues = new Vector (numberOfVariables());
      maxAllowedValues = new Vector (numberOfVariables());
      
      for (int i=0; i< numberOfVariables(); i++)
      {
  		minAllowedValues.add(new Double(-32.768));
  		maxAllowedValues.add(new Double(32.768));
      }
      
  } // Rastrigin constructor
  
  public Object eval(Individual individual) {
    return new Double(fAckley((RealIndividual)individual));
  } // Evaluate
  
  // Count the number of 1's in the string
  private double fAckley(RealIndividual individual) {
    double fitness = 0.0;
    double gene,sum1,sum2;
    int length = individual.getLength();
    
    sum1=0.0;
    sum2=0.0;
    
    for(int i = 0; i < length; i++) {
      gene = individual.getRealAllele(i);
      sum1 += gene*gene;
      sum2 += Math.cos(2*Math.PI*gene);
    } //for
    
    fitness += -20.0 * Math.exp(-0.2*Math.sqrt(sum1/length)) - Math.exp(sum2/length) + 20.0 + Math.E;
	
    return fitness;
  } // fRastrigin
} // class Rastrigin
