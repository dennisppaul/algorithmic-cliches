package de.hfkbremen.algorithmiccliches.agents;


public class Vector3f {

    public float x = 0;

    public float y = 0;

    public float z = 0;

    public void set(Vector3f theVector) {
        x = theVector.x;
        y = theVector.y;
        z = theVector.z;
    }

    public void set(float theX, float theY, float theZ) {
        x = theX;
        y = theY;
        z = theZ;
    }

    public void add(Vector3f theVector) {
        x += theVector.x;
        y += theVector.y;
        z += theVector.z;
    }

    public void sub(Vector3f theVector) {
        x -= theVector.x;
        y -= theVector.y;
        z -= theVector.z;
    }

    public void multiply(float s) {
        x *= s;
        y *= s;
        z *= s;
    }

    public float length() {
        float myLengthSquard = x * x + y * y + z * z;
        float myLength = (float) Math.sqrt(myLengthSquard);
        return myLength;
    }

    public void normalize() {
        float d = length();
        x /= d;
        y /= d;
        z /= d;
    }

    public void cross(Vector3f a, Vector3f b) {
        x = a.y * b.z - a.z * b.y;
        y = b.x * a.z - b.z * a.x;
        z = a.x * b.y - a.y * b.x;
    }

    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
