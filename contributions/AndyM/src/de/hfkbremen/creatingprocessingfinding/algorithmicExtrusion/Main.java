package de.hfkbremen.creatingprocessingfinding.algorithmicExtrusion;


import de.hfkbremen.creatingprocessingfinding.delaunaytriangulation.DelaunayTriangulation;
import de.hfkbremen.creatingprocessingfinding.util.ArcBall;
import de.hfkbremen.creatingprocessingfinding.voronoidiagramWin.Qvoronoi;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;
import mathematik.Vector3f;
import processing.core.PApplet;
import static processing.core.PApplet.max;
import static processing.core.PApplet.min;
import static processing.core.PConstants.TRIANGLES;
import static processing.core.PConstants.TWO_PI;
import processing.core.PImage;


public class Main
        extends PApplet {
    
    final private int pixeldist = 4;
    final private int takeNavg = 3;
    final private float maxZ = 250;
    
    private PImage src;
    private PImage edgeresult;
    private PImage vertexresult;

    private Vector<Vector3f> vertices;
    private Vector<Vector3f> oldvertices;
    private Vector<DelaunayTriangulation.Triangle> triangles;
    private TreeSet<Vector3fExt> qvovertsExt;
    
    private boolean showt = true;
    private boolean showv = true;

    public void setup() {
        src = loadImage("Horizont.jpg");
        new ArcBall(this);
        src.resize(500, 500);
        size(1000, 500, OPENGL);
        //frameRate(1);
        //Edge detection
        CannyEdgeDetector detector = new CannyEdgeDetector();

        detector.setLowThreshold(5.0f);
        detector.setHighThreshold(5.0f);
        
        detector.setSourceImage((BufferedImage)src.getImage());
        detector.process();   
        edgeresult = new PImage(detector.getEdgesImage());
        
        //image(edgeresult, 0, 0);

        //Punctuation by mask: between two white pixels there must be
        //at least pixeldist black pixels.
        vertexresult = edgeresult.get();

        int altcount = 0;
        for (int x = 0; x < vertexresult.width; x++) {
            for (int y = 0; y < vertexresult.height; y++) {
                //System.out.println(vertexresult.get(x, y));
                    if(++altcount >= pixeldist) {
                        vertexresult.set(x, y, vertexresult.get(x,y) & -1);
                        altcount = 0;
                    } else {
                        vertexresult.set(x, y, color(0));
                    }
            }
        }
        for (int y = 0; y < vertexresult.height; y++) {
            for (int x = 0; x < vertexresult.width; x++) {
                //System.out.println(vertexresult.get(x, y));
                    if(++altcount >= pixeldist) {
                        vertexresult.set(x, y, vertexresult.get(x,y) & -1);
                        altcount = 0;
                    } else {
                        vertexresult.set(x, y, color(0));
                    }
            }
        }
        //image(vertexresult, 500, 0);
        
        //Extract vertices and triangulate
        vertices = new Vector<>();
         for (int x = 0; x < vertexresult.width; x++) {
            for (int y = 0; y < vertexresult.height; y++) {
                if(vertexresult.get(x, y) != color(0)) vertices.add(new Vector3f(x, y));
            }
        }
        triangles = DelaunayTriangulation.triangulate(vertices);

        //drawDelaunay(vertices, triangles);
        
        //Qvoronoi vertex interpolation
        Vector3f[] verticesArr = new Vector3f[vertices.size()];
        vertices.toArray(verticesArr);
        Vector3f[][] qvoRegs = new Qvoronoi().calculate2(verticesArr);
        
        //Remove redundant vertices from regions result
        TreeSet<Vector3f> qvoverts = new TreeSet<>();
        int numverts = 0;
        for (int i = 0; i < qvoRegs.length; i++) {
            numverts += qvoRegs[i].length;
            for (int j = 0; j < qvoRegs[i].length; j++) {
                qvoverts.add(qvoRegs[i][j]);
            }
        }
        System.out.println("Num of new vertices: " + qvoverts.size() + "; org size: " + numverts);
        
        //Calc min & max dist between qvoverts and vertices for z-interpolation
        qvovertsExt = new TreeSet<>();
        for(Iterator<Vector3f> i = qvoverts.iterator(); i.hasNext();) {
            final Vector3fExt v = new Vector3fExt(i.next());
            Collections.sort((Vector<Vector3f>)vertices.clone(), new Comparator<Vector3f>(){
                @Override
                public int compare(Vector3f o1, Vector3f o2) {
                    return (int)Math.ceil((o1.distance(v) - o2.distance(v))*1000);
                }      
            });
            for (int j = 0; j < takeNavg; j++) {
                v.dist += vertices.get(j).distance(v);
            }
            v.dist /= takeNavg;
            qvovertsExt.add(v);
        }
        
        float globalmindist = Float.POSITIVE_INFINITY, globalmaxdist = Float.NEGATIVE_INFINITY;
        for(Iterator<Vector3fExt> i = qvovertsExt.iterator(); i.hasNext();) {
            Vector3fExt v = i.next();
            globalmindist = min(globalmindist, v.dist);
            globalmaxdist = max(globalmaxdist, v.dist);
        }
        
        //Interpolate z-value
        for(Iterator<Vector3fExt> i = qvovertsExt.iterator(); i.hasNext();) {
            Vector3fExt v = i.next();
            v.z = maxZ*(v.dist - globalmindist)/globalmaxdist;
            //System.out.println(v.z);
        }
        
        //Add to other vertices and re-triangulate
        oldvertices = (Vector<Vector3f>)vertices.clone();
        vertices.addAll(qvovertsExt);
        triangles = DelaunayTriangulation.triangulate(vertices);
    }
    
    private class Vector3fExt extends Vector3f {
        private float dist;
        
        Vector3fExt(Vector3f v) {
            super(v);
            dist = 0;
        }
    }

    public void draw() {
        background(0);
        directionalLight(500, 500, 200, 0, -1, -1);
        ambientLight(102, 102, 102);
        
        translate(x,y);
        scale(scale);
        
        image(src, 0, 0);
      
        //if(showv) image(vertexresult, 0, 0);
 
        stroke(255,255,255,50);
        for(Vector3f v : oldvertices) {
            drawCross(v);
        }
        
        stroke(0, 255, 0,50);
        for(Vector3f v : qvovertsExt) {
            drawCross(v);
        }
        
                
        strokeWeight(1);
        //stroke(255, 127, 0, 54);
        fill(200, 200, 200, 150);     
        if(showt) drawDelaunay(vertices, triangles);
    }
    
    private int x = 0, y = 0;
    private float scale = 1.0f;
    
    public void keyPressed() {
        if(key == 't') showt = !showt;
        if(key == 'v') showv = !showv;
        
        if(key == 'w') y-=10;
        if(key == 'a') x-=10;
        if(key == 's') y+=10;
        if(key == 'd') x+=10;
        if(key == '+') scale+=0.05;
        if(key == '-') scale-=0.05;
    }
    
    private void drawCross(Vector3f v) {
        final float o = 2.0f;
        line(v.x - o, v.y, v.z, v.x + o, v.y, v.z);
        line(v.x, v.y - o, v.z, v.x, v.y + o, v.z);
        line(v.x, v.y, v.z - o, v.x, v.y, v.z + o);
    }
    
    private void drawDelaunay(Vector<Vector3f> vertices, Vector<DelaunayTriangulation.Triangle> mDelaunayTriangles) {
        /* draw delaunay trinangles */
        if (mDelaunayTriangles != null) {
            beginShape(TRIANGLES);
            for (int i = 0; i < mDelaunayTriangles.size(); i++) {
                for (int j = 0; j < 3; j++) {
                    vertex(vertices.get(mDelaunayTriangles.get(i).p[j]).x,
                            vertices.get(mDelaunayTriangles.get(i).p[j]).y,
                             vertices.get(mDelaunayTriangles.get(i).p[j]).z);
                }
            }
            endShape();
        }
    }    

    public static void main(String[] args) {
        PApplet.main(new String[]{Main.class.getName()});
    }
}
