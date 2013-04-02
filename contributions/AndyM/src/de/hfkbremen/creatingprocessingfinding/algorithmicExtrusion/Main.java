package de.hfkbremen.creatingprocessingfinding.algorithmicExtrusion;


import controlP5.Button;
import controlP5.ControlP5;
import de.hfkbremen.creatingprocessingfinding.delaunaytriangulation.DelaunayTriangulation;
import de.hfkbremen.creatingprocessingfinding.delaunaytriangulation.VoronoiDiagram;
import de.hfkbremen.creatingprocessingfinding.exporting.PLYExporter;
import de.hfkbremen.creatingprocessingfinding.util.ArcBall;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;
import mathematik.Vector3f;
import processing.core.PApplet;
import static processing.core.PApplet.radians;
import static processing.core.PConstants.CENTER;
import static processing.core.PConstants.ENABLE_DEPTH_TEST;
import static processing.core.PConstants.TRIANGLES;
import processing.core.PFont;
import processing.core.PImage;


public class Main
        extends PApplet {
    
    //Parameters for algorithm
    private int VoronoiIterations = 1;
    private float delLowerNpercent = 50; //Only if more than 1 iteration
    private int pixeldist = 4;
    private int takeNavg = 3;
    private float maxZ = 250;
    
    private PFont font;
    private PImage src;
    private PImage edgeresult;

    private ArcBall arcball;
    private ControlP5 controls;

    private Vector<Vector3f> vertices = new Vector<>();
    private Vector<Vector3f> oldvertices = new Vector<>();
    private Vector<DelaunayTriangulation.Triangle> triangles = new Vector<>();
    private TreeSet<Vector3fExt> qvovertsExt = new TreeSet<>();
    private boolean working = true;
    private final ReentrantLock lock = new ReentrantLock();
    private boolean export = false;
    private boolean lighting = true;
    private Thread thread;
    private Button p;
    
    private boolean showt = false;
    private boolean showv = false;

    public void setup() {
        //src = loadImage("Lenna.png");
        src = loadImage("Horizont.jpg");
        src.resize(500, 500);
        
        controls = new ControlP5(this);
        controls.setUpdate(false);
        arcball = new ArcBall(new Vector3f(250, 250, 0), (maxZ>250)?(maxZ/2):(250), this, true);
        
        font = createFont("Arial", 24);
        textFont(font);
        textAlign(CENTER);
        
        size(1000, 500, OPENGL);
        
        controls.addSlider("pixeldist")
                .setPosition(700, 70)
                .setSize(150, 15)
                .setRange(1, 10)
                .setNumberOfTickMarks(10);        
        
        controls.addSlider("VoronoiIterations")
                .setPosition(700, 120)
                .setSize(100, 15)
                .setRange(1, 5)
                .setNumberOfTickMarks(5);
        
        controls.addSlider("delLowerNpercent")
                .setPosition(700, 170)
                .setSize(100, 15)
                .setRange(0, 100);
        
        controls.addSlider("maxZ")
                .setPosition(700, 220)
                .setSize(150, 15)
                .setRange(0, 600)
                .setValue(250);
        
        p = controls.addButton("process")
                .setPosition(700, 270)
                .setSize(50, 30);

        process();
    }
    
    private void process() {
        if(thread == null || thread.getState() != Thread.State.RUNNABLE) {
            thread = new Thread(new Algorithm(VoronoiIterations, delLowerNpercent, pixeldist, takeNavg, maxZ, this));
            thread.setDaemon(true);
            thread.start();
        } else System.err.println("Wait for computing to finish");
    }
    
    class Algorithm implements Runnable {
        
    private int VoronoiIterations;
    private float delLowerNpercent;
    private int pixeldist;
    private int takeNavg;
    private float maxZ; 
    
    private Vector<Vector3f> vertices;
    private Vector<Vector3f> oldvertices;
    private Vector<DelaunayTriangulation.Triangle> triangles;
    private TreeSet<Vector3fExt> qvovertsExt;
    
    private PImage edgeresult;
    private PImage vertexresult;
    
    private Main reference;
    
        Algorithm(int VoronoiIterations, float delLowerNpercent, int pixeldist, int takeNavg, float maxZ, Main ref) {
        this.VoronoiIterations = VoronoiIterations;
        this.delLowerNpercent = delLowerNpercent;
        this.pixeldist = pixeldist;
        this.takeNavg = takeNavg;
        this.maxZ = maxZ;
        
        this.vertices = new Vector<>();
        this.oldvertices = new Vector<>();
        this.triangles = new Vector<>();
        this.qvovertsExt = new TreeSet<>();
        
        reference = ref;
    }

        @Override
        public void run() {
        p.lock();
        working = true;
            System.out.println("Computing: " + VoronoiIterations + " iterations, deletion of the lowest " + delLowerNpercent + "% of interpolated vertices and maxZ = "+maxZ+".");
        
       //Edge detection
        CannyEdgeDetector detector = new CannyEdgeDetector();

        detector.setLowThreshold(5.0f);
        detector.setHighThreshold(5.0f);
        
        detector.setSourceImage((BufferedImage)src.getImage());
        detector.process();
        edgeresult = new PImage(detector.getEdgesImage());

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
        
        //Extract vertices and triangulate
        vertices = new Vector<>();
         for (int x = 0; x < vertexresult.width; x++) {
            for (int y = 0; y < vertexresult.height; y++) {
                if(vertexresult.get(x, y) != color(0)) vertices.add(new Vector3f(x, y));
            }
        }
            System.out.println("Num of vertices: " + vertices.size());
        triangles = DelaunayTriangulation.triangulate(vertices);
        
        //-------------------------
        for (int z = 0; z < VoronoiIterations; z++) {
            //Qvoronoi vertex interpolation            
            Vector<VoronoiDiagram.Region> qvoRegs = VoronoiDiagram.getRegions(vertices, triangles);

            //Remove redundant vertices from regions result
            TreeSet<Vector3f> qvoverts = new TreeSet<>();
            int numverts = 0;
            for (int i = 0; i < qvoRegs.size(); i++) {
                numverts += qvoRegs.get(i).hull.size();
                for (int j = 0; j < qvoRegs.get(i).hull.size(); j++) {
                    qvoverts.add(qvoRegs.get(i).hull.get(j));
                }
            }
            System.out.println("Num of new vertices: " + qvoverts.size() + "; org size: " + numverts);

            //Get average distance between qvoverts and surrounding vertices
            qvovertsExt = new TreeSet<>();
            for (Iterator<Vector3f> i = qvoverts.iterator(); i.hasNext();) {
                final Vector3fExt v = new Vector3fExt(i.next());
                Vector<Vector3f> sortedVerts = (Vector<Vector3f>) vertices.clone();
                Collections.sort(sortedVerts, new Comparator<Vector3f>() {
                    @Override
                    public int compare(Vector3f o1, Vector3f o2) {
                        return (int)((o1.distance(v) - o2.distance(v)) * 10000);
                    }                    
                });
                for (int j = 0; j < takeNavg; j++) {
                    v.dist += sortedVerts.get(j).distance(v);
                }
                v.dist /= takeNavg;
                qvovertsExt.add(v);
            }
            
            //Get min & max dist between qvoverts and vertices for z-interpolation
            float globalmindist = Float.POSITIVE_INFINITY, globalmaxdist = Float.NEGATIVE_INFINITY;
            ArrayList<Vector3fExt> sortedVerts = new ArrayList<>(qvovertsExt);
            Collections.sort(sortedVerts, new Comparator<Vector3fExt>() {
                @Override
                public int compare(Vector3fExt o1, Vector3fExt o2) {
                    return (int)((o1.dist - o2.dist) * 10000);
                }
            });
            globalmindist = sortedVerts.get(0).dist;
            globalmaxdist = sortedVerts.get(sortedVerts.size()-1).dist;
            qvovertsExt = new TreeSet<>(sortedVerts.subList((int)(sortedVerts.size()*delLowerNpercent/100), sortedVerts.size() - 1));

            //Interpolate z-value
            for (Iterator<Vector3fExt> i = qvovertsExt.iterator(); i.hasNext();) {
                Vector3fExt v = i.next();
                v.z = maxZ * (v.dist - globalmindist) / (globalmaxdist - globalmindist);
                //System.out.println(v.z);
            }

            //Add to other vertices and re-triangulate
            oldvertices = (Vector<Vector3f>) vertices.clone();
            vertices.addAll(qvovertsExt);
            triangles = DelaunayTriangulation.triangulate(vertices);
        }
        sync();
        working = false;
        p.unlock();
        }
        
        private void sync() {
            lock.lock();
            try {
                reference.vertices = vertices;
                reference.oldvertices = oldvertices;
                reference.qvovertsExt = qvovertsExt;
                reference.triangles = triangles;
                
                reference.edgeresult = edgeresult;
            } finally {lock.unlock();}
        }
        
    }
    
    private class Vector3fExt extends Vector3f {
        private float dist;
        
        Vector3fExt(Vector3f v) {
            super(v);
            dist = 0;
        }
    }

    private float rotation;

    public void draw() {
        background(0);
        
        //Important for ControlP5
        hint(ENABLE_DEPTH_TEST);
        controls.draw();
        hint(DISABLE_DEPTH_TEST);
        
        if (lighting && !export) {
            directionalLight(300, 250, 200, 0, -1, -1);
            ambientLight(102, 102, 102);
        }
       
        pushMatrix();
            translate(100, 0);        
            
            translate(x, y);
            scale(scale);            
            
            arcball.update();
            
            if(showt && edgeresult != null) image(edgeresult,0,0);
                
            if (export) {
                //PLYExporter.VERTEX_SCALE = 0.001f;
                beginRaw(PLYExporter.class.getName(), "export.ply");
            }            
            
            if (showt) {
                noFill();
                strokeWeight(1);
                stroke(50,50,150,200);
                
                lock.lock();
                try {
                    drawDelaunay(vertices, triangles);
                } finally {
                    lock.unlock();
                }              
            } else {
                noStroke();
                
                lock.lock();
                try {
                    drawDelaunayColorInterpolation(vertices, triangles);
                } finally {
                    lock.unlock();
                }                    
            }
            
            if(export) {
                endRaw();
                export = false;
                System.out.println("Captured.");
            }            
            
            if (showv) {
                stroke(255, 0, 0, 255);
                for (Vector3f v : oldvertices) {
                    drawCross(v);
                }
                
                stroke(0, 200, 0, 255);
                for (Vector3f v : qvovertsExt) {
                    drawCross(v);
                }
            }            
            
                strokeWeight(3);
                stroke(127,127,0,200);
                line(0,0,500,0);
                line(0,0,0,500);
                line(0,500,500,500);
                line(500,0,500,500);
                
        popMatrix();
        
        pushMatrix();
        if (working) {
            fill(255);
            translate(800, 400);
            rotateY(radians(rotation));
            rotation += 3;
            text("Processing...", 0, 0);
        }
        popMatrix();        
    }
    
    private int x = 0, y = 0;
    private float scale = 1.0f;
    
    public void keyPressed() {
        if(key == 't') showt = !showt;
        if(key == 'v') showv = !showv;
        if(key == 'l') lighting = !lighting;        
        if(key == 'e') export = true;
        if(key == 'r') {
            x = y = 0;
            scale = 1.0f;
        }
        
        if(key == 'w') y+=10;
        if(key == 'a') x+=10;
        if(key == 's') y-=10;
        if(key == 'd') x-=10;
        if(key == '+') scale+=0.05;
        if(key == '-') scale-=0.05;
    }
    
    private void drawCross(Vector3f v) {
        final float o = 2.0f;
        line(v.x - o, v.y, v.z, v.x + o, v.y, v.z);
        line(v.x, v.y - o, v.z, v.x, v.y + o, v.z);
        line(v.x, v.y, v.z - o, v.x, v.y, v.z + o);
    }
    
    private void drawDelaunayColorInterpolation(final Vector<Vector3f> vertices, final Vector<DelaunayTriangulation.Triangle> mDelaunayTriangles) {
        /* draw delaunay trinangles */
        if (mDelaunayTriangles != null) {
            beginShape(TRIANGLES);
            for (int i = 0; i < mDelaunayTriangles.size(); i++) {
                Vector3f[] tri = {vertices.get(mDelaunayTriangles.get(i).p[0]),
                        vertices.get(mDelaunayTriangles.get(i).p[1]),
                        vertices.get(mDelaunayTriangles.get(i).p[2])
                };
                Vector3f[] barVecs = {
                    (Vector3f)tri[0].clone(), (Vector3f)tri[0].clone(),
                    (Vector3f)tri[1].clone(), (Vector3f)tri[1].clone(),
                    (Vector3f)tri[2].clone(), (Vector3f)tri[2].clone()
                };
                barVecs[0].scale(2.0f/3); barVecs[1].scale(1.0f/6);
                barVecs[2].scale(2.0f/3); barVecs[3].scale(1.0f/6);
                barVecs[4].scale(2.0f/3); barVecs[5].scale(1.0f/6);
                
                Vector3f[] pixels = new Vector3f[3];
                pixels[0] = new Vector3f(); pixels[1] = new Vector3f(); pixels[2] = new Vector3f();

                pixels[0].add(barVecs[0]); pixels[0].add(barVecs[3]); pixels[0].add(barVecs[5]);
                pixels[1].add(barVecs[1]); pixels[1].add(barVecs[2]); pixels[1].add(barVecs[5]);
                pixels[2].add(barVecs[1]); pixels[2].add(barVecs[3]); pixels[2].add(barVecs[4]);
                
                for (int j = 0; j < 3; j++) {
                    fill(src.get((int)pixels[j].x, (int)pixels[j].y));
                    vertex(tri[j].x, tri[j].y, tri[j].z);
                }
            }
            endShape();
        }
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
