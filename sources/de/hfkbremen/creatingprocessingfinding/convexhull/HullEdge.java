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
 * A class representing an edge.
 *
 * @author Chris Pudney <cpudney@alphapharm.pharm.uwa.edu.au>
 * @version 1.1
 */
public class HullEdge {

    /**
     * The end-points of the edge.
     */
    private HullVertex end1, end2;

    /**
     * Construct an edge using two vertices.
     *
     * @param v1, v2 the end-points of the edge
     */
    public HullEdge(HullVertex v1, HullVertex v2) {
        end1 = v1;
        end2 = v2;
    }

    /**
     * Check whether two edges are the same.
     *
     * @param e1, e2 the edges to compare
     * @see Vertex#sameVertex
     * @return True/false if the edges are the same/different.
     */
    public static boolean sameEdge(HullEdge e1, HullEdge e2) {
        return (e1 == e2
                || (HullVertex.sameVertex(e1.end1, e2.end1)
                && HullVertex.sameVertex(e1.end2, e2.end2))
                || (HullVertex.sameVertex(e1.end2, e2.end1)
                && HullVertex.sameVertex(e1.end1, e2.end2)));
    }

    /**
     * Returns the pair of end-point vertices of an edge.
     *
     * @return An array of two vertices in the order they were stored.
     */
    public HullVertex[] getVertices() {
        HullVertex v[] = new HullVertex[2];
        v[0] = end1;
        v[1] = end2;
        return v;
    }

    /**
     * Produce a string describing an edge.
     */
    public String toString() {
        return "[" + end1 + ", " + end2 + "]";
    }
}
