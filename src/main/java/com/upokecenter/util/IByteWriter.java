package com.upokecenter.util;
/*
Written by Peter O. in 2014.
Any copyright is dedicated to the Public Domain.
http://creativecommons.org/publicdomain/zero/1.0/
If you like this, you should donate to Peter O.
at: http://peteroupc.github.io/
 */

    /**
     * A generic interface for writing bytes of data.
     */
  public interface IByteWriter {
    /**
     * Writes an 8-bit byte to a data source.
     * @param b Byte to write to the data source. Only the lower 8 bits of this
     * value are used.
     */
    void write(int b);
  }
