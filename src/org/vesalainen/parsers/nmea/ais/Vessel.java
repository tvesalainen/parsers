/*
 * Copyright (C) 2013 Timo Vesalainen
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

package org.vesalainen.parsers.nmea.ais;

/**
 * @author Timo Vesalainen
 */
public class Vessel extends AbstractAISObserver
{
    protected int mmsi;
    private NavigationStatus navigationStatus;
    private float degreesPerMinute;
    private float speed;
    private double longitude;
    private float latitude;
    private float courseOverGround;
    private int second;

    public Vessel(int mmsi)
    {
        this.mmsi = mmsi;
    }

    @Override
    public void setStatus(NavigationStatus navigationStatus)
    {
        this.navigationStatus = navigationStatus;
    }

    @Override
    public void setTurn(float degreesPerMinute)
    {
        this.degreesPerMinute = degreesPerMinute;
    }

    @Override
    public void setSpeed(float speed)
    {
        this.speed = speed;
    }

    @Override
    public void setLongitude(double longitude)
    {
        if (this.longitude != 0)
        {
            System.err.println();
        }
        this.longitude = longitude;
    }

    @Override
    public void setLatitude(float latitude)
    {
        this.latitude = latitude;
    }

    @Override
    public void setCourse(float cog)
    {
        this.courseOverGround = cog;
    }

    @Override
    public void setSecond(int second)
    {
        this.second = second;
    }
    
}
