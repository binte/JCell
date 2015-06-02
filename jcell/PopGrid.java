
/**
 * @author Sergio Romero Leiva
 * 
 * Modified by Bernabe Dorronsoro
 *
 * Represents a toroidal 2 dimensional structured population
 * 
 */

package jcell;

import java.awt.Point;
import HcGA.*;

public class PopGrid extends Population
{
   private int dy; //Dimensions of the population 
   private int dx;

   private boolean gridPopInitialized; // For the hierarchical model
   
   public PopGrid(int dx, int dy)
   {
	   // The rows of the 2D pop are placed one after the other in a single list of individuals
      super(dx*dy);
      this.dx = dx;
      this.dy = dy;
      // For the hierarchical model
      gridPopInitialized = false;
      // For the Hierarchical model
   }

   public int getDimX()
   {
      return dx;
   }

   public int getDimY()
   {
      return dy;
   }

   public Individual getIndividual(int x, int y)
   {
      return getIndividual(toLineal(x,y));
   }

   public Individual getIndividual(Point p)
   {
      return getIndividual(toLineal(p));
   }

   // Fills iVector with the individuals in positions in pVector
   public void getFromPoints(Point pVector[], Individual iVector[])
   {
      for (int i=0; i<pVector.length; i++)
         iVector[i] = getIndividual(pVector[i]);
   }

   public void setDimension(int dx, int dy)
   {
      this.dx = dx;
      this.dy = dy;
   }
   
   public void setIndividual(int x, int y, Individual ind)
   {
      setIndividual(toLineal(x,y),ind);
   }
   
   public void setIndividual(Point p, Individual ind)
   {
      setIndividual(toLineal(p),ind);
   }
   
   public void incrementPopSize() {

	   this.popSize++;
	   population = new Individual[popSize];
   }
   
   
   public int toLineal(int x, int y)
   {
      while (x<0)
         x += dx;
      while (y<0)
         y += dy;
         
      x %= dx;
      y %= dy;
      
      return y*dx + x;
   }
   
   public int toLineal(Point p)
   {
      return toLineal(p.x,p.y);
   }
   
   public Point toGrid(int pos)
   {
      while (pos<0)
         pos += dx*dy;
      pos %= dx*dy;
      
      return new Point(pos%dx,pos/dx);
   }
   
   // Copies the contents of pop in this population
   public void copyPop(PopGrid pop)
   {
   	super.copyPop(pop);
   	this.dx = pop.dx;
   	this.dy = pop.dy;
   }
   
   // For the Hierarchical model

   public static int getHierarchyLevel(int hierarchyType, Point p, int dimX, int dimY) {
	   //for odd and even number of rows and columns
	   int level;
	   if (hierarchyType == Hierarchy.HIERARCHY_TYPE_PYRAMID)
		   level = (int) (Math.abs(p.x-((dimX-1)/2.0)) -((dimX-1)/2.0 - (dimX-1)/2) 
			   + Math.abs(p.y-((dimY-1)/2.0)) -((dimY-1)/2.0 - (dimY-1)/2));
	   else if (hierarchyType == Hierarchy.HIERARCHY_TYPE_RING)
		   level = (int) (Math.abs(p.x-((dimX-1)/2.0)) -((dimX-1)/2.0 - (dimX-1)/2));
	   else
		   level = 0;
	   return level;
   }

   public int getHierarchyLevel(int hierarchyType, Point p) {
	   return PopGrid.getHierarchyLevel(hierarchyType, p, dx, dy);
   }
   
   public void initializeGridPop() {
	   initializeGridPop(Hierarchy.NO_HIERARCHY);
   }
   
   public void initializeGridPop(int hierarchyType) {
	   
		  gridPopInitialized = true;
		  LineSweep ls = new LineSweep(this);
		  Point selectedCell;
		  Individual iv;
	      for (int k=0; k<getPopSize(); k++)
	      {
			  //initialize x,y positions in individuals
	         selectedCell = ls.nextCell(); // next cell
	         iv = getIndividual(selectedCell);
			 iv.setX(selectedCell.x);
			 iv.setY(selectedCell.y);
			 iv.setLevel(getHierarchyLevel(hierarchyType, selectedCell));

			 setIndividual(selectedCell, iv);
	      }

   }

   // For the Hierarchical model
}
