package com.upokecenter.text;

    /**
     * An interface for reading Unicode characters from a data source.
     */
  public interface ICharacterInput {
    /**
     * Reads a Unicode character from a data source.
     * @return Either a Unicode code point (from 0-0xd7ff or from 0xe000 to
     * 0x10ffff), or the value -1 indicating the end of the source.
     */
    int ReadChar();

    /**
     * Reads a sequence of Unicode code points from a data source.
     * @param chars Output buffer.
     * @param index A zero-based index showing where the desired portion of {@code
     * chars} begins.
     * @param length The number of elements in the desired portion of {@code chars}
     * (but not more than {@code chars} 's length).
     * @return The number of Unicode code points read, or 0 if the end of the
     * source is reached.
     * @throws NullPointerException Should be thrown if "chars" is null.
     */
    int Read(int[] chars, int index, int length);
  }
