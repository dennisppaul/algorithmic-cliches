package de.hfkbremen.algorithmiccliches.isosurface.marchingsquares;

import processing.core.PVector;
import teilchen.util.Linef;

import java.util.Vector;

public abstract class MarchingSquares {

    /**
     * cleaned up version of http://v3ga.free.fr 'liquid balls'
     */
    private static final int[][] _mySquareEdge = {{
        0, 1}, {
        1, 2}, {
        2, 3}, {
        3, 0}
    };

    private static final int[][] _myOffset = {{
        0, 0}, {
        1, 0}, {
        1, 1}, {
        0, 1}
    };

    private static final int[][] _myLine = {{
        -1, -1, -1, -1, -1}, {
        0, 3, -1, -1, -1}, {
        0, 1, -1, -1, -1}, {
        3, 1, -1, -1, -1}, {
        1, 2, -1, -1, -1}, {
        1, 2, 0, 3, -1}, {
        0, 2, -1, -1, -1}, {
        3, 2, -1, -1, -1}, {
        3, 2, -1, -1, -1}, {
        0, 2, -1, -1, -1}, {
        3, 2, 0, 2, -1}, {
        1, 2, -1, -1, -1}, {
        3, 1, -1, -1, -1}, {
        0, 1, -1, -1, -1}, {
        0, 3, -1, -1, -1}, {
        -1, -1, -1, -1, -1}
    };

    public static Vector<Linef> getLines(float[][] theArray, float theIsoValue) {
        final Vector<Linef> myLines = new Vector<>();
        for (int x = 0; x < theArray.length - 1; x++) {
            for (int y = 0; y < theArray[x].length - 1; y++) {
                int square_idx = 0;
                if (theArray[x][y] < theIsoValue) {
                    square_idx |= 1;
                }
                if (theArray[x][y + 1] < theIsoValue) {
                    square_idx |= 2;
                }
                if (theArray[x + 1][y + 1] < theIsoValue) {
                    square_idx |= 4;
                }
                if (theArray[x + 1][y] < theIsoValue) {
                    square_idx |= 8;
                }
                if (square_idx != 0 || square_idx != 15) {
                    int n = 0;
                    while (_myLine[square_idx][n] != -1) {
                        Linef myLine = new Linef();
                        myLines.add(myLine);
                        getPoint(theArray, theIsoValue, x, y, myLine.p1, _myLine[square_idx][n++]);
                        getPoint(theArray, theIsoValue, x, y, myLine.p2, _myLine[square_idx][n++]);
                    }
                }
            }
        }

        return myLines;
    }

    public static PVector[] getLinesAsArray(float[][] theArray, float theIsoValue) {
        Vector<Linef> myLines = getLines(theArray, theIsoValue);
        PVector[] myLinesArray = new PVector[myLines.size() * 2];
        int i = 0;
        for (Linef myLine : myLines) {
            myLinesArray[i++] = myLine.p1;
            myLinesArray[i++] = myLine.p2;
        }
        return myLinesArray;
    }

    private static void getPoint(float[][] theArray, float theIsoValue,
                                 int x, int y,
                                 PVector thePoint,
                                 int theEdgeIDx) {
        final int P1_idx = _mySquareEdge[theEdgeIDx][0];
        final int P2_idx = _mySquareEdge[theEdgeIDx][1];

        final float myValueA = theArray[x + _myOffset[P1_idx][1]][y + _myOffset[P1_idx][0]];
        final float myValueB = theArray[x + _myOffset[P2_idx][1]][y + _myOffset[P2_idx][0]];
        final float temp;
        if (myValueB - myValueA != 0) {
            temp = (theIsoValue - myValueA) / (myValueB - myValueA);
        } else {
            temp = 0.5f;
        }

        thePoint.y = (y + _myOffset[P1_idx][0])
                     + temp * ((y + _myOffset[P2_idx][0]) - (y + _myOffset[P1_idx][0]));
        thePoint.x = (x + _myOffset[P1_idx][1])
                     + temp * ((x + _myOffset[P2_idx][1]) - (x + _myOffset[P1_idx][1]));
    }
}
