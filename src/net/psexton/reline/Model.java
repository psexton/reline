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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    DateFormat dateFormat;
    
    public Model() {
        console = null;
        isRunning = false;
        timer = null;
        dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
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
        timer.start();
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
        appendLine("Checking");
    }
    
    /**
     * Will be prefixed with date & time info, and suffixed with newline.
     * @param contents 
     */
    private void appendLine(String content) {
        String dateAndTimePrefix = "[" + dateFormat.format(Calendar.getInstance().getTime()) + "]";
        console.append(dateAndTimePrefix + " " + content + "\n");
    }
}
