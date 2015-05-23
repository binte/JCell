/**
 * @author Stefan Janson
 *
 * Dissimilarity selection operator. Selects, from two random individuals in the neighborhood, 
 * the one with more different chromosome to the current individual  
 * 
 */
   
package operators.selection;

import jcell.*;
import java.awt.Point;
import java.util.Random;

import HcGA.*;

public class DisSimilarityTournamentSelection implements Operator
{
   private Random r;
   
   public DisSimilarityTournamentSelection(Random r)
   {
      this.r = r;
   }
   
   public Object execute(Object o) {
	   return execute(o, null);
   }
   
   // Parameter 'o' is a list of individuals (the neighborhood)
   // The function returns an Integer pointing an individual of the neighborhood
   public Object execute(Object o, Point p)
   {
      Integer ind1, ind2;
      Individual iv[] = (Individual[])o;
	   ind1 = new Integer(r.nextInt(iv.length));
      Individual i1 = iv[ind1.intValue()];
	   ind2 = new Integer(r.nextInt(iv.length));
      Individual i2 = iv[ind2.intValue()];
	  Individual pair1[] = new Individual[2];
	  Individual pair2[] = new Individual[2];
	  //pair1[0] = iv[p.x];
	  pair1[0] = iv[0];
	  pair1[1] = i1;
	  //pair2[0] = iv[p.x];
	  pair2[0] = iv[0];
	  pair2[1] = i2;
      
      if (Hierarchy.hammingDistance(pair1) >= Hierarchy.hammingDistance(pair2))
         return ind1;
      else
         return ind2;
   }
}
