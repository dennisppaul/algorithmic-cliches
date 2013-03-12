

package de.hfkbremen.creatingprocessingfinding.statemachine.state;


import mathematik.Vector3f;

import processing.core.PApplet;
import processing.core.PGraphics;


public class Entity {

    Vector3f position = new Vector3f();

    int color;

    float speed;

    float scale;

    static final float IDEAL_SCALE = 20.0f;

    private State mState;

    private final PApplet p;


    public Entity(PApplet pPApplet) {
        p = pPApplet;
        switchState(new StateFollowMouse(this, p));
        position.set(p.random(p.width), p.random(p.height));
        speed = p.random(1, 5) * 20;
        scale = IDEAL_SCALE;
    }


    public void update(final float pDelta) {
        mState.update(pDelta);
    }


    public void draw(PGraphics g) {
        g.noStroke();
        g.fill(color);
        g.pushMatrix();
        g.translate(position.x, position.y);
        g.ellipse(0, 0, scale, scale);
        if (mState instanceof StateBrownianMotion) {
            g.stroke(color);
            g.line(scale, scale, -scale, -scale);
            g.line(-scale, scale, scale, -scale);
        }
        g.popMatrix();
    }


    public final void switchState(State pState) {
        if (mState != null) {
            mState.done();
        }
        mState = pState;
        mState.setup();
    }
}
