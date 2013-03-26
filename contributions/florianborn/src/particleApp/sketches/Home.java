package particleApp.sketches;


import processing.core.PApplet;
import teilchen.Particle;
import teilchen.Physics;
import teilchen.force.flowfield.FlowField;
import teilchen.force.simplevectorfield.SimpleVectorField;

public class Home extends PApplet {

    int particleNumber;
    float pSize = 6;
    Physics mPhysics;

    public void setup() {
        size(1024, 768, OPENGL);
        background(30);
        particleNumber = 100;
        ellipseMode(CENTER);
        mPhysics = new Physics();
        for (int i = 0; i < particleNumber; i++) {
            mPhysics.makeParticle ( random( 0, width ), random(0,height));
        }
        
        mPhysics.add(new SimpleVectorField(100, 100));
    }

    public void draw() {
        for(Particle p : mPhysics.particles()){
            ellipse(p.position().x, p.position().y, pSize,pSize );
        }
        update();
        
    }
    
    public void update(){
        
    }
    
    public static void main(String[] args) {
        PApplet.main(new String[]{Home.class.getName()});
    }
}
