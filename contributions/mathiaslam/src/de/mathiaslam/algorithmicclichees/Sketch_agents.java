package de.mathiaslam.algorithmicclichees;


import processing.core.*;
import static processing.core.PApplet.print;


public class Sketch_agents extends PApplet {

    Agent[] agents = new Agent[1];

    Vector2f acceleration;

    public void setup() {
        size(640, 480);
        smooth();
        frameRate(60);
        agents[0] = new Agent(10);
        //agent2 = new Agent(10);
    }

    public void draw() {
        background(255);
        acceleration = new Vector2f(random(-2, 2), random(-2, 2));
        for (int i = 0; i < agents.length; i++) {
            print(acceleration);
            agents[i].constrainVelocity();
            agents[i].setAcceleration(acceleration);
            agents[i].move(this);
            agents[i].display(this);
        }
    }

    public void mousePressed() {
        // A new ball object
        Agent b = new Agent(10);
        agents = (Agent[]) append(agents, b);
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{Sketch_agents.class.getName()});
    }
}
