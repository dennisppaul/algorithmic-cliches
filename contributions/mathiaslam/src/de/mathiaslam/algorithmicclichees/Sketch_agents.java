package de.mathiaslam.algorithmicclichees;


import processing.core.*;


public class Sketch_agents extends PApplet {

    private Agent agent1;
    private Agent agent2;
    Agent[] agents = new Agent[1];
    
    Vector2f acceleration;

    public void setup() {

        size(640, 480);
        smooth();
        frameRate(60);
        agents[0] = new Agent(10);
        //agent2 = new Agent(10);


    }

    public void draw() {
        background(255);
        acceleration = new Vector2f(random(-0.1f, 0.1f), random(-0.1f, 0.1f));
        
        for (int i = 0; i < agents.length; i ++ ) {
        agents[i].setAcceleration(acceleration);
        //agent2.setAcceleration(acceleration);
        agents[i].move(this);
        //agent2.move(this);
        agents[i].display(this);
        //agent2.display(this);
        }
        
    }
public void mousePressed() {
  // A new ball object
  Agent b = new Agent(10); 
  agents = (Agent[]) append(agents,b);
}

    public static void main(String[] args) {
        PApplet.main(new String[]{Sketch_agents.class.getName()});
    }
}
