

package de.hfkbremen.creatingprocessingfinding.voronoidiagram;


import mathematik.Vector3f;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;


public class Qvoronoi {

    public static String QVORONOI_APP = "/opt/local/bin/qvoronoi";

    public static boolean VERBOSE = false;


    public String computeDiagram(int pDimensions, Vector3f[] pPoints) {
        try {
            /* assemble shell command */
            String myParameter = "cat - | " + QVORONOI_APP + " o";
            String[] myExecString = new String[] {"sh", "-c", myParameter};
            Process myProcess = Runtime.getRuntime().exec(myExecString);
            BufferedReader br = new BufferedReader(new InputStreamReader(myProcess.getInputStream()));

            /* assemble query */
            OutputStream myOutputStream = myProcess.getOutputStream();
            myOutputStream.write(String.valueOf(pDimensions).getBytes());
            myOutputStream.write('\n');
            myOutputStream.write(String.valueOf(pPoints.length).getBytes());
            myOutputStream.write('\n');
            for (int i = 0; i < pPoints.length; i++) {
                String myVectorString = (pPoints[i].x + " " + pPoints[i].y + " " + pPoints[i].z);
                myOutputStream.write(myVectorString.getBytes());
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


    public Vector3f[][] parseRegions(String pRawResult) {
        String[] myLines = pRawResult.split("\n");
        final String[] mHeaderA = myLines[0].split(" ");
        final String[] mHeaderB = myLines[1].split(" ");
        int myDimensions = Integer.parseInt(mHeaderA[0]);
        int myNumberOfVertices = Integer.parseInt(mHeaderB[0]);
        int myNumberOfFaces = Integer.parseInt(mHeaderB[1]);
        int myNumberOfRidges = Integer.parseInt(mHeaderB[2]);
        Vector3f[] myVertices = new Vector3f[myNumberOfVertices];
        Vector3f[][] myRegions = new Vector3f[myNumberOfFaces][];
        int myVertexCounter = 0;
        int myFacesCounter = 0;

        /* vertices */
        for (int i = 2; i < 2 + myNumberOfVertices; i++) {
            String[] myWords = myLines[i].split(" ");
            Vector3f myVertex = new Vector3f(Float.parseFloat(myWords[0]),
                                             Float.parseFloat(myWords[1]),
                                             Float.parseFloat(myWords[2]));
            myVertices[myVertexCounter] = myVertex;
            myVertexCounter++;
        }

        /* faces */
        for (int i = 2 + myNumberOfVertices; i < myLines.length; i++) {
            String[] myWords = myLines[i].split(" ");
            myRegions[myFacesCounter] = new Vector3f[Integer.parseInt(myWords[0])];
            for (int j = 1; j < myWords.length; j++) {
                int myIndex = Integer.parseInt(myWords[j]);
                if (myIndex == 0) {
                    myIndex = Integer.parseInt(myWords[1]);
                }
                myRegions[myFacesCounter][j - 1] = new Vector3f(myVertices[myIndex]);
            }
            myFacesCounter++;
        }

        return myRegions;
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
            System.arraycopy(pRegions[myNonCulledRegions[i]], 0, myCleanRegions[i], 0,
                             pRegions[myNonCulledRegions[i]].length);
        }
        return myCleanRegions;
    }


    public boolean isWithInBox(Vector3f pVertex, Vector3f pBox) {
        if (pVertex.x > -pBox.x / 2
                && pVertex.x < pBox.x / 2
                && pVertex.y > -pBox.y / 2
                && pVertex.y < pBox.y / 2
                && pVertex.z > -pBox.z / 2
                && pVertex.z < pBox.z / 2) {
            return true;
        }
        return false;
    }


    public Vector3f[][] calculate3(Vector3f[] pPoints) {
        final String mData = computeDiagram(3, pPoints);
        return parseRegions(mData);
    }


    public static void main(String[] args) {
        Vector3f[] myTestData = new Vector3f[] {
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
        System.out.println(myResult);
    }
}
