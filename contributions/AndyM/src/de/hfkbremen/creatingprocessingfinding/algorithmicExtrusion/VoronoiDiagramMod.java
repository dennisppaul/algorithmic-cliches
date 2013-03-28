package de.hfkbremen.creatingprocessingfinding.algorithmicExtrusion;


import de.hfkbremen.creatingprocessingfinding.algorithmicExtrusion.DelaunayTriangulation.Triangle;
import mathematik.Vector3f;

import java.util.Vector;


public class VoronoiDiagramMod {

    public static class Region {

        public Vector<Vector3f> hull = new Vector<Vector3f>();

        public Vector3f center = new Vector3f();

    }

    /* utility functions */
    private static float oLastAngle;

    public static VoronoiDiagramMod.Region getRegion(final Vector<Vector3f> pVertices,
            final Vector<Triangle> pDelaunayTriangles,
            final int pCenterVertexIndex) {

        final VoronoiDiagramMod.Region myVoronoiRegion = new VoronoiDiagramMod.Region();

        if (pDelaunayTriangles != null && pVertices != null) {
            /* get connected triangles */
            final Vector<Triangle> myTriangles = getConnectedTriangles(pDelaunayTriangles, pCenterVertexIndex);
            /* get umkreis */
            for (Triangle myTriangle : myTriangles) {
                /* get region points */
                final Vector3f myCenter = DelaunayTriangulation.getCenter(pVertices, myTriangle);
                myVoronoiRegion.hull.add(myCenter);
            }
            /* sort region points */
            myVoronoiRegion.hull = sort(myVoronoiRegion.hull);
            myVoronoiRegion.center.set(pVertices.get(pCenterVertexIndex));
        }

        return myVoronoiRegion;
    }

    public static Vector<VoronoiDiagramMod.Region> getRegions(final Vector<Vector3f> pVertices,
            final Vector<Triangle> pDelaunayTriangles) {
        final Vector<VoronoiDiagramMod.Region> myRegions = new Vector<VoronoiDiagramMod.Region>();
        for (int i = 0; i < pVertices.size(); i++) {
            final VoronoiDiagramMod.Region myRegionPoints = getRegion(pVertices, pDelaunayTriangles, i);
            myRegions.add(myRegionPoints);
        }
        return myRegions;
    }

    private static Vector<Triangle> getConnectedTriangles(final Vector<Triangle> pTriangles,
            final int pVertexIndex) {
        final Vector<Triangle> myTriangles = new Vector<Triangle>();
        for (int i = 0; i < pTriangles.size(); i++) {
            if (pTriangles.get(i).p[0] == pVertexIndex
                    || pTriangles.get(i).p[1] == pVertexIndex
                    || pTriangles.get(i).p[2] == pVertexIndex) {
                myTriangles.add(pTriangles.get(i));
            }
        }
        return myTriangles;
    }

    public static Vector<Vector3f> sort(Vector<Vector3f> thePoints) {

        if (thePoints.size() <= 3) {
            return thePoints;
        }

        oLastAngle = 0;

        Vector3f myLowestNode = new Vector3f(0, Float.MIN_VALUE, 0);
        for (int i = 0; i < thePoints.size(); i++) {
            Vector3f myNode = thePoints.get(i);
            if (myNode.y > myLowestNode.y) {
                myLowestNode.set(myNode);
            } else if (myNode.y == myLowestNode.y && myNode.x < myLowestNode.x) {
                myLowestNode.set(myNode);
            }
        }

        Vector<Vector3f> mySortedPoints = new Vector<Vector3f>();
        Vector3f myRefPoint = getNode(myLowestNode, thePoints, mySortedPoints);
        mySortedPoints.add(myRefPoint);
        while (!myRefPoint.almost(myLowestNode)) {
            myRefPoint = getNode(myRefPoint, thePoints, mySortedPoints);
            mySortedPoints.add(myRefPoint);
            if (mySortedPoints.size() >= thePoints.size()) {
                break;
            }
        }
        mySortedPoints.add(myLowestNode);

        return mySortedPoints;
    }

    private static Vector3f getNode(Vector3f theRefPoint,
            Vector<Vector3f> thePoints,
            Vector<Vector3f> theSortedPoins) {
        float myMinAngle = 10;
        Vector3f myNextPoint = new Vector3f(theRefPoint);
        for (int i = 0; i < thePoints.size(); i++) {
            Vector3f myNode = thePoints.get(i);
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

    private static boolean isAlreadySorted(Vector3f theRefPoint, Vector<Vector3f> thePoints) {
        for (int i = 0; i < thePoints.size(); i++) {
            Vector3f myNode = thePoints.get(i);
            if (theRefPoint.equals(myNode)) {
                return true;
            }
        }
        return false;
    }
}
