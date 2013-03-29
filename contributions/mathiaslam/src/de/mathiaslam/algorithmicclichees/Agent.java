package de.mathiaslam.algorithmicclichees;


import java.util.Vector;
import processing.core.PApplet;
import static processing.core.PConstants.POINTS;


public class Agent {

    private float mRadius;

    private Vector2f mPosition;

    private Vector2f mVelocity;

    private Vector2f mAcceleration;

    private Vector2f mNormalizeAcceleration;

private Vector<Vector2f> mVertices;
    private Vector2f start;

    private int infected;

    private Vector2f diff;

    private static final float MIN_DISTANCE = 10.0f;

    private static final int AGENT_INFECTED = 0;

    private static final int AGENT_NOT_INFECTED = 1;

    public Agent(PApplet p, float r_) {
        mPosition = new Vector2f(p.width / 2, p.height / 2);
        mVelocity = new Vector2f(1f, 1f);
        mAcceleration = new Vector2f(2f, 2f);
        start = new Vector2f(160f, 240f);
        diff = new Vector2f(0, 0);
        mVertices = new Vector<Vector2f>();
        infected = 0;
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

    public void getNeighbours(PApplet p ,Vector<Agent> agents) {
        float desiredseparation = 50.0f;

        int count = 0;
        for (Agent other : agents) {
         
            float d = Vector2f.distance(mPosition, other.mPosition);
            if ((d > 0) && (d < desiredseparation)) {
              

                if (infected == AGENT_INFECTED) {
                    infected = AGENT_NOT_INFECTED;
                   // mAcceleration.sub(other.mPosition);
                    
                     p.stroke(0,100,255,128);
               p.strokeWeight(0.01f);
             // p.line(mPosition.x,mPosition.y,other.mPosition.x,other.mPosition.y);

                } else if (infected == AGENT_NOT_INFECTED) {
                    infected = AGENT_INFECTED;
                  
                    // mPosition.sub(mVelocity);
                     p.stroke(128,128,128,128);
               p.strokeWeight(0.01f);
            //  p.line(mPosition.x,mPosition.y,other.mPosition.x,other.mPosition.y);
                }
            }
        }
    }


    public void display(PApplet p) {
        if (infected == 0) {
            p.fill(0, 0, 0, 127);
            p.noStroke();
            p.ellipse(mPosition.x, mPosition.y, mRadius, mRadius);
        } else if (infected == 1) {
            p.fill(0, 100, 255, 127);
            p.noStroke();
            p.ellipse(mPosition.x, mPosition.y, mRadius, mRadius);

        }
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