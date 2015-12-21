package de.hfkbremen.algorithmiccliches.additional.examples;

import processing.core.PApplet;

import java.util.Vector;
import mathematik.Vector3f;
import processing.core.PGraphics;

/**
 * http://en.wikipedia.org/wiki/State_machine
 */
public class SketchStateMachineWithObjects extends PApplet {

    private final Vector<Entity> mEntities = new Vector<Entity>();

    public void settings() {
        size(1024, 768, P3D);
    }

    public void setup() {
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

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchStateMachineWithObjects.class.getName()});
    }
}
