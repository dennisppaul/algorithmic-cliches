import oscP5.*;
import netP5.*;
import teilchen.util.*;
import teilchen.util.Packing;
import teilchen.util.Packing.PackingEntity;
import teilchen.util.Util;

import java.util.ArrayList;

/**
 * http://en.wikipedia.org/wiki/Circle_packing_theorem
 */
final PVector mCenter = new PVector();
ArrayList<PackingEntity> mEntities;

void settings() {
    size(1024, 768, P3D);
}

void setup() {
    smooth();
    mEntities = createRandomEntites(50);
    mCenter.set(width / 2, height / 2, 0);
}

void draw() {
    background(255);

    stroke(0);
    noFill();
    for (PackingEntity mEntity : mEntities) {
        ellipse(mEntity.position().x, mEntity.position().y, mEntity.radius() * 2, mEntity.radius() * 2);
    }

    final int ITERATIONS = 50;
    for (int i = 1; i < ITERATIONS; i++) {
        attachToMouse();
        Packing.pack(mEntities, mCenter, 1.0f / (float) frameRate * 0.0251f);
    }
}

ArrayList<PackingEntity> createRandomEntites(int pNumberOfShapes) {
    ArrayList<PackingEntity> mRandomEntities = new ArrayList<>();
    for (int i = 0; i < pNumberOfShapes; i++) {
        PackingEntity c = new PackingEntity();
        c.position().set(random(width), random(height), 0);
        c.radius(random(pNumberOfShapes) + 10);
        mRandomEntities.add(c);
    }
    return mRandomEntities;
}

void mousePressed() {
    mCenter.set(mouseX, mouseY, 0);
}

void keyPressed() {
    mEntities = createRandomEntites(50);
}

boolean contains(PackingEntity c, PVector pPosition) {
    float d = Util.distance(c.position(), pPosition);
    return d <= c.radius();
}

void attachToMouse() {
    for (PackingEntity c : mEntities) {
        if (contains(c, new PVector(mouseX, mouseY))) {
            c.position().set(mouseX, mouseY, 0);
        }
    }
}
