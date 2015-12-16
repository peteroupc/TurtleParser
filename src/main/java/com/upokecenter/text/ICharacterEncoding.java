package com.upokecenter.text;

import java.io.*;
import com.upokecenter.util.*;

/// <summary>
/// Defines methods that can be implemented by classes
/// that convert to and from bytes and character code points.
/// </summary>
public interface ICharacterEncoding {
    /**
     * Creates an encoder for this character encoding with initial state. If the
     * encoder is stateless, multiple calls of this method can return the
     * same encoder.
     * @return A character encoder object.
     */

    ICharacterEncoder GetEncoder();

    /**
     * Creates a decoder for this character encoding with initial state. If the
     * decoder is stateless, multiple calls of this method can return the
     * same decoder.
     * @return A character decoder object.
     */

    ICharacterDecoder GetDecoder();
}
