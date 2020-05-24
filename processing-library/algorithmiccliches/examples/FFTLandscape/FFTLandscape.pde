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


static final float BAND_SCALE = 8.0f;
static float mCurrentTime = 0.0f;
final MBands[] mBands = new MBands[200];
Minim mMinim;
AudioInput mLiveAudioInput;
FFT mFFT;
int mBandsPointer = 0;
void settings() {
    size(1024, 768, P3D);
}
void stop() {
    mLiveAudioInput.close();
    mMinim.stop();
    super.stop();
}
void setup() {
    new ACArcBall(this);
    mMinim = new Minim(this);
    mLiveAudioInput = mMinim.getLineIn(Minim.STEREO, 1024);
    mFFT = new FFT(mLiveAudioInput.bufferSize(), mLiveAudioInput.sampleRate());
    mFFT.logAverages(55, 4);
    for (int i = 0; i < mBands.length; i++) {
        mBands[i] = new MBands();
    }
}
void draw() {
    mCurrentTime += 1.0f / frameRate;
    handleFFT();
    background(255);
    directionalLight(126, 126, 126, 0, 0, -1);
    ambientLight(102, 102, 102);
    translate(0, 0, -height / 2.0f);
    fill(255, 127, 0);
    noStroke();
    drawFFTLandscape();
    fill(0, 127, 255);
    noStroke();
    translate(0, 0, 20);
    drawBands(mBands[mBandsPointer]);
    mBandsPointer++;
    mBandsPointer %= mBands.length;
}
void handleFFT() {
    mFFT.forward(mLiveAudioInput.left);
    MBands mCurrentBands = new MBands();
    mCurrentBands.time = mCurrentTime;
    for (int i = 0; i < mFFT.avgSize(); i++) {
        mCurrentBands.bands.add(i, mFFT.getAvg(i));
    }
    mBands[mBandsPointer] = mCurrentBands;
}
void drawBands(MBands b) {
    for (int i = 0; i < b.bands.size(); i++) {
        final float x = width * (float) i / b.bands.size();
        final float myHeight = b.bands.get(i) * BAND_SCALE;
        rect(x, height, (float) width / b.bands.size(), -myHeight);
    }
}
void drawFFTLandscape() {
    beginShape(TRIANGLES);
    if (mBands.length > 2) {
        final float z = -20.0f;
        for (int i = 0; i < mBands.length; i++) {
            int mIndex = (mBands.length - i + mBandsPointer) % mBands.length;
            MBands b = mBands[mIndex];
            int mPIndex = (mIndex + 1) % mBands.length;
            MBands bP = mBands[mPIndex];
            if (bP.bands.isEmpty() || b.bands.isEmpty()) {
                continue;
            }
            for (int j = 0; j < b.bands.size(); j++) {
                int mJ = (j + 1) % b.bands.size();
                final float x0 = width * (float) j / b.bands.size();
                final float y0 = height - b.bands.get(j) * BAND_SCALE;
                final float x1 = width * (float) (j + 1) / b.bands.size();
                final float y1 = height - b.bands.get(mJ) * BAND_SCALE;
                final float x2 = width * (float) (j + 1) / bP.bands.size();
                final float y2 = height - bP.bands.get(mJ) * BAND_SCALE;
                final float x3 = width * (float) j / bP.bands.size();
                final float y3 = height - bP.bands.get(j) * BAND_SCALE;
                vertex(x0, y0, z * i);
                vertex(x1, y1, z * i);
                vertex(x2, y2, z * (i - 1));
                vertex(x0, y0, z * i);
                vertex(x2, y2, z * (i - 1));
                vertex(x3, y3, z * (i - 1));
            }
        }
    }
    endShape();
}
static class MBands {
    ArrayList<Float> bands = new ArrayList();
    float time;
}
