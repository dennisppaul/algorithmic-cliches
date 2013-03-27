package Sketch02;


import de.hfkbremen.creatingprocessingfinding.*;
import processing.core.PApplet;


public class Sketch02
        extends PApplet {

    public void setup() {
        size(500, 500, OPENGL);
        background(100,120,131);
    }

    public void draw() {
        
        
        
        
    }
    public void mouseDragged() {
       line(mouseX, mouseY, pmouseX, pmouseY);
   }

    public static void main(String[] args) {
        PApplet.main(new String[]{Sketch02.class.getName()});
    }
}
