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


final ArrayList<Entity> mEntities = new ArrayList();
void settings() {
    size(1024, 768, P3D);
}
void setup() {
    rectMode(CENTER);
    for (int i = 0; i < 100; i++) {
        mEntities.add(new Entity(this));
    }
}
void draw() {
    final float mDelta = 1.0f / frameRate;
    background(255);
    for (Entity m : mEntities) {
        m.update(mDelta);
        m.draw(g);
    }
}
class Entity {
    static final float IDEAL_SCALE = 20.0f;
    PVector position = new PVector();
    int entity_color;
    float speed;
    float scale;
    State mState;
    Entity(PApplet pPApplet) {
        switchState(new StateFollowMouse(this, pPApplet));
        position.set(pPApplet.random(pPApplet.width), pPApplet.random(pPApplet.height));
        speed = pPApplet.random(1, 5) * 20;
        scale = IDEAL_SCALE;
    }
    void update(final float pDelta) {
        mState.update(pDelta);
    }
    void draw(PGraphics g) {
        g.noStroke();
        g.fill(entity_color);
        g.pushMatrix();
        g.translate(position.x, position.y);
        g.ellipse(0, 0, scale, scale);
        if (mState instanceof StateBrownianMotion) {
            g.stroke(entity_color);
            g.line(scale, scale, -scale, -scale);
            g.line(-scale, scale, scale, -scale);
        }
        g.popMatrix();
    }
    final void switchState(State pState) {
        if (mState != null) {
            mState.done();
        }
        mState = pState;
        mState.setup();
    }
}
abstract class State {
    final Entity entity;
    final PApplet sketch;
    State(Entity pParent, PApplet pPApplet) {
        entity = pParent;
        sketch = pPApplet;
    }
    abstract void setup();
    abstract void update(final float pDelta);
    abstract void done();
}
class StateBrownianMotion
        extends State {
    static final float STATE_DURATION = 4.0f;
    static final float BROWNIAN_SPEED = 15.0f;
    float mStateTime;
    StateBrownianMotion(Entity pParent, PApplet pPApplet) {
        super(pParent, pPApplet);
    }
    void setup() {
        entity.entity_color = color(255, 127, 0, 127);
    }
    void update(final float pDelta) {
        mStateTime += pDelta;
        if (mStateTime > STATE_DURATION) {
            entity.switchState(new StateChangeRandomly(entity, sketch));
        } else {
            entity.position.add(sketch.random(-BROWNIAN_SPEED, BROWNIAN_SPEED),
                                sketch.random(-BROWNIAN_SPEED, BROWNIAN_SPEED));
        }
    }
    void done() {
    }
}
class StateChangeRandomly
        extends State {
    static final float STATE_DURATION = 1.5f;
    float mStateTime;
    StateChangeRandomly(Entity pParent, PApplet pPApplet) {
        super(pParent, pPApplet);
    }
    void setup() {
    }
    void update(float pDelta) {
        mStateTime += pDelta;
        if (mStateTime > STATE_DURATION) {
            entity.switchState(new StateFollowMouse(entity, sketch));
        } else {
            entity.entity_color = color(sketch.random(127, 255), sketch.random(127, 255), 0, 127);
            entity.scale = sketch.random(50, 100);
        }
    }
    void done() {
    }
}
class StateFollowMouse
        extends State {
    static final float MIN_DISTANCE = 10.0f;
    StateFollowMouse(Entity pParent, PApplet pPApplet) {
        super(pParent, pPApplet);
    }
    void setup() {
        entity.entity_color = color(0, 127, 255, 127);
    }
    void update(float pDelta) {
        PVector mDiff = PVector.sub(new PVector(sketch.mouseX, sketch.mouseY), entity.position);
        if (mDiff.mag() < MIN_DISTANCE) {
            entity.switchState(new StateBrownianMotion(entity, sketch));
        } else {
            entity.scale += (Entity.IDEAL_SCALE - entity.scale) * pDelta;
            mDiff.normalize();
            mDiff.mult(pDelta);
            mDiff.mult(entity.speed);
            entity.position.add(mDiff);
        }
    }
    void done() {
        entity.scale = Entity.IDEAL_SCALE;
    }
}
