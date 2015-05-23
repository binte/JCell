/**
 * @author Bernabe Dorronsoro
 *
 * PALS local seacrh algorithm for the DNA Fragment Assembly Problem
 * 
 */

package operators.localSearch;

import java.util.Date;
import java.util.Random;
import jcell.*;
import problems.Combinatorial.DNAFragmentAssembling;
import java.util.Vector;

public class PALS implements Operator
{
   private Random r;
   private DNAFragmentAssembling prob; //Problem
   private int max_steps; // Maximum number of iterations
   private static int cutoff = 30;
   
   public PALS(Random r, EvolutionaryAlg ea)
   {
	   this.r = r;
	   if (ea!=null)
	   {
		   this.prob = (DNAFragmentAssembling) ea.getParam(EvolutionaryAlg.PARAM_PROBLEM);
		   this.max_steps = ((Integer) ea.getParam(EvolutionaryAlg.PARAM_LOCAL_SEARCH_STEPS)).intValue();
	   }
   }
   
   public Object execute(Object o)
   {
      int steps = 0;
            
      PermutationIndividual ind = (PermutationIndividual) o;
      
      int len = ind.getLength();
      
      int best_i=0, best_j=0;
      int best_delta_cont = -4;
      int best_delta_fit=0;
      int current_delta_cont;
      int current_delta_fit;
      
      boolean changes = true;
      
      while ((changes) && (steps<=max_steps))
      {
    	best_delta_cont = -4;
    	
    	for (int i=0; i<len-1; i++)
    		for (int j=i+1; j<len; j++)
    		{
    			double [] delta = CalculateDelta(ind, i, j);
    			
    			current_delta_cont = (int)Math.round(delta[1]);
    			current_delta_fit =  (int)Math.round(delta[0]);
    			if((current_delta_cont > best_delta_cont) ||
    				((current_delta_cont == best_delta_cont) && (current_delta_fit > best_delta_fit)))
    			{
    				best_i = i;
    				best_j = j;
    				best_delta_cont = current_delta_cont;
    				best_delta_fit = current_delta_fit;
    			}
    			
    		}
    	if(best_delta_cont >= 0 && !(best_delta_cont == 0 && best_delta_fit == 0))
    	{
    		ApplyMovement(ind,best_i,best_j);
    	}
    	else
    		changes = false;
    	steps++;
      }
      return ind;
   }
   
   private void ApplyMovement(PermutationIndividual ind, int i, int j)
   {
	   while(i<j)
	   {
		   ind.swap(i, j);
		   i++;
		   j--;
	   }
	   
   }
   
   public double[] CalculateDelta(PermutationIndividual ind, int i, int j)
   {
	   double[] delta = new double[2];
	   delta[0] = 0.0; // delta_f
	   delta[1] = 0.0; // delta_c
	   
	   int s_i = ind.getIntegerAllele(i);
	   int s_j = ind.getIntegerAllele(j);
	   
	   int[][] score = (int[][])prob.getScore(); // get the overlap degrees of every pair of fragment 

	   if (i>0)
	   {
		   int s_i1 = ind.getIntegerAllele(i-1);
		   delta[0] -= Math.abs(score[s_i1][s_i]);
		   delta[0] += Math.abs(score[s_i1][s_j]);
		   
		   if(Math.abs(score[s_i1][s_i]) >= cutoff)
			   delta[1]--;
		   if(Math.abs(score[s_i1][s_j]) >= cutoff)
			   delta[1]++;
	   }
	   
	   if(j < score[0].length-1)
	   {
		   int s_j1 = ind.getIntegerAllele(j+1);
		   delta[0] -= Math.abs(score[s_j][s_j1]);
		   delta[0] += Math.abs(score[s_i][s_j1]);

		   if(Math.abs(score[s_j][s_j1]) >= cutoff)
			   delta[1]--;
		   if(Math.abs(score[s_i][s_j1]) >= cutoff)
			   delta[1]++;
	   }
	   
	   return delta;
   }
   
   private Movement bestMovement(Vector L)
   {
	   Movement best=(Movement) L.get(0);
	   
	   int indexBest = 0;
	   boolean moreThanOneBest = false;
	   int len = L.size();
	   
	   for (int i=1; i<len; i++)
	   {
		   Movement m = ((Movement)L.get(i));
		   if (best.delta[1] > m.delta[1])
		   {
			   best = m;
			   indexBest = i;
			   moreThanOneBest = false;
		   }
		   else if (best.delta[1] == m.delta[1])
			   moreThanOneBest = true;
	   }
	   
	   if (moreThanOneBest) 
	   // if there are several solutions having the same best delta_c 
	   {
		   for (int i=indexBest; i<len; i++)
		   {
			   Movement m = ((Movement)L.get(i));
			   if ((best.delta[1] == m.delta[1]) && (Math.abs(best.delta[0]) < Math.abs(m.delta[0])))
				   best = m;
		   }
		   
	   }
	   
	   return best;
   }
   
   private Movement randomMovement(Vector L)
   {
	   return (Movement) L.get(r.nextInt(L.size()));
   }
   
   private class Movement
   {
	   public double[] delta;
	   public int i, j;
	   
	   public Movement()
	   {
		   delta = new double[2];
		   delta[0]=0.0; // delta_f
		   delta[1]=0.0; // delta_c
		   i=0;
		   j=0;
	   }
   }
   
   public static void main(String args[])
   {
	   Random r = new Random();
	   if (args.length != 1)
	   {
		   System.out.println("Error. Try: java PALS instanceFile");
	   	   return;
	   }

	   //DNAFragmentAssembling p = new DNAFragmentAssembling("problems/score_4.txt");
	   DNAFragmentAssembling p = new DNAFragmentAssembling(args[0]);
	   //DNAFragmentAssembling p = new DNAFragmentAssembling("problems/score_7.txt");
	   PermutationIndividual ind = new PermutationIndividual(p.getNbOfFrags());
	   PALS pals = new PALS(null,null);
	   pals.max_steps = 500;
	   pals.prob = p;
	   ind.setRandomValues(r);
	   
	   p.evaluate(ind);
	   long inicio = (new Date()).getTime();
	   ind = (PermutationIndividual) pals.execute(ind);
	   long fin = (new Date()).getTime();
	   
	   p.evaluate(ind);
	   
	   System.out.println(((Double)ind.getFitness()).doubleValue() + " " + p.evalContigs(ind) + " "+ p.getNEvals() +" "+(fin-inicio));
	   for (int i=0; i<ind.getLength(); i++)
		   System.out.print(ind.getIntegerAllele(i) + ", ");
	   
	   System.out.println();
   }
   
}
