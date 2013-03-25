package de.hfkbremen.creatingprocessingfinding.cellularautomata;


import processing.core.PGraphics;


public class CellularAutomaton2 {

    private final int mGridX;

    private final int mGridY;

    private final boolean[][][] mCells;

    private static final int GRID_WRITE = 0;

    private static final int GRID_READ = 1;

    public CellularAutomaton2(int pGridX, int pGridY) {
        mGridX = pGridX;
        mGridY = pGridY;
        mCells = new boolean[mGridX][mGridY][2];
    }

    public void randomizeCells(float pDistribution) {
        for (int x = 0; x < mGridX; x++) {
            for (int y = 0; y < mGridY; y++) {
                mCells[x][y][GRID_READ] = Math.random() > pDistribution;
                mCells[x][y][GRID_WRITE] = mCells[x][y][GRID_READ];
            }
        }
    }

    void draw(PGraphics g) {
        g.pushMatrix();
        g.translate(mGridX / -2.0f, mGridY / -2.0f);
        for (int x = 0; x < mGridX; x++) {
            for (int y = 0; y < mGridY; y++) {
                if (mCells[x][y][GRID_READ]) {
                    g.pushMatrix();
                    g.translate(x, y);
                    g.rect(0, 0, 1, 1);
                    g.popMatrix();
                }
            }
        }
        g.popMatrix();
    }

    public void update() {
        /* this is a classic 'working' parameter-set: B3/S23
         * birth with 3 neighbors, die with less then 2 or more than 3 neighbors */
        update(3, 2, 3);
    }

    public void update(int pBirth, int pMinSurvive, int pMaxSurvive) {
        evaluateCells(pBirth, pMinSurvive, pMaxSurvive);
        copyCellStates();
    }

    private void evaluateCells(int pBirth, int pMinSurvive, int pMaxSurvive) {
        for (int x = 0; x < mGridX; x++) {
            for (int y = 0; y < mGridY; y++) {
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
        for (int x = 0; x < mGridX; x++) {
            for (int y = 0; y < mGridY; y++) {
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
                final int mX = (pX + x + mGridX) % mGridX;
                final int mY = (pY + y + mGridY) % mGridY;
                if (mCells[mX][mY][GRID_READ]) {
                    mNeighbors++;
                }
            }
        }
        return mNeighbors;
    }
}
