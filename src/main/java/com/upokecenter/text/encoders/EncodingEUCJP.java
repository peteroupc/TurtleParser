package com.upokecenter.text.encoders;

import java.io.*;
import com.upokecenter.util.*;

import com.upokecenter.text.*;

  public class EncodingEUCJP implements ICharacterEncoding {
    private static class Decoder implements ICharacterDecoder {
      private final DecoderState state;
      private int lead;
        private boolean jis0212;

      public Decoder() {
        this.state = new DecoderState(1);
        this.lead = 0;
      }

      public int ReadChar(IByteReader stream) {
        while (true) {
          int b = this.state.ReadInputByte(stream);
          if (b < 0) {
            if (this.lead != 0) {
              this.lead = 0;
              return -2;
            }
            return -1;
          }
          if (this.lead == 0x8e && (b >= 0xa1 && b <= 0xdf)) {
            this.lead = 0;
            return 0xff61 + b - 0xa1;
          }
          if (this.lead == 0x8f && (b >= 0xa1 && b <= 0xfe)) {
            this.lead = b;
            this.jis0212 = true;
            continue;
          }
          if (this.lead != 0) {
            int c = -1;
      if ((this.lead >= 0xa1 && this.lead <= 0xfe) && b >= 0xa1 && b <= 0xfe) {
              c = ((this.lead - 0xa1) * 94) + (b - 0xa1);
              c = this.jis0212 ? Jis0212.IndexToCodePoint(c) :
                    Jis0208.IndexToCodePoint(c);
            }
            this.lead = 0;
            this.jis0212 = false;
            if (b < 0xa1 || b == 0xff) {
              this.state.PrependOne(b);
            }
            return c < 0 ? -2 : c;
          }
          if (b <= 0x7f) {
            return b;
          } else if (b == 0x8e || b == 0x8f || (b >= 0xa1 && b <= 0xfe)) {
            this.lead = b;
            continue;
          } else {
           return -2;
          }
        }
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
        if (c == 0x203e) {
          output.write((byte)0x7e);
          return 1;
        }
        if (c == 0xa5) {
          output.write((byte)0x5c);
          return 1;
        }
        if (c >= 0xff61 && c <= 0xff9f) {
          output.write((byte)0x8e);
          output.write((byte)(c - 0xff61 + 0xa1));
          return 2;
        }
        if (c == 0x2022) {
          c = 0xff0d;
        }
        int cp = Jis0208.CodePointToIndex(c);
        if (cp < 0) {
          return -2;
        }
        int a = cp / 94;
        int b = cp % 94;
        output.write((byte)(a + 0xa1));
        output.write((byte)(b + 0xa1));
        return 2;
      }
    }

    public ICharacterDecoder GetDecoder() {
      return new Decoder();
    }

    public ICharacterEncoder GetEncoder() {
      return new Encoder();
    }
  }
