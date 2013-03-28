package de.peterb.algorithmicclichees.sketches_film;


import processing.core.PApplet;
//import de.peterb.algorithmicclichees.sketches.Vector2f;


public class Agent {

    private float mRadius;

    private Vector2f mPosition;

    private Vector2f mVelocity;

    private Vector2f mAcceleration;
//Konstruktor 1

    public Agent() {
    }
//Konstruktor 2 2Vectors and a radius

    public Agent(Vector2f p_, Vector2f v_, Vector2f a_, float r_) {

        mPosition = p_;
        mVelocity = v_;
        mAcceleration = a_;
        mRadius = r_;

    }
//Konstruktor 2 1Vectors and a radius

    public Agent(Vector2f p_, float r_) {

        mPosition = p_;
        mVelocity = new Vector2f();
        mAcceleration = new Vector2f();
        mRadius = r_;

    }

//Konstruktor 3 only radius
    public Agent(float r_) {
        mPosition = new Vector2f(640, 400);
        mVelocity = new Vector2f(0, 0);
        mRadius = r_;
    }

    /* SETTER */
    public void setVelocity(Vector2f v_) {
        mVelocity = v_;
    }

    public void setVelocity(float x_, float y_) {
        mVelocity.x = x_;
        mVelocity.y = y_;
    }

    public void setPosition(Vector2f p_) {
        mPosition.x = p_.x;
        mPosition.y = p_.y;
    }

    public void setPosition(float x_, float y_) {
        mPosition.x = x_;
        mPosition.y = y_;
    }

    public void setRadius(float r_) {
        mRadius = r_;
    }

    public void setAcceleration(float x_, float y_) {
        mAcceleration.x = x_ - mPosition.x;
        mAcceleration.y = y_ - mPosition.y;
        mAcceleration.normalize();

    }

    /*DISPLAY*/
    public void display(PApplet p) {

        p.stroke(225, 0, 0);
        p.strokeWeight(1);
//        p.line(mPosition.x, mPosition.y, mVelocity.x * 5 + mPosition.x, mVelocity.y * 5 + mPosition.y);
//        p.stroke(0);
        p.fill(0, 20);
        p.noStroke();
        p.ellipse(mPosition.x, mPosition.y, mRadius * 2, mRadius * 2);

    }

    public void move(PApplet p) {

        checkWall(p);

        mVelocity.add(mAcceleration);
        mPosition.add(mVelocity);
    }

    private void checkWall(PApplet p) {
        if (mPosition.x > p.width) {
//            mVelocity.x *= -1;
            setPosition(0, mPosition.y);
        } else if (mPosition.x < 0) {
            setPosition(p.width, mPosition.y);
        }

        if (mPosition.y > p.height) {
//            mVelocity.y *= -1;
            setPosition(mPosition.x, 0);
        } else if (mPosition.y < 0) {
            setPosition(mPosition.x, p.height);
        }
    }

    public void agentArray(float x_) {
    }
}
