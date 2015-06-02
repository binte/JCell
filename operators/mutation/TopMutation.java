package operators.mutation;
   
import jcell.*; // uses jcell package

import java.awt.Point;
import java.util.Random;

public class TopMutation implements Operator
{
   private Random r;
   private EvolutionaryAlg ea;
   
   public TopMutation(Random r, EvolutionaryAlg ea)
   {
      this.r = r;
      this.ea = ea;
   }

   
	public Object execute(Object o, int lastWeight) {
		
		return execute(o, null, lastWeight);
	}
	
	public Object execute(Object o) {
		
		return execute(o, null);
	}
	
	
	// Parameter Individual, returns Individual
	public Object execute(Object o, Point p, int lastWeight)
	{
		TopIndividual ind = (TopIndividual) o;
		int len = ind.getLength(); //Length of individual
		double prob = ((Double)ea.getParam(CellularGA.PARAM_ALLELE_MUTATION_PROB)).doubleValue(); 
      
		for (int i=0; i<len; i++)
			if (r.nextDouble() <= prob)
				ind.mutate(r,i, lastWeight); //Mutate gene i
      
		return ind;
    }
	
	// Parameter Individual, returns Individual
	public Object execute(Object o, Point p)  
	{
		TopIndividual ind = (TopIndividual) o;
		int len = ind.getLength(); //Length of individual
		double prob = ((Double)this.ea.getParam(CellularGA.PARAM_ALLELE_MUTATION_PROB)).doubleValue();

//int nGenes2mutate = (Integer)this.ea.getParam(CellularGA.PARAM_MUTATIONS_PER_CHROMOSOME);				
//int nGenes2mutate = ( ((len/2)-1) * ((Integer)this.ea.getParam(CellularGA.PARAM_GENERATION_NUMBER)).intValue() / 500/*((Integer)this.ea.getParam(CellularGA.PARAM_GENERATION_LIMIT)).intValue()*/ ) + 1; // nœmero de genes a mutar nesta itera‹o
//System.out.println("Geraao: " + ((Integer)this.ea.getParam(CellularGA.PARAM_GENERATION_NUMBER)).intValue());
//System.out.println("nGenes2mutate: " + nGenes2mutate);
		
		for (int i=0; i<len ; i++)
			if (r.nextDouble() <= prob)
				ind.mutate(r,i); //Mutate a randomly chosen gene 

      return ind;
   }
}
