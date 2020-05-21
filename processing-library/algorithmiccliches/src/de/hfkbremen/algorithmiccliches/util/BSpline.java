package de.hfkbremen.algorithmiccliches.util;

import processing.core.PVector;

import java.util.ArrayList;

public abstract class BSpline {

    /**
     * b-spline source code from: Tim Lambert 'nice page on curves, splines etc.'
     * http://www.cse.unsw.edu.au/~lambert/splines/
     */

    public static ArrayList<PVector> curve(ArrayList<PVector> thePoints, int theSteps, ArrayList<PVector> theResult) {
        for (int i = 2; i < thePoints.size() - 1; i++) {
            for (int j = 1; j <= theSteps; j++) {
                theResult.add(p(thePoints, i, j / (float) theSteps));
            }
        }
        return theResult;
    }

    public static ArrayList<PVector> curve(ArrayList<PVector> thePoints, int theSteps) {
        return curve(thePoints, theSteps, new ArrayList<PVector>());
    }

    public static ArrayList<PVector> closeCurve(ArrayList<PVector> thePoints) {
        /* copy points */
        ArrayList<PVector> myClosedPoints = new ArrayList<PVector>(thePoints);

        /* repeat first three points */
        if (thePoints.size() > 2) {
            myClosedPoints.add(thePoints.get(0));
            myClosedPoints.add(thePoints.get(1));
            myClosedPoints.add(thePoints.get(2));
        }
        return myClosedPoints;
    }

    /* the basis function for a cubic B spline */
    private static float b(int i, float t) {
        switch (i) {
            case -2:
                return (((-t + 3) * t - 3) * t + 1) / 6;
            case -1:
                return (((3 * t - 6) * t) * t + 4) / 6;
            case 0:
                return (((-3 * t + 3) * t + 3) * t + 1) / 6;
            case 1:
                return (t * t * t) / 6;
        }
        return 0; //we only get here if an invalid i is specified
    }

    /* evaluate a point on the B spline */
    private static PVector p(ArrayList<PVector> thePoints, int i, float t) {
        PVector p = new PVector();
        for (int j = -2; j <= 1; j++) {
            p.x += b(j, t) * thePoints.get(i + j).x;
            p.y += b(j, t) * thePoints.get(i + j).y;
            p.z += b(j, t) * thePoints.get(i + j).z;
        }
        return p;
    }
}
