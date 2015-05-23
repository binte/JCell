/* ------------------------------------------
File: SelXxY.java
Author: Bernabé Dorronsoro
Description
Ejecuta un cGA en el que solo se usa selección
(ni mutación ni cruce) y un sólo individuo
tiene valor máximo. Para cuando todos los 
individuos alcanzan el máximo valor o cuando 
se realizan 1000 generaciones
------------------------------------------*/

package repast;

import jcell.*;
import operators.selection.*;
import operators.replacement.*;
import jcell.neighborhoods.*;
import java.util.Random;
import problems.TakeoverProblem;

public class Takeover implements GenerationListener
{

// Default Population Size
public int x = 64;
public int y = 64;

public CellularGA cea;

final int longitCrom = 10; // Individual's chromosome length
final double maxFitness = Math.pow(2,longitCrom); // maximum fitness value
final int generationsLimit = 1000000; // Maximum number of generations

public double numPreviousBest;
public Repast repast;

public Takeover(int x, int y)
{
	super();
	
	this.x = x;
	this.y = y;
	
	numPreviousBest = 0.0;
}

public void start (Repast _repast)
{
   repast = _repast;
   Random r = new Random();
   
   //CellularGA cea = new SelectionCEA(r);
   cea = new SynchronousCEAStepbyStep(r);
	//((SynchronousCEAStepbyStep)cea).setMaxFitness(maxFitness);
   Population pop = new PopGrid(x,y);

   Individual ind = new BinaryIndividual(longitCrom);

   pop.setRandomPop(r,ind);
   
   // set all individuals' fitness to 0.0
   for (int i=0; i<pop.getPopSize(); i++)
   	pop.getIndividual(i).setFitness(new Double(0.0));
   
   // One unique individual is set to maxfitness
   pop.getIndividual(r.nextInt(pop.getPopSize())).setFitness(new Double(maxFitness));

   Problem problem = new TakeoverProblem(maxFitness);
   
   SimpleStats statistic = new SimpleStats();
   // Set parameters of CEA
   cea.setParam(CellularGA.PARAM_POPULATION, pop);
   cea.setParam(CellularGA.PARAM_STATISTIC, statistic);
   cea.setParam(CellularGA.PARAM_NEIGHBOURHOOD, new Linear5(x,y));
   cea.setParam(CellularGA.PARAM_CELL_UPDATE, null);
   //cea.setParam(CellularGA.PARAM_PROBLEM, new IntValue());
   cea.setParam(CellularGA.PARAM_PROBLEM, problem);
   cea.setParam(CellularGA.PARAM_LISTENER,this);
   //cea.setParam(CellularGA.PARAM_MUTATION_PROB, null);
   //cea.setParam(CellularGA.PARAM_CROSSOVER_PROB, null);
   cea.setParam(CellularGA.PARAM_TARGET_FITNESS, (Double) new Double(maxFitness));
   //cea.setParam("selection1", new RouletteWheelSelection(r));
   ////cea.setParam("selection2", new RouletteWheelSelection(r));
   cea.setParam("selection1", new LinearRankSelection(r));
   //cea.setParam("selection1", new TournamentSelection(r));
//   cea.setParam("selection2", new TournamentSelection(r));
//   cea.setParam("crossover", cross);
//   cea.setParam("mutation", muta);
   cea.setParam("replacement", new ReplaceIfBetter());
   
   problem.reset(); //Evaluaciones a 0
   //problem.evaluatePopulation(pop); // Evaluate initial population
   statistic.calculate(pop); // Compute statistics
   cea.setParam(CellularGA.PARAM_GENERATION_NUMBER, new Integer(0));
   ((SynchronousCEAStepbyStep)cea).setOptimum(maxFitness);
}

// executes one generation
public void step()
{
	   cea.experiment();
}

public void generation(EvolutionaryAlg ea)
{
	CellularGA cea = (CellularGA) ea;
   //int best = ((SelectionCEA) cea).getNumBest();
	SimpleStats _sstat = (SimpleStats)cea.getParam(CellularGA.PARAM_STATISTIC);
	System.out.print("Generation: " + ((Integer)cea.getParam(CellularGA.PARAM_GENERATION_NUMBER)).intValue());
	int popsize = ((Population)cea.getParam(CellularGA.PARAM_POPULATION)).getPopSize();
	double numBest = ((Double)_sstat.getStat(SimpleStats.AVG_FIT)).doubleValue() * popsize / maxFitness;
	System.out.print("; Num. Best Individuals: " + numBest);
	double difference = numBest - numPreviousBest;
	System.out.println("; Num. New Best Individuals: " + difference);
	numPreviousBest = numBest;
	if (((Double)_sstat.getStat(SimpleStats.AVG_FIT)).doubleValue() == maxFitness)
		return;
   //repast.setPopulation((Population) cea.getParam(CellularGA.PARAM_POPULATION));
}

}