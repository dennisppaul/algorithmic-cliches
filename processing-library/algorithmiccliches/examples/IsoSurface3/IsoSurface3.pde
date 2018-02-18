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


MetaballManager mMetaballManager;
int mCurrentCircle = 0;
ArcBall mArcBall;
void settings() {
    size(1024, 768, P3D);
}
void setup() {
    textFont(createFont("Courier", 11));
    mArcBall = new ArcBall(width / 2, height / 2, 0, 400.0f, this, true);
    mMetaballManager = new MetaballManager();
    mMetaballManager.dimension.set(width, height, height);
    mMetaballManager.resolution.set(30, 30, 30);
    mMetaballManager.position.set(width / -2, height / -2, height / -2);
    mMetaballManager.add(new Metaball(new PVector(0, 0, 0), 1, 100));
}
void draw() {
    background(255);
    directionalLight(126, 126, 126, 0, 0, -1);
    ambientLight(102, 102, 102);
    /* draw extra info */
    fill(0);
    noStroke();
    text("ISOVALUE : " + mMetaballManager.isovalue(), 10, 12);
    text("SELECTED : " + mCurrentCircle, 10, 24);
    text("FPS      : " + (int) frameRate, 10, 36);
    /* draw isosurface */
    mArcBall.update();
    if (!mMetaballManager.metaballs().isEmpty()) {
        mMetaballManager.metaballs().get(mCurrentCircle).position.x = mouseX - width / 2;
        mMetaballManager.metaballs().get(mCurrentCircle).position.y = mouseY - height / 2;
    }
    final Vector<PVector> myData = mMetaballManager.createSurface();
    /* draw metaballs */
    translate(width / 2, height / 2);
    fill(255, 127, 0);
    noStroke();
    beginShape(TRIANGLES);
    for (int i = 0; i < myData.size(); i++) {
        vertex(myData.get(i).x, myData.get(i).y, myData.get(i).z);
    }
    endShape();
}
void keyPressed() {
    switch (key) {
        case '+':
            mMetaballManager.isovalue(mMetaballManager.isovalue() + 0.01f);
            break;
        case '-':
            mMetaballManager.isovalue(mMetaballManager.isovalue() - 0.01f);
            break;
        case ' ':
            mCurrentCircle++;
            mCurrentCircle %= mMetaballManager.metaballs().size();
            break;
        case 'c':
            mMetaballManager.add(new Metaball(new PVector(mouseX - width / 2,
                                                          mouseY - height / 2,
                                                          random(-100, 100)), random(1, 2), random(50, 150)));
            break;
    }
}
