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

import jcell.*;

//import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimModelImpl;
import uchicago.src.sim.event.SliderListener;
import uchicago.src.sim.gui.ColorMap;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.gui.Value2DDisplay;
import uchicago.src.sim.space.Multi2DGrid;
//import uchicago.src.sim.space.Object2DTorus;
//import cern.jet.random.Uniform;

/**
 * A translation of the Swarm example simulation Food Bugs. The food bugs
 * simulation consists of food bugs - simple agents that absorb and expel food
 * and a foodspace which diffuses this food into the area surrounding the
 * bug. Food bugs have an ideal temperature and will move about the space
 * in attempt to achieve this idea temperature.
 *
 * @author Swarm Project and Nick Collier
 * @version $Revision: 1.4 $ $Date: 2005/11/16 19:02:15 $
 */

public class CGADisplay extends SimModelImpl {
	
	public static int NO_TEXT=1;
	public static int TEXT=2;
	
	public static final int DISPLAY_VALUE = 4;
	public static final int DISPLAY_BESTDISTANCE = 8;

  private double maxValue;
	
  //private int pauseVal = -1;
  private Schedule schedule;
  private ArrayList agentList = new ArrayList ();
  private Multi2DGrid world;
  private FoodSpace space;

  private DisplaySurface dsurf;

  private CellularGA cea;
  
  private int displayProperties = NO_TEXT + DISPLAY_BESTDISTANCE;
  
  //private int displayCharacteristic = DISPLAY_VALUE;
  
  private Individual bestIndividualReference = null;

  /**
   * Test whether a particular parameter is set
   * @param setting Sum of certain parameter values (each of 2^i)
   * @param param The parameter to be tested
   * @return
   */ 
  public static boolean isSet(int setting, int param) {
	  return ((setting & param) == param);
  }
  
  
  public CGADisplay (CellularGA _cea, double _maxValue, int _displayProperties) {
	  this.displayProperties = _displayProperties; 
	  this.cea = _cea;
	  this.maxValue = _maxValue;
	  setup();
	  begin();
	  
	  //if ((displayProperties & DISPLAY_BESTDISTANCE) == 1)
	  //	  displayCharacteristic = DISPLAY_BESTDISTANCE;
  }
  
  public CGADisplay (CellularGA _cea, double _maxValue) {
	  this.cea = _cea;
	  this.maxValue = _maxValue;
	  setup();
	  begin();

  }

  private void buildModel () {
    space = new FoodSpace (((PopGrid) cea.getPopulation()).getDimX(), ((PopGrid) cea.getPopulation()).getDimY());
    world = new Multi2DGrid(space.getSizeX (), space.getSizeY (), true);


	  for (int x=0; x<((PopGrid) cea.getPopulation()).getDimX(); x++) {
		  for (int y=0; y<((PopGrid) cea.getPopulation()).getDimY(); y++) {

		      Agent bug = new Agent (space, world, x, y, this);
		      world.putObjectAt (x, y, bug);
		      agentList.add (bug);
		  }
	  }
  }
  
  private void updateAgents() {
	  int bestInd = ((Integer) ((Statistic) cea.getParam(CellularGA.PARAM_STATISTIC)).getStat(ComplexStats.MAX_FIT_POS)).intValue();
	  bestIndividualReference = cea.getPopulation().getIndividual(bestInd);
	    for (int i = 0; i < agentList.size(); i++) {
			PopGrid pop = (PopGrid) cea.getPopulation();
			Agent ag = (Agent) agentList.get(i);
			int p = pop.toLineal(ag.getX(), ag.getY());
			Individual ind = pop.getIndividual(p);
			if (p == bestInd)
				//ag.setValue(ind.getFitness()+maxValue);
				ag.setIndividual(ind);
			else
				//ag.setValue(ind.getFitness());
				ag.setIndividual(ind);
	    }	  
  }

  private void buildDisplay () {

    Object2DDisplay agentDisplay = new Object2DDisplay (world);
    agentDisplay.setObjectList (agentList);

    // 64 shades of red
    ColorMap map = new ColorMap ();
    for (int i = 0; i < 64; i++) {
      map.mapColor (i, i / 63.0, 0, 0);
    }

    Value2DDisplay foodDisplay = new Value2DDisplay (space, map);
	
    foodDisplay.setZeroTransparent (true);
    foodDisplay.setDisplayMapping (512, 0);
    //dsurf.addDisplayable (foodDisplay, "Food Space");
	
    agentDisplay.reSize(600,600);
    if (isSet(displayProperties, CGADisplay.DISPLAY_BESTDISTANCE))
		dsurf.setLocation(650,0);
    dsurf.addDisplayable(agentDisplay, "Bugs" + displayProperties);


    //addSimEventListener (dsurf);
  }

  public void step () {
	  updateAgents();

    dsurf.updateDisplay ();
  }

  private void buildSchedule () {
    schedule.scheduleActionBeginning (1, this, "step");

    /*
     * Older style hand coded inner classes, replaced by the step method
     * and the single line of schedule code above.

    class CGARunner extends BasicAction {
      public void execute() {
        space.diffuse();
        for (int i = 0; i < agentList.size(); i++) {
          DecBee bug = (DecBee)agentList.get(i);
          bug.step();
        }
        space.update();
        dsurf.updateDisplay();



        //System.out.println("seed: " + getRngSeed());
        //System.out.println(Uniform.staticNextIntFromTo(0, 100));
        //System.out.println(normal.nextDouble());

      }
    };
    */
  }

  public void begin () {
    //setRngSeed(1972L);
    buildModel ();
    buildDisplay ();
    buildSchedule ();
	if (!dsurf.isShowing())
		dsurf.display ();
  }

  public void setup () {

    /*if (dsurf != null)
      dsurf.dispose ();
    dsurf = null;*/
    schedule = null;
    System.gc ();

	if (dsurf == null) {
		dsurf = new DisplaySurface (this, "cGA Display" + displayProperties);
		registerDisplaySurface ("Main" + displayProperties, dsurf);
	}
    schedule = new Schedule (1);


    //pauseVal = -1;

    agentList = new ArrayList ();
    world = null;
    space = null;
    setupCustomAction ();
  }

  public String[] getInitParam () {
    String[] params = {"worldXSize",
                       "worldYSize", "numBees", "Pause", "Model"};
    return params;
  }

  public Schedule getSchedule () {
    return schedule;
  }

  public String getName () {
    return "CGA";
  }

  private void setupCustomAction () {

    modelManipulator.init ();

    // this adds a button to the Custom Action tab that
    // will set the food space to 0 food when clicked
    modelManipulator.addButton ("Deep Freeze", new ActionListener () {
      public void actionPerformed (ActionEvent evt) {
        for (int i = 0; i < space.getSizeX (); i++) {
          for (int j = 0; j < space.getSizeY (); j++) {
            space.putValueAt (i, j, 0);
          }
        }
      }
    });

    // this will add a slider to the Custom Action tab that will
    // increment the output food of each bug by a the slider amount.
    // The code doesn't really work when trying to decrement the food
    // by sliding the slider to the left. But it is a good example
    // of what you can do with a slider.
    modelManipulator.addSlider ("Increment Food", 0, 100, 10, new SliderListener () {
      public void execute () {
        for (int i = 0; i < agentList.size (); i++) {
          Agent bug = (Agent) agentList.get (i);
          int outputFood = 0;//bug.getOutputFood ();
          if (isSlidingLeft && !isAdjusting) {
            outputFood -= outputFood * value * .01;
          } else if (!isSlidingLeft && !isAdjusting) {
            outputFood += outputFood * value * .01;
          }
          //bug.setOutputFood (outputFood);
        }
      }
    });
  }

public int getDisplayProperties() {
	return displayProperties;
}

public void setDisplayProperties(int displayProperties) {
	this.displayProperties = displayProperties;
}

public double getMaxValue() {
	return maxValue;
}

public Individual getBestIndividualReference() {
	return bestIndividualReference;
}

public boolean isBest(Individual ind) {
	if (ind == null)
		return false;
	else
		return (((Double)ind.getFitness()).doubleValue() == ((Double)bestIndividualReference.getFitness()).doubleValue());
}

public void resize()
{
	//dsurf.resize(x,y);
	//dsurf.repaint();

	if (dsurf != null)
		dsurf.dispose ();
    dsurf = null;
	
	setup();
	
	//begin();
	buildModel ();
	    
	//buildDisplay ();
	    
	//    updateAgents();
	
	Object2DDisplay agentDisplay = new Object2DDisplay (world);
    agentDisplay.setObjectList (agentList);

    // 64 shades of red
    ColorMap map = new ColorMap ();
    for (int i = 0; i < 64; i++) {
      map.mapColor (i, i / 63.0, 0, 0);
    }

    Value2DDisplay foodDisplay = new Value2DDisplay (space, map);
	
    foodDisplay.setZeroTransparent (true);
    foodDisplay.setDisplayMapping (512, 0);
    //dsurf.addDisplayable (foodDisplay, "Food Space");
	
    agentDisplay.reSize(600,600);
    //if (isSet(displayProperties, CGADisplay.DISPLAY_BESTDISTANCE))
		//dsurf.setLocation(0,0);
	dsurf.addDisplayable(agentDisplay, "Bugs" + displayProperties);
	
	
    buildSchedule ();
    
    if (!dsurf.isShowing())
		dsurf.display ();
    
	//dsurf.repaint ();
    dsurf.updateDisplay();
}

  /*public static void main (String[] args) {
    uchicago.src.sim.engine.SimInit init = new uchicago.src.sim.engine.SimInit ();
    CGADisplay model = new CGADisplay ();
    if (args.length > 0)
      init.loadModel (model, args[0], false);
    else
      init.loadModel (model, null, false);
  }*/
}
