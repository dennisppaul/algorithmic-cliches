package de.hfkbremen.algorithmiccliches.isosurface;

import processing.core.PVector;

public class MetaCircle {

    private PVector mPosition = new PVector();

    private float mStrength = 30000;

    public PVector position() {
        return mPosition;
    }

    public void strength(float pStrength) {
        mStrength = pStrength;
    }

    public float strength() {
        return mStrength;
    }

    public float getStrengthAt(float x, float y, float z) {
        float dx;
        float dy;
        float dz;
        dx = mPosition.x - x;
        dy = mPosition.y - y;
        dz = mPosition.z - z;
        return mStrength / (dx * dx + dy * dy + dz * dz);
    }

    public float getStrengthAt(float x, float y) {
        float dx;
        float dy;
        dx = mPosition.x - x;
        dy = mPosition.y - y;
        return mStrength / (dx * dx + dy * dy);
    }
}
