package de.peterb.algorithmicclichees.sketches;


import processing.core.PApplet;
//import de.peterb.algorithmicclichees.sketches.Vector2f;


public class Agent {

    private float mRadius;

    private Vector2f mPosition;

    private Vector2f mVelocity;

    private Vector2f mAcceleration;

    public static int ArrivedAtMouse = 1;

    public static int NotArrivedAtMouse = 2;

//Konstruktor 1
    private float mMaxVelocity;

    private float myInterval;

    public Agent() {
        mPosition = new Vector2f();
        mVelocity = new Vector2f();
        mAcceleration = new Vector2f();
        mMaxVelocity = 4.0f;
        mRadius = 1.0f;
    }

//Konstruktor 2 1Vectors and a radius
    public Agent(Vector2f p_, float r_) {
        this();
        mPosition.set(p_);
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

    public void setAccelerationToMouse(PApplet p) {
        mAcceleration.x = p.mouseX - mPosition.x;
        mAcceleration.y = p.mouseY - mPosition.y;
        mAcceleration.normalize();
    }

    public void setAcceleration(float x_, float y_) {
        mAcceleration.x = x_;
        mAcceleration.y = y_;
        mAcceleration.normalize();
    }

    private float setmyInterval(float i_) {
        return myInterval;
    }

    /*DISPLAY*/
    public void display(PApplet p) {

        if (checkMouse(p) == NotArrivedAtMouse) {

            p.strokeWeight(mRadius);
            p.stroke(0);
            p.point(mPosition.x, mPosition.y);
            setAccelerationToMouse(p);

        } else if (checkMouse(p) == ArrivedAtMouse) {
            // p.strokeWeight(mRadius);
            p.stroke(255, 0, 0);
            p.ellipse(mPosition.x, mPosition.y, mRadius * 5, mRadius * 5);
            setAcceleration(p.random(-2, 2), p.random(-2, 2));


        }

    }

    /*MOVE*/
    public void update(PApplet p, float pDeltaTime) {

        myInterval += pDeltaTime;

        checkWall(p);
        checkMouse(p);

        mVelocity.add(mAcceleration);
        mPosition.add(mVelocity);
        constrainVelocity();
    }

    private void checkWall(PApplet p) {
        if (mPosition.x > p.width) {
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

    private int checkMouse(PApplet p) {
        float mScaledRadius = (mRadius / mRadius) * 20;
        Vector2f mAB = new Vector2f(p.mouseX - mPosition.x, p.mouseY - mPosition.y);
        float mDistance = mAB.mag();

        if (mDistance < mScaledRadius) {
            return ArrivedAtMouse;
        } else {

            if (myInterval > 2) {
//                myInterval = 0;
                return NotArrivedAtMouse;
            } else {
                return ArrivedAtMouse;
            }
        }
    }

    public Vector2f getPosition() {
        return mPosition;
    }

    public void setMaxAcceleration(float f) {
        mMaxVelocity = f;
    }

    private void constrainVelocity() {
        if (mVelocity.mag() > mMaxVelocity) {
            mVelocity.normalize();
            mVelocity.mult(mMaxVelocity);
        }

    }
}
