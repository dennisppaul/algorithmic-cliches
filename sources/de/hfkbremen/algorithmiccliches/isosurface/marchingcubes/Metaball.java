/**
 * data structure for a metaball
 */
package de.hfkbremen.algorithmiccliches.isosurface.marchingcubes;


import mathematik.Vector3f;


public class Metaball {

    public Vector3f position;

    public float strength;

    public float radius;

    public Metaball(Vector3f thePosition, float theStrength, float theRadius) {
        position = thePosition;
        strength = theStrength;
        radius = theRadius;
    }
}
