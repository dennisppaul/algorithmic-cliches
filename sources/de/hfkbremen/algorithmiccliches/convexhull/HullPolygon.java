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
package de.hfkbremen.algorithmiccliches.convexhull;


import java.util.Enumeration;
import java.util.Vector;


/**
 * A class representing a polygon
 *
 * @author Chris Pudney <cpudney@alphapharm.pharm.uwa.edu.au>
 * @version 1.1
 */
public class HullPolygon {

    /**
     * The set of vertices representing the polygon. The vertices must be stored
     * in the vector in the order in which they define polygon perimeter. For
     * planar polygons this should be clockwise with respect to the outwards
     * pointing normal.
     */
    private Vector vertices;	// Polygon vertices

    /**
     * Construct a polygon with no vertices.
     */
    public HullPolygon() {
        vertices = new Vector();
    }

    /**
     * Construct a polygon given a list of vertices.
     *
     * @param v the list of vertices
     */
    public HullPolygon(Vector v) {
        vertices = v;
    }

    /**
     * Reverse the order of the vertices that define a contour
     */
    public void reverse() {
        /* Add vertices in reverse order */
        Vector vertices = getVertices();
        int size = vertices.size();
        for (int i = 1;
                i < size;
                i++) {
            vertices.insertElementAt(vertices.elementAt(i), 0);
            vertices.removeElementAt(i + 1);
        }
    }

    /**
     * Get the vertices of a polygon.
     *
     * @return Returns a vector p
     */
    public Vector getVertices() {
        return vertices;
    }

    /**
     * Returns the next vertex of a polygon
     *
     * @param v the vertex
     * @return the vertex following v, or null if v is not a vertex of the
     * polygon
     */
    public HullVertex nextVertex(HullVertex v) {
        int ind = vertices.indexOf(v);
        return (HullVertex) (ind == -1
                ? null : vertices.elementAt((ind + 1) % vertices.size()));
    }

    /**
     * Returns a string describing a polygon.
     */
    public String toString() {
        String s = new String();

        for (Enumeration e = getVertices().elements();
                e.hasMoreElements();) {
            s += e.nextElement();
        }
        return s;
    }
}
