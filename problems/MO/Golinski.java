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
 * The problem: Golinski multi-objective problem
 * 
 */

package problems.MO;

import jcell.*; //Use jcell package
import java.util.Vector;

public class Golinski extends Problem
{

    public  final static int    longitCrom		= 7; // Number of variables
  	private final static int    numberOfFuncts	= 2;


	public Golinski(String dataFile, CellularGA cmoea)
	{
		super(dataFile);
		
		variables   = longitCrom;
	    constraints = 11;
	  	functions   = numberOfFuncts;
	  	
		maxAllowedValues = new Vector(variables);
		maxAllowedValues.add(0,new Double(3.6));
		maxAllowedValues.add(1,new Double(0.8));
		maxAllowedValues.add(2,new Double(28.0));
		maxAllowedValues.add(3,new Double(8.3));
		maxAllowedValues.add(4,new Double(8.3));
		maxAllowedValues.add(5,new Double(3.9));
		maxAllowedValues.add(6,new Double(5.5));
		cmoea.setParam(CellularGA.PARAM_UPPER_LIMIT, maxAllowedValues);
		minAllowedValues = new Vector(variables);
		minAllowedValues.add(0,new Double(2.6));
		minAllowedValues.add(1,new Double(0.7));
		minAllowedValues.add(2,new Double(17.0));
		minAllowedValues.add(3,new Double(7.3));
		minAllowedValues.add(4,new Double(7.3));
		minAllowedValues.add(5,new Double(2.9));
		minAllowedValues.add(6,new Double(5.0));
		cmoea.setParam(CellularGA.PARAM_LOWER_LIMIT, minAllowedValues);
		
	  	Target.maximize = false;
	}
   // Overwrite eval method from Problem class.
   // Returns the fitness of Individual ind
   public Object eval(Individual ind)
   {
      RealIndividual ri = (RealIndividual)ind;
      
      Double [] fitness = new Double[functions];
      
      double [] x = new double[variables];
      for (int i=0; i<variables; i++)
      	x[i] = ri.getRealAllele(i);

      // First function
      
      fitness[0] = new Double(0.7854 * x[0]*x[1]*x[1]*(10*x[2]*x[2]/3 + 
                   14.933*x[2]-43.0934) -
                   1.508*x[0]*(x[5]*x[5]+x[6]*x[6]) +
                   7.477*(x[5]*x[5]*x[5] + x[6]*x[6]*x[6]) +
                   0.7854*(x[3]*x[5]*x[5] + x[4]*x[6]*x[6])) ;
  	  
  	  // Second function	
  	  
  	  double aux = 745.0 * x[3] / (x[1] * x[2]) ; 
      fitness[1] = new Double(Math.sqrt((aux*aux + 1.69e7)) / (0.1*x[5]*x[5]*x[5])) ;
  
      return fitness;
   }

   public int computeNumberOfViolatedConstraints(Individual indiv)
   {
		RealIndividual ri = (RealIndividual)indiv;
		double [] x = new double[variables];
		double [] constraintValue = new double[constraints];
		double aux;
		double function2;

		int numberOfViolatedConstraints = 0;
		int overallConstraintViolation  = 0;
		
		for (int i=0; i<variables; i++)
      		x[i] = ri.getRealAllele(i);

  		aux = 745.0 * x[3] / (x[1] * x[2]) ; 
  		function2 = Math.sqrt((aux*aux + 1.69e7)) / (0.1*x[5]*x[5]*x[5]) ;		
  		
  		constraintValue[0] = - ((1.0/(x[0]*x[1]*x[1]*x[2])) - 1.0/27.0) ;
  		constraintValue[1] = - ((1.0/(x[0]*x[1]*x[1]*x[2]*x[2])) - 1.0/397.5);
		constraintValue[2] = - (x[3]*x[3]*x[3]/(x[1]*x[2]*x[5]*x[5]*x[5]*x[5]) - 1.0/1.93) ;
		constraintValue[3] = - (x[4]*x[4]*x[4]/(x[1]*x[2]*x[6]*x[6]*x[6]*x[6]) - 1.0/1.93) ;
		constraintValue[4] = - (x[1]*x[2] - 40.0);
		constraintValue[5] = - (x[0]/x[1] - 12.0);
		constraintValue[6] = - (5.0 - x[0]/x[1]);
		constraintValue[7] = - (1.9 - x[3] + 1.5*x[5]) ;
		constraintValue[8] = - (1.9 - x[4] + 1.1*x[6]) ;
		constraintValue[9] =   1300 - function2 ;
		constraintValue[10] =  1100 - (Math.sqrt((745*x[4]/(x[1]*x[2]))*
                               (745*x[4]/(x[1]*x[2]))+1.575e8)/
 			                   (.1*x[6]*x[6]*x[6])) ;  

		for (int i = 0 ; i < constraints; i++) 
			if (constraintValue[i] < 0) 
			{
				overallConstraintViolation -= constraintValue[i];
				numberOfViolatedConstraints ++ ;
			} // if
			
		return numberOfViolatedConstraints;
	}
}