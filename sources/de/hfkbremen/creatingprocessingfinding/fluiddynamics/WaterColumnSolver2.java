package de.hfkbremen.creatingprocessingfinding.fluiddynamics;


public class WaterColumnSolver2 {

    private float mFluidDensity = 0.1f; /* the density of the fluid */


    public float mGravitation = 1.0f; /* gravitational force */


    public float mPipeLength = 5.0f; /* length of a connection pipe */


    public float pPipeArea = 0.0007f; /* the cross-sectional area of a pipe */


    public float mDamping = 0.9997f; /* acceleration damping factor */


    private final int mGridY;

    private final int mGridX;

    private final float[][] mColumnVolumes;

    private final float[][] mPipeLeftUp;

    private final float[][] mPipeUp;

    private final float[][] mPipeRightUp;

    private final float[][] mPipeRight;

    private float mIdealTotalVolume; // Ideal volume of the water body

    private float mTotalVolume;

    public WaterColumnSolver2(int pGridX, int pGridY) {
        this(pGridX, pGridY, 100);
    }

    /*
     The ctor builds a water simulation object of the specified surface resolution (xsize, ysize),
     with an amplitude of (hscale). The user must specify a pointer to the volumemap, that will
     hold the result after a simulation step.
     */
    public WaterColumnSolver2(int pGridX, int pGridY, float pInitialVolume) {
        mIdealTotalVolume = pInitialVolume;
        mGridX = pGridX;
        mGridY = pGridY;
        mColumnVolumes = new float[mGridX][mGridY];
        mPipeUp = new float[mGridX][mGridY];
        mPipeRight = new float[mGridX][mGridY];
        mPipeLeftUp = new float[mGridX][mGridY];
        mPipeRightUp = new float[mGridX][mGridY];

        for (int x = 0; x < mGridX; x++) {
            for (int y = 0; y < mGridY; y++) {
                mPipeUp[x][y] = 0.0f;
                mPipeRight[x][y] = 0.0f;
                mPipeLeftUp[x][y] = 0.0f;
                mPipeRightUp[x][y] = 0.0f;
                mColumnVolumes[x][y] = mIdealTotalVolume / (pGridX * pGridY); /* fill columns */
            }
        }
    }


    /*
     This private member function will update the flow within the virtual pipe system. For each pipe, the
     flow acceleration is computed from the pressure differential across the pipe between the two connected
     columns. The pipe flow during a time step is then updated using a simple Euler integrator. Pipes at
     the edges of the simulation grid are assumed to have zero flow.
     */
    private void computePipeFlows(final float pDeltaTime) {
        final float p_cacc = (pDeltaTime * pPipeArea * mGravitation) / (mPipeLength * (1.0f / (mGridY * mGridX)));

        /* Update the water flow in the horizontal virtual pipes, the vertical ones, and finally, the left / right diagonal ones. */
        for (int x = 0; x < mGridX; x++) {
            for (int y = 0; y < mGridY; y++) {
                float myDiff;
                /* vertical */
                myDiff = mColumnVolumes[X(x + 1)][y] - mColumnVolumes[x][y];
                mPipeRight[x][y] = (mPipeRight[x][y] + p_cacc * myDiff) * mDamping;
                /* horizontal */
                myDiff = mColumnVolumes[x][Y(y + 1)] - mColumnVolumes[x][y];
                mPipeUp[x][y] = (mPipeUp[x][y] + p_cacc * myDiff) * mDamping;
                /* left-up diagonal */
                myDiff = mColumnVolumes[X(x - 1)][Y(y + 1)] - mColumnVolumes[x][y];
                mPipeLeftUp[x][y] = (mPipeLeftUp[x][y] + p_cacc * myDiff) * mDamping;
                /* right-up diagonal*/
                myDiff = mColumnVolumes[X(x + 1)][Y(y + 1)] - mColumnVolumes[x][y];
                mPipeRightUp[x][y] = (mPipeRightUp[x][y] + p_cacc * myDiff) * mDamping;
            }
        }
    }


    /*
     Here we compute the net volume change of all columns (over a constant time step) due to the flow within the
     virtual pipe system. We will also make sure to converve the initial volume of water in the system, in order
     to keep our simulation consistent and stable.
     */
    private void updateVolumes(final float pDeltaTime) {
        /* Recompute the volume of all columns, using the new pipe flow values. 
         * Negative volumes can result from our approximative model, clamp them to zero, and record the global error term */
        float dist = 0.0f;
        mTotalVolume = 0.0f;

        for (int x = 0; x < mGridX; x++) {
            for (int y = 0; y < mGridY; y++) {
                mColumnVolumes[x][y] += pDeltaTime
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
        final float d = dist / (mGridY * mGridX);
        for (int x = 0; x < mGridX; x++) {
            for (int y = 0; y < mGridY; y++) {
                mColumnVolumes[x][y] += d;
            }
        }
    }

    public void step(final float pDeltaTime) {
        computePipeFlows(pDeltaTime);
        updateVolumes(pDeltaTime);
    }

    void step(float pDeltaTime, int pIterations) {
        for (int i = 0; i < 10; i++) {
            step(pDeltaTime / (float) pIterations);
        }
    }


    /*
     The public 'ApplyForce' member function will apply an external force to the water simulation object. The force will be
     applied at the given location on the column grid, with the specified intensity.
     */
    public void applyForce(int x, int y, final float pMagnitude, final float pDeltaTime) {
        final float p_cforce = (pDeltaTime * pPipeArea) / (mPipeLength * mFluidDensity);

        // Compute the total flow resulting from the applied force
        float p = -pMagnitude * p_cforce;

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

    public void addVolume(int x, int y, float pVolume) {
        mIdealTotalVolume += pVolume;
        mColumnVolumes[x][y] += pVolume;
    }

    public float totalvolume() {
        return mTotalVolume;
    }

    private int X(int x) {
        return (x + mGridX) % mGridX;
    }

    private int Y(int y) {
        return (y + mGridY) % mGridY;
    }

    public float[][] volumemap() {
        return mColumnVolumes;
    }

    public void fluid_density(float pFluidDensity) {
        mFluidDensity = pFluidDensity;
    }

    public void gravitation(float pGravitation) {
        mGravitation = pGravitation;
    }

    public void pipe_length(float pPipeLength) {
        mPipeLength = pPipeLength;
    }

    public void pipe_area(float pPipeArea) {
        this.pPipeArea = pPipeArea;
    }

    public void damping(float pDamping) {
        mDamping = pDamping;
    }
}
