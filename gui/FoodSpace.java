/*$$
 * Copyright (c) 1999, Trustees of the University of Chicago
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with 
 * or without modification, are permitted provided that the following 
 * conditions are met:
 *
 *	 Redistributions of source code must retain the above copyright notice,
 *	 this list of conditions and the following disclaimer.
 *
 *	 Redistributions in binary form must reproduce the above copyright notice,
 *	 this list of conditions and the following disclaimer in the documentation
 *	 and/or other materials provided with the distribution.
 *
 * Neither the name of the University of Chicago nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE TRUSTEES OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *$$*/
package gui;

import java.awt.Point;

import uchicago.src.sim.space.Diffuse2D;
import uchicago.src.sim.space.Object2DGrid;
//import cern.jet.random.Uniform;

/**
 * The environment that the food bugs inhabit. This uses a Diffuse2D to
 * diffuse the food absorbed from the bugs.
 *
 * @author Swarm Project and Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/09/22 14:10:03 $
 */

public class FoodSpace extends Object2DGrid {

  public static final int HOT = 0;
  public static final int COLD = 1;

  private long maxFood = Diffuse2D.MAX;
  private int[] xpoints = new int[9];
  private int[] ypoints = new int[9];

  public FoodSpace(int xSize, int ySize)
  {
    super(xSize, ySize);
  }

  public void addFood(int x, int y, int food) {
    long foodHere = (long)this.getValueAt(x, y);

    if (foodHere + food <= maxFood) {
      foodHere += food;
    } else {
      foodHere = maxFood;
    }

    this.putValueAt(x, y, foodHere);
  }

  /**
   * Find the extreme hot or cold within this 9 cell neighborhood
   *
   * @return the extreme point
   */
  public Point findExtreme(int x, int y) {

    return new Point(x,y);
  }
}
