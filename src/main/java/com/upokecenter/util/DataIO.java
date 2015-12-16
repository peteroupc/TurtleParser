package com.upokecenter.util;
/*
Written by Peter O. in 2014.
Any copyright is dedicated to the Public Domain.
http://creativecommons.org/publicdomain/zero/1.0/
If you like this, you should donate to Peter O.
at: http://upokecenter.dreamhosters.com/articles/donate-now-2/
 */

import java.io.*;

    /**
     * Convenience class that contains static methods for wrapping byte arrays and
     * streams into byte readers and byte writers.
     */
  public final class DataIO {
private DataIO() {
}
    /**
     * Wraps a byte array into a byte reader. The reader will start at the
     * beginning of the byte array. <p>In the .NET implementation, this
     * method is implemented as an extension method to any byte array object
     * and can be called as follows: <code>bytes.ToByteReader()</code>. If the
     * object's class already has a ToByteReader method with the same
     * parameters, that method takes precedence over this extension
     * method.</p>
     * @param bytes The byte array to wrap.
     * @return A byte reader wrapping the byte array.
     * @throws NullPointerException The parameter {@code bytes} is null.
     */
    public static IByteReader ToByteReader(byte[] bytes) {
      if (bytes == null) {
        throw new NullPointerException("bytes");
      }
      return new ByteArrayTransform(bytes, 0, bytes.length);
    }

    /**
     * Wraps a portion of a byte array into a byte reader object. <p>In the .NET
     * implementation, this method is implemented as an extension method to
     * any byte array object and can be called as follows:
     * <code>bytes.ToByteReader(offset, length)</code>. If the object's class
     * already has a ToByteReader method with the same parameters, that
     * method takes precedence over this extension method.</p>
     * @param bytes The byte array to wrap.
     * @param offset A zero-based index showing where the desired portion of
     * "bytes" begins.
     * @param length The length, in bytes, of the desired portion of "bytes" (but
     * not more than "bytes" 's length).
     * @return A byte reader wrapping the byte array.
     * @throws NullPointerException The parameter {@code bytes} is null.
     * @throws IllegalArgumentException Either {@code offset} or {@code length} is less
     * than 0 or greater than {@code bytes} 's length, or {@code bytes} 's
     * length minus {@code offset} is less than {@code length}.
     */
    public static IByteReader ToByteReader(
byte[] bytes,
int offset,
int length) {
      if (bytes == null) {
        throw new NullPointerException("bytes");
      }
      if (offset < 0) {
        throw new IllegalArgumentException("offset (" + offset +
          ") is less than " + 0);
      }
      if (offset > bytes.length) {
        throw new IllegalArgumentException("offset (" + offset + ") is more than " +
          bytes.length);
      }
      if (length < 0) {
        throw new IllegalArgumentException("length (" + length +
          ") is less than " + 0);
      }
      if (length > bytes.length) {
        throw new IllegalArgumentException("length (" + length + ") is more than " +
          bytes.length);
      }
      if (bytes.length - offset < length) {
        throw new IllegalArgumentException("bytes's length minus " + offset + " (" +
          (bytes.length - offset) + ") is less than " + length);
      }
      return new ByteArrayTransform(bytes, offset, length);
    }

    /**
     * Wraps an input stream into a reader object. If an IOException is thrown by
     * the input stream, the reader object throws IllegalStateException
     * instead. <p>In the .NET implementation, this method is implemented as
     * an extension method to any object implementing InputStream and can be
     * called as follows: <code>input.ToByteReader()</code>. If the object's class
     * already has a ToByteReader method with the same parameters, that
     * method takes precedence over this extension method.</p>
     * @param input The input stream to wrap.
     * @return A byte reader wrapping the input stream.
     * @throws NullPointerException The parameter {@code input} is null.
     */
    public static IByteReader ToByteReader(InputStream input) {
      if (input == null) {
        throw new NullPointerException("input");
      }
      return new WrappedStream(input);
    }

    /**
     * Wraps a byte array into a byte reader. The reader will start at the
     * beginning of the byte array. <p>In the .NET implementation, this
     * method is implemented as an extension method to any byte array object
     * and can be called as follows: <code>bytes.ToTransform()</code>. If the
     * object's class already has a ToTransform method with the same
     * parameters, that method takes precedence over this extension
     * method.</p>
     * @param bytes The byte array to wrap.
     * @return A byte reader wrapping the byte array.
     * @throws NullPointerException The parameter {@code bytes} is null.
     * @deprecated Renamed to ToByteReader.
 */
@Deprecated
    public static IByteReader ToTransform(byte[] bytes) {
      if (bytes == null) {
        throw new NullPointerException("bytes");
      }
      return new ByteArrayTransform(bytes, 0, bytes.length);
    }

    /**
     * Wraps a portion of a byte array into a byte reader object. <p>In the .NET
     * implementation, this method is implemented as an extension method to
     * any byte array object and can be called as follows:
     * <code>bytes.ToTransform(offset, length)</code>. If the object's class
     * already has a ToTransform method with the same parameters, that
     * method takes precedence over this extension method.</p>
     * @param bytes The byte array to wrap.
     * @param offset A zero-based index showing where the desired portion of
     * "bytes" begins.
     * @param length The length, in bytes, of the desired portion of "bytes" (but
     * not more than "bytes" 's length).
     * @return A byte reader wrapping the byte array.
     * @throws NullPointerException The parameter {@code bytes} is null.
     * @throws IllegalArgumentException Either {@code offset} or {@code length} is less
     * than 0 or greater than {@code bytes} 's length, or {@code bytes} 's
     * length minus {@code offset} is less than {@code length}.
     * @deprecated Renamed to ToByteReader.
 */
@Deprecated
    public static IByteReader ToTransform(
byte[] bytes,
int offset,
int length) {
      if (bytes == null) {
        throw new NullPointerException("bytes");
      }
      if (offset < 0) {
        throw new IllegalArgumentException("offset (" + offset +
          ") is less than " + 0);
      }
      if (offset > bytes.length) {
        throw new IllegalArgumentException("offset (" + offset + ") is more than " +
          bytes.length);
      }
      if (length < 0) {
        throw new IllegalArgumentException("length (" + length +
          ") is less than " + 0);
      }
      if (length > bytes.length) {
        throw new IllegalArgumentException("length (" + length + ") is more than " +
          bytes.length);
      }
      if (bytes.length - offset < length) {
        throw new IllegalArgumentException("bytes's length minus " + offset + " (" +
          (bytes.length - offset) + ") is less than " + length);
      }
      return new ByteArrayTransform(bytes, offset, length);
    }

    /**
     * Wraps an input stream into a reader object. If an IOException is thrown by
     * the input stream, the reader object throws IllegalStateException
     * instead. <p>In the .NET implementation, this method is implemented as
     * an extension method to any object implementing InputStream and can be
     * called as follows: <code>input.ToTransform()</code>. If the object's class
     * already has a ToTransform method with the same parameters, that
     * method takes precedence over this extension method.</p>
     * @param input The input stream to wrap.
     * @return A byte reader wrapping the input stream.
     * @throws NullPointerException The parameter {@code input} is null.
     * @deprecated Renamed to ToByteReader.
 */
@Deprecated
    public static IByteReader ToTransform(InputStream input) {
      if (input == null) {
        throw new NullPointerException("input");
      }
      return new WrappedStream(input);
    }

    /**
     * Wraps an output stream into a writer object. If an IOException is thrown by
     * the input stream, the writer object throws IllegalStateException
     * instead. <p>In the .NET implementation, this method is implemented as
     * an extension method to any object implementing InputStream and can be
     * called as follows: <code>output.ToWriter()</code>. If the object's class
     * already has a ToWriter method with the same parameters, that method
     * takes precedence over this extension method.</p>
     * @param output Output stream to wrap.
     * @return A byte writer that wraps the given output stream.
     * @throws NullPointerException The parameter {@code output} is null.
     */
    public static IWriter ToWriter(OutputStream output) {
      if (output == null) {
        throw new NullPointerException("output");
      }
      return new WrappedOutputStream(output);
    }

    /**
     * Wraps a byte writer (one that only implements a ReadByte method) to a writer
     * (one that also implements a three-parameter Read method.) <p>In the
     * .NET implementation, this method is implemented as an extension
     * method to any object implementing IByteWriter and can be called as
     * follows: <code>output.ToWriter()</code>. If the object's class already has
     * a ToWriter method with the same parameters, that method takes
     * precedence over this extension method.</p>
     * @param output A byte stream.
     * @return A writer that wraps the given stream.
     * @throws NullPointerException The parameter {@code output} is null.
     */
    public static IWriter ToWriter(IByteWriter output) {
      if (output == null) {
        throw new NullPointerException("output");
      }
      return new WrappedOutputStreamFromByteWriter(output);
    }

    private static final class ByteArrayTransform implements IByteReader {
      private final byte[] bytes;
      private int offset;
      private final int endOffset;

      public ByteArrayTransform (byte[] bytes, int offset, int length) {
        this.bytes = bytes;
        this.offset = offset;
        this.endOffset = offset + length;
      }

    /**
     * This is an internal method.
     * @return A 32-bit signed integer.
     */
      public int read() {
        if (this.offset >= this.endOffset) {
          return -1;
        }
        int b = this.bytes[this.offset];
        ++this.offset;
        return ((int)b) & 0xff;
      }
    }

    private static final class WrappedOutputStream implements IWriter {
      private final OutputStream output;

      public WrappedOutputStream (OutputStream output) {
        this.output = output;
      }

    /**
     * This is an internal method.
     * @param byteValue A 32-bit signed integer.
     */
      public void write(int byteValue) {
        try {
          this.output.write((byte)byteValue);
        } catch (IOException ex) {
          throw new IllegalStateException(ex.getMessage(), ex);
        }
      }

    /**
     * This is an internal method.
     * @param bytes A byte array.
     * @param offset A zero-based index showing where the desired portion of
     * "bytes" begins.
     * @param length The length, in bytes, of the desired portion of "bytes" (but
     * not more than "bytes" 's length).
     * @throws IllegalArgumentException Either {@code offset} or {@code length} is less
     * than 0 or greater than {@code bytes} 's length, or {@code bytes} 's
     * length minus {@code offset} is less than {@code length}.
     * @throws NullPointerException The parameter {@code bytes} is null.
     */
      public void write(byte[] bytes, int offset, int length) {
        try {
          this.output.write(bytes, offset, length);
        } catch (IOException ex) {
          throw new IllegalStateException(ex.getMessage(), ex);
        }
      }
    }

    private static final class WrappedOutputStreamFromByteWriter implements IWriter {
      private final IByteWriter output;

      public WrappedOutputStreamFromByteWriter (IByteWriter output) {
        this.output = output;
      }

    /**
     * This is an internal method.
     * @param byteValue A 32-bit signed integer.
     */
      public void write(int byteValue) {
        this.output.write((byte)byteValue);
      }

    /**
     * This is an internal method.
     * @param bytes A byte array.
     * @param offset A zero-based index showing where the desired portion of
     * "bytes" begins.
     * @param length The length, in bytes, of the desired portion of "bytes" (but
     * not more than "bytes" 's length).
     * @throws NullPointerException The parameter {@code bytes} is null.
     * @throws IllegalArgumentException Either {@code offset} or {@code length} is less
     * than 0 or greater than {@code bytes} 's length, or {@code bytes} 's
     * length minus {@code offset} is less than {@code length}.
     */
      public void write(byte[] bytes, int offset, int length) {
        if (bytes == null) {
          throw new NullPointerException("bytes");
        }
        if (offset < 0) {
          throw new IllegalArgumentException("offset (" + offset +
            ") is less than " + 0);
        }
        if (offset > bytes.length) {
          throw new IllegalArgumentException("offset (" + offset + ") is more than " +
            bytes.length);
        }
        if (length < 0) {
          throw new IllegalArgumentException("length (" + length +
            ") is less than " + 0);
        }
        if (length > bytes.length) {
          throw new IllegalArgumentException("length (" + length + ") is more than " +
            bytes.length);
        }
        if (bytes.length - offset < length) {
          throw new IllegalArgumentException("bytes's length minus " + offset + " (" +
            (bytes.length - offset) + ") is less than " + length);
        }
        for (int i = 0; i < length; ++i) {
          this.output.write((byte)bytes[i]);
        }
      }
    }

    private static final class WrappedStream implements IByteReader {
      private final InputStream stream;

      public WrappedStream (InputStream stream) {
        this.stream = stream;
      }

    /**
     * This is an internal method.
     * @return A 32-bit signed integer.
     */
      public int read() {
        try {
          return this.stream.read();
        } catch (IOException ex) {
          throw new IllegalStateException(ex.getMessage(), ex);
        }
      }
    }
  }
