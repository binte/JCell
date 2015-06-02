/**
 * @author Sergio Romero Leiva
 * 
 * Modified by Bernabe Dorronsoro
 *
 * Implements an individual with chromosome of integer numbers
 * 
 */

package jcell;

import java.io.IOException;
import java.util.Random;
import java.util.Vector;

public class TopIndividual extends Individual
{
   protected int alleles[];
   
   // Create a new individual with 10 empty genes (to be initialized)
   public TopIndividual()
   {
   	super();

   	int len = 10;
   	
   	alleles = new int[len];
   	minAlleleValues = null;
    maxAlleleValues = null;
    
   	for (int i=0; i<len; i++)
        alleles[i] = Integer.MIN_VALUE;
   }
   
   // Create a new individual with 'len' empty genes (to be initialized)
   public TopIndividual(int len)
   {
   	super(len);
   	  	
   	alleles = new int[len];
   	Vector minAlleleValues = null;
    Vector maxAlleleValues = null;
    
   	for (int i=0; i<len; i++)
        alleles[i] = Integer.MIN_VALUE;
   }
   
   // Create a new individual with 'len' empty genes (to be initialized) 
   // having the minimum and maximum allowed values specified in minValue and maxValue vectors, respectively
   public TopIndividual(int len, Vector minValue, Vector maxValue)
   {
      super(len);
      minAlleleValues = minValue;
      maxAlleleValues = maxValue;
      alleles = new int[len];
      
      for (int i=0; i<len; i++)
         alleles[i] = ((Integer)minAlleleValues.elementAt(i)).intValue();
   }
   
   
   // Return the maximum (when min = false) or minimum (when min = true) allowed values for each variable
   public Vector getMinMaxAlleleValue(boolean min)
   {
      return min?minAlleleValues:maxAlleleValues;
   }
   
   // Get the allele value in position 'locus'
   // Returns an Integer object
   public Object getAllele(int locus)
   {
      return new Integer(alleles[locus]);
   }

   // Get the allele value in position 'locus'
   // Returns an integer value
   public int getIntegerAllele(int locus)
   {
      return alleles[locus];
   }
      
   // Set the allele value in position 'locus'
   // The parameters are an Integer object and the 'locus' position
   public void setAllele(int locus, Object allele)
   {
      int value = ((Integer)allele).intValue();
      setIntegerAllele(locus,value);
   }

   // Set the allele value in position 'locus'
   // The parameters are an integer value and the 'locus' position
   public void setIntegerAllele(int locus, int allele)
   {
   	  if ((minAlleleValues != null) && (allele < ((Integer)minAlleleValues.elementAt(locus)).intValue()))
         allele = ((Integer)minAlleleValues.elementAt(locus)).intValue();
      if ((maxAlleleValues != null) && (allele > ((Integer)maxAlleleValues.elementAt(locus)).intValue()))
         allele = ((Integer)maxAlleleValues.elementAt(locus)).intValue();
         
      alleles[locus] = allele;
   }
   
   // Mutates the gene in position 'locus' assigning it a random value 
   // in the allowed range
   // Considerei que os vectores recebidos s‹o sempre nulos. Se isso mudar, Ž necess‡rio alterar os restantes
   // 
   public void mutate(Random r, int locus)
   {  
   	  if ((minAlleleValues!=null) && (maxAlleleValues!=null))
   		  alleles[locus] = r.nextInt(((Integer)maxAlleleValues.elementAt(locus)).intValue() - ((Integer)minAlleleValues.elementAt(locus)).intValue()) + ((Integer)minAlleleValues.elementAt(locus)).intValue();
   	  else
   		  if ((minAlleleValues!=null) && (maxAlleleValues==null))
   			  alleles[locus] = r.nextInt(Integer.MAX_VALUE - ((Integer)minAlleleValues.elementAt(locus)).intValue()) + ((Integer)minAlleleValues.elementAt(locus)).intValue();
   		  else 
   			  if ((minAlleleValues==null) && (maxAlleleValues!=null))
   				  alleles[locus] = r.nextInt(((Integer)maxAlleleValues.elementAt(locus)).intValue());
   			  else 
   				  if ((minAlleleValues==null) && (maxAlleleValues==null))
   					  alleles[locus] = r.nextInt(10);
   }
   
   // Mutates the gene in position 'locus' assigning it a random value 
   // in the allowed range
   // Considerei que os vectores recebidos s‹o sempre nulos. Se isso mudar, Ž necess‡rio alterar os restantes
   // 
   public void mutate(Random r, int locus, int lastWeight)
   {  
   	  if ((minAlleleValues!=null) && (maxAlleleValues!=null))
   		  alleles[locus] = r.nextInt(((Integer)maxAlleleValues.elementAt(locus)).intValue() - ((Integer)minAlleleValues.elementAt(locus)).intValue()) + ((Integer)minAlleleValues.elementAt(locus)).intValue();
   	  else
   		  if ((minAlleleValues!=null) && (maxAlleleValues==null))
   			  alleles[locus] = r.nextInt(Integer.MAX_VALUE - ((Integer)minAlleleValues.elementAt(locus)).intValue()) + ((Integer)minAlleleValues.elementAt(locus)).intValue();
   		  else 
   			  if ((minAlleleValues==null) && (maxAlleleValues!=null))
   				  alleles[locus] = r.nextInt(((Integer)maxAlleleValues.elementAt(locus)).intValue());
   			  else 
   				  if ((minAlleleValues==null) && (maxAlleleValues==null))
   					  alleles[locus] = r.nextInt(lastWeight);
   }
   
   // Changes the length of the chromosome
   public void setLength(int len)
   {
      super.setLength(len);
      alleles = new int[len];
      
      for (int i=0; i<len; i++)
      	if (minAlleleValues == null)
      		alleles[i] = Integer.MIN_VALUE;
      	else
         alleles[i] = ((Integer)minAlleleValues.elementAt(i)).intValue();
      
   }
   
   // Set random values to every gene in position 'locus' assigning them random values 
   // in their allowed ranges
   public void setRandomValues(Random r)
   {
      for (int i=0; i<len; i++)
      	if ((minAlleleValues!=null) && (maxAlleleValues!=null))
  	      alleles[i] = ((Integer)minAlleleValues.elementAt(i)).intValue() + 
  	         r.nextInt((((Integer)maxAlleleValues.elementAt(i)).intValue() - 
  	         		Math.abs(((Integer)minAlleleValues.elementAt(i)).intValue())));
     	  else if ((minAlleleValues!=null) && (maxAlleleValues==null))
  	      alleles[i] = ((Integer)minAlleleValues.elementAt(i)).intValue() + 
  	         r.nextInt((Integer.MAX_VALUE - 
  	         		Math.abs(((Integer)minAlleleValues.elementAt(i)).intValue())));
     	  else if ((minAlleleValues==null) && (maxAlleleValues!=null))
     	  	alleles[i] = (Integer.MIN_VALUE + 
  	         r.nextInt(((Integer)maxAlleleValues.elementAt(i)).intValue() - 
  			 		Math.abs(Integer.MIN_VALUE)));
     	else if ((minAlleleValues==null) && (maxAlleleValues==null))
  	      alleles[i] = r.nextInt();
   }
   
   public void setValues(int value)
   {
      for (int i=0; i<len; i++)
  	      alleles[i] = value;
   }
   
   public void copyIndividual(Individual ind)
   {
      TopIndividual iInd = (TopIndividual)ind;
      
      for (int i=0; i<len; i++)
         alleles[i] = iInd.getIntegerAllele(i);
      fitness = iInd.getFitness();
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
      TopIndividual ind;
      
      if (!(obj instanceof TopIndividual))
         return false;
         
      ind = (TopIndividual)obj;
      if (ind.getLength() != len)
         return false;
         
      for (int i=0; i<len; i++)
         if (ind.getIntegerAllele(i) != alleles[i])
            return false;
      return true;
   }
   
   public Object clone()
   {
      TopIndividual ind;
      
      ind = (TopIndividual)super.clone();
      ind.alleles = (int[])alleles.clone();
      
      return ind;
   }
   
   public boolean isEqualTo(Individual ind)
	{
	    TopIndividual topInd = (TopIndividual) ind;
	    
		if (!this.identicalFitness(topInd))
			return false;
		
		int length = this.getLength();
		
		if (length != topInd.getLength())
			return false;
		
		for (int i=0; i<length; i++)
		{
			if(this.getIntegerAllele(i)!=topInd.getIntegerAllele(i))
				return false;
		}
		
		return true;
	}
   
   public boolean hasRepeatedGenes()
   {
	   for (int i=0; i<len-1; i++)
		   for (int j=i+1; j<len; j++)
			   if (alleles[i]==alleles[j])
				   return true;
	   return false;
   }
   
   // Force all the genes to be different 
   public void setDifferentGenes(Random r)
   {
	   for (int i=0; i<len-1; i++)
		   for (int j=i+1; j<len; j++)
			   if (alleles[i]==alleles[j])
			   {
				   boolean rightGene = true;
				   int val = 0;
				   while (!rightGene)
				   {
					   rightGene = true;
					   val = r.nextInt(((Integer)maxAlleleValues.elementAt(j)).intValue());
					   
					   // Check if the new gene to be inserted is not already in the chromosome
					   for (int k=0; k<len; k++)
						   if (alleles[k] == val)
							   rightGene = false;
				   }
			   }
   }
}
