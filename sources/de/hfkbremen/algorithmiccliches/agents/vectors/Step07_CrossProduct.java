package de.hfkbremen.algorithmiccliches.agents.vectors;


import processing.core.PApplet;


public class Step07_CrossProduct extends PApplet {

    /*
     * vectorbasics
     * step 07 - crossproduct vectors
     */
    public void setup() {
        /* creating a vector */
        Vector2f myVector = new Vector2f();
        myVector.x = 4;
        myVector.y = 5;

        /* get the crossproduct of a vector */
        Vector2f myCrossVector = new Vector2f();
        myCrossVector.cross(myVector);
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

        void cross(Vector2f theVector) {
            /*
             *
             * the cross product does not make much sense in this context.
             * we just asume that vector b is the z-axis and the z component
             * of our vector a is 0. after simplifing we have
             * x = a.y and y = -a.x
             *
             * Vector3f b = new Vector3f(0, 0, 1);
             * Vector3f a = new Vector3f(x, y, 0);
             * x = a.y * b.z - a.z * b.y;
             * y = b.x * a.z - b.z * a.x;
             * z = a.x * b.y - a.y * b.x;
             *
             */
            x = theVector.y;
            y = -theVector.x;
        }
    }
}
