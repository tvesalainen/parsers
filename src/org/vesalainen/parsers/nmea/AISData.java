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
package org.vesalainen.parsers.nmea;

/**
 *
 * @author Timo Vesalainen
 */
public interface AISData
{   
    /**
     * Number of Sentences (some messages need more then one)
     * @param numberOfSentences 
     */
    public void setNumberOfSentences(int numberOfSentences);
    /**
     * Sentence Number (1 unless it´s a multi-sentence message)
     * @param sentenceNumber 
     */
    public void setSentenceNumber(int sentenceNumber);

    public void setSequenceMessageId(int sequentialMessageId);
    /**
     * The AIS Channel (A or B)
     * @param channel 
     */
    public void setChannel(char channel);

    public void setMessageType(int messageType);

    public void setRepeatIndicator(int repeatIndicator);

    public void setMMSI(int mmsi);
    
}
