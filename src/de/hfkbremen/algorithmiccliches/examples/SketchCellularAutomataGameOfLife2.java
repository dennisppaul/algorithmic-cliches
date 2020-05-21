package de.hfkbremen.algorithmiccliches.examples;

import de.hfkbremen.algorithmiccliches.cellularautomata.CellularAutomaton2;
import processing.core.PApplet;

public class SketchCellularAutomataGameOfLife2 extends PApplet {

    /**
     * http://en.wikipedia.org/wiki/Conway%27s_Game_of_Life
     */

    private CellularAutomaton2 mCA;

    public void settings() {
        size(1024, 768, P3D);
    }

    public void setup() {
        rectMode(CENTER);
        textFont(createFont("Courier", 11));

        mCA = new CellularAutomaton2(1024 / 8, 768 / 8);
        mCA.randomizeCells(0.6f);
    }

    public void draw() {
        lights();
        background(255);

        /* evaluate cells */
        int mBirth = 3;
        int mMinNeighbors = 2;
        int mMaxNeighbors = 3;
        mCA.update(mBirth, mMinNeighbors, mMaxNeighbors); // B3/S23

        /* draw grid */
        fill(127);
        noStroke();
        pushMatrix();
        translate(width / 2.0f, height / 2.0f);
        scale(6);
        mCA.draw(g);
        popMatrix();

        /* draw info */
        fill(0);
        noStroke();
        text("RULE     : " + "B" + mBirth + "/S" + mMinNeighbors + "" + mMaxNeighbors, 10, 12);
        text("FPS      : " + frameRate, 10, 24);
    }

    public void keyPressed() {
        mCA.randomizeCells(0.6f);
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{SketchCellularAutomataGameOfLife2.class.getName()});
    }
}
