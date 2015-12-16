package com.upokecenter.text.encoders;

import java.io.*;

import com.upokecenter.util.*;

import com.upokecenter.text.*;

  public class EncoderAlgorithms {
    private static class DecodeWithFallbackDecoder implements ICharacterDecoder,
      ICharacterEncoding {
      private boolean bomChecked;
      private final DecoderState state;
      private ICharacterDecoder decoder;
      private boolean useOriginal;

      public DecodeWithFallbackDecoder(ICharacterEncoding encoding) {
        this.decoder = encoding.GetDecoder();
        this.state = new DecoderState(3);
        this.useOriginal = false;
      }

      public int ReadChar(IByteReader input) {
        if (input == null) {
          throw new NullPointerException("input");
        }
        if (this.useOriginal) {
          return this.decoder.ReadChar(input);
        }
        if (!this.bomChecked) {
          int c = 0;
          int[] buffer = new int[3];
          int bufferCount = 0;
          this.bomChecked = true;
          while (c >= 0 && bufferCount < 3) {
            c = input.read();
            if (c >= 0) {
              buffer[bufferCount++] = c;
            }
          }
          if (bufferCount >= 3 && buffer[0] == 0xef &&
             buffer[1] == 0xbb && buffer[2] == 0xbf) {
            this.decoder = Encodings.UTF8.GetDecoder();
            this.useOriginal = true;
          } else if (bufferCount >= 2 && buffer[0] == 0xfe &&
            buffer[1] == 0xff) {
            if (bufferCount == 3) {
              this.state.PrependOne(buffer[2]);
              this.useOriginal = true;
            } else {
              this.useOriginal = false;
            }
            this.decoder = new EncodingUtf16BE().GetDecoder();
          } else if (bufferCount >= 2 && buffer[0] == 0xff &&
            buffer[1] == 0xfe) {
            if (bufferCount == 3) {
              this.state.PrependOne(buffer[2]);
              this.useOriginal = true;
            } else {
              this.useOriginal = false;
            }
            this.decoder = new EncodingUtf16().GetDecoder();
          } else {
            // No BOM found
            this.useOriginal = false;
            switch (bufferCount) {
              case 1:
                this.state.PrependOne(buffer[0]);
                break;
              case 2:
                this.state.PrependTwo(buffer[0], buffer[1]);
                break;
              case 3:
                this.state.PrependThree(buffer[0], buffer[1], buffer[2]);
                break;
            }
          }
        }
        IByteReader br = this.state.ToTransformIfBuffered(input);
        this.useOriginal = br == input;
        return this.decoder.ReadChar(br);
      }

      public ICharacterEncoder GetEncoder() {
        throw new UnsupportedOperationException();
      }

      public ICharacterDecoder GetDecoder() {
        return this;
      }
    }

    private static class BomBufferedTransform implements IByteReader {
      private final int[] buffer;
      private int bufferOffset;
      private int bufferCount;
      private final IByteReader transform;
      private boolean bomChecked;

      public BomBufferedTransform(IByteReader transform) {
        this.buffer = new int[3];
        this.transform = transform;
      }

      private void CheckForUtf8BOM() {
        int c = 0;
        while (c >= 0 && this.bufferCount < 3) {
          c = this.transform.read();
          if (c >= 0) {
            this.buffer[this.bufferCount++] = c;
          }
        }
        if (this.bufferCount >= 3 && this.buffer[0] == 0xef &&
           this.buffer[1] == 0xbb && this.buffer[2] == 0xbf) {
          // UTF-8 BOM found
          this.bufferOffset = this.bufferCount = 0;
        } else {
          // BOM not found
          this.bufferOffset = 0;
        }
      }

      public int read() {
        if (!this.bomChecked) {
          this.bomChecked = true;
          this.CheckForUtf8BOM();
        }
        if (this.bufferOffset < this.bufferCount) {
          int c = this.buffer[this.bufferOffset++];
          if (this.bufferOffset >= this.bufferCount) {
            this.bufferOffset = this.bufferCount = 0;
          }
          return c;
        }
        return this.transform.read();
      }
    }

    public static ICharacterInput Utf8DecodeAlgorithmInput(
       IByteReader transform) {
      // Implements the "utf-8 decode" algorithm in the Encoding
      // Standard
      if (transform == null) {
        throw new NullPointerException("transform");
      }
      BomBufferedTransform bomTransform = new BomBufferedTransform(transform);
      return Encodings.GetDecoderInput(
Encodings.UTF8,
bomTransform);
    }

    public static int Utf8EncodeAlgorithm(
       ICharacterInput stream,
       IWriter output) {
      // Implements the "utf-8 encode" algorithm
      // in the Encoding Standard
      return EncodeAlgorithm(stream, Encodings.UTF8, output);
    }

    public static int EncodeAlgorithm(
      ICharacterInput stream,
      ICharacterEncoding encoding,
      IWriter output) {
      int total = 0;
      ICharacterEncoder encoder = encoding.GetEncoder();
      // Implements the "encode" algorithm
      // in the Encoding Standard
      if (stream == null) {
        throw new NullPointerException("stream");
      }
      if (output == null) {
        throw new NullPointerException("output");
      }
      DecoderState state = new DecoderState(1);
      while (true) {
        int c = state.GetChar();
        if (c < 0) {
          c = stream.ReadChar();
        }
        int r = encoder.Encode(c, output);
        if (r == -1) {
          break;
        }
        if (r == -2) {
          if (c < 0 || c >= 0x110000 || ((c & 0xf800) == 0xd800)) {
            throw new IllegalArgumentException("code point out of range");
          }
          state.AppendChar(0x26);
          state.AppendChar(0x23);
          if (c == 0) {
            state.AppendChar(0x30);
          } else {
            while (c > 0) {
              state.AppendChar(0x30 + (c % 10));
              c /= 10;
            }
          }
          state.AppendChar(0x3b);
        } else {
          total += r;
        }
      }
      return total;
    }

    public static ICharacterInput Utf8DecodeWithoutBOMAlgorithmInput(
       IByteReader transform) {
      // Implements the "utf-8 decode without BOM" algorithm
      // in the Encoding Standard
      if (transform == null) {
        throw new NullPointerException("transform");
      }
      return Encodings.GetDecoderInput(
Encodings.UTF8,
transform);
    }

    public static ICharacterInput DecodeAlgorithmInput(
       IByteReader transform,
       ICharacterEncoding fallbackEncoding) {
      // Implements the "decode" algorithm in the Encoding
      // Standard
      if (transform == null) {
        throw new NullPointerException("transform");
      }
      if (fallbackEncoding == null) {
        throw new NullPointerException("fallbackEncoding");
      }
      DecodeWithFallbackDecoder decoder = new DecodeWithFallbackDecoder(
        fallbackEncoding);
      return Encodings.GetDecoderInput(decoder, transform);
    }
  }
