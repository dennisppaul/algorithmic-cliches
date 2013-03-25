package de.hfkbremen.creatingprocessingfinding.exporting;


import processing.core.PApplet;
import processing.core.PGraphics;
import processing.pdf.PGraphicsPDF;

import static processing.core.PConstants.OPENGL;


public class SketchRenderAsPDF
        extends PApplet {

    PGraphicsPDF pdf;

    public void setup() {
        size(1024, 768, OPENGL);
        pdf = (PGraphicsPDF) createGraphics(width, height, PDF, "export.pdf");
        beginRecord(pdf);
        background(255);
    }

    public void draw() {
        background(255);
        draw(pdf);
        draw(g);
    }

    public void draw(PGraphics pG) {
        pG.line(pmouseX, pmouseY, mouseX, mouseY);
    }

    public void keyPressed() {
        if (key == 'q') {
            endRecord();
            exit();
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchRenderAsPDF.class.getName()});
    }
}
