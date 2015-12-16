package com.upokecenter.text;

import java.io.*;
import com.upokecenter.util.*;

    /**
     * Defines a method that can be implemented by classes that convert Unicode
     * code points to bytes.
     */
  public interface ICharacterEncoder {
    /**
     * Converts a Unicode code point to bytes and writes the bytes to an output
     * stream. <p>If this method returns -2, indicating an error, the caller
     * of this method can take one of a variety of actions to handle the
     * error. For example, it can write an escape sequence using the code
     * point (such as the HTML error mode in the Encoding Standard), it can
     * replace the code point with a "best fit" to that code point (which
     * can consist of one or more bytes) and write the "best fit" instead,
     * it can replace the code point with an arbitrary byte or sequence of
     * bytes, or it can throw an exception. In some cases, where the error
     * won't cause data loss or a security problem, the caller can also
     * ignore the encoding error.</p>
     * @param c Either a Unicode code point (from 0-0xd7ff or from 0xe000 to
     * 0x10ffff), or the value -1 indicating the end of the stream.
     * @param output Output stream where the converted bytes will be written. The
     * decoder can maintain internal state, including data on bytes already
     * passed as input, so this parameter should not change when using the
     * same character encoder object.
     * @return The number of bytes written to the stream; -1 if no further code
     * points remain (for example, if _c_ is -1 indicating the end of the
     * stream), or -2 if an encoding error occurs. (Note that it's possible
     * for this method to return 0 if, for example, it can't generate new
     * bytes yet based on the current input.). If this method returns -2, it
     * should not write any bytes to the output stream.
     */
    int Encode(int c, IWriter output);
  }
