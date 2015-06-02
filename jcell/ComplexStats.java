/**
 * @author Sergio Romero Leiva
 * 
 * Modified by Bernabe Dorronsoro
 * 
 * It computes some sttistics of the population, such as the mean fitness value, 
 * its standard deviation, and Pearson coefficient, the genotipic entropy of the
 * population, and the maximum and minimum fitness values
 * 
 */

package jcell;

public class ComplexStats implements Statistic
{
   public static final int MAX_FIT_VALUE = 0;
   public static final int MIN_FIT_VALUE = 1;
   public static final int MAX_FIT_POS   = 2;
   public static final int MIN_FIT_POS   = 3;
   public static final int AVG_FIT       = 4;
   public static final int ENTROPY_GEN   = 5;
   public static final int VARIANCE_FIT  = 6;
   public static final int STD_DEV_FIT   = 7;
   public static final int PEARSON_FIT   = 8;
   public static final int ENTROPY_PHEN  = 9;
   
   // For the hierarchical model
   public static final int NR_SWAPS = 10;
   // For the hierarchical model
   
   public static final int MAX_FIT_INDIVIDUAL = 11;
   
   public static final double LOG_2 = Math.log(2.0);
   
   public static double log2(double x)
   {
      return Math.log(x) / LOG_2;
   }
   
   private double maxFitValue;
   private double minFitValue;
   private double avgFit;
   private double entropyGen;
   private double entropyPhen;
   private double varianceFit;
   private double stdDevFit;
   private double pearsonFit;
   private int maxFitPos;
   private int minFitPos;
   
   // For the Hierarchical model
   private int swaps;
   // For the Hierarchical model
   
   private Population popAux;
   
   private boolean calculatePhEntropy = false;
   
   public ComplexStats()
   {
      popAux = new Population(1);
   }
   
   public Object getStat(int keyName)
   {
      switch (keyName)
      {
         case MAX_FIT_VALUE: return new Double(maxFitValue);
         case MIN_FIT_VALUE: return new Double(minFitValue);
         case MAX_FIT_POS:   return new Integer(maxFitPos);
         case MIN_FIT_POS:   return new Integer(minFitPos);
         case AVG_FIT:       return new Double(avgFit);
         case ENTROPY_GEN:   return new Double(entropyGen);
         case ENTROPY_PHEN:   return new Double(entropyPhen);
         case VARIANCE_FIT:  return new Double(varianceFit);
         case STD_DEV_FIT:   return new Double(stdDevFit);
         case PEARSON_FIT:   return new Double(pearsonFit);
         case MAX_FIT_INDIVIDUAL:  return popAux.getIndividual((int) maxFitPos);
         
         default:			Double val = new Double(0.0);
		 if (characteristics.containsKey(new Integer(keyName)))
			 val = (Double) characteristics.get(new Integer(keyName));
	 	
		 //reset value
		 characteristics.put(new Integer(keyName), new Double(0.0));
		return val;
      }
   }
   
   public void calculate(Population pop)
   {
      Individual ind, indAux;
      double auxFit, sumFit;
      int i, frec, popSize = pop.getPopSize();
      
      maxFitPos = 0;
      minFitPos = 0;
      maxFitValue = ((Double)pop.getIndividual(0).getFitness()).doubleValue();
      minFitValue = ((Double)pop.getIndividual(0).getFitness()).doubleValue();
      sumFit = 0.0;
      entropyGen  = 0.0;
      entropyPhen  = 0.0;
      varianceFit = 0.0;
      stdDevFit   = 0.0;
      pearsonFit  = 0.0;
      
      if (popSize != popAux.getPopSize())
         popAux.setPopSize(popSize);
         
      popAux.copyPop(pop);
      
      // Initialize values for calculating Phenotipic entropy	(Added by Bernabe Dorronsoro)
      int indLength = pop.getIndividual(0).getLength();
      double entropy[] = new double[indLength];
      
      for(i=0;i<indLength;i++)	
    	  entropy[i] = 0.0;
      
      // calculate statistics
      for (i=0; i<popSize; i++)
      {
         ind = pop.getIndividual(i);
         if (ind == null)
             continue;
         
         auxFit = ((Double)ind.getFitness()).doubleValue();
         
         // A vari‡vel sumFit vai conter a soma de todos os valores de fitness dos genes do cromossoma
         sumFit += auxFit;
         
         // O m‡ximo valor de fitness fica contido na vari‡vel maxFitValue, e o ’ndice equivalente na vari‡vel maxFitPos
         if (auxFit > maxFitValue)
         {
            maxFitValue = auxFit;
            maxFitPos = i;
         }
         
         //O menor valor de fitness fica contido na vari‡vel minFitValue, e o ’ndice equivalente na vari‡vel minFitPos
         if (auxFit < minFitValue)
         {
            minFitValue = auxFit;
            minFitPos = i;
         }


         frec = 1;
         
         for (int j=i+1; j<popSize; j++)
         {
            indAux = popAux.getIndividual(j);
            
            if (indAux != null && ind.equals(indAux))
            {
               frec++;
               popAux.setIndividual(j,null);
            }
         }
         
         entropyGen += (((double)frec)/popSize) *
                       log2(((double)popSize)/frec);
         
         // For calculating Phenotipic entropy
         if (ind.getClass() == BinaryIndividual.class)
         {
         	calculatePhEntropy = true;
         	for(int k=0;k<indLength;k++)
         		if (((BinaryIndividual)ind).getBooleanAllele(k)) entropy[k] += 1.0;
         }
         	
      }
      
      avgFit = sumFit / popSize;
      entropyGen /= log2((double)popSize);
      
      for (i=0; i<popSize; i++)
      {
         auxFit = ((Double)pop.getIndividual(i).getFitness()).doubleValue();
         varianceFit += (auxFit - avgFit)*(auxFit - avgFit);
      }
      
      varianceFit /= popSize;
      stdDevFit = Math.sqrt(varianceFit);
      pearsonFit = stdDevFit / avgFit;
      
      // Calculate phenotipic entropy
      if (calculatePhEntropy)
      {
		for(i=0;i<indLength;i++)
		{
			double p1 = entropy[i]/(double)popSize;		if(p1<0.00000001) p1=0.00000001;
			double p0 = (popSize-entropy[i])/(double)popSize;	if(p0<0.00000001) p0=0.00000001;
			entropy[i] = -p1*log2(p1) - p0*log2(p0);
			entropyPhen += entropy[i];
		}
		
		entropyPhen /= (double)(indLength);
      }
   }
   
   public String toString()
   {
      return ""+maxFitValue+" "+avgFit+" "+entropyGen+" "+
                varianceFit+" "+stdDevFit+" "+pearsonFit;
   }
   
   public void recordCharacteristic(Integer characteristic) 
   {
	  if (!characteristics.containsKey(characteristic)) {
		  //create the vector for this characteristic
		  //for recording all data
		  //characteristics.put(characteristic, new ArrayList());
		  characteristics.put(characteristic, new Double(0.0));
	  }
	  
	  Double characteristicValue = (Double) characteristics.get(characteristic);
	  
	  double val = characteristicValue.doubleValue();
	  val++;
	  characteristicValue = new Double(val);
	  characteristics.put(characteristic, characteristicValue);
   }

}
