package de.hfkbremen.algorithmiccliches.examples;

import de.hfkbremen.algorithmiccliches.isosurface.MetaBall;
import de.hfkbremen.algorithmiccliches.isosurface.MetaBallManager;
import de.hfkbremen.algorithmiccliches.util.ACArcBall;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class SketchIsoSurface3MetaBall extends PApplet {
    /*
     * http://en.wikipedia.org/wiki/Marching_cubes
     */

    private MetaBallManager mMetaBallManager;
    private int mCurrentCircle = 0;
    private ACArcBall mArcBall;

    public void settings() {
        size(1024, 768, P3D);
    }

    public void setup() {
        textFont(createFont("Courier", 11));

        mArcBall = new ACArcBall(width / 2.0f, height / 2.0f, 0, 400.0f, this, true);

        mMetaBallManager = new MetaBallManager();
        mMetaBallManager.dimension.set(width, height, height);
        mMetaBallManager.resolution.set(30, 30, 30);
        mMetaBallManager.position.set(width / -2.0f, height / -2.0f, height / -2.0f);

        mMetaBallManager.add(new MetaBall(new PVector(0, 0, 0), 1, 100));
    }

    public void draw() {
        background(255);
        directionalLight(126, 126, 126, 0, 0, -1);
        ambientLight(102, 102, 102);

        /* draw extra info */
        fill(0);
        noStroke();
        text("ISO VALUE : " + mMetaBallManager.isovalue(), 10, 12);
        text("SELECTED  : " + mCurrentCircle, 10, 24);
        text("FPS       : " + (int) frameRate, 10, 36);

        /* draw iso surface */
        mArcBall.update();

        if (!mMetaBallManager.metaballs().isEmpty()) {
            mMetaBallManager.metaballs().get(mCurrentCircle).position.x = mouseX - width / 2.0f;
            mMetaBallManager.metaballs().get(mCurrentCircle).position.y = mouseY - height / 2.0f;
        }

        final ArrayList<PVector> myData = mMetaBallManager.createSurface();

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
                mMetaBallManager.isovalue(mMetaBallManager.isovalue() + 0.01f);
                break;
            case '-':
                mMetaBallManager.isovalue(mMetaBallManager.isovalue() - 0.01f);
                break;
            case ' ':
                mCurrentCircle++;
                mCurrentCircle %= mMetaBallManager.metaballs().size();
                break;
            case 'c':
                mMetaBallManager.add(new MetaBall(new PVector(mouseX - width / 2.0f,
                                                              mouseY - height / 2.0f,
                                                              random(-100, 100)), random(1, 2), random(50, 150)));
                break;
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchIsoSurface3MetaBall.class.getName()});
    }
}
