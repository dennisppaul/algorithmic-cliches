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
import teilchen.force.flowfield.*; 
import teilchen.force.vectorfield.*; 
import de.hfkbremen.gewebe.*; 
import ddf.minim.*; 
import ddf.minim.analysis.*; 
import quickhull3d.*; 


/*
 * Applet display interface for fluid solver.
 *
 * @author Alexander McKenzie
 * @version 1.0
 */

// frame dimensions (dxd pixels)
final int d = 60 * 10;
final FluidDynamicsBuoyancyVorticity fs = new FluidDynamicsBuoyancyVorticity();
// solver variables
int n = 60;
float dt = 0.2f;
// flag to display velocity field
boolean vKey = false;

// cell dimensions
int dg, dg_2;

void settings() {
    size(d, d);
}

void setup() {
    reset();
    frameRate(240);
}

void draw() {
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
        // cell position
        int dx = (int) ((k - 0.5f) * dg);
        for (int l = 1; l <= n; l++) {
            // y position of current cell
            int dy = (int) ((l - 0.5f) * dg);

            // draw density
            if (fs.d[I(k, l)] > 0) {
                int c = (int) ((1.0 - fs.d[I(k, l)]) * 255);
                if (c < 0) {
                    c = 0;
                }
                fill(c);
                noStroke();
                rect(dx - dg_2, dy - dg_2, dg, dg);
            }

            // draw velocity field
            if (vKey) { // && i % 5 == 1 && j % 5 == 1) {
                // fluid velocity
                int u = (int) (50.0f * fs.u[I(k, l)]);
                int v = (int) (50.0f * fs.v[I(k, l)]);
                stroke(255, 0, 0);
                line(dx, dy, dx + u, dy + v);
            }
        }
    }
}

void keyPressed() {
    // set flag for drawing velocity field
    if (key == 'v') {
        vKey = !vKey;
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

void reset() {
    // calculate cell deimensions
    dg = d / n;
    dg_2 = dg / 2;

    fs.setup(n, dt);
}

void updateLocation() {
    // get index for fluid cell under mouse position
    // cell index
    int i = (int) ((mouseX / (float) d) * n + 1);
    int j = (int) ((mouseY / (float) d) * n + 1);

    // set boundaries
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
int I(int i, int j) {
    return i + (n + 2) * j;
}

