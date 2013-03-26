package de.hfkbremen.creatingprocessingfinding.agents.vectors;


import processing.core.PApplet;


public class Step02_AddingVectors extends PApplet {
    /*
     * vectorbasics
     * step 02 - adding vectors
     */

    public void setup() {
        /* creating a vector */
        Vector2f myVector = new Vector2f();
        myVector.x = 4;
        myVector.y = 5;

        /* creating anonther vector */
        Vector2f myOtherVector = new Vector2f();
        myOtherVector.x = 1;
        myOtherVector.y = 2;

        /* adding one vector to the other */
        myVector.add(myOtherVector);
    }

    class Vector2f {

        float x = 0;

        float y = 0;

        void add(Vector2f theVector) {
            x += theVector.x;
            y += theVector.y;
        }
    }
}
