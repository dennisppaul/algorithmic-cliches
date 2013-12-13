package de.hfkbremen.algorithmiccliches.exporting;


import processing.core.PApplet;
import processing.core.PGraphics;


/**
 * http://sunflow.sourceforge.net/
 * http://en.wikipedia.org/wiki/Global_illumination
 */
public class SketchRenderWithSunflow
        extends PApplet {

    public void setup() {
        size(1024, 768, OPENGL);
    }

    public void draw() {
        background(255);
        draw(g);
    }

    public void keyPressed() {
        SunflowRenderer.render(this, "draw");
        System.out.println("");
    }

    public void draw(PGraphics pG) {
        // Adjust perspective
        pG.noStroke();
        pG.translate(pG.width / 2, pG.height / 2, 100);
        pG.rotateX(PGraphics.QUARTER_PI);
        pG.rotateZ(PGraphics.QUARTER_PI);
        pG.translate(-pG.width / 2, -pG.height / 2);

        // Draw some boxes
        float boxSize = pG.width / 11;
        pG.translate(boxSize, boxSize);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                pG.pushMatrix();
                pG.translate(j * boxSize, j * boxSize);
                pG.fill(j * 50);
                pG.sphere(boxSize);
                pG.popMatrix();
            }
            pG.translate(boxSize * 2, 0);
        }

        pG.stroke(0);
        for (int i = 0; i < 50; i++) {
            pG.line(-random(width), -random(width), -random(width),
                    random(width), random(width), random(width));
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchRenderWithSunflow.class.getName()});
    }
}
