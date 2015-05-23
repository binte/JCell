/* ------------------------------------------
   File: SelectionBetween2Best.java
   Author:   Bernabé Dorronsoro
   Description
   
   ------------------------------------------*/

/**
 * @author Bernabe Dorronsoro
 *
 * Seleccion Operator. Returns with a given probability the best
 * individual in the population (small prob.) or the best one 
 * in the neighborhod (higher prob.) 
 * 
 */
   
package operators.selection;

import jcell.*;

import java.util.Random;

public class SelectionBetween2Best implements Operator
{
	private Random r;
	private CellularGA cea;
	
	public SelectionBetween2Best(Random r, CellularGA cea)
	{
		this.r   = r;
		this.cea = cea;
	}
	
   // o is an array of Individual
   public Object execute(Object o)
   {
      Individual iv[] = (Individual[])o;
      Individual ind = iv[0];
      
      Integer bestPos;
      
      if (Target.maximize)
      	bestPos = (Integer)((ComplexStats)cea.getParam(CellularGA.PARAM_STATISTIC)).getStat(ComplexStats.MAX_FIT_POS);
      else
      	bestPos = (Integer)((ComplexStats)cea.getParam(CellularGA.PARAM_STATISTIC)).getStat(ComplexStats.MIN_FIT_POS);
      
      Individual best = (Individual) ((Population)cea.getParam(CellularGA.PARAM_POPULATION)).getIndividual(bestPos.intValue());
      
      Integer ind1 = new Integer(r.nextInt(iv.length));
      Individual i1 = iv[ind1.intValue()];
	  Integer ind2 = new Integer(r.nextInt(iv.length));
      Individual i2 = iv[ind2.intValue()];
      
      if (Target.isBetterOrEqual(i1, i2))
         ind = i1;
      else
         ind = i2;
         
         int generacion = ((Integer)cea.getParam(CellularGA.PARAM_GENERATION_NUMBER)).intValue();
         int finGeneracion = ((Integer)cea.getParam(CellularGA.PARAM_GENERATION_LIMIT)).intValue();
      if (r.nextDouble() <= ((double)generacion/(double)finGeneracion)*0.25)
      	return ind;
      else return best;
   }
}
