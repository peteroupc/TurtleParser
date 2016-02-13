# com.upokecenter.util.DataIO

    public final class DataIO extends Object

Convenience class that contains static methods for wrapping byte arrays and
 streams into byte readers and byte writers.

## Methods

* `static IByteReader ToByteReader(byte[] bytes)`<br>
 Wraps a byte array into a byte reader.
* `static IByteReader ToByteReader(byte[] bytes,
            int offset,
            int length)`<br>
 Wraps a portion of a byte array into a byte reader object.
* `static IByteReader ToByteReader(InputStream input)`<br>
 Wraps an input stream into a reader object.
* `static IByteReader ToTransform(byte[] bytes)`<br>
 Deprecated.
Renamed to ToByteReader.
 Renamed to ToByteReader.
* `static IByteReader ToTransform(byte[] bytes,
           int offset,
           int length)`<br>
 Deprecated.
Renamed to ToByteReader.
 Renamed to ToByteReader.
* `static IByteReader ToTransform(InputStream input)`<br>
 Deprecated.
Renamed to ToByteReader.
 Renamed to ToByteReader.
* `static IWriter ToWriter(IByteWriter output)`<br>
 Wraps a byte writer (one that only implements a ReadByte method) to a writer
 (one that also implements a three-parameter Read method.)
* `static IWriter ToWriter(OutputStream output)`<br>
 Wraps an output stream into a writer object.

## Method Details

### ToByteReader
    public static IByteReader ToByteReader(byte[] bytes)
Wraps a byte array into a byte reader. The reader will start at the
 beginning of the byte array. <p>In the .NET implementation, this
 method is implemented as an extension method to any byte array object
 and can be called as follows: <code>bytes.ToByteReader()</code>. If the
 object's class already has a ToByteReader method with the same
 parameters, that method takes precedence over this extension
 method.</p>

**Parameters:**

* <code>bytes</code> - The byte array to wrap.

**Returns:**

* A byte reader wrapping the byte array.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>bytes</code> is null.

### ToByteReader
    public static IByteReader ToByteReader(byte[] bytes, int offset, int length)
Wraps a portion of a byte array into a byte reader object. <p>In the .NET
 implementation, this method is implemented as an extension method to
 any byte array object and can be called as follows:
 <code>bytes.ToByteReader(offset, length)</code>. If the object's class
 already has a ToByteReader method with the same parameters, that
 method takes precedence over this extension method.</p>

**Parameters:**

* <code>bytes</code> - The byte array to wrap.

* <code>offset</code> - A zero-based index showing where the desired portion of
 "bytes" begins.

* <code>length</code> - The length, in bytes, of the desired portion of "bytes" (but
 not more than "bytes" 's length).

**Returns:**

* A byte reader wrapping the byte array.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>bytes</code> is null.

* <code>IllegalArgumentException</code> - Either <code>offset</code> or <code>length</code> is less
 than 0 or greater than <code>bytes</code> 's length, or <code>bytes</code> 's
 length minus <code>offset</code> is less than <code>length</code>.

### ToByteReader
    public static IByteReader ToByteReader(InputStream input)
Wraps an input stream into a reader object. If an IOException is thrown by
 the input stream, the reader object throws IllegalStateException
 instead. <p>In the .NET implementation, this method is implemented as
 an extension method to any object implementing InputStream and can be
 called as follows: <code>input.ToByteReader()</code>. If the object's class
 already has a ToByteReader method with the same parameters, that
 method takes precedence over this extension method.</p>

**Parameters:**

* <code>input</code> - The input stream to wrap.

**Returns:**

* A byte reader wrapping the input stream.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>input</code> is null.

### ToTransform
    @Deprecated public static IByteReader ToTransform(byte[] bytes)
<span class='deprecatedLabel'>Deprecated.</span>&nbsp;<span class='deprecationComment'>Renamed to ToByteReader.</span>

**Parameters:**

* <code>bytes</code> - The byte array to wrap.

**Returns:**

* A byte reader wrapping the byte array.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>bytes</code> is null.

### ToTransform
    @Deprecated public static IByteReader ToTransform(byte[] bytes, int offset, int length)
<span class='deprecatedLabel'>Deprecated.</span>&nbsp;<span class='deprecationComment'>Renamed to ToByteReader.</span>

**Parameters:**

* <code>bytes</code> - The byte array to wrap.

* <code>offset</code> - A zero-based index showing where the desired portion of
 "bytes" begins.

* <code>length</code> - The length, in bytes, of the desired portion of "bytes" (but
 not more than "bytes" 's length).

**Returns:**

* A byte reader wrapping the byte array.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>bytes</code> is null.

* <code>IllegalArgumentException</code> - Either <code>offset</code> or <code>length</code> is less
 than 0 or greater than <code>bytes</code> 's length, or <code>bytes</code> 's
 length minus <code>offset</code> is less than <code>length</code>.

### ToTransform
    @Deprecated public static IByteReader ToTransform(InputStream input)
<span class='deprecatedLabel'>Deprecated.</span>&nbsp;<span class='deprecationComment'>Renamed to ToByteReader.</span>

**Parameters:**

* <code>input</code> - The input stream to wrap.

**Returns:**

* A byte reader wrapping the input stream.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>input</code> is null.

### ToWriter
    public static IWriter ToWriter(OutputStream output)
Wraps an output stream into a writer object. If an IOException is thrown by
 the input stream, the writer object throws IllegalStateException
 instead. <p>In the .NET implementation, this method is implemented as
 an extension method to any object implementing InputStream and can be
 called as follows: <code>output.ToWriter()</code>. If the object's class
 already has a ToWriter method with the same parameters, that method
 takes precedence over this extension method.</p>

**Parameters:**

* <code>output</code> - Output stream to wrap.

**Returns:**

* A byte writer that wraps the given output stream.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>output</code> is null.

### ToWriter
    public static IWriter ToWriter(IByteWriter output)
Wraps a byte writer (one that only implements a ReadByte method) to a writer
 (one that also implements a three-parameter Read method.) <p>In the
 .NET implementation, this method is implemented as an extension
 method to any object implementing IByteWriter and can be called as
 follows: <code>output.ToWriter()</code>. If the object's class already has
 a ToWriter method with the same parameters, that method takes
 precedence over this extension method.</p>

**Parameters:**

* <code>output</code> - A byte stream.

**Returns:**

* A writer that wraps the given stream.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>output</code> is null.
