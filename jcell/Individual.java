/**
 * @author Sergio Romero Leiva
 * 
 * Modified by Bernabe Dorronsoro
 *
 * Implements a generic template for individuals
 * 
 */

package jcell;

import java.util.Random;
import java.util.Vector;

import HcGA.*;

public abstract class Individual implements Cloneable
{
   protected int len; // Number of genes 
   protected Object fitness; // fitness value
   
   // For the multiobjective case
   private int gridLocation;	// if an adaptive grid is used
   protected int numberOfViolatedConstraints;
   protected int numberOfFuncts = 1;
   
   protected double distance;
   protected int strength;
   protected int strengthRawFitness;
   // For the multiobjective case
   
   // For the hierarchical model
   int level;	// The level of the hierarchy an individual is on
   int x,y;		//the cellular position
   int lastMove = Hierarchy.MOVE_NO;
   int noMoveCount;
   // For the hierarchical model
   
   protected Vector minAlleleValues = null; // Minimum allowed value for every gene
   protected Vector maxAlleleValues = null; // Maximum allowed value for every gene
      
   public Individual()
   {
      len = 0;
      fitness = new Double(0.0);
   }
   
   public Individual(int len)
   {
      this.len = len;
      fitness = new Double(0.0);
      minAlleleValues = new Vector(len);
      maxAlleleValues = new Vector(len);
   }
   
   // For the multiobjective case
   public Individual(int len, int functs)
   {
      this.len = len;
      
      minAlleleValues = new Vector(len);
      maxAlleleValues = new Vector(len);
      
      fitness = new Double [functs];
      for (int i=0; i<functs; i++)
      	((Double [])fitness)[i] = new Double(0.0);
      
      numberOfFuncts = functs;
      numberOfViolatedConstraints = 0;
      
      distance = Double.MAX_VALUE;
      strength = -1;
      strengthRawFitness = -1;
   }
   
   // For the multiobjective case
   public Individual(int len, int functs, int sizeArch)
   {
      this.len = len;
      fitness = new Double [functs];
      minAlleleValues = new Vector(len);
      maxAlleleValues = new Vector(len);
      for (int i=0; i<functs; i++)
      	((Double [])fitness)[i] = new Double(0.0);
      	
      numberOfFuncts = functs;
      
      distance = Double.MAX_VALUE;
      strength = -1;
      strengthRawFitness = -1;
   }
   // For the multiobjective case
   
   public int getLength()
   {
      return len;
   }
   
   public Object getFitness()
   {
      return fitness;
   }
   
   
   public void setLength(int len)
   {
      this.len = len;
   }
   
   public void setFitness(Object fitness)
   {
   	if (fitness.getClass() == Double.class) // Single objective 
      this.fitness = new Double(((Double)fitness).doubleValue());
   	else									// Multiple Objectives
   	{
   		int length = ((Double[])fitness).length;
   		this.fitness = new Double[length];
 	    for (int i=0; i<length; i++)
 	    	((Double [])this.fitness)[i] = new Double(((Double[])fitness)[i].doubleValue());
   	}
   }
   
   public Object clone()
   {
      Object o = null;
      
      try
      {
         o = super.clone();
      }
      catch(CloneNotSupportedException e)
      {
      }
      
      ((Individual)o).setFitness(this.fitness);
      
      return o;
   }
   
   // For the multiobjective case
   public Object getFitnessValues()
   {
   	  Double [] aux;
   	  
   	  aux = new Double[numberOfFuncts];
   	  for (int i=0; i< numberOfFuncts; i++)
   	  	aux[i] = ((Double [])fitness)[i];
   	  
      return aux;
   }
   
   public int getNumberOfFuncts()
   {
   	return numberOfFuncts;
   }
   
   public void setNumberOfFuncts(int functs)
   {
   	numberOfFuncts = functs;
   	fitness = new Double[functs];
   	for (int i=0; i<functs; i++)
      	((Double[])fitness)[i] = new Double(0.0);
   }
   
   public void setMinMaxAlleleValue(boolean min, Vector value)
   {
      if (min)
         minAlleleValues = value;
      else
         maxAlleleValues = value;
   }
   
   public void setGridLocation(int gridLoc)
   {
   	gridLocation = gridLoc;
   }
   
   public int getGridLocation()
   {
   	return gridLocation;
   }
   
   public boolean identicalFitness(Individual individual) 
	{

	   if (getNumberOfFuncts() == 1)
	   {
		   return (((Double) fitness).doubleValue() == ((Double)individual.getFitness()).doubleValue()); 
	   }
		for (int i = 0; i < getNumberOfFuncts(); i++)
			if (((Double [])fitness)[i].doubleValue() != ((Double[])individual.getFitness())[i].doubleValue())
				return false ;
		return true ;  

	}
	
	// Compares the number of violated constraints of two individuals
	// returns:-1 if this individual violates less constraints than the the individual passed as parameter
	//          1 if the individual passed as parameter violates less constraints than the current individual
	//          0 otherwise
	
	public int numberOfViolatedConstraintsTest(Individual individual) 
	{
		int result = 0;
		
		if (this.numberOfViolatedConstraints > individual.numberOfViolatedConstraints)
			result =  1 ;
		else if (this.numberOfViolatedConstraints < individual.numberOfViolatedConstraints)
			result = -1 ;
		
		return result ;
		
	}
	
	public int getNumberOfViolatedConstraints()
	{
		return numberOfViolatedConstraints;
	}
	
	public void setNumberOfViolatedConstraints(int numberOfViolatedConstraints)
	{
		this.numberOfViolatedConstraints = numberOfViolatedConstraints;
	}
	
	// Makes a dominance test. the individual dominates b if it violates a lesser 
	// number of constraints than b, or the number of violated constraints 
	// of the individual and b is the same, and the fitness value of a for
	// every function is better (lower if maximize=false and larger if maximize=true) 
	// than the fitness functions of b
	// returns -1 if the individual dominates ind
	// 		    1 if ind dominates the individual
	//	 	    0 if nor the individual dominates b neither b dominates the individual
	
	public int dominanceTest(Individual ind)
	{
  		
		int i = 0;
		int last = 0;
		int current; 
 		int  result = 0;
 		boolean finished = false;
 		 		
 		result = numberOfViolatedConstraintsTest(ind);
 		if (result != 0) 
 			finished = true;
 			
 		while (!finished) 
     	{
 			if (!Target.maximize)
 			{
 				if (((Double[])this.getFitness())[i].doubleValue() < ((Double [])ind.getFitness())[i].doubleValue()) 
 					current = -1;
 				else if (((Double[])this.getFitness())[i].doubleValue() > ((Double [])ind.getFitness())[i].doubleValue()) 
 					current = 1;
 				else 
 					current = 0;
 			}
 			
 			else 
 			{
 				if (((Double[])this.getFitness())[i].doubleValue() < ((Double [])ind.getFitness())[i].doubleValue()) 
 					current = 1;
 				else if (((Double[])this.getFitness())[i].doubleValue() > ((Double [])ind.getFitness())[i].doubleValue()) 
 					current = -1;
 				else 
 					current = 0;
 			}
     		
     		if ((current != 0) && (current == -last)) 
     		{
			    finished = true;
			    result   = 0;
		    }
		    else
		    {
		    	if (current != 0)
		    		last = current;
		    	
		    	i++;
		    	if (i == this.getNumberOfFuncts())
		    	{
		    		finished = true;
		    		result = last;
		    	}
		    	if (i > this.getNumberOfFuncts())
		    		return 1;
		    }
     	}
     	
		return result;
  }
	
	public void setStrength(int st)
	{
		strength = st;
	}
	
	public int getStrength()
	{
		return strength;
	}
	
	public void setStrengthRawFitness(int st)
	{
		strengthRawFitness = st;
	}
	
	public int getStrengthRawFitness()
	{
		return strengthRawFitness;
	}
	
	public void setDistance(double dst)
	{
		distance = dst;
	}
	
	public double getDistance()
	{
		return distance;
	}
	
   // For the multiobjective case
   
   public abstract Object getAllele(int locus); // Returns the value of gene in locatino 'locus'
   public abstract void setAllele(int locus, Object allele); //  assigns value 'allele' in 'locus'
   public abstract void mutate(Random r, int locus); // Mutates gene in 'locus'
   public abstract void setRandomValues(Random r); // Assigns random values to the genes
   public abstract void copyIndividual(Individual ind); // Copies the information in 'ind' into another one
   
   // For the hierarchical model
   public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getLastMove() {
		return lastMove;
	}
	
	public void setLastMove(int lastMove) {
		this.lastMove = lastMove;
		if (lastMove == Hierarchy.MOVE_NO)
			noMoveCount++;
		else 
			noMoveCount = 0;
	}
	
	public int getNoMoveCount() {
		return noMoveCount;
	}
	
	public void setNoMoveCount(int noMoveCount) {
		this.noMoveCount = noMoveCount;
	}
	//For the hierarchical model
	
	// Abstract method for checking if two individuals contain the same info
	public abstract boolean isEqualTo(Individual ind);
	
}
