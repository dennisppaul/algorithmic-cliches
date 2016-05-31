/**
 * data structure for a metaball
 */

package de.hfkbremen.algorithmiccliches.isosurface.marchingcubes;

import processing.core.PVector;

public class Metaball {

    public PVector position;

    public float strength;

    public float radius;

    public Metaball(PVector thePosition, float theStrength, float theRadius) {
        position = thePosition;
        strength = theStrength;
        radius = theRadius;
    }
}
