# com.upokecenter.text.Encodings

    public final class Encodings extends Object

Contains methods for converting text from one character encoding to another.
 This class also contains convenience methods for converting strings
 and other character inputs to sequences of bytes. <p>The Encoding
 Standard, which is a Candidate Recommendation as of early November
 2015, defines algorithms for the most common character encodings used
 on Web pages and recommends the UTF-8 encoding for new specifications
 and Web pages. Calling the <code>GetEncoding(name)</code> method returns
 one of the character encodings with the given name under the Encoding
 Standard.</p> <p>Now let's define some terms.</p> <p><b>Encoding
 Terms</b></p> <ul> <li>A <b>code point</b> is a number that
 identifies a single text character, such as a letter, digit, or
 symbol.</li> <li>A <b>character set</b> is a set of code points which
 are each assigned to a single text character. (This may also be
 called a <i>coded character set</i>.) As used here, character sets
 don't define the in-memory representation of those code points.</li>
 <li>A <b>character encoding</b> is a mapping from a sequence of code
 points, in one or more specific character sets, to a sequence of
 bytes and vice versa.</li> <li><b>ASCII</b> is a 128-code-point
 character set that includes the English letters and digits, common
 punctuation and symbols, and control characters. As used here, its
 code points match the code points within the Basic Latin range (0-127
 or U + 0000 to U + 007F) of the Unicode Standard.</li></ul> <p>There
 are several kinds of character encodings:</p> <ul> <li><b>Single-byte
 encodings</b> define a character set that assigns one code point to
 one byte. Thus, they can have a maximum of 256 code points. For
 example:</li> <li>(a) ISO 8859 encodings and
 <code>windows-1252</code>.</li> <li>(b) ASCII is usually used as a
 single-byte encoding where each code point fits in the lower 7 bits
 of an eight-bit byte. In the Encoding Standard, all single-byte
 encodings use the ASCII characters as the first 128 code points of
 their character sets.</li> <li><b>Multi-byte encodings</b> include
 code points from one or more character sets and assign some or all
 code points to several bytes. For example:</li> <li>(a) UTF-16 uses 2
 bytes for the most common Unicode code points and 4 bytes for
 supplementary code points.</li> <li>(b) <code>utf-8</code> uses 1 byte for
 ASCII and 2 to 4 bytes for the other Unicode code points.</li>
 <li>(c) Most legacy East Asian encodings, such as <code>shift_jis</code>,
 <code>gbk</code>, and <code>big5</code> use 1 byte for ASCII (or a slightly
 modified version) and, usually, 2 or more bytes for national standard
 character sets. In many of these encodings, notably <code>shift_jis</code>,
 characters whose code points use one byte traditionally take half the
 space of characters whose code points use two bytes.</li>
 <li><b>Escape-based encodings</b> are combinations of single- and/or
 multi-byte encodings, and use escape sequences and/or shift codes to
 change which encoding to use for the bytes that follow. For
 example:</li> <li>(a) <code>iso-2022-jp</code> supports several escape
 sequences that shift into different encodings, including a Katakana,
 a Kanji, and an ASCII encoding (with ASCII as the default).</li>
 <li>(b) UTF-7 (not included in the Encoding Standard) is a Unicode
 encoding that uses a limited subset of ASCII. The plus symbol is used
 to shift into a modified version of base-64 to encode other Unicode
 code points.</li> <li>The Encoding Standard also defines a
 <b>replacement encoding</b>, which causes a decoding error and is
 used to alias a few problematic or unsupported encoding names, such
 as <code>hz-gb-2312</code>.</li></ul> <p><b>Getting an Encoding</b></p>
 <p>The Encoding Standard includes UTF-8, UTF-16, and many legacy
 encodings, and gives each one of them a name. The
 <code>GetEncoding(name)</code> method takes a name string and returns an
 ICharacterEncoding object that implements that encoding, or
 <code>null</code> if the name is unrecognized.</p> <p>However, the Encoding
 Standard is designed to include only encodings commonly used on Web
 pages, not in other protocols such as email. For email, the Encoding
 class includes an alternate function <code>GetEncoding(name,
 forEmail)</code>. Setting <code>forEmail</code> to <code>true</code> will use rules
 modified from the Encoding Standard to better suit encoding and
 decoding text from email messages.</p> <p><b>Classes for Character
 Encodings</b></p> <p>This Encodings class provides access to common
 character encodings through classes as described below:</p> <ul>
 <li>An <b>encoder class</b> is a class that converts a sequence of
 bytes to a sequence of code points in the universal character set
 (otherwise known under the name Unicode). An encoder class implements
 the <code>ICharacterEncoder</code> interface.</li> <li>A <b>decoder
 class</b> is a class that converts a sequence of Unicode code points
 to a sequence of bytes. A decoder class implements the
 <code>ICharacterDecoder</code> interface.</li> <li>An <b>encoding class</b>
 allows access to both an encoder class and a decoder class and
 implements the <code>ICharacterEncoding</code> interface. The encoder and
 decoder classes should implement the same character
 encoding.</li></ul> <p><b>Custom Encodings</b></p> <p>Classes that
 implement the ICharacterEncoding interface can provide additional
 character encodings not included in the Encoding Standard. Some
 examples of these include the following:</p> <ul> <li>A modified
 version of UTF-8 used in Java's serialization formats.</li> <li>A
 modified version of UTF-7 used in the IMAP email protocol.</li></ul>
 <p>(Note that this library doesn't implement either encoding.)</p>

## Fields

* `static ICharacterEncoding UTF8`<br>
 Character encoding object for the UTF-8 character encoding, which represents
 each code point in the universal character set using 1 to 4 bytes.

## Methods

* `static String DecodeToString(ICharacterEncoding enc,
              byte[] bytes)`<br>
 Reads a byte array from a data source and converts the bytes from a given
 encoding to a text string.
* `static String DecodeToString(ICharacterEncoding enc,
              byte[] bytes,
              int offset,
              int length)`<br>
 Reads a portion of a byte array from a data source and converts the bytes
 from a given encoding to a text string.
* `static String DecodeToString(ICharacterEncoding encoding,
              IByteReader transform)`<br>
 Reads bytes from a data source and converts the bytes from a given encoding
 to a text string.
* `static byte[] EncodeToBytes(ICharacterInput input,
             ICharacterEncoder encoder)`<br>
 Reads Unicode characters from a character input and writes them to a byte
 array encoded using a given character encoding.
* `static byte[] EncodeToBytes(ICharacterInput input,
             ICharacterEncoding encoding)`<br>
 Reads Unicode characters from a character input and writes them to a byte
 array encoded using the given character encoder.
* `static byte[] EncodeToBytes(String str,
             ICharacterEncoding enc)`<br>
 Reads Unicode characters from a text string and writes them to a byte array
 encoded in a given character encoding.
* `static void EncodeToWriter(ICharacterInput input,
              ICharacterEncoder encoder,
              IWriter writer)`<br>
 Reads Unicode characters from a character input and writes them to a byte
 array encoded in a given character encoding.
* `static void EncodeToWriter(ICharacterInput input,
              ICharacterEncoding encoding,
              IWriter writer)`<br>
 Reads Unicode characters from a character input and writes them to a byte
 array encoded using the given character encoder.
* `static void EncodeToWriter(String str,
              ICharacterEncoding enc,
              IWriter writer)`<br>
 Converts a text string to bytes and writes the bytes to an output byte
 writer.
* `static ICharacterInput GetDecoderInput(ICharacterEncoding encoding,
               IByteReader stream)`<br>
 Converts a character encoding into a character input stream, given a
 streamable source of bytes.
* `static ICharacterInput GetDecoderInputSkipBom(ICharacterEncoding encoding,
                      IByteReader stream)`<br>
 Converts a character encoding into a character input stream, given a
 streamable source of bytes.
* `static ICharacterEncoding GetEncoding(String name)`<br>
 Returns a character encoding from the given name.
* `static ICharacterEncoding GetEncoding(String name,
           boolean forEmail)`<br>
 Returns a character encoding from the given name.
* `static ICharacterEncoding GetEncoding(String name,
           boolean forEmail,
           boolean allowReplacement)`<br>
 Returns a character encoding from the given name.
* `static String InputToString(ICharacterInput reader)`<br>
 Reads Unicode characters from a character input and converts them to a text
 string.
* `static String ResolveAlias(String name)`<br>
 Resolves a character encoding's name to a standard form.
* `static String ResolveAliasForEmail(String name)`<br>
 Resolves a character encoding's name to a canonical form, using rules more
 suitable for email.
* `static byte[] StringToBytes(ICharacterEncoder encoder,
             String str)`<br>
 Converts a text string to a byte array using the given character encoder.
* `static byte[] StringToBytes(ICharacterEncoding encoding,
             String str)`<br>
 Converts a text string to a byte array encoded in a given character
 encoding.
* `static ICharacterInput StringToInput(String str)`<br>
 Converts a text string to a character input.
* `static ICharacterInput StringToInput(String str,
             int offset,
             int length)`<br>
 Converts a portion of a text string to a character input.

## Field Details

### UTF8
    public static final ICharacterEncoding UTF8
Character encoding object for the UTF-8 character encoding, which represents
 each code point in the universal character set using 1 to 4 bytes.
## Method Details

### DecodeToString
    public static String DecodeToString(ICharacterEncoding encoding, IByteReader transform)
Reads bytes from a data source and converts the bytes from a given encoding
 to a text string. <p>In the .NET implementation, this method is
 implemented as an extension method to any object implementing
 ICharacterEncoding and can be called as follows:
 "encoding.DecodeString(transform)". If the object's class already has
 a DecodeToString method with the same parameters, that method takes
 precedence over this extension method.</p>

**Parameters:**

* <code>encoding</code> - An object that implements a given character encoding. Any
 bytes that can&#x27;t be decoded are converted to the replacement
 character (U + FFFD).

* <code>transform</code> - An object that implements a byte stream.

**Returns:**

* The converted string.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>encoding</code> or <code>transform</code> is null.

### DecodeToString
    public static String DecodeToString(ICharacterEncoding enc, byte[] bytes)
Reads a byte array from a data source and converts the bytes from a given
 encoding to a text string. Errors in decoding are handled by
 replacing erroneous bytes with the replacement character (U + FFFD).
 <p>In the .NET implementation, this method is implemented as an
 extension method to any object implementing ICharacterEncoding and
 can be called as follows: <code>enc.DecodeToString(bytes)</code>. If the
 object's class already has a DecodeToString method with the same
 parameters, that method takes precedence over this extension
 method.</p>

**Parameters:**

* <code>enc</code> - An ICharacterEncoding object.

* <code>bytes</code> - A byte array.

**Returns:**

* A string consisting of the decoded text.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>enc</code> or <code>bytes</code> is
 null.

### DecodeToString
    public static String DecodeToString(ICharacterEncoding enc, byte[] bytes, int offset, int length)
Reads a portion of a byte array from a data source and converts the bytes
 from a given encoding to a text string. Errors in decoding are
 handled by replacing erroneous bytes with the replacement character
 (U + FFFD). <p>In the .NET implementation, this method is implemented
 as an extension method to any object implementing ICharacterEncoding
 and can be called as follows: <code>enc.DecodeToString(bytes, offset,
 length)</code>. If the object's class already has a DecodeToString
 method with the same parameters, that method takes precedence over
 this extension method.</p>

**Parameters:**

* <code>enc</code> - An object implementing a character encoding (gives access to an
 encoder and a decoder).

* <code>bytes</code> - A byte array containing the desired portion to read.

* <code>offset</code> - A zero-based index showing where the desired portion of <code>bytes</code> begins.

* <code>length</code> - The length, in bytes, of the desired portion of <code>bytes</code>
 (but not more than <code>bytes</code> 's length).

**Returns:**

* A string consisting of the decoded text.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>enc</code> or <code>bytes</code> is
 null.

* <code>IllegalArgumentException</code> - Either <code>offset</code> or <code>length</code> is less
 than 0 or greater than <code>bytes</code> 's length, or <code>bytes</code> 's
 length minus <code>offset</code> is less than <code>length</code>.

### EncodeToBytes
    public static byte[] EncodeToBytes(ICharacterInput input, ICharacterEncoding encoding)
Reads Unicode characters from a character input and writes them to a byte
 array encoded using the given character encoder. When writing to the
 byte array, any characters that can't be encoded are replaced with
 the byte 0x3f (the question mark character). <p>In the .NET
 implementation, this method is implemented as an extension method to
 any object implementing ICharacterInput and can be called as follows:
 <code>input.EncodeToBytes(encoding)</code>. If the object's class already
 has an EncodeToBytes method with the same parameters, that method
 takes precedence over this extension method.</p>

**Parameters:**

* <code>input</code> - An object that implements a stream of universal code points.

* <code>encoding</code> - An object that implements a given character encoding.

**Returns:**

* A byte array containing the encoded text.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>encoding</code> is null.

### EncodeToBytes
    public static byte[] EncodeToBytes(ICharacterInput input, ICharacterEncoder encoder)
Reads Unicode characters from a character input and writes them to a byte
 array encoded using a given character encoding. When writing to the
 byte array, any characters that can't be encoded are replaced with
 the byte 0x3f (the question mark character). <p>In the .NET
 implementation, this method is implemented as an extension method to
 any object implementing ICharacterInput and can be called as follows:
 <code>input.EncodeToBytes(encoder)</code>. If the object's class already
 has a EncodeToBytes method with the same parameters, that method
 takes precedence over this extension method.</p>

**Parameters:**

* <code>input</code> - An object that implements a stream of universal code points.

* <code>encoder</code> - An object that implements a character encoder.

**Returns:**

* A byte array.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>encoder</code> or <code>input</code>
 is null.

### EncodeToBytes
    public static byte[] EncodeToBytes(String str, ICharacterEncoding enc)
Reads Unicode characters from a text string and writes them to a byte array
 encoded in a given character encoding. When reading the string, any
 unpaired surrogate characters are replaced with the replacement
 character (U + FFFD), and when writing to the byte array, any
 characters that can't be encoded are replaced with the byte 0x3f (the
 question mark character). <p>In the .NET implementation, this method
 is implemented as an extension method to any string object and can be
 called as follows: <code>str.EncodeToBytes(enc)</code>. If the object's
 class already has a EncodeToBytes method with the same parameters,
 that method takes precedence over this extension method.</p>

**Parameters:**

* <code>str</code> - A string object.

* <code>enc</code> - An object implementing a character encoding (gives access to an
 encoder and a decoder).

**Returns:**

* A byte array.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>str</code> or <code>enc</code> is
 null.

### EncodeToWriter
    public static void EncodeToWriter(ICharacterInput input, ICharacterEncoding encoding, IWriter writer)
Reads Unicode characters from a character input and writes them to a byte
 array encoded using the given character encoder. When writing to the
 byte array, any characters that can't be encoded are replaced with
 the byte 0x3f (the question mark character). <p>In the .NET
 implementation, this method is implemented as an extension method to
 any object implementing ICharacterInput and can be called as follows:
 <code>input.EncodeToBytes(encoding)</code>. If the object's class already
 has a EncodeToBytes method with the same parameters, that method
 takes precedence over this extension method.</p>

**Parameters:**

* <code>input</code> - An object that implements a stream of universal code points.

* <code>encoding</code> - An object that implements a character encoding.

* <code>writer</code> - A byte writer to write the encoded bytes to.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>encoding</code> is null.

### EncodeToWriter
    public static void EncodeToWriter(ICharacterInput input, ICharacterEncoder encoder, IWriter writer)
Reads Unicode characters from a character input and writes them to a byte
 array encoded in a given character encoding. When writing to the byte
 array, any characters that can't be encoded are replaced with the
 byte 0x3f (the question mark character). <p>In the .NET
 implementation, this method is implemented as an extension method to
 any object implementing ICharacterInput and can be called as follows:
 <code>input.EncodeToBytes(encoder)</code>. If the object's class already
 has a EncodeToBytes method with the same parameters, that method
 takes precedence over this extension method.</p>

**Parameters:**

* <code>input</code> - An object that implements a stream of universal code points.

* <code>encoder</code> - An object that implements a character encoder.

* <code>writer</code> - A byte writer to write the encoded bytes to.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>encoder</code> or <code>input</code>
 is null.

### EncodeToWriter
    public static void EncodeToWriter(String str, ICharacterEncoding enc, IWriter writer)
Converts a text string to bytes and writes the bytes to an output byte
 writer. When reading the string, any unpaired surrogate characters
 are replaced with the replacement character (U + FFFD), and when
 writing to the byte stream, any characters that can't be encoded are
 replaced with the byte 0x3f (the question mark character). <p>In the
 .NET implementation, this method is implemented as an extension
 method to any string object and can be called as follows:
 <code>str.EncodeToBytes(enc, writer)</code>. If the object's class already
 has a EncodeToBytes method with the same parameters, that method
 takes precedence over this extension method.</p>

**Parameters:**

* <code>str</code> - A string object to encode.

* <code>enc</code> - An object implementing a character encoding (gives access to an
 encoder and a decoder).

* <code>writer</code> - A byte writer where the encoded bytes will be written to.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>str</code> or <code>enc</code> is
 null.

### GetDecoderInput
    public static ICharacterInput GetDecoderInput(ICharacterEncoding encoding, IByteReader stream)
Converts a character encoding into a character input stream, given a
 streamable source of bytes. The input stream doesn't check the first
 few bytes for a byte-order mark indicating a Unicode encoding such as
 UTF-8 before using the character encoding's decoder. <p>In the .NET
 implementation, this method is implemented as an extension method to
 any object implementing ICharacterEncoding and can be called as
 follows: "encoding.GetDecoderInput(transform)". If the object's class
 already has a GetDecoderInput method with the same parameters, that
 method takes precedence over this extension method.</p>

**Parameters:**

* <code>encoding</code> - Encoding that exposes a decoder to be converted into a
 character input stream. If the decoder returns -2 (indicating a
 decode error), the character input stream handles the error by
 returning a replacement character in its place.

* <code>stream</code> - Byte stream to convert into Unicode characters.

**Returns:**

* An ICharacterInput object.

### GetDecoderInputSkipBom
    public static ICharacterInput GetDecoderInputSkipBom(ICharacterEncoding encoding, IByteReader stream)
Converts a character encoding into a character input stream, given a
 streamable source of bytes. But if the input stream starts with a
 UTF-8 or UTF-16 byte order mark, the input is decoded as UTF-8 or
 UTF-16, as the case may be, rather than the given character encoding.
 <p>This method implements the "decode" algorithm specified in the
 Encoding standard.</p> <p>In the .NET implementation, this method is
 implemented as an extension method to any object implementing
 ICharacterEncoding and can be called as follows:
 "encoding.GetDecoderInput(transform)". If the object's class already
 has a GetDecoderInput method with the same parameters, that method
 takes precedence over this extension method.</p>

**Parameters:**

* <code>encoding</code> - Encoding object that exposes a decoder to be converted into
 a character input stream. If the decoder returns -2 (indicating a
 decode error), the character input stream handles the error by
 returning a replacement character in its place.

* <code>stream</code> - Byte stream to convert into Unicode characters.

**Returns:**

* An ICharacterInput object.

### GetEncoding
    public static ICharacterEncoding GetEncoding(String name)
Returns a character encoding from the given name.

**Parameters:**

* <code>name</code> - A string naming a character encoding. See the ResolveAlias
 method. Can be null.

**Returns:**

* An ICharacterEncoding object.

### GetEncoding
    public static ICharacterEncoding GetEncoding(String name, boolean forEmail)
Returns a character encoding from the given name.

**Parameters:**

* <code>name</code> - A string naming a character encoding. See the ResolveAlias
 method. Can be null.

* <code>forEmail</code> - A Boolean object.

**Returns:**

* An ICharacterEncoding object.

### GetEncoding
    public static ICharacterEncoding GetEncoding(String name, boolean forEmail, boolean allowReplacement)
Returns a character encoding from the given name.

**Parameters:**

* <code>name</code> - A string naming a character encoding. See the ResolveAlias
 method. Can be null.

* <code>forEmail</code> - If false, uses the encoding resolution rules in the Encoding
 Standard. If true, uses modified rules as described in the
 ResolveAliasForEmail method.

* <code>allowReplacement</code> - If true, allows the label <code>replacement</code> to
 return the replacement encoding.

**Returns:**

* An object that enables encoding and decoding text in the given
 character encoding. Returns null if the name is null or empty, or if
 it names an unrecognized or unsupported encoding.

### InputToString
    public static String InputToString(ICharacterInput reader)
Reads Unicode characters from a character input and converts them to a text
 string. <p>In the .NET implementation, this method is implemented as
 an extension method to any object implementing ICharacterInput and
 can be called as follows: <code>reader.InputToString()</code>. If the
 object's class already has a InputToString method with the same
 parameters, that method takes precedence over this extension
 method.</p>

**Parameters:**

* <code>reader</code> - A character input whose characters will be converted to a text
 string.

**Returns:**

* A text string containing the characters read.

### ResolveAlias
    public static String ResolveAlias(String name)
Resolves a character encoding's name to a standard form. This involves
 changing aliases of a character encoding to a standardized name.
 <p>In several Internet standards, this name is known as a "charset"
 parameter. In HTML and HTTP, for example, the "charset" parameter
 indicates the encoding used to represent text in the HTML page, text
 file, etc.</p>

**Parameters:**

* <code>name</code> - A string that names a given character encoding. Can be null. Any
 leading and trailing whitespace is removed and the name converted to
 lowercase before resolving the encoding&#x27;s name. The Encoding
 Standard supports only the following encodings (and defines aliases
 for most of them):. <ul> <li> <code>utf-8</code> - UTF-8 (8-bit universal
 character set, the encoding recommended by the Encoding Standard for
 new data formats)</li> <li> <code>utf-16le</code> - UTF-16 little-endian
 (16-bit UCS)</li> <li> <code>utf-16be</code> - UTF-16 big-endian (16-bit
 UCS)</li> <li>The special-purpose encoding <code>x-user-defined</code></li> <li>The special-purpose encoding <code>replacement</code>, which this function returns only if one of several
 aliases are passed to it, as defined in the Encoding Standard.</li>
 <li>28 legacy single-byte encodings: <ul> <li> <code>windows-1252</code> :
 Western Europe (Note: The Encoding Standard aliases the names <code>us-ascii</code> and <code>iso-8859-1</code> to <code>windows-1252</code>, which
 specifies a different character set from either; it differs from
 <code>iso-8859-1</code> by assigning different characters to some bytes
 from 0x80 to 0x9F. The Encoding Standard does this for compatibility
 with existing Web pages.)</li> <li> <code>iso-8859-2</code>, <code>windows-1250</code> : Central Europe</li> <li> <code>iso-8859-10</code> :
 Northern Europe</li> <li> <code>iso-8859-4</code>, <code>windows-1257</code> :
 Baltic</li> <li> <code>iso-8859-13</code> : Estonian</li> <li> <code>iso-8859-14</code> : Celtic</li> <li> <code>iso-8859-16</code> : Romanian</li>
 <li> <code>iso-8859-5</code>, <code>ibm866</code>, <code>koi8-r</code>, <code>windows-1251</code>, <code>x-mac-cyrillic</code> : Cyrillic</li> <li> <code>koi8-u</code> : Ukrainian</li> <li> <code>iso-8859-7</code>, <code>windows-1253</code>
 : Greek</li> <li> <code>iso-8859-6</code>, <code>windows-1256</code> :
 Arabic</li> <li> <code>iso-8859-8</code>, <code>iso-8859-8-i</code>, <code>windows-1255</code> : Hebrew</li> <li> <code>iso-8859-3</code> : Latin 3</li>
 <li> <code>iso-8859-15</code> : Latin 9</li> <li> <code>windows-1254</code> :
 Turkish</li> <li> <code>windows-874</code> : Thai</li> <li> <code>windows-1258</code> : Vietnamese</li> <li> <code>macintosh</code> : Mac
 Roman</li></ul></li> <li>Three legacy Japanese encodings: <code>shift_jis</code>, <code>euc-jp</code>, <code>iso-2022-jp</code></li> <li>Two legacy
 simplified Chinese encodings: <code>gbk</code> and <code>gb18030</code></li>
 <li> <code>big5</code> : legacy traditional Chinese encoding</li>
 <li> <code>euc-kr</code> : legacy Korean encoding</li></ul> <p>The <code>utf-8</code>, <code>utf-16le</code>, and <code>utf-16be</code> encodings don't encode
 a byte-order mark at the start of the text (doing so is not
 recommended for <code>utf-8</code>, while in <code>utf-16le</code> and <code>utf-16be</code>, the byte-order mark character U + FEFF is treated as an
 ordinary character, unlike in to the UTF-16 encoding form). The
 Encoding Standard aliases <code>utf-16</code> to <code>utf-16le</code> "to deal
 with deployed content".</p> .

**Returns:**

* A standardized name for the encoding. Returns the empty string if
 <code>name</code> is null or empty, or if the encoding name is
 unsupported.

### ResolveAliasForEmail
    public static String ResolveAliasForEmail(String name)
Resolves a character encoding's name to a canonical form, using rules more
 suitable for email.

**Parameters:**

* <code>name</code> - A string naming a character encoding. Can be null. Uses a
 modified version of the rules in the Encoding Standard to better
 conform, in some cases, to email standards like MIME. In addition to
 the encodings mentioned in ResolveAlias, the following additional
 encodings are supported:. <ul> <li> <code>us-ascii</code> - ASCII
 single-byte encoding, rather than an alias to <code>windows-1252</code>,
 as specified in the Encoding Standard.</li> <li> <code>iso-8859-1</code> -
 Latin-1 single-byte encoding, rather than an alias to <code>windows-1252</code>, as specified in the Encoding Standard.</li> <li> <code>utf-7</code> - UTF-7 (7-bit universal character set).</li></ul>.

**Returns:**

* A standardized name for the encoding. Returns the empty string if
 <code>name</code> is null or empty, or if the encoding name is
 unsupported.

### StringToBytes
    public static byte[] StringToBytes(ICharacterEncoding encoding, String str)
Converts a text string to a byte array encoded in a given character
 encoding. When reading the string, any unpaired surrogate characters
 are replaced with the replacement character (U + FFFD), and when
 writing to the byte array, any characters that can't be encoded are
 replaced with the byte 0x3f (the question mark character). <p>In the
 .NET implementation, this method is implemented as an extension
 method to any object implementing ICharacterEncoding and can be
 called as follows: <code>encoding.StringToBytes(str)</code>. If the
 object's class already has a StringToBytes method with the same
 parameters, that method takes precedence over this extension
 method.</p>

**Parameters:**

* <code>encoding</code> - An object that implements a character encoding.

* <code>str</code> - A string to be encoded into a byte array.

**Returns:**

* A byte array containing the string encoded in the given text
 encoding.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>encoding</code> is null.

### StringToBytes
    public static byte[] StringToBytes(ICharacterEncoder encoder, String str)
Converts a text string to a byte array using the given character encoder.
 When reading the string, any unpaired surrogate characters are
 replaced with the replacement character (U + FFFD), and when writing
 to the byte array, any characters that can't be encoded are replaced
 with the byte 0x3f (the question mark character). <p>In the .NET
 implementation, this method is implemented as an extension method to
 any object implementing ICharacterEncoder and can be called as
 follows: <code>encoder.StringToBytes(str)</code>. If the object's class
 already has a StringToBytes method with the same parameters, that
 method takes precedence over this extension method.</p>

**Parameters:**

* <code>encoder</code> - An object that implements a character encoder.

* <code>str</code> - A text string to encode into a byte array.

**Returns:**

* A byte array.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>encoder</code> or <code>str</code>
 is null.

### StringToInput
    public static ICharacterInput StringToInput(String str)
Converts a text string to a character input. The resulting input can then be
 used to encode the text to bytes, or to read the string code point by
 code point, among other things. When reading the string, any unpaired
 surrogate characters are replaced with the replacement character (U +
 FFFD). <p>In the .NET implementation, this method is implemented as
 an extension method to any string object and can be called as
 follows: <code>str.StringToInput(offset, length)</code>. If the object's
 class already has a StringToInput method with the same parameters,
 that method takes precedence over this extension method.</p>

**Parameters:**

* <code>str</code> - A string object.

**Returns:**

* An ICharacterInput object.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>str</code> is null.

### StringToInput
    public static ICharacterInput StringToInput(String str, int offset, int length)
Converts a portion of a text string to a character input. The resulting
 input can then be used to encode the text to bytes, or to read the
 string code point by code point, among other things. When reading the
 string, any unpaired surrogate characters are replaced with the
 replacement character (U + FFFD). <p>In the .NET implementation, this
 method is implemented as an extension method to any string object and
 can be called as follows: <code>str.StringToInput(offset, length)</code>.
 If the object's class already has a StringToInput method with the
 same parameters, that method takes precedence over this extension
 method.</p>

**Parameters:**

* <code>str</code> - A string object.

* <code>offset</code> - A zero-based index showing where the desired portion of <code>str</code> begins.

* <code>length</code> - The length, in code units, of the desired portion of <code>str</code> (but not more than <code>str</code> 's length).

**Returns:**

* An ICharacterInput object.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>str</code> is null.

* <code>IllegalArgumentException</code> - Either <code>offset</code> or <code>length</code> is less
 than 0 or greater than <code>str</code> 's length, or <code>str</code> 's
 length minus <code>offset</code> is less than <code>length</code>.
