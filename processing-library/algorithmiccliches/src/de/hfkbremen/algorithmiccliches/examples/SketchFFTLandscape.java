package de.hfkbremen.algorithmiccliches.examples;

import ddf.minim.AudioInput;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import de.hfkbremen.algorithmiccliches.util.ArcBall;
import java.util.Vector;
import processing.core.PApplet;

/**
 * http://en.wikipedia.org/wiki/Fft
 */
public class SketchFFTLandscape extends PApplet {

    private Minim mMinim;

    private AudioInput mLiveAudioInput;

    private FFT mFFT;

    private static float mCurrentTime = 0.0f;

    private final MBands[] mBands = new MBands[200];

    private int mBandsPointer = 0;

    private static final float BAND_SCALE = 8.0f;

    public void settings() {
        size(1024, 768, P3D);
    }

    public void setup() {
        new ArcBall(this);

        mMinim = new Minim(this);

        mLiveAudioInput = mMinim.getLineIn(Minim.STEREO, 1024);
        mFFT = new FFT(mLiveAudioInput.bufferSize(), mLiveAudioInput.sampleRate());
        mFFT.logAverages(55, 4);

        for (int i = 0; i < mBands.length; i++) {
            mBands[i] = new MBands();
        }
    }

    public void draw() {
        mCurrentTime += 1.0f / frameRate;
        handleFFT();

        background(255);
        directionalLight(126, 126, 126, 0, 0, -1);
        ambientLight(102, 102, 102);
        translate(0, 0, -height / 2);

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

    public void stop() {
        mLiveAudioInput.close();
        mMinim.stop();
        super.stop();
    }

    private void handleFFT() {
        mFFT.forward(mLiveAudioInput.left);
        MBands mCurrentBands = new MBands();
        mCurrentBands.time = mCurrentTime;
        for (int i = 0; i < mFFT.avgSize(); i++) {
            mCurrentBands.bands.add(i, mFFT.getAvg(i));
        }
        mBands[mBandsPointer] = mCurrentBands;
    }

    private void drawBands(MBands b) {
        for (int i = 0; i < b.bands.size(); i++) {
            final float x = width * (float) i / b.bands.size();
            final float myHeight = b.bands.get(i) * BAND_SCALE;
            rect(x, height, width / b.bands.size(), -myHeight);
        }
    }

    private void drawFFTLandscape() {
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

    class MBands {

        Vector<Float> bands = new Vector<Float>();

        float time;

    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchFFTLandscape.class.getName()});
    }
}
