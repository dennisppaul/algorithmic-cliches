import de.hfkbremen.algorithmiccliches.*; 
import de.hfkbremen.algorithmiccliches.agents.*; 
import de.hfkbremen.algorithmiccliches.cellularautomata.*; 
import de.hfkbremen.algorithmiccliches.convexhull.*; 
import de.hfkbremen.algorithmiccliches.delaunaytriangulation2.*; 
import de.hfkbremen.algorithmiccliches.delaunaytriangulation2.VoronoiDiagram.Region; 
import de.hfkbremen.algorithmiccliches.exporting.*; 
import de.hfkbremen.algorithmiccliches.fluiddynamics.*; 
import de.hfkbremen.algorithmiccliches.isosurface.marchingcubes.*; 
import de.hfkbremen.algorithmiccliches.isosurface.marchingsquares.*; 
import de.hfkbremen.algorithmiccliches.laserline.*; 
import de.hfkbremen.algorithmiccliches.lindenmayersystems.*; 
import de.hfkbremen.algorithmiccliches.octree.*; 
import de.hfkbremen.algorithmiccliches.util.*; 
import de.hfkbremen.algorithmiccliches.util.ArcBall; 
import de.hfkbremen.algorithmiccliches.voronoidiagram.*; 
import oscP5.*; 
import netP5.*; 
import teilchen.*; 
import teilchen.constraint.*; 
import teilchen.force.*; 
import teilchen.behavior.*; 
import teilchen.cubicle.*; 
import teilchen.util.*; 
import teilchen.util.Vector3i; 
import teilchen.util.Util; 
import teilchen.util.Packing; 
import teilchen.util.Packing.PackingEntity; 
import de.hfkbremen.mesh.*; 
import java.util.*; 
import ddf.minim.*; 
import ddf.minim.analysis.*; 
import quickhull3d.*; 
import javax.swing.*; 


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
    ArrayList<PackingEntity> mRandomEntities = new ArrayList();
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
