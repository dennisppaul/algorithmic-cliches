package de.peterb.algorithmicclichees.sketches;


import processing.core.PApplet;
import processing.core.PVector;


public class MeinErsterProcessingSketch extends PApplet {

    Ball ball_one;

    Vector2f v1;

    Vector2f d1;

//    PVector direction = new PVector(random(10),random(10));
    //PVector position = new PVector(10, 10);
    // 
    //PVector direction; 
//    PVector randomA = new PVector (random(3),random(3));
    int radius = 13;

    public void setup() {
        size(640, 480, OPENGL);
        //  direction = new PVector();
        v1 = new Vector2f(320, 240);
        d1 = new Vector2f(1, 1);
        //ball_one = new Ball(320, 240, 20, 0.1f);
    }

    public void draw() {

        //direction.set(1, 1, 0);
        frameRate(10);
        background(255);

        fill(225, 50);
        //rect(0, 0, width, height);
        v1.add(d1);

        if (v1.x < radius || v1.x > (width - radius)) {
            d1.x *= -1;
            // postion.add();
        }
        if (v1.y < radius || v1.y > (height - radius)) {
            d1.y *= -1;
        }
        fill(50);

        ellipse(v1.x, v1.y, radius * 2, radius * 2);

//        ball_one.display(this);
//        ball_one.move(this);

    }

    public static void main(String[] args) {
        PApplet.main(new String[]{MeinErsterProcessingSketch.class.getName()});
    }
}
