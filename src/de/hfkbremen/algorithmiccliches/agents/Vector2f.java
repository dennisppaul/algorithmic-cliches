package de.hfkbremen.algorithmiccliches.agents;

public class Vector2f {

    public float x = 0;

    public float y = 0;

    public void set(Vector2f theVector) {
        x = theVector.x;
        y = theVector.y;
    }

    public void set(float theX, float theY) {
        x = theX;
        y = theY;
    }

    public void add(Vector2f theVector) {
        x += theVector.x;
        y += theVector.y;
    }

    public void sub(Vector2f theVector) {
        x -= theVector.x;
        y -= theVector.y;
    }

    public void scale(float s) {
        x *= s;
        y *= s;
    }

    public float length() {
        float myLengthSquard = x * x + y * y;
        float myLength = (float) Math.sqrt(myLengthSquard);
        return myLength;
    }

    public void normalize() {
        float d = length();
        x /= d;
        y /= d;
    }

    public void cross(Vector2f a) {
        /*
         *
         * the cross product does not make much sense in this context.
         * we just asume that vector b is the z-axis and the z component
         * of our vector a is 0. after simplifing we have
         * x = a.y and y = -a.y
         *
         * Vector3f b = new Vector3f(0, 0, 1);
         * Vector3f a = new Vector3f(x, y, 0);
         * x = a.y * b.z - a.z * b.y;
         * y = b.x * a.z - b.z * a.x;
         * z = a.x * b.y - a.y * b.x;
         *
         */
        x = a.y;
        y = -a.x;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
