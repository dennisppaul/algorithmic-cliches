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


/*
 * http://en.wikipedia.org/wiki/Delaunay_Triangulation
 */
ArrayList<PVector> mVertices;
void settings() {
    size(1024, 768, P3D);
}
void setup() {
    noFill();
    mVertices = new ArrayList();
}
void draw() {
    background(255);
    /* apply brownian motion to vertices */
    for (PVector v : mVertices) {
        v.x += random(-1.0f, 1.0f);
        v.y += random(-1.0f, 1.0f);
    }
    ArrayList<DelaunayTriangle> mDelaunayTriangles = DelaunayTriangulation.triangulate(mVertices);
    ArrayList<VoronoiDiagram.Region> mVoronoiRegions = VoronoiDiagram.getRegions(mVertices, mDelaunayTriangles);
    if (mousePressed) {
        addCrookedCircle(mouseX, mouseY, random(20, 100), random(2, 5));
    }
    /* draw shapes */
    strokeWeight(2);
    stroke(255);
    fill(255, 192, 31, 17);
    drawVoronoi(mVoronoiRegions);
    strokeWeight(1);
    stroke(255, 127, 0, 54);
    fill(255, 127, 0, 42);
    drawDelaunay(mDelaunayTriangles);
    noFill();
    stroke(0, 127, 255, 191);
    drawVertices(2);
}
void keyPressed() {
    if (key == ' ') {
        mVertices.clear();
    }
}
void addCrookedCircle(float pXOffset, float pYOffset, float pRadius, float pSteps) {
    final float mSteps = TWO_PI / pSteps;
    final float mOffset = 5;
    final float mROffset = random(TWO_PI);
    for (float r = 0; r < TWO_PI; r += mSteps) {
        final float x = sin(r + mROffset) * pRadius + pXOffset + random(-mOffset, mOffset);
        final float y = cos(r + mROffset) * pRadius + pYOffset + random(-mOffset, mOffset);
        DelaunayTriangulation.addVertexSafely(mVertices, new PVector(x, y), 1.0f);
    }
}
void drawVoronoi(ArrayList<VoronoiDiagram.Region> mVoronoiRegions) {
    /* draw voronoi diagrams */
    if (mVoronoiRegions != null) {
        for (VoronoiDiagram.Region mVoronoiRegion : mVoronoiRegions) {
            beginShape();
            for (PVector v : mVoronoiRegion.hull) {
                vertex(v.x, v.y, v.z);
            }
            endShape(CLOSE);
        }
    }
}
void drawDelaunay(ArrayList<DelaunayTriangle> mDelaunayTriangles) {
    /* draw delaunay triangles */
    if (mDelaunayTriangles != null) {
        beginShape(TRIANGLES);
        for (DelaunayTriangle mDelaunayTriangle : mDelaunayTriangles) {
            for (int j = 0; j < 3; j++) {
                vertex(mVertices.get(mDelaunayTriangle.p[j]).x, mVertices.get(mDelaunayTriangle.p[j]).y);
            }
        }
        endShape();
    }
}
void drawVertices(float pRadius) {
    /* draw vertices */
    for (PVector mVertices : mVertices) {
        cross(mVertices.x, mVertices.y, pRadius);
    }
}
void cross(float x, float y, float r) {
    line(x - r, y - r, x + r, y + r);
    line(x - r, y + r, x + r, y - r);
}
