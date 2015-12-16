package com.upokecenter.text.encoders;

import java.io.*;
import com.upokecenter.util.*;

import com.upokecenter.text.*;

  public class EncodingKoreanEUC implements ICharacterEncoding {
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
            int c = -1;
            if (b >= 0x41 && b <= 0xfe) {
              c = ((this.lead - 0x81) * 190) + (b - 0x41);
              c = Korean.IndexToCodePoint(c);
            }
            this.lead = 0;
            if (c < 0) {
              if (b < 0x80) {
 this.state.PrependOne(b);
}
                return -2;
            }
              return c;
          }
          if (b <= 0x7f) {
            return b;
          } else if (b >= 0x81 && b <= 0xfe) {
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
        int cp = Korean.CodePointToIndex(c);
        if (cp < 0) {
          return -2;
        }
        int a = cp / 190;
        int b = cp % 190;
        output.write((byte)(a + 0x81));
        output.write((byte)(b + 0x41));
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
