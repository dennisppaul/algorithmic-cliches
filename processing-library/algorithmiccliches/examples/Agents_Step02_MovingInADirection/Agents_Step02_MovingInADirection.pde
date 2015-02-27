import mathematik.*;
import oscP5.*;
import netP5.*;
import mathematik.Vector2f;
/*
 * the agent
 * step 02 - moving in a direction.
 *
 * import Vector2f
 */
Agent myAgent;

void setup() {
    size(1024, 768);
    smooth();
    noFill();
    ellipseMode(CENTER);
    frameRate(10);

    myAgent = new Agent();
    myAgent.position.set(width / 2, height / 2);
    myAgent.velocity.set(5, 8);
    myAgent.radius = 15;
}

void draw() {
    background(255);

    myAgent.loop();
    myAgent.draw();
}

class Agent {

    Vector2f position = new Vector2f();

    Vector2f velocity = new Vector2f();

    float radius = 0;

    void loop() {
        position.add(velocity);
    }

    void draw() {
        stroke(0);
        ellipse(position.x, position.y, radius * 2, radius * 2);
        stroke(255, 0, 0);
        line(position.x,
                position.y,
                position.x + velocity.x,
                position.y + velocity.y);
    }
}
