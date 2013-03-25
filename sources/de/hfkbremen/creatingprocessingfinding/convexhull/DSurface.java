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


import java.util.Enumeration;
import java.util.Vector;


/**
 * A class representing a dynamic surface.
 *
 * @author Chris Pudney <mailto:cpudney@alphapharm.pharm.uwa.edu.au>
 * @version 1.1
 */
public class DSurface
        extends HullSurface {

    /**
     * Add a polygonal face to a surface.
     *
     * @param p a polygon representing the face
     */
    public void addFace(HullPolygon p) {
        getFaces().addElement(p);
    }

    /**
     * Add a set of polygonal faces to a surface.
     *
     * @param v vector of polygonal faces
     */
    public void addFaces(Vector v) {
        for (Enumeration e = v.elements();
                e.hasMoreElements();) {
            addFace((HullPolygon) e.nextElement());
        }
    }

    /**
     * Delete a polygonal face from a surface.
     *
     * @param a polygon representing the face
     */
    public boolean deleteFace(HullPolygon p) {
        return getFaces().removeElement(p);
    }
}
