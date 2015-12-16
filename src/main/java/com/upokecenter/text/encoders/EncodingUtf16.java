package com.upokecenter.text.encoders;

import java.io.*;
import com.upokecenter.util.*;

import com.upokecenter.text.*;

  public class EncodingUtf16 implements ICharacterEncoding {
    private static class Decoder implements ICharacterDecoder {
      private final DecoderState state;
      private int lead;
      private int surrogate;
      private final boolean bigEndian;

      public Decoder(boolean bigEndian) {
        this.bigEndian = bigEndian;
        this.state = new DecoderState(1);
        this.lead = -1;
        this.surrogate = -1;
      }

      public int ReadChar(IByteReader stream) {
        while (true) {
          int b = this.state.ReadInputByte(stream);
          if (b < 0) {
            if (this.lead >= 0 || this.surrogate >= 0) {
              this.lead = this.surrogate = -1;
              return -2;
            }
            return -1;
          }
          if (this.lead < 0) {
            this.lead = b;
            continue;
          }
       int code = this.bigEndian ? b + (this.lead << 8) : this.lead + (b <<
            8);
          this.lead = -1;
          if (this.surrogate >= 0) {
            if ((code & 0xfc00) == 0xdc00) {
          code = 0x10000 + (code - 0xdc00) + ((this.surrogate - 0xd800) <<
                10);
              this.surrogate = -1;
              return code;
            }
            this.lead = -1;
            int b1 = (code >> 8) & 0xff;
            int b2 = code & 0xff;
            if (this.bigEndian) {
              this.state.PrependTwo(b1, b2);
            } else {
              this.state.PrependTwo(b2, b1);
            }
            this.surrogate = -1;
            return -2;
          }
          if ((code & 0xfc00) == 0xd800) {
            this.surrogate = code;
          } else {
 return (code & 0xfc00) == 0xdc00 ? -2 : code;
}
        }
      }
    }

    private static class Encoder implements ICharacterEncoder {
      private final boolean bigEndian;

      public Encoder(boolean bigEndian) {
        this.bigEndian = bigEndian;
      }

      public int Encode(
       int c,
       IWriter output) {
        if (c < 0) {
          return -1;
        }
        if (c > 0x10ffff || ((c & 0x1ff800) == 0xd800)) {
          return -2;
        }
        int b1, b2;
        if (c <= 0xffff) {
          b1 = (c >> 8) & 0xff;
          b2 = c & 0xff;
          if (this.bigEndian) {
            output.write((byte)b1);
            output.write((byte)b2);
          } else {
            output.write((byte)b2);
            output.write((byte)b1);
          }
          return 2;
        }
        byte[] buf = new byte[4];
        int c1 = ((c - 0x10000) >> 10) + 0xd800;
        int c2 = ((c - 0x10000) & 0x3ff) + 0xdc00;
        b1 = (c1 >> 8) & 0xff;
        b2 = c1 & 0xff;
        if (this.bigEndian) {
          buf[0] = (byte)b1;
          buf[1] = (byte)b2;
        } else {
          buf[0] = (byte)b2;
          buf[1] = (byte)b1;
        }
        b1 = (c2 >> 8) & 0xff;
        b2 = c2 & 0xff;
        if (this.bigEndian) {
          buf[2] = (byte)b1;
          buf[3] = (byte)b2;
        } else {
          buf[2] = (byte)b2;
          buf[3] = (byte)b1;
        }
        output.write(buf, 0, 4);
        return 4;
      }
    }

    public static ICharacterDecoder GetDecoder2(boolean bigEndian) {
      return new Decoder(bigEndian);
    }

    public static ICharacterEncoder GetEncoder2(boolean bigEndian) {
      return new Encoder(bigEndian);
    }

    public ICharacterDecoder GetDecoder() {
      return GetDecoder2(false);
    }

    public ICharacterEncoder GetEncoder() {
      return GetEncoder2(false);
    }
  }
