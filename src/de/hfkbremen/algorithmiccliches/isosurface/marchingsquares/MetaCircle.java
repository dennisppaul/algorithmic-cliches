package de.hfkbremen.algorithmiccliches.isosurface.marchingsquares;

import mathematik.Vector3f;

public class MetaCircle {

    private Vector3f mPosition = new Vector3f();

    private float mStrength = 30000;

    public Vector3f position() {
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
