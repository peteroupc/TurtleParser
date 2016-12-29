package com.upokecenter.util;
/*
Written in 2013 by Peter Occil.
Any copyright is dedicated to the Public Domain.
http://creativecommons.org/publicdomain/zero/1.0/

If you like this, you should donate to Peter O.
at: http://peteroupc.github.io/
*/

import com.upokecenter.text.*;

    /**
     * Not documented yet.
     */
public interface IMarkableCharacterInput extends ICharacterInput {
    /**
     * Gets the zero-based character position in the stream from the last-set mark.
     * @return The return value is not documented yet.
     */
   int getMarkPosition();

    /**
     * Moves the stream position back the given number of characters.
     * @param count The parameter {@code count} is not documented yet.
     */
   void moveBack(int count);

    /**
     * Sets a mark on the stream's current position.
     * @return The return value is not documented yet.
     */
   int setHardMark();

    /**
     * Sets the stream's position from the last set mark. <param
     * name='pos'>Zero-based character offset from the last set
     * mark.</param>
     * @param pos The parameter {@code pos} is not documented yet.
     * @param pos Zero-based character offset from the last set mark.
     */
   void setMarkPosition(int pos);

    /**
     * If no mark is set, sets a mark on the stream, and characters read before the
     * currently set mark are no longer available, while characters read
     * after will be available if moveBack is called. Otherwise, behaves
     * like getMarkPosition.
     * @return The return value is not documented yet.
     */
    int setSoftMark();
}
