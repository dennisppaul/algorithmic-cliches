package particleApp_02.sketches;


import geomerative.*;
import java.util.ArrayList;
import mathematik.Vector3f;
import processing.core.PApplet;
import processing.dxf.RawDXF;
import teilchen.CheckPointParticle;
import teilchen.Particle;
import teilchen.Physics;
import teilchen.behavior.Seek;
import teilchen.force.Attractor;
import teilchen.force.ViscousDrag;
import teilchen.force.simplevectorfield.SimpleVectorField;


public class Home extends PApplet {

    private RFont font;

    private int particleNumber = 20000;

    private int gridSize = 5;

    private float mNoiseScale = 0.024f;

    private float forceScaleField = 60.0f;

    private float mOffset = 0.0f;

    private Physics mPhysics;

    private SimpleVectorField mField;

    private SimpleVectorField mField2;

    private Attractor attractor1;

    private Seek s;

    private ArrayList<CheckPointParticle> particles;

    private ArrayList<Attractor> attractor;

    private ArrayList<Attractor> deflector;

    private ArrayList<Vector3f> seekPoints;

    private boolean mRecord = false;

    /*Controls*/
    private Util util;

    private boolean drawForces;

    private boolean addAttractor;

    private boolean addDeflector;

    private float seekScale;

    private boolean addForces;
    
    private RPoint[] pnts;

    public void setup() {
        size(1024, 768, OPENGL);

        pushMatrix();
        translate(width / 2, height / 2);
        RG.init(this);
        System.out.println(this.sketchPath);
        font = new RFont(this.sketchPath + "/lucon.ttf", 400, RFont.CENTER);
        RGroup grp = font.toGroup("LISA");
        RCommand.setSegmentStep(2);
        RCommand.setSegmentator(RCommand.UNIFORMSTEP);
        grp = grp.toPolygonGroup();
        pnts = grp.getPoints();
        for(RPoint p : pnts){
            p.x += width/2;
            p.y += height/2 +100;
        }
        System.out.println(pnts.length);
        popMatrix();

        background(80);
        strokeWeight(1);
        stroke(255);
        noFill();
        ellipseMode(CENTER);
        mPhysics = new Physics();
        util = new Util(this);
        particles = new ArrayList<CheckPointParticle>();
        attractor = new ArrayList<Attractor>();
        deflector = new ArrayList<Attractor>();
        seekPoints = new ArrayList<Vector3f>();
        
        for(RPoint p : pnts){
            seekPoints.add(new Vector3f(p.x,p.y));
        }
        
//        for (int i = 0; i < 360; i += 20) {
//            float x = sin(radians((i))) * (300);
//            float y = cos(radians((i))) * (300);
//            seekPoints.add(new Vector3f((width / 2) + x, (height / 2) + y));
//        }
        for (int i = 0; i < particleNumber; i++) {
            particles.add(new CheckPointParticle(seekPoints));
        }
        /*Forces*/
        mField2 = new SimpleVectorField(width, height, gridSize);
        mField2.setNoiseScale(mNoiseScale);
        mField2.setForceScale(60.0f);
        attractor1 = new Attractor();
        attractor1.setPositionRef(new Vector3f(width / 2, height / 2, 0.0f));
        attractor1.radius(100);
        attractor1.strength(-500);
        for (CheckPointParticle p : particles) {
            p.setPositionRef(new Vector3f(random(width / 2 - 200, width / 2 + 200), random(height / 2 - 200, height / 2 + 200), 0));
            p.mass(random(0.1f, 10.0f));
            p.setMinDistance(60.0f);
            p.maximumInnerForce(1000.0f);
        }
        mPhysics.add(mField2);
        //mPhysics.add(attractor1);
        mPhysics.add(particles);
        for (CheckPointParticle p : particles) {
            Seek s = (Seek) p.behaviors().firstElement();
            s.scale(120.0f);
        }
        mPhysics.add(new ViscousDrag());
    }

    public void draw() {
        background(0.0f);
//        pushMatrix();
//        //translate(width/2,height/2+100);
//        ellipse(pnts[0].x, pnts[0].y, 5, 5);
//        for (int i = 1; i < pnts.length; i++) {
//            line(pnts[i - 1].x, pnts[i - 1].y, pnts[i].x, pnts[i].y);
//            ellipse(pnts[i].x, pnts[i].y, 5, 5);
//        }
//        popMatrix();

        update();
        if (mRecord) {
            toDXF();
        }
        beginShape(POINTS);
        for (Particle p : mPhysics.particles()) {
            vertex(p.position().x, p.position().y);
        }
        endShape();
        if (drawForces) {
            drawAttractor(attractor1);
            drawVectorField(mField2);
            for (Attractor a : attractor) {
                drawAttractor(a);
            }
            for (Attractor d : deflector) {
                drawAttractor(d);
            }
            for (Vector3f v : seekPoints) {
                drawSeekPoint(v);
            }
        }


    }

    private void teleportPatricles() {
        for (CheckPointParticle p : particles) {
            if (p.position().x < 0) {
                p.position().x = width;
            }
            if (p.position().x > width) {
                p.position().x = 0;
            }
            if (p.position().y < 0) {
                p.position().y = height;
            }
            if (p.position().y > height) {
                p.position().y = 0;
            }
        }
    }

    private void update() {
        text(Float.toString(frameRate), 20.0f, height - 40);
        final float mDeltaTime = 1.0f / frameRate;
        for (CheckPointParticle p : particles) {
            p.update(mDeltaTime);
        }
        mOffset += 0.05f * mDeltaTime;
        mField2.setOffset(mOffset);
        mField2.setNoiseScale(mNoiseScale);
        mField2.setForceScale(forceScaleField);
        mField2.update();
        mPhysics.step(mDeltaTime);
    }

    private void toDXF() {
        beginRaw(RawDXF.class.getName(), "output"+ hour() + minute() + second() +".dxf");
        pushStyle();
        sphereDetail(4);
        background(0);
        fill(255);
        for (CheckPointParticle p : particles) {
            pushMatrix();
            float z = p.velocity().length() * 0.5f;
            translate(p.position().x, p.position().y, z);
            sphere(1);
            popMatrix();
        }
        popStyle();
        endRaw();
        mRecord = false;
    }

    public void addForce() {

        if (addDeflector) {
            deflector.add(new Attractor());
            Attractor d = deflector.get(deflector.size() - 1);
            d.setPositionRef(new Vector3f(mouseX, mouseY));
            d.radius(100);
            d.strength(-400);
            mPhysics.add(d);
        }
        if (addAttractor) {
            attractor.add(new Attractor());
            Attractor a = attractor.get(attractor.size() - 1);
            a.setPositionRef(new Vector3f(mouseX, mouseY));
            a.radius(100);
            a.strength(200);
            mPhysics.add(a);
        }

    }

    public void drawAttractor(Attractor a) {
        pushStyle();
        fill(255, 30);
        if (a.strength() < 0) {
            stroke(150, 0, 0);
        } else if (a.strength() > 0) {
            stroke(0, 150, 0);
        }
        strokeWeight(1);
        ellipse(a.position().x, a.position().y, a.radius(), a.radius());
        popStyle();
    }

    public void drawVectorField(SimpleVectorField v) {
        pushStyle();
        stroke(255, 0, 0);
        Vector3f[][] vf = v.getField();
        for (int i = 0; i < vf.length; i++) {
            for (int j = 0; j < vf[i].length; j++) {
                Vector3f dir = vf[i][j];
                Vector3f pos = new Vector3f(i * gridSize, j * gridSize);
                pushMatrix();
                translate(pos.x, pos.y);
                line(0, 0, dir.x * 10, dir.y * 10);
                popMatrix();
            }
        }
        popStyle();
    }

    public void drawSeekPoint(Vector3f v) {
        pushStyle();
        fill(0, 0, 150, 50);
        ellipse(v.x, v.y, 10, 10);
        popStyle();
    }

    public void keyPressed() {
        if (key == 'h') {
            util.toggleHide();
        }

        if (key == 'r') {
            mRecord = true;
        }
    }

    public void mouseClicked() {
        if (addForces) {
            addForce();
        }
        if (addAttractor || addDeflector) {
            addForces = true;
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{Home.class.getName()});
    }
}
