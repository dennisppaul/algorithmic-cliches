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


    extends PApplet {
boolean mRecord;
static final int ARRIVED_AT_POSITION = 1;
static final int NOT_ARRIVED_YET = 2;
void settings() {
    size(1024, 768, P3D);
}
void setup() {
    mRecord = false;
}
void draw() {
    if (mRecord) {
        PLYExporter.VERTEX_SCALE = 0.001f;
        beginRaw(PLYExporter.class.getName(), "output.ply");
    }
    draw(g);
    if (mRecord) {
        endRaw();
        mRecord = false;
    }
}
void keyPressed() {
    if (key == 'r') {
        mRecord = true;
    }
}
void draw(PGraphics pG) {
    pG.background(255);
    for (int i = 0; i < 10; i++) {
        pG.strokeWeight(i * 3);
        pG.stroke(random(255), random(255, 127), random(0, 127));
        pG.line(0, i * height / 10.0f, 0,
                width, i * height / 10.0f, 0);
    }
    pG.translate(width / 2, height / 2);
    pG.fill(0);
    pG.noStroke();
    pG.sphere(50);
    pG.noFill();
    final float mRadius = 400;
    for (int i = 0; i < 250; i++) {
        PVector v = new PVector();
        v.set(random(1), random(1), random(1));
        v.mult(mRadius);
        pG.strokeWeight(random(0.1f, 2.0f));
        pG.stroke(random(255), random(255, 127), random(0, 127));
        pG.line(0, 0, 0, v.x, v.y, v.z);
    }
}
