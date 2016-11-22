package com.orange.documentare.core.comp.bwt;
/*
 * Copyright (c) 2016 Orange
 *
 * Authors: Christophe Maldivi & Joel Gardes
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 */

/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements. See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

/**
 * This class implements the Dual-Pivot Quicksort algorithm by
 * Vladimir Yaroslavskiy, Jon Bentley, and Joshua Bloch. The algorithm
 * offers O(n log(n)) performance on many data sets that cause other
 * quicksorts to degrade to quadratic performance, and is typically
 * faster than traditional (one-pivot) Quicksort implementations.
 *
 * @author Vladimir Yaroslavskiy
 * @author Jon Bentley
 * @author Josh Bloch
 *
 * @version 2009.11.29 m765.827.12i
 */

/** Specialized for BWT, only work on array indices */
final class BwtDualPivotQuicksort {

  /**
   * If the length of an array to be sorted is less than this
   * constant, insertion sort is used in preference to Quicksort.
   */
  private static final int INSERTION_SORT_THRESHOLD = 24;

  /** Helper class for byte arrays comparison */
  //private final ByteArrayCompare cmp = new ByteArrayCompare();

  /** Helper class to compute change Indices as a pre step */
  private final ChangeIndices changeIndices = new ChangeIndices();

  /**
   * Sorts the array into ascending numerical order. The array is left untouched.
   * @param readOnlyArray array to sort
   * @return sorted array indices
   */
  public int[] sort(byte[] readOnlyArray) {
    ByteArrayCompare cmp = new ByteArrayCompare(changeIndices.getChangeIndices(readOnlyArray));
    int[] sortedIndices = getIndicesArray(readOnlyArray.length);
    doSort(0, readOnlyArray.length - 1, sortedIndices, readOnlyArray, cmp);
    return sortedIndices;
  }

  private void doSort(int left, int right, int[] indices, byte[] readOnlyArray, ByteArrayCompare cmp) {
    // Use insertion sort on tiny arrays
    if (right - left + 1 < INSERTION_SORT_THRESHOLD) {
      int ii;
      int j;
      for (int i = left + 1; i <= right; i++) {
        ii = indices[i];
        for (j = i - 1; j >= left && cmp.isGreaterThanInitialIndices(indices[j], ii, readOnlyArray); j--) {
          indices[j + 1] = indices[j];
        }
        indices[j + 1] = ii;
      }
    } else { // Use Dual-Pivot Quicksort on large arrays
      dualPivotQuicksort(left, right, indices, readOnlyArray, cmp);
    }
  }

  private void dualPivotQuicksort(int left, int right, int[] indices, byte[] readOnlyArray, ByteArrayCompare cmp) {
    // Compute indices of five evenly spaced elements
    final int sixth = (right - left + 1) / 6;
    final int e1 = left  + sixth;
    final int e5 = right - sixth;
    final int e3 = (left + right) >>> 1; // The midpoint
    final int e4 = e3 + sixth;
    final int e2 = e3 - sixth;

    // Sort these elements using a 5-element sorting network
    int ie1 = indices[e1], ie2 = indices[e2], ie3 = indices[e3], ie4 = indices[e4], ie5 = indices[e5];

    if (cmp.isGreaterThanInitialIndices(ie1, ie2, readOnlyArray)) {
      int it = ie1; ie1 = ie2; ie2 = it;
    }
    if (cmp.isGreaterThanInitialIndices(ie4, ie5, readOnlyArray)) {
      int it = ie4; ie4 = ie5; ie5 = it;
    }
    if (cmp.isGreaterThanInitialIndices(ie1, ie3, readOnlyArray)) {
      int it = ie1; ie1 = ie3; ie3 = it;
    }
    if (cmp.isGreaterThanInitialIndices(ie2, ie3, readOnlyArray)) {
      int it = ie2; ie2 = ie3; ie3 = it;
    }
    if (cmp.isGreaterThanInitialIndices(ie1, ie4, readOnlyArray)) {
      int it = ie1; ie1 = ie4; ie4 = it;
    }
    if (cmp.isGreaterThanInitialIndices(ie3, ie4, readOnlyArray)) {
      int it = ie3; ie3 = ie4; ie4 = it;
    }
    if (cmp.isGreaterThanInitialIndices(ie2, ie5, readOnlyArray)) {
      int it = ie2; ie2 = ie5; ie5 = it;
    }
    if (cmp.isGreaterThanInitialIndices(ie2, ie3, readOnlyArray)) {
      int it = ie2; ie2 = ie3; ie3 = it;
    }
    if (cmp.isGreaterThanInitialIndices(ie4, ie5, readOnlyArray)) {
      int it = ie4; ie4 = ie5; ie5 = it;
    }

    indices[e1] = ie1; indices[e3] = ie3; indices[e5] = ie5;

        /*
         * Use the second and fourth of the five sorted elements as pivots.
         * These values are inexpensive approximations of the first and
         * second terciles of the array. Note that pivot1 <= pivot2.
         *
         * The pivots are stored in local variables, and the first and
         * the last of the elements to be sorted are moved to the locations
         * formerly occupied by the pivots. When partitioning is complete,
         * the pivots are swapped back into their final positions, and
         * excluded from subsequent sorting.
         */
    indices[e2] = indices[left];
    indices[e4] = indices[right];
    final int iPivot1 = ie2;
    final int iPivot2 = ie4;

    // Pointers
    int less  = left  + 1; // The index of first element of center part
    int great = right - 1; // The index before first element of right part

    final boolean pivotsDiffer = !cmp.equals(iPivot1, iPivot2, readOnlyArray);

    if (pivotsDiffer) {
            /*
             * Partitioning:
             *
             *   left part         center part                    right part
             * +------------------------------------------------------------+
             * | < pivot1  |  pivot1 <= && <= pivot2  |    ?    |  > pivot2 |
             * +------------------------------------------------------------+
             *              ^                          ^       ^
             *              |                          |       |
             *             less                        k     great
             *
             * Invariants:
             *
             *              all in (left, less)   < pivot1
             *    pivot1 <= all in [less, k)     <= pivot2
             *              all in (great, right) > pivot2
             *
             * Pointer k is the first index of ?-part
             */
      outer:
      for (int k = less; k <= great; k++) {
        final int ik = indices[k];
        if (cmp.isGreaterThanInitialIndices(iPivot1, ik, readOnlyArray)) { // Move a[k] to left part
          if (k != less) {
            indices[k] = indices[less];
            indices[less] = ik;
          }
          less++;
        } else if (cmp.isGreaterThanInitialIndices(ik, iPivot2, readOnlyArray)) { // Move a[k] to right part
          while (cmp.isGreaterThanInitialIndices(indices[great], iPivot2, readOnlyArray)) {
            if (great-- == k) {
              break outer;
            }
          }
          if (cmp.isGreaterThanInitialIndices(iPivot1, indices[great], readOnlyArray)) {
            indices[k] = indices[less];
            indices[less] = indices[great];
            less++;
            indices[great] = ik;
            great--;
          } else { // pivot1 <= a[great] <= pivot2
            indices[k] = indices[great];
            indices[great] = ik;
            great--;
          }
        }
      }
    } else { // Pivots are equal
            /*
             * Partition degenerates to the traditional 3-way,
             * or "Dutch National Flag", partition:
             *
             *   left part   center part            right part
             * +----------------------------------------------+
             * |  < pivot  |  == pivot  |    ?    |  > pivot  |
             * +----------------------------------------------+
             *              ^            ^       ^
             *              |            |       |
             *             less          k     great
             *
             * Invariants:
             *
             *   all in (left, less)   < pivot
             *   all in [less, k)     == pivot
             *   all in (great, right) > pivot
             *
             * Pointer k is the first index of ?-part
             */
      for (int k = less; k <= great; k++) {
        final int ik = indices[k];
        if (cmp.equals(ik, iPivot1, readOnlyArray)) {
          continue;
        }
        if (cmp.isGreaterThanInitialIndices(iPivot1, ik, readOnlyArray)) { // Move a[k] to left part
          if (k != less) {
            indices[k] = indices[less];
            indices[less] = ik;
          }
          less++;
        } else { // (a[k] > pivot1) -  Move a[k] to right part
                    /*
                     * We know that pivot1 == a[e3] == pivot2. Thus, we know
                     * that great will still be >= k when the following loop
                     * terminates, even though we don't test for it explicitly.
                     * In other words, a[e3] acts as a sentinel for great.
                     */
          while (cmp.isGreaterThanInitialIndices(indices[great], iPivot1, readOnlyArray)) {
            great--;
          }
          if (cmp.isGreaterThanInitialIndices(iPivot1, indices[great], readOnlyArray)) {
            indices[k] = indices[less];
            indices[less] = indices[great];
            less++;
            indices[great] = ik;
            great--;
          } else { // a[great] == pivot1
            indices[k] = iPivot1;
            indices[great] = ik;
            great--;
          }
        }
      }
    }

    // Swap pivots into their final positions
    indices[left] = indices[less - 1]; indices[less  - 1] = iPivot1;
    indices[right] = indices[great + 1]; indices[great + 1] = iPivot2;

    // Sort left and right parts recursively, excluding known pivot values
    doSort(left,   less - 2, indices, readOnlyArray, cmp);
    doSort(great + 2, right, indices, readOnlyArray, cmp);

        /*
         * If pivot1 == pivot2, all elements from center
         * part are equal and, therefore, already sorted
         */
    if (!pivotsDiffer) {
      return;
    }

        /*
         * If center part is too large (comprises > 2/3 of the array),
         * swap internal pivot values to ends
         */
    if (less < e1 && great > e5) {
      while (cmp.equals(indices[less], iPivot1, readOnlyArray)) {
        less++;
      }
      while (cmp.equals(indices[great], iPivot2, readOnlyArray)) {
        great--;
      }

            /*
             * Partitioning:
             *
             *   left part       center part                   right part
             * +----------------------------------------------------------+
             * | == pivot1 |  pivot1 < && < pivot2  |    ?    | == pivot2 |
             * +----------------------------------------------------------+
             *              ^                        ^       ^
             *              |                        |       |
             *             less                      k     great
             *
             * Invariants:
             *
             *              all in (*, less)  == pivot1
             *     pivot1 < all in [less, k)   < pivot2
             *              all in (great, *) == pivot2
             *
             * Pointer k is the first index of ?-part
             */
      outer:
      for (int k = less; k <= great; k++) {
        final int ik = indices[k];
        if (cmp.equals(ik, iPivot2, readOnlyArray)) { // Move a[k] to right part
          while (cmp.equals(indices[great], iPivot2, readOnlyArray)) {
            if (great-- == k) {
              break outer;
            }
          }
          if (cmp.equals(indices[great], iPivot1, readOnlyArray)) {
            indices[k] = indices[less];
            indices[less] = iPivot1;
            less++;
          } else { // pivot1 < a[great] < pivot2
            indices[k] = indices[great];
          }
          indices[great] = iPivot2;
          great--;
        } else if (cmp.equals(ik, iPivot1, readOnlyArray)) { // Move a[k] to left part
          indices[k] = indices[less];
          indices[less] = iPivot1;
          less++;
        }
      }
    }

    // Sort center part recursively, excluding known pivot values
    doSort(less, great, indices, readOnlyArray, cmp);
  }

  private int[] getIndicesArray(int length) {
    final int[] indices = new int[length];
    for(int i = 0; i < length; i++) {
      indices[i] = i;
    }
    return indices;
  }
}
