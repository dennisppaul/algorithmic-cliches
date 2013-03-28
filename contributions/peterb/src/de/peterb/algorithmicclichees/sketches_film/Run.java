package de.peterb.algorithmicclichees.sketches_film;


import java.util.ArrayList;
import processing.core.PApplet;
//import processing.core.PVector;


public class Run extends PApplet {

    private ArrayList<Agent> agents;

    private Agent agent1;

    private Agent agent2;

    private Agent agent3;

    public void setup() {

        size(1280, 800, OPENGL);
//        background(255);
        //  direction = new PVector();
        Vector2f position = new Vector2f(320, 240);
        Vector2f velocity = new Vector2f(2, 1);

//        agent1 = new Agent(new Vector2f(position), new Vector2f(velocity), 10);
//        agent2 = new Agent(new Vector2f(position), new Vector2f(2, 4), 5);
//        agent3 = new Agent(new Vector2f(position), new Vector2f(velocity), 5);


        agents = new ArrayList<Agent>();


//        agent1.setRadius(10);

//        agent1.setVelocity(2, 1);
//        agent2.setVelocity(-5, 5);
    }

    public void draw() {

        frameRate(60);
        background(255);

        if (mousePressed) {

            for (int i = 0; i < 100; i++) {

                agents.add(new Agent(new Vector2f(mouseX, mouseY), 10));
            }

        }


        for (Agent a : agents) {
//            a.setVelocity(random(-10, 10), random(-10, 10));
//            a.setVelocity(1, 1);
//            a.setAcceleration(random(-10, 10), random(-10, 10));
            a.setAcceleration(mouseX * random(-0.1f, 0.1f), mouseY);
//            a.setAcceleration(1, 1);
            a.setRadius(2);
            a.move(this);
            a.display(this);

        }

    }

    public static void main(String[] args) {
        PApplet.main(new String[]{Run.class.getName()});
    }
}
