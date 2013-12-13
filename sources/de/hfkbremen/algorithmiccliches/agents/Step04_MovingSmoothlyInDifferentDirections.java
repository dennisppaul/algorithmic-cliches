package de.hfkbremen.algorithmiccliches.agents;


import de.hfkbremen.algorithmiccliches.agents.Vector2f;
import processing.core.PApplet;


public class Step04_MovingSmoothlyInDifferentDirections extends PApplet {

    /*
     * the agent
     * step 04 - moving smoothly in different directions.
     *
     * introducing:
     * acceleration
     *
     * import Vector2f
     */
    private Agent myAgent;

    public void setup() {
        size(1024, 768);
        smooth();
        noFill();
        ellipseMode(CENTER);
        frameRate(10);

        myAgent = new Agent();
        myAgent.position.set(width / 2, height / 2);
        myAgent.velocity.set(3f, 4f);
        myAgent.radius = 15;
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

        float radius = 0;

        void loop() {
            velocity.add(acceleration);
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
            stroke(0, 255, 0);
            line(position.x + velocity.x,
                 position.y + velocity.y,
                 position.x + velocity.x + acceleration.x,
                 position.y + velocity.y + acceleration.y);
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{Step04_MovingSmoothlyInDifferentDirections.class.getName()});
    }
}
