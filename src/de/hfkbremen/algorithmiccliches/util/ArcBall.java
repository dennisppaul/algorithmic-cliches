package de.hfkbremen.algorithmiccliches.util;

import mathematik.Quaternion;
import mathematik.Vector3f;
import mathematik.Vector4f;

import processing.core.PApplet;

/**
 * http://de.wikipedia.org/wiki/Rolling-Ball-Rotation
 */
public class ArcBall {

    public static ArcBall setupRotateAroundCenter(PApplet pApplet) {
        return new ArcBall(pApplet.width / 2, pApplet.height / 2, 0,
                           Math.min(pApplet.g.width / 2.0f, pApplet.g.height / 2.0f),
                           pApplet, false);
    }
    private final PApplet mParent;

    private final Vector3f mCenter;

    private float mRadius;

    private final Vector3f mDownPosition;

    private final Vector3f mDragPosition;

    private Quaternion mCurrentQuaternion;

    private Quaternion mDownQuaternion;

    private Quaternion mDragQuaternion;

    private boolean mLastActiveState = false;

    public ArcBall(PApplet pApplet, boolean pDONT_REGISTER) {
        this(pApplet.g.width / 2.0f,
             pApplet.g.height / 2.0f,
             -Math.min(pApplet.g.width / 2.0f, pApplet.g.height / 2.0f),
             Math.min(pApplet.g.width / 2.0f, pApplet.g.height / 2.0f),
             pApplet, pDONT_REGISTER);
    }

    public ArcBall(PApplet parent) {
        this(parent, false);
    }

    public ArcBall(float theCenterX,
                   float theCenterY,
                   float theCenterZ,
                   float theRadius,
                   PApplet pApplet,
                   boolean pDONT_REGISTER) {
        this(new Vector3f(theCenterX, theCenterY, theCenterZ), theRadius, pApplet, pDONT_REGISTER);
    }

    public ArcBall(final Vector3f theCenter,
                   final float theRadius,
                   final PApplet pApplet,
                   boolean pDONT_REGISTER) {

        mParent = pApplet;

        if (!pDONT_REGISTER) {
//            pApplet.registerPre(this);
            pApplet.registerMethod("pre", this); // new in processing 3.0 @test
        }

        mCenter = theCenter;
        mRadius = theRadius;

        mDownPosition = new Vector3f();
        mDragPosition = new Vector3f();

        mCurrentQuaternion = new Quaternion();
        mDownQuaternion = new Quaternion();
        mDragQuaternion = new Quaternion();
    }

    public void mousePressed(float theX, float theY) {
        mDownPosition.set(mouse_to_sphere(theX, theY));
        mDownQuaternion.set(mCurrentQuaternion);
        mDragQuaternion.reset();
    }

    public void mouseDragged(float theX, float theY) {
        mDragPosition.set(mouse_to_sphere(theX, theY));
        mDragQuaternion.set(mDownPosition.dot(mDragPosition), mathematik.Util.cross(mDownPosition, mDragPosition));
    }

    public void update() {
        update(mParent.mousePressed, mParent.mouseX, mParent.mouseY);
    }

    public void update(boolean theActiveState, float theX, float theY) {
        if (mParent == null) {
            return;
        }

        if (theActiveState) {
            if (!mLastActiveState) {
                mousePressed(theX, theY);
            }
            mouseDragged(theX, theY);
        } else {
            if (mLastActiveState) {
            }
        }
        mLastActiveState = theActiveState;


        /* apply transform */
        mParent.translate(mCenter.x, mCenter.y, mCenter.z);
        mCurrentQuaternion.multiply(mDragQuaternion, mDownQuaternion);
        final Vector4f myRotationAxisAngle = mCurrentQuaternion.getVectorAndAngle();
        if (!myRotationAxisAngle.isNaN()) {
            mParent.rotate(myRotationAxisAngle.w,
                           myRotationAxisAngle.x,
                           myRotationAxisAngle.y,
                           myRotationAxisAngle.z);
        }
        mParent.translate(-mCenter.x, -mCenter.y, -mCenter.z);
    }

    public void radius(float pRadius) {
        mRadius = pRadius;
    }

    public Vector3f center() {
        return mCenter;
    }

    private Vector3f mouse_to_sphere(float x, float y) {
        final Vector3f v = new Vector3f();
        v.x = (x - mCenter.x) / mRadius;
        v.y = (y - mCenter.y) / mRadius;

        float myLengthSquared = v.x * v.x + v.y * v.y;
        if (myLengthSquared > 1.0f) {
            v.normalize();
        } else {
            v.z = (float) Math.sqrt(1.0f - myLengthSquared);
        }
        return v;
    }


    /* processing callbacks */
    public void pre() {
        update();
    }
}
