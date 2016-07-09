import oscP5.*;
import netP5.*;
import teilchen.util.*;

/*
 * the agent
 * step 01 - a simple agent.
 *
 * introducing:
 * position
 * velocity
 * radius
 *
 */

Agent myAgent;

void settings() {
    size(1024, 768, P3D);
}

void setup() {
    smooth();
    noFill();
    ellipseMode(CENTER);
    frameRate(10);

    myAgent = new Agent();
    myAgent.position.set(width / 2, height / 2);
    myAgent.velocity.set(14, 18);
    myAgent.radius = 15;
}

void draw() {
    background(255);
    myAgent.draw();
}

class Agent {

    PVector position = new PVector();

    PVector velocity = new PVector();

    float radius = 0;

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
