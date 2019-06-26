package com.upokecenter.util;
import com.upokecenter.text.*;

/*
Written in 2013 by Peter Occil.
Any copyright is dedicated to the Public Domain.
http://creativecommons.org/publicdomain/zero/1.0/

If you like this, you should donate to Peter O.
at: http://peteroupc.github.io/
*/

  /**
   * Not documented yet.
   */
  public interface IMarkableCharacterInput extends ICharacterInput {
    /**
     * Gets the zero-based character position in the stream from the last-set mark.
     * @return The return value is not documented yet.
     */
    int GetMarkPosition();

    /**
     * Moves the stream position back the given number of characters.
     * @param count The parameter {@code count} is not documented yet.
     */
    void MoveBack(int count);

    /**
     * Sets a mark on the stream's current position.
     * @return The return value is not documented yet.
     */
    int SetHardMark();

    /**
     * Sets the stream's position from the last set mark. <param
     * name='pos'>Zero-based character offset from the last set mark.
     * </param>
     * @param pos Zero-based character offset from the last set mark.
     */
    void SetMarkPosition(int pos);

    /**
     * If no mark is set, sets a mark on the stream, and characters read before the
     * currently set mark are no longer available, while characters read
     * after will be available if MoveBack is called. Otherwise, behaves
     * like GetMarkPosition.
     * @return The return value is not documented yet.
     */
    int SetSoftMark();
  }
