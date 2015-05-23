/**
 * @author Stefan Janson
 *
 * Class for holding parameters about the implied hierarchy
 */

package HcGA;

import java.awt.Point;
import jcell.*;

public class Hierarchy {
	
	   public static final int NO_HIERARCHY=0;
	   
	   //the hierarchy levels are ordered along a ring, all the individuals on the inner circle are on the top level
	   public static final int HIERARCHY_TYPE_RING=1;

	   //also do ud sweep
		public static final int HIERARCHY_TYPE_PYRAMID = 2;
		
		public static final int HIERARCHY_SWAP_FITNESS = 0;
		public static final int HIERARCHY_SWAP_REL_FITNESS = 1;
		public static final int HIERARCHY_SWAP_ENTROPY = 2;
		public static final int HIERARCHY_SWAP_FITNESS_ENTROPY = 3;
		public static final int HIERARCHY_SWAP_FITNESS_MAX = 4;
		
		public static final int MOVE_UP = 1000;
		public static final int MOVE_DOWN = 1001;
		public static final int MOVE_LEFT = 1002;
		public static final int MOVE_RIGHT = 1003;
		public static final int MOVE_NO = 1004;

		   protected double relativeImprovementRequired;
		   
		   protected double swapProbability = 1.0;
		
		   protected int swapDecision;
		   
		   //the CEA that is used, required for getNeighbourhood and getPopulation
		   protected CellularGA cea;
		   protected PopGrid population;
		   protected Neighborhood neighbourhood;
		   protected BinaryIndividual zeroIndividual, oneIndividual;

			public Hierarchy(int type, int swapDecision) {
				this(type, swapDecision, null, 0.0);
			}
		
			public Hierarchy(int type, int swapDecision, CellularGA cea) {
				this(type, swapDecision, cea, 0.0);
			}
			
		public Hierarchy(int type, int swapDecision, CellularGA cea, double relativeImprove) {
			this.hierarchyType = type;
			relativeImprovementRequired = relativeImprove;
			this.swapDecision = swapDecision;
			this.cea = cea;
			swapProbability = ((Double)cea.getParam(EvolutionaryAlg.PARAM_SWAP_PROB)).doubleValue();
		}
	   
	   
	   //what kind of hierarchy is implemented
	   //=0, no hierarchy implemented
	   public int hierarchyType=0;
	   
	   //how often a swap operation is performed
	   protected int swapFrequency=1;
	  


	public boolean decideSwap(Individual[] iv, Point p) {
		boolean swap = false;
		switch (swapDecision) {
		case Hierarchy.HIERARCHY_SWAP_FITNESS:
			swap = decideSwapFitness(iv);
			break;
		case Hierarchy.HIERARCHY_SWAP_FITNESS_MAX:
			swap = decideSwapFitnessMax(iv);
			break;
		case Hierarchy.HIERARCHY_SWAP_REL_FITNESS:
			double best = ((Double)(((Statistic) cea.getParam(CellularGA.PARAM_STATISTIC))).getStat(ComplexStats.MAX_FIT_VALUE)).doubleValue();
			double worst = ((Double)(((Statistic) cea.getParam(CellularGA.PARAM_STATISTIC))).getStat(ComplexStats.MIN_FIT_VALUE)).doubleValue(); 
			swap = decideSwapRelativeFitness(iv,best,worst,relativeImprovementRequired);
			break;
		case Hierarchy.HIERARCHY_SWAP_ENTROPY:
			swap = decideSwapEntropy(iv, p);
			break;
		case Hierarchy.HIERARCHY_SWAP_FITNESS_ENTROPY:
			if (iv[0].getY() == iv[1].getY())
				swap = decideSwapFitness(iv);
			else
				//swap = decideSwapString(iv, p);
				swap = decideSwapEntropy(iv, p);
			break;

		default:
			System.err.println("Select proper swapDecision (Hierarchy.java)");
			System.exit(-1);
			break;
		}
		
		
		return swap;
	}
	
	
	/**
	 * Decide whether to swap the individuals based on their fitness values
	 * @param iv -- an array of 2 individuals, the first one located more centrally
	 * @return whether to swap them
	 */
	public boolean decideSwapFitness(Individual[] iv) {
		boolean swap = false;
		if (((Double)iv[1].getFitness()).doubleValue() > ((Double)iv[0].getFitness()).doubleValue())
			swap=true;
		return swap;
	}
	
	/**
	 * Decide whether to swap the individuals based on their fitness values assuming maximization
	 * @param iv -- an array of 2 individuals, the first one located more centrally
	 * @return whether to swap them
	 */
	public boolean decideSwapFitnessMax(Individual[] iv) {
		boolean swap = false;
		if (((Double)iv[1].getFitness()).doubleValue() < ((Double)iv[0].getFitness()).doubleValue())
			swap=true;
		return swap;
	}

	/**
	 * Decide whether to swap the individuals based on their relative fitness values
	 * @param iv -- an array of 2 individuals, the first one located more centrally
	 * @param best -- the current best fitness value
	 * @param worst -- the current worst fitness value
	 * @param improvementRequired -- the fractional improvement required 
	 * @return whether to swap them
	 */
	public boolean decideSwapRelativeFitness(Individual[] iv, double best, double worst, double improvementRequired) {
		boolean swap = false;
		if ((((Double)iv[1].getFitness()).doubleValue() - ((Double)iv[0].getFitness()).doubleValue())/(best-worst) > improvementRequired)		
			swap=true;
		return swap;
	}

	/**
	 * Decide whether to swap the individuals based on their Hamming distance
	 * @param iv -- an array of 2 individuals, the first one located more centrally
	 * @param entropyRequired -- the minimum entropy value required to swap them
	 * @return whether to swap them
	 */
	public boolean decideSwapDistance(Individual[] iv, int distanceRequired) {
		boolean swap = false;
		
		
		if (hammingDistance(iv) > distanceRequired)
			swap=true;
		return swap;
	}

	/**
	 * Decide whether to swap the individuals based on the entropy within their neighbourhood
	 * @param iv -- an array of 2 individuals, the first one located more centrally
	 * @param p -- the selected cell of the individual higher in the hierarchy
	 * @param entropyRequired -- the minimum entropy value required to swap them
	 * @return whether to swap them
	 */
	public boolean decideSwapEntropy(Individual[] iv, Point p) {//, int entropyRequired) {
		boolean swap = false;

		population = (PopGrid) cea.getParam(CellularGA.PARAM_POPULATION);
		neighbourhood = (Neighborhood) cea.getParam(CellularGA.PARAM_NEIGHBOURHOOD);
		
		Individual[] neighbours = new Individual[neighbourhood.getNeighSize()]; 
		Point[] neighPoints = neighbourhood.getNeighbors(p);
        population.getFromPoints(neighPoints,neighbours);
		
		double e1 = entropy(neighbours, iv[0]);
		double e2 = entropy(neighbours, iv[1]);

		
		if (e2 < e1)
			swap=true;
		return swap;
	}
	
	/**
	 * Calculate the entropy within the bitstrings of the given Individuals
	 * @param iv The individuals among which the entropy is calculated
	 * @param excludeIndividual Exclude one individual
	 * @return
	 */
	double entropy(Individual[] iv, Individual excludeIndividual) {
		double entropy = 0.0;
		
		double[] entropyBit = new double[iv[0].getLength()];
		for (int i = 0; i < entropyBit.length; i++) 
			entropyBit[i] = 0.0;
		

		for (int i = 0; i < iv.length; i++) {
			BinaryIndividual ind = (BinaryIndividual) iv[i];
			//exclude one individual from the neighbourhood
			if (! ind.equals(excludeIndividual)) {
		        for(int k=0;k<ind.getLength();k++)
		        	if (ind.getBooleanAllele(k)) entropyBit[k] += 1.0;
			}
		}

		int n = (iv.length);
		if (excludeIndividual != null)
			n--;
		for(int i = 0; i < iv[0].getLength(); i++)
		{
			double p1 = entropyBit[i]/(double) n;
			if(p1<0.00000001) p1=0.00000001;
			double p0 = (n-entropyBit[i])/(double) n;
			if(p0<0.00000001) p0=0.00000001;
			entropyBit[i] = -p1*Math.log(p1)/Math.log(2.0) - p0*Math.log(p0)/Math.log(2.0);
			entropy += entropyBit[i];
		}
		
		entropy = entropy/(double) iv[0].getLength();
		
		return entropy;
	}
	
	
	/**
	 * Decide whether to swap the individuals based on their similarity to 0-String and 1-String
	 * the more 1s a string has the further down it will move
	 * @param iv -- an array of 2 individuals, the first one located more centrally
	 * @return whether to swap them
	 */
	public boolean decideSwapString(Individual[] iv, Point p) {
		boolean swap = false;
		Individual[] candidates = new Individual[2];
		
		/*population = (PopGrid) cea.getParam(CellularGA.PARAM_POPULATION);
		neighbourhood = (Neighbourhood) cea.getParam(CellularGA.PARAM_NEIGHBOURHOOD);
		
		Individual[] neighbours = new Individual[neighbourhood.getNeighSize()]; 
		Point[] neighPoints = neighbourhood.getNeighbors(p);
        population.getFromPoints(neighPoints,neighbours);
		
		//get the opposite neighbour
		for (int i = 0; i < neighbours.length; i++) {
			if ((neighbours[i].x == iv[0].x)&&(neighbours[i].y != iv[0].y)&&(neighbours[i].y != iv[1].y))
				candidates[1] = neighbours[i];
		}
		candidates[0] = iv[0];
		double e1 = entropy(candidates, null);
		candidates[0] = iv[1];
		double e2 = entropy(candidates, null);*/
		
		if (zeroIndividual == null) {
			zeroIndividual = new BinaryIndividual(iv[0].getLength());
			oneIndividual = new BinaryIndividual(iv[0].getLength());
			for (int i = 0; i < zeroIndividual.getLength(); i++) {
				zeroIndividual.setBooleanAllele(i, false);
				oneIndividual.setBooleanAllele(i, true);
			}
		}
		//swap downwards
		if (iv[0].getY() > iv[1].getY())
			candidates[1] = oneIndividual;
		else
			candidates[1] = zeroIndividual;
		
		candidates[0] = iv[0];
		double e1 = entropy(candidates, null);
		candidates[0] = iv[1];
		double e2 = entropy(candidates, null);
		
		if (e2 < e1)
			swap=true;
		return swap;
	}
	
	
	public static int hammingDistance(Individual[] iv) {
		int dist=0;
		
        if (iv[0] instanceof BinaryIndividual) {
		//binary distance
			BinaryIndividual bi0 = (BinaryIndividual) iv[0];
			BinaryIndividual bi1 = (BinaryIndividual) iv[1];
		
			for (int i = 0; i < bi0.getLength(); i++) {
				if (((Boolean) bi0.getAllele(i)).booleanValue() != ((Boolean) bi1.getAllele(i)).booleanValue())
					dist++;
			}
        }
		return dist;
	}

	public double getSwapProbability() {
		return swapProbability;
	}

	public void setSwapProbability(double swapProbability) {
		this.swapProbability = swapProbability;
	}

	public int getHierarchyType() {
		return hierarchyType;
	}
}