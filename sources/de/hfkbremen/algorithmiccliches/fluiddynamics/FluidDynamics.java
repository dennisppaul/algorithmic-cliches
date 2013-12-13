package de.hfkbremen.algorithmiccliches.fluiddynamics;


import processing.core.PGraphics;

import static processing.core.PApplet.floor;
import static processing.core.PApplet.max;
import static processing.core.PApplet.min;
import static processing.core.PApplet.sqrt;
import static processing.core.PConstants.QUADS;


public class FluidDynamics {

    /*
     * Fluid DynamicsFluid Dynamics
     * Simple fluid dynamics sketch,
     * based on <a href="http://www.dgp.toronto.edu/people/stam/reality/Research/pdf/GDC03.pdf" target="_blank">Real-time Fluid Dynamics for Games (PDF)</a>
     * and <a href="http://www.plasmapong.com/" target="_blank">Plasma Pong</a>.
     */
    private final int mX;

    private final int mY;

    private final int mGridX;

    private final int mGridY;

    private float mDiffusion = 0.0001f;

    private float mViscosity = 0.0001f;

    private float mDrag = 1.0f;

    private float[][] mDensityMap;

    private float[][] mPrevDensityMap;

    private float[][] mVelocityMapU;

    private float[][] mVelocityMapV;

    private float[][] mPrevVelocityMapU;

    private float[][] mPrevVelocityMapV;

    public FluidDynamics(int pWidth, int pHeight) {
        mX = pWidth;
        mY = pHeight;
        mGridX = mX + 2;
        mGridY = mY + 2;
        resetVelocity();
        resetDensity();
    }

    public int width() {
        return mX;
    }

    public int height() {
        return mY;
    }

    public void drawVelocity(PGraphics g) {
        for (int y = 1; y <= mY; y++) {
            for (int x = 1; x <= mX; x++) {
                float vu = 50 * mVelocityMapU[x][y];
                float vv = 50 * mVelocityMapV[x][y];
                g.line((x - 1),
                       (y - 1),
                       (x - 1) + vu,
                       (y - 1) + vv);
            }
        }
    }

    public void drawDensity(PGraphics g, int c) {
        for (int y = 1; y <= mY; y++) {
            for (int x = 1; x <= mX; x++) {
                float d00 = mDensityMap[x][y];
                float d01 = mDensityMap[x][y + 1];
                float d10 = mDensityMap[x + 1][y];
                float d11 = mDensityMap[x + 1][y + 1];

                g.beginShape(QUADS);
                fill(g, c, d00);
                g.vertex((x - 1), (y - 1));
                fill(g, c, d10);
                g.vertex((x - 1) + 1, (y - 1));
                fill(g, c, d11);
                g.vertex((x - 1) + 1, (y - 1) + 1);
                fill(g, c, d01);
                g.vertex((x - 1), (y - 1) + 1);
                g.endShape();
            }
        }
    }

    public float diffusion() {
        return mDiffusion;
    }

    public void diffusion(float pDiffusion) {
        mDiffusion = pDiffusion;
    }

    public void drag(float pDrag) {
        mDrag = pDrag;
    }

    public float viscosity() {
        return mViscosity;
    }

    public void viscosity(float pViscosity) {
        mViscosity = pViscosity;
    }

    private void fill(PGraphics g, int pColor, float pAlphaRatio) {
        pAlphaRatio = max(0.0f, min(pAlphaRatio, 1.0f));
        g.fill(g.red(pColor), g.green(pColor), g.blue(pColor), g.alpha(pColor) * pAlphaRatio);
    }


    /* ---- */
    public void update(float mDeltaTime) {
        initPrevMaps();
        calculateVelocity(mDeltaTime);
        calculateDensity(mDeltaTime);
    }

    public void reset() {
        resetVelocity();
        resetDensity();
    }

    private void setForceArea(float[][] pMap,
                              int pX, int pY,
                              float pStrength,
                              float pRadius) {
        for (int x = (int) (range(pX - pRadius, 1, mX)); x <= (int) (range(pX + pRadius, 1, mX)); x++) {
            int dx = pX - x;
            for (int y = (int) (range(pY - pRadius, 1, mY)); y <= (int) (range(pY + pRadius, 1, mY)); y++) {
                int dy = pY - y;
                float f = 1 - (sqrt(dx * dx + dy * dy) / pRadius);
                pMap[x][y] += range(f, 0, 1) * pStrength;
            }
        }
    }

    public void setDensityArea(int x, int y,
                               float pStrength,
                               int pRadius) {
        setForceArea(mDensityMap, x, y, pStrength, pRadius);
    }

    public void setVelocityArea(int x, int y,
                                float vX, float vY,
                                int pRadius) {
        setForceArea(mVelocityMapU, x, y, vX, pRadius);
        setForceArea(mVelocityMapV, x, y, vY, pRadius);
    }

    private float range(float f, float minf, float maxf) {
        return Math.max(Math.min(f, maxf), minf);
    }

    private void resetVelocity() {
        mVelocityMapU = new float[mGridX][mGridY];
        mVelocityMapV = new float[mGridX][mGridY];
        mPrevVelocityMapU = new float[mGridX][mGridY];
        mPrevVelocityMapV = new float[mGridX][mGridY];
    }

    private void resetDensity() {
        mDensityMap = new float[mGridX][mGridY];
        mPrevDensityMap = new float[mGridX][mGridY];
    }

    private void addSource(float[][] x, float[][] s, float dt) {
        for (int i = 0; i < mGridX; i++) {
            for (int j = 0; j < mGridY; j++) {
                x[i][j] += s[i][j] * dt;
            }
        }
    }

    private void removeEnergy(float[][] x, float s) {
        for (int i = 0; i < mGridX; i++) {
            for (int j = 0; j < mGridY; j++) {
                x[i][j] *= s;
            }
        }
    }

    private void diffuse(float[][] x, float[][] x0, float diff, float dt) {
        int i, j, k;
        float a = dt * diff * mX * mY;
        for (k = 0; k < 20; k++) {
            for (i = 1; i <= mX; i++) {
                for (j = 1; j <= mY; j++) {
                    x[i][j] = (x0[i][j] + a * (x[i - 1][j] + x[i + 1][j] + x[i][j - 1] + x[i][j + 1])) / (1 + 4 * a);
                }
            }
        }
    }

    private void project() {
        int i, j, k;
        final float hX = 1.0f / mX;
        final float hY = 1.0f / mY;

        for (i = 1; i <= mX; i++) {
            for (j = 1; j <= mY; j++) {
                mPrevVelocityMapV[i][j] = -0.5f * hY * (mVelocityMapU[i + 1][j] - mVelocityMapU[i - 1][j] + mVelocityMapV[i][j + 1] - mVelocityMapV[i][j - 1]);
                mPrevVelocityMapU[i][j] = 0;
            }
        }

        for (k = 0; k < 20; k++) {
            for (i = 1; i <= mX; i++) {
                for (j = 1; j <= mY; j++) {
                    mPrevVelocityMapU[i][j] = (mPrevVelocityMapV[i][j] + mPrevVelocityMapU[i - 1][j] + mPrevVelocityMapU[i + 1][j] + mPrevVelocityMapU[i][j - 1] + mPrevVelocityMapU[i][j + 1]) / 4;
                }
            }
        }

        for (i = 1; i <= mX; i++) {
            for (j = 1; j <= mY; j++) {
                mVelocityMapU[i][j] -= 0.5f * (mPrevVelocityMapU[i + 1][j] - mPrevVelocityMapU[i - 1][j]) / hX;
                mVelocityMapV[i][j] -= 0.5f * (mPrevVelocityMapU[i][j + 1] - mPrevVelocityMapU[i][j - 1]) / hY;
            }
        }
    }

    private void advect(float[][] d, float[][] d0, float[][] u, float[][] v, float dt) {

        int i, j, i0, j0, i1, j1;
        float x, y, s0, t0, s1, t1;

        final float dt0X = dt * mX;
        final float dt0Y = dt * mY;
        for (i = 1; i <= mX; i++) {
            for (j = 1; j <= mY; j++) {
                x = i - dt0X * u[i][j];
                y = j - dt0Y * v[i][j];

                x = max(0.5f, x);
                x = min(mX + 0.5f, x);

                i0 = floor(x);
                i1 = i0 + 1;

                y = max(0.5f, y);
                y = min(mY + 0.5f, y);

                j0 = floor(y);
                j1 = j0 + 1;

                s1 = x - i0;
                s0 = 1 - s1;
                t1 = y - j0;
                t0 = 1 - t1;

                d[i][j] = s0 * (t0 * d0[i0][j0] + t1 * d0[i0][j1])
                        + s1 * (t0 * d0[i1][j0] + t1 * d0[i1][j1]);
            }
        }
    }

    private void calculateVelocity(float pDeltaTime) {
        float[][] mTemp;

        addSource(mVelocityMapU, mPrevVelocityMapU, pDeltaTime);
        addSource(mVelocityMapV, mPrevVelocityMapV, pDeltaTime);

        removeEnergy(mVelocityMapU, mDrag);
        removeEnergy(mVelocityMapV, mDrag);

        mTemp = mVelocityMapU;
        mVelocityMapU = mPrevVelocityMapU;
        mPrevVelocityMapU = mTemp;

        mTemp = mVelocityMapV;
        mVelocityMapV = mPrevVelocityMapV;
        mPrevVelocityMapV = mTemp;

        diffuse(mVelocityMapU, mPrevVelocityMapU, mViscosity, pDeltaTime);
        diffuse(mVelocityMapV, mPrevVelocityMapV, mViscosity, pDeltaTime);

        project();

        mTemp = mVelocityMapU;
        mVelocityMapU = mPrevVelocityMapU;
        mPrevVelocityMapU = mTemp;

        mTemp = mVelocityMapV;
        mVelocityMapV = mPrevVelocityMapV;
        mPrevVelocityMapV = mTemp;

        advect(mVelocityMapU, mPrevVelocityMapU, mPrevVelocityMapU, mPrevVelocityMapV, pDeltaTime);
        advect(mVelocityMapV, mPrevVelocityMapV, mPrevVelocityMapU, mPrevVelocityMapV, pDeltaTime);

        project();
    }

    private void calculateDensity(float pDeltaTime) {
        float[][] mTemp;

        addSource(mDensityMap, mPrevDensityMap, pDeltaTime);

        mTemp = mDensityMap;
        mDensityMap = mPrevDensityMap;
        mPrevDensityMap = mTemp;

        diffuse(mDensityMap, mPrevDensityMap, mDiffusion, pDeltaTime);

        mTemp = mDensityMap;
        mDensityMap = mPrevDensityMap;
        mPrevDensityMap = mTemp;

        advect(mDensityMap, mPrevDensityMap, mVelocityMapU, mVelocityMapV, pDeltaTime);
    }

    private void initPrevMaps() {
        mPrevVelocityMapU = new float[mGridX][mGridY];
        mPrevVelocityMapV = new float[mGridX][mGridY];
        mPrevDensityMap = new float[mGridX][mGridY];
    }
}
