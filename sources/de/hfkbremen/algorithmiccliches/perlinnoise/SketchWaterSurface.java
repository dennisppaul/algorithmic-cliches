package de.hfkbremen.algorithmiccliches.perlinnoise;


import de.hfkbremen.algorithmiccliches.util.ArcBall;
import processing.core.PApplet;


public class SketchWaterSurface
        extends PApplet {

    private float[][] mWaterSurface;

    private final int mGridScale = 16;

    private float mWaveOffset;

    public void setup() {
        size(1024, 768, OPENGL);
        new ArcBall(this);
        smooth();

        final int GRID_WIDTH = width / mGridScale;
        final int GRID_HEIGHT = height / mGridScale;
        mWaterSurface = new float[GRID_WIDTH][GRID_HEIGHT];
    }

    public void draw() {

        final float mFrameRate = 1.0f / frameRate;
        mWaveOffset += mFrameRate;

        for (int x = 0; x < mWaterSurface.length; x++) {
            for (int y = 0; y < mWaterSurface[x].length; y++) {
                final float mWaveScale = 5.0f;
                final float mNormalizedX = (float) x / (float) mWaterSurface.length * mWaveScale;
                final float mNormalizedY = (float) y / (float) mWaterSurface[x].length * mWaveScale;
                final float mWaveOffsetScale = 0.5f;
                final float mX = mNormalizedX + mWaveOffset * mWaveOffsetScale;
                final float mY = mNormalizedY;
                final float mHeightScale = 100.0f;
                mWaterSurface[x][y] = noise(mX, mY) * mHeightScale;
            }
        }

        /* draw */
        background(255);
        directionalLight(126, 126, 126, 0, 0, -1);
        ambientLight(102, 102, 102);

        pushMatrix();
        translate(0, 0, -height / 2);
        noStroke();
        fill(255, 127, 0);
        rect(0, 0, width, height);

        stroke(15, 143, 255);
        fill(0, 127, 255);
        pushMatrix();
        scale(mGridScale, mGridScale, 1);
        for (int x = 0; x < mWaterSurface.length - 1; x++) {
            for (int y = 0; y < mWaterSurface[x].length - 1; y++) {
                pushMatrix();
                translate(x, y);
                beginShape(QUADS);
                vertex(0, 0, mWaterSurface[x][y]);
                vertex(1, 0, mWaterSurface[x + 1][y]);
                vertex(1, 1, mWaterSurface[x + 1][y + 1]);
                vertex(0, 1, mWaterSurface[x][y + 1]);
                endShape();
                popMatrix();
            }
        }
        popMatrix();
        popMatrix();
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchWaterSurface.class.getName()});
    }
}
