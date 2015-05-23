
/**
 * @author Sergio Romero Leiva
 * 
 * Modified by Bernabe Dorronsoro
 *
 * Implements an individual with chromosome of Double type numbers
 * 
 */


package jcell;

import java.util.Random;
import java.util.Vector;

public class RealIndividual extends Individual
{
   protected double alleles[]; // Chromosome
   
   // Create a new individual with 10 empty genes (to be initialized)
   public RealIndividual()
   {
	   super();
   }
   
   // Create a new individual with 'len' empty genes (to be initialized)
   public RealIndividual(int len)
   {
   	//this(len,new Vector(len),new Vector(len));
   	super(len);
   }
   
   // Create a new individual with 'len' empty genes (to be initialized) 
   // having the minimum and maximum allowed values specified in minValue and maxValue vectors, respectively
   public RealIndividual(int len, Vector minValues, Vector maxValues)
   {
      super(len);
      minAlleleValues = copyVector(minValues);
      maxAlleleValues = copyVector(maxValues);
      alleles = new double[len];
      
      for (int i=0; i<len; i++)
      	alleles[i] = ((Double)minAlleleValues.get(i)).doubleValue();
   }
   
   // For the multiobjective case
   public RealIndividual(int len, int functs, Vector minValues, Vector maxValues)
   {
      super(len, functs);
      minAlleleValues = (Vector)minValues.clone();
      maxAlleleValues = (Vector)maxValues.clone();
      alleles = new double[len];
      
      for (int i=0; i<len; i++)
      	alleles[i] = ((Double)minAlleleValues.get(i)).doubleValue();
   }
   
   // Return the maximum (when min = false) or minimum (when min = true) allowed values for each variable
   public Vector getMinMaxAlleleValue(boolean min)
   {
      return min?minAlleleValues:maxAlleleValues;
   }
   
   // Get the allele value in position 'locus'
   // Returns a Double object
   public Object getAllele(int locus)
   {
      return new Double(alleles[locus]);
   }
   
   // Get the allele value in position 'locus'
   // Returns a double value
   public double getRealAllele(int locus)
   {
      return alleles[locus];
   }
   
   // Set the allele value in position 'locus'
   // The parameters are a Double object and the 'locus' position
   public void setAllele(int locus, Object allele)
   {
      double value = ((Double)allele).doubleValue();
      setRealAllele(locus,value);
   }
   
   // Set the allele value in position 'locus'
   // The parameters are a double value and the 'locus' position
   public void setRealAllele(int locus, double allele)
   {
   	if (allele < ((Double) minAlleleValues.get(locus)).doubleValue())
   		allele  = ((Double) minAlleleValues.get(locus)).doubleValue();
   	if (allele > ((Double) maxAlleleValues.get(locus)).doubleValue())
        allele  = ((Double) maxAlleleValues.get(locus)).doubleValue();
         
      alleles[locus] = allele;      
   }
   
   // Mutates the gene in position 'locus' assigning it a random value 
   // in the allowed range
   public void mutate(Random r, int locus)
   {
   	alleles[locus] = ((Double) minAlleleValues.get(locus)).doubleValue() +
    r.nextDouble() * (((Double) maxAlleleValues.get(locus)).doubleValue() - 
    ((Double) minAlleleValues.get(locus)).doubleValue());
   }
   
   
   // method added for non-uniform mutation
   
    public void nuMutate(Random r, int locus, int generation, int generationLimit)
    {

	double y;
	double b=5; // degree of dependency on iteration number

	if (r.nextDouble() < 0.5) {

		y=(((Double) maxAlleleValues.get(locus)).doubleValue()-alleles[locus]);
	    alleles[locus]=alleles[locus]+y*(1-Math.pow(r.nextDouble(), Math.pow(1.-(generation/generationLimit), b)));
	}
	else {

		y=(alleles[locus]-((Double) minAlleleValues.get(locus)).doubleValue());
	    alleles[locus]=alleles[locus]-y*(1-Math.pow(r.nextDouble(), Math.pow(1.-(generation/generationLimit), b)));
	}

    }
   
    // Changes the length of the chromosome
   public void setLength(int len)
   {
      super.setLength(len);
      alleles = new double[len];
      
      for (int i=0; i<len; i++)
      	alleles[i] = ((Double) minAlleleValues.get(i)).doubleValue();
   }
   
   // Set random values to every gene in position 'locus' assigning them random values 
   // in their allowed ranges
   public void setRandomValues(Random r)
   {
      for (int i=0; i<len; i++)
      	alleles[i] = ((Double) minAlleleValues.get(i)).doubleValue() + 
        r.nextDouble()*(((Double) maxAlleleValues.get(i)).doubleValue() - ((Double) minAlleleValues.get(i)).doubleValue());
   }
   
   
   public void copyIndividual(Individual ind)
   {
      RealIndividual rInd = (RealIndividual)ind;
      
      for (int i=0; i<len; i++)
         alleles[i] = rInd.getRealAllele(i);
      this.setFitness(rInd.getFitness());
   }
   
   public String toString()
   {
      StringBuffer sb = new StringBuffer("[");
      
      for (int i=0; i<len; i++)
         sb.append(alleles[i]+" ");
      sb.append("] Fitness: "+fitness);
      
      return sb.toString();
   }
   
   public boolean equals(Object obj)
   {
      RealIndividual ind;
      //double delta= (maxAlleleValue - minAlleleValue) / 1.0E+3;
      
      if (!(obj instanceof RealIndividual))
         return false;
         
      ind = (RealIndividual)obj;
      if (ind.getLength() != len)
         return false;
         
      for (int i=0; i<len; i++)
      {
      	//delta = (((Double)maxAlleleValues.get(i)).doubleValue() - 
		//		    ((Double)minAlleleValues.get(i)).doubleValue()) / 1.0E+3;
        // if (Math.abs(ind.getRealAllele(i) - alleles[i]) > delta)
    	  if (ind.getRealAllele(i) == alleles[i])
            return false;
      }
      return true;
   }
   
   public Vector copyVector(Vector v)
   {
   	Vector copiedVector = null;
   	
   	if (v!=null)
   	{
   		copiedVector = new Vector(v.size());
   	
	   	for (int i=0; i<v.size(); i++)
	   	{
	   		copiedVector.add(i, new Double(((Double)v.get(i)).doubleValue()));
	   	}
   	}
   	return copiedVector;
   }
   
   public Object clone()
   {
      RealIndividual ind;
      
      ind = (RealIndividual)super.clone();
      
      ind.alleles = (double[])alleles.clone();
      ind.minAlleleValues = copyVector(minAlleleValues);
      ind.maxAlleleValues = copyVector(maxAlleleValues);
      
      return ind;
   }
   
   public void checkAlleleRanges()
   {
   	for (int i=0; i<len; i++)
   	{
	   	double allele = getRealAllele(i);
	   	if (minAlleleValues.size() == 1)
	   	{
			if (allele < ((Double)minAlleleValues.firstElement()).doubleValue())
			{
				allele = ((Double)minAlleleValues.firstElement()).doubleValue();
				setRealAllele(i,allele);
			}
			else if (allele > ((Double)maxAlleleValues.firstElement()).doubleValue())
			{
				allele = ((Double)maxAlleleValues.firstElement()).doubleValue();
				setRealAllele(i,allele);
			}
	   	}
		else
		{
			if (allele < ((Double)minAlleleValues.elementAt(i)).doubleValue())
			{
				allele = ((Double)minAlleleValues.firstElement()).doubleValue();
				setRealAllele(i,allele);
			}
			else if (allele > ((Double)maxAlleleValues.elementAt(i)).doubleValue())
			{
				allele = ((Double)maxAlleleValues.firstElement()).doubleValue();
				setRealAllele(i,allele);
			}
		}
    }
   }
   
   public boolean isEqualTo(Individual ind)
	{
	    RealIndividual realInd = (RealIndividual) ind;
	    
		if (!this.identicalFitness(realInd))
			return false;
		
		int length = this.getLength();
		
		if (length != realInd.getLength())
			return false;
		
		for (int i=0; i<length; i++)
		{
			if(this.getRealAllele(i)!=realInd.getRealAllele(i))
				return false;
		}
		
		return true;
	}
}
