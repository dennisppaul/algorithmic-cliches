package de.hfkbremen.algorithmiccliches.examples;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.ArrayList;

public class SketchStateMachineWithObjects extends PApplet {

    private final ArrayList<Entity> mEntities = new ArrayList<>();

    public void settings() {
        size(1024, 768, P3D);
    }

    public void setup() {
        rectMode(CENTER);
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

        static final float IDEAL_SCALE = 20.0f;
        PVector position = new PVector();
        int entity_color;
        float speed;
        float scale;
        private State mState;

        public Entity(PApplet pPApplet) {
            switchState(new StateFollowMouse(this, pPApplet));
            position.set(pPApplet.random(pPApplet.width), pPApplet.random(pPApplet.height));
            speed = pPApplet.random(1, 5) * 20;
            scale = IDEAL_SCALE;
        }

        public void update(final float pDelta) {
            mState.update(pDelta);
        }

        public void draw(PGraphics g) {
            g.noStroke();
            g.fill(entity_color);
            g.pushMatrix();
            g.translate(position.x, position.y);
            g.ellipse(0, 0, scale, scale);
            if (mState instanceof StateBrownianMotion) {
                g.stroke(entity_color);
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

        protected final Entity entity;

        protected final PApplet sketch;

        public State(Entity pParent, PApplet pPApplet) {
            entity = pParent;
            sketch = pPApplet;
        }

        public abstract void setup();

        public abstract void update(final float pDelta);

        public abstract void done();
    }

    public class StateBrownianMotion
            extends State {

        private static final float STATE_DURATION = 4.0f;
        private static final float BROWNIAN_SPEED = 15.0f;
        private float mStateTime;

        public StateBrownianMotion(Entity pParent, PApplet pPApplet) {
            super(pParent, pPApplet);
        }

        public void setup() {
            entity.entity_color = color(255, 127, 0, 127);
        }

        public void update(final float pDelta) {
            mStateTime += pDelta;
            if (mStateTime > STATE_DURATION) {
                entity.switchState(new StateChangeRandomly(entity, sketch));
            } else {
                entity.position.add(sketch.random(-BROWNIAN_SPEED, BROWNIAN_SPEED),
                                    sketch.random(-BROWNIAN_SPEED, BROWNIAN_SPEED));
            }
        }

        public void done() {
        }
    }

    public class StateChangeRandomly
            extends State {

        private static final float STATE_DURATION = 1.5f;
        private float mStateTime;

        public StateChangeRandomly(Entity pParent, PApplet pPApplet) {
            super(pParent, pPApplet);
        }

        public void setup() {
        }

        public void update(float pDelta) {
            mStateTime += pDelta;
            if (mStateTime > STATE_DURATION) {
                entity.switchState(new StateFollowMouse(entity, sketch));
            } else {
                entity.entity_color = color(sketch.random(127, 255), sketch.random(127, 255), 0, 127);
                entity.scale = sketch.random(50, 100);
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
            entity.entity_color = color(0, 127, 255, 127);
        }

        public void update(float pDelta) {
            PVector mDiff = PVector.sub(new PVector(sketch.mouseX, sketch.mouseY), entity.position);
            if (mDiff.mag() < MIN_DISTANCE) {
                entity.switchState(new StateBrownianMotion(entity, sketch));
            } else {
                entity.scale += (Entity.IDEAL_SCALE - entity.scale) * pDelta;
                mDiff.normalize();
                mDiff.mult(pDelta);
                mDiff.mult(entity.speed);
                entity.position.add(mDiff);
            }
        }

        public void done() {
            entity.scale = Entity.IDEAL_SCALE;
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchStateMachineWithObjects.class.getName()});
    }
}
