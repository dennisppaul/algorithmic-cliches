package de.hfkbremen.algorithmiccliches.examples;

import de.hfkbremen.algorithmiccliches.lindenmayersystems.LSystem;
import processing.core.PApplet;

public class SketchLindenmayerSystemBasic extends PApplet {

    LSystem ds;

    public void settings() {
        size(1024, 768);
    }

    public void setup() {
        ds = new LSystem();
        ds.simulate(4);
        System.out.println(ds.production);
    }

    public void draw() {
        background(255);
        stroke(0);
        ds.render(g);
    }

    public static void main(String[] args) {
        PApplet.main(SketchLindenmayerSystemBasic.class.getName());
    }
}
