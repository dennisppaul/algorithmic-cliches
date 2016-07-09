package de.hfkbremen.algorithmiccliches.additional.examples;

import de.hfkbremen.algorithmiccliches.delaunaytriangulation2.DelaunayTriangle;
import de.hfkbremen.algorithmiccliches.delaunaytriangulation2.DelaunayTriangulation;
import de.hfkbremen.algorithmiccliches.delaunaytriangulation2.VoronoiDiagram;
import de.hfkbremen.algorithmiccliches.delaunaytriangulation2.VoronoiDiagram.Region;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.Vector;

/**
 * http://en.wikipedia.org/wiki/Delaunay_Triangulation
 */
public class SketchDelaunayTriangulation2 extends PApplet {

    public Vector<PVector> mVertices;

    public void settings() {
        size(1024, 768, P3D);
    }

    public void setup() {
        noFill();
        mVertices = new Vector<PVector>();
    }

    public void draw() {
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

    public void keyPressed() {
        if (key == ' ') {
            mVertices.clear();
        }
    }

    private void addCrookedCircle(float pXOffset, float pYOffset, float pRadius, float pSteps) {
        final float mSteps = TWO_PI / pSteps;
        final float mOffset = 5;
        final float mROffset = random(TWO_PI);
        for (float r = 0; r < TWO_PI; r += mSteps) {
            final float x = sin(r + mROffset) * pRadius + pXOffset + random(-mOffset, mOffset);
            final float y = cos(r + mROffset) * pRadius + pYOffset + random(-mOffset, mOffset);
            DelaunayTriangulation.addVertexSafely(mVertices, new PVector(x, y), 1.0f);
        }
    }

    private void drawVoronoi(Vector<Region> mVoronoiRegions) {
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

    private void drawDelaunay(Vector<DelaunayTriangle> mDelaunayTriangles) {
        /* draw delaunay trinangles */
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

    private void drawVertices(float pRadius) {
        /* draw vertices */
        for (PVector mVertice : mVertices) {
            cross(mVertice.x, mVertice.y, pRadius);
        }
    }

    private void cross(float x, float y, float r) {
        line(x - r, y - r, x + r, y + r);
        line(x - r, y + r, x + r, y - r);
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchDelaunayTriangulation2.class.getName()});
    }
}
