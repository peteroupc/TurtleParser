# com.upokecenter.text.ICharacterInput

    public interface ICharacterInput

An interface for reading Unicode characters from a data source.

## Methods

* `int Read(int[] chars,
    int index,
    int length)`<br>
 Reads a sequence of Unicode code points from a data source.
* `int ReadChar()`<br>
 Reads a Unicode character from a data source.

## Method Details

### ReadChar
    int ReadChar()
Reads a Unicode character from a data source.

**Returns:**

* Either a Unicode code point (from 0-0xd7ff or from 0xe000 to
 0x10ffff), or the value -1 indicating the end of the source.

### Read
    int Read(int[] chars, int index, int length)
Reads a sequence of Unicode code points from a data source.

**Parameters:**

* <code>chars</code> - Output buffer.

* <code>index</code> - A zero-based index showing where the desired portion of <code>chars</code> begins.

* <code>length</code> - The number of elements in the desired portion of <code>chars</code>
 (but not more than <code>chars</code> 's length).

**Returns:**

* The number of Unicode code points read, or 0 if the end of the
 source is reached.

**Throws:**

* <code>NullPointerException</code> - Should be thrown if "chars" is null.
