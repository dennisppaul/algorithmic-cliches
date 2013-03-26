package de.hfkbremen.creatingprocessingfinding.agents.vectors;


import processing.core.PApplet;


public class Step03_SubtractingVectors extends PApplet {

    /*
     * vectorbasics
     * step 03 - subtracting vectors
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

        /* subtracting one vector from the other */
        myVector.sub(myOtherVector);
    }

    class Vector2f {

        float x = 0;

        float y = 0;

        void add(Vector2f theVector) {
            x += theVector.x;
            y += theVector.y;
        }

        void sub(Vector2f theVector) {
            x -= theVector.x;
            y -= theVector.y;
        }
    }
}
