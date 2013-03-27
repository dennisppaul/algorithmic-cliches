package de.hfkbremen.creatingprocessingfinding.delaunaytriangulation;


import mathematik.Vector3f;

import de.hfkbremen.creatingprocessingfinding.delaunaytriangulation.DelaunayTriangulation.Triangle;
import de.hfkbremen.creatingprocessingfinding.delaunaytriangulation.VoronoiDiagram.Region;
import processing.core.PApplet;

import java.util.Vector;

import static processing.core.PConstants.TRIANGLES;


/**
 * http://en.wikipedia.org/wiki/Delaunay_Triangulation
 */
public class SketchDelaunayTriangulation
        extends PApplet {

    public Vector<Vector3f> mVertices;

    public void setup() {
        size(1024, 768, OPENGL);
        noFill();
        mVertices = new Vector<Vector3f>();
    }

    public void draw() {
        background(255);

        /* apply brownian motion to vertices */
        for (Vector3f v : mVertices) {
            v.x += random(-1.0f, 1.0f);
            v.y += random(-1.0f, 1.0f);
        }

        Vector<DelaunayTriangulation.Triangle> mDelaunayTriangles = DelaunayTriangulation.triangulate(mVertices);
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
            DelaunayTriangulation.addVertexSafely(mVertices, new Vector3f(x, y), 1.0f);
        }
    }

    private void drawVoronoi(Vector<Region> mVoronoiRegions) {
        /* draw voronoi diagrams */
        if (mVoronoiRegions != null) {
            for (VoronoiDiagram.Region mVoronoiRegion : mVoronoiRegions) {
                beginShape();
                for (Vector3f v : mVoronoiRegion.hull) {
                    vertex(v.x, v.y, v.z);
                }
                endShape(CLOSE);
            }
        }
    }

    private void drawDelaunay(Vector<Triangle> mDelaunayTriangles) {
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

    private void drawVertices(float pRadius) {
        /* draw vertices */
        for (int i = 0; i < mVertices.size(); i++) {
            cross(mVertices.get(i).x, mVertices.get(i).y, pRadius);
        }
    }

    private void cross(float x, float y, float r) {
        line(x - r, y - r,
             x + r, y + r);
        line(x - r, y + r,
             x + r, y - r);
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchDelaunayTriangulation.class.getName()});
    }
}
