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
 * A class representing the convex hull of a set of points.
 *
 * @author - Chris Pudney <mailto:cpudney@alphapharm.pharm.uwa.edu.au>
 * @version - 1.1
 */
public class ConvexHull
        extends DSurface {

    /**
     * Acceptable range of coordinates.
     */
    private static final int COORD_RANGE = 1000000;

    // private static final int COORD_RANGE = 512;   Use with Volume6()
    /**
     * Constructs a convex hull initialised to a null surface.
     */
    public ConvexHull() {
        super();
    }

    public void calculateHull(Vector vertices) {
        init();
        if (vertices.size() < 4) {
        }
        Enumeration e = vertices.elements();
        if (e.hasMoreElements()) {
            HullVertex v1 = (HullVertex) e.nextElement();
            HullVertex v2 = null;
            for (; e.hasMoreElements();) {
                v2 = (HullVertex) e.nextElement();
                if (!HullVertex.sameVertex(v1, v2)) {
                    break;
                }
            }

            HullVertex v3 = new HullVertex();
            HullTriangle t = null;
            Vector coVerts = new Vector();
            for (; e.hasMoreElements();) {
                v3 = (HullVertex) e.nextElement();
                if (HullVertex.collinear(v1, v2, v3)) {
                    coVerts.addElement(v3);
                } else {
                    t = new HullTriangle(v1, v2, v3);
                    break;
                }
            }

            HullVertex v4 = new HullVertex();
            for (; e.hasMoreElements();) {
                v4 = (HullVertex) e.nextElement();
                int volSign = t.volumeSign(v4);
                if (volSign == 0) {
                    coVerts.addElement(v4);
                } else {
                    addFace(t);
                    triToTet(t, v4, volSign);
                    break;
                }
            }

            // Check vertex coords
            checkVertex(v1);
            checkVertex(v2);
            checkVertex(v3);
            checkVertex(v4);

            int i = 0;

            // Add vertices to the hull one at a time
            for (; e.hasMoreElements();) {
                addVertex((HullVertex) e.nextElement());
            }

            // Reprocess the previously found co-linear/planar vertices
            if (getFaces().size() > 0) {
                for (e = coVerts.elements();
                        e.hasMoreElements();) {
                    addVertex((HullVertex) e.nextElement());
                }

            } else {
            }
        }
    }

    /**
     * Constructs the convex hull of a set of vertices.
     *
     * @param - vertices the set of vertices
     * @exception - ConvexHullException if hull construction fails
     */
    public ConvexHull(Vector vertices) {
        calculateHull(vertices);
    }

    public float[] getVerticesArray() {
        float[] myVertices = new float[getFaces().size() * 3 * 3];
        for (int i = 0; i < getFaces().size(); i++) {
            HullPolygon myFace = (HullPolygon) getFaces().get(i);
            HullVertex myVertex = (HullVertex) (myFace.getVertices().get(0));
            myVertices[i * 9 + 0] = myVertex.x;
            myVertices[i * 9 + 1] = myVertex.y;
            myVertices[i * 9 + 2] = myVertex.z;

            myVertex = (HullVertex) (myFace.getVertices().get(1));
            myVertices[i * 9 + 3] = myVertex.x;
            myVertices[i * 9 + 4] = myVertex.y;
            myVertices[i * 9 + 5] = myVertex.z;

            myVertex = (HullVertex) (myFace.getVertices().get(2));
            myVertices[i * 9 + 6] = myVertex.x;
            myVertices[i * 9 + 7] = myVertex.y;
            myVertices[i * 9 + 8] = myVertex.z;
        }
        return myVertices;
    }

    /**
     * Determine the hull faces that have vertices in each of two sets
     *
     * @param - s1, s2 the vertex sets
     * @return - returns a vector of triangles that lie on the hull
     */
    public Vector interSetFaces(Vector s1, Vector s2) {
        Vector faces = getFaces();
        Vector xFaces = new Vector();

        for (Enumeration e = faces.elements();
                e.hasMoreElements();) {
            HullTriangle t = (HullTriangle) e.nextElement();
            Vector v = t.getVertices();
            HullVertex v1 = (HullVertex) v.firstElement();
            HullVertex v2 = (HullVertex) v.elementAt(1);
            HullVertex v3 = (HullVertex) v.lastElement();
            if ((s1.contains(v1) || s1.contains(v2) || s1.contains(v3))
                    && (s2.contains(v1) || s2.contains(v2) || s2.contains(v3))) {
                xFaces.addElement(t);
            }
        }
        return xFaces;
    }

    /**
     * Check that the vertex coordinates are within bounds
     *
     * @param - vertex the vertex to check
     * @return - True/false if the vertex is inside/outside the legal bounds
     * @see - COORD_RANGE
     */
    private static boolean checkVertex(HullVertex vertex) {
        int[] c = vertex.getCoords();
        if (Math.abs(c[0]) > COORD_RANGE || Math.abs(c[1]) > COORD_RANGE
                || Math.abs(c[2]) > COORD_RANGE) {
            System.out.println("Warning: vertex coordinates > " + COORD_RANGE + " or < "
                    + -COORD_RANGE + " may create problems");
            return false;
        }
        return true;
    }

    /**
     * Form a tetrahedron from vertex and the existing triangular hull.
     *
     * @param face - a triangular face of the tetrahedron
     * @param vertex - the fourth point of the tetrahedron
     * @param vol - indicates on which side of face vertex lies
     */
    private void triToTet(HullPolygon face, HullVertex vertex, int vol) {
        Vector v = face.getVertices();
        HullVertex v1 = (HullVertex) v.elementAt(0);
        HullVertex v2 = (HullVertex) v.elementAt(1);
        HullVertex v3 = (HullVertex) v.elementAt(2);

        // Store the vertices in CCW order
        if (vol < 0) {
            v.setElementAt(v3, 0);
            v.setElementAt(v1, 2);
            HullVertex tv = v1;
            v1 = v3;
            v3 = tv;
        }
        addFace(new HullTriangle(v3, v2, vertex));
        addFace(new HullTriangle(v2, v1, vertex));
        addFace(new HullTriangle(v1, v3, vertex));
    }

    /**
     * Add a vertex to a convex hull. Determine all faces visible from the
     * vertex. If none are visible then the point is marked as inside the hull.
     * Delete the visible faces and construct faces between the vertex and the
     * edges that border the visible faces.
     *
     * @param vertex - the vertex to add to the convex hull.
     */
    private void addVertex(HullVertex vertex) {
        Vector visEdges = new Vector();
        Vector visFaces = new Vector();

        // Check vertex coordinates
        checkVertex(vertex);

        // Delete visible faces
        for (Enumeration e = getFaces().elements();
                e.hasMoreElements();) {
            HullTriangle face = (HullTriangle) e.nextElement();
            if (face.volumeSign(vertex) < 0) {
                visFaces.addElement(face);
                // System.out.println(vertex + " visible from " + face);
            }
            // else
            // {
            // System.out.println(vertex + " NOT visible from " + face);
            // }
        }
        // Delete visible faces and construct visible edges list
        for (Enumeration e = visFaces.elements();
                e.hasMoreElements();) {
            HullPolygon face = (HullPolygon) e.nextElement();
            deleteVisibleFace(face, visEdges);
        }

        //System.out.println("Visible edges: " + visEdges);

        // Construct new faces using visible edges
        for (Enumeration f = visEdges.elements();
                f.hasMoreElements();) {
            HullEdge edge = (HullEdge) f.nextElement();
            HullVertex ends[] = edge.getVertices();
            addFace(new HullTriangle(ends[0], ends[1], vertex));
        }
    }

    /**
     * Delete a visible face from the convex hull. Adjust the list of visible
     * edges accordingly.
     *
     * @param face - a face visible from a vertex to be deleted
     * @param visibleEdges - the list of hull edges visible from a vertex
     */
    private void deleteVisibleFace(HullPolygon face, Vector visibleEdges) {
        Vector v = face.getVertices();
        HullVertex v1 = (HullVertex) v.elementAt(0);
        HullVertex v2 = (HullVertex) v.elementAt(1);
        HullVertex v3 = (HullVertex) v.elementAt(2);
        HullEdge e1 = new HullEdge(v1, v2);
        HullEdge e2 = new HullEdge(v2, v3);
        HullEdge e3 = new HullEdge(v3, v1);
        updateVisibleEdges(e1, visibleEdges);
        updateVisibleEdges(e2, visibleEdges);
        updateVisibleEdges(e3, visibleEdges);
        deleteFace(face);
    }

    /**
     * Update the visible edge list. If e is not in the list then add it if it
     * is then delete it from the list
     *
     * @param e - a visible edge
     * @param visibleEdges - a list of edges visible from a vertex
     */
    private void updateVisibleEdges(HullEdge e, Vector visibleEdges) {
        Enumeration f;
        boolean same = false;

        for (f = visibleEdges.elements();
                f.hasMoreElements();) {
            HullEdge edge = (HullEdge) f.nextElement();
            if (HullEdge.sameEdge(e, edge)) {
                same = true;
                e = edge;
                break;
            }
        }
        if (same) {
            visibleEdges.removeElement(e);
        } else {
            visibleEdges.addElement(e);
        }
    }
}
