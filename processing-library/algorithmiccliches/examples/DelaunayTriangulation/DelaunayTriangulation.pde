import oscP5.*;
import netP5.*;
import teilchen.util.*;
import java.util.Vector;
import de.hfkbremen.algorithmiccliches.delaunaytriangulation.DelaunayTriangle;
import de.hfkbremen.algorithmiccliches.delaunaytriangulation.DelaunayTriangulation;
import de.hfkbremen.algorithmiccliches.delaunaytriangulation.VoronoiDiagram;
import de.hfkbremen.algorithmiccliches.delaunaytriangulation.VoronoiDiagram.Region;

import java.util.Vector;

/**
 * http://en.wikipedia.org/wiki/Delaunay_Triangulation
 */
Vector<PVector> mVertices;

void settings() {
    size(1024, 768, P3D);
}

void setup() {
    noFill();
    mVertices = new Vector<PVector>();
}

void draw() {
    background(255);

    /* apply brownian motion to vertices */
    for (PVector v : mVertices) {
        v.x += random(-1.0f, 1.0f);
        v.y += random(-1.0f, 1.0f);
    }

    Vector<DelaunayTriangle> mDelaunayTriangles = DelaunayTriangulation.triangulate(mVertices);
    Vector<VoronoiDiagram.Region> mVoronoiRegions = VoronoiDiagram.getRegions(mVertices, mDelaunayTriangles);

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

void drawVoronoi(Vector<Region> mVoronoiRegions) {
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

void drawDelaunay(Vector<DelaunayTriangle> mDelaunayTriangles) {
    /* draw delaunay trinangles */
    if (mDelaunayTriangles != null) {
        beginShape(TRIANGLES);
        for (int i = 0; i < mDelaunayTriangles.size(); i++) {
            for (int j = 0; j < 3; j++) {
                vertex(mVertices.get(mDelaunayTriangles.get(i).p[j]).x,
                       mVertices.get(mDelaunayTriangles.get(i).p[j]).y);
            }
        }
        endShape();
    }
}

void drawVertices(float pRadius) {
    /* draw vertices */
    for (int i = 0; i < mVertices.size(); i++) {
        cross(mVertices.get(i).x, mVertices.get(i).y, pRadius);
    }
}

void cross(float x, float y, float r) {
    line(x - r, y - r, x + r, y + r);
    line(x - r, y + r, x + r, y - r);
}
