package de.hfkbremen.algorithmiccliches.additional.util;

import processing.core.PApplet;

public class SketchStub
        extends PApplet {

    public void setup() {
        size(1024, 768, OPENGL);
    }

    public void draw() {
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchStub.class.getName()});
    }
}
