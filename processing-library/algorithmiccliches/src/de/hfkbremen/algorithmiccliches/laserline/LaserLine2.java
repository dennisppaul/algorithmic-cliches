package de.hfkbremen.algorithmiccliches.laserline;

import processing.core.PGraphics;
import processing.core.PVector;

public class LaserLine2 {

    public float core_width = 2;
    public float fade_width = 10;
    public int core_color = 0;
    public int inner_fade_color = 0;
    public int outer_fade_color = 0;
    public PVector p0 = new PVector();
    public PVector p1 = new PVector();

    public void draw(PGraphics g) {
        PVector d = PVector.sub(p1, p0);
        PVector c = new PVector(-d.y, d.x);
        c.normalize();
        c.mult(core_width / 2);
        g.beginShape(g.QUADS);
        /* core */
        g.fill(core_color);
        g.vertex(p0.x + c.x, p0.y + c.y);
        g.fill(core_color);
        g.vertex(p1.x + c.x, p1.y + c.y);
        g.fill(core_color);
        g.vertex(p1.x - c.x, p1.y - c.y);
        g.fill(core_color);
        g.vertex(p0.x - c.x, p0.y - c.y);
        /* top */
        g.fill(outer_fade_color);
        g.vertex(p0.x + c.x * fade_width, p0.y + c.y * fade_width);
        g.fill(outer_fade_color);
        g.vertex(p1.x + c.x * fade_width, p1.y + c.y * fade_width);
        g.fill(inner_fade_color);
        g.vertex(p1.x + c.x, p1.y + c.y);
        g.fill(inner_fade_color);
        g.vertex(p0.x + c.x, p0.y + c.y);
        /* bottom */
        g.fill(outer_fade_color);
        g.vertex(p0.x - c.x * fade_width, p0.y - c.y * fade_width);
        g.fill(outer_fade_color);
        g.vertex(p1.x - c.x * fade_width, p1.y - c.y * fade_width);
        g.fill(inner_fade_color);
        g.vertex(p1.x - c.x, p1.y - c.y);
        g.fill(inner_fade_color);
        g.vertex(p0.x - c.x, p0.y - c.y);
        g.endShape();
    }
}
