
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
 * The problem: Chebyschev Problem
 * 
 */

package problems.Continuous;
import jcell.*;
import java.util.Vector;
import java.util.Random;

import java.lang.Math;

public class Chebyschev extends Problem{
 
  private int len = 0;
  private RealIndividual ri; 
  
  public Chebyschev(String dataFile) {
	  
      super(dataFile);
      
      Target.maximize = false;
      variables = 9;
      maxFitness = 0.0; 

      minAllowedValues = new Vector (this.getVariables());
      maxAllowedValues = new Vector (this.getVariables());
      
      for (int i=0; i< this.getVariables(); i++)
      {
  		minAllowedValues.add(new Double(-5.12));
  		maxAllowedValues.add(new Double(5.12));
      }
      
  } 
  
  public Object eval(Individual individual) {
    ri = (RealIndividual) individual;
    len = ri.getLength();
    return new Double(fCheb());
  } // Evaluate
  
  private double fCheb() {
    double[] p = new double[101];
    double R       = 0.0 ;
    double tmp1,tmp2;
    Random r = new Random();
    
    for (int i = 0; i < 101; i ++)
        p[i] = -1.0 + 2.0 * r.nextDouble();
    
    for (int i = 0; i < 101; i ++) {
        tmp1 = Pc(p[i]); 
        if ((tmp1 < -1) || (tmp1 > 1))
            R += Math.pow( (1.0 - tmp1),  2.0);
    }//for
    
    tmp1 = Pc(1.2);
    tmp2 = T8(1.2);
    if (tmp1 - tmp2  < 0)
        R += Math.pow( tmp1 - tmp2, 2.0);
    
    tmp1 = Pc(-1.2);
    tmp2 = T8(-1.2);
    if (tmp1 - tmp2  < 0)
        R += Math.pow( tmp1 - tmp2, 2.0);
    
    //if (R == 0.0) return 0.0; 
    //else return 1.0/R;
    return R;
  } // fPolinomial
  
  private double Pc(double z) {
    double sum = 0.0;
    
    for (int i = 0; i < len; i ++)
        sum += ri.getRealAllele(i)
               *
               Math.pow(z, i);
    return sum;
  } //Pc
  
  private double T8(double z) {
    double sum ;
    
    sum = 1.0 
          - 32.0  * Math.pow(z, 2.0)
          + 160.0 * Math.pow(z, 4.0)
          - 256.0 * Math.pow(z, 6.0)
          + 128.0 * Math.pow(z, 8.0) ;
    
    return sum;
  } //T8
} 
