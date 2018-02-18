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
PGraphicsPDF pdf;
void settings() {
    size(1024, 768, P3D);
}
void setup() {
    pdf = (PGraphicsPDF) createGraphics(width, height, PDF, "export.pdf");
    beginRecord(pdf);
    background(255);
}
void draw() {
    background(255);
    draw(pdf);
    draw(g);
}
void draw(PGraphics pG) {
    pG.line(pmouseX, pmouseY, mouseX, mouseY);
}
void keyPressed() {
    if (key == 'q') {
        endRecord();
        exit();
    }
}
