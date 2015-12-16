package com.upokecenter.text.encoders;

import java.io.*;
import com.upokecenter.util.*;

import com.upokecenter.text.*;

  public class EncodingShiftJIS implements ICharacterEncoding {
    private static class Decoder implements ICharacterDecoder {
      private final DecoderState state;
      private int lead;

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
          if (this.lead != 0) {
            int cp = -1;
            int offset = (b < 0x7f) ? 0x40 : 0x41;
            int leadoffset = (this.lead < 0xa0) ? 0x81 : 0xc1;
            if ((b >= 0x40 && b <= 0xfc) && b != 0x7f) {
              cp = ((this.lead - leadoffset) * 188) + (b - offset);
            }
            this.lead = 0;
            int c = (cp < 0) ? -1 : Jis0208.IndexToCodePoint(cp);
            if (c < 0 && cp >= 8836 && cp <= 10528) {
              return 0xe000 + cp - 8836;
            }
            if (c < 0) {
              if (b < 0) {
                this.state.PrependOne(b);
              }
              return -2;
            }
            return c;
          }
          if (b <= 0x80) {
            return b;
          } else if (b >= 0xa1 && b <= 0xdf) {
            return 0xff61 + b - 0xa1;
          } else if (b >= 0x81 && b <= 0xfc) {
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
        if (c <= 0x80) {
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
          output.write((byte)(c - 0xff61 + 0xa1));
          return 1;
        }
        if (c == 0x2022) {
          c = 0xff0d;
        }
        int cp = Jis0208.ShiftJISCodePointToIndex(c);
        if (cp < 0) {
          return -2;
        }
        int lead = cp / 188;
        int trail = cp % 188;
        int a = (lead < 0x1f) ? 0x81 : 0xc1;
        int b = (trail < 0x3f) ? 0x40 : 0x41;
        output.write((byte)(a + lead));
        output.write((byte)(b + trail));
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
