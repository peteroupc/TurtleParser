# com.upokecenter.text.ICharacterDecoder

    public interface ICharacterDecoder

Defines a method that can be implemented by classes that convert a stream of
 bytes to Unicode code points.

## Methods

* `int ReadChar(IByteReader input)`<br>
 Reads bytes from an input transform until a Unicode code points is decoded
 or until the end of the stream is reached.

## Method Details

### ReadChar
    int ReadChar(IByteReader input)
Reads bytes from an input transform until a Unicode code points is decoded
 or until the end of the stream is reached. <p>If this method returns
 -2, indicating an error, the caller of this method can take one of a
 variety of actions to handle the error. For example, it can output
 one or more replacement code points instead (such as the Replacement
 Character 0xfffd), or it can throw an exception. In some cases, where
 the error won't cause data loss or a security problem, the caller can
 also ignore the decoder error.</p>

**Parameters:**

* <code>input</code> - Source of bytes to decode into code points. The decoder can
 maintain internal state, including data on bytes already read, so
 this parameter should not change when using the same character
 decoder object. It's also possible for the decoder to read no bytes
 but still return a code point, depending on the encoding it suppports
 and its internal state.

**Returns:**

* The Unicode code point decoded, from 0-0xd7ff or from 0xe000 to
 0x10ffff. Returns -1 if the end of the source is reached or -2 if a
 decoder error occurs.

### ReadChar
    int ReadChar(IByteReader input)
Reads bytes from an input transform until a Unicode code points is decoded
 or until the end of the stream is reached. <p>If this method returns
 -2, indicating an error, the caller of this method can take one of a
 variety of actions to handle the error. For example, it can output
 one or more replacement code points instead (such as the Replacement
 Character 0xfffd), or it can throw an exception. In some cases, where
 the error won't cause data loss or a security problem, the caller can
 also ignore the decoder error.</p>

**Parameters:**

* <code>input</code> - Source of bytes to decode into code points. The decoder can
 maintain internal state, including data on bytes already read, so
 this parameter should not change when using the same character
 decoder object. It's also possible for the decoder to read no bytes
 but still return a code point, depending on the encoding it suppports
 and its internal state.

**Returns:**

* The Unicode code point decoded, from 0-0xd7ff or from 0xe000 to
 0x10ffff. Returns -1 if the end of the source is reached or -2 if a
 decoder error occurs.
