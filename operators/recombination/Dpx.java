
/**
 * @author Sergio Romero
 *
 * Two points crossover. It keeps the largest part of the best parent 
 * 
 */

package operators.recombination;

import java.util.Random;

import jcell.*;

public class Dpx implements Operator
{
private Random r;

public Dpx(Random r)
{
   this.r = r;
}

public Object execute(Object o)
{
   Individual iv[] = (Individual[])o;
   Individual i1, i2, auxInd, newInd;
   int i, cut1, cut2, len = iv[0].getLength();
   
   newInd = (Individual)iv[0].clone();
   i1 = iv[0];
   i2 = iv[1];
   cut1 = r.nextInt(len);  //choose randomly a position from some individual
   
   /* ciclo utilizado para prevenir que cut1 seja igual ao cut2 */
   do				
      cut2 = r.nextInt(len);
   while 
	  (cut1 == cut2);	
   
   /* ciclo utilizado para trocar os valores das variáveis cut1 e cut2 caso cut1 seja superior a cut2 */
   if (cut1 > cut2)
   {
      i = cut1;
      cut1 = cut2;
      cut2 = i;
   }
   
   /* se a diferença entre as duas posições do Individual for superior a metade do tamanho dos Individuals */
   if ((cut2 - cut1) > len/2) {
	   
	   //if (Target.isBetter(((Double)i1.getFitness()).doubleValue(), ((Double)i2.getFitness()).doubleValue()))
	   
	   // se o fitness do Individual i1 for melhor do que o fitness do Individual i2
	   if (Target.isBetter(i1, i2))
	   {
		   /* trocar i1 por i2 */
		   auxInd = i1;
		   i1 = i2;
		   i2 = auxInd;
	   }
   }
   
   /* Substituir os índices cut1+1 até cut2+1 do Individual i1 com os correspondentes índices do Individual i2 */
   for (i=cut1+1; i<cut2+1; i++)
      i1.setAllele(i,i2.getAllele(i));
   
   return i1;
}
}
