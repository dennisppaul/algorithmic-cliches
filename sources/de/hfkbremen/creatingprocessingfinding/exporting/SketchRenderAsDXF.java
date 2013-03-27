package de.hfkbremen.creatingprocessingfinding.exporting;


import processing.core.PApplet;
import processing.core.PGraphics;


public class SketchRenderAsDXF
        extends PApplet {

    boolean mRecord;

    public void setup() {
        size(1024, 768, OPENGL);
    }

    public void draw() {
        if (mRecord) {
            beginRaw(DXF, "output.dxf");
        }

        draw(g);

        if (mRecord) {
            endRaw();
            mRecord = false;
        }
    }

    private void draw(PGraphics pG) {
        pG.sphere(100);
    }

    public void keyPressed() {
        if (key == 'r') {
            mRecord = true;
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchRenderAsDXF.class.getName()});
    }
}
