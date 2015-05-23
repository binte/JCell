
/**
 * @author Bernabe Dorronsoro
 *
 * 
 * Class for the adaptive grid archive of individuals 
 * for the multiobjective cEA
 * 
 */

package MO;

import jcell.*;
//import java.io.*;

public class AdaptiveGrid extends Archive
{
   
	public int [] hypercube;		// Hypercube division for keeping diversity
	public double [] divisionSize; 	// Division sizes of the adaptive grid
	//public double [] gridLimits; 	// Limits of the adaptive grid
	public int currentGridSize; 	// Current size of the adaptive grid
	public int mostCrowdedHypercube;

	//public int numberOfFunctions;
	public int depth;
    
	public double [] upperBestFitness;
	public double [] lowerBestFitness;
	
	private int [] increment;
	private double [] tmpDivisionSize;
  
	public AdaptiveGrid()
	{
		super();
	}
	
	public AdaptiveGrid(int numberOfFunctions)
	{
		super();
		this.numberOfFunctions = numberOfFunctions;
		depth = 5;
		
		currentGridSize = (int) Math.floor(Math.pow(2.0, (double) depth * (double) numberOfFunctions));
		
		hypercube = new int[currentGridSize];
		divisionSize = new double[numberOfFunctions];
		
		upperBestFitness = new double[numberOfFunctions];
		lowerBestFitness = new double[numberOfFunctions];  
		
		tmpDivisionSize = new double[numberOfFunctions];
		increment       = new int[numberOfFunctions];
	}
	
	public AdaptiveGrid(int depth, int numberOfFunctions)
	{
		super();
		this.numberOfFunctions = numberOfFunctions;
		this.depth = depth;
		
		currentGridSize = (int) Math.floor(Math.pow(2.0, (double) depth * (double) numberOfFunctions));
		
		hypercube = new int[currentGridSize];
		divisionSize = new double[numberOfFunctions];
		
		upperBestFitness = new double[numberOfFunctions];
		lowerBestFitness = new double[numberOfFunctions];  
		
		tmpDivisionSize = new double[numberOfFunctions];
		increment       = new int[numberOfFunctions];
	}
	
	public void setNumberOfFunctions(int numberOfFunctions)
	{
		this.numberOfFunctions = numberOfFunctions;
		depth = 5;
		
		currentGridSize = (int) Math.floor(Math.pow(2.0, (double) depth * (double) numberOfFunctions));
		
		hypercube = new int[currentGridSize];
		divisionSize = new double[numberOfFunctions];
		
		upperBestFitness = new double[numberOfFunctions];
		lowerBestFitness = new double[numberOfFunctions];  
		
		tmpDivisionSize = new double[numberOfFunctions];
		increment       = new int[numberOfFunctions];
	}

	public void updateGridLocations(Archive arch, Individual individual) 
	{
		
		for (int i = 0; i < numberOfFunctions; ++i) 
		{
			upperBestFitness[i] = Integer.MIN_VALUE;
			lowerBestFitness[i] = Integer.MAX_VALUE;
		}
		
		int archSize = arch.getSize();
		
		for (int i = 0; i < numberOfFunctions; i++) 
		{
			if (((Double[])individual.getFitnessValues())[i].doubleValue() < lowerBestFitness[i])
				lowerBestFitness[i] = ((Double[])individual.getFitnessValues())[i].doubleValue();
			
			if (((Double[])individual.getFitnessValues())[i].doubleValue() > upperBestFitness[i])
      			upperBestFitness[i] = ((Double[])individual.getFitnessValues())[i].doubleValue();
      		
      		for (int j = 0; j < archSize; j ++) 
      		{
      			if (((Double[])arch.getIth(j).getFitnessValues())[i].doubleValue() < lowerBestFitness[i]) 
      				lowerBestFitness[i] = ((Double[])arch.getIth(j).getFitnessValues())[i].doubleValue();
      				
      			if (((Double[])arch.getIth(j).getFitnessValues())[i].doubleValue() > upperBestFitness[i]) 
      				upperBestFitness[i] = ((Double[])arch.getIth(j).getFitnessValues())[i].doubleValue();
      		} // for
      		
      		divisionSize[i] = (upperBestFitness[i] - lowerBestFitness[i]) ;
    		// gridLimits[i]   = lowerBestFitness[i] - (divisionSize[i] / 2.0) ;
		} // for
		
		int len = hypercube.length; //(int) pow(2.0, numberOfFunctions * depth);
		for (int i = 0; i < len ; i++)
			hypercube[i] = 0;
			
		int location;
		mostCrowdedHypercube = 0;
		location = findLocation(individual);
		individual.setGridLocation(location);
		hypercube[location] ++;
		
		for (int i = 0; i < archSize; i++) 
		{
			location = findLocation(arch.getIth(i));
			arch.getIth(i).setGridLocation(location);
			hypercube[location] ++;
			
			if (hypercube[location] > hypercube[mostCrowdedHypercube])
				mostCrowdedHypercube = location;
		} // for
	}
	
	
	// Finds the location of the individual in the adaptive grid
	public int findLocation(Individual individual) 
	{
		int location = 0;
		int counter = 1;
		
		for (int i = 0; i < numberOfFunctions; i++) 
		{
			increment[i] = counter;
			counter *= 2;
			tmpDivisionSize[i] = divisionSize[i];
		} // for
		
		for (int i = 1; i <= depth; i++) 
		{
			for (int j = 0; j < numberOfFunctions; j++) 
			{
				if (((Double[])individual.getFitnessValues())[j].doubleValue() < 
					(tmpDivisionSize[j]/2 + lowerBestFitness[j]))
					
					location += increment[j];
				else
					lowerBestFitness[j] += tmpDivisionSize[j] / 2.0;
			}
			
			for (int j = 0; j < numberOfFunctions; j++) 
			{
				increment[j] *= numberOfFunctions * 2;
				tmpDivisionSize[j] /= 2.0;
			} // for
		} // for
		
		return location;
	}
	
	public boolean Insert(Individual solution)//(int location, IndividualCMoEA solution)
	{
		int result;
		boolean finish = false;
		int counter = 0;
		boolean storeNewSolution = true;
		RealIndividual auxInd;
		
		// CASE1. The archive is empty
		//		  Action: add solution to the archive
		
		if (getNumStoredSols() == 0)
		{
			//if (! Add(solution)) 
			//	System.err.println("Archive.java: The archive is alreeady full. Not possible to add a new solution.");
			finish = true;
		}
		
		//while ((counter < getNumStoredSols()) && !finish)
		while ((counter < getSize()) && !finish)
		{
			if (archive[counter] != null)
			{	
				if (solution.identicalFitness(archive[counter]))
				{
					finish = true;
					storeNewSolution = false;
				}
				else
				{
					result = solution.numberOfViolatedConstraintsTest(archive[counter]);
					if (result  == 0)
						result = solution.dominanceTest(archive[counter]);
					if (result == 1)
					{
						finish = true;
						storeNewSolution = false;
					}
					else if (result == -1) 
					{
						Delete(counter);
						counter++;
					}
					else
						counter++;
				}
			}
			else counter++;
		}
		if (storeNewSolution)
		{
			if (getNumStoredSols() < getSize())
			{
				Add(solution);
			}
			else // The archive is full
			{
				int location;
				// If the location is not the most crowded region, add it
				location = solution.getGridLocation();
				if (location == mostCrowdedHypercube)	
					storeNewSolution = false;
				
				else // Find and replace an individual of the most crowded region
				{
					finish = false;
					int i = 0;
					while (!finish)
					{
						location = archive[i].getGridLocation();
						if (location == mostCrowdedHypercube)
						{
							Delete(i);
							updateGridLocations(this,solution);
							Add(solution);
							finish = true;
						}
						i++;
						if (i == getNumStoredSols())
							finish = true;
					}
				}
			}
		}
		
		return storeNewSolution;
	}
	
	public Object clone()
    {
      AdaptiveGrid ag;
      
      ag = new AdaptiveGrid(this.depth, this.numberOfFunctions);
      
      for (int i=0; i<this.getSize(); i++)
         	if (archive[i] == null) ag.archive[i] = null;
      		else ag.archive[i] = (Individual) archive[i].clone();
      
      ag.setSize(this.getSize());
      ag.setNumStoredSols(this.getNumStoredSols());
      
      
      
      ag.currentGridSize = currentGridSize;
      for (int i=0; i<this.divisionSize.length; i++)
      	ag.divisionSize[i] = divisionSize[i];
      
      for (int i=0; i<this.hypercube.length; i++)
      	ag.hypercube[i] = hypercube[i];
      	
      for (int i=0; i<this.increment.length; i++)
      	ag.increment[i] = increment[i];
      
      for (int i=0; i<this.lowerBestFitness.length; i++)
      	ag.lowerBestFitness[i] = lowerBestFitness[i];
      	
      ag.mostCrowdedHypercube = mostCrowdedHypercube;
      ag.numberOfFunctions = numberOfFunctions;
      for (int i=0; i<this.tmpDivisionSize.length; i++)
      	ag.tmpDivisionSize[i] = tmpDivisionSize[i];
      
      for (int i=0; i<this.tmpDivisionSize.length; i++)
      	ag.upperBestFitness[i] = upperBestFitness[i];
      
      return ag;
   }
   	
}
