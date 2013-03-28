package de.peterb.algorithmicclichees.sketches;


import java.util.ArrayList;
import processing.core.PApplet;


public class Run extends PApplet {

    private ArrayList<Agent> agents;

    public void setup() {
        size(1280, 800, OPENGL);
        frameRate(60);

//        background(255);

        Vector2f position = new Vector2f(320, 240);
        Vector2f velocity = new Vector2f(2, 1);
        agents = new ArrayList<Agent>();


    }

    public void mousePressed() {
        for (int i = 0; i < 100; i++) {
            Agent a;
            a = new Agent(new Vector2f(random(0, width), random(0, height)), 2);
            a.setMaxAcceleration(random(1, 10));
            agents.add(a);
        }
    }

    public void draw() {

        float deltaTime = 1.0f / frameRate;
        background(255);


//        if (mousePressed) {
//            for (int i = 0; i < 10; i++) {
//                Agent a;
//                a = new Agent(new Vector2f(width / 2, height / 2), random(1, 10));
//                a.setMaxAcceleration(random(1, 10));
//                agents.add(a);
//            }
//        }
        for (Agent a : agents) {
            a.update(this, deltaTime);
            a.display(this);
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{Run.class.getName()});
    }
}
