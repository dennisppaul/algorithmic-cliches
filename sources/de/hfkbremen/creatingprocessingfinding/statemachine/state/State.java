package de.hfkbremen.creatingprocessingfinding.statemachine.state;


import processing.core.PApplet;


public abstract class State {

    protected final Entity e;

    protected final PApplet p;

    public State(Entity pParent, PApplet pPApplet) {
        e = pParent;
        p = pPApplet;
    }

    public abstract void setup();

    public abstract void update(final float pDelta);

    public abstract void done();
}
