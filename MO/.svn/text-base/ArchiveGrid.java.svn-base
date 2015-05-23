/**
 * @author Bernabe Dorronsoro
 *
 * 'ArchiveGrid' is a latice in which every point is 
 * the archive of an individual of the population.
 * 
 * ArchiveGrid has the same stucture and size than the population
 * 
 */

package MO;

import jcell.*;

import java.awt.Point;
//import java.io.*;

public class ArchiveGrid //extends PopGrid
{

   private int archiveSize; // size of the grid of archives (must be equal to popSize)
   
   protected Archive[] archive;
   
	public ArchiveGrid(int archivesize, Archive arch)
	{
		archive = new Archive[archiveSize];
		for (int i=0; i<archiveSize; i++)
			archive[i] = (Archive) arch.clone();
	  
	}
  
   public void InitializeArchives(Population pop)//, Archive arch)
   {
   	//Point p = new Point(0,0);
   	Individual ind;
  	for (int i=0; i<archiveSize; i++)
   	{
   		//p = toGrid(i);
   		ind = (Individual)pop.getIndividual(i);
   		addToArchive(i, (Individual)ind.clone());
   		//setIndividual(p, ind);
   	}
   }
   
   public boolean addToArchive(int p, Individual ind)
   {
   	Archive arch = getArchive(p);
   	return arch.Insert(ind);
   }
   
   public Archive getArchive(int pos)
   {
   	return archive[pos];
   }
   
   public Archive mergeFronts(Archive SolFront)
   {
   	

   	SolFront = (Archive) archive[0].clone();
   	
   	for (int i=1; i<archiveSize; i++)
   		for (int j=0; j<archive[i].getSize(); j++)
   				if (archive[i].archive[j] != null)
   					SolFront.Insert(archive[i].getIth(j));

   	return SolFront;
   }
}
