package de.peterb.algorithmicclichees;

import processing.core.PApplet;

public class MeinErsterProcessingSketch extends PApplet {

    public void setup() {
        size(640, 480, OPENGL);
        println("test");
        
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{MeinErsterProcessingSketch.class.getName()});
    }
}

