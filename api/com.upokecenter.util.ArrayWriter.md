# com.upokecenter.util.ArrayWriter

    public final class ArrayWriter extends Object implements IWriter

A growable array of bytes.

## Methods

* `ArrayWriter()`<br>
 Initializes a new instance of the ArrayWriter class.
* `ArrayWriter(int initialSize)`<br>
 Initializes a new instance of the ArrayWriter class.
* `void Clear()`<br>
 Offers a fast way to reset the length of the array writer's data to 0.
* `byte[] ToArray()`<br>
 Generates an array of all bytes written so far to it.
* `void write(byte[] src,
     int offset,
     int length)`<br>
 Writes a series of bytes to the array.
* `void write(int byteValue)`<br>
 Writes an 8-bit byte to the array.

## Constructors

* `ArrayWriter()`<br>
 Initializes a new instance of the ArrayWriter class.
* `ArrayWriter(int initialSize)`<br>
 Initializes a new instance of the ArrayWriter class.

## Method Details

### ArrayWriter
    public ArrayWriter()
Initializes a new instance of the ArrayWriter class.
### ArrayWriter
    public ArrayWriter(int initialSize)
Initializes a new instance of the ArrayWriter class.

**Parameters:**

* <code>initialSize</code> - A 32-bit signed integer.

### Clear
    public void Clear()
Offers a fast way to reset the length of the array writer's data to 0.
### ToArray
    public byte[] ToArray()
Generates an array of all bytes written so far to it.

**Returns:**

* A byte array.

### write
    public void write(int byteValue)
Writes an 8-bit byte to the array.

**Specified by:**

* <code>write</code>&nbsp;in interface&nbsp;<code>IByteWriter</code>

**Parameters:**

* <code>byteValue</code> - An integer containing the byte to write. Only the lower 8
 bits of this value will be used.

### write
    public void write(byte[] src, int offset, int length)
Writes a series of bytes to the array.

**Specified by:**

* <code>write</code>&nbsp;in interface&nbsp;<code>IWriter</code>

**Parameters:**

* <code>src</code> - Byte array containing the data to write.

* <code>offset</code> - A zero-based index showing where the desired portion of <code>src</code> begins.

* <code>length</code> - The number of elements in the desired portion of <code>src</code>
 (but not more than <code>src</code> 's length).

**Throws:**

* <code>NullPointerException</code> - The parameter <code>src</code> is null.

* <code>IllegalArgumentException</code> - Either <code>offset</code> or <code>length</code> is less
 than 0 or greater than <code>src</code> 's length, or <code>src</code> 's
 length minus <code>offset</code> is less than <code>length</code>.
