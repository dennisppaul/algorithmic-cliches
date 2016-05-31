import oscP5.*;
import netP5.*;
import teilchen.util.*;
import java.util.Vector;
/*
 * the agent
 * step 03 - moving in different directions.
 *
 * import PApplet
 */

Agent myAgent;

void settings() {
    size(1204, 768, P3D);
}

void setup() {
    smooth();
    noFill();
    ellipseMode(CENTER);
    frameRate(10);

    myAgent = new Agent();
    myAgent.position.set(width / 2, height / 2, 0);
    myAgent.velocity.set(5, 8, 0);
    myAgent.radius = 15;
}

void draw() {
    background(255);

    myAgent.velocity.set(random(-5, 5), random(-5, 5), 0);
    myAgent.loop();
    myAgent.draw();
}

class Agent {

    PVector position = new PVector();

    PVector velocity = new PVector();

    float radius = 0;

    void loop() {
        position.add(velocity);
    }

    void draw() {
        stroke(0);
        ellipse(position.x, position.y, radius * 2, radius * 2);
        stroke(255, 0, 0);
        line(position.x, position.y, position.x + velocity.x, position.y + velocity.y);
    }
}
