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
void settings() {
    size(1024, 768, P3D);
}
void setup() {
    mRecord = false;
}
void draw() {
    if (mRecord) {
        beginRaw(RawDXF.class.getName(), "output.dxf");
    }
    background(255);
    noStroke();
    fill(255, 0, 0);
    translate(width / 2, height / 2);
    sphere(100);
    stroke(0);
    noFill();
    for (int i = 0; i < 100; i++) {
        line(0, 0, 0,
                random(width / -2, width / 2),
                random(height / -2, height / 2),
                random(height / -2, height / 2));
    }
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
