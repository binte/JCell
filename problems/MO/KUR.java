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
 * The problem: Kursawe multi-objective problem
 * 
 */

package problems.MO;

import jcell.*; //Use jcell package
import java.util.Vector;

public class KUR extends Problem
{

    public  final static int    longitCrom  	= 3   ; // Number of variables
    public  final static int 	numberOfFuncts	= 2   ;
    private final static double a 				= 0.8 ; // Constant a  
  	private final static int    b				= 3   ; // Constant b


	public KUR(String dataFile, CellularGA cmoea)
	{
		super(dataFile);
		
		maxAllowedValues = new Vector(3);
		maxAllowedValues.add(0,new Double(5.0));
		maxAllowedValues.add(1,new Double(5.0));
		maxAllowedValues.add(2,new Double(5.0));
		cmoea.setParam(CellularGA.PARAM_UPPER_LIMIT, maxAllowedValues);
		minAllowedValues = new Vector(3);
		minAllowedValues.add(0,new Double(-5.0));
		minAllowedValues.add(1,new Double(-5.0));
		minAllowedValues.add(2,new Double(-5.0));
		cmoea.setParam(CellularGA.PARAM_LOWER_LIMIT, minAllowedValues);
		Target.maximize = false;
		
		variables = longitCrom;
		functions = numberOfFuncts;
		
	}
   // Overwrite eval method from Problem class.
   // Returns the fitness of Individual ind
   public Object eval(Individual ind)
   {
      RealIndividual bi = (RealIndividual)ind;
      
      Double [] fitness = new Double[2];
      double aux;
      
      // First function
      double xi,xj;
      
      aux=0.0;
      for (int i = 0; i < variables - 1 ; i++) 
      {
      	xi = bi.getRealAllele(i);  
    	xj = bi.getRealAllele(i+1);
    	aux += -10.0 * Math.exp((-0.2)*Math.sqrt(Math.pow(xi,2)+Math.pow(xj,2)));
  	  }
      fitness[0] = new Double(aux);
  	  // Second function	
  	  
      aux = 0.0;
  	  for (int i = 0; i < variables ; i++) 
  	  {
  	  	xi = bi.getRealAllele(i);
    	aux += Math.pow(Math.abs(xi), a) + 5 * Math.sin(Math.pow(xi, b));
  	  } //for
  	  fitness[1] = new Double(aux);
  			
      return fitness;
   }

   public int computeNumberOfViolatedConstraints(Individual indiv)
   {
		return 0;
   }
}