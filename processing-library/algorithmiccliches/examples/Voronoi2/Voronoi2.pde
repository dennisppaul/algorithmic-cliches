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


final Qvoronoi mQvoronoi = new Qvoronoi();
final ArrayList<PVector> mPoints = new ArrayList();
PVector[][] mRegions;
int mCurrentRegion;
void settings() {
    size(1024, 768, P3D);
}
void setup() {
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
void draw() {
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
void mousePressed() {
    addPoint(mouseX, mouseY);
}
void keyPressed() {
    mCurrentRegion++;
    mCurrentRegion %= mRegions.length;
}
void addPoint(float x, float y) {
    mCurrentRegion = 0;
    mPoints.add(new PVector(x, y));
}
void drawCross(PVector v) {
    final float o = 2.0f;
    line(v.x - o, v.y, v.z, v.x + o, v.y, v.z);
    line(v.x, v.y - o, v.z, v.x, v.y + o, v.z);
    line(v.x, v.y, v.z - o, v.x, v.y, v.z + o);
}
void drawRegion(PVector[] pVertex) {
    beginShape();
    for (PVector v : pVertex) {
        vertex(v.x, v.y, v.z);
    }
    endShape(CLOSE);
}
