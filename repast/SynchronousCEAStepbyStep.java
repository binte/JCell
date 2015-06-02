
/* ---------------------------------------------------------
   Fichero: SynchronousCEAStepbyStep.java
   Autor:   Bernabé Dorronsoro
   Description
   Like SynchronousCEA.java but it performs only one generation
   instead of the whole execution 
   ---------------------------------------------------------*/

package repast;

import java.util.Random;
import java.awt.Point;
import jcell.*;

public class SynchronousCEAStepbyStep extends CellularGA
{
	double optimum; //Mejor fitness de la poblacion
	
    public SynchronousCEAStepbyStep(Random r, int genLimit)
    {
    	super(r, genLimit);
    }

    public void experiment()
    {
	
	Point neighPoints[]; //Puntos de la vecindad
	Operator oper;
	Individual iv[] = new Individual[2]; //Necesario para cruce
	Integer ind[] = new Integer[2];      //Necesario para evitar la selección de dos individuos iguales
	Individual neighIndivs[] = new Individual[neighborhood.getNeighSize()];
	//PopGrid auxPop = new PopGrid(population.getDimX(),population.getDimY());
	LineSweep ls = new LineSweep((PopGrid)population);
    
	listener.generation(this);
	
	if (Target.isBetterOrEqual(optimum, targetFitness))
	    return; //Si mejor = deseado parar
	
//	while ((problem.getNEvals() < evaluationLimit) && (generationNumber < generationLimit))
//	    {
		PopGrid auxPop = new PopGrid(((PopGrid)population).getDimX(),((PopGrid)population).getDimY());
		for (int k=0; k<population.getPopSize(); k++)
		    {
			selectedCell = ls.nextCell(); //Siguiente celda
			neighPoints = neighborhood.getNeighbors(selectedCell);
			((PopGrid)population).getFromPoints(neighPoints,neighIndivs);
			
			//Seleccion primer padre
			oper = (Operator)operators.get("selection1");
			ind[0] = (Integer)oper.execute(neighIndivs);
			
			iv[0] = (Individual)neighIndivs[ind[0].intValue()].clone();
			
			//Seleccion segundo padre
			oper = (Operator)operators.get("selection2");
			if (oper != null)
			    {
				neighPoints = neighborhood.getNeighbors(selectedCell);
				((PopGrid)population).getFromPoints(neighPoints,neighIndivs);
				ind[1] = (Integer)oper.execute(neighIndivs);
				if ((neighType == PARAM_NEWS) && (((PopGrid)population).getDimX()==1 || ((PopGrid)population).getDimY()==1))       // Population of shape 1xA or Ax1
				    while (ind[0].intValue() == ind[1].intValue())
					{
					    // It is not allowed the same parent to be selected twice
					    // with this population size
					    ind[1] = (Integer)oper.execute(neighIndivs);
					}
				else if  ((neighType == PARAM_NEWS) && (((PopGrid)population).getDimX()==2 || ((PopGrid)population).getDimY()==2)) // Population of shape 2xA or Ax2
				    while (ind[0].intValue() == ind[1].intValue())
					{
					    // It is not allowed the same parent to be selected twice
					    // with this population size
					    ind[1] = (Integer)oper.execute(neighIndivs);
					}
				iv[1] = (Individual)neighIndivs[ind[1].intValue()].clone();
			    }
			
			//Cruce
			oper = (Operator)operators.get("crossover");
			if (oper != null)
			    if (r.nextDouble() < crossoverProb)
				iv[0] = (Individual)oper.execute(iv);
			
			//Mutacion
			oper = (Operator)operators.get("mutation");
			if (oper != null)
			    if (r.nextDouble() < mutationProb)
				iv[0] = (Individual)oper.execute(iv[0]);
			
			//Busqueda local
			oper = (Operator)operators.get("local");
			if (oper != null)
			    if (r.nextDouble() < localSearchProb)
			    	iv[0] = (Individual)oper.execute(iv[0]);
			    else problem.evaluate(iv[0]);
			else problem.evaluate(iv[0]);
			
			iv[1] = ((PopGrid)population).getIndividual(selectedCell);
			
			//Reemplazo
			oper = (Operator)operators.get("replacement");
			iv[0] = (Individual)oper.execute(iv);
			auxPop.setIndividual(selectedCell,iv[0]);
		    }
		
		population.copyPop(auxPop);
		statistic.calculate(population);
		generationNumber++;
		listener.generation(this);
		
		if (Target.isBetter(optimum, targetFitness))
		    return; //Si mejor = deseado parar
	    }
    //}
    
    public void setOptimum(double _optimum)
    {
    	optimum = _optimum;
    }
}
