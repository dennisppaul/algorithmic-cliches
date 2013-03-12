

package de.hfkbremen.creatingprocessingfinding.statemachine;


import mathematik.Vector3f;

import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.Vector;


public class SketchSimpleStateMachine
        extends PApplet {

    private Vector<MEntity> mEntities = new Vector<MEntity>();


    public void setup() {
        size(1024, 768);
        rectMode(CENTER);
        smooth();

        for (int i = 0; i < 100; i++) {
            mEntities.add(new MEntity());
        }
    }


    public void draw() {
        final float mDelta = 1.0f / frameRate;

        background(255);

        for (MEntity m : mEntities) {
            m.update(mDelta);
            m.draw(g);
        }
    }


    public class MEntity {

        public int state;

        private static final int STATE_FOLLOW_MOUSE = 0;

        private static final int STATE_CHANGE_RANDOMLY = 1;

        private static final int STATE_BROWNIAN_MOTION = 2;

        public Vector3f position = new Vector3f();

        public int color;

        public float speed;

        public float state_time;

        public float scale;

        private final float IDEAL_SCALE = 20.0f;


        public MEntity() {
            state = STATE_FOLLOW_MOUSE;
            position.set(random(width), random(height));
            speed = random(1, 5) * 20;
            color = color(0, 127);
            scale = IDEAL_SCALE;
        }


        public void update(final float pDelta) {
            state_time += pDelta;
            switch (state) {
                case STATE_FOLLOW_MOUSE:
                    update_follow_mouse(pDelta);
                    break;
                case STATE_CHANGE_RANDOMLY:
                    update_change_randomly(pDelta);
                    break;
                case STATE_BROWNIAN_MOTION:
                    update_brownian_motion(pDelta);
                    break;
            }
        }


        public void draw(PGraphics g) {
            noStroke();
            fill(color);
            pushMatrix();
            translate(position.x, position.y);
            ellipse(0, 0, scale, scale);
            popMatrix();
        }


        private void update_brownian_motion(final float pDelta) {
            final float STATE_DURATION = 4.0f;
            if (state_time > STATE_DURATION) {
                state_time = 0.0f;
                state = STATE_CHANGE_RANDOMLY;
            } else {
                color = color(255, 127, 0, 127);
                final float BROWNIAN_SPEED = 15.0f;
                position.add(random(-BROWNIAN_SPEED, BROWNIAN_SPEED),
                             random(-BROWNIAN_SPEED, BROWNIAN_SPEED));
            }
        }


        private void update_change_randomly(final float pDelta) {
            final float STATE_DURATION = 1.5f;
            if (state_time > STATE_DURATION) {
                state_time = 0.0f;
                state = STATE_FOLLOW_MOUSE;
            } else {
                color = color(random(127, 255), random(127, 255), 0, 127);
                scale = random(50, 100);
            }
        }


        private void update_follow_mouse(final float pDelta) {
            Vector3f mDiff = mathematik.Util.sub(new Vector3f(mouseX, mouseY), position);
            final float MIN_DISTANCE = 10.0f;
            if (mDiff.length() < MIN_DISTANCE) {
                state_time = 0.0f;
                state = STATE_BROWNIAN_MOTION;
                scale = IDEAL_SCALE; /* make sure to always clean up volatile values */
            } else {
                scale += (IDEAL_SCALE - scale) * pDelta;
                color = color(0, 127, 255, 127);
                mDiff.normalize();
                mDiff.scale(pDelta);
                mDiff.scale(speed);
                position.add(mDiff);
            }
        }
    }


    public static void main(String[] args) {
        PApplet.main(new String[] {SketchSimpleStateMachine.class.getName()});
    }
}
