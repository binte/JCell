
/**
 * @author Bernabe Dorronsoro
 *
 * Fuzzy Connectives Based Crossover (Herrera et al. 94).
 * In this implementation, the fuzzy crossover method
 * to apply is randomly selected between the 12 possibilities.
 * 
 */


package operators.recombination;
import java.util.Random;
import java.util.Vector;

import jcell.Individual;
import jcell.Operator;
import jcell.RealIndividual;

public class FCBX implements Operator
{
	
   Random r;
   Vector minAlleleValue;
   Vector maxAlleleValue;
   
   private double lambda = 0.35;
   
   public FCBX(Random r)
   {
      this.r = r;
   }
   
   public void setLambda(double lambda)
   {
   	this.lambda = lambda;
   }
   
   public double getLambda()
   {
   	return lambda;
   }
   
   public Object execute(Object o)
   {
   	Individual iv[] = (Individual[])o;
    
   	RealIndividual ind[] = new RealIndividual[2];
        
    ind[0] = (RealIndividual) iv[0];
    ind[1] = (RealIndividual) iv[1];
    
   	int len = ind[0].getLength();
   	minAlleleValue = ind[0].getMinMaxAlleleValue(true);
   	maxAlleleValue = ind[0].getMinMaxAlleleValue(false);
   	RealIndividual child = new RealIndividual(len, minAlleleValue, maxAlleleValue);

   	switch (r.nextInt(12))
	{
   		case 1: 
   			for (int i = 0; i<len; i++)
   				child.setRealAllele(i,logicalMFunction(ind[0].getRealAllele(i), ind[1].getRealAllele(i), lambda)); 
   			break;
   		case 2: 
   			for (int i = 0; i<len; i++)
   				child.setRealAllele(i,hamacherMFunction(ind[0].getRealAllele(i), ind[1].getRealAllele(i), lambda)); 
   			break;
   		case 3: 
   			for (int i = 0; i<len; i++)
   				child.setRealAllele(i,algebraicMFunction(ind[0].getRealAllele(i), ind[1].getRealAllele(i), lambda)); 
   			break;
   		case 4: 
   			for (int i = 0; i<len; i++)
   				child.setRealAllele(i,einsteinMFunction(ind[0].getRealAllele(i), ind[1].getRealAllele(i), lambda)); 
   			break;
   		case 5: 
   			for (int i = 0; i<len; i++)
   				child.setRealAllele(i,logicalFFunction(ind[0].getRealAllele(i), ind[1].getRealAllele(i))); 
   			break;
   		case 6: 
   			for (int i = 0; i<len; i++)
   	   		child.setRealAllele(i,logicalSFunction(ind[0].getRealAllele(i), ind[1].getRealAllele(i))); 
   			break;
   		case 7: 
   			for (int i = 0; i<len; i++)
   	   		child.setRealAllele(i,hamacherFFunction(ind[0].getRealAllele(i), ind[1].getRealAllele(i))); 
   			break;
   		case 8: 
   			for (int i = 0; i<len; i++)
   	   		child.setRealAllele(i,hamacherSFunction(ind[0].getRealAllele(i), ind[1].getRealAllele(i))); 
   			break;
   		case 9: 
   			for (int i = 0; i<len; i++)
   	   		child.setRealAllele(i,algebraicFFunction(ind[0].getRealAllele(i), ind[1].getRealAllele(i))); 
   			break;
   		case 10: 
   			for (int i = 0; i<len; i++)
   	   		child.setRealAllele(i,algebraicSFunction(ind[0].getRealAllele(i), ind[1].getRealAllele(i))); 
   			break;
   		case 11: 
   			for (int i = 0; i<len; i++)
   	   		child.setRealAllele(i,einsteinFFunction(ind[0].getRealAllele(i), ind[1].getRealAllele(i))); 
   			break;
   		case 12: 
   			for (int i = 0; i<len; i++)
   	   		child.setRealAllele(i,einsteinSFunction(ind[0].getRealAllele(i), ind[1].getRealAllele(i))); 
   			break;
	}
  	return child;
   }
   
   // @brief Maps the gene from [lowerBound..upperBound] to [0..1]
   // @param gene The gene to map
   // @return The mapped gene
   
   double map(double gene) {
   	double minAlleleV = ((Double)minAlleleValue.firstElement()).doubleValue();
   	double maxAlleleV = ((Double)maxAlleleValue.firstElement()).doubleValue();
     return ( (gene - minAlleleV) / (maxAlleleV - minAlleleV));
   } //map

   // @brief Unmaps a value from [0..1] to [lowerBound..upperBound]
   // @param value The value to unmap
   // @return The unmapped value
    
   double unmap(double value) {
   	double minAlleleV = ((Double)minAlleleValue.firstElement()).doubleValue();
   	double maxAlleleV = ((Double)maxAlleleValue.firstElement()).doubleValue();
     return ( minAlleleV + (maxAlleleV - minAlleleV) * value);
   } //unmap

   // @brief Calculates the M Function with the Logical family of fuzzy conectives 
   // @param geneParent1 The first gene to cross
   // @param geneParent2 The second gene to cross
   // @param lambda The parameter LAMBDA of the M Function
   // @return A double with the result of the M Function
   double logicalMFunction(double geneParent1, double geneParent2, double lambda) 
   {
     double mappedAllele1 = map(geneParent1);
     double mappedAllele2 = map(geneParent2);
     double tmp;
    
     try {
       tmp = (1.0 - lambda)* mappedAllele1 + lambda * mappedAllele2;
     } //try
     catch(Exception e) {
       if (r.nextDouble() <= 0.5)
           return geneParent1;
       else
           return geneParent2;
     } //catch    
     return unmap(tmp);
   } // logicalMFunction

    // @brief Calculates the M Function with the Hamacher family of fuzzy conectives
    // @param geneParent1 The first gene to cross
    // @param geneParent2 The second gene to cross
    // @param lambda The parameter LAMBDA of the M Function
    // @return A double with the result of the M Function
   double hamacherMFunction (double geneParent1, double geneParent2, double lambda) 
   {
     double mappedAllele1 = map(geneParent1);
     double mappedAllele2 = map(geneParent2);
     double tmp;

     try {
       tmp = (mappedAllele1 * mappedAllele2)
            /
             (mappedAllele2 - mappedAllele2*lambda + mappedAllele1*lambda) ;
     } //try
     catch(Exception e) {
       if (r.nextDouble() <= 0.5)
           return geneParent1;
       else
           return geneParent2;
     } //catch
    
     return unmap(tmp);
   } //hamacherMFunction

    // @brief Calculates the M Function with the Algebraic family of fuzzy conectives
    // @param geneParent1 The first gene to cross
    // @param geneParent2 The second gene to cross
    // @param lambda The parameter LAMBDA of the M Function
    // @return A double with the result of the M Function
   double algebraicMFunction(double geneParent1, double geneParent2, double lambda) 
   {
     double mappedAllele1 = map(geneParent1);
     double mappedAllele2 = map(geneParent2);
     double tmp;
    
     try {
       tmp = Math.pow(mappedAllele1,(1.0 - lambda)) * Math.pow(mappedAllele2,lambda);
     } //try
     catch(Exception e) {
       if (r.nextDouble() <= 0.5)
           return geneParent1;
       else
           return geneParent2;
     } //catch
    
     return unmap(tmp);

   } //algebraicMFunction

    // @brief Calculates the M Function with the Einstein family of fuzzy conectives
    // @param geneParent1 The first gene to cross
    // @param geneParent2 The second gene to cross
    // @param lambda The parameter LAMBDA of the M Function
    // @return A double with the result of the M Function
   double einsteinMFunction (double geneParent1, double geneParent2, double lambda) 
   {
     double mappedAllele1 = map(geneParent1);
     double mappedAllele2 = map(geneParent2);
     double tmp;
    
     try {
       tmp = 2.0
            /
             (
                  1.0 +
                  (
                   Math.pow( ((2.0 - mappedAllele1) / mappedAllele1),(1.0 - lambda))
                   *
                   Math.pow( ((2.0 - mappedAllele2) / mappedAllele2), lambda)
                  )
             );
     } //try
     catch(Exception e) {
       if (r.nextDouble() <= 0.5)
           return geneParent1;
       else
           return geneParent2;
     } //catch
    
     return unmap(tmp);

   } //einsteinMFunction

    // @brief Calculates the F Function with the Logical family of fuzzy conectives
    // @param geneParent1 The first gene to cross
    // @param geneParent2 The second gene to cross
    // @return A double with the result of the F Function
   double logicalFFunction  (double geneParent1,double geneParent2) 
   {
     if (geneParent1 <= geneParent2)
       return geneParent1;
     else
       return geneParent2;
   } //logicalFFunction

    // @brief Calculates the S Function with the Logical family of fuzzy conectives
    // @param geneParent1 The first gene to cross
    // @param geneParent2 The second gene to cross
    // @return A double with the result of the S Function
   double logicalSFunction  (double geneParent1,double geneParent2) 
   {
     if (geneParent1 >= geneParent2)
       return geneParent1;
     else
       return geneParent2;
   } //logicalSFunction

    // @brief Calculates the F Function with the Hamacher family of fuzzy conectives
    // @param geneParent1 The first gene to cross
    // @param geneParent2 The second gene to cross
    // @return A double with the result of the F Function
   double hamacherFFunction (double geneParent1,double geneParent2) 
   {
     double mappedAllele1 = map(geneParent1);
     double mappedAllele2 = map(geneParent2);
     double tmp;
    
     try {
       tmp = (mappedAllele1 * mappedAllele2)
            /
             (mappedAllele1 + mappedAllele2 - ( mappedAllele1 * mappedAllele2));
     } //try
     catch(Exception e) {
       if (r.nextDouble() <= 0.5)
           return geneParent1;
       else
           return geneParent2;
     } //catch
    
     return unmap(tmp);
   } //hamacherFFunction

    // @brief Calculates the S Function with the Hamacher family of fuzzy conectives
    // @param geneParent1 The first gene to cross
    // @param geneParent2 The second gene to cross
    // @return A double with the result of the S Function
   double hamacherSFunction (double geneParent1,double geneParent2) 
   {
     double mappedAllele1 = map(geneParent1);
     double mappedAllele2 = map(geneParent2);
     double tmp;
    
     try {
       tmp = (mappedAllele1 + mappedAllele2 - (2.0 * mappedAllele1 * 
   mappedAllele2))
            /
             (1.0 - ( mappedAllele1 * mappedAllele2));
     } //try
     catch(Exception e) {
       if (r.nextDouble() <= 0.5)
           return geneParent1;
       else
           return geneParent2;
     } //catch
    
     return unmap(tmp);
   } //hamacherSFunction

    // @brief Calculates the F Function with the Algebraic family of fuzzy conectives
    // @param geneParent1 The first gene to cross
    // @param geneParent2 The second gene to cross
    // @return A double with the result of the F Function
   double algebraicFFunction(double geneParent1,double geneParent2) 
   {
     double mappedAllele1 = map(geneParent1);
     double mappedAllele2 = map(geneParent2);
     double tmp;
    
     try {
       tmp = (mappedAllele1 * mappedAllele2);
     } //try
     catch(Exception e) {
       if (r.nextDouble() <= 0.5)
           return geneParent1;
       else
           return geneParent2;
     } //catch
    
     return unmap(tmp);

   } //algebraicFFunction

    // @brief Calculates the S Function with the Algebraic family of fuzzy conectives
    // @param geneParent1 The first gene to cross
    // @param geneParent2 The second gene to cross
    // @return A double with the result of the S Function
   double algebraicSFunction(double geneParent1,double geneParent2) 
   {
     double mappedAllele1 = map(geneParent1);
     double mappedAllele2 = map(geneParent2);
     double tmp;
    
     try {
       tmp = (mappedAllele1 + mappedAllele2 - (mappedAllele1 * mappedAllele2)) ;
     } //try
     catch(Exception e) {
       if (r.nextDouble() <= 0.5)
           return geneParent1;
       else
           return geneParent2;
     } //catch
    
     return unmap(tmp);
   } //algebraicSFunction

    // @brief Calculates the F Function with the Einstein family of fuzzy conectives
    // @param geneParent1 The first gene to cross
    // @param geneParent2 The second gene to cross
    // @return A double with the result of the F Function
   double einsteinFFunction (double geneParent1,double geneParent2) 
   {
     double mappedAllele1 = map(geneParent1);
     double mappedAllele2 = map(geneParent2);
     double tmp;
    
     try {
       tmp = (mappedAllele1 * mappedAllele2)
            /
             (
              1.0
              +
              ( (1.0 - mappedAllele1) * (1.0 - mappedAllele2))
             );
     } //try
     catch(Exception e) {
       if (r.nextDouble() <= 0.5)
           return geneParent1;
       else
           return geneParent2;
     } //catch
    
     return unmap(tmp);

   } //einsteinFFunction

   // @brief Calculates the S Function with the Einstein family of fuzzy conectives
   // @param geneParent1 The first gene to cross
   // @param geneParent2 The second gene to cross
   // @return A double with the result of the S Function

   double einsteinSFunction (double geneParent1,double geneParent2) 
   {
     double mappedAllele1 = map(geneParent1);
     double mappedAllele2 = map(geneParent2);
     double tmp;
    
     try {
       tmp = (mappedAllele1 + mappedAllele2)
            /
             (1.0 + ( mappedAllele1 * mappedAllele2));
     } //try
     catch(Exception e) {
       if (r.nextDouble() <= 0.5)
           return geneParent1;
       else
           return geneParent2;
     } //catch
    
     return unmap(tmp);

   } //einsteinSFunction
}