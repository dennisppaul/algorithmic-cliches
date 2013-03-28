package de.mathiaslam.algorithmicclichees;


import java.util.Vector;
import processing.core.*;


public class Sketch_agents extends PApplet {

    private Vector<Agent> agents;

    Vector2f acceleration;

    public void setup() {
        size(640, 480);
        smooth();
        frameRate(60);
        agents = new Vector<Agent>();
    }

    public void draw() {
        background(255);
        //  acceleration = new Vector2f(random(-2, 2), random(-2, 2));
        for (Agent a : agents) {
            acceleration = new Vector2f(random(-2, 2), random(-2, 2));
            a.constrainVelocity();
            a.setAcceleration(acceleration);
            a.move(this);
            a.display(this);
        }
    }

    public void mousePressed() {
        for (int i = 0; i < 100; i++) {
            agents.add(new Agent(3));
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{Sketch_agents.class.getName()});
    }
}
