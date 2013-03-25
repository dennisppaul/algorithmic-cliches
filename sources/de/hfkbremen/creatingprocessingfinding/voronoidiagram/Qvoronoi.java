package de.hfkbremen.creatingprocessingfinding.voronoidiagram;


import mathematik.Vector3f;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Vector;


public class Qvoronoi {

    /*
     * install qhull ( http://www.qhull.org/ ) via macports ( http://www.macports.org/ )
     * 
     */
    public static String QVORONOI_APP = "/opt/local/bin/qvoronoi";

    public static boolean VERBOSE = false;

    private static final float VERTEX_AT_INFINITY = -10.101f;

    public String computeDiagram(int pDimensions, Vector3f[] pPoints) {
        try {
            /* assemble shell command */
            String myParameter = "cat - | " + QVORONOI_APP + " o";
            String[] myExecString = new String[]{"sh", "-c", myParameter};
            Process myProcess = Runtime.getRuntime().exec(myExecString);
            BufferedReader br = new BufferedReader(new InputStreamReader(myProcess.getInputStream()));

            /* assemble query */
            OutputStream myOutputStream = myProcess.getOutputStream();
            myOutputStream.write(String.valueOf(pDimensions).getBytes());
            myOutputStream.write('\n');
            myOutputStream.write(String.valueOf(pPoints.length).getBytes());
            myOutputStream.write('\n');
            for (int i = 0; i < pPoints.length; i++) {
                if (pDimensions == 3) {
                    String myVectorString = (pPoints[i].x + " " + pPoints[i].y + " " + pPoints[i].z);
                    myOutputStream.write(myVectorString.getBytes());
                } else if (pDimensions == 2) {
                    String myVectorString = (pPoints[i].x + " " + pPoints[i].y);
                    myOutputStream.write(myVectorString.getBytes());
                }
                myOutputStream.write('\n');
            }
            myOutputStream.close();

            /* collect result */
            final StringBuilder myResult = new StringBuilder();
            String myLine;
            while ((myLine = br.readLine()) != null) {
                myResult.append(myLine);
                myResult.append('\n');
                if (VERBOSE) {
                    System.out.println(myLine);
                }
            }

            return myResult.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Vector3f[][] parseRegions(final String pRawResult, final int pDimesions) {
//        if (pDimesions == 2) {
//        System.out.println("---\n" + pRawResult);
//        }

        final String[] mResult = pRawResult.split("\n");
        /* remove space */
        for (int i = 0; i < mResult.length; i++) {
            mResult[i] = mResult[i].trim().replaceAll(" +", " ");
        }

        /* header line 1 */
        final int LINE_HEADER_A = 0;
        final String[] mHeaderA = mResult[LINE_HEADER_A].split(" ");
        if (mHeaderA.length == 0 || mHeaderA[0].isEmpty()) {
            return null;
        }
        final int myDimensions = Integer.parseInt(mHeaderA[0]);

        /* header line 2 */
        final int LINE_HEADER_B = 1;
        final String[] mHeaderB = mResult[LINE_HEADER_B].split(" ");
        final int ELEMENT_NUMBER_OF_VERTICES = 0;
        final int ELEMENT_NUMBER_OF_REGIONS = 1;
        final int ELEMENT_NUMBER_OF_RIDGES = 2;
        final int mNumberOfVertices = Integer.parseInt(mHeaderB[ELEMENT_NUMBER_OF_VERTICES]);
        final int mNumberOfRegions = Integer.parseInt(mHeaderB[ELEMENT_NUMBER_OF_REGIONS]);
        final int mNumberOfRidges = Integer.parseInt(mHeaderB[ELEMENT_NUMBER_OF_RIDGES]);

        /* vertices */
        int myVertexCounter = 0;
        int mVertexAtInfinityMarker = -1;
        final int LINE_VERTEX_OFFSET = LINE_HEADER_B + 1;
        final Vector3f[] myVertices = new Vector3f[mNumberOfVertices];
        for (int i = LINE_VERTEX_OFFSET; i < mNumberOfVertices + LINE_VERTEX_OFFSET; i++) {
            final String[] mVertexStr = mResult[i].split(" ");
            final Vector3f mVertex = new Vector3f();
            if (pDimesions == 3) {
                mVertex.set(Float.parseFloat(mVertexStr[0]),
                        Float.parseFloat(mVertexStr[1]),
                        Float.parseFloat(mVertexStr[2]));
                if (mVertex.x == VERTEX_AT_INFINITY
                        && mVertex.y == VERTEX_AT_INFINITY
                        && mVertex.z == VERTEX_AT_INFINITY) {
                    mVertexAtInfinityMarker = myVertexCounter;
                }
            } else if (pDimesions == 2) {
                mVertex.set(Float.parseFloat(mVertexStr[0]),
                        Float.parseFloat(mVertexStr[1]),
                        0);
                if (mVertex.x == VERTEX_AT_INFINITY
                        && mVertex.y == VERTEX_AT_INFINITY) {
                    mVertexAtInfinityMarker = myVertexCounter;
                }
            }
            myVertices[myVertexCounter] = mVertex;
            myVertexCounter++;
        }

        /* faces */
        int mRegionsCounter = 0;
        final int LINE_FACE_OFFSET = LINE_VERTEX_OFFSET + mNumberOfVertices;
        final Vector<Vector3f[]> myRegions = new Vector<Vector3f[]>();
        for (int i = LINE_FACE_OFFSET; i < LINE_FACE_OFFSET + mNumberOfRegions; i++) {
            boolean mVertexAtInfinityMark = false;
            final String[] mFaces = mResult[i].split(" ");
            final int ENTRY_FACE_COUNT = 0;
            final int mNumberOfFaces = Integer.parseInt(mFaces[ENTRY_FACE_COUNT]);
            final Vector3f[] mRegion = new Vector3f[mNumberOfFaces];
            for (int j = 0; j < mNumberOfFaces; j++) {
                final int ENTRY_OFFSET = 1;
                final int mIndex = Integer.parseInt(mFaces[j + ENTRY_OFFSET]);
                if (mIndex == mVertexAtInfinityMarker) {
                    mVertexAtInfinityMark = true;
                }
                mRegion[j] = new Vector3f(myVertices[mIndex]);
            }
            if (!mVertexAtInfinityMark) {
                myRegions.add(mRegion);
                mRegionsCounter++;
            }
        }

        final Vector3f[][] mRegionsArray = new Vector3f[myRegions.size()][];
        myRegions.toArray(mRegionsArray);
        return mRegionsArray;
    }

    public Vector3f[][] cullReagions(Vector3f[][] pRegions, Vector3f pBox) {
        int[] myNonCulledRegions = new int[pRegions.length];
        int myNonCulledCounter = 0;
        for (int i = 0; i < pRegions.length; i++) {
            boolean isInArea = true;
            for (int j = 0; j < pRegions[i].length; j++) {
                isInArea = isWithInBox(pRegions[i][j], pBox);
                if (!isInArea) {
                    break;
                }
            }
            if (isInArea) {
                myNonCulledRegions[myNonCulledCounter] = i;
                myNonCulledCounter++;
            }
        }
        Vector3f[][] myCleanRegions = new Vector3f[myNonCulledCounter][];
        for (int i = 0; i < myNonCulledCounter; i++) {
            myCleanRegions[i] = new Vector3f[pRegions[myNonCulledRegions[i]].length];
            System.arraycopy(pRegions[myNonCulledRegions[i]], 0,
                    myCleanRegions[i], 0,
                    pRegions[myNonCulledRegions[i]].length);
        }
        return myCleanRegions;
    }

    public boolean isWithInBox(Vector3f pVertex, Vector3f pBox) {
        if (pVertex.x > -pBox.x / 2
                && pVertex.x <= pBox.x / 2
                && pVertex.y >= -pBox.y / 2
                && pVertex.y <= pBox.y / 2
                && pVertex.z >= -pBox.z / 2
                && pVertex.z <= pBox.z / 2) {
            return true;
        }
        return false;
    }

    public Vector3f[][] calculate3(Vector3f[] pPoints) {
        final String mData = computeDiagram(3, pPoints);
        return parseRegions(mData, 3);
    }

    public Vector3f[][] calculate2(Vector3f[] pPoints) {
        final String mData = computeDiagram(2, pPoints);
        return parseRegions(mData, 2);
    }

    public static void main(String[] args) {
        Vector3f[] myTestData = new Vector3f[]{
            new Vector3f(-0.3871359948170853f, 0.2713311749239735f, 0.1628039158968905f),
            new Vector3f(0.3411034895376994f, -0.3402090239048531f, -0.1338141602331819f),
            new Vector3f(-0.1911684421170798f, 0.3928607867327655f, 0.2431358241523477f),
            new Vector3f(0.4776323890993975f, 0.1446410269029255f, -0.03076157050068981f),
            new Vector3f(-0.3600901223756653f, 0.2623469103584902f, -0.2269563887464564f),
            new Vector3f(0.1527006834626436f, 0.2188966181527613, -0.422808197450482f),
            new Vector3f(-0.1407945180671705f, 0.4166124825766334f, 0.2379305424773532f),
            new Vector3f(-0.3935169844113147f, 0.1964031571024019f, -0.2378448714183983f),
            new Vector3f(0.2665084159570797f, 0.3398643259133964f, 0.2519236078565899f),
            new Vector3f(-0.01053233822924869, 0.4645876571310951f, 0.1845193179395177f)};

        Qvoronoi myQhull = new Qvoronoi();
        String myResult = myQhull.computeDiagram(3, myTestData);
        Vector3f[][] mDiagram = myQhull.parseRegions(myResult, 3);
        System.out.println(myResult);

        for (int i = 0; i < mDiagram.length; i++) {
            Vector3f[] mRegions = mDiagram[i];
            for (int j = 0; j < mRegions.length; j++) {
                final Vector3f v = mRegions[j];
                System.out.println(v);
            }
            System.out.println("---");
        }
    }
}
