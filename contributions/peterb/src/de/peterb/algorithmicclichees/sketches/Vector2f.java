package de.peterb.algorithmicclichees.sketches;


public class Vector2f {

    float x;

    float y;

    public Vector2f() {
    }

    public Vector2f(float x_, float y_) {
        x = x_;
        y = y_;
    }

    public Vector2f(Vector2f v) {
        x = v.x;
        y = v.y;
    }

    public void set(Vector2f v) {
        x = v.x;
        y = v.y;

    }

    public void set(float x_, float y_) {
        x = x_;
        y = y_;

    }

    /* magnitude(länge) rausfinden*/
    public float mag() {
        return (float) Math.sqrt(x * x + y * y);
    }

    /*NORMALIZE*/
    public void normalize() {
        float mMagnitude;
        mMagnitude = mag();

        if (mMagnitude > 0.0f) {
            x /= mMagnitude;
            y /= mMagnitude;
        }
    }

//addieren
    public void add(Vector2f v) {
        x += v.x;
        y += v.y;
    }

    public void add(float x_, float y_) {
        x += x_;
        y += y_;
    }

//subtrahieren
    public void sub(Vector2f v) {
        x -= v.x;
        y -= v.y;
    }

    public void sub(float x_, float y_) {
        x -= x_;
        y -= y_;
    }

//multiplizieren mit einem skalar
    public void mult(float s_) {
        x *= s_;
        y *= s_;
    }

//dividieren
    public void div(float d_) {
        x /= d_;
        y /= d_;
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
