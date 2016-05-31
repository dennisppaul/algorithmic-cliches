package de.hfkbremen.algorithmiccliches.agents.vectors;

import processing.core.PApplet;

public class Step01_WhatDoesAVectorLookLike extends PApplet {

    /*
     * vectorbasics
     * step 01 - what does a vector look like
     */
    public void setup() {
        /* creating a 2dimensional vector */
        Vector2f myVector2f = new Vector2f();
        myVector2f.x = 3;
        myVector2f.y = 2;
    }

    class Vector2f {

        float x = 0;

        float y = 0;

    }
}
