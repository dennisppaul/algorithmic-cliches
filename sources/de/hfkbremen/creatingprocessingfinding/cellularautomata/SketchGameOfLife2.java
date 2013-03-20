

package de.hfkbremen.creatingprocessingfinding.cellularautomata;


import processing.core.PApplet;


public class SketchGameOfLife2
        extends PApplet {

    private static final int GRID_X = 1024 / 8;

    private static final int GRID_Y = 768 / 8;

    private static final int GRID_WRITE = 0;

    private static final int GRID_READ = 1;

    private boolean[][][] mCells;


    public void setup() {
        size(1024, 768, OPENGL);
        rectMode(CENTER);
        textFont(createFont("Courier", 11));

        mCells = new boolean[GRID_X][GRID_Y][2];
        randomizeCells();
    }


    public void draw() {
        lights();
        background(255);

        /* evaluate cells */
        int mB = 3;
        int mSmin = 2;
        int mSmax = 3;
        evaluateCells(mB, mSmin, mSmax); // B3/S23

        /* copy to back */
        copyCellStates();

        /* draw grid */
        fill(127);
        noStroke();
        drawCells();

        /* draw info */
        fill(0);
        noStroke();
        text("RULE     : " + "B" + mB + "/S" + mSmin + "" + mSmax, 10, 12);
        text("FPS      : " + frameRate, 10, 24);
    }


    private void randomizeCells() {
        for (int x = 0; x < GRID_X; x++) {
            for (int y = 0; y < GRID_Y; y++) {
                mCells[x][y][GRID_READ] = random(1.0f) > 0.6f;
                mCells[x][y][GRID_WRITE] = mCells[x][y][GRID_READ];
            }
        }
    }


    private void drawCells() {
        pushMatrix();
        translate(width / 2, height / 2);
        scale(6);
        translate(GRID_X / -2, GRID_Y / -2);
        for (int x = 0; x < GRID_X; x++) {
            for (int y = 0; y < GRID_Y; y++) {
                if (mCells[x][y][GRID_READ]) {
                    pushMatrix();
                    translate(x, y);
                    rect(0, 0, 1, 1);
                    popMatrix();
                }
            }
        }
        popMatrix();
    }


    private void evaluateCells(int pBirth,
                               int pMinSurvive,
                               int pMaxSurvive) {
        for (int x = 0; x < GRID_X; x++) {
            for (int y = 0; y < GRID_Y; y++) {
                int mNeighbors = getNeighbors(x, y);
                boolean mCellState = mCells[x][y][GRID_READ];
                if (mCellState) {
                    if (mNeighbors < pMinSurvive || mNeighbors > pMaxSurvive) {
                        mCells[x][y][GRID_WRITE] = false;
                    }
                } else {
                    if (mNeighbors == pBirth) {
                        mCells[x][y][GRID_WRITE] = true;
                    }
                }
            }
        }
    }


    private void copyCellStates() {
        for (int x = 0; x < GRID_X; x++) {
            for (int y = 0; y < GRID_Y; y++) {
                mCells[x][y][GRID_READ] = mCells[x][y][GRID_WRITE];
            }
        }
    }


    private int getNeighbors(int pX, int pY) {
        int mNeighbors = 0;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x == 0 && y == 0) {
                    continue;
                }
                final int mX = (pX + x + GRID_X) % GRID_X;
                final int mY = (pY + y + GRID_Y) % GRID_Y;
                if (mCells[mX][mY][GRID_READ]) {
                    mNeighbors++;
                }
            }
        }
        return mNeighbors;
    }


    public void keyPressed() {
        randomizeCells();
    }


    public static void main(String[] args) {
        PApplet.main(new String[] {SketchGameOfLife2.class
            .getName()});
    }
}
