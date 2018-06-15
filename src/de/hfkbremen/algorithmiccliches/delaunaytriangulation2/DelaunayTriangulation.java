package de.hfkbremen.algorithmiccliches.delaunaytriangulation2;

import processing.core.PApplet;
import processing.core.PVector;
import teilchen.util.Intersection;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class DelaunayTriangulation {

    /*
     * 2008-02-08
     * cleaned up and rewrote a lot of stuff
     * -- d3
     *
     * ported from p bourke's triangulate.c
     * http://astronomy.swin.edu.au/~pbourke/terrain/triangulate/triangulate.c
     *
     * fjenett, 20th february 2005, offenbach-germany.
     * contact: http://www.florianjenett.de/
     *
     */
    public static boolean VERBOSE = false;

    private static boolean getCircumCircle(float xp,
                                           float yp,
                                           float x1,
                                           float y1,
                                           float x2,
                                           float y2,
                                           float x3,
                                           float y3,
                                           PVector circle) {
        /*
         * Return TRUE if a point (xp,yp) is inside the circumcircle made up
         * of the points (x1, y1), (x2, y2), (x3, y3)
         * The circumcircle centre is returned in (xc, yc) and the radius r
         * NOTE: A point on the edge is inside the circumcircle
         */

        float m1, m2, mx1, mx2, my1, my2;
        float dx, dy, rsqr, drsqr;
        float xc, yc, r;

        /* Check for coincident points */
        if (Math.abs(y1 - y2) < PApplet.EPSILON && Math.abs(y2 - y3) < PApplet.EPSILON) {
            if (VERBOSE) {
                System.out.println("### points are coincident.");
            }
            return false;
        }

        if (Math.abs(y2 - y1) < PApplet.EPSILON) {
            m2 = -(x3 - x2) / (y3 - y2);
            mx2 = (x2 + x3) / 2.0f;
            my2 = (y2 + y3) / 2.0f;
            xc = (x2 + x1) / 2.0f;
            yc = m2 * (xc - mx2) + my2;
        } else if (Math.abs(y3 - y2) < PApplet.EPSILON) {
            m1 = -(x2 - x1) / (y2 - y1);
            mx1 = (x1 + x2) / 2.0f;
            my1 = (y1 + y2) / 2.0f;
            xc = (x3 + x2) / 2.0f;
            yc = m1 * (xc - mx1) + my1;
        } else {
            m1 = -(x2 - x1) / (y2 - y1);
            m2 = -(x3 - x2) / (y3 - y2);
            mx1 = (x1 + x2) / 2.0f;
            mx2 = (x2 + x3) / 2.0f;
            my1 = (y1 + y2) / 2.0f;
            my2 = (y2 + y3) / 2.0f;
            xc = (m1 * mx1 - m2 * mx2 + my2 - my1) / (m1 - m2);
            yc = m1 * (xc - mx1) + my1;
        }

        dx = x2 - xc;
        dy = y2 - yc;
        rsqr = dx * dx + dy * dy;
        r = (float) Math.sqrt(rsqr);

        dx = xp - xc;
        dy = yp - yc;
        drsqr = dx * dx + dy * dy;

        circle.x = xc;
        circle.y = yc;
        circle.z = r;

        return (drsqr <= rsqr);
    }

    static PVector getCenter(final Vector<PVector> theVertices, final DelaunayTriangle theTriangle) {
        final PVector myCenter = new PVector();
        final PVector v0 = theVertices.get(theTriangle.p[0]);
        final PVector v1 = theVertices.get(theTriangle.p[1]);
        final PVector v2 = theVertices.get(theTriangle.p[2]);
        final PVector myNormal = new PVector(0, 0, 1);

        final PVector pA = PVector.sub(new PVector(v2.x, v2.y), new PVector(v0.x, v0.y));
        pA.mult(0.5f);
        PVector pAc = new PVector();
        pAc.cross(pA, myNormal);
        pAc.add(v0);
        pAc.add(pA);
        pA.add(v0);

        final PVector pB = PVector.sub(new PVector(v2.x, v2.y), new PVector(v1.x, v1.y));
        pB.mult(0.5f);
        PVector pBc = new PVector().set(pB);
        pBc.cross(pB, myNormal);
        pBc.add(v1);
        pBc.add(pB);
        pB.add(v1);

        Intersection.lineLineIntersect(pA, pAc, pB, pBc, myCenter, new PVector());
        return myCenter;
    }

    private static boolean almost(final PVector v0, final PVector v1, float ALMOST_THRESHOLD) {
        return Math.abs(v1.x - v0.x) < ALMOST_THRESHOLD && Math.abs(v1.y - v0.y) < ALMOST_THRESHOLD;
    }

    public static Vector<DelaunayTriangle> triangulate(Vector<PVector> theVertices) {
        sort(theVertices);
        return triangulateSortedVertices(theVertices);
    }

    public static Vector<DelaunayTriangle> triangulateSortedVertices(Vector<PVector> theVertices) {
        /*
         * Triangulation subroutine
         * Takes as input NV vertices in array pxyz
         * Returned is a list of ntri triangular faces in the array v
         * These triangles are arranged in a consistent clockwise order.
         * The triangle array 'v' should be malloced to 3 * nv
         * The vertex array pxyz must be big enough to hold 3 more points
         * The vertex array must be sorted in increasing x values say
         */

        boolean complete[];
        final int nv = theVertices.size();
        boolean inside;

        if (nv < 3) {
            return null;
        }

        final Vector<DelaunayTriangle> mDelaunayTriangles = new Vector<DelaunayTriangle>();

        /* Allocate memory for the completeness list, flag for each triangle */
        final int trimax = 4 * theVertices.size();
        complete = new boolean[trimax];
        for (int ic = 0; ic < trimax; ic++) {
            complete[ic] = false;
        }

        /*
         * Find the maximum and minimum vertex bounds.
         * This is to allow calculation of the bounding triangle
         */
        float xmin = theVertices.get(0).x;
        float ymin = theVertices.get(0).y;
        //        float zmin = theVertices.get(0).z;
        float xmax = xmin;
        float ymax = ymin;
        //        float zmax = zmin;
        for (int i = 1; i < nv; i++) {
            if (theVertices.get(i).x < xmin) {
                xmin = theVertices.get(i).x;
            }
            if (theVertices.get(i).x > xmax) {
                xmax = theVertices.get(i).x;
            }
            if (theVertices.get(i).y < ymin) {
                ymin = theVertices.get(i).y;
            }
            if (theVertices.get(i).y > ymax) {
                ymax = theVertices.get(i).y;
            }
            //            if (theVertices.get(i).z < zmin) {
            //                zmin = theVertices.get(i).z;
            //            }
            //            if (theVertices.get(i).z > zmax) {
            //                zmax = theVertices.get(i).z;
            //            }
        }

        final float dx = xmax - xmin;
        final float dy = ymax - ymin;
        //        final float dz = zmax - zmin;
        final float dmax = (dx > dy) ? dx : dy;
        float xmid = (xmax + xmin) / 2.0f;
        float ymid = (ymax + ymin) / 2.0f;
        //        float zmid = (zmax + zmin) / 2.0f;

        /*
         * Set up the supertriangle
         * This is a triangle which encompasses all the sample points.
         * The supertriangle coordinates are added to the end of the
         * vertex list. The supertriangle is the first triangle in
         * the triangle list.
         */
        theVertices.add(new PVector(xmid - 2.0f * dmax, ymid - dmax, 0.0f));
        theVertices.add(new PVector(xmid, ymid + 2.0f * dmax, 0.0f));
        theVertices.add(new PVector(xmid + 2.0f * dmax, ymid - dmax, 0.0f));

        DelaunayTriangle myTriangle = new DelaunayTriangle();
        myTriangle.p[0] = nv;
        myTriangle.p[1] = nv + 1;
        myTriangle.p[2] = nv + 2;
        mDelaunayTriangles.add(myTriangle);
        complete[0] = false;

        /*
         * Include each point one at a time into the existing mesh
         */
        for (int i = 0; i < nv; i++) {

            final float xp = theVertices.get(i).x;
            final float yp = theVertices.get(i).y;
            Vector<DelaunayTriangleEdge> edges = new Vector<DelaunayTriangleEdge>();

            /*
             * Set up the edge buffer.
             * If the point (xp,yp) lies inside the circumcircle then the
             * three edges of that triangle are added to the edge buffer
             * and that triangle is removed.
             */
            final PVector _myCircle = new PVector();
            for (int j = 0; j < mDelaunayTriangles.size(); j++) {
                final float x1, y1, x2, y2, x3, y3, xc, yc, r;

                if (complete[j]) {
                    continue;
                }

                x1 = theVertices.get(mDelaunayTriangles.get(j).p[0]).x;
                y1 = theVertices.get(mDelaunayTriangles.get(j).p[0]).y;
                x2 = theVertices.get(mDelaunayTriangles.get(j).p[1]).x;
                y2 = theVertices.get(mDelaunayTriangles.get(j).p[1]).y;
                x3 = theVertices.get(mDelaunayTriangles.get(j).p[2]).x;
                y3 = theVertices.get(mDelaunayTriangles.get(j).p[2]).y;
                inside = getCircumCircle(xp, yp, x1, y1, x2, y2, x3, y3, _myCircle);
                xc = _myCircle.x;
                yc = _myCircle.y;
                r = _myCircle.z;
                if (xc + r < xp) {
                    complete[j] = true;
                }
                if (inside) {
                    final DelaunayTriangleEdge a = new DelaunayTriangleEdge();
                    final DelaunayTriangleEdge b = new DelaunayTriangleEdge();
                    final DelaunayTriangleEdge c = new DelaunayTriangleEdge();
                    a.p[0] = mDelaunayTriangles.get(j).p[0];
                    a.p[1] = mDelaunayTriangles.get(j).p[1];
                    b.p[0] = mDelaunayTriangles.get(j).p[1];
                    b.p[1] = mDelaunayTriangles.get(j).p[2];
                    c.p[0] = mDelaunayTriangles.get(j).p[2];
                    c.p[1] = mDelaunayTriangles.get(j).p[0];
                    edges.add(a);
                    edges.add(b);
                    edges.add(c);

                    mDelaunayTriangles.get(j).p[0] = mDelaunayTriangles.lastElement().p[0];
                    mDelaunayTriangles.get(j).p[1] = mDelaunayTriangles.lastElement().p[1];
                    mDelaunayTriangles.get(j).p[2] = mDelaunayTriangles.lastElement().p[2];
                    complete[j] = complete[mDelaunayTriangles.size() - 1];
                    mDelaunayTriangles.remove(mDelaunayTriangles.size() - 1);
                    j--;
                }
            }

            /*
             * Tag multiple edges
             * Note: if all triangles are specified anticlockwise then all
             * interior edges are opposite pointing in direction.
             */
            for (int j = 0; j < edges.size(); j++) {
                for (int k = j + 1; k < edges.size(); k++) {
                    if ((edges.get(j).p[0] == edges.get(k).p[1]) && (edges.get(j).p[1] == edges.get(k).p[0])) {
                        edges.get(j).p[0] = -1;
                        edges.get(j).p[1] = -1;
                        edges.get(k).p[0] = -1;
                        edges.get(k).p[1] = -1;
                    }
                    /* Shouldn't need the following, see note above */
                    if ((edges.get(j).p[0] == edges.get(k).p[0]) && (edges.get(j).p[1] == edges.get(k).p[1])) {
                        edges.get(j).p[0] = -1;
                        edges.get(j).p[1] = -1;
                        edges.get(k).p[0] = -1;
                        edges.get(k).p[1] = -1;
                    }
                }
            }

            /*
             * Form new triangles for the current point
             * Skipping over any tagged edges.
             * All edges are arranged in clockwise order.
             */
            for (int j = 0; j < edges.size(); j++) {
                if (edges.get(j).p[0] == -1 || edges.get(j).p[1] == -1) {
                    continue;
                }
                if (mDelaunayTriangles.size() >= trimax) {
                    return null;
                }
                DelaunayTriangle myNewTriangle = new DelaunayTriangle();
                myNewTriangle.p[0] = edges.get(j).p[0];
                myNewTriangle.p[1] = edges.get(j).p[1];
                myNewTriangle.p[2] = i;
                complete[mDelaunayTriangles.size()] = false;
                mDelaunayTriangles.add(myNewTriangle);
            }
        }

        /*
         * Remove triangles with supertriangle vertices
         * These are triangles which have a vertex number greater than nv
         */
        for (int i = 0; i < mDelaunayTriangles.size(); i++) {
            if (mDelaunayTriangles.get(i).p[0] >= nv || mDelaunayTriangles.get(i).p[1] >= nv || mDelaunayTriangles.get(i).p[2] >= nv) {
                mDelaunayTriangles.set(i, mDelaunayTriangles.lastElement());
                mDelaunayTriangles.remove(mDelaunayTriangles.size() - 1);
                i--;
            }
        }

        /* remove supertriangle from point list */
        theVertices.remove(theVertices.size() - 1);
        theVertices.remove(theVertices.size() - 1);
        theVertices.remove(theVertices.size() - 1);

        return mDelaunayTriangles;
    }

    public static void sort(Vector<PVector> theConvexHullVertices) {
        Collections.sort(theConvexHullVertices, new PVectorConpare());
    }

    public static boolean addVertexSafely(Vector<PVector> pVertices, PVector pNewVertex, float mMinimumApproxDistance) {
        /* check if vertex is redundant */
        for (PVector myVertex : pVertices) {
            if (almost(myVertex, pNewVertex, mMinimumApproxDistance)) {
                if (VERBOSE) {
                    System.out.println("### new vertex is not added; it is redundant.");
                }
                return false;
            }
        }
        pVertices.add(pNewVertex);
        return true;
    }

    private static class PVectorConpare implements Comparator<PVector> {

        @Override
        public int compare(PVector p1, PVector p2) {
            return p1.x > p2.x ? 1 : (p1.x < p2.x ? -1 : 0);
        }
    }
}
