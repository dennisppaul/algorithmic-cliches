

package de.mathiaslam.algorithmicclichees;

import java.awt.Rectangle;
import java.util.ArrayList;
 
/**
 * @author Jacob
 * Data structure class of a QuadTree to be used for collisions
 * Inspired from: http://gamedev.tutsplus.com/tutorials/implementation/quick-tip-use-quadtrees-to-detect-likely-collisions-in-2d-space/
 */
public class QuadTree {
    //maximum number of objects before it will need to be split
    private final int MAX_OBJECTS = 10;
    private final int MAX_LEVEL = 5;//max depth level of nodes in the tree
    private int level;//the current level of the node
    private Rectangle bounds;//bound of the space of the node
    private ArrayList<Rectangle> element;//list of all my elements, being rectangles
    private QuadTree[][] nodes;//all 4 nodes for a 2D QuadTree
     
    //Constructor
    public QuadTree(int currentLevel, Rectangle bound)
    {
        level = currentLevel; bounds = bound;
        element = new ArrayList<Rectangle>();
        nodes = new QuadTree[2][2];
    }
     
    //method to clear the tree for new positions at each instances
    public synchronized void clear()
    {
        element.clear();
        for(int i = 0; i < nodes.length; i++)
        {
            for(int j = 0; j < nodes.length; j++)
            {
                nodes[i][j].clear();
            }
        }
    }
     
    //method that split a node into 4 new nodes
    private void split()
    {
        int subWidth = bounds.width/2, subHeight = bounds.height/2;
        int x = bounds.x, y = bounds.y;
         
        nodes[0][0] = new QuadTree(level+1, new Rectangle(x, y, subWidth, subHeight));
        nodes[0][1] = new QuadTree(level+1, new Rectangle(x+subWidth, y, subWidth, subHeight));
        nodes[1][0] = new QuadTree(level+1, new Rectangle(x, y+subHeight, subWidth, subHeight));
        nodes[1][1] = new QuadTree(level+1, new Rectangle(x+subWidth, y+subHeight, subWidth, subHeight));
    }
     
    /**
     * Method that will determine in which node the object belongs, if return -1
     * it means that the object will belong to the parent node since it cannot
     * fit into the child node. Index from 1 to 4 determine top for 1 and 2 and
     * bottom for 3 and 4 with 1 and 3 on left and 2 and 4 being on the right.
     */
    private int getIndex(Rectangle bound)
    {
        int index = -1;
        //midpoint of the whole area, used to divide the area in four parts
        double verticalMid = bounds.getCenterY(), horizontalMid = bounds.getCenterX();
        //Can completly fit in top quadrant
        boolean topQuad = (bound.getY() < verticalMid && bound.getY() + bound.getHeight() < verticalMid);
        //Can completly fit in bottom quadrant
        boolean botQuad = bound.getY() > verticalMid;
         
        //Can completly fit into left Quadrant
        if(bound.getX() < horizontalMid && bound.getX() + bound.getWidth() < horizontalMid)
        {
            if(topQuad){index = 1;}
            else if(botQuad){index = 3;}
        }
        //Can completly fit into right quandrant
        if(bound.getX() > horizontalMid)
        {
            if(topQuad){index = 2;}
            else if(botQuad){index = 4;}
        }
        return index;
    }
     
    /**
     * Method that will insert a rectangle representing a object of the GUI
     * at the right location into the Quadtree's child. If the node exceeds its
     * maximal capacity, it will split the node and add all rectangles in their
     * corresponding nodes.
     */
    public synchronized void insert(Rectangle object)
    {
        if(nodes != null)//if not empty
        {
            int index = getIndex(object);//get its corresponding index
            switch(index)//and add it to its corresponding position
            {
                case 1:
                    nodes[0][0].insert(object); break;
                case 2:
                    nodes[0][1].insert(object); break;
                case 3:
                    nodes[1][0].insert(object); break;
                case 4:
                    nodes[1][1].insert(object); break;
                default://if -1 in other words
                    break;
            }
            element.add(object);//add the object to this node
             
            /**
             * If this node is full and hasn't reached its maximal level limit,
             * then split the node and any object that can fit into a child
             * will be moved in the corresponding child node, otherwise will
             * be inserted in the parent node (current node).
             */
            if(element.size() > MAX_OBJECTS && level < MAX_LEVEL)
            {
                if(nodes == null){split();}//if it has no child, make some
                 
                int i = 0;//iterator into all objects and index
                while(i < element.size())
                {
                    index = getIndex(element.get(i));
                    switch(index)//and add it to its corresponding position
                    {
                        case 1:
                            nodes[0][0].insert(element.remove(i)); break;
                        case 2:
                            nodes[0][1].insert(element.remove(i)); break;
                        case 3:
                            nodes[1][0].insert(element.remove(i)); break;
                        case 4:
                            nodes[1][1].insert(element.remove(i)); break;
                        default://if -1 in other words
                            i++; break;
                    }
                }
            }
        }
    }
     
    /**
     * Method that return all objects that can collide with the given object
     */
    public synchronized ArrayList<Rectangle> retrieve(ArrayList<Rectangle> returnObj, Rectangle possibleCollision)
    {
        int index = getIndex(possibleCollision);
        //if node not empty and has reached the furthest child node
        if(index != -1 && nodes != null)
        {
            switch(index)//and add it to its corresponding position
            {
                case 1:
                    nodes[0][0].retrieve(returnObj, possibleCollision); break;
                case 2:
                    nodes[0][1].retrieve(returnObj, possibleCollision); break;
                case 3:
                    nodes[1][0].retrieve(returnObj, possibleCollision); break;
                case 4:
                    nodes[1][1].retrieve(returnObj, possibleCollision); break;
                default://if -1 in other words
                    break;
            }
        }
        returnObj.addAll(element);
         
        return returnObj;
    }
}