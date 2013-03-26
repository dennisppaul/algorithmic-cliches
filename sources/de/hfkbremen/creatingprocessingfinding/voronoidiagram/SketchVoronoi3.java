package de.hfkbremen.creatingprocessingfinding.voronoidiagram;


import mathematik.Vector3f;

import processing.core.PApplet;
import quickhull3d.Point3d;
import quickhull3d.QuickHull3D;


/**
 * http://en.wikipedia.org/wiki/Voronoi_diagram
 */
public class SketchVoronoi3
        extends PApplet {

    private Vector3f[][] mRegions;

    private Qvoronoi mQvoronoi = new Qvoronoi();

    private final static int GRID_SIZE = 4;

    private final static float GRID_SPACE = 50;

    private Vector3f[] mGridPoints = new Vector3f[GRID_SIZE * GRID_SIZE * GRID_SIZE];

    private Vector3f mAcceptableRegion = new Vector3f(GRID_SIZE * GRID_SPACE * 1.5f,
            GRID_SIZE * GRID_SPACE * 1.5f,
            GRID_SIZE * GRID_SPACE * 1.5f);

    private int mCurrentRegion;

    public void setup() {
        size(1024, 768, OPENGL);
        frameRate(30);
        populatePointArray();
    }

    private void populatePointArray() {
        mCurrentRegion = 0;
        /* populate array with almost random points */
        int i = 0;
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                for (int z = 0; z < GRID_SIZE; z++) {
                    final float mRandomOffset = 0.5f;
                    mGridPoints[i] = new Vector3f(x * GRID_SPACE + random(-GRID_SPACE * mRandomOffset, GRID_SPACE * mRandomOffset),
                            y * GRID_SPACE + random(-GRID_SPACE * mRandomOffset, GRID_SPACE * mRandomOffset),
                            z * GRID_SPACE + random(-GRID_SPACE * mRandomOffset, GRID_SPACE * mRandomOffset));
                    i++;
                }
            }
        }
    }

    public void draw() {
        mRegions = mQvoronoi.calculate3(mGridPoints);
        mRegions = mQvoronoi.cullReagions(mRegions, mAcceptableRegion);

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

        /* draw regions */
        for (int i = 0; i < mRegions.length; i++) {
            fill(255, 223, 192);
            noStroke();
            pushMatrix();
            final float JITTER = (float) mouseX / width * 4.0f;
            translate(random(-JITTER, JITTER),
                    random(-JITTER, JITTER),
                    random(-JITTER, JITTER));
            if (mCurrentRegion != i) {
                drawHull(mRegions[i]);
            }
            popMatrix();
        }

        /* draw selected region */
        noStroke();
        fill(255, 127, 0);
        drawHull(mRegions[mCurrentRegion]);

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

    private void drawHull(Vector3f[] pVertex) {
        final QuickHull3D hull = new QuickHull3D();

        final Point3d[] myNewVertices = new Point3d[pVertex.length];
        for (int i = 0; i < pVertex.length; i++) {
            myNewVertices[i] = new Point3d(pVertex[i].x,
                    pVertex[i].y,
                    pVertex[i].z);
        }

        hull.build(myNewVertices);
        hull.triangulate();
        Point3d[] vertices = hull.getVertices();  //get vertices

        beginShape(TRIANGLE_STRIP);
        int[][] faceIndices = hull.getFaces();
        for (int i = 0; i < faceIndices.length; i++) {
            for (int k = 0; k < faceIndices[i].length; k++) {
                Point3d p = vertices[faceIndices[i][k]];
                float x = (float) p.x;
                float y = (float) p.y;
                float z = (float) p.z;
                vertex(x, y, z);
            }
        }
        endShape(CLOSE);
    }

    public void mousePressed() {
        mCurrentRegion++;
        mCurrentRegion %= mRegions.length;
    }

    public void keyPressed() {
        populatePointArray();
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchVoronoi3.class.getName()});
    }
}
