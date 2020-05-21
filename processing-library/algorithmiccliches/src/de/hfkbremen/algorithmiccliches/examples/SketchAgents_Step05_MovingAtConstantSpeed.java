package de.hfkbremen.algorithmiccliches.examples;

import de.hfkbremen.algorithmiccliches.agents.Vector2f;
import processing.core.PApplet;

public class SketchAgents_Step05_MovingAtConstantSpeed extends PApplet {

    /*
     * the agent
     * step 05 - moving at constant mSpeed.
     *
     * introducing:
     * maximum mSpeed
     *
     * import Vector2f
     */

    private Agent myAgent;

    public void settings() {
        size(1024, 768, P3D);
    }

    public void setup() {
        noFill();
        ellipseMode(CENTER);
        frameRate(10);

        myAgent = new Agent();
        myAgent.position.set(width / 2.0f, height / 2.0f);
        myAgent.velocity.set(3f, 4f);
        myAgent.radius = 15;
        myAgent.maxspeed = 3.5f;
    }

    public void draw() {
        background(255);

        myAgent.acceleration.set(random(-1.0f, 1.0f), random(-1.0f, 1.0f));
        myAgent.loop();
        myAgent.draw();
    }

    private class Agent {

        Vector2f position = new Vector2f();
        Vector2f velocity = new Vector2f();
        Vector2f acceleration = new Vector2f();
        float maxspeed = 0;
        float radius = 0;

        void loop() {
            velocity.add(acceleration);

            float mySpeed = velocity.length();
            if (mySpeed > maxspeed) {
                velocity.normalize();
                velocity.scale(maxspeed);
            }
            position.add(velocity);
        }

        void draw() {
            stroke(0, 0, 0);
            ellipse(position.x, position.y, radius * 2, radius * 2);
            stroke(255, 0, 0);
            line(position.x,
                 position.y,
                 position.x + velocity.x,
                 position.y + velocity.y);
            stroke(0, 255, 0);
            line(position.x + velocity.x,
                 position.y + velocity.y,
                 position.x + velocity.x + acceleration.x,
                 position.y + velocity.y + acceleration.y);
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchAgents_Step05_MovingAtConstantSpeed.class.getName()});
    }
}
