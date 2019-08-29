package de.peterb.algorithmicclichees.sketches_film;


import de.peterb.algorithmicclichees.Vector2f;
import java.util.ArrayList;
import processing.core.PApplet;
import static processing.core.PConstants.PDF;


public class Run extends PApplet {

    private ArrayList<Agent> agents;

    private Agent agent1;

    private Agent agent2;

    private Agent agent3;

    boolean record;

    public void setup() {
        size(1280, 800, OPENGL);
        Vector2f position = new Vector2f(320, 240);
        Vector2f velocity = new Vector2f(2, 1);
        agents = new ArrayList<Agent>();
    }

    public void draw() {

        if (record) {
            // Note that #### will be replaced with the frame number. Fancy!
            beginRecord(PDF, "frame-####.pdf");
        }
        frameRate(60);
        background(255);

        if (mousePressed) {
            for (int i = 0; i < 100; i++) {
                agents.add(new Agent(new Vector2f(mouseX, mouseY), 10));
            }
        }

        for (Agent a : agents) {
            a.setAcceleration(mouseX * random(-0.1f, 0.1f), mouseY);
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
