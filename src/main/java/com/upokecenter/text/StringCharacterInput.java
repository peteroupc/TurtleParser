package com.upokecenter.text;
/*
Written by Peter O. in 2014.
Any copyright is dedicated to the Public Domain.
http://creativecommons.org/publicdomain/zero/1.0/
If you like this, you should donate to Peter O.
at: http://upokecenter.dreamhosters.com/articles/donate-now-2/
 */

import com.upokecenter.util.*;

  class StringCharacterInput implements ICharacterInput
  {
    private final String str;
    private int index;
    private final int endIndex;

    public StringCharacterInput(String str) {
      if (str == null) {
        throw new NullPointerException("str");
      }
      this.str = str;
      this.endIndex = str.length();
    }

    public StringCharacterInput(String str, int index, int length) {
      if (str == null) {
        throw new NullPointerException("str");
      }
      if (index < 0) {
      throw new IllegalArgumentException("index (" + index + ") is less than " +
          "0");
      }
      if (index > str.length()) {
        throw new IllegalArgumentException("index (" + index + ") is more than " +
          str.length());
      }
      if (length < 0) {
    throw new IllegalArgumentException("length (" + length + ") is less than " +
          "0");
      }
      if (length > str.length()) {
        throw new IllegalArgumentException("length (" + length + ") is more than " +
          str.length());
      }
      if (str.length() - index < length) {
        throw new IllegalArgumentException("str's length minus " + index + " (" +
          (str.length() - index) + ") is less than " + length);
      }
      this.str = str;
      this.index = index;
      this.endIndex = index + length;
    }

    public int ReadChar() {
      if (this.index >= this.endIndex) {
        return -1;
      }
      int c = this.str.charAt(this.index);
      if ((c & 0xfc00) == 0xd800 && this.index + 1 < this.str.length() &&
    this.str.charAt(this.index + 1) >= 0xdc00 && this.str.charAt(this.index + 1) <=
            0xdfff) {
        // Get the Unicode code point for the surrogate pair
      c = 0x10000 + ((c - 0xd800) << 10) + (this.str.charAt(this.index + 1) -
          0xdc00);
        ++this.index;
      } else if ((c & 0xf800) == 0xd800) {
        // unpaired surrogate
        c = 0xfffd;
      }
      ++this.index;
      return c;
    }

    public int Read(int[] chars, int index, int length) {
      if (chars == null) {
        throw new NullPointerException("chars");
      }
      if (index < 0) {
      throw new IllegalArgumentException("index (" + index + ") is less than " +
          "0");
      }
      if (index > chars.length) {
        throw new IllegalArgumentException("index (" + index + ") is more than " +
          chars.length);
      }
      if (length < 0) {
    throw new IllegalArgumentException("length (" + length + ") is less than " +
          "0");
      }
      if (length > chars.length) {
        throw new IllegalArgumentException("length (" + length + ") is more than " +
          chars.length);
      }
      if (chars.length - index < length) {
        throw new IllegalArgumentException("chars's length minus " + index + " (" +
          (chars.length - index) + ") is less than " + length);
      }
      if (this.endIndex == this.index) {
        return -1;
      }
      if (length == 0) {
        return 0;
      }
      for (int i = 0; i < length; ++i) {
        int c = this.ReadChar();
        if (c == -1) {
          return (i == 0) ? -1 : i;
        }
        chars[index + i] = c;
      }
      return length;
    }
  }
