package de.hfkbremen.algorithmiccliches.exporting;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.pdf.PGraphicsPDF;

public class SketchExportAsPDF
        extends PApplet {

    PGraphicsPDF pdf;

    public void settings() {
        size(1024, 768, P3D);
    }

    public void setup() {
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
        PApplet.main(new String[]{SketchExportAsPDF.class.getName()});
    }
}
