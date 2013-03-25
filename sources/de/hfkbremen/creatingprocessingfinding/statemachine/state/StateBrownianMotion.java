package de.hfkbremen.creatingprocessingfinding.statemachine.state;


import processing.core.PApplet;


public class StateBrownianMotion
        extends State {

    private float mStateTime;

    private static final float STATE_DURATION = 4.0f;

    private static final float BROWNIAN_SPEED = 15.0f;

    public StateBrownianMotion(Entity pParent, PApplet pPApplet) {
        super(pParent, pPApplet);
    }

    public void setup() {
        e.color = p.color(255, 127, 0, 127);
    }

    public void update(final float pDelta) {
        mStateTime += pDelta;
        if (mStateTime > STATE_DURATION) {
            e.switchState(new StateChangeRandomly(e, p));
        } else {
            e.position.add(p.random(-BROWNIAN_SPEED, BROWNIAN_SPEED),
                    p.random(-BROWNIAN_SPEED, BROWNIAN_SPEED));
        }
    }

    public void done() {
    }
}