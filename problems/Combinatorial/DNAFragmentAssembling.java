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
 * The problem: DNA fragment assembly problem
 * 
 */

package problems.Combinatorial;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;
import java.util.Vector;

import jcell.*;

public class DNAFragmentAssembling extends Problem {

	public static int longitCrom = 0;
	public static double maxFitness = Double.MAX_VALUE;
	
	private int[][] score;
	private int nbOfFrags = 0;

	public DNAFragmentAssembling(String filename)
	{
		super();
		super.maxFitness = maxFitness;
		Target.maximize = true;

		readInst(filename);
	}

	public void readInst(String filename)
	{
		
		nbOfFrags = 0;
		BufferedReader br = null;
      	StringTokenizer st;
    
		try
      	{
			br = new BufferedReader(new FileReader(filename)); 
        	st = new StringTokenizer(br.readLine());
        	
        	nbOfFrags = (new Integer(st.nextToken())).intValue();
        	score = new int[nbOfFrags][]; 
        	
        	for (int i = 0; i < nbOfFrags ; i++)   // Read all clauses
        	{        
        		score[i] = new int[nbOfFrags];
        		st = new StringTokenizer(br.readLine());
        		for (int j=0; j<nbOfFrags; j++)
        			score[i][j] = (new Integer(st.nextToken())).intValue();
        	}
        	br.close();
        	
        	longitCrom = nbOfFrags;
        	variables = longitCrom;
        }
        catch (Exception e)
      	{
        	System.err.println("ERROR "+e);
      	}
        
	}

	public Object eval(Individual ind)
	{
		// F1 maximization
		double fit = 0.0;
		for(int k = 0; k < nbOfFrags-1; k++)
		{
			int i = ((Integer)ind.getAllele(k)).intValue();
			int j = ((Integer)ind.getAllele(k+1)).intValue();
			fit += Math.abs(score[i][j]);
		}
		// F2 minimization
	
		return new Double(fit);
	}

	public int evalContigs(Individual ind)
	{
		int contigs = 1;
		
		for (int k=0; k<nbOfFrags-1; k++)
		{
			int i = ((Integer)ind.getAllele(k)).intValue();
			int j = ((Integer)ind.getAllele(k+1)).intValue();
			if (score[i][j] == 0) contigs++; 
		}
		
		return contigs;
	}
	
	public int getNbOfFrags()
	{
		return nbOfFrags;
	}
	
	public Object getScore()
	{
		return score;
	}
}
