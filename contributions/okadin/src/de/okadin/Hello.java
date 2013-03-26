package okadin.src.de.okadin;


import processing.core.PApplet;


public class Hello extends PApplet {
    
    
    public void setup() {
        size(640, 480);
    }

    
    public void draw() {
        
        println("Hello, is it me you looking for...? "
                + ""
                + ""
                + "...I can see it in your eyes, I can see it in your smile!");
    }

   
        public static void main(String[] args) {
        PApplet.main(new String[]{Hello.class.getName()});
    }
        
}
