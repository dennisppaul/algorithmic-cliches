			 import de.hfkbremen.algorithmiccliches.*; 
import de.hfkbremen.algorithmiccliches.agents.*; 
import de.hfkbremen.algorithmiccliches.cellularautomata.*; 
import de.hfkbremen.algorithmiccliches.convexhull.*; 
import de.hfkbremen.algorithmiccliches.delaunaytriangulation2.*; 
import de.hfkbremen.algorithmiccliches.fluiddynamics.*; 
import de.hfkbremen.algorithmiccliches.isosurface.*; 
import de.hfkbremen.algorithmiccliches.laserline.*; 
import de.hfkbremen.algorithmiccliches.lindenmayersystems.*; 
import de.hfkbremen.algorithmiccliches.octree.*; 
import de.hfkbremen.algorithmiccliches.util.*; 
import de.hfkbremen.algorithmiccliches.voronoidiagram.*; 
import teilchen.*; 
import teilchen.behavior.*; 
import teilchen.constraint.*; 
import teilchen.cubicle.*; 
import teilchen.integration.*; 
import teilchen.util.*; 
import teilchen.force.*; 
import gewebe.*; 
import ddf.minim.*; 
import ddf.minim.analysis.*; 
import quickhull3d.*; 

			 
		float mIsoValue = 32.0f;
MetaCircle[] mMetaCircles;
void settings() {
    size(1024, 768, P3D);
}
void setup() {
    textFont(createFont("Courier", 11));
    mMetaCircles = new MetaCircle[30];
    for (int i = 0; i < mMetaCircles.length; i++) {
        mMetaCircles[i] = new MetaCircle();
        mMetaCircles[i].position().set(random(0, width), random(0, height));
        mMetaCircles[i].strength(random(5000, 50000));
    }
}
void draw() {
    background(255);
    /* drag circle  */
    mMetaCircles[0].position().set(mouseX, mouseY);
    /* populate field */
    final int mSquareSizeX = 8;
    final int mSquareSizeY = 8;
    final int mNumberOfSquaresX = width / mSquareSizeX;
    final int mNumberOfSquaresY = height / mSquareSizeY;
    float[][] mEnergyGrid = new float[mNumberOfSquaresX][mNumberOfSquaresY];
    for (int x = 0; x < mEnergyGrid.length; x++) {
        for (int y = 0; y < mEnergyGrid[x].length; y++) {
            mEnergyGrid[x][y] = 0;
            for (MetaCircle mMetaCircle : mMetaCircles) {
                mEnergyGrid[x][y] += mMetaCircle.getStrengthAt(mSquareSizeX * x, mSquareSizeY * y);
            }
        }
    }
    /* draw lines */
    final ArrayList<Linef> mLines = IsoSurface2.getLines(mEnergyGrid, mIsoValue);
    stroke(0, 127, 255);
    beginShape(LINES);
    for (Linef myLine : mLines) {
        vertex(myLine.p1.x * mSquareSizeX, myLine.p1.y * mSquareSizeY);
        vertex(myLine.p2.x * mSquareSizeX, myLine.p2.y * mSquareSizeY);
    }
    endShape();
    /* draw blobs */
    ArrayList<ArrayList<PVector>> mBlobShapes = IsoSurface2.extractBlobs(mLines);
    noStroke();
    fill(0, 127, 255, 127);
    for (ArrayList<PVector> mBlobShape : mBlobShapes) {
        beginShape();
        for (PVector p : mBlobShape) {
            vertex(p.x * mSquareSizeX, p.y * mSquareSizeY);
        }
        endShape();
    }
    /* draw extra info */
    drawGrid(mSquareSizeX, mSquareSizeY);
    drawMetaCenter();
    fill(0);
    noStroke();
    text("ISO VALUE   : " + mIsoValue, 10, 12);
    text("#BLOB_SHAPE : " + mBlobShapes.size(), 10, 24);
    text("FPS         : " + (int) frameRate, 10, 36);
}
void keyPressed() {
    switch (key) {
        case '+':
            mIsoValue++;
            break;
        case '-':
            mIsoValue--;
            break;
    }
}
void drawGrid(int RES_X, int RES_Y) {
    final int SQUARES_NX = width / RES_X;
    final int SQUARES_NY = height / RES_X;
    stroke(0, 16);
    for (int y = 0; y < SQUARES_NX + 1; y++) {
        int xp = y * RES_X;
        line(xp, 0, xp, height);
    }
    for (int x = 0; x < SQUARES_NY + 1; x++) {
        int yp = x * RES_Y;
        line(0, yp, width, yp);
    }
}
void drawMetaCenter() {
    stroke(0, 127, 255, 192);
    beginShape(LINES);
    for (MetaCircle mMetaCircle : mMetaCircles) {
        final float mLength = 2.0f;
        final PVector p = mMetaCircle.position();
        vertex(p.x + mLength, p.y + mLength);
        vertex(p.x - mLength, p.y - mLength);
        vertex(p.x + mLength, p.y - mLength);
        vertex(p.x - mLength, p.y + mLength);
    }
    endShape();
}
