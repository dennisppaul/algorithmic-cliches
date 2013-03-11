

package de.hfkbremen.creatingprocessingfinding.octree;


import mathematik.Vector3f;

import processing.core.PApplet;

import java.util.Vector;


public class SketchSimpleOctreeDemo
        extends PApplet {

    // mOctree dimensions
    private float DIM = 100;

    private float DIM2 = DIM / 2;

    private float mSelectRadius = 20;

    private int NUM = 100;

    private boolean showOctree = true;

    private boolean useSphere = true;

    private float mRotationX = THIRD_PI;

    private float mRotationZ = 0.1f;

    VisibleOctree mOctree;

    Vector3f mPosition = new Vector3f();

    int numParticles = 1;


    public void setup() {
        size(1024, 768, OPENGL);
        textFont(createFont("Courier", 11));

        mOctree = new VisibleOctree(mathematik.Util.scale(new Vector3f(-1, -1, -1), DIM2), DIM);
        mOctree.addPoint(new Vector3f());
    }


    public void draw() {
        background(255);
        pushMatrix();

        translate(width / 2, height / 2, 0);

        /* rotate */
        if (mousePressed) {
            mRotationZ += (mouseX * 0.01f - mRotationZ) * 0.05f;
        } else {
            mPosition.x = -(width * 0.5f - mouseX) / (width / 2) * DIM2;
            mPosition.y = -(height * 0.5f - mouseY) / (height / 2) * DIM2;
        }
        rotateX(mRotationX);
        rotateZ(mRotationZ);
        scale(4);

        /* get points from octree */
        Vector<Vector3f> points;
        if (useSphere) {
            points = mOctree.getPointsWithinSphere(mPosition, mSelectRadius);
        } else {
            points = mOctree.getPointsWithinBox(mPosition, new Vector3f(mSelectRadius / 2, mSelectRadius / 2, mSelectRadius / 2));
        }

        /* draw points */
        int mNumberOfPointsSelected = 0;
        stroke(0, 127, 255, 127);
        noFill();
        if (points != null) {
            mNumberOfPointsSelected = points.size();
            for (Vector3f p : points) {
                drawCross(p, 1.0f);
            }
        }

        /* draw octree */
        if (showOctree) {
            stroke(0, 4);
            noFill();
            mOctree.draw();
        }

        /* draw crosshair */
        stroke(255, 0, 0, 63);
        noFill();
        beginShape(LINES);
        vertex(mPosition.x, -DIM2, 0);
        vertex(mPosition.x, DIM2, 0);
        vertex(-DIM2, mPosition.y, 0);
        vertex(DIM2, mPosition.y, 0);
        endShape();

        /* draw selection sphere */
        stroke(255, 0, 0, 63);
        noFill();
        translate(mPosition.x, mPosition.y, 0);
        sphereDetail(8);
        sphere(mSelectRadius);
        popMatrix();

        fill(0);
        noStroke();
        text("POINTS   : " + numParticles, 10, 12);
        text("SELECTED : " + mNumberOfPointsSelected, 10, 24);
        text("FPS      : " + frameRate, 10, 36);
    }


    private void drawCross(Vector3f v, float pRadius) {
        line(v.x - pRadius, v.y, v.z, v.x + pRadius, v.y, v.z);
        line(v.x, v.y - pRadius, v.z, v.x, v.y + pRadius, v.z);
        line(v.x, v.y, v.z - pRadius, v.x, v.y, v.z + pRadius);
    }


    public void keyPressed() {
        if (key == ' ') {
            for (int i = 0; i < NUM; i++) {
                final Vector3f v = new Vector3f();
                v.x = random(-DIM2, DIM2);
                v.y = random(-DIM2, DIM2);
                v.z = random(-DIM2, DIM2);
                mOctree.addPoint(v);
            }
            numParticles += NUM;
        } else if (key == 's') {
            useSphere = !useSphere;
        } else if (key == 'o') {
            showOctree = !showOctree;
        } else if (key == '-') {
            mSelectRadius = max(mSelectRadius - 1, 2);
        } else if (key == '=') {
            mSelectRadius = min(mSelectRadius + 1, DIM);
        } else if (key == 'c') {
            mOctree.removeAll();
            numParticles = 0;
        }
    }


    class VisibleOctree
            extends Octree {

        VisibleOctree(Vector3f o, float d) {
            super(o, d);
        }


        void draw() {
            drawNode(this);
        }


        void drawNode(Octree pOctree) {
            if (pOctree.getNumChildren() > 0) {
                pushMatrix();
                translate(pOctree.origin().x, pOctree.origin().y, pOctree.origin().z);
                box(pOctree.getNodeSize());
                popMatrix();
                Octree[] childNodes = pOctree.getChildren();
                for (int i = 0; i < Octree.NUMBER_OF_CHILDREN; i++) {
                    if (childNodes[i] != null) {
                        drawNode(childNodes[i]);
                    }
                }
            }
        }
    }


    public static void main(String[] args) {
        PApplet.main(new String[] {SketchSimpleOctreeDemo.class.getName()});
    }
}
