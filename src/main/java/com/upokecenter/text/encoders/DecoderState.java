package com.upokecenter.text.encoders;

import com.upokecenter.util.*;

  public class DecoderState {
    // NOTE: bytes is an int array because some
    // decoders can prepend end-of-stream (-1).
    private int[] bytes;
    private int charCount;
    private int charOffset;
    private int[] chars;
    private int prependedBytes;

    public DecoderState(int initialSize) {
      this.bytes = new int[initialSize];
    }

    public void AppendChar(int ch) {
      this.chars = (this.chars == null) ? ((new int[4])) : this.chars;
      if (this.charCount >= this.chars.length) {
        int[] newchars = new int[this.chars.length + 8];
        System.arraycopy(this.chars, 0, newchars, 0, this.chars.length);
        this.chars = newchars;
      }
      this.chars[this.charCount] = ch;
      ++this.charCount;
    }

    public int GetChar() {
      if (this.charCount == 0) {
        return -1;
      }
      int c = this.chars[this.charOffset++];
      if (this.charOffset >= this.charCount) {
        this.charCount = 0;
        this.charOffset = 0;
      }
      return c;
    }

    public void PrependOne(int b1) {
      if (this.prependedBytes + 1 > this.bytes.length) {
        int[] newbytes = new int[this.prependedBytes + 8];
        System.arraycopy(this.bytes, 0, newbytes, 0, this.bytes.length);
        this.bytes = newbytes;
      }
      this.bytes[this.prependedBytes++] = b1;
    }

    public void PrependThree(int b1, int b2, int b3) {
      if (this.prependedBytes + 3 > this.bytes.length) {
        int[] newbytes = new int[this.prependedBytes + 8];
        System.arraycopy(this.bytes, 0, newbytes, 0, this.bytes.length);
        this.bytes = newbytes;
      }
      this.bytes[this.prependedBytes++] = b3;
      this.bytes[this.prependedBytes++] = b2;
      this.bytes[this.prependedBytes++] = b1;
    }

    public void PrependTwo(int b1, int b2) {
      if (this.prependedBytes + 2 > this.bytes.length) {
        int[] newbytes = new int[this.prependedBytes + 8];
        System.arraycopy(this.bytes, 0, newbytes, 0, this.bytes.length);
        this.bytes = newbytes;
      }
      this.bytes[this.prependedBytes++] = b2;
      this.bytes[this.prependedBytes++] = b1;
    }

    public int ReadInputByte(IByteReader stream) {
      if (this.prependedBytes > 0) {
        --this.prependedBytes;
        int b = this.bytes[this.prependedBytes];
        return b;
      } else {
        return stream.read();
      }
    }

    public IByteReader ToTransform(IByteReader stream) {
      return new StateToTransform(this, stream);
    }

    public IByteReader ToTransformIfBuffered(IByteReader stream) {
      return (
  this.prependedBytes == 0) ? stream : (
  new StateToTransform(
  this,
  stream));
    }

    private static class StateToTransform implements IByteReader {
      private final DecoderState s;
      private final IByteReader t;

      public StateToTransform(DecoderState s, IByteReader t) {
        this.t = t;
        this.s = s;
      }

      public int read() {
        return this.s.ReadInputByte(this.t);
      }
    }
  }
