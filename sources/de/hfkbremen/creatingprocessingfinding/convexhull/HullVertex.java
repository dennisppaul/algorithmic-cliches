// Copyright (C) Chris Pudney, The University of Western Australia, 1998.
// All rights reserved.
//
// Permission to use, copy, modify and distribute this software and its
// documentation only for the purposes of teaching and research is hereby
// granted without fee, provided that the above copyright notice and this
// permission notice appear in all copies of this software/documentation
// and that you do not sell the software.  No commercial use or
// distribution of the software is permitted without the consent of the
// copyright owners.  Commercial licensing is available by contacting the
// author(s).
//
// THIS SOFTWARE/DOCUMENTATION IS PROVIDED WITH NO WARRANTY, EXPRESS OR
// IMPLIED, INCLUDING, WITHOUT LIMITATION, WARRANTY OF MERCHANTABILITY OR
// FITNESS FOR A PARTICULAR PURPOSE.
package de.hfkbremen.creatingprocessingfinding.convexhull;


/**
 * A class defining a vertex.
 *
 * @author Chris Pudney <cpudney@alphapharm.pharm.uwa.edu.au>
 * @version 1.1
 */
public class HullVertex {

    /**
     * The 3D coordinates (x, y, z) of the vertex.
     */
    public int x, y, z;

    /**
     * Construct the vertex at (0, 0, 0).
     */
    public HullVertex() {
        x = 0;
        y = 0;
        z = 0;
    }

    /**
     * Construct a vertex given a set of coordinates.
     *
     * @param x, y, z The coordinates.
     */
    public HullVertex(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public HullVertex(float x, float y, float z) {
        this.x = (int) x;
        this.y = (int) y;
        this.z = (int) z;
    }

    /**
     * Get the coordinates of the vertex.
     *
     * @return An array of three integers containing the coordinates (x, y, z).
     */
    public int[] getCoords() {
        int[] c = new int[3];

        c[0] = x;
        c[1] = y;
        c[2] = z;
        return c;
    }

    /**
     * Tests whether two vertices are the same point.
     *
     * @param v1, v2 the vertices to compare
     * @return True/false if the vertices are the same/different.
     */
    public static boolean sameVertex(HullVertex v1, HullVertex v2) {
        return v1 == v2
                || (v1.x == v2.x
                && v1.y == v2.y
                && v1.z == v2.z);
    }

    /**
     * Tests whether three vertices are collinear.
     *
     * @param v1, v2, v3 the vertices to test
     * @return True/false if the vertices are collinear/non-collinear
     */
    public static boolean collinear(HullVertex v1, HullVertex v2, HullVertex v3) {
        long x1 = v1.x, y1 = v1.y, z1 = v1.z;
        long x2 = v2.x, y2 = v2.y, z2 = v2.z;
        long x3 = v3.x, y3 = v3.y, z3 = v3.z;
        return (z3 - z1) * (y2 - y1) - (z2 - z1) * (y3 - y1) == 0
                && (z2 - z1) * (x3 - x1) - (x2 - x1) * (z3 - z1) == 0
                && (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1) == 0;
    }

    /**
     * Returns the pair of vertices that bound three collinear vertices.
     *
     * @param v1, v2, v3 the vertices to test
     * @return An array of two vertices that bound the tested vertices
     */
    public static HullVertex[] bounds(HullVertex v1, HullVertex v2, HullVertex v3) {
        HullVertex min12, max12;

        // Compare v1 and v2
        if (v1.x == v2.x) {
            if (v1.y == v2.y) {
                if (v1.z <= v2.z) {
                    min12 = v1;
                    max12 = v2;
                } else {
                    min12 = v2;
                    max12 = v1;
                }
            } else if (v1.y < v2.y) {
                min12 = v1;
                max12 = v2;
            } else {
                min12 = v2;
                max12 = v1;
            }
        } else if (v1.x < v2.x) {
            min12 = v1;
            max12 = v2;
        } else {
            min12 = v2;
            max12 = v1;
        }

        HullVertex[] bounds = new HullVertex[2];

        // Compare min of v1, v2 with v3
        if (min12.x == v3.x) {
            if (min12.y == v3.y) {
                bounds[0] = min12.z <= v3.z ? min12 : v3;
            } else {
                bounds[0] = min12.y < v3.y ? min12 : v3;
            }
        } else {
            bounds[0] = min12.x < v3.x ? min12 : v3;
        }


        // Compare max of v1, v2 with v3
        if (max12.x == v3.x) {
            if (max12.y == v3.y) {
                bounds[1] = max12.z >= v3.z ? max12 : v3;
            } else {
                bounds[1] = max12.y > v3.y ? max12 : v3;
            }
        } else {
            bounds[1] = max12.x > v3.x ? max12 : v3;
        }
        return bounds;
    }

    /**
     * Returns the square of the distance between two vertices.
     *
     * @param v1, v2 the vertices
     * @return the distance squared between them
     */
    public static long distanceSquared(HullVertex v1,
            HullVertex v2) {
        long x1 = v1.x, y1 = v1.y, z1 = v1.z;
        long x2 = v2.x, y2 = v2.y, z2 = v2.z;
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)
                + (z1 - z2) * (z1 - z2);
    }

    /**
     * Returns the square of the norm of a vector
     *
     * @param v the vector
     * @return the square of the norm of v
     */
    public long normSq() {
        long x = this.x;
        long y = this.y;
        long z = this.z;
        return x * x + y * y + z * z;
    }

    /**
     * Returns the dot product of two vectors
     *
     * @param v1, v2 the vectors
     * @return v1.v2
     */
    public static long dotProduct(HullVertex v1,
            HullVertex v2) {
        long x1 = v1.x, y1 = v1.y, z1 = v1.z;
        long x2 = v2.x, y2 = v2.y, z2 = v2.z;
        return x1 * x2 + y1 * y2 + z1 * z2;
    }

    /**
     * Returns the vector difference between two vertices
     *
     * @param v1, v2 the vertices
     * @return v1 - v2
     */
    public static HullVertex vectorDiff(HullVertex v1,
            HullVertex v2) {
        return new HullVertex(v1.x - v2.x,
                v1.y - v2.y,
                v1.z - v2.z);
    }

    /**
     * Returns the cross product of two vectors
     *
     * @param v1, v2 the vectors
     * @return v1 x v2
     */
    public static HullVertex crossProduct(HullVertex v1,
            HullVertex v2) {
        return new HullVertex(v1.y * v2.z - v1.z * v2.y,
                v1.z * v2.x - v1.x * v2.z,
                v1.x * v2.y - v1.y * v2.x);
    }

    /**
     * Returns a string that describes a vertex.
     */
    public String toString() {
        return ("(" + x + ", " + y + ", " + z + ")");
    }
}
