package de.hfkbremen.algorithmiccliches.examples;

import de.hfkbremen.algorithmiccliches.util.BSpline;
import de.hfkbremen.algorithmiccliches.voronoidiagram.Qvoronoi;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class SketchVoronoi2RoundRegions extends PApplet {

    private final Qvoronoi mQvoronoi = new Qvoronoi();
    private final ArrayList<PVector> mPoints = new ArrayList<>();
    private PVector[][] mRegions;
    private int mCurrentRegion;

    public void settings() {
        size(1024, 768, P3D);
    }

    public void setup() {
        final int NUMBER_OF_POINTS_ON_CIRCLE = 20;
        for (int i = 0; i < NUMBER_OF_POINTS_ON_CIRCLE; i++) {
            final float r = (float) i / NUMBER_OF_POINTS_ON_CIRCLE * TWO_PI;
            final float x = sin(r) * 50 + width / 2.0f;
            final float y = cos(r) * 50 + height / 2.0f;
            addPoint(x, y);
        }
        for (int i = 0; i < NUMBER_OF_POINTS_ON_CIRCLE; i++) {
            final float r = (float) i / NUMBER_OF_POINTS_ON_CIRCLE * TWO_PI + 0.3f;
            final float x = sin(r) * 100 + width / 2.0f;
            final float y = cos(r) * 100 + height / 2.0f;
            addPoint(x, y);
        }
        for (int i = 0; i < NUMBER_OF_POINTS_ON_CIRCLE; i++) {
            final float r = (float) i / NUMBER_OF_POINTS_ON_CIRCLE * TWO_PI + 1.1f;
            final float x = sin(r) * 150 + width / 2.0f;
            final float y = cos(r) * 150 + height / 2.0f;
            addPoint(x, y);
        }

        addPoint(width / 2.0f, height / 2.0f);
    }

    public void draw() {
        PVector[] mGridPointsArray = new PVector[mPoints.size()];
        mPoints.toArray(mGridPointsArray);
        mRegions = mQvoronoi.calculate2(mGridPointsArray);

        mPoints.get(mPoints.size() - 1).set(mouseX, mouseY);

        if (mousePressed) {
            addPoint(mouseX, mouseY);
        }

        /* setup scene */
        background(255);

        /* draw regions */
        if (mRegions != null) {
            for (PVector[] mRegion : mRegions) {
                stroke(255, 223, 192);
                noFill();
                drawRegion(mRegion);
            }

            /* draw selected region */
            if (mRegions.length > 0) {
                noStroke();
                fill(255, 127, 0);
                drawRegion(mRegions[mCurrentRegion]);
            }
        }

        /* draw points */
        stroke(255, 0, 0, 127);
        for (PVector v : mPoints) {
            drawCross(v);
        }
    }

    public void mousePressed() {
        addPoint(mouseX, mouseY);
    }

    public void keyPressed() {
        mCurrentRegion++;
        mCurrentRegion %= mRegions.length;
    }

    private void addPoint(float x, float y) {
        mCurrentRegion = 0;
        mPoints.add(new PVector(x, y));
    }

    private void drawCross(PVector v) {
        final float o = 2.0f;
        line(v.x - o, v.y, v.z, v.x + o, v.y, v.z);
        line(v.x, v.y - o, v.z, v.x, v.y + o, v.z);
        line(v.x, v.y, v.z - o, v.x, v.y, v.z + o);
    }

    private void drawRegion(PVector[] pVertex) {
        ArrayList<PVector> mRegion = new ArrayList(java.util.Arrays.asList(pVertex));
        final ArrayList<PVector> mRoundRegion = BSpline.curve(BSpline.closeCurve(mRegion), 10);

        beginShape();
        for (PVector v : mRoundRegion) {
            vertex(v.x, v.y, v.z);
        }
        endShape(CLOSE);
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchVoronoi2RoundRegions.class.getName()});
    }
}
