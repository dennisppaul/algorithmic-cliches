package de.hfkbremen.algorithmiccliches.additional.examples;

import de.hfkbremen.algorithmiccliches.convexhull.ConvexHull;
import de.hfkbremen.algorithmiccliches.convexhull.HullVertex;
import mathematik.Vector3f;

import processing.core.PApplet;

import java.util.Vector;

/**
 * http://en.wikipedia.org/wiki/Convex_hull
 */
public class SketchConvexHull extends PApplet {

    private final static int GRID_SIZE = 4;

    private final static float GRID_SPACE = 50;

    private final Vector3f[] mGridPoints = new Vector3f[GRID_SIZE * GRID_SIZE * GRID_SIZE];

    private float mNoNoTriangle = 0;

    public void settings() {
        size(1024, 768, P3D);
    }

    public void setup() {
        frameRate(30);
        populatePointArray();
    }

    private void populatePointArray() {
        /* populate array with almost random points */
        int i = 0;
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                for (int z = 0; z < GRID_SIZE; z++) {
                    final float mRandomOffset = 1.5f;
                    mGridPoints[i] = new Vector3f(x * GRID_SPACE + random(-GRID_SPACE * mRandomOffset, GRID_SPACE * mRandomOffset),
                            y * GRID_SPACE + random(-GRID_SPACE * mRandomOffset, GRID_SPACE * mRandomOffset),
                            z * GRID_SPACE + random(-GRID_SPACE * mRandomOffset, GRID_SPACE * mRandomOffset));
                    i++;
                }
            }
        }
    }

    public void draw() {
        /* setup scene */
        background(255);
        directionalLight(126, 126, 126, 0, 0, -1);
        ambientLight(102, 102, 102);

        /* rotate object */
        translate(width / 2, height / 2);
        rotateY(TWO_PI * (float) mouseX / width);
        rotateX(TWO_PI * (float) mouseY / height);
        translate(-(GRID_SIZE - 1) * GRID_SPACE / 2.0f,
                -(GRID_SIZE - 1) * GRID_SPACE / 2.0f,
                -(GRID_SIZE - 1) * GRID_SPACE / 2.0f);

        /* draw hull */
        noStroke();
        fill(255, 127, 0);
        computeAndDrawHull(mGridPoints);

        /* draw points */
        stroke(255, 0, 0, 127);
        for (int i = 0; i < mGridPoints.length; i++) {
            Vector3f v = mGridPoints[i];
            drawCross(v);
        }
    }

    private void drawCross(Vector3f v) {
        final float o = 2.0f;
        line(v.x - o, v.y, v.z, v.x + o, v.y, v.z);
        line(v.x, v.y - o, v.z, v.x, v.y + o, v.z);
        line(v.x, v.y, v.z - o, v.x, v.y, v.z + o);
    }

    private void computeAndDrawHull(Vector3f[] pVertex) {
        final ConvexHull mHull = new ConvexHull();
        final Vector<HullVertex> mNewVertices = new Vector<HullVertex>();
        for (int i = 0; i < pVertex.length; i++) {
            mNewVertices.add(new HullVertex(pVertex[i].x,
                    pVertex[i].y,
                    pVertex[i].z));
        }
        mHull.calculateHull(mNewVertices);
        float[] myVertices = mHull.getVerticesArray();

        mNoNoTriangle += 0.01f;
        mNoNoTriangle %= myVertices.length / 9;

        beginShape(TRIANGLES);
        for (int i = 0; i < myVertices.length; i += 3) {
            if ((int) mNoNoTriangle != i / 9) {
                vertex(myVertices[i], myVertices[i + 1], myVertices[i + 2]);
            }
        }
        endShape();
    }

    public void keyPressed() {
        populatePointArray();
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchConvexHull.class.getName()});
    }
}
