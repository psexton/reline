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
        PatchFinder pf = new PatchFinder();
        Point joinClickPoint = pf.findInImage(screenShot);
        if(joinClickPoint != null) {
            appendLine("\tFound join button");
            robot.mouseMove(joinClickPoint.x, joinClickPoint.y);
        }
        else {
            appendLine("\tDid not find join button");
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
    
}
