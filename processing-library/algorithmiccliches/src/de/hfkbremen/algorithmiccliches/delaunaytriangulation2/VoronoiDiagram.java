package de.hfkbremen.algorithmiccliches.delaunaytriangulation2;

import processing.core.PVector;
import teilchen.util.Util;

import java.util.ArrayList;

public class VoronoiDiagram {

    /* utility functions */
    private static float oLastAngle;

    public static VoronoiDiagram.Region getRegion(final ArrayList<PVector> pVertices,
                                                  final ArrayList<DelaunayTriangle> pDelaunayTriangles,
                                                  final int pCenterVertexIndex) {

        final VoronoiDiagram.Region myVoronoiRegion = new VoronoiDiagram.Region();

        if (pDelaunayTriangles != null && pVertices != null) {
            /* get connected triangles */
            final ArrayList<DelaunayTriangle> myTriangles = getConnectedTriangles(pDelaunayTriangles,
                                                                                  pCenterVertexIndex);
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

    public static ArrayList<VoronoiDiagram.Region> getRegions(final ArrayList<PVector> pVertices,
                                                              final ArrayList<DelaunayTriangle> pDelaunayTriangles) {
        final ArrayList<VoronoiDiagram.Region> myRegions = new ArrayList<>();
        for (int i = 0; i < pVertices.size(); i++) {
            final VoronoiDiagram.Region myRegionPoints = getRegion(pVertices, pDelaunayTriangles, i);
            myRegions.add(myRegionPoints);
        }
        return myRegions;
    }

    public static ArrayList<PVector> sort(ArrayList<PVector> thePoints) {

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

        ArrayList<PVector> mySortedPoints = new ArrayList<>();
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

    private static ArrayList<DelaunayTriangle> getConnectedTriangles(final ArrayList<DelaunayTriangle> pTriangles,
                                                                     final int pVertexIndex) {
        final ArrayList<DelaunayTriangle> myTriangles = new ArrayList<>();
        for (DelaunayTriangle pTriangle : pTriangles) {
            if (pTriangle.p[0] == pVertexIndex || pTriangle.p[1] == pVertexIndex || pTriangle.p[2] == pVertexIndex) {
                myTriangles.add(pTriangle);
            }
        }
        return myTriangles;
    }

    private static PVector getNode(PVector theRefPoint,
                                   ArrayList<PVector> thePoints,
                                   ArrayList<PVector> theSortedPoins) {
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

    private static boolean isAlreadySorted(PVector theRefPoint, ArrayList<PVector> thePoints) {
        for (PVector myNode : thePoints) {
            if (theRefPoint.equals(myNode)) {
                return true;
            }
        }
        return false;
    }

    public static class Region {

        public PVector center = new PVector();
        public ArrayList<PVector> hull = new ArrayList<>();

    }
}
