package de.dennisppaul.creatingprocessingfinding;


import processing.core.PApplet;


public class SketchExample
        extends PApplet {

    public void setup() {
        size(1024, 768);
        System.out.println(sketchPath);
    }

    public void draw() {
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchExample.class.getName()});
    }
}
