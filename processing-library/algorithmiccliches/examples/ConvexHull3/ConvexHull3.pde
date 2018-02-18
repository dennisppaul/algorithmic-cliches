import de.hfkbremen.algorithmiccliches.*; 
import de.hfkbremen.algorithmiccliches.agents.*; 
import de.hfkbremen.algorithmiccliches.cellularautomata.*; 
import de.hfkbremen.algorithmiccliches.convexhull.*; 
import de.hfkbremen.algorithmiccliches.delaunaytriangulation2.*; 
import de.hfkbremen.algorithmiccliches.delaunaytriangulation2.VoronoiDiagram.Region; 
import de.hfkbremen.algorithmiccliches.exporting.*; 
import de.hfkbremen.algorithmiccliches.fluiddynamics.*; 
import de.hfkbremen.algorithmiccliches.isosurface.marchingcubes.*; 
import de.hfkbremen.algorithmiccliches.isosurface.marchingsquares.*; 
import de.hfkbremen.algorithmiccliches.laserline.*; 
import de.hfkbremen.algorithmiccliches.lindenmayersystems.*; 
import de.hfkbremen.algorithmiccliches.octree.*; 
import de.hfkbremen.algorithmiccliches.util.*; 
import de.hfkbremen.algorithmiccliches.util.ArcBall; 
import de.hfkbremen.algorithmiccliches.voronoidiagram.*; 
import oscP5.*; 
import netP5.*; 
import teilchen.*; 
import teilchen.constraint.*; 
import teilchen.force.*; 
import teilchen.behavior.*; 
import teilchen.cubicle.*; 
import teilchen.util.*; 
import teilchen.util.Vector3i; 
import teilchen.util.Util; 
import teilchen.util.Packing; 
import teilchen.util.Packing.PackingEntity; 
import de.hfkbremen.mesh.*; 
import java.util.*; 
import ddf.minim.*; 
import ddf.minim.analysis.*; 
import quickhull3d.*; 
import javax.swing.*; 


final static int GRID_SIZE = 4;
final static float GRID_SPACE = 50;
final PVector[] mGridPoints = new PVector[GRID_SIZE * GRID_SIZE * GRID_SIZE];
float mNoNoTriangle = 0;
void settings() {
    size(1024, 768, P3D);
}
void setup() {
    frameRate(30);
    populatePointArray();
}
void populatePointArray() {
    /* populate array with almost random points */
    int i = 0;
    for (int x = 0; x < GRID_SIZE; x++) {
        for (int y = 0; y < GRID_SIZE; y++) {
            for (int z = 0; z < GRID_SIZE; z++) {
                final float mRandomOffset = 1.5f;
                mGridPoints[i] = new PVector(x * GRID_SPACE + random(-GRID_SPACE * mRandomOffset, GRID_SPACE * mRandomOffset),
                        y * GRID_SPACE + random(-GRID_SPACE * mRandomOffset, GRID_SPACE * mRandomOffset),
                        z * GRID_SPACE + random(-GRID_SPACE * mRandomOffset, GRID_SPACE * mRandomOffset));
                i++;
            }
        }
    }
}
void draw() {
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
    for (PVector v : mGridPoints) {
        drawCross(v);
    }
}
void drawCross(PVector v) {
    final float o = 2.0f;
    line(v.x - o, v.y, v.z, v.x + o, v.y, v.z);
    line(v.x, v.y - o, v.z, v.x, v.y + o, v.z);
    line(v.x, v.y, v.z - o, v.x, v.y, v.z + o);
}
void computeAndDrawHull(PVector[] pVertex) {
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
void keyPressed() {
    populatePointArray();
}
