package de.peterb.algorithmicclichees.sketches;


import de.peterb.algorithmicclichees.Vector2f;
import java.util.ArrayList;
import processing.core.PApplet;
import static processing.core.PConstants.PDF;
import processing.pdf.*;


public class Run extends PApplet {

    private ArrayList<Agent> agents;

    private Nest nest;

    private float nestTimer;

    boolean record;

    public void setup() {
        size(1200, 720);
        background(255);
        nest = new Nest();
        nest.position().set(random(0, width), random(0, height));
        frameRate(60);
//        noCursor();





        Vector2f position = new Vector2f(320, 240);
        Vector2f velocity = new Vector2f(2, 1);
        agents = new ArrayList<Agent>();
    }

    public void draw() {
        if (record) {

            beginRecord(PDF, "frame-####.pdf");
        }

        float deltaTime = 1.0f / frameRate;
        // background(255);


        if (mousePressed) {
            for (int i = 0; i < 800; i++) {
                Agent a;
                a = new Agent(new Vector2f(random(0, width), random(0, height)), 2, nest);
                a.setMaxAcceleration(random(1, 2));
                agents.add(a);
            }
        }
        if (nestTimer < 3.0f) {
            nestTimer += deltaTime;
        } else {
            nestTimer = 0;
//            nest.position().set(random(0, height), random(0, width));
            nest.position().set(random(0, width), random(0, height));
        }


        for (Agent a : agents) {
            a.update(this, deltaTime);
            a.display(this);
        }


        // Draw something good here

        if (record) {
            endRecord();
            record = false;
        }

        if (keyPressed == true) {


            record = true;
        }



    }

    public static void main(String[] args) {
        PApplet.main(new String[]{Run.class.getName()});
    }
}
