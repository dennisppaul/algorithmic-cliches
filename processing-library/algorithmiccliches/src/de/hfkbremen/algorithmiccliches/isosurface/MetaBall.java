/**
 * data structure for a metaball
 */

package de.hfkbremen.algorithmiccliches.isosurface;

import processing.core.PVector;

public class MetaBall {

    public PVector position;

    public float strength;

    public float radius;

    public MetaBall(PVector thePosition, float theStrength, float theRadius) {
        position = thePosition;
        strength = theStrength;
        radius = theRadius;
    }
}
