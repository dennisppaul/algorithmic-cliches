

package de.hfkbremen.creatingprocessingfinding.fluiddynamics;


import mathematik.Vector3f;

import de.hfkbremen.creatingprocessingfinding.util.ArcBall;
import processing.core.PApplet;


public class SketchWaterColumns
        extends PApplet {

    private static final int X_SIZE = 1024 / 32;

    private static final int Y_SIZE = 768 / 32;

    private WaterColumnSolver2 mWater;

    private Quad[][] mQuads = new Quad[X_SIZE][Y_SIZE];


    public void setup() {
        size(1024, 768, OPENGL);
        new ArcBall(this);

        mWater = new WaterColumnSolver2(X_SIZE, Y_SIZE, 300);

        /* create view */
        final float mCellSize = 32;
        for (int x = 0; x < mQuads.length; x++) {
            for (int y = 0; y < mQuads[x].length; y++) {
                mQuads[x][y] = new Quad();
                mQuads[x][y].a().set(x * mCellSize, y * mCellSize);
                mQuads[x][y].b().set(x * mCellSize + mCellSize, y * mCellSize);
                mQuads[x][y].c().set(x * mCellSize + mCellSize, y * mCellSize + mCellSize);
                mQuads[x][y].d().set(x * mCellSize, y * mCellSize + mCellSize);
            }
        }
    }


    public void draw() {
        final float mDeltaTime = 1.0f / frameRate;

        /* step */
        final float mScaledDeltaTime = mDeltaTime * 10.0f; /* artificially speeding up the simulation */
        mWater.step(mScaledDeltaTime, 20);

        /* water interaction */
        final int mX = (int)(mouseX / (float)width * X_SIZE);
        final int mY = (int)(mouseY / (float)height * Y_SIZE);
        mWater.applyForce(mX, mY, 500.0f, mDeltaTime);
        if (keyPressed) {
            if (key == '+') {
                mWater.addVolume(mX, mY, 20.0f * mDeltaTime);
            }
            if (key == '-') {
                mWater.addVolume(mX, mY, -20.0f * mDeltaTime);
            }
        }

        /* update quads from volume map */
        for (int x = 0; x < mQuads.length; x++) {
            for (int y = 0; y < mQuads[x].length; y++) {
                mQuads[x][y].a().z = mWater.volumemap()[x][y];
                mQuads[x][y].b().z = mWater.volumemap()[x_wrapped(x + 1)][y];
                mQuads[x][y].c().z = mWater.volumemap()[x_wrapped(x + 1)][y_wrapped(y + 1)];
                mQuads[x][y].d().z = mWater.volumemap()[x][y_wrapped(y + 1)];
            }
        }

        /* draw */
        background(255);
        directionalLight(126, 126, 126, 0, 0, -1);
        ambientLight(102, 102, 102);
        translate(0, 0, -height);

        /* ground */
        fill(255, 127, 0);
        noStroke();
        rect(0, 0, width, height);

        /* water */
        fill(0, 127, 255, 191);
        stroke(31, 191, 255, 31);
        beginShape(TRIANGLES);
        scale(1, 1, 200);
        for (int x = 0; x < mQuads.length; x++) {
            for (int y = 0; y < mQuads[x].length; y++) {
                Quad q = mQuads[x][y];
                vertex(q.a);
                vertex(q.b);
                vertex(q.c);
                vertex(q.a);
                vertex(q.c);
                vertex(q.d);
            }

        }
        endShape();
    }


    private void vertex(Vector3f v) {
        vertex(v.x, v.y, v.z);
    }


    private int x_wrapped(int x) {
        return (x + X_SIZE) % X_SIZE;
    }


    private int y_wrapped(int y) {
        return (y + Y_SIZE) % Y_SIZE;
    }


    private class Quad {

        Vector3f a = new Vector3f();

        Vector3f b = new Vector3f();

        Vector3f c = new Vector3f();

        Vector3f d = new Vector3f();


        private Vector3f a() {
            return a;
        }


        private Vector3f b() {
            return b;
        }


        private Vector3f c() {
            return c;
        }


        private Vector3f d() {
            return d;
        }
    }


    public static void main(String[] args) {
        PApplet.main(new String[] {SketchWaterColumns.class.getName()});
    }
}