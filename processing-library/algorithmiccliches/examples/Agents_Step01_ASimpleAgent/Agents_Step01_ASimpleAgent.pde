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
import de.hfkbremen.gewebe.*; 
import ddf.minim.*; 
import ddf.minim.analysis.*; 
import quickhull3d.*; 


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
    myAgent.velocity.set(14, 18);
    myAgent.radius = 15;
}
void draw() {
    background(255);
    myAgent.draw();
}
class Agent {
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
