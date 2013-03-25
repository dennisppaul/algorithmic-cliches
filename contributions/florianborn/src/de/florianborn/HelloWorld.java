package de.florianborn;


import processing.core.PApplet;


public class HelloWorld extends PApplet {

    public void setup() {
        size(640, 480, OPENGL);
    }

    public void draw() {
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{HelloWorld.class.getName()});
    }
}
