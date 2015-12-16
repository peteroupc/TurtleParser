package com.upokecenter.text.encoders;

import java.io.*;
import com.upokecenter.util.*;

import com.upokecenter.text.*;

  public class EncodingXUserDefined implements ICharacterEncoding {
    private static class Decoder implements ICharacterDecoder {
      public int ReadChar(IByteReader transform) {
        int b = transform.read();
        return (b < 0) ? (-1) : ((b < 0x80) ? b : (0xf700 + b));
      }
    }

    private static class Encoder implements ICharacterEncoder {
      public int Encode(
      int c,
      IWriter output) {
        if (c < 0) {
          return -1;
        }
        if (c < 0x80) {
          output.write((byte)c);
          return 1;
        }
        if (c >= 0xf780 && c <= 0xf7ff) {
          output.write((byte)(c - 0xf700));
          return 1;
        }
        return -2;
      }
    }

    private final Encoder encoder;
    private final Decoder decoder;

    public EncodingXUserDefined() {
      this.encoder = new Encoder();
      this.decoder = new Decoder();
    }

    public ICharacterDecoder GetDecoder() {
      return this.decoder;
    }

    public ICharacterEncoder GetEncoder() {
      return this.encoder;
    }
  }
