package de.hfkbremen.algorithmiccliches.delaunaytriangulation2;

import processing.core.PVector;
import teilchen.util.Util;

import java.util.Vector;

public class VoronoiDiagram {

    /* utility functions */
    private static float oLastAngle;

    private static Vector<DelaunayTriangle> getConnectedTriangles(final Vector<DelaunayTriangle> pTriangles,
                                                                  final int pVertexIndex) {
        final Vector<DelaunayTriangle> myTriangles = new Vector<>();
        for (DelaunayTriangle pTriangle : pTriangles) {
            if (pTriangle.p[0] == pVertexIndex || pTriangle.p[1] == pVertexIndex || pTriangle.p[2] == pVertexIndex) {
                myTriangles.add(pTriangle);
            }
        }
        return myTriangles;
    }

    private static PVector getNode(PVector theRefPoint, Vector<PVector> thePoints, Vector<PVector> theSortedPoins) {
        float myMinAngle = 10;
        PVector myNextPoint = new PVector().set(theRefPoint);
        for (PVector myNode : thePoints) {
            if (!isAlreadySorted(myNode, theSortedPoins)) {
                float myAngle = (float) Math.atan2(myNode.y - theRefPoint.y, myNode.x - theRefPoint.x);
                myAngle += Math.PI;
                if (myAngle < myMinAngle && myAngle >= oLastAngle) {
                    myMinAngle = myAngle;
                    myNextPoint.set(myNode);
                }
            }
        }
        oLastAngle = myMinAngle;
        return myNextPoint;
    }

    private static boolean isAlreadySorted(PVector theRefPoint, Vector<PVector> thePoints) {
        for (PVector myNode : thePoints) {
            if (theRefPoint.equals(myNode)) {
                return true;
            }
        }
        return false;
    }

    public static VoronoiDiagram.Region getRegion(final Vector<PVector> pVertices,
                                                  final Vector<DelaunayTriangle> pDelaunayTriangles,
                                                  final int pCenterVertexIndex) {

        final VoronoiDiagram.Region myVoronoiRegion = new VoronoiDiagram.Region();

        if (pDelaunayTriangles != null && pVertices != null) {
            /* get connected triangles */
            final Vector<DelaunayTriangle> myTriangles = getConnectedTriangles(pDelaunayTriangles, pCenterVertexIndex);
            /* get umkreis */
            for (DelaunayTriangle myTriangle : myTriangles) {
                /* get region points */
                final PVector myCenter = DelaunayTriangulation.getCenter(pVertices, myTriangle);
                myVoronoiRegion.hull.add(myCenter);
            }
            /* sort region points */
            myVoronoiRegion.hull = sort(myVoronoiRegion.hull);
            myVoronoiRegion.center.set(pVertices.get(pCenterVertexIndex));
        }

        return myVoronoiRegion;
    }

    public static Vector<VoronoiDiagram.Region> getRegions(final Vector<PVector> pVertices,
                                                           final Vector<DelaunayTriangle> pDelaunayTriangles) {
        final Vector<VoronoiDiagram.Region> myRegions = new Vector<>();
        for (int i = 0; i < pVertices.size(); i++) {
            final VoronoiDiagram.Region myRegionPoints = getRegion(pVertices, pDelaunayTriangles, i);
            myRegions.add(myRegionPoints);
        }
        return myRegions;
    }

    public static Vector<PVector> sort(Vector<PVector> thePoints) {

        if (thePoints.size() <= 3) {
            return thePoints;
        }

        oLastAngle = 0;

        PVector myLowestNode = new PVector(0, Float.MIN_VALUE, 0);
        for (PVector myNode : thePoints) {
            if (myNode.y > myLowestNode.y) {
                myLowestNode.set(myNode);
            } else if (myNode.y == myLowestNode.y && myNode.x < myLowestNode.x) {
                myLowestNode.set(myNode);
            }
        }

        Vector<PVector> mySortedPoints = new Vector<>();
        PVector myRefPoint = getNode(myLowestNode, thePoints, mySortedPoints);
        mySortedPoints.add(myRefPoint);
        while (!Util.almost(myRefPoint, myLowestNode)) {
            myRefPoint = getNode(myRefPoint, thePoints, mySortedPoints);
            mySortedPoints.add(myRefPoint);
            if (mySortedPoints.size() >= thePoints.size()) {
                break;
            }
        }
        mySortedPoints.add(myLowestNode);

        return mySortedPoints;
    }

    public static class Region {

        public Vector<PVector> hull = new Vector<>();

        public PVector center = new PVector();

    }
}
