package de.hfkbremen.algorithmiccliches.util;

public class Vector3i {

    public int x;

    public int y;

    public int z;

    public Vector3i() {
        x = 0;
        y = 0;
        z = 0;
    }

    public Vector3i(int theX, int theY, int theZ) {
        set(theX, theY, theZ);
    }

    public Vector3i(Vector3i theVector) {
        set(theVector);
    }

    public Vector3i(int[] theVector) {
        set(theVector);
    }

    public void set(int theX, int theY, int theZ) {
        x = theX;
        y = theY;
        z = theZ;
    }

    public void set(Vector3i theVector) {
        x = theVector.x;
        y = theVector.y;
        z = theVector.z;
    }

    public void set(int[] theVector) {
        x = theVector[0];
        y = theVector[1];
        z = theVector[2];
    }

    public final String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

    public final float lengthSquared() {
        return x * x + y * y + z * z;
    }

    public int compareTo(Vector3i theVector3i) {
        return (int) (lengthSquared() - theVector3i.lengthSquared());
    }
}
