package de.mathiaslam.algorithmicclichees;


import java.util.Vector;
import processing.core.*;


public class Sketch_agents_02 extends PApplet {

    private Vector<Agent> agents;

    Vector2f acceleration;

    public void setup() {
        size(1024, 786);
        smooth();
        frameRate(60);
        agents = new Vector<Agent>();
        background(255);
    }

    public void draw() {
        // background(255);
        for (Agent a : agents) {
            acceleration = new Vector2f(random(-1.0f, 1.0f), random(-1.0f, 1.0f));
            //a.follow(mouseX + random(20), mouseY + random(20));
            a.getNeighbours(this, agents);
            a.setAcceleration(acceleration);
            a.move(this);
            a.display(this);
        }
    }

    public void mousePressed() {
        for (int i = 0; i < 2; i++) {
            agents.add(new Agent(this, 1));
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{Sketch_agents_01.class.getName()});
    }
}
