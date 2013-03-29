package de.mathiaslam.algorithmicclichees;


class Vector2f {

    float x = 0;

    float y = 0;

    public Vector2f(float x_, float y_) {
        x = x_;
        y = y_;
    }

    public void add(Vector2f theVector) {
        x += theVector.x;
        y += theVector.y;
    }

    public void sub(Vector2f theVector) {
        x -= theVector.x;
        y -= theVector.y;
    }

    public void multiply(float s) {
        x *= s;
        y *= s;
    }

    public void divide(float d) {
        x /= d;
        y /= d;
    }

    public float magnitude() {
        return (float) Math.sqrt(x * x + y * y);
    }

    static public float distance(Vector2f v1, Vector2f v2) {
        float dx = v1.x - v2.x;
        float dy = v1.y - v2.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public void normalize() {
        float mMagnitude;
        mMagnitude = magnitude();
        x /= mMagnitude;
        y /= mMagnitude;
    }

    public void set(Vector2f theVector) {
        x = theVector.x;
        y = theVector.y;
    }

    void add(int i, int i0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
};
