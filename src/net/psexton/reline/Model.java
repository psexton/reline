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
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.Timer;

/**
 *
 * @author PSexton
 */
public class Model {
    private static final int JOIN_PATCH_SIZE = 16;
    private static final int RESTART_PATCH_SIZE = 32;
    
    private Params params;
    private JTextArea console;
    private boolean isRunning;
    private Timer timer;
    private final DateFormat dateFormat;
    private Robot robot;
    private long counter;
    private long startTime;
    private SwingWorker currentWorker;
    
    /**
     *
     */
    public Model() {
        params = null;
        console = null;
        isRunning = false;
        timer = null;
        dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        robot = null;
        counter = 0;
        currentWorker = null;
    }
    
    /**
     *
     * @param params
     * @param console
     */
    public void startMonitor(Params params, JTextArea console) {
        if(isRunning) {
            throw new IllegalStateException("Already monitoring");
        }
        isRunning = true;
        this.params = params;
        this.console = console;
        console.setText("");
        appendLine("Starting monitoring at " + params.getIntervalInSeconds() + "s intervals");
        timer = new Timer(params.getIntervalInSeconds() * 1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                monitor();
            }
        });
        try {
            robot = new Robot();
            timer.start();
        } 
        catch (AWTException ex) {
            console.setText(ex.toString());
        }
    }
    
    /**
     *
     */
    public void stopMonitor() {
        if(!isRunning) {
            throw new IllegalStateException("Not monitoring");
        }
        if(currentWorker != null)
            currentWorker.cancel(true);
        timer.stop();
        isRunning = false;
        appendLine("Stopping monitoring");
    }
    
    private void monitor() {
        iterationStart();
        currentWorker = new RestartButtonWorker();
        currentWorker.execute(); // This will chain a call to JoinButtonWorker if needed
    }
    
    private void iterationStart() {
        appendLine("Checking... (#" + counter + ")");
        startTime = System.currentTimeMillis();
    }
    
    private void iterationEnd() {
        long stopTime = System.currentTimeMillis();
        double elapsedTime = ((double) stopTime-startTime) / 1000.0;
        appendLine("\tElapsed time: " + elapsedTime + "s");
        counter++;
    }
    
    private void moveAndClickMouse(Point point) {
        robot.mouseMove(point.x, point.y);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
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
     * For debugging, write each captured image out to a file
     * @param image
     * @param name 
     */
    private void writeImageToDisk(BufferedImage image, String name) {
        // Make sure subdir exists
        File imageDir = new File("reline_images");
        imageDir.mkdir();
        // Construct filename
        File imageFile = new File(imageDir, name + "-" + (counter % 10) + ".png");
        
        try {
            ImageIO.write(image, "png", imageFile);
        } catch (IOException ex) {
            appendLine("IOException thrown while trying to write " + imageFile.getAbsolutePath());
            appendLine(ex.toString());
        }
    }
    
    //
    // SwingWorker classes for calling PatchFinder class on a background thread
    //
    
    class RestartButtonWorker extends SwingWorker<Point, String> {
        private volatile Point clickPoint; // volatile to share between threads
        
        // Runs on caller thread (probably EDT)
        RestartButtonWorker() {
            clickPoint = null;
        }
        
        // Runs on worker thread
        @Override
        protected Point doInBackground() throws Exception {
            publish("\tLooking for restart button...");
            // To save time, divide the screen image into a 4x4 grid, with square
            // (0,0) in the top left, and square (3,0) in the top right.
            // Only look for restart button in squares (1,0) and (2,0).
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            final int gridSquareWidth = screenSize.width / 4;
            final int gridSquareHeight = screenSize.height / 4;
            final int croppedStartX = gridSquareWidth * 1;
            final int croppedStartY = gridSquareHeight * 0;
            final int croppedWidth = gridSquareWidth * 2;
            final int croppedHeight = gridSquareHeight * 1;
            BufferedImage croppedScreen = robot.createScreenCapture(new Rectangle(croppedStartX, croppedStartY, croppedWidth, croppedHeight));
            publish("\t\tGot cropped screenshot");
            if(params.isWriteImagesToDisk()) {
                writeImageToDisk(croppedScreen, "restart");
                publish("\t\tSaved image to disk");
            }
            publish("\t\tMatching...");
            PatchFinder pf = new PatchFinder();
            pf.setPatchSize(RESTART_PATCH_SIZE);
            clickPoint = pf.findInImage(croppedScreen);
            if(clickPoint != null) {
                // Need to convert croppedScreen's coords to screen's coords
                clickPoint.x = clickPoint.x + croppedStartX;
                clickPoint.y = clickPoint.y + croppedStartY;
            }
            return clickPoint;
        }
        
        // Runs on EDT
        @Override
        protected void process(List<String> chunks) {
            for(String chunk : chunks) {
                appendLine(chunk);
            }
        }
        
        // Runs on EDT
        @Override
        protected void done() {
            if(isCancelled()) {
                appendLine("\t\tTask cancelled");
                iterationEnd();
            }
            else {
                if(clickPoint != null) {
                    appendLine("\t\tFound restart button at (" + clickPoint.x + ", " + clickPoint.y + ")");
                    moveAndClickMouse(clickPoint);
                    appendLine("\t\tCursor moved and clicked");
                    iterationEnd();
                }
                else {
                    appendLine("\t\tDid not find restart button");
                    currentWorker = new JoinButtonWorker();
                    currentWorker.execute();
                }
            }
        }
    }
    
    class JoinButtonWorker extends SwingWorker<Point, String> {
        private volatile Point clickPoint; // volatile to share between threads
        
        // Runs on caller thread (probably EDT)
       JoinButtonWorker() {
            clickPoint = null;
        }
        
        // Runs on worker thread
        @Override
        protected Point doInBackground() throws Exception {
            publish("\tLooking for join button...");
            // To save time, divide the screen image into a 4x4 grid, with square
            // (0,0) in the top left, and square (3,0) in the top right.
            // Only look for join button in squares (2,1) and (2,2).
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            final int gridSquareWidth = screenSize.width / 4;
            final int gridSquareHeight = screenSize.height / 4;
            final int croppedStartX = gridSquareWidth * 2;
            final int croppedStartY = gridSquareHeight * 1;
            final int croppedWidth = gridSquareWidth * 1;
            final int croppedHeight = gridSquareHeight * 2;
            BufferedImage croppedScreen = robot.createScreenCapture(new Rectangle(croppedStartX, croppedStartY, croppedWidth, croppedHeight));
            publish("\t\tGot cropped screenshot");
            if(params.isWriteImagesToDisk()) {
                writeImageToDisk(croppedScreen, "join");
                publish("\t\tSaved image to disk");
            }
            publish("\t\tMatching...");
            PatchFinder pf = new PatchFinder();
            pf.setPatchSize(JOIN_PATCH_SIZE);
            clickPoint = pf.findInImage(croppedScreen);
            if(clickPoint != null) {
                // Need to convert croppedScreen's coords to screen's coords
                clickPoint.x = clickPoint.x + croppedStartX;
                clickPoint.y = clickPoint.y + croppedStartY;
            }
            return clickPoint;
        }
        
        // Runs on EDT
        @Override
        protected void process(List<String> chunks) {
            for(String chunk : chunks) {
                appendLine(chunk);
            }
        }
        
        // Runs on EDT
        @Override
        protected void done() {
            if(isCancelled()) {
                appendLine("\t\tTask cancelled");
            }
            else {
                if(clickPoint != null) {
                    appendLine("\t\tFound join button at (" + clickPoint.x + ", " + clickPoint.y + ")");
                    moveAndClickMouse(clickPoint);
                    appendLine("\t\tCursor moved and clicked");
                }
                else {
                    appendLine("\t\tDid not find join button");
                }
            }
            iterationEnd();
        }
    }
}
