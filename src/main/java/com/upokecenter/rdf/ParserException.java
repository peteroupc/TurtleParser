package com.upokecenter.rdf;
import java.io.*;

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
  public class ParserException extends IOException {
private static final long serialVersionUID = 1L;
    /**
     * Initializes a new instance of the {@link
     * com.upokecenter.rdf.ParserException} class.
     */
    public ParserException() {
 super();
    }

    /**
     * Initializes a new instance of the {@link
     * com.upokecenter.rdf.ParserException} class.
     * @param str The parameter {@code str} is a text string.
     */
    public ParserException(String str) {
 super(str);
    }
  }
