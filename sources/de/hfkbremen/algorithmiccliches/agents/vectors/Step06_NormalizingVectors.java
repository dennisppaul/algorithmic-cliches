package de.hfkbremen.algorithmiccliches.agents.vectors;


import processing.core.PApplet;


public class Step06_NormalizingVectors extends PApplet {

    /*
     * vectorbasics
     * step 06 - normalizing vectors
     */
    public void setup() {
        /* creating a vector */
        Vector2f myVector = new Vector2f();
        myVector.x = 4;
        myVector.y = 5;

        /* normalizing a vector */
        myVector.normalize();
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

        float length() {
            float myLengthSquard = x * x + y * y;
            float myLength = sqrt(myLengthSquard);
            return myLength;
        }

        void normalize() {
            float d = length();
            x /= d;
            y /= d;
        }
    }
}
