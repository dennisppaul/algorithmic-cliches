package de.hfkbremen.algorithmiccliches.packing;


import mathematik.Vector3f;

import processing.core.PApplet;
import teilchen.util.Packing;
import teilchen.util.Packing.PackingEntity;

import java.util.Vector;


/**
 * http://en.wikipedia.org/wiki/Circle_packing_theorem
 */
public class SketchSpherePackingAroundCenter
        extends PApplet {

    private Vector<PackingEntity> mEntities;

    private final Vector3f mCenter = new Vector3f();

    public void setup() {
        size(1024, 768);
        smooth();
        mEntities = createRandomEntites(50);
        mCenter.set(width / 2, height / 2, 0);
    }

    public void draw() {
        background(255);

        stroke(0);
        noFill();
        for (int i = 0; i < mEntities.size(); i++) {
            PackingEntity mEntity = mEntities.get(i);
            ellipse(mEntity.position().x, mEntity.position().y,
                    mEntity.radius() * 2, mEntity.radius() * 2);

        }

        final int ITERATIONS = 50;
        for (int i = 1; i < ITERATIONS; i++) {
            attachToMouse();
            Packing.pack(mEntities, mCenter, 1.0f / (float) frameRate * 0.0251f);
        }
    }

    private Vector<PackingEntity> createRandomEntites(int pNumberOfShapes) {
        Vector<PackingEntity> mRandomEntities = new Vector<PackingEntity>();
        for (int i = 0; i < pNumberOfShapes; i++) {
            PackingEntity c = new PackingEntity();
            c.position().set(random(width), random(height), 0);
            c.radius(random(pNumberOfShapes) + 10);
            mRandomEntities.add(c);
        }
        return mRandomEntities;
    }

    public void mousePressed() {
        mCenter.set(mouseX, mouseY, 0);
    }

    public void keyPressed() {
        mEntities = createRandomEntites(50);
    }

    private boolean contains(PackingEntity c, Vector3f pPosition) {
        float d = c.position().distance(pPosition);
        return d <= c.radius();
    }

    private void attachToMouse() {
        for (int j = 0; j < mEntities.size(); j++) {
            PackingEntity c = mEntities.get(j);
            if (contains(c, new Vector3f(mouseX, mouseY))) {
                c.position().set(mouseX, mouseY, 0);
            }
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchSpherePackingAroundCenter.class.getName()});
    }
}
