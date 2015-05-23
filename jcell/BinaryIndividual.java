/**
 * @author Sergio Romero Leiva
 * 
 * Modified by Bernabe Dorronsoro
 *
 * Implements an individual with binary chromosome
 * 
 */
   
package jcell;

import java.util.Random;

public class BinaryIndividual extends Individual
{
   protected boolean alleles[]; // binary chromosome
   
   // Create a new individual with 10 empty binary genes (to be initialized)
   public BinaryIndividual()
   {
      this(10);
   }
   
   // Create a new individual with 'len' empty binary genes (to be initialized)
   public BinaryIndividual(int len)
   {
      super(len);
      alleles = new boolean[len];
   }
   
   // Get the allele value in position 'locus'
   // Returns a Boolean object
   public Object getAllele(int locus)
   {
      return new Boolean(alleles[locus]);
   }
   
   // Get the allele value in position 'locus'
   // Returns a boolean value
   public boolean getBooleanAllele(int locus)
   {
      return alleles[locus];
   }
   
   // Set the allele value in position 'locus'
   // The parameters are a Boolean object and the 'locus' position
   public void setAllele(int locus, Object allele)
   {
      alleles[locus] = ((Boolean)allele).booleanValue();
   }
   
   // Set the allele value in position 'locus'
   // The parameters are a boolean value and the 'locus' position
   public void setBooleanAllele(int locus, boolean allele)
   {
      alleles[locus] = allele;
   }
   
   public int getIntValue(int gene, int geneSize, int maxValue, int minValue)
   // Returns the integer number represented
   // as a binary chain between begin and end
   // both of them included
   {
	  int value=0;
	  
	  int end = gene+geneSize;
	  
	  for (int i=gene; i<end; i++)
	  {
		   if (alleles[i])
		       value++;
		   value<<=1;
	  }
	  
	  value>>=1;

	 double val = (double)value / Math.pow(2,geneSize);
     val = val * (maxValue - minValue);
     value = (int) Math.round(val) + minValue;
     
	 return value;
	       
   }
   
   // Flips the value of an allele
   public void mutate(Random r, int locus)
   {
      alleles[locus] = !alleles[locus];
   }
   
   // Modify the chromosome length
   public void setLength(int len) 
   {
      super.setLength(len);
      alleles = new boolean[len];
   }
   
   // Set random values to every gene
   public void setRandomValues(Random r)
   {
      for (int i=0; i<len; i++)
         alleles[i] = r.nextBoolean();
   }
   
   // Copy the values of individual 'ind' to this one 
   public void copyIndividual(Individual ind)
   {
      BinaryIndividual bInd = (BinaryIndividual)ind;
      
      for (int i=0; i<len; i++)
         alleles[i] = bInd.getBooleanAllele(i);
      fitness = bInd.getFitness();
   }
   
   // Returns the decimal value of the number represented
   // as a binary chain bstarting in 'locus' with length 'len' 
   public long binaryToDecimal(int locus, int len)
   {
      long ac = 1L, sum = 0L;
      int i = locus + len - 1;
      
      while (i >= locus)
      {
         sum += ac * (alleles[i]?1L:0L);
         ac *= 2L;
         i--;
      }
      
      return sum;
   }
   
   // Creates an string with the individual information
   public String toString()
   {
      StringBuffer sb = new StringBuffer("[");
      
      for (int i=0; i<len; i++)
         sb.append(alleles[i]?"1":"0");
      sb.append("] Fitness: "+fitness);
      
      return sb.toString();
   }
   
   // Check if obj has the same values than this one
   public boolean isEqualTo(Individual obj)
   {
      BinaryIndividual ind;
      
      if (!(obj instanceof BinaryIndividual))
         return false;
         
      ind = (BinaryIndividual)obj;
      
      if (!this.identicalFitness(ind))
			return false;
      
      if (ind.getLength() != len)
         return false;
         
      for (int i=0; i<len; i++)
         if (ind.getBooleanAllele(i) != alleles[i])
            return false;
      return true;
   }
      
   // returns a new individual with the same values as this one
   public Object clone()
   {
      BinaryIndividual ind;
      
      ind = (BinaryIndividual)super.clone();
      ind.alleles = (boolean[])alleles.clone();
      
      return ind;
   }
}
