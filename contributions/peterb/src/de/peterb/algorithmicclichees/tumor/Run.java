package de.peterb.algorithmicclichees.tumor;


import de.peterb.algorithmicclichees.Vector2f;


import java.util.ArrayList;
import processing.core.PApplet;
import static processing.core.PConstants.PDF;
//import processing.core.PVector;


public class Run extends PApplet {

    private ArrayList<Agent> agents;

    boolean record;

    public void setup() {
        if (record) {

            beginRecord(PDF, "frame-####.pdf");
        }


        size(1280, 800);
        background(255);
        //  direction = new PVector();
        Vector2f position = new Vector2f(320, 240);
        Vector2f velocity = new Vector2f(2, 1);

        agents = new ArrayList<Agent>();


    }

    public void draw() {


        // frameRate(10);
        // background(255);

        if (mousePressed) {

            for (int i = 0; i < 100; i++) {

                agents.add(new Agent(new Vector2f(mouseX, mouseY), 10));
            }

        }


        for (Agent a : agents) {
//            a.setVelocity(random(-10, 10), random(-10, 10));
            a.setVelocity(1, 1);
            a.setAcceleration(random(-10, 10), random(-10, 10));
            a.setRadius(2);
            a.move(this);
            a.display(this);

        }



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
