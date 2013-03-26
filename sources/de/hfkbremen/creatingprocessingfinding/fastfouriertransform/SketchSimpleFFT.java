package de.hfkbremen.creatingprocessingfinding.fastfouriertransform;


import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import ddf.minim.analysis.FFT;
import java.util.Vector;
import processing.core.PApplet;


/**
 * http://en.wikipedia.org/wiki/Fft
 */
public class SketchSimpleFFT
        extends PApplet {

    private Minim mMinim;

    private AudioPlayer mPlayer;

    private FFT mFFT;

    private static float mCurrentTime = 0.0f;

    public void setup() {
        size(1024, 768, OPENGL);

        mMinim = new Minim(this);
        mPlayer = mMinim.loadFile("file.mp3", 1024);
        mPlayer.play();
        mFFT = new FFT(mPlayer.bufferSize(), mPlayer.sampleRate());
        mFFT.logAverages(55, 4);
    }

    public void draw() {
        mFFT.forward(mPlayer.left);
        mCurrentTime = (float) mPlayer.position() / (float) mPlayer.length();

        Vector<Float> mBands = new Vector<Float>(mFFT.avgSize());
        for (int i = 0; i < mFFT.avgSize(); i++) {
            mBands.add(i, mFFT.getBand(i));
        }

        /* draw bands */
        background(255);
        for (int i = 0; i < mFFT.avgSize(); i++) {
            final float x = width * (float) i / mFFT.avgSize();
            final float myHeight = mFFT.getAvg(i) * 16;
            fill(0);
            stroke(0);
            rect(x, height, width / mFFT.avgSize(), -myHeight);
        }
        line(0, height / 2, (float) mCurrentTime * width, height / 2);
    }

    public void stop() {
        mPlayer.pause();
        mMinim.stop();
        super.stop();
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchSimpleFFT.class.getName()});
    }
}
