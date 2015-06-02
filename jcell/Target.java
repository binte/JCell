/**
 * @author Bernabe Dorronsoro
 *
 * Defines whether our goal is to maximize or minimize
 * the fitness function. Some comparisons between solutions are
 * also provided
 * 
 */

package jcell;

public class Target {

	public static boolean maximize = true;
	
	public Target() {
		super();
	}
	
	public static boolean isBetterOrEqual(Object indiv1, Object indiv2)
	{
		Individual ind1 = (Individual) indiv1;
		Individual ind2 = (Individual) indiv2;
		
		if (ind1.getFitness().getClass() == Double.class) // Single Target
		{
			if (maximize)
				return (((Double)ind1.getFitness()).doubleValue() >= ((Double)ind2.getFitness()).doubleValue());
			else
				return (((Double)ind1.getFitness()).doubleValue() <= ((Double)ind2.getFitness()).doubleValue());
		}
		else // if (ind1.getFitness().getClass() == Double[].class) // multiple objectives
			return (ind1.dominanceTest(ind2) <= 0);
	}
	
	public static boolean isBetter(Object indiv1, Object indiv2)
	{
		Individual ind1 = (Individual) indiv1;
		Individual ind2 = (Individual) indiv2;
		if (ind1.getFitness().getClass() == Double.class) // Single Target
		{
			if (maximize)
				return (((Double)ind1.getFitness()).doubleValue() > ((Double)ind2.getFitness()).doubleValue());
			else
				return (((Double)ind1.getFitness()).doubleValue() < ((Double)ind2.getFitness()).doubleValue());
		}
		else // if (ind1.getFitness().getClass() == Double[].class) // multiple objectives
			return (ind1.dominanceTest(ind2) < 0);
	}
	
	public static boolean isBetter(double fitness1, double fitness2)
	{
		if (maximize)
			return fitness1 > fitness2;
		
		else
			return fitness1 < fitness2;
	}
	
	public static boolean isBetterOrEqual(double fitness1, double fitness2)
	{		
		if (maximize) 
			return fitness1 >= fitness2;
		else
			return fitness1 <= fitness2;
	}
	
	public static boolean isWorseOrEqual(double fitness1, double fitness2)
	{
		if (maximize)
			return fitness1 <= fitness2;
		
		else
			return fitness1 >= fitness2;
	}
	
	public static boolean isWorse(double fitness1, double fitness2)
	{
		if (maximize)
			return fitness1 < fitness2;
		
		else
			return fitness1 > fitness2;
	}
	
	public static boolean isWorse(Object indiv1, Object indiv2)
	{
		Individual ind1 = (Individual) indiv1;
		Individual ind2 = (Individual) indiv2;
		if (ind1.getFitness().getClass() == Double.class) // Single Target
		{
			if (maximize)
				return (((Double)ind1.getFitness()).doubleValue() < ((Double)ind2.getFitness()).doubleValue());
			else
				return (((Double)ind1.getFitness()).doubleValue() > ((Double)ind2.getFitness()).doubleValue());
		}
		else // if (ind1.getFitness().getClass() == Double[].class) // multiple objectives
			return (ind1.dominanceTest(ind2) > 0);
	}

	public static int getBest(EvolutionaryAlg ea)
	{
		int bestInd = 0;
		if (Target.maximize)
			  bestInd = ((Integer) ((Statistic) ea.getParam(CellularGA.PARAM_STATISTIC)).getStat(ComplexStats.MAX_FIT_POS)).intValue();
		else
			  bestInd = ((Integer) ((Statistic) ea.getParam(CellularGA.PARAM_STATISTIC)).getStat(ComplexStats.MIN_FIT_POS)).intValue();
		
		return bestInd;
	}
}
