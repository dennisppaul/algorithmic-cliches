package de.hfkbremen.creatingprocessingfinding.exporting;


import mathematik.Vector3f;
import processing.core.PApplet;
import processing.core.PGraphics;


public class SketchRenderAsCUSTOM
        extends PApplet {

    private boolean mRecord;

    public static final int ARRIVED_AT_POSITION = 1;

    public static final int NOT_ARRIVED_YET = 2;

    public void setup() {
        size(1024, 768, OPENGL);
        mRecord = false;
    }

    int checkMouse() {
        return ARRIVED_AT_POSITION;
    }

    public void draw() {

        if (mRecord) {
            beginRaw(CustomExporter.class.getName(), "output-2.dxf");
        } else {
        }

        draw(g);

        if (mRecord) {
            endRaw();
            mRecord = false;
        }
    }

    public void keyPressed() {
        if (key == 'r') {
            mRecord = true;
        }
    }

    private void draw(PGraphics pG) {
        for (int i = 0; i < 10; i++) {
            pG.noFill();
            pG.stroke(0);
            pG.line(0, i * 30, 0, width, i * 30, 0);
        }

//        pG.beginShape(TRIANGLES);
//        pG.vertex(0, 0, 0);
//        pG.vertex(width, height, 0);
//        pG.vertex(0, height, 0);
//        pG.endShape();

//        pG.background(255);
//        pG.translate(width / 2, height / 2);
//
//        pG.fill(0);
//        pG.noStroke();
//        pG.sphere(100);
//
//        pG.stroke(0);
//        pG.noFill();
//        for (int i = 0; i < 100; i++) {
//            pG.line(0, 0, 0,
//                    random(width / -2, width / 2),
//                    random(height / -2, height / 2),
//                    random(height / -2, height / 2));
//        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchRenderAsCUSTOM.class.getName()});
    }
}
