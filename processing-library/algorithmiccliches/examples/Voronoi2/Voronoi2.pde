import mathematik.*;
import oscP5.*;
import netP5.*;
import java.util.Vector;
import de.hfkbremen.algorithmiccliches.voronoidiagram.Qvoronoi;
import java.util.Vector;
import mathematik.Vector3f;
/**
 * http://en.wikipedia.org/wiki/Voronoi_diagram
 */
Vector3f[][] mRegions;

final Qvoronoi mQvoronoi = new Qvoronoi();

final Vector<Vector3f> mPoints = new Vector<Vector3f>();

int mCurrentRegion;

void settings() {
    size(1024, 768, P3D);
}

void setup() {
    smooth();

    final int NUMBER_OF_POINTS_ON_CIRLCE = 20;
    for (int i = 0; i < NUMBER_OF_POINTS_ON_CIRLCE; i++) {
        final float r = (float) i / NUMBER_OF_POINTS_ON_CIRLCE * TWO_PI;
        final float x = sin(r) * 50 + width / 2;
        final float y = cos(r) * 50 + height / 2;
        addPoint(x, y);
    }
    for (int i = 0; i < NUMBER_OF_POINTS_ON_CIRLCE; i++) {
        final float r = (float) i / NUMBER_OF_POINTS_ON_CIRLCE * TWO_PI + 0.3f;
        final float x = sin(r) * 100 + width / 2;
        final float y = cos(r) * 100 + height / 2;
        addPoint(x, y);
    }
    for (int i = 0; i < NUMBER_OF_POINTS_ON_CIRLCE; i++) {
        final float r = (float) i / NUMBER_OF_POINTS_ON_CIRLCE * TWO_PI + 1.1f;
        final float x = sin(r) * 150 + width / 2;
        final float y = cos(r) * 150 + height / 2;
        addPoint(x, y);
    }

    addPoint(width / 2, height / 2);
}

void addPoint(float x, float y) {
    mCurrentRegion = 0;
    mPoints.add(new Vector3f(x, y));
}

void draw() {
    Vector3f[] mGridPointsArray = new Vector3f[mPoints.size()];
    mPoints.toArray(mGridPointsArray);
    mRegions = mQvoronoi.calculate2(mGridPointsArray);

    mPoints.lastElement().set(mouseX, mouseY);

    if (mousePressed) {
        addPoint(mouseX, mouseY);
    }

    /* setup scene */
    background(255);

    /* draw regions */
    if (mRegions != null) {
        for (Vector3f[] mRegion : mRegions) {
            stroke(255, 223, 192);
            noFill();
            drawRegion(mRegion);
        }

        /* draw selected region */
        if (mRegions.length > 0) {
            noStroke();
            fill(255, 127, 0);
            drawRegion(mRegions[mCurrentRegion]);
        }
    }

    /* draw points */
    stroke(255, 0, 0, 127);
    for (int i = 0; i < mPoints.size(); i++) {
        Vector3f v = mPoints.get(i);
        drawCross(v);
    }
}

void drawCross(Vector3f v) {
    final float o = 2.0f;
    line(v.x - o, v.y, v.z, v.x + o, v.y, v.z);
    line(v.x, v.y - o, v.z, v.x, v.y + o, v.z);
    line(v.x, v.y, v.z - o, v.x, v.y, v.z + o);
}

void drawRegion(Vector3f[] pVertex) {
    beginShape();
    for (Vector3f v : pVertex) {
        vertex(v.x, v.y, v.z);
    }
    endShape(CLOSE);
}

void keyPressed() {
    mCurrentRegion++;
    mCurrentRegion %= mRegions.length;
}

void mousePressed() {
    addPoint(mouseX, mouseY);
}
