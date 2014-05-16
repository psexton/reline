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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JTextArea;
import javax.swing.Timer;

/**
 *
 * @author PSexton
 */
public class Model {
    private static final int JOIN_PATCH_SIZE = 16;
    private static final int RESTART_PATCH_SIZE = 32;
    
    private JTextArea console;
    private boolean isRunning;
    private Timer timer;
    private final DateFormat dateFormat;
    private Robot robot;
    private long counter;
    
    public Model() {
        console = null;
        isRunning = false;
        timer = null;
        dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        robot = null;
        counter = 0;
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
        appendLine("Checking... (#" + counter + ")");
        long startTime = System.currentTimeMillis();
        BufferedImage screenshot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        // For debugging, write each captured image out to a file:
//        try {
//            ImageIO.write(screenshot, "png", new File("screenshot-" + counter + ".png"));
//        } catch (IOException ex) {
//            appendLine("IOException thrown while trying to write screenshot-" + counter + ".png:");
//            appendLine(ex.toString());
//        }
        appendLine("\tGot screenshot");
        
        appendLine("\tLooking for restart button...");
        Point restartClickPoint = lookForRestartButton(screenshot);
        if(restartClickPoint != null) {
            appendLine("\t\tFound restart button at (" + restartClickPoint.x + ", " + restartClickPoint.y + ")");
            moveAndClickMouse(restartClickPoint);
            appendLine("\t\tCursor moved and clicked");
        }
        else {
            appendLine("\t\tDid not find restart button");
            appendLine("\tLooking for join button...");
            Point joinClickPoint = lookForJoinButton(screenshot);
            if(joinClickPoint != null) {
                appendLine("\t\tFound join button at (" + joinClickPoint.x + ", " + joinClickPoint.y + ")");
                moveAndClickMouse(joinClickPoint);
                appendLine("\t\tCursor moved and clicked");
            }
            else {
                appendLine("\t\tDid not find join button");
            }
        }
        
        long stopTime = System.currentTimeMillis();
        double elapsedTime = ((double) stopTime-startTime) / 1000.0;
        appendLine("\tElapsed time: " + elapsedTime + "s");
        counter++;
    }
    
    private Point lookForJoinButton(BufferedImage screenshot) {
        // To save time, divide the screen image into a 4x4 grid, with square
        // (0,0) in the top left, and square (3,0) in the top right.
        // Only look for join button in squares (2,1) and (2,2).
        final int gridSquareWidth = screenshot.getWidth() / 4;
        final int gridSquareHeight = screenshot.getHeight() / 4;
        final int croppedStartX = gridSquareWidth * 2;
        final int croppedStartY = gridSquareHeight * 1;
        final int croppedWidth = gridSquareWidth * 1;
        final int croppedHeight = gridSquareHeight * 2;
        BufferedImage croppedScreen = screenshot.getSubimage(croppedStartX, croppedStartY, croppedWidth, croppedHeight);
        PatchFinder pf = new PatchFinder();
        pf.setPatchSize(JOIN_PATCH_SIZE);
        Point clickPoint = pf.findInImage(croppedScreen);
        if(clickPoint != null) {
            // Need to convert croppedScreen's coords to screen's coords
            clickPoint.x = clickPoint.x + croppedStartX;
            clickPoint.y = clickPoint.y + croppedStartY;
        }
        return clickPoint;
    }
    
    private Point lookForRestartButton(BufferedImage screenshot) {
        // To save time, divide the screen image into a 4x4 grid, with square
        // (0,0) in the top left, and square (3,0) in the top right.
        // Only look for restart button in squares (1,0) and (2,0).
        final int gridSquareWidth = screenshot.getWidth() / 4;
        final int gridSquareHeight = screenshot.getHeight() / 4;
        final int croppedStartX = gridSquareWidth * 1;
        final int croppedStartY = gridSquareHeight * 0;
        final int croppedWidth = gridSquareWidth * 2;
        final int croppedHeight = gridSquareHeight * 1;
        BufferedImage croppedScreen = screenshot.getSubimage(croppedStartX, croppedStartY, croppedWidth, croppedHeight);
        PatchFinder pf = new PatchFinder();
        pf.setPatchSize(RESTART_PATCH_SIZE);
        Point clickPoint = pf.findInImage(croppedScreen);
        if(clickPoint != null) {
            // Need to convert croppedScreen's coords to screen's coords
            clickPoint.x = clickPoint.x + croppedStartX;
            clickPoint.y = clickPoint.y + croppedStartY;
        }
        return clickPoint;
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
    
}
