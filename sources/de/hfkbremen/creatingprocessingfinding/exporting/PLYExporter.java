package de.hfkbremen.creatingprocessingfinding.exporting;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import processing.core.PGraphics;
import processing.core.PVector;


/**
 * very simple PLY exporter, based on the DXF exporter.<br/>
 * - converts lines into rectangular tubes<br/>
 * - supports vertex coloring<br/>
 * - does not support texturing<br/>
 * - only supports triangles ( and what is what is made out of it ) and
 * lines<br/>
 */
public class PLYExporter extends PGraphics {

    /*
     * RawDXF - Code to write DXF files with beginRaw/endRaw
     * An extension for the Processing project - http://processing.org
     * <p/>
     * This library is free software; you can redistribute it and/or
     * modify it under the terms of the GNU Lesser General Public
     * License as published by the Free Software Foundation; either
     * version 2.1 of the License, or (at your option) any later version.
     * <p/>
     * This library is distributed in the hope that it will be useful,
     * but WITHOUT ANY WARRANTY; without even the implied warranty of
     * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
     * Lesser General Public License for more details.
     * <p/>
     * You should have received a copy of the GNU Lesser General
     * Public License along with the Processing project; if not,
     * write to the Free Software Foundation, Inc., 59 Temple Place,
     * Suite 330, Boston, MA  02111-1307  USA
     */
    /* a very simple PLY file for testing purposes
     *
     ply
     format ascii 1.0           { ascii/binary, format version number }
     element vertex 3           { define "vertex" element, 3 of them in file }
     property float x           { vertex contains float "x" coordinate }
     property float y           { y coordinate is also a vertex property }
     property float z           { z coordinate, too }
     property uchar red         { start of vertex color }
     property uchar green
     property uchar blue
     property uchar alpha
     element face 1             { there is 1 "face" elements in the file }
     property list uchar int vertex_index { "vertex_indices" is a list of ints }
     end_header                 { delimits the end of the header }
     0 0 0 255 0 0 255              { start of vertex list }
     0 0 1 0 255 0 127
     0 1 1 0 0 255 0
     3 0 1 2                    { start of face list }
     *
     */
    private File file;

    private PrintWriter writer;

    private StringBuffer mVertices = new StringBuffer();

    private StringBuffer mFaces = new StringBuffer();

    private int mVertexCounterPLY = 0;

    public static float VERTEX_SCALE = 1.0f;

    public void setPath(String path) {
        this.path = path;
        if (path != null) {
            file = new File(path);
            if (!file.isAbsolute()) {
                file = null;
            }
        }
        if (file == null) {
            throw new RuntimeException(PLYExporter.class.getSimpleName()
                    + " requires an absolute path "
                    + "for the location of the output file.");
        }
    }

    public void dispose() {
        writeHeader();
        writeData();

        writer.flush();
        writer.close();
        writer = null;
    }

    public boolean displayable() {
        return false;
    }

    public boolean is2D() {
        return false;
    }

    public boolean is3D() {
        return true;
    }

    public void beginDraw() {
        if (writer == null) {
            try {
                writer = new PrintWriter(new FileWriter(file));
            } catch (IOException e) {
                throw new RuntimeException(e);  // java 1.4+
            }
        }
    }

    public void endDraw() {
        writer.flush();
    }

    private void writeHeader() {
        writer.println("ply");
        writer.println("format ascii 1.0");
        writer.println("element vertex " + mVertexCounterPLY);
        writer.println("property float x");
        writer.println("property float y");
        writer.println("property float z");
        writer.println("property uchar red");
        writer.println("property uchar green");
        writer.println("property uchar blue");
        writer.println("property uchar alpha");
        writer.println("element face " + mVertexCounterPLY / 3);
        writer.println("property list uchar int vertex_index");
        writer.println("end_header");
    }

    protected void writeLine(int index1, int index2) {
        /* write line as 3D shape by using triangle emitter */

        PVector vA = new PVector(vertices[index1][X], vertices[index1][Y], vertices[index1][Z]);
        PVector vB = new PVector(vertices[index2][X], vertices[index2][Y], vertices[index2][Z]);
        vertexCount = 0;

        PVector vAB = new PVector();
        vAB.set(vB);
        vAB.sub(vA);
        vAB.normalize();

        PVector vUp = findPerpVec(vAB);
        PVector vSi = vUp.cross(vAB);

        final float mStrokeWeightA = vertices[index1][SW] / 2.0f;
        final float mStrokeWeightB = vertices[index2][SW] / 2.0f;

        /* 0 */

        vertices[0][X] = vA.x + vUp.x * mStrokeWeightA;
        vertices[0][Y] = vA.y + vUp.y * mStrokeWeightA;
        vertices[0][Z] = vA.z + vUp.z * mStrokeWeightA;

        vertices[1][X] = vB.x + vSi.x * mStrokeWeightB;
        vertices[1][Y] = vB.y + vSi.y * mStrokeWeightB;
        vertices[1][Z] = vB.z + vSi.z * mStrokeWeightB;

        vertices[2][X] = vB.x + vUp.x * mStrokeWeightB;
        vertices[2][Y] = vB.y + vUp.y * mStrokeWeightB;
        vertices[2][Z] = vB.z + vUp.z * mStrokeWeightB;

        writeLineSegment();

        vertices[0][X] = vA.x + vUp.x * mStrokeWeightA;
        vertices[0][Y] = vA.y + vUp.y * mStrokeWeightA;
        vertices[0][Z] = vA.z + vUp.z * mStrokeWeightA;

        vertices[1][X] = vA.x + vSi.x * mStrokeWeightA;
        vertices[1][Y] = vA.y + vSi.y * mStrokeWeightA;
        vertices[1][Z] = vA.z + vSi.z * mStrokeWeightA;

        vertices[2][X] = vB.x + vSi.x * mStrokeWeightB;
        vertices[2][Y] = vB.y + vSi.y * mStrokeWeightB;
        vertices[2][Z] = vB.z + vSi.z * mStrokeWeightB;

        writeLineSegment();

        /* 1 */

        vertices[0][X] = vA.x - vSi.x * mStrokeWeightA;
        vertices[0][Y] = vA.y - vSi.y * mStrokeWeightA;
        vertices[0][Z] = vA.z - vSi.z * mStrokeWeightA;

        vertices[1][X] = vA.x + vUp.x * mStrokeWeightA;
        vertices[1][Y] = vA.y + vUp.y * mStrokeWeightA;
        vertices[1][Z] = vA.z + vUp.z * mStrokeWeightA;

        vertices[2][X] = vB.x + vUp.x * mStrokeWeightB;
        vertices[2][Y] = vB.y + vUp.y * mStrokeWeightB;
        vertices[2][Z] = vB.z + vUp.z * mStrokeWeightB;

        writeLineSegment();

        vertices[0][X] = vA.x - vSi.x * mStrokeWeightA;
        vertices[0][Y] = vA.y - vSi.y * mStrokeWeightA;
        vertices[0][Z] = vA.z - vSi.z * mStrokeWeightA;

        vertices[1][X] = vB.x + vUp.x * mStrokeWeightB;
        vertices[1][Y] = vB.y + vUp.y * mStrokeWeightB;
        vertices[1][Z] = vB.z + vUp.z * mStrokeWeightB;

        vertices[2][X] = vB.x - vSi.x * mStrokeWeightB;
        vertices[2][Y] = vB.y - vSi.y * mStrokeWeightB;
        vertices[2][Z] = vB.z - vSi.z * mStrokeWeightB;

        writeLineSegment();

        /* 2 */

        vertices[0][X] = vA.x + vSi.x * mStrokeWeightA;
        vertices[0][Y] = vA.y + vSi.y * mStrokeWeightA;
        vertices[0][Z] = vA.z + vSi.z * mStrokeWeightA;

        vertices[1][X] = vA.x - vUp.x * mStrokeWeightA;
        vertices[1][Y] = vA.y - vUp.y * mStrokeWeightA;
        vertices[1][Z] = vA.z - vUp.z * mStrokeWeightA;

        vertices[2][X] = vB.x - vUp.x * mStrokeWeightB;
        vertices[2][Y] = vB.y - vUp.y * mStrokeWeightB;
        vertices[2][Z] = vB.z - vUp.z * mStrokeWeightB;

        writeLineSegment();

        vertices[0][X] = vA.x + vSi.x * mStrokeWeightA;
        vertices[0][Y] = vA.y + vSi.y * mStrokeWeightA;
        vertices[0][Z] = vA.z + vSi.z * mStrokeWeightA;

        vertices[1][X] = vB.x - vUp.x * mStrokeWeightB;
        vertices[1][Y] = vB.y - vUp.y * mStrokeWeightB;
        vertices[1][Z] = vB.z - vUp.z * mStrokeWeightB;

        vertices[2][X] = vB.x + vSi.x * mStrokeWeightB;
        vertices[2][Y] = vB.y + vSi.y * mStrokeWeightB;
        vertices[2][Z] = vB.z + vSi.z * mStrokeWeightB;

        writeLineSegment();

        /* 3 */

        vertices[0][X] = vA.x - vUp.x * mStrokeWeightA;
        vertices[0][Y] = vA.y - vUp.y * mStrokeWeightA;
        vertices[0][Z] = vA.z - vUp.z * mStrokeWeightA;

        vertices[1][X] = vA.x - vSi.x * mStrokeWeightA;
        vertices[1][Y] = vA.y - vSi.y * mStrokeWeightA;
        vertices[1][Z] = vA.z - vSi.z * mStrokeWeightA;

        vertices[2][X] = vB.x - vSi.x * mStrokeWeightB;
        vertices[2][Y] = vB.y - vSi.y * mStrokeWeightB;
        vertices[2][Z] = vB.z - vSi.z * mStrokeWeightB;

        writeLineSegment();

        vertices[0][X] = vA.x - vUp.x * mStrokeWeightA;
        vertices[0][Y] = vA.y - vUp.y * mStrokeWeightA;
        vertices[0][Z] = vA.z - vUp.z * mStrokeWeightA;

        vertices[1][X] = vB.x - vSi.x * mStrokeWeightB;
        vertices[1][Y] = vB.y - vSi.y * mStrokeWeightB;
        vertices[1][Z] = vB.z - vSi.z * mStrokeWeightB;

        vertices[2][X] = vB.x - vUp.x * mStrokeWeightB;
        vertices[2][Y] = vB.y - vUp.y * mStrokeWeightB;
        vertices[2][Z] = vB.z - vUp.z * mStrokeWeightB;

        writeLineSegment();
    }

    private static PVector findPerpVec(PVector v) {
        /* find a non-parallel vector */
        PVector mUp = new PVector();
        if (v.x == 0.0f && v.y == 0.0f && v.z == 0.0f) {
            mUp.set(1.0f, 0.0f, 0.0f);
        } else if (v.x == 1.0f && v.y == 0.0f && v.z == 0.0f) {
            mUp.set(0.0f, 1.0f, 0.0f);
        } else if (v.x == 0.0f && v.y == 1.0f && v.z == 0.0f) {
            mUp.set(0.0f, 0.0f, 1.0f);
        } else {
            mUp.set(1.0f, 0.0f, 0.0f);
        }

        PVector vP = new PVector(0, 0, 1.0f);
        final float mDot = v.dot(mUp);
        if (mDot < 1.0f && mDot > -1.0f) {
            PVector.cross(v, mUp, vP);
        } else {
            System.out.println("### " + PLYExporter.class.getSimpleName() + " / problem finding perp vector. ");
        }

        return vP;
    }

    private void writeTriangle() {
        writeFace(false);
    }

    private void writeLineSegment() {
        writeFace(true);
    }

    private void writeFace(boolean pStrokeColor) {
        appendVertex(vertices[0], pStrokeColor);
        appendVertex(vertices[1], pStrokeColor);
        appendVertex(vertices[2], pStrokeColor);
        appendFace();

        mVertexCounterPLY += 3;
        vertexCount = 0;
    }

    private void appendFace() {
        mFaces.append("3");
        mFaces.append(" ");
        mFaces.append(mVertexCounterPLY + 0);
        mFaces.append(" ");
        mFaces.append(mVertexCounterPLY + 1);
        mFaces.append(" ");
        mFaces.append(mVertexCounterPLY + 2);
        mFaces.append("\n");
    }

    private void appendVertex(float[] v, boolean useStrokeColor) {
        mVertices.append(v[X] * VERTEX_SCALE);
        mVertices.append(" ");
        mVertices.append(v[Y] * VERTEX_SCALE);
        mVertices.append(" ");
        mVertices.append(v[Z] * VERTEX_SCALE);
        mVertices.append(" ");
        mVertices.append((int) (v[useStrokeColor ? SR : R] * 255));
        mVertices.append(" ");
        mVertices.append((int) (v[useStrokeColor ? SG : G] * 255));
        mVertices.append(" ");
        mVertices.append((int) (v[useStrokeColor ? SB : B] * 255));
        mVertices.append(" ");
        mVertices.append((int) (v[useStrokeColor ? SA : A] * 255));
        mVertices.append("\n");
    }

    private void writeData() {
        writer.print(mVertices.toString());
        writer.print(mFaces.toString());
    }

    public void beginShape(int kind) {
        shape = kind;

        if ((shape != LINES)
                && (shape != TRIANGLES)
                && (shape != POLYGON)) {
            String err =
                    PLYExporter.class.getSimpleName() + "can only be used with beginRaw(), "
                    + "because it only supports lines and triangles";
            throw new RuntimeException(err);
        }

        if ((shape == POLYGON) && fill) {
            throw new RuntimeException(PLYExporter.class.getSimpleName() + "only supports non-filled shapes.");
        }

        vertexCount = 0;
    }

    public void vertex(float x, float y) {
        vertex(x, y, 0);
    }

    public void vertex(float x, float y, float z) {
        float[] vertex = vertices[vertexCount];

        vertex[X] = x;
        vertex[Y] = y;
        vertex[Z] = z;

        if (fill) {
            vertex[R] = fillR;
            vertex[G] = fillG;
            vertex[B] = fillB;
            vertex[A] = fillA;
        }

        if (stroke) {
            vertex[SR] = strokeR;
            vertex[SG] = strokeG;
            vertex[SB] = strokeB;
            vertex[SA] = strokeA;
            vertex[SW] = strokeWeight;
        }

        if (textureImage != null) {
            vertex[U] = textureU;
            vertex[V] = textureV;
        }
        vertexCount++;

        if ((shape == LINES) && (vertexCount == 2)) {
            writeLine(0, 1);
            vertexCount = 0;
        } else if ((shape == TRIANGLES) && (vertexCount == 3)) {
            writeTriangle();
        }
    }

    public void endShape(int mode) {
        if (shape == POLYGON) {
            for (int i = 0; i < vertexCount - 1; i++) {
                writeLine(i, i + 1);
            }
            if (mode == CLOSE) {
                writeLine(vertexCount - 1, 0);
            }
        }
    }
}
