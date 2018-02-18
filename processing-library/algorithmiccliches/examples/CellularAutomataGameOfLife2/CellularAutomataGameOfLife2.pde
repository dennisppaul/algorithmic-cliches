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


CellularAutomaton2 mCA;
void settings() {
    size(1024, 768, P3D);
}
void setup() {
    rectMode(CENTER);
    textFont(createFont("Courier", 11));
    mCA = new CellularAutomaton2(1024 / 8, 768 / 8);
    mCA.randomizeCells(0.6f);
}
void draw() {
    lights();
    background(255);
    /* evaluate cells */
    int mBirth = 3;
    int mMinNeighbors = 2;
    int mMaxNeighbors = 3;
    mCA.update(mBirth, mMinNeighbors, mMaxNeighbors); // B3/S23
    /* draw grid */
    fill(127);
    noStroke();
    pushMatrix();
    translate(width / 2, height / 2);
    scale(6);
    mCA.draw(g);
    popMatrix();
    /* draw info */
    fill(0);
    noStroke();
    text("RULE     : " + "B" + mBirth + "/S" + mMinNeighbors + "" + mMaxNeighbors, 10, 12);
    text("FPS      : " + frameRate, 10, 24);
}
void keyPressed() {
    mCA.randomizeCells(0.6f);
}
