

package de.hfkbremen.creatingprocessingfinding.octree;


import mathematik.Vector3f;

import java.util.Collection;
import java.util.List;
import java.util.Vector;


public class Octree {

    /* modified version of toxi's */

    /*
     *   __               .__       .__  ._____.           
     * _/  |_  _______  __|__| ____ |  | |__\_ |__   ______
     * \   __\/  _ \  \/  /  |/ ___\|  | |  || __ \ /  ___/
     *  |  | (  <_> >    <|  \  \___|  |_|  || \_\ \\___ \ 
     *  |__|  \____/__/\_ \__|\___  >____/__||___  /____  >
     *                   \/       \/             \/     \/ 
     *
     * Copyright (c) 2006-2011 Karsten Schmidt
     * 
     * This library is free software; you can redistribute it and/or
     * modify it under the terms of the GNU Lesser General Public
     * License as published by the Free Software Foundation; either
     * version 2.1 of the License, or (at your option) any later version.
     * 
     * http://creativecommons.org/licenses/LGPL/2.1/
     * 
     * This library is distributed in the hope that it will be useful,
     * but WITHOUT ANY WARRANTY; without even the implied warranty of
     * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
     * Lesser General Public License for more details.
     * 
     * You should have received a copy of the GNU Lesser General Public
     * License along with this library; if not, write to the Free Software
     * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
     */
    /*
     * Implements a spatial subdivision tree to work efficiently with large numbers
     * of 3D particles. This octree can only be used for particle type objects and
     * does NOT support 3D mesh geometry as other forms of Octrees do.
     */
    public static final int NUMBER_OF_CHILDREN = 8;

    /*
     * alternative tree recursion limit, number of world units when cells are
     * not subdivided any further
     */
    private float mMinNodeSize = 4;

    private Octree mParent;

    private Octree[] mChildren;

    private byte mNumberOfChildren;

    private Vector<OctreeEntity> mEntities;

    private float mSize;

    private float mHalfSize;

    private Vector3f mOffset;

    private Vector3f mScale;

    private Vector3f mOrigin;

    private int mDepth = 0;

    private boolean isAutoReducing = false;


    private Octree(Octree pParentOctree,
                   Vector3f pOffset,
                   float pHalfSize) {
        mOrigin = mathematik.Util.add(pOffset, new Vector3f(pHalfSize,
                                                            pHalfSize,
                                                            pHalfSize));
        mScale = new Vector3f(pHalfSize,
                              pHalfSize,
                              pHalfSize);

        mParent = pParentOctree;
        mHalfSize = pHalfSize;
        mSize = pHalfSize * 2;
        mOffset = pOffset;
        mNumberOfChildren = 0;
        if (mParent != null) {
            mDepth = mParent.mDepth + 1;
            mMinNodeSize = mParent.mMinNodeSize;
        }
    }


    public Octree(Vector3f pOffset, float pSize) {
        /* Constructs a new Octree node within the cube volume. */
        this(null, pOffset, pSize * 0.5f);
    }


    public Vector3f origin() {
        return mOrigin;
    }


    public Vector3f scale() {
        return mScale;
    }


    public boolean addAll(Collection<OctreeEntity> mEntities) {
        boolean addedAll = true;
        for (OctreeEntity p : mEntities) {
            addedAll &= add(p);
        }
        return addedAll;
    }


    public boolean add(OctreeEntity pEntity) {
        if (isPointInBox(pEntity.position(), mOrigin, mScale)) {
            // only add entities to leaves for now
            if (mHalfSize <= mMinNodeSize) {
                if (mEntities == null) {
                    mEntities = new Vector<OctreeEntity>();
                }
                mEntities.add(pEntity);
                return true;
            } else {
                final Vector3f pLocalPosition = mathematik.Util.sub(pEntity.position(), mOffset);
                if (mChildren == null) {
                    mChildren = new Octree[NUMBER_OF_CHILDREN];
                }
                final int mOctant = getOctantID(pLocalPosition);
                if (mChildren[mOctant] == null) {
                    Vector3f off = mathematik.Util.add(mOffset,
                                                       new Vector3f((mOctant & 1) != 0 ? mHalfSize : 0,
                                                                    (mOctant & 2) != 0 ? mHalfSize : 0,
                                                                    (mOctant & 4) != 0 ? mHalfSize : 0));
                    mChildren[mOctant] = new Octree(this, off,
                                                    mHalfSize * 0.5f);
                    mNumberOfChildren++;
                }
                return mChildren[mOctant].add(pEntity);
            }
        }
        return false;
    }


    public Octree[] getChildren() {
        if (mChildren != null) {
            Octree[] clones = new Octree[NUMBER_OF_CHILDREN];
            System.arraycopy(mChildren, 0, clones, 0, NUMBER_OF_CHILDREN);
            return clones;
        }
        return null;
    }


    /**
     * @return the depth
     */
    public int getDepth() {
        return mDepth;
    }


    private Octree getLeafForEntity(OctreeEntity pEntity) {
        /* Finds the leaf node which spatially relates to the given point */
        if (isPointInBox(pEntity.position(), mOrigin, mScale)) {
            if (mNumberOfChildren > 0) {
                int octant = getOctantID(mathematik.Util.sub(pEntity.position(), mOffset));
                if (mChildren[octant] != null) {
                    return mChildren[octant].getLeafForEntity(pEntity);
                }
            } else if (mEntities != null) {
                return this;
            }
        }
        return null;
    }


    public float getNodeSize() {
        return mSize;
    }


    public int getNumChildren() {
        return mNumberOfChildren;
    }


    private int getOctantID(Vector3f pPoint) {
        /* Computes the local child octant/cube index for the given point */
        return (pPoint.x >= mHalfSize ? 1 : 0)
                + (pPoint.y >= mHalfSize ? 2 : 0)
                + (pPoint.z >= mHalfSize ? 4 : 0);
    }


    public Vector3f offset() {
        return mOffset;
    }


    private Octree parent() {
        return mParent;
    }


    public float size() {
        return mSize;
    }


    public Vector<OctreeEntity> entities() {
        Vector<OctreeEntity> results = null;
        if (mEntities != null) {
            results = new Vector<OctreeEntity>(mEntities);
        } else if (mNumberOfChildren > 0) {
            for (int i = 0; i < NUMBER_OF_CHILDREN; i++) {
                if (mChildren[i] != null) {
                    Vector<OctreeEntity> childPoints = mChildren[i].entities();
                    if (childPoints != null) {
                        if (results == null) {
                            results = new Vector<OctreeEntity>();
                        }
                        results.addAll(childPoints);
                    }
                }
            }
        }
        return results;
    }


    public Vector<OctreeEntity> getEntitiesWithinBox(Vector3f pBoxOrigin, Vector3f pBoxScale) {
        Vector<OctreeEntity> mResults = null;
        if (intersectsBox(origin(), scale(), pBoxOrigin, pBoxScale)) {
            if (mEntities != null) {
                for (OctreeEntity mEntity : mEntities) {
                    if (isPointInBox(mEntity.position(), origin(), scale())) {
                        if (mResults == null) {
                            mResults = new Vector<OctreeEntity>();
                        }
                        mResults.add(mEntity);
                    }
                }
            } else if (mNumberOfChildren > 0) {
                for (int i = 0; i < NUMBER_OF_CHILDREN; i++) {
                    if (mChildren[i] != null) {
                        Vector<OctreeEntity> mChildrenPoints = mChildren[i].getEntitiesWithinBox(pBoxOrigin, pBoxScale);
                        if (mChildrenPoints != null) {
                            if (mResults == null) {
                                mResults = new Vector<OctreeEntity>();
                            }
                            mResults.addAll(mChildrenPoints);
                        }
                    }
                }
            }
        }
        return mResults;
    }


    public Vector<OctreeEntity> getEntitesWithinSphere(Vector3f pSphereOrigin, float pSphereRadius) {
        Vector<OctreeEntity> results = null;
        if (isBoxIntersectingSphere(origin(), scale(), pSphereOrigin, pSphereRadius)) {
            if (mEntities != null) {
                for (OctreeEntity mEntity : mEntities) {
                    if (isPointInSphere(mEntity.position(), pSphereOrigin, pSphereRadius)) {
                        if (results == null) {
                            results = new Vector<OctreeEntity>();
                        }
                        results.add(mEntity);
                    }
                }
            } else if (mNumberOfChildren > 0) {
                for (int i = 0; i < NUMBER_OF_CHILDREN; i++) {
                    if (mChildren[i] != null) {
                        Vector<OctreeEntity> mChildrenPoints = mChildren[i].getEntitesWithinSphere(pSphereOrigin, pSphereRadius);
                        if (mChildrenPoints != null) {
                            if (results == null) {
                                results = new Vector<OctreeEntity>();
                            }
                            results.addAll(mChildrenPoints);
                        }
                    }
                }
            }
        }
        return results;
    }


    private void reduceBranch() {
        if (mEntities != null && mEntities.isEmpty()) {
            mEntities = null;
        }
        if (mNumberOfChildren > 0) {
            for (int i = 0; i < NUMBER_OF_CHILDREN; i++) {
                if (mChildren[i] != null && mChildren[i].mEntities == null) {
                    mChildren[i] = null;
                }
            }
        }
        if (mParent != null) {
            mParent.reduceBranch();
        }
    }


    public boolean remove(OctreeEntity pEntity) {
        /* Removes a point from the tree and (optionally) tries to release memory by reducing now empty sub-branches. */
        boolean mFoundPoint = false;
        final Octree mLeaf = getLeafForEntity(pEntity);
        if (mLeaf != null) {
            if (mLeaf.mEntities.remove(pEntity)) {
                mFoundPoint = true;
                if (isAutoReducing && mLeaf.mEntities.isEmpty()) {
                    mLeaf.reduceBranch();
                }
            }
        }
        return mFoundPoint;
    }


    public void remove(Collection<OctreeEntity> pEntities) {
        for (OctreeEntity p : pEntities) {
            remove(p);
        }
    }


    public void removeAll() {
        if (entities() != null) {
            for (OctreeEntity p : entities()) {
                remove(p);
            }
        }
    }


    public void auto_reduction(boolean pState) {
        /* Enables/disables auto reduction of branches after entities have been deleted from the tree. Turned off by default. */
        isAutoReducing = pState;
    }


    public float getMinNodeSize() {
        /*
         * Returns the minimum size of nodes (in world units). This value acts as
         * tree recursion limit since nodes smaller than this size are not
         * subdivided further. Leaf node are always smaller or equal to this size.
         */
        return mMinNodeSize;
    }


    public void setMinNodeSize(float pMinNodeSize) {
        mMinNodeSize = pMinNodeSize;
    }


    private static boolean intersectsBox(Vector3f pBoxAOrigin, Vector3f pBoxAScale,
                                         Vector3f pBoxBOrigin, Vector3f pBoxBScale) {
        Vector3f t = mathematik.Util.sub(pBoxBOrigin, pBoxAOrigin);
        return Math.abs(t.x) <= (pBoxAScale.x + pBoxBScale.x)
                && Math.abs(t.y) <= (pBoxAScale.y + pBoxBScale.y)
                && Math.abs(t.z) <= (pBoxAScale.z + pBoxBScale.z);
    }


    private static boolean isBoxIntersectingSphere(Vector3f pBoxOrigin, Vector3f pBoxScale,
                                                   Vector3f pSphereCenter, float pSphereRadius) {
        Vector3f mMin = mathematik.Util.sub(pBoxOrigin, pBoxScale);
        Vector3f mMax = mathematik.Util.add(pBoxOrigin, pBoxScale);
        float s;
        float d = 0;

        if (pSphereCenter.x < mMin.x) {
            s = pSphereCenter.x - mMin.x;
            d = s * s;
        } else if (pSphereCenter.x > mMax.x) {
            s = pSphereCenter.x - mMax.x;
            d += s * s;
        }

        if (pSphereCenter.y < mMin.y) {
            s = pSphereCenter.y - mMin.y;
            d += s * s;
        } else if (pSphereCenter.y > mMax.y) {
            s = pSphereCenter.y - mMax.y;
            d += s * s;
        }

        if (pSphereCenter.z < mMin.z) {
            s = pSphereCenter.z - mMin.z;
            d += s * s;
        } else if (pSphereCenter.z > mMax.z) {
            s = pSphereCenter.z - mMax.z;
            d += s * s;
        }

        return d <= pSphereRadius * pSphereRadius;
    }


    private static boolean isPointInSphere(Vector3f pPoint, Vector3f pSphereOrigin, float pSphereRadius) {
        float d = pSphereOrigin.distanceSquared(pPoint);
        return (d <= pSphereRadius * pSphereRadius);
    }


    private static boolean isPointInBox(Vector3f pPoint, Vector3f pBoxOrigin, Vector3f pBoxScale) {
        float w = pBoxScale.x;
        if (pPoint.x < pBoxOrigin.x - w || pPoint.x > pBoxOrigin.x + w) {
            return false;
        }
        w = pBoxScale.y;
        if (pPoint.y < pBoxOrigin.y - w || pPoint.y > pBoxOrigin.y + w) {
            return false;
        }
        w = pBoxScale.z;
        if (pPoint.z < pBoxOrigin.z - w || pPoint.z > pBoxOrigin.z + w) {
            return false;
        }
        return true;
    }
}
