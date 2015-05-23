/* -----------------------------------------------------------
   Fichero: Cx.java
   Autor:   Sergio Romero Leiva
   Descripcion
   Cruce por ciclos para individios de genotipo permutacion.
   Cruce CX
   -----------------------------------------------------------*/

/**
 * @author Sergio Romero
 *
 * Cicle crossover for permutation individuals
 * 
 */

package operators.recombination;

import java.util.Random;

import jcell.Individual;
import jcell.Operator;
import jcell.PermutationIndividual;

public class Cx implements Operator
{
   private int intv[];
   private boolean boolv[];
   private Random r;
   
   public Cx(Random r)
   {
      this.r = r;
   }
   
   public Object execute(Object o)
   {
      Individual iv[] = (Individual[])o;
      PermutationIndividual i1, i2, newInd;
      int i, len = iv[0].getLength();
      
      newInd = (PermutationIndividual)iv[0].clone();
      if (r.nextDouble() < 0.5)
      {
         i1 = (PermutationIndividual)iv[0];
         i2 = (PermutationIndividual)iv[1];
      }
      else
      {
         i1 = (PermutationIndividual)iv[1];
         i2 = (PermutationIndividual)iv[0];
      }
   
      // Starts by choosing the first gene from a random parent
      if (intv == null || intv.length != len)
      {
         intv = new int[len]; // for storing the positions
         boolv = new boolean[len]; // alleles already selected are true here
      }
      for (i=0; i<len; i++)
      {
         intv[i1.getIntegerAllele(i)] = i;
         boolv[i] = false;
      }
      i = 0;
      // Assigns a cicle from the parent to the offspring 
      while (!boolv[i])
      {
         newInd.setIntegerAllele(i,i1.getIntegerAllele(i));
         boolv[i] = true;
         i = intv[i2.getIntegerAllele(i)];
      }
      
      // Assigns the rest of the genes to the offspring
      for (i=0; i<len; i++)
         if (boolv[i])
            continue;
         else
            newInd.setIntegerAllele(i,i2.getIntegerAllele(i));
      
      return newInd;
   }
}
