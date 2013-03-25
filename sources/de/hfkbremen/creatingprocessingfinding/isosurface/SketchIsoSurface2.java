package de.hfkbremen.creatingprocessingfinding.isosurface;


import mathematik.Linef;
import mathematik.Vector2f;
import mathematik.Vector3f;

import de.hfkbremen.creatingprocessingfinding.isosurface.marchingsquares.MarchingSquares;
import de.hfkbremen.creatingprocessingfinding.isosurface.marchingsquares.MetaCircle;
import processing.core.PApplet;

import java.util.Vector;


public class SketchIsoSurface2
        extends PApplet {

    private float mIsoValue = 32.0f;

    private MetaCircle[] mMetaCircles;

    private boolean mDrawGrid = false;

    private boolean mDrawCenter = false;

    private int mCurrentCircle = 0;

    public void setup() {
        size(1024, 768, OPENGL);
        textFont(createFont("Courier", 11));
        noSmooth();

        mMetaCircles = new MetaCircle[30];
        for (int i = 0; i < mMetaCircles.length; i++) {
            mMetaCircles[i] = new MetaCircle();
            mMetaCircles[i].position().set(random(0, width), random(0, height));
            mMetaCircles[i].strength(random(5000, 50000));
        }
    }

    public void draw() {
        background(255);

        /* drag circle  */
        mMetaCircles[mCurrentCircle].position().set(mouseX, mouseY);

        /* populate field */
        final int mSquareSizeX = 8;
        final int mSquareSizeY = 8;
        final int mNumberOfSquaresX = width / mSquareSizeX;
        final int mNumberOfSquaresY = height / mSquareSizeY;
        float[][] mEnergyGrid = new float[mNumberOfSquaresX][mNumberOfSquaresY];

        for (int x = 0; x < mEnergyGrid.length; x++) {
            for (int y = 0; y < mEnergyGrid[x].length; y++) {
                mEnergyGrid[x][y] = 0;
                /* collect levels from all metaballs */
                for (int i = 0; i < mMetaCircles.length; i++) {
                    mEnergyGrid[x][y] += mMetaCircles[i].getStrengthAt(mSquareSizeX * x, mSquareSizeY * y);
                }
            }
        }

        /* draw blobs */
        final Vector<Linef<Vector2f>> mLines = MarchingSquares.getLines(mEnergyGrid, mIsoValue);
        stroke(0, 175);
        stroke(255, 127, 0);

        beginShape(LINES);
        for (Linef<Vector2f> myLine : mLines) {
            vertex(myLine.p1.x * mSquareSizeX, myLine.p1.y * mSquareSizeY);
            vertex(myLine.p2.x * mSquareSizeX, myLine.p2.y * mSquareSizeY);
        }
        endShape();

        /* draw extra info */
        if (mDrawGrid) {
            drawGrid(mSquareSizeX, mSquareSizeY);
        }
        if (mDrawCenter) {
            drawMetaCenter();
        }

        fill(0);
        noStroke();
        text("ISOVALUE : " + mIsoValue, 10, 12);
        text("SELECTED : " + mCurrentCircle, 10, 24);
        text("FPS      : " + (int) frameRate, 10, 36);
    }

    public void keyPressed() {
        switch (key) {
            case 'g':
                mDrawGrid = !mDrawGrid;
                break;
            case 'c':
                mDrawCenter = !mDrawCenter;
                break;
            case '+':
                mIsoValue++;
                break;
            case '-':
                mIsoValue--;
                break;
            case ' ':
                mCurrentCircle++;
                mCurrentCircle %= mMetaCircles.length;
                break;
        }
    }

    private void drawGrid(int RES_X, int RES_Y) {
        final int SQUARES_NX = width / RES_X;
        final int SQUARES_NY = height / RES_X;

        stroke(0, 16);
        for (int y = 0; y < SQUARES_NX + 1; y++) {
            int xp = y * RES_X;
            line(xp, 0, xp, height);
        }

        for (int x = 0; x < SQUARES_NY + 1; x++) {
            int yp = x * RES_Y;
            line(0, yp, width, yp);
        }
    }

    private void drawMetaCenter() {
        stroke(0);
        beginShape(LINES);
        for (int n = 0; n < mMetaCircles.length; n++) {
            final float mLength = 2.0f;
            final Vector3f p = mMetaCircles[n].position();
            vertex(p.x, p.y - mLength);
            vertex(p.x, p.y + mLength);
            vertex(p.x - mLength, p.y);
            vertex(p.x + mLength, p.y);
        }
        endShape();
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchIsoSurface2.class.getName()});
    }
}
