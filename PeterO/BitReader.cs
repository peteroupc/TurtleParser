/*
Written in 2008-2013 by Peter O.
Any copyright is dedicated to the Public Domain.
http://creativecommons.org/publicdomain/zero/1.0/

If you like this, you should donate to Peter O.
at: http://upokecenter.dreamhosters.com/articles/donate-now-2/
 */
namespace PeterO {
using System;
using System.Globalization;
/// <summary>
/// Reads a stream of bytes as a set of bits.
/// </summary>
public sealed class BitReader {
  private IReader stream;
  private int bitsLeft;
  private int bits;
  public enum BitStreamMode {
    MsbFirst, LsbFirst, ByteAlignment
  }
  BitStreamMode mode = BitStreamMode.MsbFirst;

    /// <summary>Gets or sets the bit stream reading mode. Note that
    /// setting this property will align the stream to the next byte
    /// boundary.</summary>
    /// <returns>A BitStreamMode object.</returns>
  public BitStreamMode getMode() {
 return mode;
}
  public void setMode(BitStreamMode value) {
    this.bitsLeft = 0;
    mode = value;
  }

    /// <summary>Initializes a new instance of the BitReader class. Creates
    /// a new BitReader from the specified stream.</summary>
    /// <param name='stream'>Readable stream to wrap.</param>
    /// <exception cref='NullPointerException'>The parameter <paramref
    /// name='stream'/> is null.</exception>
    /// <exception cref='ArgumentException'>The parameter <paramref
    /// name='stream'/> does not support reading.</exception>
  public BitReader(PeterO.IReader stream) {
    if ((stream) == null) {
 throw new ArgumentNullException();
}
    this.stream = stream;
  }

    /// <summary>Reads a number of bytes from the stream. Before reading,
    /// the method aligns the position to the next byte.</summary>
    /// <param name='count'>Not documented yet.</param>
    /// <returns>The bytes read.</returns>
    /// <exception cref='System.IO.IOException'>The end of the stream was
    /// reached.</exception>
  public byte[] ReadBytes(int count) {
    var array = new byte[count];
    bitsLeft = 0;
    stream.Read(array, 0, count);
    return array;
  }
  public void Reset() {
    bitsLeft = 0;
  }

    /// <summary>Reads the next byte from the stream. Before reading, the
    /// method aligns the position to the next byte.</summary>
    /// <returns>The byte read.</returns>
    /// <exception cref='EndOfStreamException'>The end of the stream was
    /// reached.</exception>
  public int ReadByte() {
    bitsLeft = 0;
    return stream.ReadByte();
  }

  private int[] bitMask={
      0x00, 0x01, 0x03, 0x07, 0x0f,
      0x1f, 0x3f, 0x7f, 0xFF
  };
  public int ReadBits(int bitCount) {
    if (bitCount < 0 || (bitCount)>32) {
 throw new ArgumentException("bitCount not in range ("
   +Convert.ToString(bitCount,CultureInfo.InvariantCulture)+")");
}
    var ret = 0;
    var shift = 0;
    var tmp = 0;
    if (this.mode == BitStreamMode.ByteAlignment) {
      // Least-significant byte first
      for (int i = bitCount;i>0;i-=8) {
        if (i< 8) {
          ret|=((ReadByte() & bitMask[i]) << shift);
        } else {
          ret|=(ReadByte() << shift);
        }
        shift+=8;
      }
    } else if (this.mode == BitStreamMode.MsbFirst) {
      // Most-significant bit per byte first
      for (int i = bitCount;i>0;) {
        if (bitsLeft == 0) {
          bits = ReadByte();
          bitsLeft = 8;
        }
        if (i >= bitsLeft) {
          bits <<= bitsLeft;
          tmp=((bits >> 8) & bitMask[bitsLeft]);
          ret|=(tmp << shift);
          i-=bitsLeft;
          shift+=bitsLeft;
          bitsLeft = 0;
        } else {
          bits <<= i;
          tmp=((bits >> 8) & bitMask[i]);
          ret|=(tmp << shift);
          bitsLeft-=i;
          break;
        }
      }
    } else if (this.mode == BitStreamMode.LsbFirst) {
      // Least-significant bit per byte first
      for (int i = bitCount;i>0;) {
        if (bitsLeft == 0) {
          bits = ReadByte();
          bitsLeft = 8;
        }
        if (i >= bitsLeft) {
          tmp = bits & bitMask[bitsLeft];
          bits>>= bitsLeft;
          ret|=tmp << shift;
          i-=bitsLeft;
          shift+=bitsLeft;
          bitsLeft = 0;
        } else {
          tmp = bits & bitMask[i];
          bits>>= i;
          ret|=tmp << shift;
          bitsLeft-=i;
          break;
        }
      }
    }
    return ret;
  }
}
}
