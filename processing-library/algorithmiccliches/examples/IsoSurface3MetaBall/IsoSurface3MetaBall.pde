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


MetaBallManager mMetaBallManager;
int mCurrentCircle = 0;
ACArcBall mArcBall;
void settings() {
    size(1024, 768, P3D);
}
void setup() {
    textFont(createFont("Courier", 11));
    mArcBall = new ACArcBall(width / 2.0f, height / 2.0f, 0, 400.0f, this, true);
    mMetaBallManager = new MetaBallManager();
    mMetaBallManager.dimension.set(width, height, height);
    mMetaBallManager.resolution.set(30, 30, 30);
    mMetaBallManager.position.set(width / -2.0f, height / -2.0f, height / -2.0f);
    mMetaBallManager.add(new MetaBall(new PVector(0, 0, 0), 1, 100));
}
void draw() {
    background(255);
    directionalLight(126, 126, 126, 0, 0, -1);
    ambientLight(102, 102, 102);
    /* draw extra info */
    fill(0);
    noStroke();
    text("ISO VALUE : " + mMetaBallManager.isovalue(), 10, 12);
    text("SELECTED  : " + mCurrentCircle, 10, 24);
    text("FPS       : " + (int) frameRate, 10, 36);
    /* draw iso surface */
    mArcBall.update();
    if (!mMetaBallManager.metaballs().isEmpty()) {
        mMetaBallManager.metaballs().get(mCurrentCircle).position.x = mouseX - width / 2.0f;
        mMetaBallManager.metaballs().get(mCurrentCircle).position.y = mouseY - height / 2.0f;
    }
    final ArrayList<PVector> myData = mMetaBallManager.createSurface();
    /* draw metaballs */
    translate(width / 2.0f, height / 2.0f);
    fill(255, 127, 0);
    noStroke();
    beginShape(TRIANGLES);
    for (PVector myDatum : myData) {
        vertex(myDatum.x, myDatum.y, myDatum.z);
    }
    endShape();
}
void keyPressed() {
    switch (key) {
        case '+':
            mMetaBallManager.isovalue(mMetaBallManager.isovalue() + 0.01f);
            break;
        case '-':
            mMetaBallManager.isovalue(mMetaBallManager.isovalue() - 0.01f);
            break;
        case ' ':
            mCurrentCircle++;
            mCurrentCircle %= mMetaBallManager.metaballs().size();
            break;
        case 'c':
            mMetaBallManager.add(new MetaBall(new PVector(mouseX - width / 2.0f,
                                                          mouseY - height / 2.0f,
                                                          random(-100, 100)), random(1, 2), random(50, 150)));
            break;
    }
}
