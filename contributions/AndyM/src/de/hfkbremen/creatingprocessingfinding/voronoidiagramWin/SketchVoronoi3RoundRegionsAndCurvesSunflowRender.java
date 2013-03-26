package de.hfkbremen.creatingprocessingfinding.voronoidiagramWin;


import java.util.Vector;
import mathematik.BSpline;
import mathematik.Vector3f;

import processing.core.PApplet;
import processing.core.PGraphics;
import quickhull3d.Point3d;
import quickhull3d.QuickHull3D;


public class SketchVoronoi3RoundRegionsAndCurvesSunflowRender
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
    
    private PGraphics pG;

    public void setup() {
        size(1024, 600, OPENGL);
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
        background(255);
        draw(g);
    }
    
    public synchronized void draw(PGraphics pG) {
        this.pG = pG;
        mRegions = mQvoronoi.calculate3(mGridPoints);
        mRegions = mQvoronoi.cullReagions(mRegions, mAcceptableRegion);

        /* setup scene */
        
        pG.directionalLight(126, 126, 126, 0, 0, -1);
        pG.ambientLight(102, 102, 102);

        /* rotate object */
        pG.translate(width / 2, height / 2);
        pG.rotateY(TWO_PI * (float) mouseX / width);
        pG.rotateX(TWO_PI * (float) mouseY / height);
        pG.translate(-(GRID_SIZE - 1) * GRID_SPACE / 2.0f,
                -(GRID_SIZE - 1) * GRID_SPACE / 2.0f,
                -(GRID_SIZE - 1) * GRID_SPACE / 2.0f);
        pG.scale(2.5f);
        /* draw regions */
        for (int i = 0; i < mRegions.length; i++) {
            pG.fill(255, 223, 192);
            pG.noStroke();
            pG.pushMatrix();
            /*final float JITTER = (float) mouseX / width * 4.0f;
            translate(random(-JITTER, JITTER),
                    random(-JITTER, JITTER),
                    random(-JITTER, JITTER));*/
            if (mCurrentRegion != i) {
                drawHull(mRegions[i]);
            }
            pG.popMatrix();
        }

        /* draw selected region */
        pG.noStroke();
        pG.fill(255, 127, 0);
        drawHull(mRegions[mCurrentRegion]);

        /* draw points */
        pG.stroke(255, 0, 0, 127);
        for (int i = 0; i < mGridPoints.length; i++) {
            Vector3f v = mGridPoints[i];
            drawCross(v);
        }
    }

    private void drawCross(Vector3f v) {
        final float o = 2.0f;
        pG.line(v.x - o, v.y, v.z, v.x + o, v.y, v.z);
        pG.line(v.x, v.y - o, v.z, v.x, v.y + o, v.z);
        pG.line(v.x, v.y, v.z - o, v.x, v.y, v.z + o);
    }

    private void drawHull(Vector3f[] pVertex) {
        QuickHull3D hull = new QuickHull3D();

        final Point3d[] myNewVertices = new Point3d[pVertex.length];
        for (int i = 0; i < pVertex.length; i++) {
            myNewVertices[i] = new Point3d(pVertex[i].x,
                    pVertex[i].y,
                    pVertex[i].z);
        }

        hull.build(myNewVertices);
        Point3d[] hullverts = hull.getVertices();  //get vertices
        Vector<Vector3f> asdf = new Vector<Vector3f>();
        for(int i = 0; i < hullverts.length; ++i){
            asdf.add(new Vector3f(hullverts[i].x,hullverts[i].y,hullverts[i].z));
        }
        final Vector<Vector3f> interpolated = BSpline.curve(BSpline.closeCurve(asdf), 100);
        Point3d[] interpolverts = new Point3d[interpolated.size()];
        for(int i = 0; i < interpolverts.length; ++i){
            interpolverts[i] = new Point3d(interpolated.get(i).x,interpolated.get(i).y,interpolated.get(i).z);
        }
        hull = new QuickHull3D();
        hull.build(interpolverts);
        hull.triangulate();
        Point3d[] vertices = hull.getVertices();

        int[][] faceIndices = hull.getFaces();
        pG.beginShape(TRIANGLE_STRIP);
        for (int i = 0; i < faceIndices.length; i++) {
            for (int k = 0; k < faceIndices[i].length; k++) {
                Point3d p = vertices[faceIndices[i][k]];
                float x = (float) p.x;
                float y = (float) p.y;
                float z = (float) p.z;
                pG.vertex(x, y, z);
            }
        }
        pG.endShape();
        
        pG.scale(random(1.1f, 2.3f));
        pG.beginShape();
        for (int i = 0; i < hullverts.length; i++) {
                Point3d p = hullverts[i];
                float x = (float) p.x;
                float y = (float) p.y;
                float z = (float) p.z;
                pG.curveVertex(x, y, z);
        }
        pG.endShape(CLOSE);
    }

    public void mousePressed() {
        mCurrentRegion++;
        mCurrentRegion %= mRegions.length;
    }

    public void keyPressed() {
        if(key == 'p') populatePointArray();
        else if(key == 's') {
            noLoop();
            de.hfkbremen.creatingprocessingfinding.exportingMod.SunflowRenderer.render(this, "draw", this.getClass());
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchVoronoi3RoundRegionsAndCurvesSunflowRender.class.getName()});
    }
}
