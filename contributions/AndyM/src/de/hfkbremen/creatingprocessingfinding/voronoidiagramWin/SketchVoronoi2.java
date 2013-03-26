package de.hfkbremen.creatingprocessingfinding.voronoidiagramWin;


import mathematik.Vector3f;

import processing.core.PApplet;

import java.util.Vector;


public class SketchVoronoi2
        extends PApplet {

    private Vector3f[][] mRegions;

    private Qvoronoi mQvoronoi = new Qvoronoi();

    private Vector<Vector3f> mPoints = new Vector<Vector3f>();

    private int mCurrentRegion;

    public void setup() {
        size(1024, 768, OPENGL);
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
        mPoints.add(new Vector3f(x, y));
    }

    public void draw() {
        Vector3f[] mGridPointsArray = new Vector3f[mPoints.size()];
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
            for (int i = 0; i < mRegions.length; i++) {
                stroke(255, 223, 192);
                noFill();
                drawRegion(mRegions[i]);
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
            Vector3f v = mPoints.get(i);
            drawCross(v);
        }
    }

    private void drawCross(Vector3f v) {
        final float o = 2.0f;
        line(v.x - o, v.y, v.z, v.x + o, v.y, v.z);
        line(v.x, v.y - o, v.z, v.x, v.y + o, v.z);
        line(v.x, v.y, v.z - o, v.x, v.y, v.z + o);
    }

    private void drawRegion(Vector3f[] pVertex) {
        beginShape();
        for (int i = 0; i < pVertex.length; i++) {
            Vector3f v = pVertex[i];
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
        PApplet.main(new String[]{SketchVoronoi2.class.getName()});
    }
}
