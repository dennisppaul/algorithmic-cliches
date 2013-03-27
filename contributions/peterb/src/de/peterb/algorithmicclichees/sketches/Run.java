package de.peterb.algorithmicclichees.sketches;


import processing.core.PApplet;
//import processing.core.PVector;


public class Run extends PApplet {

    Ball ball_one;

    Agent agent1;

    Agent agent2;

    Vector2f position;

    Vector2f velocity;

    int radius = 5;

    public void setup() {
        size(640, 480, OPENGL);
        //  direction = new PVector();
        position = new Vector2f(100, 200);
        velocity = new Vector2f(0, 0);
        agent1 = new Agent(10);
        agent2 = new Agent(random(20));
    }

    public void draw() {

        frameRate(60);
        background(255);

        agent1.display(this);
        agent1.move(this);

        agent2.display(this);
        agent2.move(this);

//        velocity.set(random(-2, 2), random(-2, 2));
//        position.add(velocity);






//        ball_one.display(this);
//        ball_one.move(this);


    }

    public static void main(String[] args) {
        PApplet.main(new String[]{Run.class.getName()});
    }
}
