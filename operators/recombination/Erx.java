/* ---------------------------------------------------------
   Fichero: Erx.java
   Autor:   Sergio Romero Leiva
   Descripcion
   Cruce por recombinacion de ejes para genotipo permutacion
   Operador cruce ERX
   ---------------------------------------------------------*/

/**
 * @author Sergio Romero
 *
 * Edge recombination crossover
 * 
 */

package operators.recombination;

import java.util.Random;

import jcell.Individual;
import jcell.Operator;
import jcell.PermutationIndividual;

public class Erx implements Operator
{
   public Random r;
   public int edgeTable[][]; // adjacencies
   public int numEdges[]; // alive edges
   public boolean active[]; // for storing whether edges are alive or not
   
   public Erx(Random r)
   {
      this.r = r;
   }
   
   private void insertEdge(int n, int e)
   {
      for (int i=0; i<4; i++)
      {
         if (edgeTable[n][i] == e) // repeated edge
            return;
         if (edgeTable[n][i] == -1) // empty edge
         {
            edgeTable[n][i] = e;
            numEdges[n]++;
            return;
         }
      }
   }
   
   private void setup(PermutationIndividual i1, PermutationIndividual i2)
   {
      int i, j, c10, c1, c11, c20, c2, c21, len = i1.getLength();
      
      // Initializes the table of edges
      for (i=0; i<i1.getLength(); i++)
      {
         for (j=0; j<4; j++)
            edgeTable[i][j] = -1;
         active[i] = true;
         numEdges[i] = 0;
      }
      c1 = i1.getIntegerAllele(0);
      c10 = i1.getIntegerAllele(1);
      c2 = i2.getIntegerAllele(0);
      c20 = i2.getIntegerAllele(1);
      insertEdge(c1,c10);
      insertEdge(c2,c20);
      
      c1 = i1.getIntegerAllele(len-1);
      c10 = i1.getIntegerAllele(len-2);
      c2 = i2.getIntegerAllele(len-1);
      c20 = i2.getIntegerAllele(len-2);
      insertEdge(c1,c10);
      insertEdge(c2,c20);
      
      for (i=1; i<i1.getLength()-1; i++)
      {
         c1 = i1.getIntegerAllele(i);
         c10 = i1.getIntegerAllele(i-1);
         c11 = i1.getIntegerAllele(i+1);
         c2 = i2.getIntegerAllele(i);
         c20 = i2.getIntegerAllele(i-1);
         c21 = i2.getIntegerAllele(i+1);
         insertEdge(c1,c10);
         insertEdge(c1,c11);
         insertEdge(c2,c20);
         insertEdge(c2,c21);
      }
   }
   
   // Returns the first free element
   private int getRandom()
   {
      for (int i=0; i<active.length; i++)
         if (active[i])
            return i;
      return -1;
   }
   
   // remove a node and decrement references
   private void kill(int n)
   {
      int c;
      
      active[n] = false;
      for (int i=0; i<4; i++)
      {
         c = edgeTable[n][i];
         if (c != -1 && active[c])
            numEdges[c]--;
      }
   }
   
   private int next(int n)
   {
      int c, cmin=-1, edges;
      
      edges = 5;
      for (int i=0; i<4; i++)
      {
         c = edgeTable[n][i];
         if (c != -1 && active[c])
            if (numEdges[c] < edges)
            {
               edges = numEdges[c];
               cmin = c;
            }
      }
      
      if (edges == 5)
         return getRandom();
      else
         return cmin;
   }
   
   public Object execute(Object o)
   {
      Individual iv[] = (Individual[])o;
      PermutationIndividual i1, i2, newInd;
      int c1, c2, len;
      
      newInd = (PermutationIndividual)iv[0].clone();
      i1 = (PermutationIndividual)iv[0];
      i2 = (PermutationIndividual)iv[1];
      len = i1.getLength();
      
      // create tables
      if (active == null || active.length != len)
      {
         edgeTable = new int[len][4];
         numEdges = new int[len];
         active = new boolean[len];
      }
      
      setup(i1,i2);
      c1 = i1.getIntegerAllele(0);
      c2 = i2.getIntegerAllele(0);
      
      if (numEdges[c2] < numEdges[c1])
         c1 = c2;

      newInd.setIntegerAllele(0,c1);
      kill(c1);
      for (int i=1; i<len; i++)
      {
         c1 = next(c1);
         newInd.setIntegerAllele(i,c1);
         kill(c1);
      }
      
      return newInd;
   }
}
