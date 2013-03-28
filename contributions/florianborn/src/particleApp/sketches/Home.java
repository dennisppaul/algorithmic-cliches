package particleApp.sketches;


import mathematik.PerlinNoise;
import mathematik.Vector3f;
import processing.core.PApplet;
import teilchen.Particle;
import teilchen.Physics;
import teilchen.force.Attractor;
import teilchen.force.ViscousDrag;
import teilchen.force.simplevectorfield.SimpleVectorField;


public class Home extends PApplet {

    private int particleNumber = 10000;

    private int fieldWidth;

    private int fieldHeight;

    private int gridSize = 20;

    private float mNoiseScale = 0.024f;

    private float mOffset = 0.0f;

    private float pSize = 6;

    private Physics mPhysics;

    private SimpleVectorField mField;
    private SimpleVectorField mField2;
    private Attractor attractor1;
    
    public void setup() {
        size(1024, 768, OPENGL);
        fieldWidth = width / gridSize;
        fieldHeight = height / gridSize;
        background(80);
        //noStroke();
        strokeWeight(1);
        stroke(255);
        noFill();
        ellipseMode(CENTER);
        mPhysics = new Physics();
        
        for (int i = 0; i < particleNumber; i++) {
            mPhysics.makeParticle(random(0, width), random(0, height),0,random(0.4f,20f));
            
        }
        /*Forces*/
        mField = new SimpleVectorField( width, height, gridSize);
        mField.setForceScale(20.0f);
        mField.setNoiseScale(0.09f);
        mField2 = new SimpleVectorField( width, height, gridSize);
        mField2.setNoiseScale(0.001f);
        mField2.setForceScale(60.0f);
        attra
        
        
        
        mPhysics.add(mField);
        mPhysics.add(mField2);
        mPhysics.add(new ViscousDrag());
    }

    public void draw() {
        background(0);
        update();
        beginShape(POINTS);
        for (Particle p : mPhysics.particles()) {
            vertex(p.position().x, p.position().y);
        }
        endShape();
        drawVectorField(mField);
        drawVectorField(mField2);

    }

    public void update() {
        final float mDeltaTime = 1.0f / frameRate;
        for (Particle p : mPhysics.particles()) {
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
        mOffset += 0.05f * mDeltaTime;
        mField.setOffset(mOffset);
        mField2.setOffset(mOffset);
        mField.update();
        mField2.update();
        mPhysics.step(mDeltaTime);


    }

    public void drawVectorField(SimpleVectorField v) {
        pushStyle();
        stroke(255, 0, 0);
        Vector3f[][] vf = v.getField();
        for (int i = 0; i < vf.length; i++) {
            for (int j = 0; j < vf[i].length; j++) {
                Vector3f dir = vf[i][j];
                Vector3f pos = new Vector3f(i * gridSize, j * gridSize);
                //System.out.println(pos);
                pushMatrix();
                translate(pos.x, pos.y);
                line(0, 0, dir.x * 10, dir.y * 10);
                popMatrix();
            }
        }
        popStyle();
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{Home.class.getName()});
    }
}
