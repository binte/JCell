
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
 * The problem: Systems of linear equations problem
 * 
 */

package problems.Continuous;
import jcell.*;
import java.util.Vector;
import java.lang.Math;

public class Sle extends Problem{

	private double [][] a = {
		{5.0, 4.0, 5.0, 2.0, 9.0, 5.0, 4.0, 2.0, 3.0, 1.0},
		{9.0, 7.0, 1.0, 1.0, 7.0, 2.0, 2.0, 6.0, 6.0, 9.0},
		{3.0, 1.0, 8.0, 6.0, 9.0, 7.0, 4.0, 2.0, 1.0, 6.0},
		{8.0, 3.0, 7.0, 3.0, 7.0, 5.0, 3.0, 9.0, 9.0, 5.0},
		{9.0, 5.0, 1.0, 6.0, 3.0, 4.0, 2.0, 3.0, 3.0, 9.0},
		{1.0, 2.0, 3.0, 1.0, 7.0, 6.0, 6.0, 3.0, 3.0, 3.0},
		{1.0, 5.0, 7.0, 8.0, 1.0, 4.0, 7.0, 8.0, 4.0, 8.0},
		{9.0, 3.0, 8.0, 6.0, 3.0, 4.0, 7.0, 1.0, 8.0, 1.0},
		{8.0, 2.0, 8.0, 5.0, 3.0, 8.0, 7.0, 2.0, 7.0, 5.0},
		{2.0, 1.0, 2.0, 2.0, 9.0, 8.0, 7.0, 4.0, 4.0, 1.0},
		};   
	 
    private double [] b = {40.0, 50.0, 47.0, 59.0, 45.0, 35.0, 53.0, 50.0, 55.0, 40.0};

  public Sle() {
      super() ;
      
      Target.maximize = false;
      maxFitness = 0.0;
      variables = 10;
      minAllowedValues = new Vector(numberOfVariables());
      maxAllowedValues = new Vector(numberOfVariables());
      
      for (int i=0; i< numberOfVariables(); i++)
      {
  		minAllowedValues.add(new Double(-9.0));
  		maxAllowedValues.add(new Double(11.0));
      }
  } 
  
  public Object eval(Individual individual) {
    return new Double(fSle((RealIndividual) individual));
  } // Evaluate
  
  private double fSle(RealIndividual ri) {
    double fitness = 0.0;
    double x = 0.0;

    int len = ri.getLength();

    for(int i = 0; i < len; i++)
    {
        for (int j = 0; j < len; j++)
        {
	    x = ri.getRealAllele(j);
            fitness += a[i][j]*x;
        }
        fitness = fitness - b[i];
    }
   fitness = Math.abs(fitness);

   return fitness;
  } // Sle
} 
