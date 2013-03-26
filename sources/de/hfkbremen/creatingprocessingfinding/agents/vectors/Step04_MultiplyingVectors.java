package de.hfkbremen.creatingprocessingfinding.agents.vectors;


import processing.core.PApplet;


public class Step04_MultiplyingVectors extends PApplet {

    /*
     * vectorbasics
     * step 04 - multiplying vectors
     */
    public void setup() {
        /* creating a vector */
        Vector2f myVector2f = new Vector2f();
        myVector2f.x = 4;
        myVector2f.y = 5;

        /* multiplying one vector with a value */
        myVector2f.multiply(2);
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

        void multiply(float s) {
            x *= s;
            y *= s;
        }
    }
}
