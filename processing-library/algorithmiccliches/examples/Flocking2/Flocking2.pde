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

			 
		Physics                mPhysics;
ArrayList<SwarmEntity> mSwarmEntities;
void settings() {
    size(1024, 768, P3D);
}
void setup() {
    frameRate(60);
    rectMode(CENTER);
    hint(DISABLE_DEPTH_TEST);
    textFont(createFont("Courier", 11));
    /* physics */
    mPhysics = new Physics();
    Teleporter mTeleporter = new Teleporter();
    mTeleporter.min().set(0, 0, 0);
    mTeleporter.max().set(width, height, 0);
    mPhysics.add(mTeleporter);
    ViscousDrag myViscousDrag = new ViscousDrag();
    mPhysics.add(myViscousDrag);
    for (int i = 0; i < 3; i++) {
        Attractor mAttractor = new Attractor();
        mAttractor.position().set(i * width / 2.0f, i * height / 2.0f);
        mAttractor.strength(-200);
        mAttractor.radius(350);
        mPhysics.add(mAttractor);
    }
    /* setup entities */
    mSwarmEntities = new ArrayList();
}
void draw() {
    final float mDeltaTime = 1.0f / frameRate;
    if (frameRate > 50) {
        spawnEntity();
    }
    /* physics */
    final int mIterations = 6;
    for (int i = 0; i < mIterations; i++) {
        float mDeltaTimeStep = mDeltaTime / mIterations;
        mPhysics.step(mDeltaTimeStep);
        Overlap.resolveOverlap(mPhysics.particles());
    }
    /* entities */
    for (SwarmEntity s : mSwarmEntities) {
        s.update(mDeltaTime);
    }
    /* draw */
    background(255);
    for (SwarmEntity s : mSwarmEntities) {
        s.draw(g);
    }
    /* draw extra info */
    fill(0);
    noStroke();
    text("ENTITIES : " + mSwarmEntities.size(), 10, 12);
    text("FPS      : " + (int) frameRate, 10, 24);
}
void spawnEntity() {
    SwarmEntity mSwarmEntity = new SwarmEntity();
    mSwarmEntity.position().set(random(width), random(height));
    mSwarmEntities.add(mSwarmEntity);
    mPhysics.add(mSwarmEntity);
}
class SwarmEntity
        extends BasicBehaviorParticle {
    final Separation<SwarmEntity> separation;
    final Alignment<SwarmEntity> alignment;
    final Cohesion<SwarmEntity> cohesion;
    final Wander wander;
    final Motor motor;
    SwarmEntity() {
        maximumInnerForce(random(100.0f, 1000.0f));
        radius(9f);
        separation = new Separation<>();
        separation.proximity(30);
        separation.weight(100.0f);
        behaviors().add(separation);
        alignment = new Alignment<>();
        alignment.proximity(40);
        alignment.weight(60.0f);
        behaviors().add(alignment);
        cohesion = new Cohesion<>();
        cohesion.proximity(200);
        cohesion.weight(5.0f);
        behaviors().add(cohesion);
        wander = new Wander();
        behaviors().add(wander);
        motor = new Motor();
        motor.auto_update_direction(true);
        motor.strength(20.0f);
        behaviors().add(motor);
    }
    void update(float theDeltaTime) {
        separation.neighbors(mSwarmEntities);
        alignment.neighbors(mSwarmEntities);
        cohesion.neighbors(mSwarmEntities);
    }
    void draw(PGraphics g) {
        pushMatrix();
        translate(position().x, position().y, position().z);
        /* velocity */
        noFill();
        stroke(0, 127, 255, 127);
        line(0, 0, velocity().x, velocity().y);
        /* body */
        rotate(atan2(velocity().y, velocity().x));
        noStroke();
        fill(0, 127, 255, 255);
        rect(0, 0, 20, 7);
        popMatrix();
    }
}
