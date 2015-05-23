/**
 * @author Sergio Romero Leiva
 * 
 * Modified by Bernabe Dorronsoro
 *
 * Implements an individual whose chromosome is a permutation of integer numbers
 * 
 */
   
package jcell;

import java.util.Random;
import java.util.Vector;

public class PermutationIndividual extends IntegerIndividual
{
	// Creates a new permutation individual with length 10
   public PermutationIndividual()
   {
      this(10);
   }
   
   // Creates a new permutation individual with length 'len'
   public PermutationIndividual(int len)
   {
      super(len);
      
      minAlleleValues = new Vector(len);
      maxAlleleValues = new Vector(len);
      alleles = new int[len];
      
      for (int i=0; i<len; i++)
      {
      	 minAlleleValues.add(new Integer(0));
      	 maxAlleleValues.add(new Integer(len-1));
         alleles[i] = i;
      }
   }
   
   // Modifies the length of the chromosome
   public void setLength(int len)
   {
      super.setLength(len);
      alleles = new int[len];
      
      for (int i=0; i<len; i++)
        alleles[i] = i;
   }
   
   // The mutation is to swap the values of two genes
   public void mutate(Random r, int locus)
   {
      swap(locus,r.nextInt(len));
   }
   
   // Assigns a random permutation chromosome to the individual
   public void setRandomValues(Random r)
   {
      int i, j, tmp;
      
      for (i=0; i<len; i++)
         alleles[i] = i;
      for (i=1; i<len; i++)
      {
         j = r.nextInt(i+1);
         swap(i,j);
      }
   }
   
   // Swap positions i y j
   public void swap(int i, int j)
   {
      int tmp = alleles[i];
      alleles[i] = alleles[j];
      alleles[j] = tmp;
   }
   
   // Inverts the order of the genes between two points
   public void inversion(int i, int j)
   {
      int aux;
      
      if (j < i)
      {
         aux = i;
         i = j;
         j = aux;
      }
      
      while (j > i)
      {
         swap(i,j);
         i++;
         j--;
      }
   }
   
   // Removes the ith gene and inserts it in position j
   public void relocate(int i, int j)
   {
      int aux, v;
      
      if (i == j)
         return;
      
      aux = i;
      v = alleles[i];
      if (i < j)
         while (aux < j)
         {
            alleles[aux] = alleles[aux+1];
            aux++;
         }
      else
         while (aux > j)
         {
            alleles[aux] = alleles[aux-1];
            aux--;
         }
      alleles[j] = v;
   }
   
   public boolean isEqualTo(Individual ind)
	{
	    PermutationIndividual intInd = (PermutationIndividual) ind;
	    
		if (!this.identicalFitness(intInd))
			return false;
		
		int length = this.getLength();
		
		if (length != intInd.getLength())
			return false;
		
		for (int i=0; i<length; i++)
		{
			if(this.getIntegerAllele(i)!=intInd.getIntegerAllele(i))
				return false;
		}
		
		return true;
	}
}
