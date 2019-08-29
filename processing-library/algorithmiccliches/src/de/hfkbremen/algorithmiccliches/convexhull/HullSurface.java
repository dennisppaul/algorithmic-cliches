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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

/**
 * A class representing a surface.
 *
 * @author Chris Pudney <cpudney@alphapharm.pharm.uwa.edu.au>
 * @version 1.1
 */
public class HullSurface {

    /**
     * The list of polygonal faces defining the surface.
     */
    private Vector faces;

    /**
     * Construct a surface containing no faces.
     */
    public HullSurface() {
        faces = new Vector();
    }

    public void init() {
        faces = new Vector();
    }

    /**
     * Construct a surface given a list of faces.
     *
     * @param f the list of faces
     */
    public HullSurface(Vector f) {
        faces = f;
    }

    /**
     * Get the faces of a surfaces.
     *
     * @return The vector of polygons that make up the surface.
     */
    public Vector getFaces() {
        return faces;
    }

    /**
     * Set the faces of a surfaces.
     */
    public void setFaces(Vector f) {
        faces = f;
    }

    /**
     * Get the vertices of a surface.
     *
     * @return The vector of vertices that make up a surface.
     */
    public Vector getVertices() {
        Vector vertices = new Vector();

        // Get the vertices of each face
        for (Enumeration e = faces.elements();
                e.hasMoreElements();) {
            Vector face_verts = ((HullPolygon) e.nextElement()).getVertices();
            for (Enumeration f = face_verts.elements();
                    f.hasMoreElements();) {
                HullVertex vertex = (HullVertex) f.nextElement();
                if (vertices.indexOf(vertex) == -1) {
                    vertices.addElement(vertex);
                }
            }
        }
        return vertices;
    }

    /**
     * Write an OFF file that describes a surface. OFF files can be viewed using
     * Geomview http://www.geom.umn.edu/software/download/geomview.html
     *
     * @param pw A PrintWriter for the file
     */
    public void writeOFF(PrintWriter pw) throws IOException {
        /* Write header */
        pw.println("OFF");
        Vector vertices = getVertices();
        pw.println(vertices.size() + " " + " " + faces.size() + " "
                   + (3 * faces.size()));

        /* Write vertex list */
        for (Enumeration v = vertices.elements();
                v.hasMoreElements();) {
            int[] c = ((HullVertex) v.nextElement()).getCoords();
            pw.println(c[0] + " " + c[1] + " " + c[2]);
        }

        /* Write polygon list */
        for (Enumeration f = faces.elements();
                f.hasMoreElements();) {
            Vector pV = ((HullPolygon) f.nextElement()).getVertices();
            pw.print(pV.size());
            for (Enumeration v = pV.elements();
                    v.hasMoreElements();) {
                pw.print(" " + vertices.indexOf((HullVertex) v.nextElement()));
            }
            pw.println();
        }
    }

    /**
     * Returns a string describing a surface.
     */
    public String toString() {
        String s = new String();

        for (Enumeration e = faces.elements();
                e.hasMoreElements();) {
            s += (HullPolygon) e.nextElement() + "\n";
        }
        return s;
    }
}
