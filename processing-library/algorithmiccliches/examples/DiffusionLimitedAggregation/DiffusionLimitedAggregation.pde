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
import gewebe.*; 
import ddf.minim.*; 
import ddf.minim.analysis.*; 
import quickhull3d.*; 

			 
		static final int NUMBER_OF_PARTICLES_UNATTACHED = 200;
static final int NUMBER_OF_MAX_PARTICLES = 1000;
final float mOctreeSize = 150;
Octree mOctree;
float mRotationZ = 0.1f;
int mSphereDetail = 8;
void settings() {
    size(1024, 768, P3D);
}
void setup() {
    textFont(createFont("Courier", 11));
    strokeWeight(0.25f);
    mOctree = new Octree(new PVector(-mOctreeSize / 2, -mOctreeSize / 2, -mOctreeSize / 2), mOctreeSize);
    for (int i = 0; i < 270; i += 16) {
        float x = sin(radians(i)) * 50;
        float y = cos(radians(i)) * 50;
        float r = 2.5f + sin(radians(i));
        addInitialParticle(r, x, y, 0);
    }
}
void draw() {
    /* move particles */
    for (OctreeEntity oe : mOctree.entities()) {
        BrownianParticle bp = (BrownianParticle) oe;
        bp.move();
    }
    /* teleport */
    float mBoxSize = mOctreeSize / 2;
    for (OctreeEntity oe : mOctree.entities()) {
        BrownianParticle bp = (BrownianParticle) oe;
        if (bp.position().x > mBoxSize) {
            bp.position().x = -mBoxSize;
        }
        if (bp.position().y > mBoxSize) {
            bp.position().y = -mBoxSize;
        }
        if (bp.position().z > mBoxSize) {
            bp.position().z = -mBoxSize;
        }
        if (bp.position().x < -mBoxSize) {
            bp.position().x = mBoxSize;
        }
        if (bp.position().y < -mBoxSize) {
            bp.position().y = mBoxSize;
        }
        if (bp.position().z < -mBoxSize) {
            bp.position().z = mBoxSize;
        }
    }
    /* maintain particle number */
    final ArrayList<BrownianParticle> mAttachedParticles = new ArrayList();
    for (OctreeEntity oe : mOctree.entities()) {
        BrownianParticle bp = (BrownianParticle) oe;
        if (bp.attached()) {
            mAttachedParticles.add(bp);
        }
    }
    int mNumberOfUnattachedParticles = mOctree.entities().size() - mAttachedParticles.size();
    if (mNumberOfUnattachedParticles < NUMBER_OF_PARTICLES_UNATTACHED && mOctree.entities()
                                                                                .size() < NUMBER_OF_MAX_PARTICLES) {
        addBrownianParticle();
    }
    /* resolve overlap */
    Overlap.resolveOverlap(mAttachedParticles);
    /* --- */
    background(255);
    lights();
    pushMatrix();
    translate(width / 2.0f, height / 2.0f, 0);
    /* rotate */
    mRotationZ += 1.0f / frameRate * 0.1f;
    rotateX(THIRD_PI);
    rotateZ(mRotationZ);
    scale(4);
    /* draw unattached */
    noFill();
    stroke(0);
    for (OctreeEntity oe : mOctree.entities()) {
        BrownianParticle bp = (BrownianParticle) oe;
        if (!bp.attached()) {
            drawCross(bp.position(), bp.radius());
        }
    }
    /* draw attached */
    noStroke();
    sphereDetail(mSphereDetail);
    for (BrownianParticle bp : mAttachedParticles) {
        fill(bp.entity_color);
        pushMatrix();
        translate(bp.position().x, bp.position().y, bp.position().z);
        sphere(bp.radius() * 1.1f);
        popMatrix();
    }
    popMatrix();
    /* draw info */
    fill(0);
    noStroke();
    text("TOTAL    : " + mOctree.entities().size(), 10, 12);
    text("ATTACHED : " + mAttachedParticles.size(), 10, 24);
    text("FPS      : " + frameRate, 10, 36);
}
void keyPressed() {
    switch (key) {
        case '+':
            mSphereDetail++;
            break;
        case '-':
            mSphereDetail--;
            mSphereDetail = max(mSphereDetail, 3);
            break;
        default:
            break;
    }
}
void addInitialParticle(float r, float x, float y, float z) {
    BrownianParticle p = new BrownianParticle(r);
    p.position().set(x, y, z);
    p.attach(true);
    mOctree.add(p);
}
void drawCross(PVector v, float pRadius) {
    line(v.x - pRadius, v.y, v.z, v.x + pRadius, v.y, v.z);
    line(v.x, v.y - pRadius, v.z, v.x, v.y + pRadius, v.z);
    line(v.x, v.y, v.z - pRadius, v.x, v.y, v.z + pRadius);
}
void addBrownianParticle() {
    BrownianParticle mEntity = new BrownianParticle(random(0.5f, 2.5f));
    mEntity.position().x = random(-mOctreeSize / 2, mOctreeSize / 2);
    mEntity.position().y = random(-mOctreeSize / 2, mOctreeSize / 2);
    mEntity.position().z = random(-mOctreeSize / 2, mOctreeSize / 2);
    mOctree.add(mEntity);
}
class BrownianParticle extends BasicParticle implements OctreeEntity {
    static final float SPEED = 4;
    static final float SELECT_RADIUS = 20;
    int entity_color = color(191);
    boolean mAttached = false;
    BrownianParticle(float pRadius) {
        radius(pRadius);
    }
    void move() {
        if (!mAttached) {
            position().x += random(-SPEED, SPEED);
            position().y += random(-SPEED, SPEED);
            position().z += random(-SPEED, SPEED);
            attach();
        }
    }
    void attach(boolean pAttachState) {
        mAttached = pAttachState;
    }
    boolean attach() {
        ArrayList<OctreeEntity> mEntities = mOctree.getEntitesWithinSphere(position(), SELECT_RADIUS);
        if (mEntities != null) {
            for (OctreeEntity mEntity : mEntities) {
                BrownianParticle m = (BrownianParticle) mEntity;
                if (m != this & m.attached()) { // only attach to still particles?
                    float mDistance = PVector.dist(position(), m.position());
                    if (mDistance < (radius() + m.radius())) {
                        mAttached = true;
                        return true;
                    }
                }
            }
        }
        return false;
    }
    boolean attached() {
        return mAttached;
    }
}
