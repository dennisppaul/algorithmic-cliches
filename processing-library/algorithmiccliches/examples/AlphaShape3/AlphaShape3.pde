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


CGALAlphaShape3 cgal;
float[] mPoints3;
Mesh mMesh;
void settings() {
    size(1024, 768, P3D);
}
void setup() {
    cgal = new CGALAlphaShape3();
    final int NUMBER_OF_POINTS = 2000;
    mPoints3 = new float[NUMBER_OF_POINTS * 3];
    final float mRange = 2;
    for (int i = 0; i < NUMBER_OF_POINTS; i++) {
        PVector p = new PVector().set(random(-1, 1), random(-1, 1), random(-1, 1));
        p.normalize();
        p.mult(random(mRange * 0.75f, mRange));
        mPoints3[i * 3 + 0] = p.x;
        mPoints3[i * 3 + 1] = p.y;
        mPoints3[i * 3 + 2] = p.z;
    }
    cgal.compute_cgal_alpha_shape(mPoints3);
    computeAlphaShape(0.5f);
}
void draw() {
    background(255);
    directionalLight(126, 126, 126, 0, 0, -1);
    ambientLight(102, 102, 102);
    translate(width / 2, height / 2);
    scale(100);
    rotateX(frameCount * 0.01f);
    rotateY(frameCount * 0.003f);
    fill(0, 127, 255);
    noStroke();
    if (mMesh != null) {
        mMesh.draw(g);
    }
    strokeWeight(1f / 25f);
    stroke(255, 127, 0);
    noFill();
    beginShape(POINTS);
    for (int i = 0; i < mPoints3.length; i += 3) {
        vertex(mPoints3[i + 0], mPoints3[i + 1], mPoints3[i + 2]);
    }
    endShape();
}
void computeAlphaShape(float mAlpha) {
    mMesh = cgal.mesh(mAlpha);
}
void mouseMoved() {
    computeAlphaShape(mouseX / (float) width);
}
