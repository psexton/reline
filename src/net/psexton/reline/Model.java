/*
 * Model.java, part of the reline project
 * Created on May 14, 2014, 12:14:22 PM
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

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.util.Calendar;
import javax.swing.JTextArea;
import javax.swing.Timer;

/**
 *
 * @author PSexton
 */
public class Model {
    private static final int JOIN_REGION_WIDTH = 142;
    private static final int RESTART_REGION_WIDTH = 254;
    private static final int REGION_HEIGHT = 3;
    private static final int WHITE = new Color(255, 255, 255).getRGB();
    private static final int GREEN = new Color(69, 173, 58).getRGB();
    
    private JTextArea console;
    private boolean isRunning;
    private Timer timer;
    private final DateFormat dateFormat;
    private Robot robot;
    
    public Model() {
        console = null;
        isRunning = false;
        timer = null;
        dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        robot = null;
    }
    
    public void startMonitor(int intervalInSeconds, JTextArea console) {
        if(isRunning) {
            throw new IllegalStateException("Already monitoring");
        }
        isRunning = true;
        this.console = console;
        console.setText("");
        appendLine("Starting monitoring at " + intervalInSeconds + "s intervals");
        timer = new Timer(intervalInSeconds * 1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                monitor();
            }
        });
        try {
            robot = new Robot();
            timer.setInitialDelay(0); // trigger immediately upon starting
            //timer.setRepeats(false);
            timer.start();
        } 
        catch (AWTException ex) {
            console.setText(ex.toString());
        }
    }
    
    public void stopMonitor() {
        if(!isRunning) {
            throw new IllegalStateException("Not monitoring");
        }
        timer.stop();
        isRunning = false;
        appendLine("Stopping monitoring");
    }
    
    private void monitor() {
        appendLine("Checking...");
        BufferedImage screenShot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        appendLine("\tGot screenshot");
        Point joinClickPoint = findGreenSubregion(screenShot, JOIN_REGION_WIDTH, REGION_HEIGHT);
        if(joinClickPoint != null) {
            appendLine("\tFound join button");
            robot.mouseMove(joinClickPoint.x, joinClickPoint.y);
        }
        else {
            appendLine("\tDid not find join button");
            Point restartClickPoint = findGreenSubregion(screenShot, RESTART_REGION_WIDTH, REGION_HEIGHT);
            if(restartClickPoint != null) {
                appendLine("\tFound restart button");
                robot.mouseMove(restartClickPoint.x, restartClickPoint.y);
            }
            else {
                appendLine("\tDid not find restart button");
            }
        }
    }
    
    /**
     * Will be prefixed with date & time info, and suffixed with newline.
     * @param contents 
     */
    private void appendLine(String content) {
        appendLine(content, false);
    }
    private void appendLine(String content, boolean noPrefix) {
        String dateAndTimePrefix = noPrefix ? "" : "[" + dateFormat.format(Calendar.getInstance().getTime()) + "] ";
        console.append(dateAndTimePrefix + content + "\n");
    }
    
    /**
     * 
     * @param large The larger image to look in
     * @return Point with top left coordinates, or null
     */
    private Point findGreenSubregion(BufferedImage image, int regionWidth, int regionHeight) {
        /*
         * Rather than actually search for the images, we vastly simplify:
         * For each button we look for a Nx3 region.
         * The first row will be all white pixels.
         * The second and third rows will be white pixels at the ends, and green
         * pixels in all interior locations.
         * 
         * We will search until we find a matching subregion, or run out of screen.
         * If we do find a match, return the location in the original image of the
         * top left green pixel (second row, second column in the subregion).
         */
        
        // Iterating over all possible start points
        int minStartX = image.getMinX();
        int minStartY = image.getMinY();
        int maxStartX = image.getWidth() - regionWidth - 1;
        int maxStartY = image.getHeight() - regionHeight - 1;
        for(int y = minStartY; y <= maxStartY; y++) {
            for(int x = minStartX; x <= maxStartX; x++) {
                BufferedImage subImage = image.getSubimage(x, y, regionWidth, regionHeight);
                if(isMatchingSubRegion(subImage))
                    return new Point(x, y);
            }
        }
        
        return null;
    }
    
    private boolean isMatchingSubRegion(BufferedImage image) {
        // First row should be all white
        final int maxX = image.getWidth() - 1;
        for(int x = 0; x <= maxX; x++) {
            if(image.getRGB(x, 0) != WHITE)
                return false;
        }
        // Second and third rows should be white on edges, and green in interior
        for(int y = 1; y <= 2; y++) {
            if(image.getRGB(0, y) != WHITE || image.getRGB(maxX, y) != WHITE)
                return false;
            for(int x = 1; x < maxX; x++) {
                if(image.getRGB(x, y) != GREEN)
                    return false;
            }
        }
        
        return true;
    }
}
