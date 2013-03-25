/*
 * from http://hipstersinc.com/p5sunflow/cameras/
 */

import hipstersinc.sunflow.*;
import hipstersinc.sunflow.shader.*;
import hipstersinc.*;

// Set up the scene, make sure we don't loop
void setup() {
  size(510, 300, "hipstersinc.P5Sunflow");
  noStroke();
  noLoop();
}

// Draw our scene
void draw() {
  setupCamera();

  background(255);

  // Adjust perspective
  translate(width/2, height/2, 100);
  rotateX(QUARTER_PI);
  rotateZ(QUARTER_PI);
  translate(-width/2, -height/2);

  // Draw some boxes
  float boxSize = width / 11;
  translate(boxSize, boxSize);
  for(int i=0; i<3; i++) {
    for(int j=0; j<5; j++) {
      pushMatrix();
      translate(j*boxSize, j*boxSize);

      fill(j * 50);
      box(boxSize);
      popMatrix();
    }
    translate(boxSize * 2, 0);
  }
}


// Tweak our camera parameters.  Make sure to call this from `draw`
void setupCamera() {
  // Get the P5Sunflow PGraphics
  P5Sunflow sunflow = (P5Sunflow) g;

  // Set our camera to "pinhole"
  sunflow.camera.setType(SunflowCamera.PINHOLE);
}

/*

// Pinhole
void setupCamera() {
  P5Sunflow sunflow = (P5Sunflow) g;
  sunflow.camera.setType(SunflowCamera.PINHOLE);
}

// Thinlens
void setupCamera() {
  P5Sunflow sunflow = (P5Sunflow) g;
  sunflow.camera.setType(SunflowCamera.THINLENS);
}

// Spherical
void setupCamera() {
  P5Sunflow sunflow = (P5Sunflow) g;
  sunflow.camera.setType(SunflowCamera.SPHERICAL);
}

// Fisheye
void setupCamera() {
  P5Sunflow sunflow = (P5Sunflow) g;
  sunflow.camera.setType(SunflowCamera.FISHEYE);
}

// Depth of Field
void setupCamera() {
  P5Sunflow sunflow = (P5Sunflow) g;

  // Make sure we have a thinlens
  sunflow.camera.setType(SunflowCamera.THINLENS);

  // Focus it somewhere around the center of the scene.  This
  // often just has to be tweaked by hand to your liking.
  sunflow.camera.setFocalDistance(450);

  // Put a huge lens on the camera.  This too will often have
  // to be tweaked by hand.
  sunflow.camera.setLensRadius(12f);

  // Now we set the Anti-Aliasing sampling.
  sunflow.scene.setAaSamples(12);
}
*/
