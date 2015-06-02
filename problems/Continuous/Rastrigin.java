
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

public class Rastrigin extends Problem{
  
  /** Creates new Rastrigin */
  public Rastrigin(String dataFile) {
      
	  super(dataFile) ;

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
      
      aux += Math.pow(gene,2.0);				// Modified because this argument is rounded in the other case 
      fitness -=  ( a * Math.cos(w * gene)) ;
    } //for
    
    return fitness + aux;
  } // fRastrigin
} // class Rastrigin
