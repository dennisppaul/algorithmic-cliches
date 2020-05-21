package de.hfkbremen.algorithmiccliches.voronoidiagram;

import processing.core.PVector;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

public class Qvoronoi {

    /*
     * install qhull ( http://www.qhull.org/ ) via homebrew ( http://brew.sh )
     * and set `QVORONOI_APP` to path to binary.
     */

    private static final float VERTEX_AT_INFINITY = -10.101f;
    public static String QVORONOI_APP = "/usr/local/bin/qvoronoi";
    public static boolean VERBOSE = false;

    public String computeDiagram(int pDimensions, PVector[] pPoints) {

        File f = new File(QVORONOI_APP);
        if (!f.exists()) {
            System.err.println("### ERROR @" + Qvoronoi.class.getCanonicalName() + " / couldn t find qvoronoi at '" + QVORONOI_APP + "'");
            System.exit(-1);
        }
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
            for (PVector pPoint : pPoints) {
                if (pDimensions == 3) {
                    String myVectorString = pPoint.x + " " + pPoint.y + " " + pPoint.z;
                    myOutputStream.write(myVectorString.getBytes());
                } else if (pDimensions == 2) {
                    String myVectorString = pPoint.x + " " + pPoint.y;
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

    public PVector[][] parseRegions(final String pRawResult, final int pDimesions) {
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
//        final int myDimensions = Integer.parseInt(mHeaderA[0]);

        /* header line 2 */
        final int LINE_HEADER_B = 1;
        final String[] mHeaderB = mResult[LINE_HEADER_B].split(" ");
        final int ELEMENT_NUMBER_OF_VERTICES = 0;
        final int ELEMENT_NUMBER_OF_REGIONS = 1;
//        final int ELEMENT_NUMBER_OF_RIDGES = 2;
        final int mNumberOfVertices = Integer.parseInt(mHeaderB[ELEMENT_NUMBER_OF_VERTICES]);
        final int mNumberOfRegions = Integer.parseInt(mHeaderB[ELEMENT_NUMBER_OF_REGIONS]);
//        final int mNumberOfRidges = Integer.parseInt(mHeaderB[ELEMENT_NUMBER_OF_RIDGES]);

        /* vertices */
        int myVertexCounter = 0;
        int mVertexAtInfinityMarker = -1;
        final int LINE_VERTEX_OFFSET = LINE_HEADER_B + 1;
        final PVector[] myVertices = new PVector[mNumberOfVertices];
        for (int i = LINE_VERTEX_OFFSET; i < mNumberOfVertices + LINE_VERTEX_OFFSET; i++) {
            final String[] mVertexStr = mResult[i].split(" ");
            final PVector mVertex = new PVector();
            if (pDimesions == 3) {
                mVertex.set(Float.parseFloat(mVertexStr[0]),
                            Float.parseFloat(mVertexStr[1]),
                            Float.parseFloat(mVertexStr[2]));
                if (mVertex.x == VERTEX_AT_INFINITY && mVertex.y == VERTEX_AT_INFINITY && mVertex.z == VERTEX_AT_INFINITY) {
                    mVertexAtInfinityMarker = myVertexCounter;
                }
            } else if (pDimesions == 2) {
                mVertex.set(Float.parseFloat(mVertexStr[0]), Float.parseFloat(mVertexStr[1]), 0);
                if (mVertex.x == VERTEX_AT_INFINITY && mVertex.y == VERTEX_AT_INFINITY) {
                    mVertexAtInfinityMarker = myVertexCounter;
                }
            }
            myVertices[myVertexCounter] = mVertex;
            myVertexCounter++;
        }

        /* faces */
//        int mRegionsCounter = 0;
        final int LINE_FACE_OFFSET = LINE_VERTEX_OFFSET + mNumberOfVertices;
        final ArrayList<PVector[]> myRegions = new ArrayList<>();
        for (int i = LINE_FACE_OFFSET; i < LINE_FACE_OFFSET + mNumberOfRegions; i++) {
            boolean mVertexAtInfinityMark = false;
            final String[] mFaces = mResult[i].split(" ");
            final int ENTRY_FACE_COUNT = 0;
            final int mNumberOfFaces = Integer.parseInt(mFaces[ENTRY_FACE_COUNT]);
            final PVector[] mRegion = new PVector[mNumberOfFaces];
            for (int j = 0; j < mNumberOfFaces; j++) {
                final int ENTRY_OFFSET = 1;
                final int mIndex = Integer.parseInt(mFaces[j + ENTRY_OFFSET]);
                if (mIndex == mVertexAtInfinityMarker) {
                    mVertexAtInfinityMark = true;
                }
                mRegion[j] = new PVector().set(myVertices[mIndex]);
            }
            if (!mVertexAtInfinityMark) {
                myRegions.add(mRegion);
//                mRegionsCounter++;
            }
        }

        final PVector[][] mRegionsArray = new PVector[myRegions.size()][];
        myRegions.toArray(mRegionsArray);
        return mRegionsArray;
    }

    public PVector[][] cullReagions(PVector[][] pRegions, PVector pBox) {
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
        PVector[][] myCleanRegions = new PVector[myNonCulledCounter][];
        for (int i = 0; i < myNonCulledCounter; i++) {
            myCleanRegions[i] = new PVector[pRegions[myNonCulledRegions[i]].length];
            System.arraycopy(pRegions[myNonCulledRegions[i]],
                             0,
                             myCleanRegions[i],
                             0,
                             pRegions[myNonCulledRegions[i]].length);
        }
        return myCleanRegions;
    }

    public boolean isWithInBox(PVector pVertex, PVector pBox) {
        return pVertex.x > -pBox.x / 2 && pVertex.x <= pBox.x / 2 && pVertex.y >= -pBox.y / 2 && pVertex.y <= pBox.y / 2 && pVertex.z >= -pBox.z / 2 && pVertex.z <= pBox.z / 2;
    }

    public PVector[][] calculate3(PVector[] pPoints) {
        final String mData = computeDiagram(3, pPoints);
        return parseRegions(mData, 3);
    }

    public PVector[][] calculate2(PVector[] pPoints) {
        final String mData = computeDiagram(2, pPoints);
        return parseRegions(mData, 2);
    }

    public static void main(String[] args) {
        PVector[] myTestData = new PVector[]{new PVector(-0.3871359948170853f,
                                                         0.2713311749239735f,
                                                         0.1628039158968905f),
                                             new PVector(0.3411034895376994f,
                                                         -0.3402090239048531f,
                                                         -0.1338141602331819f),
                                             new PVector(-0.1911684421170798f,
                                                         0.3928607867327655f,
                                                         0.2431358241523477f),
                                             new PVector(0.4776323890993975f,
                                                         0.1446410269029255f,
                                                         -0.03076157050068981f),
                                             new PVector(-0.3600901223756653f,
                                                         0.2623469103584902f,
                                                         -0.2269563887464564f),
                                             new PVector(0.1527006834626436f, 0.2188966181527613f, -0.422808197450482f),
                                             new PVector(-0.1407945180671705f,
                                                         0.4166124825766334f,
                                                         0.2379305424773532f),
                                             new PVector(-0.3935169844113147f,
                                                         0.1964031571024019f,
                                                         -0.2378448714183983f),
                                             new PVector(0.2665084159570797f, 0.3398643259133964f, 0.2519236078565899f),
                                             new PVector(-0.01053233822924869f,
                                                         0.4645876571310951f,
                                                         0.1845193179395177f)};

        Qvoronoi myQhull = new Qvoronoi();
        String myResult = myQhull.computeDiagram(3, myTestData);
        PVector[][] mDiagram = myQhull.parseRegions(myResult, 3);
        System.out.println(myResult);

        for (PVector[] mRegions : mDiagram) {
            for (final PVector v : mRegions) {
                System.out.println(v);
            }
            System.out.println("---");
        }
    }
}
