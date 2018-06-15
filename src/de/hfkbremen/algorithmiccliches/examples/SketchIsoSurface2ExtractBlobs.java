package de.hfkbremen.algorithmiccliches.examples;

import de.hfkbremen.algorithmiccliches.isosurface.marchingsquares.MarchingSquares;
import de.hfkbremen.algorithmiccliches.isosurface.marchingsquares.MetaCircle;
import processing.core.PApplet;
import processing.core.PVector;
import teilchen.util.Linef;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

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

        /* draw blobs */
        final Vector<Linef> mLines = MarchingSquares.getLines(mEnergyGrid, mIsoValue);
        stroke(0, 175);
        stroke(255, 127, 0);

        beginShape(LINES);
        for (Linef myLine : mLines) {
            vertex(myLine.p1.x * mSquareSizeX, myLine.p1.y * mSquareSizeY);
            vertex(myLine.p2.x * mSquareSizeX, myLine.p2.y * mSquareSizeY);
        }
        endShape();

        ArrayList<ArrayList<PVector>> mBlobShapes = extractBlobs(mLines);
        noStroke();
        fill(0, 127);
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
        text("ISOVALUE   : " + mIsoValue, 10, 12);
        text("BLOB_SHAPE : " + mBlobShapes.size(), 10, 24);
        text("FPS        : " + (int) frameRate, 10, 36);
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

    public ArrayList<ArrayList<PVector>> extractBlobs(Vector<Linef> pLines) {
        Vector<Linef> mLines = new Vector<>(pLines);
        ArrayList<ArrayList<PVector>> mBlobs = new ArrayList<>();
        while (!mLines.isEmpty()) {
            ArrayList<PVector> mBlob = extractBlob(mLines);
            if (mBlob != null) {
                mBlobs.add(mBlob);
            }
        }
        return mBlobs;
    }

    public ArrayList<PVector> extractBlob(Vector<Linef> pLines) {
        ArrayList<PVector> mBlob = new ArrayList<>();
        Linef mFirstLine = pLines.remove(0);
        mBlob.add(mFirstLine.p1);
        mBlob.add(mFirstLine.p2);

        int mShapesAdded;
        do {
            mShapesAdded = 0;
            for (Iterator<Linef> iterator = pLines.iterator(); iterator.hasNext(); ) {
                Linef l = iterator.next();
                PVector mFirst = mBlob.get(0);
                PVector mLast = mBlob.get(mBlob.size() - 1);
                if (close(l.p2, mFirst) && close(l.p1, mLast)) {
                    iterator.remove();
                    return mBlob;
                } else if (close(l.p1, mFirst) && close(l.p2, mLast)) {
                    iterator.remove();
                    return mBlob;
                } else if (close(l.p1, mFirst)) {
                    mBlob.add(0, l.p1);
                    mBlob.add(0, l.p2);
                    mShapesAdded++;
                    iterator.remove();
                } else if (close(l.p2, mFirst)) {
                    mBlob.add(0, l.p2);
                    mBlob.add(0, l.p1);
                    mShapesAdded++;
                    iterator.remove();
                } else if (close(l.p1, mLast)) {
                    mBlob.add(l.p1);
                    mBlob.add(l.p2);
                    mShapesAdded++;
                    iterator.remove();
                } else if (close(l.p2, mLast)) {
                    mBlob.add(l.p2);
                    mBlob.add(l.p1);
                    mShapesAdded++;
                    iterator.remove();
                }
            }
        } while (mShapesAdded > 0);
        final boolean RETURN_CLOSED_ONLY = false;
        return RETURN_CLOSED_ONLY ? null : (mBlob.size() == 2 ? null : mBlob);
    }

    private boolean close(PVector a, PVector b) {
        final float mEpsilon = 0.001f;
        return a.dist(b) < mEpsilon;
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
        for (MetaCircle mMetaCircle : mMetaCircles) {
            final float mLength = 2.0f;
            final PVector p = mMetaCircle.position();
            vertex(p.x, p.y - mLength);
            vertex(p.x, p.y + mLength);
            vertex(p.x - mLength, p.y);
            vertex(p.x + mLength, p.y);
        }
        endShape();
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchIsoSurface2ExtractBlobs.class.getName()});
    }
}
