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

    public void set(Vector2f theVector) {
        x = theVector.x;
        y = theVector.y;
    }
};
