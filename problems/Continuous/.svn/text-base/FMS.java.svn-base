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
 * The problem: Frequency modulation sounds problem
 * 
 */

package problems.Continuous;

import jcell.*;
import java.util.Vector;

public class FMS extends Problem{
  
  /** Creates new ProblemFMS */
  public FMS() {
      super() ;
      
      Target.maximize = false;
      variables = 6; 
      maxFitness = 0.0;

      minAllowedValues = new Vector (numberOfVariables());
      maxAllowedValues = new Vector (numberOfVariables());
      
      for (int i=0; i< numberOfVariables(); i++)
      {
  		minAllowedValues.add(new Double(-6.4));
  		maxAllowedValues.add(new Double(6.35));
      }
      
  } // ProblemFMS constructor
  
  private RealIndividual individual;
  
  public Object eval(Individual individual) {
  	this.individual = (RealIndividual) individual;
    return new Double(fFMS());
  } // Evaluate
  
  // Count the number of 1's in the string
  private double fFMS() {
    double fitness = 0.0;
    double tmp;
    for(int i = 0; i < 100; i++) {
      tmp = y(i) - y0(i);
      fitness += Math.pow(tmp,2.0);
    } //for  
    //if (fitness == 0.0) return 0.0;
    //return 1.0/fitness;
    return fitness;
  } // fFMS
  
  private double y(int i) {
    double a1 = individual.getRealAllele(0);
    double w1 = individual.getRealAllele(1);
    double a2 = individual.getRealAllele(2);
    double w2 = individual.getRealAllele(3);
    double a3 = individual.getRealAllele(4);
    double w3 = individual.getRealAllele(5);
    double teta = (2.0 * Math.PI) / 100.0;
    double t  = (double) i;
    double tmp;
    
    tmp = w3 * t * teta;
    tmp = a3 * Math.sin(tmp);
    tmp = w2 * t * teta + tmp;
    tmp = a2 * Math.sin(tmp);
    tmp = w1 * t * teta + tmp;
    
    return a1 * Math.sin(tmp);
  } //y
  
  private double y0(int i) {
    double t = (double) i;
    double teta = (2.0 * Math.PI) / 100.0;
    double tmp ;
    
    tmp = 4.9 * t * teta;
    tmp = 2.0 * Math.sin(tmp);
    tmp = 4.8 * t * teta + tmp ;
    tmp = 5.0 * t * teta - 1.5 * Math.sin(tmp);
    
    return Math.sin(tmp) ;
  } //y
} // class ProblemFMS
