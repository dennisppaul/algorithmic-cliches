package de.hfkbremen.creatingprocessingfinding.util;


import controlP5.ControlP5;
import controlP5.Slider;
import processing.core.PApplet;


public class SketchControlP5
        extends PApplet {

    private ControlP5 mCP5;

    private int mColor = color(0, 0, 0);

    private int sliderValue = 100;

    private int sliderTicks1 = 100;

    private int sliderTicks2 = 30;

    Slider abc;

    public void setup() {
        size(700, 400);
        noStroke();
        mCP5 = new ControlP5(this);

        // add a horizontal sliders, the value of this slider will be linked
        // to variable 'sliderValue'
        mCP5.addSlider("sliderValue")
                .setPosition(100, 50)
                .setRange(0, 255);

        // create another slider with tick marks, now without
        // default value, the initial value will be set according to
        // the value of variable sliderTicks2 then.
        mCP5.addSlider("sliderTicks1")
                .setPosition(100, 140)
                .setSize(20, 100)
                .setRange(0, 255)
                .setNumberOfTickMarks(5);


        // add a vertical slider
        mCP5.addSlider("slider")
                .setPosition(100, 305)
                .setSize(200, 20)
                .setRange(0, 200)
                .setValue(128);

        // reposition the Label for controller 'slider'
        mCP5.getController("slider").getValueLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);
        mCP5.getController("slider").getCaptionLabel().align(ControlP5.RIGHT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);


        mCP5.addSlider("sliderTicks2")
                .setPosition(100, 370)
                .setWidth(400)
                .setRange(255, 0) // values can range from big to small as well
                .setValue(128)
                .setNumberOfTickMarks(7)
                .setSliderMode(Slider.FLEXIBLE);
        // use Slider.FIX or Slider.FLEXIBLE to change the slider handle
        // by default it is Slider.FIX
    }

    public void draw() {
        background(sliderTicks1);

        fill(sliderValue);
        rect(0, 0, width, 100);

        fill(mColor);
        rect(0, 280, width, 70);

        fill(sliderTicks2);
        rect(0, 350, width, 50);
    }

    public void slider(float theColor) {
        mColor = color(theColor);
        println("a slider event. setting background to " + theColor);
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchControlP5.class.getName()});
    }
}
