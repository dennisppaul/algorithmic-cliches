package de.peterb.algorithmicclichees.sketches;


import processing.core.PApplet;


public class Agent {

    float radius;

    Vector2f position;

    Vector2f velocity;
//Konstruktor 1

    public Agent() {
    }
//Konstruktor 2

    public Agent(Vector2f p_, Vector2f v_, float r_) {

        position = p_;
        velocity = v_;
        radius = r_;

    }
//Konstruktor 3

    public Agent(float r_) {

        position = new Vector2f(320, 240);
        velocity = new Vector2f(0, 0);

        radius = r_;
    }

    //SETTER
    public void setV(Vector2f v_) {
        velocity.x = v_.x;
        velocity.y = v_.y;

    }

    public void setP(Vector2f p_) {
        position.x = p_.x;
        position.y = p_.y;
    }

    public void setR(float r_) {
        radius = r_;
    }

    public void display(PApplet p) {

        p.stroke(225, 0, 0);
        p.strokeWeight(2);
        p.line(position.x, position.y, velocity.x * 10 + position.x, velocity.y * 10 + position.y);
        p.stroke(0);
        p.noFill();
        p.strokeWeight(1);
        p.ellipse(position.x, position.y, radius * 2, radius * 2);

    }

    public void move(PApplet p) {

        checkWall(p);
//        velocity.set(p.random(-2, 2), p.random(-2, 2));
        velocity.set(1, -2);
        position.add(velocity);

    }

    public void checkWall(PApplet p) {
        if (position.x < radius || position.x > (p.width - radius)) {
            velocity.x *= -1;
        }

        if (position.y < radius || position.y > (p.height - radius)) {
            velocity.y *= -1;

        }
    }
}
