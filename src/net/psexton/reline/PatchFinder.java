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
    private static final Color DEFAULT_MARGIN_COLOR = new Color(255, 255, 255);
    private static final int DEFAULT_PATCH_SIZE = 25;
    private static final boolean DEFAULT_USE_LEFT_MARGIN = true;
    
    private Color patchColor = DEFAULT_PATCH_COLOR;
    private Color marginColor = DEFAULT_MARGIN_COLOR;
    private int patchSize = DEFAULT_PATCH_SIZE;
    private boolean useLeftMargin = DEFAULT_USE_LEFT_MARGIN;

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
     * Get the value of marginColor
     *
     * @return the value of marginColor
     */
    public Color getMarginColor() {
        return marginColor;
    }

    /**
     * Set the value of marginColor
     *
     * @param marginColor new value of marginColor
     */
    public void setMarginColor(Color marginColor) {
        this.marginColor = marginColor;
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
    
    public boolean isLeftMarginEnabled() {
        return useLeftMargin;
    }
    
    public void setLeftMarginEnabled(boolean useLeftMargin) {
        this.useLeftMargin = useLeftMargin;
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
                if(isAPatch(subImage)) {
                    // If left margin is enabled, we need to check that and return null
                    // if we found a patch, but it doesn't have the margin.
                    if(isLeftMarginEnabled()) {
                        // special case of no left margin because we're at the left edge
                        if(x == 0)
                            return null;
                        BufferedImage marginImage = image.getSubimage(x-1, y, 1, getPatchSize());
                        if(!isAMargin(marginImage))
                            return null;
                    }
                    return new Point(x, y);
                }
            }
        }
        
        // If we're still here, nothing matched
        return null;
    }
    
    private boolean isAPatch(BufferedImage image) {
        int DEPTH_REDUCTION = 7;
        // Break out the red, green, and blue values for patch
        int patchRed = getPatchColor().getRed() >> DEPTH_REDUCTION;
        int patchGreen = getPatchColor().getGreen() >> DEPTH_REDUCTION;
        int patchBlue = getPatchColor().getBlue() >> DEPTH_REDUCTION;
        
        // Entire image should have the same RGB value as the patch
        int[] imageColors = image.getRGB(0, 0, getPatchSize(), getPatchSize(), null, 0, getPatchSize());
        for(int i = 0; i < imageColors.length; i++) {
            // Need to convert from ARGB color space to RGB color space
            Color pixelColor = new Color(imageColors[i], true);
            
            // Break out the red, green, and blue values from pixelColor
            int pixelRed = pixelColor.getRed() >> DEPTH_REDUCTION;
            int pixelGreen = pixelColor.getGreen() >> DEPTH_REDUCTION;
            int pixelBlue = pixelColor.getBlue() >> DEPTH_REDUCTION;
            
            if(pixelRed != patchRed || pixelGreen != patchGreen || pixelBlue != patchBlue)
                return false;
        }
        
        // If left margin is being used, check that before returning true
        if(isLeftMarginEnabled()) {
            
        }
        
        return true;
    }
    
    private boolean isAMargin(BufferedImage image) {
        // Entire image should have the same RGB value as the margin
        // Don't do any color depth reduction
        int[] imageColors = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        for(int i = 0; i < imageColors.length; i++) {
            if(imageColors[i] != getMarginColor().getRGB())
                return false;
        }
        return true;
    }
}
