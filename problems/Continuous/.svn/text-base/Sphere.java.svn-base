
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
 * The problem: Sphere problem
 * 
 */



package problems.Continuous;
import jcell.*;

import java.lang.Math;
import java.util.Vector;

public class Sphere extends Problem{
  
  public Sphere() {
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
  	      
  } // ProblemSphereModel constructor
  
  public Object eval(Individual individual) {
    return new Double(fSphere(individual));
  } // Evaluate
  
  private double fSphere(Individual individual) {
    double fitness = 0.0;
    RealIndividual ri = (RealIndividual) individual;
    int len = ri.getLength();
    for(int i = 0; i < len; i++)
      fitness += Math.pow(ri.getRealAllele(i),2.0);
   
    return fitness;
  } // Sphere
  
} // class Sphere
