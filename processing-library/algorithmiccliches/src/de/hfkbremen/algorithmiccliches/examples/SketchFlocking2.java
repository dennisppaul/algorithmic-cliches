package de.hfkbremen.algorithmiccliches.examples;

import processing.core.PApplet;
import processing.core.PGraphics;
import teilchen.BehaviorParticle;
import teilchen.Physics;
import teilchen.behavior.Alignment;
import teilchen.behavior.Cohesion;
import teilchen.behavior.Motor;
import teilchen.behavior.Separation;
import teilchen.behavior.Wander;
import teilchen.constraint.Teleporter;
import teilchen.force.Attractor;
import teilchen.force.ViscousDrag;

import java.util.ArrayList;

public class SketchFlocking2 extends PApplet {
    /*
     * http://en.wikipedia.org/wiki/Flocking_(behavior)
     * http://de.wikipedia.org/wiki/Craig_Reynolds
     */

    private Physics mPhysics;
    private ArrayList<SwarmEntity> mSwarmEntities;

    public void settings() {
        size(1024, 768, P3D);
    }

    public void setup() {
        frameRate(60);
        rectMode(CENTER);
        hint(DISABLE_DEPTH_TEST);
        textFont(createFont("Courier", 11));

        /* physics */
        mPhysics = new Physics();

        Teleporter mTeleporter = new Teleporter();
        mTeleporter.min().set(0, 0, 0);
        mTeleporter.max().set(width, height, 0);
        mPhysics.add(mTeleporter);

        ViscousDrag myViscousDrag = new ViscousDrag();
        mPhysics.add(myViscousDrag);

        for (int i = 0; i < 3; i++) {
            Attractor mAttractor = new Attractor();
            mAttractor.position().set(i * width / 2.0f, i * height / 2.0f);
            mAttractor.strength(-200);
            mAttractor.radius(350);
            mPhysics.add(mAttractor);
        }

        /* setup entities */
        mSwarmEntities = new ArrayList<>();
    }

    public void draw() {
        final float mDeltaTime = 1.0f / frameRate;

        if (frameRate > 50) {
            spawnEntity();
        }

        /* physics */
        mPhysics.step(mDeltaTime);

        /* entities */
        for (SwarmEntity s : mSwarmEntities) {
            s.update(mDeltaTime);
        }

        /* draw */
        background(255);
        for (SwarmEntity s : mSwarmEntities) {
            s.draw(g);
        }

        /* draw extra info */
        fill(0);
        noStroke();
        text("ENTITIES : " + mSwarmEntities.size(), 10, 12);
        text("FPS      : " + (int) frameRate, 10, 24);
    }

    private void spawnEntity() {
        SwarmEntity mSwarmEntity = new SwarmEntity();
        mSwarmEntity.position().set(random(width), random(height));
        mSwarmEntities.add(mSwarmEntity);
        mPhysics.add(mSwarmEntity);
    }

    private class SwarmEntity
            extends BehaviorParticle {

        private final Separation separation;

        private final Alignment alignment;

        private final Cohesion cohesion;

        private final Wander wander;

        private final Motor motor;

        public SwarmEntity() {
            maximumInnerForce(random(100.0f, 1000.0f));
            radius(10f);

            separation = new Separation();
            separation.proximity(30);
            separation.weight(100.0f);
            behaviors().add(separation);

            alignment = new Alignment();
            alignment.proximity(40);
            alignment.weight(60.0f);
            behaviors().add(alignment);

            cohesion = new Cohesion();
            cohesion.proximity(200);
            cohesion.weight(5.0f);
            behaviors().add(cohesion);

            wander = new Wander();
            behaviors().add(wander);

            motor = new Motor();
            motor.auto_update_direction(true);
            motor.strength(20.0f);
            behaviors().add(motor);
        }

        public void update(float theDeltaTime) {
            separation.neighbors(mSwarmEntities);
            alignment.neighbors(mSwarmEntities);
            cohesion.neighbors(mSwarmEntities);
        }

        private void draw(PGraphics g) {
            pushMatrix();
            translate(position().x, position().y, position().z);

            /* velocity */
            noFill();
            stroke(0, 127, 255, 127);
            line(0, 0, velocity().x, velocity().y);

            /* body */
            rotate(atan2(velocity().y, velocity().x));
            noStroke();
            fill(0, 127, 255, 255);
            rect(0, 0, 20, 7);

            popMatrix();
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchFlocking2.class.getName()});
    }
}
