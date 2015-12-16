package com.upokecenter.text.encoders;

import java.io.*;
import com.upokecenter.util.*;

import com.upokecenter.text.*;

  public class EncodingUtf7 implements ICharacterEncoding {
    public static final int[] Alphabet = { -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
          -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
        52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
        -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
        15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
        -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
        41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1 };

    public static final int[] ToAlphabet = {
  0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4a, 0x4b, 0x4c, 0x4d,
  0x4e, 0x4f, 0x50, 0x51, 0x52, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58, 0x59, 0x5a,
  0x61, 0x62, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6a, 0x6b, 0x6c, 0x6d,
  0x6e, 0x6f, 0x70, 0x71, 0x72, 0x73, 0x74, 0x75, 0x76, 0x77, 0x78, 0x79, 0x7a,
        0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x2b, 0x2f
      };

    private static class Decoder implements ICharacterDecoder {
      private final DecoderState state;
      private int alphavalue;
      private int base64value;
      private final CodeUnitAppender appender;
      private int base64count;
      // 0: not in base64; 1: start of base 64; 2: continuing base64
      private int machineState;

      public Decoder() {
        this.state = new DecoderState(4);
        this.appender = new CodeUnitAppender();
      }

      private static final class CodeUnitAppender {
        private int surrogate;
        private int lastByte;

        public CodeUnitAppender() {
          this.surrogate = -1;
          this.lastByte = -1;
        }

        public void FinalizeAndReset(DecoderState state) {
          if (this.surrogate >= 0 && this.lastByte >= 0) {
            // Unpaired surrogate and an unpaired byte value
            state.AppendChar(-2);
            state.AppendChar(-2);
          } else if (this.surrogate >= 0 || this.lastByte >= 0) {
            // Unpaired surrogate or byte value remains
            state.AppendChar(-2);
          }
          this.surrogate = -1;
          this.lastByte = -1;
        }

        public void AppendIncompleteByte() {
          // Make sure lastByte isn't -1, for FinalizeAndReset
          // purposes
          this.lastByte = 0;
        }

        public void AppendByte(int value, DecoderState state) {
          if (this.lastByte >= 0) {
            int codeunit = this.lastByte << 8;
            codeunit |= value & 0xff;
            this.AppendCodeUnit(codeunit, state);
            this.lastByte = -1;
          } else {
            this.lastByte = value;
          }
        }

        private void AppendCodeUnit(int codeunit, DecoderState state) {
          if (this.surrogate >= 0) {
            // If we have a surrogate, "codeunit"
            // must be a valid "low surrogate" to complete the pair
            if ((codeunit & 0xfc00) == 0xdc00) {
              // valid low surrogate
              int codepoint = 0x10000 + (codeunit - 0xdc00) +
                ((this.surrogate - 0xd800) << 10);
              state.AppendChar(codepoint);
              this.surrogate = -1;
            } else if ((codeunit & 0xfc00) == 0xd800) {
              // unpaired high surrogate
              state.AppendChar(-2);
              this.surrogate = codeunit;
            } else {
              // not a surrogate, output the first as U + FFFD
              // and the second as is
              state.AppendChar(-2);
              state.AppendChar((char)codeunit);
              this.surrogate = -1;
            }
          } else {
            if ((codeunit & 0xfc00) == 0xdc00) {
              // unpaired low surrogate
              state.AppendChar(-2);
            } else if ((codeunit & 0xfc00) == 0xd800) {
              // valid high surrogate
              this.surrogate = codeunit;
            } else {
              // not a surrogate
              state.AppendChar((char)codeunit);
            }
          }
        }

        public void Reset() {
          this.surrogate = -1;
          this.lastByte = -1;
        }
      }

      public int ReadChar(IByteReader stream) {
        int ch = this.state.GetChar();
        if (ch != -1) {
          return ch;
        }
        while (true) {
          int b;
          switch (this.machineState) {
            case 0:
              // not in base64
              b = this.state.ReadInputByte(stream);
              if (b < 0) {
                // done
                return -1;
              }
              if (b == 0x09 || b == 0x0a || b == 0x0d) {
                return b;
              } else if (b == 0x5c || b >= 0x7e || b < 0x20) {
                // Illegal byte in UTF-7
                return -2;
              } else if (b == 0x2b) {
                // plus sign
                machineState = 1;  // change state to "start of base64"
                base64value = 0;
                base64count = 0;
                appender.Reset();
              } else {
                return b;
              }
              break;
            case 1:  // start of base64
              b = this.state.ReadInputByte(stream);
              if (b < 0) {
                // End of stream, illegal
                this.machineState = 0;
                return -2;
              }
              if (b == 0x2d) {
                // hyphen, so output a plus sign
                this.machineState = 0;
                this.state.AppendChar('+');
                ch = this.state.GetChar();
                if (ch != -1) {
                  return ch;
                }
              } else if (b >= 0x80) {
                // Non-ASCII byte, illegal
                machineState = 0;
                state.AppendChar(-2);  // for the illegal plus
             state.AppendChar(-2);  // for the illegal non-ASCII byte
                ch = state.GetChar();
                if (ch != -1) {
                  return ch;
                }
              } else {
                alphavalue = Alphabet[b];
                if (alphavalue >= 0) {
                  machineState = 2;  // change state to "continuing base64"
                  base64value <<= 6;
                  base64value |= alphavalue;
                  ++base64count;
                } else {
                  // Non-base64 byte (NOTE: Can't be plus or
                  // minus at this point)
                  machineState = 0;
                  state.AppendChar(-2);  // for the illegal plus
                  if (b == 0x09 || b == 0x0a || b == 0x0d) {
                    state.AppendChar((char)b);
                  } else if (b == 0x5c || b >= 0x7e || b < 0x20) {
                    // Illegal byte in UTF-7
                    state.AppendChar(-2);
                  } else {
                    state.AppendChar((char)b);
                  }
                  ch = state.GetChar();
                  if (ch != -1) {
                    return ch;
                  }
                }
              }
              break;
            case 2:
              // continuing base64
              b = this.state.ReadInputByte(stream);
              this.alphavalue = (b < 0 || b >= 0x80) ? -1 : Alphabet[b];
              if (this.alphavalue >= 0) {
                // Base64 alphabet (except padding)
                this.base64value <<= 6;
                this.base64value |= this.alphavalue;
                ++this.base64count;
                if (this.base64count == 4) {
                  // Generate UTF-16 bytes
         this.appender.AppendByte((this.base64value >> 16) & 0xff, this.state);
          this.appender.AppendByte((this.base64value >> 8) & 0xff, this.state);
                  this.appender.AppendByte(this.base64value & 0xff, this.state);
                  this.base64count = 0;
                }
              } else {
                machineState = 0;
                switch (base64count) {
                  case 1: {
                    // incomplete base64 byte
                    appender.AppendIncompleteByte();
                    break;
                    }
                  case 2: {
                    base64value <<= 12;
                    appender.AppendByte((base64value >> 16) & 0xff, state);
                    if ((base64value & 0xffff) != 0) {
                    // Redundant pad bits
                    appender.AppendIncompleteByte();
                    }
                    break;
                    }
                  case 3: {
                    base64value <<= 6;
                    appender.AppendByte((base64value >> 16) & 0xff, state);
                    appender.AppendByte((base64value >> 8) & 0xff, state);
                    if ((base64value & 0xff) != 0) {
                    // Redundant pad bits
                    appender.AppendIncompleteByte();
                    }
                    break;
                    }
                }
                appender.FinalizeAndReset(state);
                if (b < 0) {
                  // End of stream
                  ch = state.GetChar();
                  return (ch != -1) ? (ch) : (-1);
                }
                if (b == 0x2d) {
                  // Ignore the hyphen
                } else if (b == 0x09 || b == 0x0a || b == 0x0d) {
                  state.AppendChar((char)b);
                } else if (b == 0x5c || b >= 0x7e || b < 0x20) {
                  // Illegal byte in UTF-7
                  state.AppendChar(-2);
                } else {
                  state.AppendChar((char)b);
                }
              }
              ch = this.state.GetChar();
              if (ch != -1) {
                return ch;
              }
              break;
            default: throw new IllegalStateException("Unexpected state");
          }
        }
      }
    }

    private static class Encoder implements ICharacterEncoder {
      private static int Base64Char(int c, IWriter output) {
        if (c <= 0xffff) {
          int byte1 = (c >> 8) & 0xff;
          int byte2 = c & 0xff;
          int c1 = ToAlphabet[(byte1 >> 2) & 63];
          int c2 = ToAlphabet[((byte1 & 3) << 4) + ((byte2 >> 4) & 15)];
          int c3 = ToAlphabet[((byte2 & 15) << 2)];
          output.write((byte)0x2b);
          output.write((byte)c1);
          output.write((byte)c2);
          output.write((byte)c3);
          output.write((byte)0x2d);
          return 5;
        } else {
          int cc1 = (((c - 0x10000) >> 10) & 0x3ff) + 0xd800;
          int cc2 = ((c - 0x10000) & 0x3ff) + 0xdc00;
          int byte1 = (cc1 >> 8) & 0xff;
          int byte2 = cc1 & 0xff;
          int byte3 = (cc2 >> 8) & 0xff;
          int byte4 = cc2 & 0xff;
          int c1 = ToAlphabet[(byte1 >> 2) & 63];
          int c2 = ToAlphabet[((byte1 & 3) << 4) + ((byte2 >> 4) & 15)];
          int c3 = ToAlphabet[((byte2 & 15) << 2) + ((byte3 >> 6) & 3)];
          int c4 = ToAlphabet[byte3 & 63];
          int c5 = ToAlphabet[(byte4 >> 2) & 63];
          int c6 = ToAlphabet[((byte4 & 3) << 4)];
          output.write((byte)0x2b);
          output.write((byte)c1);
          output.write((byte)c2);
          output.write((byte)c3);
          output.write((byte)c4);
          output.write((byte)c5);
          output.write((byte)c6);
          output.write((byte)0x2d);
          return 8;
        }
      }

      public int Encode(int c, IWriter output) {
        if (c < 0) {
 return -1;
}
        if (c == 0x2b) {
          output.write((byte)0x2b);
          output.write((byte)0x2d);
          return 2;
        }
     if (c == 0x09 || c == 0x0a || c == 0x0d || (c >= 0x20 && c < 0x7e && c !=
          0x5c)) {
          output.write((byte)c);
          return 2;
        }
        return (c >= 0x110000 || (c >= 0xd800 && c < 0xe000)) ? (-2) :
          Base64Char(c, output);
      }
    }

    public ICharacterDecoder GetDecoder() {
      return new Decoder();
    }

    public ICharacterEncoder GetEncoder() {
      return new Encoder();
    }
  }
