package particleApp_02.sketches;


import controlP5.CColor;
import controlP5.ControlP5;
import processing.core.PApplet;


public class Util {
    private ControlP5 cp5;
    private PApplet p;    
        
    public Util(PApplet theP){
        cp5 = new ControlP5(theP);
        cp5.addToggle("drawForces", false).setPosition(20, 20);
        cp5.addSlider("mNoiseScale").setValue(0.024f).setRange(0.008f,0.19f).setPosition(20, 60);
        cp5.addSlider("forceScaleField").setRange(0.0f, 100.0f).setPosition(20.0f, 220.0f);
        
        cp5.addSlider("seekScale").setValue(120.0f).setRange(0.0f,200.0f).setPosition(20, 100);        
        cp5.addToggle("addAttractor", false).setPosition(20, 140);
        cp5.addToggle("addDeflector", false).setPosition(20, 180);
        
    }
    public void toggleHide(){
        if(cp5.isVisible()){
            cp5.hide();
        }
        else{
            cp5.show();
        }
    }
}
