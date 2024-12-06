package de.hfkbremen.algorithmiccliches.examples;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;
import teilchen.util.Overlap;
import teilchen.util.SpatialEntity;

import java.util.ArrayList;

public class SketchFlowFields extends PApplet {

    private final ArrayList<FlowFieldParticle> mParticles = new ArrayList<>();
    private       float                        mOffset    = 10;
    private       FlowField                    mFlowField;

    public void settings() {
        size(1024, 768, P3D);
    }

    public void setup() {
        frameRate(120);
        noiseSeed(System.currentTimeMillis());
        mFlowField = new FlowField(32);
        mFlowField.populateVectorField(mOffset);

        for (int i = 0; i < 2500; i++) {
            FlowFieldParticle p = new FlowFieldParticle(mFlowField);
            p.position.set(random(width), random(height));
            p.speed = random(5, 50);
            mParticles.add(p);
        }
    }

    public void draw() {
        float mDeltaTime = 1.0f / frameRate;
        mFlowField.populateVectorField(mOffset += mDeltaTime * 0.1f);

        background(255);
        noFill();
        stroke(0, 31);
        mFlowField.draw(g);

        /* move particle in flow field */
        for (FlowFieldParticle p : mParticles) {
            /* teleport particle to screen */
            final float mPadding = 2;
            p.teleport(mPadding, width - mPadding, mPadding, height - mPadding);
            p.move(mDeltaTime);
        }
        Overlap.resolveOverlap(mParticles);

        /* draw particles */
        stroke(255, 127, 0, 127);
        for (FlowFieldParticle p : mParticles) {
            p.draw(g);
        }
    }

    class FlowField {

        private final int         CELL_SIZE;
        private final PVector[][] mField;

        FlowField(int pCellSize) {
            CELL_SIZE = pCellSize;
            mField    = new PVector[width / CELL_SIZE][height / CELL_SIZE];
        }

        PVector[][] field() {
            return mField;
        }

        int cell_size() {
            return CELL_SIZE;
        }

        void draw(PGraphics g) {
            for (int x = 0; x < mField.length; x++) {
                for (int y = 0; y < mField[x].length; y++) {
                    PVector v = mField[x][y];
                    g.pushMatrix();
                    g.translate(x * CELL_SIZE, y * CELL_SIZE);
                    g.rect(0, 0, CELL_SIZE, CELL_SIZE);
                    g.translate(CELL_SIZE / 2.0f, CELL_SIZE / 2.0f);
                    g.line(0, 0, v.x, v.y);
                    g.popMatrix();
                }
            }
        }

        void populateVectorField(float pOffset) {
            for (int x = 0; x < mField.length; x++) {
                for (int y = 0; y < mField[x].length; y++) {
                    mField[x][y] = new PVector();
                    final float mAngle = noise(x * 0.02f + pOffset, y * 0.033f + pOffset * 0.13f) * TWO_PI * 2;
                    mField[x][y].set(sin(mAngle), cos(mAngle));
                    final float mScale = noise(x * 0.013f + pOffset, y * 0.005f + pOffset) * 20;
                    mField[x][y].mult(mScale + 1);
                }
            }
        }
    }

    class FlowFieldParticle implements SpatialEntity {

        final PVector   position = new PVector();
        final FlowField mFlowField;
        float speed = 7;

        FlowFieldParticle(FlowField pFlowField) {
            mFlowField = pFlowField;
        }

        public void teleport(float x_min, float x_max, float y_min, float y_max) {
            if (position.x > x_max) {
                position.x = x_min;
            } else if (position.x < x_min) {
                position.x = x_max;
            }
            if (position.y > y_max) {
                position.y = y_min;
            } else if (position.y < y_min) {
                position.y = y_max;
            }
        }

        public void move(float pDeltaTime) {
            /* find position in flow field */
            int     x = (int) (position.x / mFlowField.cell_size());
            int     y = (int) (position.y / mFlowField.cell_size());
            PVector v = mFlowField.field()[x][y];
            /* add a fraction of flow field vector to particle position */
            position.add(PVector.mult(v, pDeltaTime * speed));
        }

        public void draw(PGraphics g) {
            final float mSize = 2;
            g.line(position.x - mSize, position.y - mSize, position.x + mSize, position.y + mSize);
            g.line(position.x + mSize, position.y - mSize, position.x - mSize, position.y + mSize);
        }

        @Override
        public float radius() {
            return 2;
        }

        @Override
        public PVector position() {
            return position;
        }
    }

    public static void main(String[] args) {
        PApplet.main(SketchFlowFields.class.getName());
    }
}
