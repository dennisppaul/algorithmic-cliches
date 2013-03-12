

package de.hfkbremen.creatingprocessingfinding.statemachine.state;


import processing.core.PApplet;


public class StateChangeRandomly
        extends State {

    private float mStateTime;

    private static final float STATE_DURATION = 1.5f;


    public StateChangeRandomly(Entity pParent, PApplet pPApplet) {
        super(pParent, pPApplet);
    }


    public void setup() {
    }


    public void update(float pDelta) {
        mStateTime += pDelta;
        if (mStateTime > STATE_DURATION) {
            e.switchState(new StateFollowMouse(e, p));
        } else {
            e.color = p.color(p.random(127, 255), p.random(127, 255), 0, 127);
            e.scale = p.random(50, 100);
        }
    }


    public void done() {
    }
}