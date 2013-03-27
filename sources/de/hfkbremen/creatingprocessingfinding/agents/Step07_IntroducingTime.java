package de.hfkbremen.creatingprocessingfinding.agents;


import de.hfkbremen.creatingprocessingfinding.agents.vectors.Vector2f;
import processing.core.PApplet;


public class Step07_IntroducingTime extends PApplet {

    /*
     * the agent
     * step 07 - introducing time.
     *
     * introducing:
     * delta time
     *
     * import Vector2f
     */
    private Agent myAgent;

    public void setup() {
        size(1024, 768);
        smooth();
        noFill();
        ellipseMode(CENTER);
        frameRate(60);

        myAgent = new Agent();
        myAgent.position.set(width / 2, height / 2);
        myAgent.velocity.set(0f, 0f);
        myAgent.acceleration.set(-4.0f, -3.0f);
        myAgent.radius = 15;
        myAgent.maxspeed = 50;
        myAgent.maxacceleration = 40;
    }

    public void draw() {
        background(255);

        goToMouse(myAgent);

        final float mDeltaTime = 1.0f / frameRate;
        myAgent.loop(mDeltaTime);
        myAgent.draw();
    }

    private void goToMouse(Agent theAgent) {
        /*
         * this method is just for quickly observing different
         * acceleration settings.
         * actually the code below already describes a first
         * simple behavior. the agent adjust its acceleration
         * vector to 'go to' the mouseposition.
         * enjoy.
         */
        Vector2f myAccelerationDirection = new Vector2f();
        myAccelerationDirection.set(mouseX, mouseY);
        myAccelerationDirection.sub(myAgent.position);
        theAgent.acceleration.set(myAccelerationDirection);
    }

    private class Agent {

        Vector2f position = new Vector2f();

        Vector2f velocity = new Vector2f();

        Vector2f acceleration = new Vector2f();

        float maxspeed = 0;

        float maxacceleration = 0;

        float radius = 0;

        void loop(float theDeltaTime) {

            float myAccelerationSpeed = acceleration.length();

            if (myAccelerationSpeed > maxacceleration) {
                acceleration.normalize();
                acceleration.multiply(maxacceleration);
            }

            Vector2f myTimerAcceleration = new Vector2f();
            myTimerAcceleration.set(acceleration);
            myTimerAcceleration.multiply(theDeltaTime);

            velocity.add(myTimerAcceleration);

            float mySpeed = velocity.length();
            if (mySpeed > maxspeed) {
                velocity.normalize();
                velocity.multiply(maxspeed);
            }

            Vector2f myTimerVelocity = new Vector2f();
            myTimerVelocity.set(velocity);
            myTimerVelocity.multiply(theDeltaTime);

            position.add(myTimerVelocity);
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
        PApplet.main(new String[]{Step07_IntroducingTime.class.getName()});
    }
}
