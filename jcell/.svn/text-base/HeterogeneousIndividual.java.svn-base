
/**
 * @author Bernabe Dorronsoro
 *
 * Implements an individual with heterogeneous chromosome
 * 
 */

package jcell;

import java.util.Random;
import java.util.Vector;

public class HeterogeneousIndividual extends Individual
{
   protected Object heterogeneousAlleles[]; // Chromosome
   protected Vector minAlleleValues = new Vector(); // Minimum value for this gene
   protected Vector maxAlleleValues = new Vector(); // Maximum value for this gene
   protected Vector variableType;
   
   public HeterogeneousIndividual(Vector types)
   {
      this(10,new Vector(10),new Vector(10), types);
      variableType = (Vector) types.clone();
   }
   
   public HeterogeneousIndividual(int len, Vector types)
   {
      this(len,new Vector(len),new Vector(len), types);
      variableType = (Vector) types.clone();
   }
   
   public HeterogeneousIndividual(int len, Vector minValues, Vector maxValues, Vector types)
   {
      super(len);
      variableType = (Vector) types.clone();
      copy(minAlleleValues, minValues);
      copy(maxAlleleValues, maxValues);
      heterogeneousAlleles = new Object[len];
      
      for (int i=0; i<len; i++)
         heterogeneousAlleles[i] = minAlleleValues.get(i);
         
   }
   
   public HeterogeneousIndividual(int len, int functs, Vector minValues, Vector maxValues, Vector types)
   {
      super(len, functs);
      variableType = (Vector) types.clone();
      copy(minAlleleValues, minValues);
      copy(maxAlleleValues, maxValues);
      heterogeneousAlleles = new Object[len];
      
      for (int i=0; i<len; i++)
         heterogeneousAlleles[i] = minAlleleValues.get(i);
         
   }
   
   private void copy (Vector a, Vector b)
   // copies b into a
   {
   	int len = b.size();
   	for (int i=0; i<len; i++)
   		a.add(b.get(i));
   }
   
   // Return the maximum (when min = false) or minimum (when min = true) allowed values for each variable
   public Vector getMinMaxAlleleValue(boolean min)
   {
      return min?minAlleleValues:maxAlleleValues;
   }
   
   public Object getAllele(int locus)
   {
      return heterogeneousAlleles[locus];
   }
   
   // Set the maximum (when min = false) or minimum (when min = true) allowed values for each variable (given in values)
   public void setMinMaxAlleleValue(boolean min, Vector values)
   {
      if (min)
         copy(minAlleleValues, values);
      else
         copy(maxAlleleValues, values);
   }
   
   public void setAllele(int locus, Object allele)
   {
      switch (((Integer)variableType.get(locus)).intValue())
      {
      	case CellularGA.PARAM_INTEGER: heterogeneousAlleles[locus] = allele;
      									 if (((Integer)allele).intValue() < ((Integer)minAlleleValues.get(locus)).intValue())
      								 	   heterogeneousAlleles[locus] = minAlleleValues.get(locus);
			      					     else if (((Integer)allele).intValue() > ((Integer)maxAlleleValues.get(locus)).intValue())
			      						   heterogeneousAlleles[locus] = maxAlleleValues.get(locus);
			      					     break;
      	case CellularGA.PARAM_DOUBLE:	 heterogeneousAlleles[locus] = allele;
      									 if (((Double)allele).doubleValue() < ((Double)minAlleleValues.get(locus)).doubleValue())
			      						   heterogeneousAlleles[locus] = minAlleleValues.get(locus);
			      					     if (((Double)allele).doubleValue() > ((Double)maxAlleleValues.get(locus)).doubleValue())
			      						   heterogeneousAlleles[locus] = maxAlleleValues.get(locus);
			      					     break;
      }
   }
   
   public void mutate(Random r, int locus)
   {
      
      switch (((Integer)variableType.get(locus)).intValue())
      {
      	case CellularGA.PARAM_INTEGER:	heterogeneousAlleles[locus] = new Integer(((Integer) minAlleleValues.get(locus)).intValue() +
								         r.nextInt(((Integer) maxAlleleValues.get(locus)).intValue() - 
								         ((Integer) minAlleleValues.get(locus)).intValue()));
			      					break;
      	case CellularGA.PARAM_DOUBLE:	heterogeneousAlleles[locus] = new Double(((Double) minAlleleValues.get(locus)).doubleValue() +
								         r.nextDouble() * (((Double) maxAlleleValues.get(locus)).doubleValue() - 
								         ((Double) minAlleleValues.get(locus)).doubleValue()));
			      					break;
      }
      
   }
   
   
   public void setLength(int len)
   {
      super.setLength(len);
      heterogeneousAlleles = new Object[len];
      
      for (int i=0; i<len; i++)
         heterogeneousAlleles[i] = minAlleleValues.get(i);
   }
   
   public void setRandomValues(Random r)
   {
   	  for (int i=0; i<len; i++)
   	  	switch (((Integer)variableType.get(i)).intValue())
	    {
	      	case CellularGA.PARAM_INTEGER:	heterogeneousAlleles[i] = new Integer(((Integer) minAlleleValues.get(i)).intValue() +
								        	r.nextInt(((Integer) maxAlleleValues.get(i)).intValue() - 
								        	((Integer) minAlleleValues.get(i)).intValue()));
			      						break;
	      	case CellularGA.PARAM_DOUBLE:	heterogeneousAlleles[i] = new Double(((Double) minAlleleValues.get(i)).doubleValue() + 
            								r.nextDouble()*(((Double) maxAlleleValues.get(i)).doubleValue() - 
            								((Double) minAlleleValues.get(i)).doubleValue()));
				      					break;
	    }
        
   }
   
   public void copyIndividual(Individual ind)
   {
      HeterogeneousIndividual hInd = (HeterogeneousIndividual)ind;
      
      for (int i=0; i<len; i++)
         heterogeneousAlleles[i] = hInd.getAllele(i);
      fitness = hInd.getFitnessValues();
   }
   
   public String toString()
   {
      StringBuffer sb = new StringBuffer("[");
      
      for (int i=0; i<len; i++)
         sb.append(heterogeneousAlleles[i]+" ");
      sb.append("] Fitness: ");
      for (int i=0; i<numberOfFuncts; i++)
         sb.append(((Double[])getFitnessValues())[i]+" ");
      
      return sb.toString();
   }
   
   public boolean equals(Object obj)
   {
      HeterogeneousIndividual ind;
           
      if (!(obj instanceof HeterogeneousIndividual))
         return false;
         
      ind = (HeterogeneousIndividual)obj;
      if (ind.getLength() != len)
         return false;
         
      for (int i=0; i<len; i++)
      {
      
      	switch (((Integer)variableType.get(i)).intValue())
	    {
	      	case CellularGA.PARAM_INTEGER:	if (Math.abs(((Integer)ind.getAllele(i)).intValue() - 
								            ((Integer)heterogeneousAlleles[i]).intValue()) != 0)
								            return false;
								        break;
	      	case CellularGA.PARAM_DOUBLE:	double delta = (((Double)maxAlleleValues.get(i)).doubleValue() - 
				      				    	((Double)minAlleleValues.get(i)).doubleValue()) / 1.0E+3;
				      				 
								        if (Math.abs(((Double)ind.getAllele(i)).doubleValue() - 
								            ((Double)heterogeneousAlleles[i]).doubleValue()) > delta)
								            return false;
								        break;
	    }
      }
      return true;
   }
   
   public Object clone()
   {
      HeterogeneousIndividual ind;
      
      ind = new HeterogeneousIndividual(len, numberOfFuncts, minAlleleValues, maxAlleleValues, variableType);

      for (int i=0; i<len; i++)
      	ind.setAllele(i, getAllele(i));

      ind.variableType = (Vector) variableType.clone();
      ind.setFitness((Double[])getFitnessValues());
      ind.numberOfViolatedConstraints = numberOfViolatedConstraints;
      ind.minAlleleValues = (Vector) minAlleleValues.clone();
      ind.maxAlleleValues = (Vector) maxAlleleValues.clone();

      return ind;
   }
   
   public boolean isEqualTo(Individual ind)
	{
	    HeterogeneousIndividual intInd = (HeterogeneousIndividual) ind;
	    
		if (!this.identicalFitness(intInd))
			return false;
		
		int length = this.getLength();
		
		if (length != intInd.getLength())
			return false;
		
		for (int i=0; i<length; i++)
		{
			if(this.getAllele(i).getClass()!=intInd.getAllele(i).getClass())
				return false;
				
			if ((this.getAllele(i).getClass() == BinaryIndividual.class) && (((Boolean) this.getAllele(i)).booleanValue() != ((Boolean) ind.getAllele(i)).booleanValue()))
				return false;
				
			if ((this.getAllele(i).getClass() == IntegerIndividual.class) && (((Integer) this.getAllele(i)).intValue() != ((Integer) ind.getAllele(i)).intValue()))
				return false;
		
			if ((this.getAllele(i).getClass() == PermutationIndividual.class) && (((Integer) this.getAllele(i)).intValue() != ((Integer) ind.getAllele(i)).intValue()))
				return false;
				
			if ((this.getAllele(i).getClass() == RealIndividual.class) && (((Double) this.getAllele(i)).doubleValue() != ((Double) ind.getAllele(i)).doubleValue()))
				return false;
	
		}
		
		return true;
	}
   
}
