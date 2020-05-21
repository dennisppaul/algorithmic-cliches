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


final ArrayList<LaserLine2> mLaserLines = new ArrayList();
void settings() {
    size(1024, 768, P3D);
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
        l.fade_width = sin(radians(frameCount * 4 + i * (360.0f / mLaserLines.size()))) * 5;
        l.fade_width += 10;
    }
}
void mousePressed() {
    LaserLine2 l = new LaserLine2();
    l.core_color = color(255, 0, 0);
    l.inner_fade_color = color(255, 0, 0, 127);
    l.outer_fade_color = color(255, 0, 0, 0);
    if (mLaserLines.isEmpty()) {
        l.p0.set(width / 2.0f, height / 2.0f);
    } else {
        l.p0.set(mLaserLines.get(mLaserLines.size() - 1).p1);
    }
    l.p1.set(mouseX, mouseY);
    mLaserLines.add(l);
}
