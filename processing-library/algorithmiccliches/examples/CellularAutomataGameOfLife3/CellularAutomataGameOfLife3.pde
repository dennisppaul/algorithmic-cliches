import oscP5.*;
import netP5.*;
import teilchen.util.*;
import de.hfkbremen.algorithmiccliches.util.ArcBall;
/**
 * http://en.wikipedia.org/wiki/Conway%27s_Game_of_Life
 */
static final int GRID_X = 64;

static final int GRID_Y = 32;

static final int GRID_Z = 32;

static final int GRID_WRITE = 0;

static final int GRID_READ = 1;

boolean[][][][] mCells;

int mB = 4;

int mSmin = 4;

int mSmax = 4;

ArcBall mArcBall;

void settings() {
    size(1024, 768, P3D);
}

void setup() {
    rectMode(CENTER);
    textFont(createFont("Courier", 11));
    mArcBall = new ArcBall(this, true);

    mCells = new boolean[GRID_X][GRID_Y][GRID_Z][2];
    randomizeCells();
}

void draw() {
    lights();
    background(255);

    /* draw info */
    fill(0);
    noStroke();
    text("RULE     : " + "B" + mB + "/S" + mSmin + "" + mSmax, 10, 12);
    text("FPS      : " + frameRate, 10, 24);

    mArcBall.update(mousePressed, mouseX, mouseY);

    /* evaluate cells */
    evaluateCells(mB, mSmin, mSmax); // B3/S23

    /* copy to back */
    copyCellStates();

    /* draw grid */
    fill(127);
    noStroke();
    drawCells();

}

void randomizeCells() {
    for (int x = 0; x < GRID_X; x++) {
        for (int y = 0; y < GRID_Y; y++) {
            for (int z = 0; z < GRID_Z; z++) {
                mCells[x][y][z][GRID_READ] = random(1.0f) > 0.4f;
                mCells[x][y][z][GRID_WRITE] = mCells[x][y][z][GRID_READ];
            }
        }
    }
}

void randomizeCells(int pCells) {
    for (int i = 0; i < pCells; i++) {
        int x = (int) random(0, GRID_X);
        int y = (int) random(0, GRID_Y);
        int z = (int) random(0, GRID_Z);
        mCells[x][y][z][GRID_READ] = true;
        mCells[x][y][z][GRID_WRITE] = true;
    }
}

void drawCells() {
    pushMatrix();
    translate(width / 2, height / 2, height / -2);
    scale(16);
    translate(GRID_X / -2, GRID_Y / -2, GRID_Z / -2);
    for (int x = 0; x < GRID_X; x++) {
        for (int y = 0; y < GRID_Y; y++) {
            for (int z = 0; z < GRID_Z; z++) {
                if (mCells[x][y][z][GRID_READ]) {
                    pushMatrix();
                    translate(x, y, z);
                    box(1);
                    popMatrix();
                }
            }
        }
    }
    popMatrix();
}

void evaluateCells(int pBirth,
        int pMinSurvive,
        int pMaxSurvive) {
    for (int x = 0; x < GRID_X; x++) {
        for (int y = 0; y < GRID_Y; y++) {
            for (int z = 0; z < GRID_Z; z++) {
                int mNeighbors = getNeighbors(x, y, z);
                boolean mCellState = mCells[x][y][z][GRID_READ];
                if (mCellState) {
                    if (mNeighbors < pMinSurvive || mNeighbors > pMaxSurvive) {
                        mCells[x][y][z][GRID_WRITE] = false;
                    }
                } else {
                    if (mNeighbors == pBirth) {
                        mCells[x][y][z][GRID_WRITE] = true;
                    }
                }
            }
        }
    }
}

void copyCellStates() {
    for (int x = 0; x < GRID_X; x++) {
        for (int y = 0; y < GRID_Y; y++) {
            for (int z = 0; z < GRID_Z; z++) {
                mCells[x][y][z][GRID_READ] = mCells[x][y][z][GRID_WRITE];
            }
        }
    }
}

int getNeighbors(int pX, int pY, int pZ) {
    int mNeighbors = 0;
    for (int x = -1; x <= 1; x++) {
        for (int y = -1; y <= 1; y++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 && y == 0 && z == 0) {
                    continue;
                }
                final int mX = (pX + x + GRID_X) % GRID_X;
                final int mY = (pY + y + GRID_Y) % GRID_Y;
                final int mZ = (pZ + z + GRID_Z) % GRID_Z;
                if (mCells[mX][mY][mZ][GRID_READ]) {
                    mNeighbors++;
                }
            }
        }
    }
    return mNeighbors;
}

void keyPressed() {
    switch (key) {
        case ' ': {
            randomizeCells(128 * 4);
        }
        break;
        case '+': {
            mB++;
        }
        break;
        case '-': {
            mB--;
        }
        break;
        case 'q': {
            mSmin++;
        }
        break;
        case 'a': {
            mSmin--;
        }
        break;
        case 'Q': {
            mSmax++;
        }
        break;
        case 'A': {
            mSmax--;
        }
        break;
    }
}
