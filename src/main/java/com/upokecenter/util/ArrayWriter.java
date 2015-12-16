package com.upokecenter.util;
/*
Written by Peter O. in 2014.
Any copyright is dedicated to the Public Domain.
http://creativecommons.org/publicdomain/zero/1.0/
If you like this, you should donate to Peter O.
at: http://upokecenter.dreamhosters.com/articles/donate-now-2/
 */

    /**
     * A growable array of bytes.
     */
  public final class ArrayWriter implements IWriter {
    private int retvalPos;
    private int retvalMax;
    private byte[] retval;

    /**
     * Offers a fast way to reset the length of the array writer's data to 0.
     */
    public void Clear() {
      this.retvalPos = 0;
      this.retvalMax = 0;
    }

    /**
     * Initializes a new instance of the ArrayWriter class.
     */
    public ArrayWriter() {
 this(16);
    }

    /**
     * Initializes a new instance of the ArrayWriter class.
     * @param initialSize A 32-bit signed integer.
     */
    public ArrayWriter(int initialSize) {
      this.retval = new byte[initialSize];
    }

    /**
     * Generates an array of all bytes written so far to it.
     * @return A byte array.
     */
    public byte[] ToArray() {
      byte[] ret = new byte[this.retvalMax];
      System.arraycopy(this.retval, 0, ret, 0, this.retvalMax);
      return ret;
    }

    /**
     * Writes an 8-bit byte to the array.
     * @param byteValue An integer containing the byte to write. Only the lower 8
     * bits of this value will be used.
     */
    public void write(int byteValue) {
      if (this.retval.length <= this.retvalPos) {
        // Array too small, make it grow
        int newLength = Math.max(
            this.retvalPos + 1000,
            this.retval.length * 2);
        byte[] newArray = new byte[newLength];
        System.arraycopy(this.retval, 0, newArray, 0, this.retvalPos);
        this.retval = newArray;
      }
      this.retval[this.retvalPos] = (byte)(byteValue & 0xff);
      this.retvalPos = (this.retvalPos + 1);
      this.retvalMax = Math.max(this.retvalMax, this.retvalPos);
    }

    /**
     * Writes a series of bytes to the array.
     * @param src Byte array containing the data to write.
     * @param offset A zero-based index showing where the desired portion of {@code
     * src} begins.
     * @param length The number of elements in the desired portion of {@code src}
     * (but not more than {@code src} 's length).
     * @throws NullPointerException The parameter {@code src} is null.
     * @throws IllegalArgumentException Either {@code offset} or {@code length} is less
     * than 0 or greater than {@code src} 's length, or {@code src} 's
     * length minus {@code offset} is less than {@code length}.
     */
    public void write(byte[] src, int offset, int length) {
      if (src == null) {
        throw new NullPointerException("src");
      }
      if (offset < 0) {
        throw new IllegalArgumentException("offset (" + offset + ") is less than " +
              "0");
      }
      if (offset > src.length) {
        throw new IllegalArgumentException("offset (" + offset + ") is more than " +
          src.length);
      }
      if (length < 0) {
        throw new IllegalArgumentException("length (" + length + ") is less than " +
              "0");
      }
      if (length > src.length) {
        throw new IllegalArgumentException("length (" + length + ") is more than " +
          src.length);
      }
      if (src.length - offset < length) {
        throw new IllegalArgumentException("src's length minus " + offset + " (" +
          (src.length - offset) + ") is less than " + length);
      }
      if (this.retval.length - this.retvalPos < length) {
        // Array too small, make it grow
        int newLength = Math.max(
this.retvalPos + length + 1000,
this.retval.length * 2);
        byte[] newArray = new byte[newLength];
        System.arraycopy(this.retval, 0, newArray, 0, this.retvalPos);
        this.retval = newArray;
      }
      System.arraycopy(src, offset, this.retval, this.retvalPos, length);
      this.retvalPos = (this.retvalPos + length);
      this.retvalMax = Math.max(this.retvalMax, this.retvalPos);
    }
  }
