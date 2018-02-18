import de.hfkbremen.algorithmiccliches.*; 
import de.hfkbremen.algorithmiccliches.agents.*; 
import de.hfkbremen.algorithmiccliches.cellularautomata.*; 
import de.hfkbremen.algorithmiccliches.convexhull.*; 
import de.hfkbremen.algorithmiccliches.delaunaytriangulation2.*; 
import de.hfkbremen.algorithmiccliches.delaunaytriangulation2.VoronoiDiagram.Region; 
import de.hfkbremen.algorithmiccliches.exporting.*; 
import de.hfkbremen.algorithmiccliches.fluiddynamics.*; 
import de.hfkbremen.algorithmiccliches.isosurface.marchingcubes.*; 
import de.hfkbremen.algorithmiccliches.isosurface.marchingsquares.*; 
import de.hfkbremen.algorithmiccliches.laserline.*; 
import de.hfkbremen.algorithmiccliches.lindenmayersystems.*; 
import de.hfkbremen.algorithmiccliches.octree.*; 
import de.hfkbremen.algorithmiccliches.util.*; 
import de.hfkbremen.algorithmiccliches.util.ArcBall; 
import de.hfkbremen.algorithmiccliches.voronoidiagram.*; 
import oscP5.*; 
import netP5.*; 
import teilchen.*; 
import teilchen.constraint.*; 
import teilchen.force.*; 
import teilchen.behavior.*; 
import teilchen.cubicle.*; 
import teilchen.util.*; 
import teilchen.util.Vector3i; 
import teilchen.util.Util; 
import teilchen.util.Packing; 
import teilchen.util.Packing.PackingEntity; 
import de.hfkbremen.mesh.*; 
import java.util.*; 
import ddf.minim.*; 
import ddf.minim.analysis.*; 
import quickhull3d.*; 
import javax.swing.*; 


Minim mMinim;
AudioPlayer mPlayer;
FFT mFFT;
static float mCurrentTime = 0.0f;
void settings() {
    size(1024, 768, P3D);
}
void setup() {
    JFileChooser mfileChooser = new JFileChooser();
    mfileChooser.showOpenDialog(frame);
    String mFileName = mfileChooser.getSelectedFile().getAbsolutePath();
    mMinim = new Minim(this);
    mPlayer = mMinim.loadFile(mFileName, 1024); // make sure the sketch can find the audio
    mPlayer.play();
    mFFT = new FFT(mPlayer.bufferSize(), mPlayer.sampleRate());
    mFFT.logAverages(55, 4);
}
void draw() {
    mFFT.forward(mPlayer.left);
    mCurrentTime = (float) mPlayer.position() / (float) mPlayer.length();
    ArrayList<Float> mBands = new ArrayList<Float>(mFFT.avgSize()); // @todo revisit the diff between ```.getBand(i)```and ```.getAvg(i)```
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
void stop() {
    mPlayer.pause();
    mMinim.stop();
    super.stop();
}
