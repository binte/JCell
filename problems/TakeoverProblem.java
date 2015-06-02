/* ------------------------------------------
   File: TakeoverProblem.java
   Author: Bernabé Dorronsoro
   Description
   It defines a specific problem for computing
   the takeover time of an algorithm
   ------------------------------------------*/

package problems;

import jcell.*; //Use jcell package

public class TakeoverProblem extends Problem
{
	private double maxValue;
	
	public TakeoverProblem(String dataFile, double _maxValue) {
		
		super(dataFile);
		this.maxValue = _maxValue;
		
	}
	
   // Overwrite eval method from Problem class.
   // Returns the fitness of Individual ind
   public Object eval(Individual ind)
   {
	   return new Double(((BinaryIndividual) ind).binaryToDecimal(0,10));
   		//return ind.getFitness();
   }
}
