package de.hfkbremen.algorithmiccliches.additional.examples;

import de.hfkbremen.algorithmiccliches.agents.Vector2f;
import processing.core.PApplet;

public class SketchAgents_Step06_TurningAtConstantSpeed extends PApplet {

    /*
     * the agent
     * step 06 - turning at constant speed.
     *
     * introducing:
     * maximum acceleration
     *
     * import Vector2f
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
        myAgent.velocity.set(1f, 3f);
        myAgent.radius = 15;
        myAgent.maxspeed = 3.5f;
        myAgent.maxacceleration = 0.75f;
    }

    public void draw() {
        background(255);

        myAgent.acceleration.set(random(-0.5f, 0.5f), random(-0.5f, 0.5f));
        myAgent.loop();
        myAgent.draw();
    }

    private class Agent {

        Vector2f position = new Vector2f();

        Vector2f velocity = new Vector2f();

        Vector2f acceleration = new Vector2f();

        float maxspeed = 0;

        float maxacceleration = 0;

        float radius = 0;

        void loop() {
            float myAccelerationSpeed = acceleration.length();
            if (myAccelerationSpeed > maxacceleration) {
                acceleration.normalize();
                acceleration.scale(maxacceleration);
            }
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
        PApplet.main(new String[]{SketchAgents_Step06_TurningAtConstantSpeed.class.getName()});
    }
}
