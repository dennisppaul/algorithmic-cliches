package de.hfkbremen.algorithmiccliches.agents;


import processing.core.PApplet;


public class Step02_MovingInADirection extends PApplet {

    /*
     * the agent
     * step 02 - moving in a direction.
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
        myAgent.velocity.set(5, 8);
        myAgent.radius = 15;
    }

    public void draw() {
        background(255);

        myAgent.loop();
        myAgent.draw();
    }

    private class Agent {

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

    public static void main(String[] args) {
        PApplet.main(new String[]{Step02_MovingInADirection.class.getName()});
    }
}
