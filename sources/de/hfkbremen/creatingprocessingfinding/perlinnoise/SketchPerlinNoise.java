package de.hfkbremen.creatingprocessingfinding.perlinnoise;


import mathematik.Vector3f;

import processing.core.PApplet;
import processing.core.PGraphics;

import java.util.Vector;


/**
 * http://en.wikipedia.org/wiki/Perlin_noise
 */
public class SketchPerlinNoise
        extends PApplet {

    private static final int GRID_SIZE = 16;

    private int mCellsX;

    private int mCellsY;

    private Vector3f[][] mVectorField;

    private Vector<MEntity> mEntities = new Vector<MEntity>();

    private float mOffset = 0.0f;

    private boolean mDrawGrid = false;

    final float mNoiseScale = 0.024f;

    public void setup() {
        size(1024, 768, OPENGL);
        smooth();
        rectMode(CENTER);

        mCellsX = width / GRID_SIZE;
        mCellsY = height / GRID_SIZE;

        mVectorField = new Vector3f[mCellsX][mCellsY];

        for (int x = 0; x < mVectorField.length; x++) {
            for (int y = 0; y < mVectorField[x].length; y++) {
                mVectorField[x][y] = new Vector3f();
            }
        }
        populateField(mOffset);

        for (int i = 0; i < 3000; i++) {
            mEntities.add(new MEntity());
        }
    }

    private void populateField(float mOffset) {
        for (int x = 0; x < mVectorField.length; x++) {
            for (int y = 0; y < mVectorField[x].length; y++) {
                float mNoise = noise((x * mNoiseScale) + mOffset,
                        (y * mNoiseScale) + mOffset);
                mNoise *= TWO_PI * 2;
                final Vector3f v = mVectorField[x][y];
                v.x = sin(mNoise);
                v.y = cos(mNoise);
            }
        }
    }

    public void draw() {
        background(255);

        /* update flowfield */
        final float mDeltaTime = 1.0f / frameRate;
        mOffset += 0.05f * mDeltaTime;
        populateField(mOffset);


        /* draw grid */
        if (mDrawGrid) {
            noFill();
            for (int x = 0; x < mVectorField.length; x++) {
                for (int y = 0; y < mVectorField[x].length; y++) {
                    final Vector3f v = mVectorField[x][y];
                    pushMatrix();
                    translate(x * GRID_SIZE, y * GRID_SIZE);
                    stroke(0, 7);
                    translate(GRID_SIZE / 2, GRID_SIZE / 2);
                    rect(0, 0, GRID_SIZE, GRID_SIZE);
                    scale(10);
                    stroke(0, 31);
                    line(0, 0, v.x, v.y);
                    popMatrix();
                }
            }
        }

        /* update + draw entities */
        for (MEntity mEntity : mEntities) {
            mEntity.update();
        }
        for (MEntity mEntity : mEntities) {
            noStroke();
            fill(255, 127, 0, 31);
            mEntity.draw(g);
        }
    }

    class MEntity {

        Vector3f position = new Vector3f();

        Vector3f velocity = new Vector3f();

        Vector3f acceleration = new Vector3f();

        float speed = random(150, 300);

        float force = random(600, 900);

        void draw(PGraphics g) {
            pushMatrix();
            translate(position.x, position.y, position.z);
            rotate(atan2(velocity.y, velocity.x));
            rect(0, 0, 15, 5);
            popMatrix();
        }

        void update() {
            /* teleport */
            if (position.x < 0) {
                position.x = width;
            }
            if (position.x > width) {
                position.x = 0;
            }
            if (position.y < 0) {
                position.y = height;
            }
            if (position.y > height) {
                position.y = 0;
            }

            /* set acceleration from forcefield */
            final int mCellX = (int) (position.x / GRID_SIZE);
            final int mCellY = (int) (position.y / GRID_SIZE);
            if (withinBounds(mCellX, mCellY)) {
                Vector3f v = mVectorField[mCellX][mCellY];
                acceleration.set(v);
            }

            /* move entity */
            final float mDeltaTime = 1.0f / frameRate;
            acceleration.scale(force);
            Vector3f mAcc = new Vector3f(acceleration);
            mAcc.scale(mDeltaTime);
            velocity.add(mAcc);
            velocity.normalize();
            velocity.scale(speed);
            Vector3f mVel = new Vector3f(velocity);
            mVel.scale(mDeltaTime);
            position.add(mVel);
        }

        boolean withinBounds(int pCellX, int pCellY) {
            return !(pCellX < 0 || pCellY < 0 || pCellX >= mCellsX || pCellY >= mCellsY);
        }
    }

    public void keyPressed() {
        if (key == ' ') {
            mOffset += 0.1f;
            populateField(mOffset);
        }
        if (key == 'g') {
            mDrawGrid = !mDrawGrid;
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchPerlinNoise.class.getName()});
    }
}
