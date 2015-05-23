
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
 * The problem: Constr_Ex multi-objctive problem
 * 
 */

package problems.MO;

import jcell.*; //Use jcell package
import java.util.Vector;

public class Constr_Ex extends Problem
{

    public  final static int    longitCrom		= 2; // Number of variables of the Kursawe function
  	private final static int    numberOfFuncts	= 2;


	public Constr_Ex(CellularGA cmoea)
	{
		super(); 
		
		variables   = longitCrom;
	    constraints = 2;
	  	functions   = numberOfFuncts;
	  	
		maxAllowedValues = new Vector(variables);
		maxAllowedValues.add(0,new Double(1.0));
		maxAllowedValues.add(1,new Double(5.0));
		cmoea.setParam(CellularGA.PARAM_UPPER_LIMIT, maxAllowedValues);
		minAllowedValues = new Vector(variables);
		minAllowedValues.add(0,new Double(0.1));
		minAllowedValues.add(1,new Double(0.0));
		cmoea.setParam(CellularGA.PARAM_LOWER_LIMIT, minAllowedValues);
	  	
	  	Target.maximize = false;
	}
   // Overwrite eval method from Problem class.
   // Returns the fitness of Individual ind
   public Object eval(Individual ind)
   {
   	  RealIndividual ri = (RealIndividual)ind;
      
      Double [] fitness = new Double[functions];
      
      fitness[0]=fitness[1] = new Double(0.0);
      
      double [] x = new double[variables];
      
      x[0] = ri.getRealAllele(0) ;
      x[1] = ri.getRealAllele(1) ;

      // First function
      fitness[0] = new Double(x[0]) ;

      // Second function
      fitness[1] = new Double((1.0 + x[1]) / x[0]) ;
  
      return fitness;
   }

   public int computeNumberOfViolatedConstraints(Individual indiv)
   {
   		RealIndividual ri = (RealIndividual)indiv;
		double [] x = new double[variables];
		double [] constraintValue = new double[constraints];
		
		int numberOfViolatedConstraints = 0;
		
		x[0] = ri.getRealAllele(0);
		x[1] = ri.getRealAllele(1);
		
		constraintValue[0] = (x[1]  + 9.0*x[0])/6.0 - 1.0 ;
        constraintValue[1] = (-x[1] + 9.0*x[0])/1.0 - 1.0 ;
        
        for (int i = 0 ; i < constraints; i++) 
    		if (constraintValue[i] < 0) {
      			numberOfViolatedConstraints ++ ;
    		} // if
        
		return numberOfViolatedConstraints;
	}
}