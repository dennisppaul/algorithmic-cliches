import de.hfkbremen.algorithmiccliches.*; 
import de.hfkbremen.algorithmiccliches.agents.*; 
import de.hfkbremen.algorithmiccliches.cellularautomata.*; 
import de.hfkbremen.algorithmiccliches.convexhull.*; 
import de.hfkbremen.algorithmiccliches.delaunaytriangulation2.*; 
import de.hfkbremen.algorithmiccliches.fluiddynamics.*; 
import de.hfkbremen.algorithmiccliches.isosurface.*; 
import de.hfkbremen.algorithmiccliches.laserline.*; 
import de.hfkbremen.algorithmiccliches.lindenmayersystems.*; 
import de.hfkbremen.algorithmiccliches.octree.*; 
import de.hfkbremen.algorithmiccliches.util.*; 
import de.hfkbremen.algorithmiccliches.voronoidiagram.*; 
import teilchen.*; 
import teilchen.behavior.*; 
import teilchen.constraint.*; 
import teilchen.cubicle.*; 
import teilchen.integration.*; 
import teilchen.util.*; 
import teilchen.force.*; 
import teilchen.force.flowfield.*; 
import teilchen.force.vectorfield.*; 
import de.hfkbremen.gewebe.*; 
import ddf.minim.*; 
import ddf.minim.analysis.*; 
import quickhull3d.*; 


/*
 * the agent
 * step 06 - turning at constant mSpeed.
 *
 * introducing:
 * maximum acceleration
 *
 * import Vector2f
 */
Agent myAgent;
void settings() {
    size(1024, 768, P3D);
}
void setup() {
    noFill();
    ellipseMode(CENTER);
    frameRate(10);
    myAgent = new Agent();
    myAgent.position.set(width / 2.0f, height / 2.0f);
    myAgent.velocity.set(1f, 3f);
    myAgent.radius = 15;
    myAgent.maxspeed = 3.5f;
    myAgent.maxacceleration = 0.75f;
}
void draw() {
    background(255);
    myAgent.acceleration.set(random(-0.5f, 0.5f), random(-0.5f, 0.5f));
    myAgent.loop();
    myAgent.draw();
}
class Agent {
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
