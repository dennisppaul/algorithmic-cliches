package de.mathiaslam.algorithmicclichees;


import processing.core.PApplet;


public class Agent {

    private float mRadius;

    private Vector2f mPosition;

    private Vector2f mVelocity;

    private Vector2f mAcceleration;

    private Vector2f mNormalizeAcceleration;

    public Agent(float r_) {
        mPosition = new Vector2f(320, 240);
        mVelocity = new Vector2f(0.2f, 0.2f);
        mAcceleration = new Vector2f(2, 2);
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
        if (mVelocity.magnitude() > 1) {
            mVelocity.normalize();
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
        mPosition.add(mVelocity);
        checkBound(p);
    }

    public void display(PApplet p) {
        p.fill(0, 0, 0, 180);
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
