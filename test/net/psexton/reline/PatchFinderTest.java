/*
 * PatchFinderTest.java, part of the reline project
 * Created on May 15, 2014, 5:23:56 PM
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

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author PSexton
 */
public class PatchFinderTest {
    private static BufferedImage allBlack;
    private static BufferedImage allWhite;
    private static BufferedImage allGreen;
    private static BufferedImage boxes;
    private static BufferedImage tinyGreenBox;
    private static BufferedImage screenshotAllGreen;
    private static BufferedImage screenshotBoxes;
    
    @BeforeClass
    public static void setUpClass() throws IOException {
        File imageDir = new File(System.getProperty("user.dir"), "images");
        allBlack = ImageIO.read(new File(imageDir, "all_black.png"));
        allWhite = ImageIO.read(new File(imageDir, "all_white.png"));
        allGreen = ImageIO.read(new File(imageDir, "all_green.png"));
        boxes = ImageIO.read(new File(imageDir, "boxes.png"));
        tinyGreenBox = ImageIO.read(new File(imageDir, "tiny_green_box.png"));
        screenshotAllGreen = ImageIO.read(new File(imageDir, "screenshot_wpv_all_green.png"));
        screenshotBoxes = ImageIO.read(new File(imageDir, "screenshot_wpv_boxes.png"));
    }

    /**
     * Test of findInImage method, of class PatchFinder.
     * On allBlack, should return null
     */
    @Test
    public void greenPatchNotFoundInAllBlack() {
        PatchFinder instance = new PatchFinder();
        Point expResult = null;
        Point result = instance.findInImage(allBlack);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of findInImage method, of class PatchFinder.
     * On allWhite, should return null
     */
    @Test
    public void greenPatchNotFoundInAllWhite() {
        PatchFinder instance = new PatchFinder();
        Point expResult = null;
        Point result = instance.findInImage(allWhite);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of findInImage method, of class PatchFinder.
     * On allGreen, should return (0,0)
     */
    @Test
    public void greenPatchFoundInAllGreen() {
        PatchFinder instance = new PatchFinder();
        Point expResult = new Point(0, 0);
        Point result = instance.findInImage(allGreen);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of findInImage method, of class PatchFinder.
     * On tinyGreenBox, should return null because patch is too small
     */
    @Test
    public void greenPatchNotFoundInTinyGreenBox() {
        PatchFinder instance = new PatchFinder();
        Point expResult = null;
        Point result = instance.findInImage(tinyGreenBox);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of findInImage method, of class PatchFinder.
     * On boxes, should return (300, 100) for top right box
     */
    @Test
    public void greenPatchFoundInBoxes() {
        PatchFinder instance = new PatchFinder();
        Point expResult = new Point(300, 100);
        Point result = instance.findInImage(boxes);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of findInImage method, of class PatchFinder.
     * On screenshot of all_green.png, should return (104, 214) for box in window
     */
    @Test
    public void greenPatchFoundInScreenshotOfAllGreen() {
        PatchFinder instance = new PatchFinder();
        Point expResult = new Point(104, 214);
        Point result = instance.findInImage(screenshotAllGreen);
        assertNotNull(result);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of findInImage method, of class PatchFinder.
     * On screenshot of boxes.png, should return (382, 203) for top right box in window
     */
    @Test
    public void greenPatchFoundInScreenshotOfBoxes() {
        PatchFinder instance = new PatchFinder();
        Point expResult = new Point(382, 203);
        Point result = instance.findInImage(screenshotBoxes);
        assertNotNull(result);
        assertEquals(expResult, result);
    }
}
