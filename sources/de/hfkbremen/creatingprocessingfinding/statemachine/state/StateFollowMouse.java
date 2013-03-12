

package de.hfkbremen.creatingprocessingfinding.statemachine.state;


import mathematik.Vector3f;

import processing.core.PApplet;


public class StateFollowMouse
        extends State {

    private static final float MIN_DISTANCE = 10.0f;


    public StateFollowMouse(Entity pParent, PApplet pPApplet) {
        super(pParent, pPApplet);
    }


    public void setup() {
        e.color = p.color(0, 127, 255, 127);
    }


    public void update(float pDelta) {
        Vector3f mDiff = mathematik.Util.sub(new Vector3f(p.mouseX, p.mouseY), e.position);
        if (mDiff.length() < MIN_DISTANCE) {
            e.switchState(new StateBrownianMotion(e, p));
        } else {
            e.scale += (Entity.IDEAL_SCALE - e.scale) * pDelta;
            mDiff.normalize();
            mDiff.scale(pDelta);
            mDiff.scale(e.speed);
            e.position.add(mDiff);
        }
    }


    public void done() {
        e.scale = Entity.IDEAL_SCALE;
    }
}
