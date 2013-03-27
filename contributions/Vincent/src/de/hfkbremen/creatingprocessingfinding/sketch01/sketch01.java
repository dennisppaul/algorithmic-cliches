package de.hfkbremen.creatingprocessingfinding.sketch01;

import java.util.ArrayList;
import processing.core.PApplet;


import static processing.core.PConstants.OPENGL;
import processing.core.PVector;

public class sketch01
        extends PApplet {

    float rotX = 0, rotY = 0;
    float a = 0;
    float b = 0;
    ArrayList<particle> particles;

    public void setup() {
        size(800, 600, P3D);
        particles = new ArrayList<particle>();
        for (int i = 0; i < (int) random(100, 1000); i++) {
            float r = width / 12;
            float theta = (float) random(0, TWO_PI);
            float phi = (float) random(0, PI);
            PVector p = new PVector(r * sin(theta) * sin(phi), r * cos(theta) * sin(phi), r * cos(phi));
            PVector v = PVector.mult(p, 1 / p.mag());
            v.mult((float) random(1, 2));
            particles.add(new particle(p, v));
        }




    }

    public static void main(String[] args) {
        PApplet.main(new String[]{sketch01.class.getName()});
    }
}
