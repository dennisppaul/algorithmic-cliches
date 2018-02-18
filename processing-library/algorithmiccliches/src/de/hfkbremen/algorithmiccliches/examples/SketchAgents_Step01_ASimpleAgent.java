package de.hfkbremen.algorithmiccliches.examples;

import processing.core.PApplet;
import processing.core.PVector;

public class SketchAgents_Step01_ASimpleAgent extends PApplet {

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

    private Agent myAgent;

    public void settings() {
        size(1024, 768, P3D);
    }

    public void setup() {
        smooth();
        noFill();
        ellipseMode(CENTER);
        frameRate(10);

        myAgent = new Agent();
        myAgent.position.set(width / 2, height / 2);
        myAgent.velocity.set(14, 18);
        myAgent.radius = 15;
    }

    public void draw() {
        background(255);
        myAgent.draw();
    }

    private class Agent {

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

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchAgents_Step01_ASimpleAgent.class.getName()});
    }
}
