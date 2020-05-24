package de.hfkbremen.algorithmiccliches.examples;

import processing.core.PApplet;
import processing.core.PVector;
import teilchen.cubicle.CubicleWorld;
import teilchen.cubicle.ICubicleEntity;
import teilchen.util.CubicleWorldView;

import java.util.ArrayList;

public class SketchCubicle extends PApplet {

    private static final int NUMBER_OF_PARTICLES_ADDED = 100;
    private static final int WORLD_NUMBER_OF_CUBICLES_X = 1024 / 64;
    private static final int WORLD_NUMBER_OF_CUBICLES_Y = 768 / 64;
    private static final int WORLD_NUMBER_OF_CUBICLES_Z = 768 / 64;
    private static final float WORLD_CUBICLE_SCALE = 32;
    private final PVector mPosition = new PVector();
    private boolean showCubicles = true;
    private float mRotationZ = 0.1f;
    private int numParticles = 1;

    private CubicleWorld mCubicleWorld;

    private CubicleWorldView mCubicleWorldView;

    public void settings() {
        size(1024, 768, P3D);
    }

    public void setup() {
        textFont(createFont("Courier", 11));
        hint(DISABLE_DEPTH_SORT);
        hint(DISABLE_DEPTH_TEST);

        /* setup world */
        mCubicleWorld = new CubicleWorld(WORLD_NUMBER_OF_CUBICLES_X,
                                         WORLD_NUMBER_OF_CUBICLES_Y,
                                         WORLD_NUMBER_OF_CUBICLES_Z);
        mCubicleWorld.cellscale().set(WORLD_CUBICLE_SCALE, WORLD_CUBICLE_SCALE, WORLD_CUBICLE_SCALE);
        mCubicleWorld.transform().translation.set(-WORLD_NUMBER_OF_CUBICLES_X * mCubicleWorld.cellscale().x / 2,
                                                  -WORLD_NUMBER_OF_CUBICLES_Y * mCubicleWorld.cellscale().y / 2,
                                                  -WORLD_NUMBER_OF_CUBICLES_Z * mCubicleWorld.cellscale().z / 2);

        mCubicleWorldView = new CubicleWorldView(mCubicleWorld);
        mCubicleWorldView.color_empty = color(0, 1);
        mCubicleWorldView.color_full = color(0, 4);

        mCubicleWorld.add(new MCubicleEntity());
    }

    public void draw() {
        /* get entities from cubicle world */
        mCubicleWorld.update();
        ArrayList<ICubicleEntity> mEntities = mCubicleWorld.getLocalEntities(mPosition, 1);

        background(255);
        pushMatrix();

        translate(width / 2.0f, height / 2.0f, 0);

        /* rotate */
        if (mousePressed) {
            mRotationZ += (mouseX * 0.01f - mRotationZ) * 0.05f;
        } else {
            mPosition.x = mouseX - width / 2.0f;
            mPosition.y = mouseY - height / 2.0f;
        }
        rotateX(THIRD_PI);
        rotateZ(mRotationZ);

        /* draw cubicle world */
        if (showCubicles) {
            strokeWeight(0.05f);
            stroke(0, 127);
            noFill();
            mCubicleWorldView.draw(g);
            strokeWeight(1.0f);
        }

        /* draw entities */
        int mNumberOfPointsSelected = 0;
        stroke(0, 127, 255, 127);
        noFill();
        if (mEntities != null) {
            mNumberOfPointsSelected = mEntities.size();
            for (ICubicleEntity mEntity : mEntities) {
                MCubicleEntity m = (MCubicleEntity) mEntity;
                stroke(m.entity_color);
                drawCross(mEntity.position(), 5.0f);
            }
        }

        /* draw crosshair */
        stroke(255, 0, 0, 63);
        noFill();
        beginShape(LINES);
        vertex(mPosition.x, -WORLD_CUBICLE_SCALE * WORLD_NUMBER_OF_CUBICLES_Y / 2, 0);
        vertex(mPosition.x, WORLD_CUBICLE_SCALE * WORLD_NUMBER_OF_CUBICLES_Y / 2, 0);
        vertex(-WORLD_CUBICLE_SCALE * WORLD_NUMBER_OF_CUBICLES_Y / 2, mPosition.y, 0);
        vertex(WORLD_CUBICLE_SCALE * WORLD_NUMBER_OF_CUBICLES_Y / 2, mPosition.y, 0);
        endShape();

        /* draw selection sphere */
        stroke(255, 0, 0, 63);
        noFill();
        translate(mPosition.x, mPosition.y, 0);
        box(WORLD_CUBICLE_SCALE);
        popMatrix();

        fill(0);
        noStroke();
        text("POINTS   : " + numParticles, 10, 12);
        text("SELECTED : " + mNumberOfPointsSelected, 10, 24);
        text("FPS      : " + frameRate, 10, 36);
    }

    public void keyPressed() {
        switch (key) {
            case ' ':
                for (int i = 0; i < NUMBER_OF_PARTICLES_ADDED; i++) {
                    MCubicleEntity mEntity = new MCubicleEntity();
                    mEntity.position().x = random(-WORLD_CUBICLE_SCALE * WORLD_NUMBER_OF_CUBICLES_X / 2,
                                                  WORLD_CUBICLE_SCALE * WORLD_NUMBER_OF_CUBICLES_X / 2);
                    mEntity.position().y = random(-WORLD_CUBICLE_SCALE * WORLD_NUMBER_OF_CUBICLES_Y / 2,
                                                  WORLD_CUBICLE_SCALE * WORLD_NUMBER_OF_CUBICLES_Y / 2);
                    mEntity.position().z = random(-WORLD_CUBICLE_SCALE * WORLD_NUMBER_OF_CUBICLES_Z / 2,
                                                  WORLD_CUBICLE_SCALE * WORLD_NUMBER_OF_CUBICLES_Z / 2);
                    mCubicleWorld.add(mEntity);
                }
                numParticles += NUMBER_OF_PARTICLES_ADDED;
                break;
            case 'o':
                showCubicles = !showCubicles;
                break;
            case 'c':
                mCubicleWorld.removeAll();
                numParticles = 0;
                break;
            default:
                break;
        }
    }

    private void drawCross(PVector v, float pRadius) {
        line(v.x - pRadius, v.y, v.z, v.x + pRadius, v.y, v.z);
        line(v.x, v.y - pRadius, v.z, v.x, v.y + pRadius, v.z);
        line(v.x, v.y, v.z - pRadius, v.x, v.y, v.z + pRadius);
    }

    class MCubicleEntity
            implements ICubicleEntity {

        private final teilchen.util.Vector3i mCubicalPosition;
        private final PVector mPosition;
        int entity_color = color(0, 127, random(0, 255), 127);

        public MCubicleEntity() {
            mCubicalPosition = new teilchen.util.Vector3i();
            mPosition = new PVector();
        }

        public teilchen.util.Vector3i cubicle() {
            return mCubicalPosition;
        }

        public PVector position() {
            return mPosition;
        }

        public boolean leaving(int theX, int theY, int theZ) {
            return theX != cubicle().x || theY != cubicle().y || theZ != cubicle().z;
        }

        public boolean isActive() {
            return true;
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchCubicle.class.getName()});
    }
}
