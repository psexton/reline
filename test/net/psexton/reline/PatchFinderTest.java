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
    private static File imageDir;
    
    @BeforeClass
    public static void setUpClass() {
        imageDir = new File(System.getProperty("user.dir"), "images");
    }

    /**
     * Test of findInImage method, of class PatchFinder.
     * On allBlack, should return null
     * @throws java.io.IOException If ImageIO.read throws it while reading in png
     */
    @Test
    public void greenPatchNotFoundInAllBlack() throws IOException {
        BufferedImage allBlack = ImageIO.read(new File(imageDir, "all_black.png"));
        PatchFinder instance = new PatchFinder();
        Point expResult = null;
        Point result = instance.findInImage(allBlack);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of findInImage method, of class PatchFinder.
     * On allWhite, should return null
     * @throws java.io.IOException If ImageIO.read throws it while reading in png
     */
    @Test
    public void greenPatchNotFoundInAllWhite() throws IOException {
        BufferedImage allWhite = ImageIO.read(new File(imageDir, "all_white.png"));
        PatchFinder instance = new PatchFinder();
        Point expResult = null;
        Point result = instance.findInImage(allWhite);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of findInImage method, of class PatchFinder.
     * On allGreen, should return (0,0), with left margin checking disabled
     * @throws java.io.IOException If ImageIO.read throws it while reading in png
     */
    @Test
    public void greenPatchWithoutMarginFoundInAllGreen() throws IOException {
        BufferedImage allGreen = ImageIO.read(new File(imageDir, "all_green.png"));
        PatchFinder instance = new PatchFinder();
        instance.setLeftMarginEnabled(false);
        Point expResult = new Point(0, 0);
        Point result = instance.findInImage(allGreen);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of findInImage method, of class PatchFinder.
     * On allGreen, should return null because there's no white margin at the left
     * @throws java.io.IOException If ImageIO.read throws it while reading in png
     */
    @Test
    public void greenPatchWithMarginNotFoundInAllGreen() throws IOException {
        BufferedImage allGreen = ImageIO.read(new File(imageDir, "all_green.png"));
        PatchFinder instance = new PatchFinder();
        instance.setLeftMarginEnabled(true);
        Point expResult = null;
        Point result = instance.findInImage(allGreen);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of findInImage method, of class PatchFinder.
     * On tinyGreenBox, should return null because patch is too small
     * @throws java.io.IOException If ImageIO.read throws it while reading in png
     */
    @Test
    public void greenPatchNotFoundInTinyGreenBox() throws IOException {
        BufferedImage tinyGreenBox = ImageIO.read(new File(imageDir, "tiny_green_box.png"));
        PatchFinder instance = new PatchFinder();
        Point expResult = null;
        Point result = instance.findInImage(tinyGreenBox);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of findInImage method, of class PatchFinder.
     * On boxes, should return (300, 100) for top right box
     * @throws java.io.IOException If ImageIO.read throws it while reading in png
     */
    @Test
    public void greenPatchFoundInBoxes() throws IOException {
        BufferedImage boxes = ImageIO.read(new File(imageDir, "boxes.png"));
        PatchFinder instance = new PatchFinder();
        Point expResult = new Point(300, 100);
        Point result = instance.findInImage(boxes);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of findInImage method, of class PatchFinder.
     * On screenshot of all_green.png, should return null when left margin 
     * checking is enabled
     * @throws java.io.IOException If ImageIO.read throws it while reading in png
     */
    @Test
    public void greenPatchWithMarginNotFoundInScreenshotOfAllGreen() throws IOException {
        BufferedImage screenshotAllGreen = ImageIO.read(new File(imageDir, "screenshot_wpv_all_green.png"));
        PatchFinder instance = new PatchFinder();
        instance.setLeftMarginEnabled(true);
        Point expResult = null;
        Point result = instance.findInImage(screenshotAllGreen);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of findInImage method, of class PatchFinder.
     * On screenshot of all_green.png, should return (104, 214) for box in window,
     * when left margin checking is disabled.
     * @throws java.io.IOException If ImageIO.read throws it while reading in png
     */
    @Test
    public void greenPatchWithoutMarginFoundInScreenshotOfAllGreen() throws IOException {
        BufferedImage screenshotAllGreen = ImageIO.read(new File(imageDir, "screenshot_wpv_all_green.png"));
        PatchFinder instance = new PatchFinder();
        instance.setLeftMarginEnabled(false);
        Point expResult = new Point(104, 214);
        Point result = instance.findInImage(screenshotAllGreen);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of findInImage method, of class PatchFinder.
     * On screenshot of boxes.png, should return (382, 203) for top right box in window
     * @throws java.io.IOException If ImageIO.read throws it while reading in png
     */
    @Test
    public void greenPatchFoundInScreenshotOfBoxes() throws IOException {
        BufferedImage screenshotBoxes = ImageIO.read(new File(imageDir, "screenshot_wpv_boxes.png"));
        PatchFinder instance = new PatchFinder();
        Point expResult = new Point(382, 203);
        Point result = instance.findInImage(screenshotBoxes);
        assertEquals(expResult, result);
    }

    /**
     * Test of findInImage method, of class PatchFinder.
     * On vline_join_cropped.png, should return (46, 32) for top left corner of button
     * Note that we change the patch size to 16x16
     * @throws java.io.IOException If ImageIO.read throws it while reading in png
     */
    @Test
    public void greenPatchFoundInCroppedJoin() throws IOException {
        BufferedImage boxes = ImageIO.read(new File(imageDir, "vline_join_cropped.png"));
        PatchFinder instance = new PatchFinder();
        instance.setPatchSize(16);
        Point expResult = new Point(46, 32);
        Point result = instance.findInImage(boxes);
        assertEquals(expResult, result);
    }
 
    /**
     * Test of findInImage method, of class PatchFinder.
     * On vline_restart_cropped.png, should return (71, 85) for top left corner of button
     * Note that we change the patch size to 32x32
     * @throws java.io.IOException If ImageIO.read throws it while reading in png
     */
    @Test
    public void greenPatchFoundInCroppedRestart() throws IOException {
        BufferedImage boxes = ImageIO.read(new File(imageDir, "vline_restart_cropped.png"));
        PatchFinder instance = new PatchFinder();
        instance.setPatchSize(32);
        Point expResult = new Point(71, 85);
        Point result = instance.findInImage(boxes);
        assertEquals(expResult, result);
    }
    

    
    /**
     * Test of findInImage method, of class PatchFinder.
     * On allGreenWithMargins, should return (1, 1)
     * @throws java.io.IOException If ImageIO.read throws it while reading in png
     */
    @Test
    public void greenPatchFoundInAllGreenWithMargins() throws IOException {
        BufferedImage boxes = ImageIO.read(new File(imageDir, "all_green_with_margins.png"));
        PatchFinder instance = new PatchFinder();
        Point expResult = new Point(1, 1);
        Point result = instance.findInImage(boxes);
        assertEquals(expResult, result);
    }
    
}
