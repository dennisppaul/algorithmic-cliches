package de.hfkbremen.algorithmiccliches.examples;

import processing.core.PApplet;
import processing.core.PVector;
import teilchen.util.Packing;

import java.util.ArrayList;

public class SketchPackingSpheresAroundCenter extends PApplet {

    private final PVector mCenter = new PVector();
    private ArrayList<Packing.PackingEntity> mEntities;

    public void settings() {
        size(1024, 768, P3D);
    }

    public void setup() {
        mEntities = createRandomEntities(50);
        mCenter.set(width / 2.0f, height / 2.0f, 0);
    }

    public void draw() {
        background(255);

        stroke(0);
        noFill();
        for (Packing.PackingEntity mEntity : mEntities) {
            ellipse(mEntity.position().x, mEntity.position().y, mEntity.radius() * 2, mEntity.radius() * 2);
        }

        final int ITERATIONS = 50;
        for (int i = 1; i < ITERATIONS; i++) {
            attachToMouse();
            Packing.pack(mEntities, mCenter, 1.0f / frameRate * 0.0251f);
        }
    }

    public void mousePressed() {
        mCenter.set(mouseX, mouseY, 0);
    }

    public void keyPressed() {
        mEntities = createRandomEntities(50);
    }

    private ArrayList<Packing.PackingEntity> createRandomEntities(int pNumberOfShapes) {
        ArrayList<Packing.PackingEntity> mRandomEntities = new ArrayList<>();
        for (int i = 0; i < pNumberOfShapes; i++) {
            Packing.PackingEntity c = new Packing.PackingEntity();
            c.position().set(random(width), random(height), 0);
            c.radius(random(pNumberOfShapes) + 10);
            mRandomEntities.add(c);
        }
        return mRandomEntities;
    }

    private boolean contains(Packing.PackingEntity c, PVector pPosition) {
        float d = PVector.dist(c.position(), pPosition);
        return d <= c.radius();
    }

    private void attachToMouse() {
        for (Packing.PackingEntity c : mEntities) {
            if (contains(c, new PVector(mouseX, mouseY))) {
                c.position().set(mouseX, mouseY, 0);
            }
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchPackingSpheresAroundCenter.class.getName()});
    }
}
