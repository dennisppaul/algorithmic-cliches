

package de.hfkbremen.creatingprocessingfinding.statemachine;


import de.hfkbremen.creatingprocessingfinding.statemachine.state.Entity;
import processing.core.PApplet;

import java.util.Vector;


public class SketchStateMachineWithObjects
        extends PApplet {

    private Vector<Entity> mEntities = new Vector<Entity>();


    public void setup() {
        size(1024, 768);
        rectMode(CENTER);
        smooth();

        for (int i = 0; i < 100; i++) {
            mEntities.add(new Entity(this));
        }
    }


    public void draw() {
        final float mDelta = 1.0f / frameRate;

        background(255);

        for (Entity m : mEntities) {
            m.update(mDelta);
            m.draw(g);
        }
    }


    public static void main(String[] args) {
        PApplet.main(new String[] {SketchStateMachineWithObjects.class.getName()});
    }
}
