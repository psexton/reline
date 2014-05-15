/*
 * PatchFinder.java, part of the reline project
 * Created on May 15, 2014, 5:12:13 PM
 * Copyright (C) 2014 PSexton
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.psexton.reline;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * Searches for a solid colored patch in an image.
 * A patch is a solid-colored square. The size and color of the square can be
 * set.
 * @author PSexton
 */
public class PatchFinder {
    private static final Color DEFAULT_PATCH_COLOR = new Color(69, 173, 58);
    private static final int DEFAULT_PATCH_SIZE = 25;
    
    private Color patchColor = DEFAULT_PATCH_COLOR;
    private int patchSize = DEFAULT_PATCH_SIZE;

    /**
     * Get the value of patchColor
     *
     * @return the value of patchColor
     */
    public Color getPatchColor() {
        return patchColor;
    }

    /**
     * Set the value of patchColor
     *
     * @param patchColor new value of patchColor
     */
    public void setPatchColor(Color patchColor) {
        this.patchColor = patchColor;
    }
    
    /**
     * Get the value of patchSize
     *
     * @return the value of patchSize
     */
    public int getPatchSize() {
        return patchSize;
    }

    /**
     * Set the value of patchSize
     *
     * @param patchSize new value of patchSize
     */
    public void setPatchSize(int patchSize) {
        this.patchSize = patchSize;
    }

    /**
     * Searches for a patch in image.
     * @param image Image to search in
     * @return Point of top-left corner if found, or null if not found
     */
    public Point findInImage(BufferedImage image) {
        /*
        As a first attempt, we brute force it.
        Starting at (0,0), and working down each column, we search for a pixel
        that matches the patch color. When we find one, then we check if it's
        the top-left corner of a patch.
         */
        
        // Iterating over all possible start points for a patch
        int minStartX = image.getMinX();
        int minStartY = image.getMinY();
        int maxStartX = image.getWidth() - getPatchSize() - 1;
        int maxStartY = image.getHeight() - getPatchSize() - 1;
        for(int x = minStartX; x <= maxStartX; x++) {
            for(int y = minStartY; y <= maxStartY; y++) {
                // Check each one of them
                BufferedImage subImage = image.getSubimage(x, y, getPatchSize(), getPatchSize());
                if(isAPatch(subImage))
                    return new Point(x, y);
            }
        }
        
        // If we're still here, nothing matched
        return null;
    }
    
    private boolean isAPatch(BufferedImage image) {
        // Entire image should have the same RGB value as the patch
        int[] imageColors = image.getRGB(0, 0, getPatchSize(), getPatchSize(), null, 0, getPatchSize());
        for(int i = 0; i < imageColors.length; i++) {
            // Need to convert from ARGB color space to RGB color space
            Color pixelColor = new Color(imageColors[i], true);
            if(!pixelColor.equals(getPatchColor()))
                return false;
        }
        
        return true;
    }
    
}
