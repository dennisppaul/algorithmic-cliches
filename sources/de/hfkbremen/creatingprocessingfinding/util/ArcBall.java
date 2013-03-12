

package de.hfkbremen.creatingprocessingfinding.util;


import mathematik.Quaternion;
import mathematik.Vector3f;
import mathematik.Vector4f;

import processing.core.PApplet;


public class ArcBall {

    public static boolean DONT_REGISTER = false;

    private final PApplet _myParent;

    private final Vector3f _myCenter;

    private float _myRadius;

    private final Vector3f _myDownPosition;

    private final Vector3f _myDragPosition;

    private Quaternion _myCurrentQuaternion;

    private Quaternion _myDownQuaternion;

    private Quaternion _myDragQuaternion;

    private boolean _myLastActiveState = false;


    public ArcBall(PApplet parent) {
        this(parent.g.width / 2.0f,
             parent.g.height / 2.0f,
             -Math.min(parent.g.width / 2.0f, parent.g.height / 2.0f),
             Math.min(parent.g.width / 2.0f, parent.g.height / 2.0f),
             parent);
    }


    public ArcBall(float theCenterX,
                   float theCenterY,
                   float theCenterZ,
                   float theRadius,
                   PApplet theParent) {
        this(new Vector3f(theCenterX, theCenterY, theCenterZ), theRadius, theParent);
    }


    public ArcBall(final Vector3f theCenter,
                   final float theRadius,
                   final PApplet theParent) {

        _myParent = theParent;

        if (!DONT_REGISTER) {
            theParent.registerPre(this);
        }

        _myCenter = theCenter;
        _myRadius = theRadius;

        _myDownPosition = new Vector3f();
        _myDragPosition = new Vector3f();

        _myCurrentQuaternion = new Quaternion();
        _myDownQuaternion = new Quaternion();
        _myDragQuaternion = new Quaternion();
    }


    public void mousePressed(float theX, float theY) {
        _myDownPosition.set(mouse_to_sphere(theX, theY));
        _myDownQuaternion.set(_myCurrentQuaternion);
        _myDragQuaternion.reset();
    }


    public void mouseDragged(float theX, float theY) {
        _myDragPosition.set(mouse_to_sphere(theX, theY));
        _myDragQuaternion.set(_myDownPosition.dot(_myDragPosition), mathematik.Util.cross(_myDownPosition, _myDragPosition));
    }


    public void update(boolean theActiveState, float theX, float theY) {
        if (_myParent == null) {
            return;
        }

        if (theActiveState) {
            if (!_myLastActiveState) {
                mousePressed(theX, theY);
            }
            mouseDragged(theX, theY);
        } else {
            if (_myLastActiveState) {
                System.out.println("!");
            }
        }
        _myLastActiveState = theActiveState;


        /* apply transform */
        _myParent.translate(_myCenter.x, _myCenter.y, _myCenter.z);
        _myCurrentQuaternion.multiply(_myDragQuaternion, _myDownQuaternion);
        final Vector4f myRotationAxisAngle = _myCurrentQuaternion.getVectorAndAngle();
        if (!myRotationAxisAngle.isNaN()) {
            _myParent.rotate(myRotationAxisAngle.w,
                             myRotationAxisAngle.x,
                             myRotationAxisAngle.y,
                             myRotationAxisAngle.z);
        }
        _myParent.translate(-_myCenter.x, -_myCenter.y, -_myCenter.z);
    }


    private Vector3f mouse_to_sphere(float x, float y) {
        final Vector3f v = new Vector3f();
        v.x = (x - _myCenter.x) / _myRadius;
        v.y = (y - _myCenter.y) / _myRadius;

        float myLengthSquared = v.x * v.x + v.y * v.y;
        if (myLengthSquared > 1.0f) {
            v.normalize();
        } else {
            v.z = (float)Math.sqrt(1.0f - myLengthSquared);
        }
        return v;
    }


    /* processing callbacks */
    public void pre() {
        update(_myParent.mousePressed, _myParent.mouseX, _myParent.mouseY);
    }
}
