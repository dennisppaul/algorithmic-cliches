import oscP5.*;
import netP5.*;
import teilchen.util.*;
import java.util.Vector;

/**
 * http://en.wikipedia.org/wiki/State_machine
 */
final Vector<Entity> mEntities = new Vector<Entity>();

void settings() {
    size(1024, 768, P3D);
}

void setup() {
    rectMode(CENTER);
    smooth();

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

    PVector position = new PVector();

    int entity_color;

    float speed;

    float scale;

    static final float IDEAL_SCALE = 20.0f;

    State mState;

    final PApplet p;

    Entity(PApplet pPApplet) {
        p = pPApplet;
        switchState(new StateFollowMouse(this, p));
        position.set(p.random(p.width), p.random(p.height));
        speed = p.random(1, 5) * 20;
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

    final Entity e;

    final PApplet p;

    State(Entity pParent, PApplet pPApplet) {
        e = pParent;
        p = pPApplet;
    }

    abstract void setup();

    abstract void update(final float pDelta);

    abstract void done();
}

class StateBrownianMotion
        extends State {

    float mStateTime;

    static final float STATE_DURATION = 4.0f;

    static final float BROWNIAN_SPEED = 15.0f;

    StateBrownianMotion(Entity pParent, PApplet pPApplet) {
        super(pParent, pPApplet);
    }

    void setup() {
        e.entity_color = p.color(255, 127, 0, 127);
    }

    void update(final float pDelta) {
        mStateTime += pDelta;
        if (mStateTime > STATE_DURATION) {
            e.switchState(new StateChangeRandomly(e, p));
        } else {
            e.position.add(p.random(-BROWNIAN_SPEED, BROWNIAN_SPEED),
                    p.random(-BROWNIAN_SPEED, BROWNIAN_SPEED));
        }
    }

    void done() {
    }
}

class StateChangeRandomly
        extends State {

    float mStateTime;

    static final float STATE_DURATION = 1.5f;

    StateChangeRandomly(Entity pParent, PApplet pPApplet) {
        super(pParent, pPApplet);
    }

    void setup() {
    }

    void update(float pDelta) {
        mStateTime += pDelta;
        if (mStateTime > STATE_DURATION) {
            e.switchState(new StateFollowMouse(e, p));
        } else {
            e.entity_color = p.color(p.random(127, 255), p.random(127, 255), 0, 127);
            e.scale = p.random(50, 100);
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
        e.entity_color = p.color(0, 127, 255, 127);
    }

    void update(float pDelta) {
        PVector mDiff = PVector.sub(new PVector(p.mouseX, p.mouseY), e.position);
        if (mDiff.mag() < MIN_DISTANCE) {
            e.switchState(new StateBrownianMotion(e, p));
        } else {
            e.scale += (Entity.IDEAL_SCALE - e.scale) * pDelta;
            mDiff.normalize();
            mDiff.mult(pDelta);
            mDiff.mult(e.speed);
            e.position.add(mDiff);
        }
    }

    void done() {
        e.scale = Entity.IDEAL_SCALE;
    }
}
