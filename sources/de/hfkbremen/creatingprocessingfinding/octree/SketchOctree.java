package de.hfkbremen.creatingprocessingfinding.octree;


import mathematik.Vector3f;

import processing.core.PApplet;

import java.util.Vector;


public class SketchOctree
        extends PApplet {

    private final int NUMBER_OF_PARTICLES_ADDED = 10000;

    private MVisibleOctree mOctree;

    private float mOctreeSize = 100;

    private float mSelectRadius = 20;

    private boolean showOctree = true;

    private boolean useSphere = true;

    private float mRotationZ = 0.1f;

    private Vector3f mPosition = new Vector3f();

    private int numParticles = 1;

    public void setup() {
        size(1024, 768, OPENGL);
        textFont(createFont("Courier", 11));

        mOctree = new MVisibleOctree(new Vector3f(-mOctreeSize / 2, -mOctreeSize / 2, -mOctreeSize / 2), mOctreeSize);
        mOctree.add(new MOctreeEntity());
    }

    public void draw() {
        background(255);
        pushMatrix();

        translate(width / 2, height / 2, 0);

        /* rotate */
        if (mousePressed) {
            mRotationZ += (mouseX * 0.01f - mRotationZ) * 0.05f;
        } else {
            mPosition.x = -(width * 0.5f - mouseX) / (width / 2) * mOctreeSize / 2;
            mPosition.y = -(height * 0.5f - mouseY) / (height / 2) * mOctreeSize / 2;
        }
        rotateX(THIRD_PI);
        rotateZ(mRotationZ);
        scale(4);

        /* get entities from octree */
        Vector<OctreeEntity> mEntities;
        if (useSphere) {
            mEntities = mOctree.getEntitesWithinSphere(mPosition, mSelectRadius);
        } else {
            mEntities = mOctree.getEntitiesWithinBox(mPosition, new Vector3f(mSelectRadius / 2,
                    mSelectRadius / 2,
                    mSelectRadius / 2));
        }

        /* draw entities */
        int mNumberOfPointsSelected = 0;
        stroke(0, 127, 255, 127);
        noFill();
        if (mEntities != null) {
            mNumberOfPointsSelected = mEntities.size();
            for (OctreeEntity mEntity : mEntities) {
                MOctreeEntity m = (MOctreeEntity) mEntity;
                stroke(m.color);
                drawCross(mEntity.position(), 1.0f);
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
        vertex(mPosition.x, -mOctreeSize / 2, 0);
        vertex(mPosition.x, mOctreeSize / 2, 0);
        vertex(-mOctreeSize / 2, mPosition.y, 0);
        vertex(mOctreeSize / 2, mPosition.y, 0);
        endShape();

        /* draw selection sphere */
        stroke(255, 0, 0, 63);
        noFill();
        translate(mPosition.x, mPosition.y, 0);
        sphereDetail(8);
        sphere(mSelectRadius);
        popMatrix();

        /* draw info */
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
            for (int i = 0; i < NUMBER_OF_PARTICLES_ADDED; i++) {
                MOctreeEntity mEntity = new MOctreeEntity();
                mEntity.position().x = random(-mOctreeSize / 2, mOctreeSize / 2);
                mEntity.position().y = random(-mOctreeSize / 2, mOctreeSize / 2);
                mEntity.position().z = random(-mOctreeSize / 2, mOctreeSize / 2);
                mOctree.add(mEntity);
            }
            numParticles += NUMBER_OF_PARTICLES_ADDED;
        } else if (key == 's') {
            useSphere = !useSphere;
        } else if (key == 'o') {
            showOctree = !showOctree;
        } else if (key == '-') {
            mSelectRadius = max(mSelectRadius - 1, 2);
        } else if (key == '+') {
            mSelectRadius = min(mSelectRadius + 1, mOctreeSize);
        } else if (key == 'c') {
            mOctree.auto_reduction(true);
            mOctree.removeAll();
            mOctree.auto_reduction(false);
            numParticles = 0;
        }
    }

    class MOctreeEntity
            implements OctreeEntity {

        Vector3f position = new Vector3f();

        int color = color(0, 127, random(0, 255), 127);

        public Vector3f position() {
            return position;
        }
    }

    class MVisibleOctree
            extends Octree {

        MVisibleOctree(Vector3f o, float d) {
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
        PApplet.main(new String[]{SketchOctree.class.getName()});
    }
}
