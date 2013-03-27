package de.hfkbremen.creatingprocessingfinding.sketch01;

import mathematik.Random;
import processing.core.PApplet;
import processing.core.PVector;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vincent
 */

public class particle {
  PVector position;
  PVector velocity;
  PVector angle;
  PVector angularVelocity;
  float radius;
  float life;
  boolean alive;
 
  public particle() {
    position=new PVector();
    velocity=new PVector();
    life=0;
    angle=new PVector();
    angularVelocity=new PVector();
    alive=false;
  }
 
  public particle(PVector position) {
    this.position=position;
    velocity=new PVector();
    radius=(float)Random.FLOAT(3, 1);
    life=(int) Random.FLOAT(10,1000);
    angle=new PVector();
    angularVelocity=new PVector((float) Random.FLOAT(0, (float) 0.05),(float) Random.FLOAT(0, (float) 0.05),(float)Random.FLOAT(0, (float) 0.05));
    alive=true;
  }
 
  public particle(PVector position, PVector velocity) {
    this.position=position;
    this.velocity=velocity;
    radius=(float) Random.FLOAT(3,10);
    life=(int) Random.FLOAT(10,1000);
    angle=new PVector();
    angularVelocity=new PVector((float) Random.FLOAT(0, (float) 0.05),(float) Random.FLOAT(0, (float) 0.05),(float) Random.FLOAT(0, (float) 0.05));
    alive=true;
  }
 
  public void move() {
    position=PVector.add(position,velocity);
    angle=PVector.add(angularVelocity,angle);
    life--;
    if(life<0)
      life=0;
  }
  public void plot() {
    pushMatrix();
    translate(position.x,position.y,position.z);
      pushMatrix();
      rotateX(angle.x);
        pushMatrix();
          rotateY(angle.y);
          pushMatrix();
            rotateZ(angle.z);
            box(radius);
          popMatrix();
        popMatrix();
      popMatrix();
    popMatrix();
  }

    private void pushMatrix() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void translate(float x, float y, float z) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void rotateX(float x) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void rotateY(float y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void box(float radius) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void rotateZ(float z) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void popMatrix() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
