/*
 * Params.java, part of the reline project
 * Created on May 29, 2014, 12:40:36 PM
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

/**
 *
 * @author PSexton
 */
public class Params {
    private final int intervalInSeconds;
    private final boolean writeImagesToDisk;
    
    public Params(int intervalInSeconds, boolean writeImagesToDisk) {
        this.intervalInSeconds = intervalInSeconds;
        this.writeImagesToDisk = writeImagesToDisk;
    }

    /**
     * Get the value of writeImagesToDisk
     *
     * @return the value of writeImagesToDisk
     */
    public boolean isWriteImagesToDisk() {
        return writeImagesToDisk;
    }

    /**
     * Get the value of intervalInSeconds
     *
     * @return the value of intervalInSeconds
     */
    public int getIntervalInSeconds() {
        return intervalInSeconds;
    }
    
    

}
