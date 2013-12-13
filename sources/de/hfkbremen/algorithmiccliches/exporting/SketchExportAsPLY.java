package de.hfkbremen.algorithmiccliches.exporting;


import mathematik.Vector3f;
import processing.core.PApplet;
import processing.core.PGraphics;


public class SketchExportAsPLY
        extends PApplet {

    private boolean mRecord;

    public static final int ARRIVED_AT_POSITION = 1;

    public static final int NOT_ARRIVED_YET = 2;

    public void setup() {
        size(1024, 768, OPENGL);
        mRecord = false;
    }

    public void draw() {
        if (mRecord) {
            PLYExporter.VERTEX_SCALE = 0.001f;
            beginRaw(PLYExporter.class.getName(), "output.ply");
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
        pG.background(255);

        for (int i = 0; i < 10; i++) {
            pG.strokeWeight(i * 3);
            pG.stroke(random(255), random(255, 127), random(0, 127));
            pG.line(0, i * height / 10.0f, 0,
                    width, i * height / 10.0f, 0);
        }

        pG.translate(width / 2, height / 2);

        pG.fill(0);
        pG.noStroke();
        pG.sphere(50);

        pG.noFill();
        final float mRadius = 400;
        for (int i = 0; i < 250; i++) {
            Vector3f v = new Vector3f();
            v.randomize();
            v.scale(mRadius);
            pG.strokeWeight(random(0.1f, 2.0f));
            pG.stroke(random(255), random(255, 127), random(0, 127));
            pG.line(0, 0, 0, v.x, v.y, v.z);
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchExportAsPLY.class.getName()});
    }
}
