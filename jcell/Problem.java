
/**
 * @author Sergio Romero Leiva
 * 
 * Modified by Bernabe Dorronsoro
 *
 * Abstract class for defining problems
 * 
 */

package jcell;
import java.util.Vector;

public abstract class Problem
{
   protected int nEvals; // number of evaluations
   protected int variables;
   protected int functions   = 1;
   protected int constraints = 0;
   protected boolean testing = false;
   protected double maxFitness = 0.0; // For single objective optimization
   protected Vector maxAllowedValues = null;
   protected Vector minAllowedValues = null;
   protected String dataFile;
   
   
   public Problem(String dataFile) {
	   
	   this.dataFile = dataFile;
   }
   
   
   public void reset()
   {
      nEvals = 0;
   }
   
   public int getNEvals()
   {
      return nEvals;
   }
   
   public int getVariables() {
	   
	   return this.variables;
   }
   
   public int numberOfObjectives()
   {
   	return this.functions;
   }
   
   public int numberOfConstraints()
   {
   	return this.constraints;
   }
   
   public boolean testing() {
	   
	   return this.testing;
   }
   
   public double getMaxFitness()
   {
   	return maxFitness;
   }
   
   public Vector getMaxAllowedValues()
   {
   	return maxAllowedValues;
   }
   
   public Vector getMinAllowedValues()
   {
   	return minAllowedValues;
   }
   
   public void setNumberOfVariables(int num)
   {
   	this.variables = num;
   }
   
   public void setNumberOfObjectives(int num)
   {
  	functions = num;
   }
   
   public void setNumberOfConstraints(int num)
   {
   	constraints = num;
   }
   
   public void setTesting(boolean flag) {
	   
	   this.testing = flag;
   }
   
   public void setMaxFitness(double num)
   {
   	maxFitness = num;
   }
   
   public void setMaxAllowedValues(Vector values)
   {
   	maxAllowedValues = values;
   }
   
   public void setMinAllowedValues(Vector values)
   {
   	minAllowedValues = values;
   }
   
   public void setDataFile(String dataFile)
   {
	   this.dataFile = dataFile;
   }
      
   
   public Object evaluate(Individual ind)
   {
      Object fit = eval(ind); // calcular o valor de fitness ap—s uma corrida do algoritmo construtivo
      
      this.nEvals++; // increase number of evaluations
      ind.setFitness(fit); // set the fitness to the individual
      
      if (fit.getClass() == Double[].class)
      {
      	int constr = computeNumberOfViolatedConstraints(ind);
      	ind.setNumberOfViolatedConstraints(constr);
      }
      
      return fit;
   }
   
   // Compute fitness of all the individuals in 'pop'
   public void evaluatePopulation(Population pop)
   {
      int popSize = pop.getPopSize();
      
      for (int i=0; i<popSize; i++) 
         evaluate(pop.getIndividual(i));
   }
   
   public abstract Object eval(Individual ind);
   
   public int computeNumberOfViolatedConstraints(Individual ind)
   {
   	return 0;
   }
}
