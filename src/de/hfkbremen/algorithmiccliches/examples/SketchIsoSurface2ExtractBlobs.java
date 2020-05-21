package de.hfkbremen.algorithmiccliches.examples;

import de.hfkbremen.algorithmiccliches.isosurface.marchingsquares.MarchingSquares;
import de.hfkbremen.algorithmiccliches.isosurface.marchingsquares.MetaCircle;
import processing.core.PApplet;
import processing.core.PVector;
import teilchen.util.Linef;

import java.util.ArrayList;

public class SketchIsoSurface2ExtractBlobs extends PApplet {

    private float mIsoValue = 32.0f;
    private MetaCircle[] mMetaCircles;

    public void settings() {
        size(1024, 768, P3D);
        noSmooth();
    }

    public void setup() {
        textFont(createFont("Courier", 11));

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
        mMetaCircles[0].position().set(mouseX, mouseY);

        /* populate field */
        final int mSquareSizeX = 8;
        final int mSquareSizeY = 8;
        final int mNumberOfSquaresX = width / mSquareSizeX;
        final int mNumberOfSquaresY = height / mSquareSizeY;
        float[][] mEnergyGrid = new float[mNumberOfSquaresX][mNumberOfSquaresY];

        for (int x = 0; x < mEnergyGrid.length; x++) {
            for (int y = 0; y < mEnergyGrid[x].length; y++) {
                mEnergyGrid[x][y] = 0;
                for (MetaCircle mMetaCircle : mMetaCircles) {
                    mEnergyGrid[x][y] += mMetaCircle.getStrengthAt(mSquareSizeX * x, mSquareSizeY * y);
                }
            }
        }

        /* draw lines */
        final ArrayList<Linef> mLines = MarchingSquares.getLines(mEnergyGrid, mIsoValue);
        stroke(0, 127, 255);

        beginShape(LINES);
        for (Linef myLine : mLines) {
            vertex(myLine.p1.x * mSquareSizeX, myLine.p1.y * mSquareSizeY);
            vertex(myLine.p2.x * mSquareSizeX, myLine.p2.y * mSquareSizeY);
        }
        endShape();

        /* draw blobs */
        ArrayList<ArrayList<PVector>> mBlobShapes = MarchingSquares.extractBlobs(mLines);
        noStroke();
        fill(0, 127, 255, 127);
        for (ArrayList<PVector> mBlobShape : mBlobShapes) {
            beginShape();
            for (PVector p : mBlobShape) {
                vertex(p.x * mSquareSizeX, p.y * mSquareSizeY);
            }
            endShape();
        }

        /* draw extra info */
        drawGrid(mSquareSizeX, mSquareSizeY);
        drawMetaCenter();

        fill(0);
        noStroke();
        text("ISO VALUE   : " + mIsoValue, 10, 12);
        text("#BLOB_SHAPE : " + mBlobShapes.size(), 10, 24);
        text("FPS         : " + (int) frameRate, 10, 36);
    }

    public void keyPressed() {
        switch (key) {
            case '+':
                mIsoValue++;
                break;
            case '-':
                mIsoValue--;
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
        stroke(0, 127, 255, 192);
        beginShape(LINES);
        for (MetaCircle mMetaCircle : mMetaCircles) {
            final float mLength = 2.0f;
            final PVector p = mMetaCircle.position();
            vertex(p.x + mLength, p.y + mLength);
            vertex(p.x - mLength, p.y - mLength);
            vertex(p.x + mLength, p.y - mLength);
            vertex(p.x - mLength, p.y + mLength);
        }
        endShape();
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchIsoSurface2ExtractBlobs.class.getName()});
    }
}
