package com.upokecenter.text.encoders;

import java.io.*;
import com.upokecenter.util.*;

import com.upokecenter.text.*;

 public class EncodingUtf8 implements ICharacterEncoding {
    private static class Decoder implements ICharacterDecoder {
      private final DecoderState state;
      private int cp;
        private int bytesSeen;
        private int bytesNeeded;
        private int lower = 0x80;
        private int upper = 0xbf;

        public Decoder() {
        this.state = new DecoderState(2);
      }

      public int ReadChar(IByteReader stream) {
        while (true) {
          int b = this.state.ReadInputByte(stream);
          if (b < 0) {
            if (this.bytesNeeded != 0) {
              this.bytesNeeded = 0;
              return -2;
            }
            return -1;
          }
          if (this.bytesNeeded == 0) {
            if ((b & 0x7f) == b) {
              return b;
            } else if (b >= 0xc2 && b <= 0xdf) {
              this.bytesNeeded = 1;
              this.cp = (b - 0xc0) << 6;
            } else if (b >= 0xe0 && b <= 0xef) {
              this.lower = (b == 0xe0) ? 0xa0 : 0x80;
              this.upper = (b == 0xed) ? 0x9f : 0xbf;
              this.bytesNeeded = 2;
              this.cp = (b - 0xe0) << 12;
            } else if (b >= 0xf0 && b <= 0xf4) {
              this.lower = (b == 0xf0) ? 0x90 : 0x80;
              this.upper = (b == 0xf4) ? 0x8f : 0xbf;
              this.bytesNeeded = 3;
              this.cp = (b - 0xf0) << 18;
            } else {
              return -2;
            }
            continue;
          }
          if (b < this.lower || b > this.upper) {
            this.cp = this.bytesNeeded = this.bytesSeen = 0;
            this.lower = 0x80;
            this.upper = 0xbf;
            this.state.PrependOne(b);
            return -2;
          } else {
            this.lower = 0x80;
            this.upper = 0xbf;
            ++this.bytesSeen;
            this.cp += (b - 0x80) << (6 * (this.bytesNeeded - this.bytesSeen));
            if (this.bytesSeen != this.bytesNeeded) {
              continue;
            }
            int ret = this.cp;
            this.cp = 0;
            this.bytesSeen = 0;
            this.bytesNeeded = 0;
            return ret;
          }
        }
      }
    }

    private static class Encoder implements ICharacterEncoder {
      public int Encode(int c, IWriter stream) {
        if (c < 0) {
 return -1;
}
        if (c < 0x80) {
          stream.write((byte)c);
          return 1;
        }
        byte[] bytes = new byte[4];
        int byteIndex = 0;
        if (c <= 0x7ff) {
          bytes[byteIndex++] = (byte)(0xc0 | ((c >> 6) & 0x1f));
          bytes[byteIndex++] = (byte)(0x80 | (c & 0x3f));
        } else if (c <= 0xffff) {
          if (c >= 0xd800 && c < 0xe000) {
            return -2;
          }
          bytes[byteIndex++] = (byte)(0xe0 | ((c >> 12) & 0x0f));
            bytes[byteIndex++] = (byte)(0x80 | ((c >> 6) & 0x3f));
            bytes[byteIndex++] = (byte)(0x80 | (c & 0x3f));
        } else if (c <= 0x10ffff) {
            bytes[byteIndex++] = (byte)(0xf0 | ((c >> 18) & 0x07));
            bytes[byteIndex++] = (byte)(0x80 | ((c >> 12) & 0x3f));
            bytes[byteIndex++] = (byte)(0x80 | ((c >> 6) & 0x3f));
            bytes[byteIndex++] = (byte)(0x80 | (c & 0x3f));
        } else {
          return -2;
        }
        stream.write(bytes, 0, byteIndex);
        return byteIndex;
      }
    }

    private final Encoder encoder = new Encoder();

    public ICharacterDecoder GetDecoder() {
    return new Decoder();
  }

  public ICharacterEncoder GetEncoder() {
    return this.encoder;
  }
 }
