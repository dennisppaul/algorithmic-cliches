package de.hfkbremen.algorithmiccliches.examples;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PMatrix3D;
import processing.core.PVector;
import teilchen.BasicBehaviorParticle;
import teilchen.Physics;
import teilchen.behavior.Alignment;
import teilchen.behavior.Cohesion;
import teilchen.behavior.Motor;
import teilchen.behavior.Separation;
import teilchen.behavior.Wander;
import teilchen.constraint.Teleporter;
import teilchen.force.ViscousDrag;

import java.util.ArrayList;

public class SketchFlocking3 extends PApplet {

    private Physics                mPhysics;
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
        mTeleporter.min().set(0, 0, height / -2.0f);
        mTeleporter.max().set(width, height, height / 2.0f);
        mPhysics.add(mTeleporter);

        ViscousDrag myViscousDrag = new ViscousDrag();
        mPhysics.add(myViscousDrag);

        /* setup entities */
        mSwarmEntities = new ArrayList<>();
        for (int i = 0; i < 80; i++) {
            SwarmEntity mSwarmEntity = new SwarmEntity();
            mSwarmEntity.position().set(random(mTeleporter.min().x, mTeleporter.max().x),
                                        random(mTeleporter.min().y, mTeleporter.max().y),
                                        random(mTeleporter.min().z, mTeleporter.max().z));
            mSwarmEntities.add(mSwarmEntity);
            mPhysics.add(mSwarmEntity);
        }
    }

    public void draw() {
        final float mDeltaTime = 1.0f / frameRate;

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

    private class SwarmEntity
            extends BasicBehaviorParticle {

        private final Separation<SwarmEntity> separation;

        private final Alignment<SwarmEntity> alignment;

        private final Cohesion<SwarmEntity> cohesion;

        private final Wander wander;

        private final Motor motor;

        public SwarmEntity() {
            maximumInnerForce(random(100.0f, 1000.0f));
            radius(10f);

            separation = new Separation<>();
            separation.proximity(20);
            separation.weight(50.0f);
            behaviors().add(separation);

            alignment = new Alignment<>();
            alignment.proximity(60);
            alignment.weight(30.0f);
            behaviors().add(alignment);

            cohesion = new Cohesion<>();
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
            {
                translate(position().x, position().y, position().z);
                pushMatrix();
                {
                    PMatrix3D p = new PMatrix3D();

                    teilchen.util.Util.pointAt(p,
                                               position(),
                                               new PVector(0, 1, 0),
                                               PVector.add(position(), velocity()));
                    applyMatrix(p);

                    pushMatrix();
                    {
                        stroke(255, 127, 0, 31);
                        noFill();
                        ellipse(0, 0, separation.proximity() * 2, separation.proximity() * 2);
                        ellipse(0, 0, alignment.proximity() * 2, alignment.proximity() * 2);
                        ellipse(0, 0, cohesion.proximity() * 2, cohesion.proximity() * 2);
                        rotateX(HALF_PI);
                        ellipse(0, 0, separation.proximity() * 2, separation.proximity() * 2);
                        ellipse(0, 0, alignment.proximity() * 2, alignment.proximity() * 2);
                        ellipse(0, 0, cohesion.proximity() * 2, cohesion.proximity() * 2);
                    }
                    popMatrix();

                    noStroke();
                    fill(0, 127, 255);
                    scale(1, 0.25f, 3);
                    box(6);
                }
                popMatrix();

                /* velocity */
                stroke(0, 127, 255, 127);
                line(0, 0, 0, velocity().x, velocity().y, velocity().z);
            }
            popMatrix();
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchFlocking3.class.getName()});
    }
}
