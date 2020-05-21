import de.hfkbremen.algorithmiccliches.*; 
import de.hfkbremen.algorithmiccliches.agents.*; 
import de.hfkbremen.algorithmiccliches.cellularautomata.*; 
import de.hfkbremen.algorithmiccliches.convexhull.*; 
import de.hfkbremen.algorithmiccliches.delaunaytriangulation2.*; 
import de.hfkbremen.algorithmiccliches.fluiddynamics.*; 
import de.hfkbremen.algorithmiccliches.isosurface.*; 
import de.hfkbremen.algorithmiccliches.laserline.*; 
import de.hfkbremen.algorithmiccliches.lindenmayersystems.*; 
import de.hfkbremen.algorithmiccliches.octree.*; 
import de.hfkbremen.algorithmiccliches.util.*; 
import de.hfkbremen.algorithmiccliches.voronoidiagram.*; 
import teilchen.*; 
import teilchen.behavior.*; 
import teilchen.constraint.*; 
import teilchen.cubicle.*; 
import teilchen.integration.*; 
import teilchen.util.*; 
import teilchen.force.*; 
import teilchen.force.flowfield.*; 
import teilchen.force.vectorfield.*; 
import de.hfkbremen.gewebe.*; 
import ddf.minim.*; 
import ddf.minim.analysis.*; 
import quickhull3d.*; 


/*
 * http://en.wikipedia.org/wiki/Circle_packing_theorem
 */
final PVector mCenter = new PVector();
ArrayList<Packing.PackingEntity> mEntities;
void settings() {
    size(1024, 768, P3D);
}
void setup() {
    mEntities = createRandomEntities(50);
    mCenter.set(width / 2.0f, height / 2.0f, 0);
}
void draw() {
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
void mousePressed() {
    mCenter.set(mouseX, mouseY, 0);
}
void keyPressed() {
    mEntities = createRandomEntities(50);
}
ArrayList<Packing.PackingEntity> createRandomEntities(int pNumberOfShapes) {
    ArrayList<Packing.PackingEntity> mRandomEntities = new ArrayList();
    for (int i = 0; i < pNumberOfShapes; i++) {
        Packing.PackingEntity c = new Packing.PackingEntity();
        c.position().set(random(width), random(height), 0);
        c.radius(random(pNumberOfShapes) + 10);
        mRandomEntities.add(c);
    }
    return mRandomEntities;
}
boolean contains(Packing.PackingEntity c, PVector pPosition) {
    float d = PVector.dist(c.position(), pPosition);
    return d <= c.radius();
}
void attachToMouse() {
    for (Packing.PackingEntity c : mEntities) {
        if (contains(c, new PVector(mouseX, mouseY))) {
            c.position().set(mouseX, mouseY, 0);
        }
    }
}
