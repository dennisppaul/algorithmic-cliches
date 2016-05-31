package de.hfkbremen.algorithmiccliches.additional.examples;

import de.hfkbremen.algorithmiccliches.util.BSpline;
import de.hfkbremen.algorithmiccliches.voronoidiagram.Qvoronoi;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.Arrays;
import java.util.Vector;

/**
 * http://en.wikipedia.org/wiki/Voronoi_diagram
 */
public class SketchVoronoi2RoundRegions extends PApplet {

    private final Qvoronoi mQvoronoi = new Qvoronoi();
    private final Vector<PVector> mPoints = new Vector<PVector>();
    private PVector[][] mRegions;
    private int mCurrentRegion;

    public void settings() {
        size(1024, 768, P3D);
    }

    public void setup() {
        smooth();

        final int NUMBER_OF_POINTS_ON_CIRLCE = 20;
        for (int i = 0; i < NUMBER_OF_POINTS_ON_CIRLCE; i++) {
            final float r = (float) i / NUMBER_OF_POINTS_ON_CIRLCE * TWO_PI;
            final float x = sin(r) * 50 + width / 2;
            final float y = cos(r) * 50 + height / 2;
            addPoint(x, y);
        }
        for (int i = 0; i < NUMBER_OF_POINTS_ON_CIRLCE; i++) {
            final float r = (float) i / NUMBER_OF_POINTS_ON_CIRLCE * TWO_PI + 0.3f;
            final float x = sin(r) * 100 + width / 2;
            final float y = cos(r) * 100 + height / 2;
            addPoint(x, y);
        }
        for (int i = 0; i < NUMBER_OF_POINTS_ON_CIRLCE; i++) {
            final float r = (float) i / NUMBER_OF_POINTS_ON_CIRLCE * TWO_PI + 1.1f;
            final float x = sin(r) * 150 + width / 2;
            final float y = cos(r) * 150 + height / 2;
            addPoint(x, y);
        }

        addPoint(width / 2, height / 2);
    }

    private void addPoint(float x, float y) {
        mCurrentRegion = 0;
        mPoints.add(new PVector(x, y));
    }

    public void draw() {
        PVector[] mGridPointsArray = new PVector[mPoints.size()];
        mPoints.toArray(mGridPointsArray);
        mRegions = mQvoronoi.calculate2(mGridPointsArray);

        mPoints.lastElement().set(mouseX, mouseY);

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
        for (int i = 0; i < mPoints.size(); i++) {
            PVector v = mPoints.get(i);
            drawCross(v);
        }
    }

    private void drawCross(PVector v) {
        final float o = 2.0f;
        line(v.x - o, v.y, v.z, v.x + o, v.y, v.z);
        line(v.x, v.y - o, v.z, v.x, v.y + o, v.z);
        line(v.x, v.y, v.z - o, v.x, v.y, v.z + o);
    }

    private void drawRegion(PVector[] pVertex) {
        Vector<PVector> mRegion = new Vector<PVector>(Arrays.asList(pVertex));
        final Vector<PVector> mRoundRegion = BSpline.curve(BSpline.closeCurve(mRegion), 10);

        beginShape();
        for (PVector v : mRoundRegion) {
            vertex(v.x, v.y, v.z);
        }
        endShape(CLOSE);
    }

    public void keyPressed() {
        mCurrentRegion++;
        mCurrentRegion %= mRegions.length;
    }

    public void mousePressed() {
        addPoint(mouseX, mouseY);
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchVoronoi2RoundRegions.class.getName()});
    }
}
