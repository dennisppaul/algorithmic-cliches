package de.hfkbremen.algorithmiccliches.examples;

import de.hfkbremen.algorithmiccliches.octree.Octree;
import de.hfkbremen.algorithmiccliches.octree.OctreeEntity;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class SketchOctree3D extends PApplet {

    /*
     * http://en.wikipedia.org/wiki/Octree
     */

    private static final int NUMBER_OF_PARTICLES_ADDED = 10000;
    private static final float OCTREE_SIZE = 100;
    private final PVector mPosition = new PVector();
    private MVisibleOctree mOctree;
    private float mSelectRadius = 20;
    private boolean showOctree = true;
    private boolean useSphere = true;
    private float mRotationZ = 0.1f;
    private int numParticles = 1;

    public void settings() {
        size(1024, 768, P3D);
    }

    public void setup() {
        textFont(createFont("Courier", 11));

        mOctree = new MVisibleOctree(new PVector(-OCTREE_SIZE / 2, -OCTREE_SIZE / 2, -OCTREE_SIZE / 2), OCTREE_SIZE);
        mOctree.add(new MOctreeEntity());

        strokeWeight(0.25f);
    }

    public void draw() {
        background(255);
        pushMatrix();

        translate(width / 2.0f, height / 2.0f, 0);

        /* rotate */
        if (mousePressed) {
            mRotationZ += (mouseX * 0.01f - mRotationZ) * 0.05f;
        } else {
            mPosition.x = -(width * 0.5f - mouseX) / (width / 2.0f) * OCTREE_SIZE / 2;
            mPosition.y = -(height * 0.5f - mouseY) / (height / 2.0f) * OCTREE_SIZE / 2;
        }
        rotateX(THIRD_PI);
        rotateZ(mRotationZ);
        scale(4);

        /* get entities from octree */
        ArrayList<OctreeEntity> mEntities;
        if (useSphere) {
            mEntities = mOctree.getEntitesWithinSphere(mPosition, mSelectRadius);
        } else {
            mEntities = mOctree.getEntitiesWithinBox(mPosition, new PVector(mSelectRadius / 2,
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
                stroke(m.entity_color);
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
        vertex(mPosition.x, -OCTREE_SIZE / 2, 0);
        vertex(mPosition.x, OCTREE_SIZE / 2, 0);
        vertex(-OCTREE_SIZE / 2, mPosition.y, 0);
        vertex(OCTREE_SIZE / 2, mPosition.y, 0);
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

    public void keyPressed() {
        switch (key) {
            case ' ':
                for (int i = 0; i < NUMBER_OF_PARTICLES_ADDED; i++) {
                    MOctreeEntity mEntity = new MOctreeEntity();
                    mEntity.position().x = random(-OCTREE_SIZE / 2, OCTREE_SIZE / 2);
                    mEntity.position().y = random(-OCTREE_SIZE / 2, OCTREE_SIZE / 2);
                    mEntity.position().z = random(-OCTREE_SIZE / 2, OCTREE_SIZE / 2);
                    mOctree.add(mEntity);
                }
                numParticles += NUMBER_OF_PARTICLES_ADDED;
                break;
            case 's':
                useSphere = !useSphere;
                break;
            case 'o':
                showOctree = !showOctree;
                break;
            case '-':
                mSelectRadius = max(mSelectRadius - 1, 2);
                break;
            case '+':
                mSelectRadius = min(mSelectRadius + 1, OCTREE_SIZE);
                break;
            case 'c':
                mOctree.auto_reduction(true);
                mOctree.removeAll();
                mOctree.auto_reduction(false);
                numParticles = 0;
                break;
            default:
                break;
        }
    }

    private void drawCross(PVector v, float pRadius) {
        line(v.x - pRadius, v.y, v.z, v.x + pRadius, v.y, v.z);
        line(v.x, v.y - pRadius, v.z, v.x, v.y + pRadius, v.z);
        line(v.x, v.y, v.z - pRadius, v.x, v.y, v.z + pRadius);
    }

    class MOctreeEntity
            implements OctreeEntity {

        PVector position = new PVector();

        int entity_color = color(0, 127, random(0, 255), 127);

        public PVector position() {
            return position;
        }
    }

    class MVisibleOctree
            extends Octree {

        MVisibleOctree(PVector o, float d) {
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
        PApplet.main(new String[]{SketchOctree3D.class.getName()});
    }
}
