package de.hfkbremen.algorithmiccliches.additional.exporting;

import processing.core.PApplet;
import processing.dxf.RawDXF;

public class SketchExportAsDXF
        extends PApplet {

    private boolean mRecord;

    public void setup() {
        size(1024, 768, OPENGL);
        mRecord = false;
    }

    public void draw() {
        if (mRecord) {
            beginRaw(RawDXF.class.getName(), "output.dxf");
        }

        background(255);
        noStroke();
        fill(255, 0, 0);
        translate(width / 2, height / 2);
        sphere(100);

        stroke(0);
        noFill();
        for (int i = 0; i < 100; i++) {
            line(0, 0, 0,
                    random(width / -2, width / 2),
                    random(height / -2, height / 2),
                    random(height / -2, height / 2));
        }

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

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchExportAsDXF.class.getName()});
    }
}
