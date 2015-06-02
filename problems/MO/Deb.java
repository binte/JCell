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
 * The problem: Deb multi-objective problem
 * 
 */

package problems.MO;

import jcell.*; //Use jcell package
import java.util.Vector;

public class Deb extends Problem
{
	
	public final static int longitCrom       = 2;
	public final static int numberOfFuncts   = 2;
    
	public Deb(String dataFile, CellularGA cmoea)
	{
		super(dataFile); 
		
		variables = longitCrom;
	    functions = numberOfFuncts;
	    
		maxAllowedValues = new Vector(variables);
		maxAllowedValues.add(0,new Double(1.0));
		maxAllowedValues.add(0,new Double(1.0));
		cmoea.setParam(CellularGA.PARAM_UPPER_LIMIT, maxAllowedValues);
		
		minAllowedValues = new Vector(variables);
		minAllowedValues.add(0,new Double(0.0));
		minAllowedValues.add(0,new Double(0.0));
		cmoea.setParam(CellularGA.PARAM_LOWER_LIMIT, minAllowedValues);
		
	    Target.maximize = false;
	    
	}
   // Overwrite eval method from Problem class.
   // Returns the fitness of Individual ind
   public Object eval(Individual ind)
   {
      RealIndividual bi = (RealIndividual)ind;
      
      final double alpha = 2.0 ; //!< Constant alpha
	  final double q     = 4.0 ; //!< Constant q
	  
      Double [] fitness = new Double[2];
      double [] x       = new double[2];
      
      x[0] = bi.getRealAllele(0);
      x[1] = bi.getRealAllele(1);

      // First function
      
      fitness[0] =  new Double(x[0]);
  	  
  	  // Second function	
  	  
  	  fitness[1] = new Double((1 + 10 * x[1]) * 
                   (1 - Math.pow(x[0]/(1+10*x[1]), alpha) -
                   (x[0] / (1+10*x[1])* Math.sin(2*3.1416*q*x[0])))) ;
  	  
      return fitness;
   }

   public int computeNumberOfViolatedConstraints(Individual indiv)
   {
		// No restrictions
		return 0;
   }
}