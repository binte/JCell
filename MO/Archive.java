
/**
 * @author Bernabe Dorronsoro
 *
 * 
 * Base class for the archive of the multiobjective cEA
 * 
 */

package MO;

import jcell.*;
import java.io.*;

public abstract class Archive implements Cloneable
{
   
	private static final int MAX_SIZE_ARCHIVE = 101;
	//protected IndividualCMoEA [] archive;
	protected Individual [] archive;
	private int size;
	private int numStoredSols;		// number of solutions in the archive
	
	protected int numberOfFunctions;
	   
	//Creates the archive with the maximum possible size
	public Archive()
	{
		//archive = new IndividualCMoEA[MAX_SIZE_ARCHIVE];
		archive = new Individual[MAX_SIZE_ARCHIVE];
		size = MAX_SIZE_ARCHIVE-1;
		numStoredSols = 0;
		for (int i=0; i<size; i++)
			archive[i] = null;
	}
	
	public Archive(int size)
	{
		//archive = new IndividualCMoEA[size];
		archive = new Individual[size];
		if (size >= MAX_SIZE_ARCHIVE) this.size = MAX_SIZE_ARCHIVE-1;
		this.size = size;
		numStoredSols = 0;
		
		for (int i=0; i<size; i++)
			archive[i] = null;
	}
	
	public void initialize(Population pop)
	{
		for (int i=0; i<pop.getPopSize(); i++)
		{
			//this.Insert((IndividualCMoEA) pop.getIndividual(i,j));
			this.Insert((Individual) pop.getIndividual(i));
		}
	}
	
	public void setNumberOfFunctions(int numberOfFunctions)
	{
		this.numberOfFunctions = numberOfFunctions;
	}
	
	//public IndividualCMoEA getIth(int pos)
	public Individual getIth(int pos)
	{
		return archive[pos];
	}
	
	//public void setIth(int pos, IndividualCMoEA ind)
	public void setIth(int pos, Individual ind)
	{
		archive[pos] = ind;
	}

	//public boolean Add(IndividualCMoEA ind)
	public boolean Add(Individual ind)
	{
		boolean added = false;
		if (numStoredSols == 0)
		{
			archive[0] = ind;
			numStoredSols++;
					return true;
		}
		if (numStoredSols < size)
			for (int i=0; i<size; i++)
			{
				if (archive[i] == null)
				{
					archive[i] = ind;
					numStoredSols++;
					return true;
				}
			}

		return added;
	}
	
	public void Delete(int i)
	{
		if ((i <= archive.length) && (archive[i] != null))
		{
			archive[i] = null;
			numStoredSols--;
		}
		
		for (int j = i; j<numStoredSols+1; j++)
			archive[j] = archive[j+1];
	}
	
	public int getNumStoredSols()
	{
		return numStoredSols;
	}
	
	public int getSize()
	{
		return size;
	}
	
	public void setSize(int size)
	{
		this.size = size;
	}
	
	public void setNumStoredSols(int numStoredSols)
	{
		this.numStoredSols = numStoredSols;
	}

	//public void printFile(String filename, ProblemCMoEA problem)
	public void printFile(String filename, Problem problem)
	{
		try
      	{
	        PrintWriter pw = new PrintWriter(new FileWriter(filename+".front",false));
	        PrintWriter pwSol = new PrintWriter(new FileWriter(filename+".sol",false));
	        double res = 0.0;

	        int archSize = getSize();
	        int numberFuncts = problem.numberOfObjectives();
	        int length = problem.getVariables();
	        
			for (int i = 0; i<archSize; i++)
			{
				if (archive[i] != null)
				{
					Individual ind = (Individual)archive[i];
					for (int j = 0; j<numberFuncts; j++)
					{
 						// file '.front'
						res = ((Double[])ind.getFitness())[j].doubleValue();
						//pw.print(-res + "\t");
						pw.print(res + "\t");
						
					}
					for (int j=0;j<length; j++)
					{
						// file '.sol'
						if (ind.getClass().getName().contains("IntegerIndividual"))
							res = ((Integer) ind.getAllele(j)).intValue();
						else
							res = ((Double) ind.getAllele(j)).doubleValue();
						pwSol.print(res + "\t\t");
					}
					pw.println("");
					pwSol.println("");
				}
			}
			pw.close();
			pwSol.close();
		}
		catch (Exception e)
		{
			System.err.println("Archive.java. Error while writing to disk: " + e);
		}
		
	}
	
	public abstract boolean Insert(Individual ind);
	//public abstract boolean Insert(IndividualCMoEA ind);
	
   public abstract Object clone();
  
}