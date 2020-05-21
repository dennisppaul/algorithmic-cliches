package de.hfkbremen.algorithmiccliches.examples;

import de.hfkbremen.algorithmiccliches.isosurface.marchingcubes.Metaball;
import de.hfkbremen.algorithmiccliches.isosurface.marchingcubes.MetaballManager;
import de.hfkbremen.algorithmiccliches.util.ArcBall;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class SketchIsoSurface3 extends PApplet {
    /*
     * http://en.wikipedia.org/wiki/Marching_cubes
     */

    private MetaballManager mMetaballManager;
    private int mCurrentCircle = 0;
    private ArcBall mArcBall;

    public void settings() {
        size(1024, 768, P3D);
    }

    public void setup() {
        textFont(createFont("Courier", 11));

        mArcBall = new ArcBall(width / 2.0f, height / 2.0f, 0, 400.0f, this, true);

        mMetaballManager = new MetaballManager();
        mMetaballManager.dimension.set(width, height, height);
        mMetaballManager.resolution.set(30, 30, 30);
        mMetaballManager.position.set(width / -2.0f, height / -2.0f, height / -2.0f);

        mMetaballManager.add(new Metaball(new PVector(0, 0, 0), 1, 100));
    }

    public void draw() {
        background(255);
        directionalLight(126, 126, 126, 0, 0, -1);
        ambientLight(102, 102, 102);

        /* draw extra info */
        fill(0);
        noStroke();
        text("ISO VALUE : " + mMetaballManager.isovalue(), 10, 12);
        text("SELECTED  : " + mCurrentCircle, 10, 24);
        text("FPS       : " + (int) frameRate, 10, 36);

        /* draw iso surface */
        mArcBall.update();

        if (!mMetaballManager.metaballs().isEmpty()) {
            mMetaballManager.metaballs().get(mCurrentCircle).position.x = mouseX - width / 2.0f;
            mMetaballManager.metaballs().get(mCurrentCircle).position.y = mouseY - height / 2.0f;
        }

        final ArrayList<PVector> myData = mMetaballManager.createSurface();

        /* draw metaballs */
        translate(width / 2.0f, height / 2.0f);
        fill(255, 127, 0);
        noStroke();
        beginShape(TRIANGLES);
        for (PVector myDatum : myData) {
            vertex(myDatum.x, myDatum.y, myDatum.z);
        }
        endShape();
    }

    public void keyPressed() {
        switch (key) {
            case '+':
                mMetaballManager.isovalue(mMetaballManager.isovalue() + 0.01f);
                break;
            case '-':
                mMetaballManager.isovalue(mMetaballManager.isovalue() - 0.01f);
                break;
            case ' ':
                mCurrentCircle++;
                mCurrentCircle %= mMetaballManager.metaballs().size();
                break;
            case 'c':
                mMetaballManager.add(new Metaball(new PVector(mouseX - width / 2.0f,
                                                              mouseY - height / 2.0f,
                                                              random(-100, 100)), random(1, 2), random(50, 150)));
                break;
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchIsoSurface3.class.getName()});
    }
}
