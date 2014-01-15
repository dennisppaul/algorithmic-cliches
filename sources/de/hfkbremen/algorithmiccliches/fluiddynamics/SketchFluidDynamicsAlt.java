package de.hfkbremen.algorithmiccliches.fluiddynamics;


import processing.core.PApplet;


/**
 * Applet display interface for fluid solver.
 *
 * @author Alexander McKenzie
 * @version 1.0
 *
 */
public class SketchFluidDynamicsAlt
        extends PApplet {

    // frame dimensions (dxd pixels)
    private final int d = 60 * 10;

    // solver variables
    private int n = 60;

    private float dt = 0.2f;

    private final FluidDynamicsAlt fs = new FluidDynamicsAlt();

    // flag to display velocity field
    private boolean vkey = false;

    // cell index
    private int i, j;

    // cell dimensions
    private int dg, dg_2;

    // cell position
    private int dx, dy;

    // fluid velocity
    private int u, v;

    private int c;

    public void setup() {
        size(d, d, OPENGL);
        reset();
        frameRate(240);
    }

    public void reset() {
        // calculate cell deimensions
        dg = d / n;
        dg_2 = dg / 2;

        fs.setup(n, dt);
    }

    public void draw() {

        println(frameRate);

        background(255);
        noStroke();

        if (mousePressed) {
            updateLocation();
        }

        // solve fluid
//        for (int i = 0; i < 5; i++) {
        fs.velocitySolver();
        fs.densitySolver();
//        }

        for (int k = 1; k <= n; k++) {
            // x position of current cell
            dx = (int) ((k - 0.5f) * dg);
            for (int l = 1; l <= n; l++) {
                // y position of current cell
                dy = (int) ((l - 0.5f) * dg);

                // draw density
                if (fs.d[I(k, l)] > 0) {
                    c = (int) ((1.0 - fs.d[I(k, l)]) * 255);
                    if (c < 0) {
                        c = 0;
                    }
                    fill(c);
                    noStroke();
                    rect(dx - dg_2, dy - dg_2, dg, dg);
                }

                // draw velocity field
                if (vkey) { // && i % 5 == 1 && j % 5 == 1) {
                    u = (int) (50.0f * fs.u[I(k, l)]);
                    v = (int) (50.0f * fs.v[I(k, l)]);
                    stroke(255, 0, 0);
                    line(dx, dy, dx + u, dy + v);
                }
            }
        }
    }

    public void keyPressed() {
        // set flag for drawing velocity field
        if (key == 'v') {
            vkey = !vkey;
        }

        // reset solver
        if (key == 'r') {
            fs.reset();
        }

        // increase fluid grid size and reset applet
        if (key == ']') {
            if (n == d) {
                return;
            }

            // calculate next ideal grid size
            int k = n + 1;
            while (d % k != 0) {
                k++;
            }
            n = k;

            reset();
        }

        // reduce fluid grid size and reset applet
        if (key == '[') {
            if (n < 10) {
                return;
            }

            // calculate previous ideal grid size
            int k = n - 1;
            while (d % k != 0) {
                k--;
            }
            n = k;

            reset();
        }

        // increase timestep
        if (key == '.') {
            if (dt > 1) {
                return;
            }

            dt += 0.05f;

            // kill fp errors
            dt = (float) Math.round(dt * 100);
            dt /= 100;

            fs.dt = dt;
        }

        // reduce timestep
        if (key == ',') {
            if (dt < 0.1f) {
                return;
            }

            dt -= 0.05f;

            // kill fp errors
            dt = (float) Math.round(dt * 100);
            dt /= 100;

            fs.dt = dt;
        }
    }

    public void updateLocation() {
        // get index for fluid cell under mouse position
        i = (int) ((mouseX / (float) d) * n + 1);
        j = (int) ((mouseY / (float) d) * n + 1);

        // set boundries
        if (i > n) {
            i = n;
        }
        if (i < 1) {
            i = 1;
        }
        if (j > n) {
            j = n;
        }
        if (j < 1) {
            j = 1;
        }

        // add density or velocity
        if (mouseButton == LEFT) {
            fs.dOld[I(i, j)] = 100;
        } else {
            fs.uOld[I(i, j)] = (mouseX - pmouseX) * 5;
            fs.vOld[I(i, j)] = (mouseY - pmouseY) * 5;
        }
    }

    // util function for indexing
    private int I(int i, int j) {
        return i + (n + 2) * j;
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchFluidDynamicsAlt.class.getName()});
    }
}
