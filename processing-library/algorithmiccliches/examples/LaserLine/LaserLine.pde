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


ArrayList<LaserLine2> mLaserLines = new ArrayList();
void settings() {
    size(640, 480, P3D);
}
void setup() {
}
void draw() {
    background(0);
    noStroke();
    for (LaserLine2 l : mLaserLines) {
        l.draw(g);
    }
    for (int i = 0; i < mLaserLines.size(); i++) {
        LaserLine2 l = mLaserLines.get(i);
        l.fade_width = sin(radians(frameCount * 4 + i * (360 / mLaserLines.size()))) * 5;
        l.fade_width += 10;
    }
}
void mousePressed() {
    LaserLine2 l = new LaserLine2();
    l.core_color = color(255, 0, 0);
    l.inner_fade_color = color(255, 0, 0, 127);
    l.outer_fade_color = color(255, 0, 0, 0);
    if (mLaserLines.isEmpty()) {
        l.p0.set(width / 2, height / 2);
    } else {
        l.p0.set(mLaserLines.get(mLaserLines.size() - 1).p1);
    }
    l.p1.set(mouseX, mouseY);
    mLaserLines.add(l);
}
