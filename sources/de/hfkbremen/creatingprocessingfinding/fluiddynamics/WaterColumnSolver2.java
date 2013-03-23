

package de.hfkbremen.creatingprocessingfinding.fluiddynamics;


public class WaterColumnSolver2 {

    // Physical parameters important to the simulation
    public float FLUID_DENSITY = 0.1f; // The density of the fluid

    public float GRAVITATION = 1.0f; // Gravitational force

    public float PIPE_LENGTH = 5.0f; // Length of a virtual connection pipe

    public float PIPE_AREA = 0.0007f; // The cross-sectional area of a pipe

    public float DAMPING = 0.9997f; // Acceleration damping factor

    private final int GRID_X; // x grid resolution

    private final int GRID_Y; // y grid resolution

    private final float[][] mColumnVolumes; // Array of xres*yres columns, containing water volume

    private final float[][] mPipeLeftUp; // Left diagonal connection pipes

    private final float[][] mPipeUp; // Vertical connection pipes

    private final float[][] mPipeRightUp; // Right diagonal connection pipes

    private final float[][] mPipeRight; // Horizontcal connection pipes

    private float mIdealTotalVolume = 100f; // Ideal volume of the water body

    private float mTotalVolume;

    /*
     The ctor builds a water simulation object of the specified surface resolution (xsize, ysize),
     with an amplitude of (hscale). The user must specify a pointer to the volumemap, that will
     hold the result after a simulation step.
     */

    public WaterColumnSolver2(int xsize, int ysize) {
        // Set the water surface dimensions and area
        GRID_X = xsize;
        GRID_Y = ysize;

        // The column height points to the user supplied heightfield, but we also need to keep track
        // of the column volume. Get memory to store it.
        mColumnVolumes = new float[xsize][ysize];


        // Allocate memory for the grid of virtual pipes (four types: vertical, horizontal, left diagonal, right diagonal).
        mPipeUp = new float[GRID_X][GRID_Y];
        mPipeRight = new float[GRID_X][GRID_Y];
        mPipeLeftUp = new float[GRID_X][GRID_Y];
        mPipeRightUp = new float[GRID_X][GRID_Y];

        // All pipes have an initial flow of zero.
        for (int x = 0; x < GRID_X; x++) {
            for (int y = 0; y < GRID_Y; y++) {
                mPipeUp[x][y] = 0.0f;
                mPipeRight[x][y] = 0.0f;
                mPipeLeftUp[x][y] = 0.0f;
                mPipeRightUp[x][y] = 0.0f;
                /* fill columns */
                mColumnVolumes[x][y] = mIdealTotalVolume / (xsize * ysize);
            }
        }
    }


    /*
     This private member function will update the flow within the virtual pipe system. For each pipe, the
     flow acceleration is computed from the pressure differential across the pipe between the two connected
     columns. The pipe flow during a time step is then updated using a simple Euler integrator. Pipes at
     the edges of the simulation grid are assumed to have zero flow.
     */
    private void computePipeFlows(final float TIMESTEP) {
        final float p_cacc = (TIMESTEP * PIPE_AREA * GRAVITATION) / (PIPE_LENGTH * (1.0f / (GRID_X * GRID_Y)));

        // Update the water flow in the horizontal virtual pipes, ...
        // ... the vertical ones, ...
        // ... and finally, the left / right diagonal ones.
        for (int x = 0; x < GRID_X; x++) {
            for (int y = 0; y < GRID_Y; y++) {
                float myDiff;
                /* vertical */
                myDiff = mColumnVolumes[X(x + 1)][y] - mColumnVolumes[x][y];
                mPipeRight[x][y] = (mPipeRight[x][y] + p_cacc * myDiff) * DAMPING;
                /* horizontal */
                myDiff = mColumnVolumes[x][Y(y + 1)] - mColumnVolumes[x][y];
                mPipeUp[x][y] = (mPipeUp[x][y] + p_cacc * myDiff) * DAMPING;
                /* left-up diagonal */
                myDiff = mColumnVolumes[X(x - 1)][Y(y + 1)] - mColumnVolumes[x][y];
                mPipeLeftUp[x][y] = (mPipeLeftUp[x][y] + p_cacc * myDiff) * DAMPING;
                /* right-up diagonal*/
                myDiff = mColumnVolumes[X(x + 1)][Y(y + 1)] - mColumnVolumes[x][y];
                mPipeRightUp[x][y] = (mPipeRightUp[x][y] + p_cacc * myDiff) * DAMPING;
            }
        }
    }


    /*
     Here we compute the net volume change of all columns (over a constant time step) due to the flow within the
     virtual pipe system. We will also make sure to converve the initial volume of water in the system, in order
     to keep our simulation consistent and stable.
     */
    private void updateVolumes(final float TIMESTEP) {
        // Recompute the volume of all columns, using the new pipe flow values.
        // Negative volumes can result from our approximative model, clamp them to zero, and record the global error term
        float dist = 0.0f;
        mTotalVolume = 0.0f;

        for (int x = 0; x < GRID_X; x++) {
            for (int y = 0; y < GRID_Y; y++) {
                mColumnVolumes[x][y] += TIMESTEP
                        * (mPipeRight[x][y] - mPipeRight[X(x - 1)][y]
                        + mPipeUp[x][y] - mPipeUp[x][Y(y - 1)]
                        + mPipeLeftUp[x][y] - mPipeLeftUp[X(x + 1)][Y(y - 1)]
                        + mPipeRightUp[x][y] - mPipeRightUp[X(x - 1)][Y(y - 1)]);
                if (mColumnVolumes[x][y] < 0) {
                    dist += mColumnVolumes[x][y];
                    mColumnVolumes[x][y] = 0;
                }
                mTotalVolume += mColumnVolumes[x][y];
            }
        }

        final float myVolumeDifference = mIdealTotalVolume - mTotalVolume;
        dist += myVolumeDifference;

        // In the update step above, the system lost water due to the clamped negative volumes. The amount of lost volume was
        // recorded in [dist]. We will now uniformly distribute this volume over the grid, in order to guarantee volume conservation.
        // At the same time (we combine both steps for performance reasons), we update the heightfield by dividing the volume
        // of each column by its base area.
        final float d = dist / (GRID_X * GRID_Y);
        for (int x = 0; x < GRID_X; x++) {
            for (int y = 0; y < GRID_Y; y++) {
                mColumnVolumes[x][y] += d;
            }
        }
    }


    /*
     The public member function 'Simulate' will trigger a full simulation cycle for the set constant time step. It needs to be
     called in regular intervals by the host application, in order to update the water simulation.
     */
    public void simulate(final float TIMESTEP) {
        computePipeFlows(TIMESTEP);
        updateVolumes(TIMESTEP);
    }


    /*
     The public 'ApplyForce' member function will apply an external force to the water simulation object. The force will be
     applied at the given location on the column grid, with the specified intensity.
     */
    public void applyForce(int x, int y, float magnitude, final float TIMESTEP) {
        final float p_cforce = (TIMESTEP * PIPE_AREA) / (PIPE_LENGTH * FLUID_DENSITY);

        // Compute the total flow resulting from the applied force
        float p = -magnitude * p_cforce;

        // We can now distribute this flow over the pipes around the point of impact
        // The distribution is symmetrical to ensure volume conservation.
        mPipeRight[X(x)][Y(y)] += p;
        mPipeUp[X(x)][Y(y)] += p;
        mPipeLeftUp[X(x)][Y(y)] += p;
        mPipeRightUp[X(x)][Y(y)] += p;

        mPipeRight[X(x + 1)][Y(y)] -= p;
        mPipeUp[X(x)][Y(y + 1)] -= p;
        mPipeLeftUp[X(x - 1)][Y(y + 1)] -= p;
        mPipeRightUp[X(x + 1)][Y(y + 1)] -= p;
    }


    public void addVolume(int x, int y, float theAmount) {
        mIdealTotalVolume += theAmount;
        mColumnVolumes[x][y] += theAmount;
    }


    public float totalvolume() {
        return mTotalVolume;
    }


    private int X(int x) {
        return (x + GRID_X) % GRID_X;
    }


    private int Y(int y) {
        return (y + GRID_Y) % GRID_Y;
    }


    public float[][] volumemap() {
        return mColumnVolumes;
    }
}
