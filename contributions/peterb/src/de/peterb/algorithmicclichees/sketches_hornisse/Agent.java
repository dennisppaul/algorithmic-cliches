package de.peterb.algorithmicclichees.sketches_hornisse;


import de.peterb.algorithmicclichees.Vector2f;
import processing.core.PApplet;


public class Agent {

    private float mRadius;

    private Vector2f mPosition;

    private Vector2f mVelocity;

    private Vector2f mAcceleration;

    public static int ArrivedAtMouse = 1;

    public static int NotArrivedAtMouse = 2;

    private float mMaxVelocity;

    private float myInterval;

    private boolean wait = false;

    private float count = 0;

    private int state = 0;

    public Agent() {
        mPosition = new Vector2f();
        mVelocity = new Vector2f();
        mAcceleration = new Vector2f();
        mMaxVelocity = 2.0f;
        mRadius = 1.0f;
    }

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

    public void setAccelerationToPoint(float x_, float y_) {
        mAcceleration.x = x_ - mPosition.x;
        mAcceleration.y = y_ - mPosition.y;
        mAcceleration.normalize();
    }

    public void setAcceleration(float x_, float y_) {
        mAcceleration.x = x_;
        mAcceleration.y = y_;
        mAcceleration.normalize();
    }


    /*DISPLAY*/
    public void display(PApplet p) {
//        myInterval += pDeltaTime;

        if (checkMouse(p) == NotArrivedAtMouse) {
        } else if (checkMouse(p) == ArrivedAtMouse) {
            // p.strokeWeight(mRadius);
        }


    }
    /*STATES*/

    private void wiggle(PApplet p) {
        //p.stroke(255, 0, 0);
        p.noStroke();
        p.fill(0, 30);
        p.ellipse(mPosition.x, mPosition.y, mRadius * 5, mRadius * 5);
        setAcceleration(p.random(-2, 2), p.random(-2, 2));
    }

    private void follow(PApplet p) {
        p.strokeWeight(mRadius);
        p.stroke(0);
        p.point(mPosition.x, mPosition.y);
        setAccelerationToPoint(p.random(0, p.width), p.random(0, p.height));
    }
    /*MOVE*/

    public void update(PApplet p, float pDeltaTime) {


        checkWall(p);
        state = checkMouse(p);
        if (state == 1) {
            wait = true;
        }
        if (wait) {
            if (myInterval <= 10.0f) {
                myInterval += pDeltaTime;
                wiggle(p);
            } else {
                myInterval = 0;
                wait = false;
                state = 2;
            }
        } else {
            follow(p);
        }

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
            setPosition(mPosition.x, 0);
        } else if (mPosition.y < 0) {
            setPosition(mPosition.x, p.height);
        }

    }

    private int checkMouse(PApplet p) {
        float mScaledRadius = (mRadius / mRadius) * 50;
        Vector2f mAB = new Vector2f(p.mouseX - mPosition.x, p.mouseY - mPosition.y);
        float mDistance = mAB.mag();

        if (mDistance < mScaledRadius) {
            return ArrivedAtMouse;
        } else {


            return NotArrivedAtMouse;
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

    private void setMyInterval(int x_) {
        myInterval = x_;
    }
}
