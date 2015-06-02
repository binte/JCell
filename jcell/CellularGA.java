/**
 * @author Sergio Romero Leiva
 * 
 * Modified by Bernabe Dorronsoro
 *
 * Base class for the cellular GA
 * 
 */

package jcell;

import java.awt.Point;
import HcGA.*;
import java.util.Random;

public class CellularGA extends EvolutionaryAlg
{

	public CellularGA(Random r, int genLimit)
    {
   	  super(r, genLimit);
    }

   public PopGrid getPopulation()
   {
   	return (PopGrid)population;
   }
         
   // Run generationLimit generations (or evaluationLimit evaluations) of a cEA
   public void experiment()
   {
   	PopGrid population = (PopGrid) super.population;
   	
   	// Are we executing a Multi-Target Problem?
   	boolean multiobjective = problem.numberOfObjectives() > 1;
   	
      double optimum; // Best fitness individual in the population
      Point neighPoints[]; // Individuals in the neighborhood 
      Operator oper;
      Individual iv[] = new Individual[2]; // Used for the recombination
	  Integer ind[] = new Integer[2];      // Used for avoiding selecting twice the same individual as parent
      Individual neighIndivs[] = new Individual[neighborhood.getNeighSize()];
      
      this.problem.reset(); // set evaluations to 0
      
      this.problem.evaluatePopulation(population);
      
      generationNumber = 0;
      
      if (synchronousUpdate)
      	cellUpdate = new LineSweep(population);
      if (multiobjective)
      	paretoFront.initialize(population);
      else
      {
      	statistic.calculate(population);
      
      	if (Target.maximize)
      		optimum = ((Double)statistic.getStat(SimpleStats.MAX_FIT_VALUE)).doubleValue();
      	else optimum = ((Double)statistic.getStat(SimpleStats.MIN_FIT_VALUE)).doubleValue();
      	if (Target.isBetterOrEqual(optimum, targetFitness))
         return; // stop if we find the best solution
      }
      
      listener.generation(this);
      
  	  // Only needed for hierarchical populations
  	  if (getParam(CellularGA.PARAM_HIERARCHY) != null)   
  	  {
  		lrSweep = new LineCenterLRSweep(population);
  		udSweep = new LineCenterUDSweep(population);
  	  }
  	
      while ((problem.getNEvals() < evaluationLimit) && (generationNumber < generationLimit))
      {
      	PopGrid auxPop = new PopGrid(population.getDimX(),population.getDimY());
         for (int k=0; k<population.getPopSize(); k++)
         {
        	 // BREEDING LOOP:
            selectedCell = cellUpdate.nextCell(); // Go to next cell
            neighPoints = neighborhood.getNeighbors(selectedCell);
            population.getFromPoints(neighPoints,neighIndivs);

			// First parent selection
			oper = (Operator)operators.get("selection1");
			ind[0] = (Integer)oper.execute(neighIndivs);
			iv[0] = (Individual)neighIndivs[ind[0].intValue()].clone();

			// Second parent selection
			oper = (Operator)operators.get("selection2");
			if (oper != null)
			    {
				neighPoints = neighborhood.getNeighbors(selectedCell);
				population.getFromPoints(neighPoints,neighIndivs);
				do
	               	ind[1] = (Integer)oper.execute(neighIndivs);
	            while (ind[0].intValue() == ind[1].intValue());
				
				iv[1] = (Individual)neighIndivs[ind[1].intValue()].clone();
			    }
            
            // Recombination
            oper = (Operator)operators.get("crossover");
            if (oper != null)
               if (r.nextDouble() < crossoverProb)
                  iv[0] = (Individual)oper.execute(iv);
            
            // Mutation
            oper = (Operator)operators.get("mutation");
            if (oper != null)
               if (r.nextDouble() < mutationProb)
                  iv[0] = (Individual)oper.execute(iv[0]);

            // Local search
            oper = (Operator)operators.get("local");
            if (oper != null)
               if (r.nextDouble() < localSearchProb)
                  iv[0] = (Individual)oper.execute(iv[0]);
               else problem.evaluate(iv[0]);
            else problem.evaluate(iv[0]);
            
            iv[1] = population.getIndividual(selectedCell);
            
            // Replacement
            
            // if we are in the multiobjective case, insert the new solution into the archive
            // if it dominates or it is non-dominated with respect to the current individual
            if (multiobjective && (iv[0].dominanceTest(iv[1]) >= 0)) paretoFront.Insert((Individual)iv[0].clone());
            
            oper = (Operator)operators.get("replacement");
            iv[0] = (Individual)oper.execute(iv);
            
            iv[0].setX(selectedCell.x);
			iv[0].setY(selectedCell.y);
			if (synchronousUpdate)
				auxPop.setIndividual(selectedCell,iv[0]);
			else
			{
				population.setIndividual(selectedCell,iv[0]);
	            
				// Asynchronous hierarchy
				if ((getParam(CellularGA.PARAM_HIERARCHY) != null) && ((Boolean)(getParam(CellularGA.PARAM_ASYNC_SWAP))).booleanValue())
		         		asyncSwapOperation();
			}
         }
         
         if (synchronousUpdate) population.copyPop(auxPop);
         
         // Synchronous hierarchy
         if ((getParam(CellularGA.PARAM_HIERARCHY) != null) && !((Boolean)(getParam(CellularGA.PARAM_ASYNC_SWAP))).booleanValue())
         		swapOperation();
         
         if (!multiobjective)
         	statistic.calculate(population);
 
         generationNumber++;
         listener.generation(this);
         if (!multiobjective)
         {
	         if (Target.maximize)
	         	optimum = ((Double)statistic.getStat(SimpleStats.MAX_FIT_VALUE)).doubleValue();
	         else optimum = ((Double)statistic.getStat(SimpleStats.MIN_FIT_VALUE)).doubleValue();
	         if (Target.isBetterOrEqual(optimum, targetFitness))
	            return; // Stop if we find the best solution
         }
      }
   }

   // Needed for the hyerarchical population
   protected void swapOperation()
   {
   	PopGrid population = (PopGrid) super.population;
   	
   	Individual [] iv = new Individual[2];
   	
   	if ((generationNumber%swapFreq == 0)) {
  		 if (hierarchy != null) {
  			 Point nextCell;
  			 if ((hierarchy.hierarchyType == Hierarchy.HIERARCHY_TYPE_PYRAMID) && (generationNumber%2==0))
  				 nextCell = udSweep.nextCell();
  			 else 
  				 nextCell = lrSweep.nextCell();
  			 
  	         for (int k=0; k<population.getPopSize()-1; k++) {
  				 selectedCell = nextCell;
  				 
  				 if ((selectedCell.x != population.getIndividual(selectedCell).getX()) ||  (selectedCell.y != population.getIndividual(selectedCell).getY())) {
  					 System.err.println("xy wrong");
  					 System.exit(-1);
  				 }
  				 
  				 //don't confuse the sweep
  				 //only iterate till popsize-1?
  				 if ((hierarchy.hierarchyType == Hierarchy.HIERARCHY_TYPE_PYRAMID) && (generationNumber%2==0))
  					 nextCell = udSweep.nextCell();
  				 else 
  					 nextCell = lrSweep.nextCell(); //always operate on two cells
  				 
  	             iv[0] = population.getIndividual(selectedCell);
  				 iv[1] = population.getIndividual(nextCell);
  		
  				 //Swap operation
  				 //only if the two cells are actually adjacent (horizontally or vertically)
  				 if ((((selectedCell.y == nextCell.y) && (Math.abs(selectedCell.x - nextCell.x) == 1)) ||
  						 ((selectedCell.x == nextCell.x) && (Math.abs(selectedCell.y - nextCell.y) == 1)))) {
  					 
  					 boolean performSwap;
  					 performSwap = hierarchy.decideSwap(iv, selectedCell);
  					 
  					 if ((swapIfStatic) && (iv[1].getNoMoveCount() > movesForSwapping))
  						 performSwap = true;
  					   					 
  					 if (performSwap) {				 
  			            Operator oper = (Operator)operators.get("swap");
  			            iv = (Individual[]) oper.execute(iv);
  						statistic.recordCharacteristic(new Integer(ComplexStats.NR_SWAPS));
  						if (selectedCell.y == nextCell.y)
  							if (selectedCell.x > nextCell.x) 
  								iv[0].setLastMove(Hierarchy.MOVE_RIGHT);
  							else
  								iv[0].setLastMove(Hierarchy.MOVE_LEFT);
  						else
  							if (selectedCell.y > nextCell.y) 
  								iv[0].setLastMove(Hierarchy.MOVE_DOWN);
  							else
  								iv[0].setLastMove(Hierarchy.MOVE_UP);
  						
  						iv[0].setX(selectedCell.x);
  						iv[0].setY(selectedCell.y);
  						iv[1].setX(nextCell.x);
  						iv[1].setY(nextCell.y);
  						iv[0].setLevel(((PopGrid)population).getHierarchyLevel(hierarchy.hierarchyType, selectedCell));
  						iv[1].setLevel(((PopGrid)population).getHierarchyLevel(hierarchy.hierarchyType, nextCell));
  			            population.setIndividual(selectedCell,iv[0]);			 
  			            population.setIndividual(nextCell,iv[1]);
  					 }
  					 else
  						 // write back into population! setIndividual() otherwise move is forgotten
  						 iv[1].setLastMove(Hierarchy.MOVE_NO);
  				 } 					 
  				 else
  					 iv[1].setLastMove(Hierarchy.MOVE_NO);

  		         }
  			 }
  		 }
   }

   // Needed for the hyerarchical population with asynchronous swap
   protected void asyncSwapOperation()
   {
   	Individual[] iv = new Individual[2];
   	PopGrid population = (PopGrid) super.population;
   	
    if ((generationNumber%swapFreq == 0)) {
		 if (hierarchy != null) {
			 Point nextCell = new Point(selectedCell);

				 //don't confuse the sweep
				 //only iterate till popsize-1?
				 //if (k==population.getPopSize()-1) {
				if ((hierarchy.hierarchyType == Hierarchy.HIERARCHY_TYPE_PYRAMID) && (generationNumber%2==0))
				{ // Move horizontally
					if (selectedCell.getX() < (population.getDimX()/2.0)) {
						//left half => move to the right
						 nextCell.translate(-1,0); // Best to the center
						//nextCell.translate(1,0); // Best to the corners
				   }
				   else //right half => move to the left
				   {
				   		nextCell.translate(1,0); // Best to the center
					    //nextCell.translate(-1,0); // Best to the corners
				   }
					   
				}
				else 
				{ // Move vertically
					if (selectedCell.getY() < (population.getDimY()/2.0)) {
						//upper half => move down
						 nextCell.translate(0,-1); // Best to the center
						//nextCell.translate(0,1); // Best to the corners
				   }
				   else //lower half => move up
				   {
					    nextCell.translate(0,1); // Best to the center
					   //nextCell.translate(0,-1); // Best to the corners
				   }
				}				
				
				iv[0] = population.getIndividual(selectedCell);
				iv[1] = population.getIndividual(nextCell);
				 
	            //Swap operation

				 
				 //only if the two cells are actually adjacent (horizontally or vertically)
				 if ((((selectedCell.y == nextCell.y) && (Math.abs(selectedCell.x - nextCell.x) == 1)) ||
						 ((selectedCell.x == nextCell.x) && (Math.abs(selectedCell.y - nextCell.y) == 1)))) {
					 
					 boolean performSwap;
					 performSwap = hierarchy.decideSwap(iv, selectedCell);
					 
					 if ((swapIfStatic) && (iv[1].getNoMoveCount() > movesForSwapping))
 						 performSwap = true;
					 
					 if (performSwap) {				 
			            Operator oper = (Operator)operators.get("swap");
			            iv = (Individual[]) oper.execute(iv);
						statistic.recordCharacteristic(new Integer(ComplexStats.NR_SWAPS));
						if (selectedCell.y == nextCell.y)
							if (selectedCell.x > nextCell.x) 
								iv[0].setLastMove(Hierarchy.MOVE_RIGHT);
							else
								iv[0].setLastMove(Hierarchy.MOVE_LEFT);
						else
							if (selectedCell.y > nextCell.y) 
								iv[0].setLastMove(Hierarchy.MOVE_DOWN);
							else
								iv[0].setLastMove(Hierarchy.MOVE_UP);
						
						iv[0].setX(selectedCell.x);
						iv[0].setY(selectedCell.y);
						iv[1].setX(nextCell.x);
						iv[1].setY(nextCell.y);
						iv[0].setLevel(((PopGrid)population).getHierarchyLevel(hierarchy.hierarchyType, selectedCell));
						iv[1].setLevel(((PopGrid)population).getHierarchyLevel(hierarchy.hierarchyType, nextCell));
			            population.setIndividual(selectedCell,iv[0]);			 
			            population.setIndividual(nextCell,iv[1]);
					 }
					 else
						 //write back into population! setIndividual() otherwise move is forgotten
						 iv[1].setLastMove(Hierarchy.MOVE_NO);
				 } 					 
				 else
					 iv[1].setLastMove(Hierarchy.MOVE_NO);
			 }
		 }
   }


}
