package com.upokecenter.text.encoders;

import java.io.*;
import com.upokecenter.util.*;

import com.upokecenter.text.*;

 public class EncodingSingleByte implements ICharacterEncoding {
   private static class Decoder implements ICharacterDecoder {
      private final int[] codepoints;

      public Decoder(int[] codepoints) {
        this.codepoints = codepoints;
      }

     public int ReadChar(IByteReader transform) {
       int b = transform.read();
       return (b < 0) ? (-1) : ((b < 0x80) ? b : this.codepoints[b - 0x80]);
    }
  }

   private static class Encoder implements ICharacterEncoder {
      private final int[] codepoints;

      public Encoder(int[] codepoints) {
        this.codepoints = codepoints;
      }

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
         for (int i = 0; i < this.codepoints.length; ++i) {
           if (this.codepoints[i] == c) {
             output.write((byte)(i + 0x80));
             return 1;
           }
         }
         return -2;
    }
  }

    private final Encoder encoder;
    private final Decoder decoder;

    public EncodingSingleByte(int[] codepoints) {
        if (codepoints == null) {
  throw new NullPointerException("codepoints");
}
        if (codepoints.length != 128) {
  throw new IllegalArgumentException("codepoints.length (" + codepoints.length +
    ") is not equal to " + 128);
}
this.encoder = new Encoder(codepoints);
this.decoder = new Decoder(codepoints);
      }

  public ICharacterDecoder GetDecoder() {
    return this.decoder;
  }

  public ICharacterEncoder GetEncoder() {
    return this.encoder;
  }
 }
