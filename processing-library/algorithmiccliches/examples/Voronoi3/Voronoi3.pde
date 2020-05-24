import de.hfkbremen.algorithmiccliches.*; 
import de.hfkbremen.algorithmiccliches.agents.*; 
import de.hfkbremen.algorithmiccliches.cellularautomata.*; 
import de.hfkbremen.algorithmiccliches.convexhull.*; 
import de.hfkbremen.algorithmiccliches.delaunaytriangulation2.*; 
import de.hfkbremen.algorithmiccliches.fluiddynamics.*; 
import de.hfkbremen.algorithmiccliches.isosurface.*; 
import de.hfkbremen.algorithmiccliches.laserline.*; 
import de.hfkbremen.algorithmiccliches.lindenmayersystems.*; 
import de.hfkbremen.algorithmiccliches.octree.*; 
import de.hfkbremen.algorithmiccliches.util.*; 
import de.hfkbremen.algorithmiccliches.voronoidiagram.*; 
import teilchen.*; 
import teilchen.behavior.*; 
import teilchen.constraint.*; 
import teilchen.cubicle.*; 
import teilchen.integration.*; 
import teilchen.util.*; 
import teilchen.force.*; 
import teilchen.force.flowfield.*; 
import teilchen.force.vectorfield.*; 
import de.hfkbremen.gewebe.*; 
import ddf.minim.*; 
import ddf.minim.analysis.*; 
import quickhull3d.*; 


final static int GRID_SIZE = 4;
final static float GRID_SPACE = 50;
final Qvoronoi mQvoronoi = new Qvoronoi();
final PVector[] mGridPoints = new PVector[GRID_SIZE * GRID_SIZE * GRID_SIZE];
final PVector mAcceptableRegion = new PVector(GRID_SIZE * GRID_SPACE * 1.5f,
                                                      GRID_SIZE * GRID_SPACE * 1.5f,
                                                      GRID_SIZE * GRID_SPACE * 1.5f);
PVector[][] mRegions;
int mCurrentRegion;
void settings() {
    size(1024, 768, P3D);
}
void setup() {
    populatePointArray();
}
void draw() {
    mRegions = mQvoronoi.calculate3(mGridPoints);
    mRegions = mQvoronoi.cullReagions(mRegions, mAcceptableRegion);
    /* setup scene */
    background(255);
    directionalLight(126, 126, 126, 0, 0, -1);
    ambientLight(102, 102, 102);
    /* rotate object */
    translate(width / 2.0f, height / 2.0f);
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
            calcAndDrawHull(mRegions[i]);
        }
        popMatrix();
    }
    /* draw selected region */
    noStroke();
    fill(255, 127, 0);
    calcAndDrawHull(mRegions[mCurrentRegion]);
    /* draw points */
    stroke(255, 0, 0, 127);
    for (PVector v : mGridPoints) {
        drawCross(v);
    }
}
void mousePressed() {
    mCurrentRegion++;
    mCurrentRegion %= mRegions.length;
}
void keyPressed() {
    populatePointArray();
}
void populatePointArray() {
    mCurrentRegion = 0;
    /* populate array with almost random points */
    int i = 0;
    for (int x = 0; x < GRID_SIZE; x++) {
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int z = 0; z < GRID_SIZE; z++) {
                final float mRandomOffset = 0.5f;
                mGridPoints[i] = new PVector(x * GRID_SPACE + random(-GRID_SPACE * mRandomOffset,
                                                                     GRID_SPACE * mRandomOffset),
                                             y * GRID_SPACE + random(-GRID_SPACE * mRandomOffset,
                                                                     GRID_SPACE * mRandomOffset),
                                             z * GRID_SPACE + random(-GRID_SPACE * mRandomOffset,
                                                                     GRID_SPACE * mRandomOffset));
                i++;
            }
        }
    }
}
void drawCross(PVector v) {
    final float o = 2.0f;
    line(v.x - o, v.y, v.z, v.x + o, v.y, v.z);
    line(v.x, v.y - o, v.z, v.x, v.y + o, v.z);
    line(v.x, v.y, v.z - o, v.x, v.y, v.z + o);
}
void calcAndDrawHull(PVector[] pVertex) {
    final QuickHull3D mHull = new QuickHull3D();
    final Point3d[] myNewVertices = new Point3d[pVertex.length];
    for (int i = 0; i < pVertex.length; i++) {
        myNewVertices[i] = new Point3d(pVertex[i].x,
                                       pVertex[i].y,
                                       pVertex[i].z);
    }
    mHull.build(myNewVertices);
    mHull.triangulate();
    Point3d[] vertices = mHull.getVertices();
    beginShape(TRIANGLES);
    int[][] mFaces = mHull.getFaces();
    for (int[] mIndices : mFaces) {
        for (int i : mIndices) {
            Point3d p = vertices[i];
            float x = (float) p.x;
            float y = (float) p.y;
            float z = (float) p.z;
            vertex(x, y, z);
        }
    }
    endShape();
}
