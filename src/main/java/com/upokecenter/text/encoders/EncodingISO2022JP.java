package com.upokecenter.text.encoders;

import java.io.*;
import com.upokecenter.util.*;

import com.upokecenter.text.*;

  public class EncodingISO2022JP implements ICharacterEncoding {
    private static class Decoder implements ICharacterDecoder {
      private int machineState;
      private int outputState;
      private int lead;
      private int output;
      private final DecoderState state;

      public Decoder() {
        this.state = new DecoderState(2);
      }

      public int ReadChar(IByteReader stream) {
        while (true) {
          int b = this.state.ReadInputByte(stream);
          switch (this.machineState) {
            case 0:
              // ASCII
              if (b == 0x1b) {
                this.machineState = 1;
              } else if (b < 0) {
                return -1;
              } else if (b <= 0x7f && b != 0x0e && b != 0x0f) {
                output = 0;
                return b;
              } else {
                output = 0;
                return -2;
              }
              break;
            case 1:
              // Escape start
              if (b == 0x24 || b == 0x28) {
                this.lead = b;
                this.machineState = 2;
              } else {
                state.PrependOne(b);
                output = 0;
                machineState = outputState;
                return -2;
              }
              break;
            case 2: {
                // Escape
                int tmpState = -1;
                if (this.lead == 0x24 && (b == 0x40 || b == 0x42)) {
                  tmpState = 4;  // JIS0208
                  this.lead = 0;
                } else if (this.lead == 0x28 && b == 0x42) {
                  tmpState = 0;  // Ascii
                  this.lead = 0;
                } else if (this.lead == 0x28 && b == 0x4a) {
                  tmpState = 3;  // Roman
                  this.lead = 0;
                } else if (this.lead == 0x28 && b == 0x49) {
                  tmpState = 6;
                  this.lead = 0;
                } else {
                  // NOTE: Escape sequence (0x24 0x28 0x44)
                  // enabling JIS0212 is no longer supported
                  this.state.PrependTwo(this.lead, b);
                  this.lead = 0;
                  this.machineState = this.outputState;
                  return -2;
                }
                this.machineState = this.outputState = tmpState;
                if (this.output != 0) {
                  return -2;
                } else {
                  this.output = 1;
                }
                break;
              }
            case 4:
              // Lead
              if (b == 0x1b) {
                this.machineState = 1;
              } else if (b >= 0x21 && b <= 0x7e) {
                output = 0;
                lead = b;
                machineState = 5;
              } else if (b < 0) {
                return -1;
              } else {
                output = 0;
                return -2;
              }
              break;
            case 5:  // Trail
              if (b < 0) {
                this.machineState = 4;
                this.state.PrependOne(b);
                return -2;
              } else if (b == 0x1b) {
                machineState = 1;
                return -2;
              } else if (b >= 0x21 && b <= 0x7e) {
                machineState = 4;
                int p = (lead - 0x21) * 94 + (b - 0x21);
                // NOTE: Only JIS0208 now supported here
                int c = Jis0208.IndexToCodePoint(p);
                return c < 0 ? -2 : c;
              } else {
                machineState = 4;
                return -2;
              }
            case 6:  // Katakana
              if (b == 0x1b) {
                this.machineState = 1;
              } else if (b >= 0x21 && b <= 0x5f) {
                output = 0;
                return 0xff61 + b - 0x21;
              } else if (b < 0) {
                return -1;
              } else {
                output = 0;
                return -2;
              }
              break;
            case 3:  // Roman
              if (b == 0x1b) {
                this.machineState = 1;
              } else if (b == 0x5c) {
                output = 0;
                return 0xa5;
              } else if (b == 0x7e) {
                output = 0;
                return 0x203e;
              } else if (b < 0x7f && b != 0x0e && b != 0x0f) {
                output = 0;
                return b;
              } else if (b < 0) {
                return -1;
              } else {
                output = 0;
                return -2;
              }
              break;
            default: {
                // NOTE: Escape final state is no longer used
                throw new IllegalStateException("Unexpected state");
              }
          }
        }
      }
    }

    private static class Encoder implements ICharacterEncoder {
      private int encoderState;

      public Encoder() {
      }

      public int Encode(int c, IWriter output) {
        int count = 0;
        while (true) {
          if (c < 0) {
            if (this.encoderState != 0) {
              this.encoderState = 0;
              output.write((byte)0x1b);
              output.write((byte)0x28);
              output.write((byte)0x42);
              return count + 3;
            } else {
              return -1;
            }
          }
          if (c <= 0x7f) {
            if (this.encoderState == 0) {
              output.write((byte)c);
              return 1 + count;
            } else if (this.encoderState == 3 && c != 0x5c && c != 0x7e) {
              output.write((byte)c);
              return 1 + count;
            } else {
              this.encoderState = 0;
              output.write((byte)0x1b);
              output.write((byte)0x28);
              output.write((byte)0x42);
              count += 3;
            }
          }
          if (this.encoderState == 3 && c == 0xa5) {
            output.write((byte)0x5c);
            return 1 + count;
          }
          if (this.encoderState == 3 && c == 0x203e) {
            output.write((byte)0x7e);
            return 1 + count;
          }
          if (this.encoderState != 3 && (c == 0xa5 || c == 0x203e)) {
            this.encoderState = 3;
            output.write((byte)0x1b);
            output.write((byte)0x28);
            output.write((byte)0x4a);
            count += 3;
            continue;
          }
          if (c == 0x2022) {
            c = 0xff0d;
          }
          int cp = Jis0208.CodePointToIndex(c);
          if (cp < 0) {
            return -2;
          }
          if (this.encoderState != 4) {
            this.encoderState = 4;
            output.write((byte)0x1b);
            output.write((byte)0x24);
            output.write((byte)0x42);
            count += 3;
          }
          int a = cp / 94;
          int b = cp % 94;
          output.write((byte)(a + 0x21));
          output.write((byte)(b + 0x21));
          return count + 2;
        }
      }
    }

    public ICharacterDecoder GetDecoder() {
      return new Decoder();
    }

    public ICharacterEncoder GetEncoder() {
      return new Encoder();
    }
  }
