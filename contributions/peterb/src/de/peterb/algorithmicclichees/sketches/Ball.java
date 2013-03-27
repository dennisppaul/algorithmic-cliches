/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.peterb.algorithmicclichees.sketches;


import processing.core.PApplet;


/**
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class Ball
        extends PApplet {

    float xpos;

    float ypos;

    float speed;

    float gravity;

    Ball(float xpos_, float ypos_, float speed_, float gravity_) {

        xpos = xpos_;
        ypos = ypos_;
        speed = speed_;
        gravity = gravity_;
    }

    public void display(PApplet p) {
        p.fill(255);
        p.ellipse(xpos, ypos, 40f, 40f);

    }

    public void move(PApplet p) {
        ypos += speed;

        //speed += gravity;
        
        
        if (ypos > p.height || ypos < 0) {
            //ypos = height;
            speed *= (-1);
        }

    }
}
