
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
 * The problem: Griewagk problem. The implementation usually studied in the literature
 * 
 */

package problems.Continuous;

import jcell.*;
import java.util.Vector;
import java.lang.Math;

public class Griewangk_Classic extends Problem{
  
	private double d = 4000.0;
  
  public Griewangk_Classic(String dataFile) {
	  
      super(dataFile);

      Target.maximize = false;
      variables = 25;
      maxFitness = 0.0;

      minAllowedValues = new Vector (this.getVariables());
      maxAllowedValues = new Vector (this.getVariables());
      
      for (int i=0; i< this.getVariables(); i++)
      {
  		minAllowedValues.add(new Double(-600.0));
  		maxAllowedValues.add(new Double(600.0));
      }

  } // ProblemSphereModel constructor
  
  public Object eval(Individual individual) {
    return new Double(fGriewangk(individual));
  } // Evaluate
  
  // Count the number of 1's in the string
  private double fGriewangk(Individual individual) {
    double fitness = 0.0;
    double sum = 0.0;
    double prod = 1.0;
    double gene;
    RealIndividual ri = (RealIndividual) individual;
    int len = ri.getLength();
    
    for(int i = 0; i < len; i++) {
        gene = ri.getRealAllele(i); 
        sum += Math.pow(gene,2.0);
        prod = prod * Math.cos(gene/Math.sqrt((double)(i+1)));
    } //for

    fitness = 1.0 + (sum / d) - prod;
    return fitness;
  } 
} 
