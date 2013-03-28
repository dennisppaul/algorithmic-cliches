package de.mathiaslam.algorithmicclichees;


import processing.core.PApplet;


public class Agent {

    private float mRadius;

    private Vector2f mPosition;

    private Vector2f mVelocity;

    private Vector2f mAcceleration;

    private Vector2f mNormalizeAcceleration;

    private static final float MIN_DISTANCE = 10.0f;

    public Agent(float r_) {
        mPosition = new Vector2f(320f, 240f);
        mVelocity = new Vector2f(1f, 1f);
        mAcceleration = new Vector2f(2f, 2f);
        mRadius = r_;
    }

    public void setPosition(Vector2f p_) {
        mPosition.x = p_.x;
        mPosition.y = p_.y;
    }

    public void setVelocity(Vector2f v_) {
        mVelocity = v_;
    }

    public void constrainVelocity() {
        if (mVelocity.magnitude() > 5) {
            mVelocity.normalize();
            mVelocity.multiply(5);
        }
    }

    public void setAcceleration(Vector2f a_) {
        mAcceleration = a_;
        mAcceleration.normalize();
    }

    public void setRadius(float r_) {
        mRadius = r_;
    }

    public void move(PApplet p) {
        mVelocity.add(mAcceleration);
        constrainVelocity();
        mPosition.add(mVelocity);
        checkBound(p);
    }

    public void follow(float x_, float y_) {
        Vector2f mMouse = new Vector2f(x_, y_);
        mMouse.sub(mPosition);
        mMouse.normalize();
        mAcceleration.set(mMouse);
    }

    public void display(PApplet p) {
        p.fill(0, 0, 0, 127);
        p.noStroke();
        p.ellipse(mPosition.x, mPosition.y, mRadius, mRadius);
    }

    private void checkBound(PApplet p) {
        if (mPosition.y > p.height) {
            mPosition.y = 0;
        } else if (mPosition.y < 0) {
            mPosition.y = p.height;
        }
        if (mPosition.x < 0) {
            mPosition.x = p.width;
        } else if (mPosition.x > p.width) {
            mPosition.x = 0;
        }
    }
;
}
