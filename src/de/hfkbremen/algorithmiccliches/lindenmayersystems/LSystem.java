package de.hfkbremen.algorithmiccliches.lindenmayersystems;

import processing.core.PGraphics;

import static processing.core.PApplet.radians;

/**
 * look into: https://en.wikipedia.org/wiki/L-system
 */

public class LSystem {

    String axiom;
    String rule;
    String production;

    private int steps = 0;
    private float startLength;
    private float drawLength;
    private float theta;
    private int generations;

    public LSystem() {

        axiom = "F";
        rule = "F+F-F";
        startLength = 90.0f;
        theta = radians(60.0f);
        reset();
    }

    public void reset() {
        production = axiom;
        drawLength = startLength;
        generations = 0;
    }

    public int getAge() {
        return generations;
    }

    public void render(PGraphics g) {
        g.translate(g.width / 2, g.height / 2);
        steps += 1;
        if (steps > production.length()) {
            steps = production.length();
        }
        for (int i = 0; i < steps; i++) {
            char step = production.charAt(i);
            if (step == 'F') {
                g.noFill();
                g.stroke(255);
                g.line(0, 0, 0, -drawLength);
                g.translate(0, -drawLength);
            } else if (step == '+') {
                g.rotate(theta);
            } else if (step == '-') {
                g.rotate(-theta);
            } else if (step == '[') {
                g.pushMatrix();
            } else if (step == ']') {
                g.popMatrix();
            }
        }
    }

    public void simulate(int gen) {
        while (getAge() < gen) {
            production = iterate(production, rule);
        }
    }

    private String iterate(String prod_, String rule_) {
        drawLength *= 0.6f;
        generations++;
        String newProduction = prod_;
        newProduction = newProduction.replaceAll("F", rule_);
        return newProduction;
    }
}