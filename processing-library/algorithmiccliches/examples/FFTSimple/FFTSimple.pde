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
 * http://en.wikipedia.org/wiki/Fft
 */
Minim mMinim;
AudioPlayer mPlayer;
FFT mFFT;
void settings() {
    size(1024, 768);
}
void stop() {
    mPlayer.pause();
    mMinim.stop();
    super.stop();
}
void setup() {
    // @TODO `JFileChooser` crashes on macOS 10.15
    javax.swing.JFileChooser mFileChooser = new javax.swing.JFileChooser();
    mFileChooser.showOpenDialog(frame);
    String mFileName = mFileChooser.getSelectedFile().getAbsolutePath();
    mMinim = new Minim(this);
    mPlayer = mMinim.loadFile(mFileName, 1024); // make sure the sketch can find the audio
    mPlayer.play();
    mFFT = new FFT(mPlayer.bufferSize(), mPlayer.sampleRate());
    mFFT.logAverages(55, 4);
}
void draw() {
    mFFT.forward(mPlayer.left);
    float mCurrentTime = (float) mPlayer.position() / (float) mPlayer.length();
    /* draw bands */
    background(255);
    for (int i = 0; i < mFFT.avgSize(); i++) {
        final float x = width * (float) i / mFFT.avgSize();
        final float myHeight = mFFT.getAvg(i) * 16;
        fill(0);
        stroke(0);
        rect(x, height, (float) width / mFFT.avgSize(), -myHeight);
    }
    line(0, height / 2.0f, mCurrentTime * width, height / 2.0f);
}
