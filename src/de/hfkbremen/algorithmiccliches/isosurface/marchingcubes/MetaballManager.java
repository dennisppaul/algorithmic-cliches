package de.hfkbremen.algorithmiccliches.isosurface.marchingcubes;

import processing.core.PVector;
import teilchen.util.Util;
import teilchen.util.Vector3i;

import java.util.ArrayList;

public class MetaballManager {

    public boolean accumulate_energy_levels = false;
    public boolean clamp_energy_levels = false;
    public PVector dimension = new PVector(100, 100, 100);
    public float maximum_energy_level = 1.0f;
    public float minimum_energy_level = 0.0f;
    public PVector position = new PVector();
    public Vector3i resolution = new Vector3i(10, 10, 10);
    protected ArrayList<Metaball> mMetaballs = new ArrayList<>();
    protected float[][][] mForceField;
    private float mIsoValue = 0.1f;

    public float isovalue() {
        return mIsoValue;
    }

    public void isovalue(float pIsoValue) {
        mIsoValue = pIsoValue;
    }

    public ArrayList<Metaball> metaballs() {
        return mMetaballs;
    }

    public float[][][] forcefield() {
        return mForceField;
    }

    public void add(Metaball pMetaball) {
        mMetaballs.add(pMetaball);
    }

    public void remove(Metaball pMetaball) {
        mMetaballs.remove(pMetaball);
    }

    public void clear() {
        mMetaballs.clear();
    }

    public void updateLevels() {
        if (mForceField == null || mForceField.length != resolution.x || mForceField[0].length != resolution.y || mForceField[0][0].length != resolution.z) {
            mForceField = new float[resolution.x][resolution.y][resolution.z];
        }

        final PVector myDimension = new PVector(dimension.x, dimension.y, dimension.z);
        myDimension.x /= resolution.x;
        myDimension.y /= resolution.y;
        myDimension.z /= resolution.z;

        /* update field */
        updateGrid(mForceField, myDimension);
    }

    public ArrayList<PVector> createSurface() {
        if (mForceField == null || mForceField.length != resolution.x || mForceField[0].length != resolution.y || mForceField[0][0].length != resolution.z) {
            mForceField = new float[resolution.x][resolution.y][resolution.z];
        }

        final PVector myDimension = new PVector(dimension.x, dimension.y, dimension.z);
        myDimension.x /= resolution.x;
        myDimension.y /= resolution.y;
        myDimension.z /= resolution.z;

        /* update field */
        updateGrid(mForceField, myDimension);

        /* polygonize field */
//        final ArrayList<PVector> myTrianglesVertices = Polygonizer.polygonizeField(mForceField, mIsoValue);
        final ArrayList<PVector> myTrianglesVertices = IsoSurface.polygonizeField(mForceField, mIsoValue);

        /* apply scale */
        for (PVector myVertex : myTrianglesVertices) {
            Util.mult(myVertex, myDimension, myVertex);
            myVertex.add(position);
        }

        return myTrianglesVertices;
    }

    protected void updateGrid(float[][][] theForceField, PVector dimension) {
        for (int x = 0; x < theForceField.length - 1; x++) {
            for (int y = 0; y < theForceField[x].length - 1; y++) {
                for (int z = 0; z < theForceField[x][y].length - 1; z++) {
                    final PVector myPosition = new PVector(x, y, z);
                    Util.mult(myPosition, dimension, myPosition);
                    myPosition.add(position);
                    if (accumulate_energy_levels) {
                        theForceField[x][y][z] += getForceFieldValue(myPosition);
                    } else {
                        theForceField[x][y][z] = getForceFieldValue(myPosition);
                    }
                    if (clamp_energy_levels) {
                        if (theForceField[x][y][z] > maximum_energy_level) {
                            theForceField[x][y][z] = maximum_energy_level;
                        }
                        if (theForceField[x][y][z] < minimum_energy_level) {
                            theForceField[x][y][z] = minimum_energy_level;
                        }
                    }
                }
            }
        }
    }

    protected float getForceFieldValue(PVector thePosition) {
        float f = 0;
        for (final Metaball myMetaball : mMetaballs) {
            final float myDistanceSquared = Util.distanceSquared(myMetaball.position, thePosition);
            float myRadiusSquared = myMetaball.radius * myMetaball.radius;
            if (myDistanceSquared < myRadiusSquared) {
                //float fallOff = ( myRadiusSquared - distance ) /  myRadiusSquared;
                float fallOff = 1f - (myDistanceSquared / myRadiusSquared);
                f += fallOff * fallOff * myMetaball.strength;
            }
        }
        return f;
    }
}
