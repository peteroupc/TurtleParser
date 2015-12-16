package com.upokecenter.text;

import java.util.*;

import com.upokecenter.util.*;
import com.upokecenter.text.encoders.*;

    /**
     * Contains methods for converting text from one character encoding to another.
     * This class also contains convenience methods for converting strings
     * and other character inputs to sequences of bytes. <p>The Encoding
     * Standard, which is a Candidate Recommendation as of early November
     * 2015, defines algorithms for the most common character encodings used
     * on Web pages and recommends the UTF-8 encoding for new specifications
     * and Web pages. Calling the <code>GetEncoding(name)</code> method returns
     * one of the character encodings with the given name under the Encoding
     * Standard.</p> <p>Now let's define some terms.</p> <p><b>Encoding
     * Terms</b></p> <ul> <li>A <b>code point</b> is a number that
     * identifies a single text character, such as a letter, digit, or
     * symbol.</li> <li>A <b>character set</b> is a set of code points which
     * are each assigned to a single text character. (This may also be
     * called a <i>coded character set</i>.) As used here, character sets
     * don't define the in-memory representation of those code points.</li>
     * <li>A <b>character encoding</b> is a mapping from a sequence of code
     * points, in one or more specific character sets, to a sequence of
     * bytes and vice versa.</li> <li><b>ASCII</b> is a 128-code-point
     * character set that includes the English letters and digits, common
     * punctuation and symbols, and control characters. As used here, its
     * code points match the code points within the Basic Latin range (0-127
     * or U + 0000 to U + 007F) of the Unicode Standard.</li></ul> <p>There
     * are several kinds of character encodings:</p> <ul> <li><b>Single-byte
     * encodings</b> define a character set that assigns one code point to
     * one byte. Thus, they can have a maximum of 256 code points. For
     * example:</li> <li>(a) ISO 8859 encodings and
     * <code>windows-1252</code>.</li> <li>(b) ASCII is usually used as a
     * single-byte encoding where each code point fits in the lower 7 bits
     * of an eight-bit byte. In the Encoding Standard, all single-byte
     * encodings use the ASCII characters as the first 128 code points of
     * their character sets.</li> <li><b>Multi-byte encodings</b> include
     * code points from one or more character sets and assign some or all
     * code points to several bytes. For example:</li> <li>(a) UTF-16 uses 2
     * bytes for the most common Unicode code points and 4 bytes for
     * supplementary code points.</li> <li>(b) <code>utf-8</code> uses 1 byte for
     * ASCII and 2 to 4 bytes for the other Unicode code points.</li>
     * <li>(c) Most legacy East Asian encodings, such as <code>shift_jis</code>,
     * <code>gbk</code>, and <code>big5</code> use 1 byte for ASCII (or a slightly
     * modified version) and, usually, 2 or more bytes for national standard
     * character sets. In many of these encodings, notably <code>shift_jis</code>,
     * characters whose code points use one byte traditionally take half the
     * space of characters whose code points use two bytes.</li>
     * <li><b>Escape-based encodings</b> are combinations of single- and/or
     * multi-byte encodings, and use escape sequences and/or shift codes to
     * change which encoding to use for the bytes that follow. For
     * example:</li> <li>(a) <code>iso-2022-jp</code> supports several escape
     * sequences that shift into different encodings, including a Katakana,
     * a Kanji, and an ASCII encoding (with ASCII as the default).</li>
     * <li>(b) UTF-7 (not included in the Encoding Standard) is a Unicode
     * encoding that uses a limited subset of ASCII. The plus symbol is used
     * to shift into a modified version of base-64 to encode other Unicode
     * code points.</li> <li>The Encoding Standard also defines a
     * <b>replacement encoding</b>, which causes a decoding error and is
     * used to alias a few problematic or unsupported encoding names, such
     * as <code>hz-gb-2312</code>.</li></ul> <p><b>Getting an Encoding</b></p>
     * <p>The Encoding Standard includes UTF-8, UTF-16, and many legacy
     * encodings, and gives each one of them a name. The
     * <code>GetEncoding(name)</code> method takes a name string and returns an
     * ICharacterEncoding object that implements that encoding, or
     * <code>null</code> if the name is unrecognized.</p> <p>However, the Encoding
     * Standard is designed to include only encodings commonly used on Web
     * pages, not in other protocols such as email. For email, the Encoding
     * class includes an alternate function <code>GetEncoding(name,
     * forEmail)</code>. Setting <code>forEmail</code> to <code>true</code> will use rules
     * modified from the Encoding Standard to better suit encoding and
     * decoding text from email messages.</p> <p><b>Classes for Character
     * Encodings</b></p> <p>This Encodings class provides access to common
     * character encodings through classes as described below:</p> <ul>
     * <li>An <b>encoder class</b> is a class that converts a sequence of
     * bytes to a sequence of code points in the universal character set
     * (otherwise known under the name Unicode). An encoder class implements
     * the <code>ICharacterEncoder</code> interface.</li> <li>A <b>decoder
     * class</b> is a class that converts a sequence of Unicode code points
     * to a sequence of bytes. A decoder class implements the
     * <code>ICharacterDecoder</code> interface.</li> <li>An <b>encoding class</b>
     * allows access to both an encoder class and a decoder class and
     * implements the <code>ICharacterEncoding</code> interface. The encoder and
     * decoder classes should implement the same character
     * encoding.</li></ul> <p><b>Custom Encodings</b></p> <p>Classes that
     * implement the ICharacterEncoding interface can provide additional
     * character encodings not included in the Encoding Standard. Some
     * examples of these include the following:</p> <ul> <li>A modified
     * version of UTF-8 used in Java's serialization formats.</li> <li>A
     * modified version of UTF-7 used in the IMAP email protocol.</li></ul>
     * <p>(Note that this library doesn't implement either encoding.)</p>
     */
  public final class Encodings {
private Encodings() {
}
    /**
     * Character encoding object for the UTF-8 character encoding, which represents
     * each code point in the universal character set using 1 to 4 bytes.
     */
    public static final ICharacterEncoding UTF8 = new EncodingUtf8();

    private static final Map<String, String> charsetAliases =
        CreateAliasMap();

    /**
     * Reads bytes from a data source and converts the bytes from a given encoding
     * to a text string. <p>In the .NET implementation, this method is
     * implemented as an extension method to any object implementing
     * ICharacterEncoding and can be called as follows:
     * "encoding.DecodeString(transform)". If the object's class already has
     * a DecodeToString method with the same parameters, that method takes
     * precedence over this extension method.</p>
     * @param encoding An object that implements a given character encoding. Any
     * bytes that can&#x27;t be decoded are converted to the replacement
     * character (U + FFFD).
     * @param transform An object that implements a byte stream.
     * @return The converted string.
     * @throws NullPointerException The parameter {@code encoding} or {@code
     * transform} is null.
     */
    public static String DecodeToString(
     ICharacterEncoding encoding,
     IByteReader transform) {
      if (encoding == null) {
        throw new NullPointerException("encoding");
      }
      if (transform == null) {
        throw new NullPointerException("transform");
      }
      return InputToString(
         GetDecoderInput(encoding, transform));
    }

    /**
     * Reads a byte array from a data source and converts the bytes from a given
     * encoding to a text string. Errors in decoding are handled by
     * replacing erroneous bytes with the replacement character (U + FFFD).
     * <p>In the .NET implementation, this method is implemented as an
     * extension method to any object implementing ICharacterEncoding and
     * can be called as follows: <code>enc.DecodeToString(bytes)</code>. If the
     * object's class already has a DecodeToString method with the same
     * parameters, that method takes precedence over this extension
     * method.</p>
     * @param enc An ICharacterEncoding object.
     * @param bytes A byte array.
     * @return A string consisting of the decoded text.
     * @throws NullPointerException The parameter {@code enc} or {@code bytes} is
     * null.
     */
    public static String DecodeToString(
ICharacterEncoding enc,
byte[] bytes) {
      if (enc == null) {
        throw new NullPointerException("enc");
      }
      if (bytes == null) {
        throw new NullPointerException("bytes");
      }
      return DecodeToString(enc, DataIO.ToByteReader(bytes));
    }

    /**
     * Reads a portion of a byte array from a data source and converts the bytes
     * from a given encoding to a text string. Errors in decoding are
     * handled by replacing erroneous bytes with the replacement character
     * (U + FFFD). <p>In the .NET implementation, this method is implemented
     * as an extension method to any object implementing ICharacterEncoding
     * and can be called as follows: <code>enc.DecodeToString(bytes, offset,
     * length)</code>. If the object's class already has a DecodeToString
     * method with the same parameters, that method takes precedence over
     * this extension method.</p>
     * @param enc An object implementing a character encoding (gives access to an
     * encoder and a decoder).
     * @param bytes A byte array containing the desired portion to read.
     * @param offset A zero-based index showing where the desired portion of {@code
     * bytes} begins.
     * @param length The length, in bytes, of the desired portion of {@code bytes}
     * (but not more than {@code bytes} 's length).
     * @return A string consisting of the decoded text.
     * @throws NullPointerException The parameter {@code enc} or {@code bytes} is
     * null.
     * @throws IllegalArgumentException Either {@code offset} or {@code length} is less
     * than 0 or greater than {@code bytes} 's length, or {@code bytes} 's
     * length minus {@code offset} is less than {@code length}.
     */
    public static String DecodeToString(
ICharacterEncoding enc,
byte[] bytes,
int offset,
int length) {
      if (enc == null) {
        throw new NullPointerException("enc");
      }
      if (bytes == null) {
        throw new NullPointerException("bytes");
      }
      if (offset < 0) {
        throw new IllegalArgumentException("offset (" + offset +
          ") is less than " + 0);
      }
      if (offset > bytes.length) {
        throw new IllegalArgumentException("offset (" + offset +
          ") is more than " + bytes.length);
      }
      if (length < 0) {
        throw new IllegalArgumentException("length (" + length +
          ") is less than " + 0);
      }
      if (length > bytes.length) {
        throw new IllegalArgumentException("length (" + length +
          ") is more than " + bytes.length);
      }
      if (bytes.length - offset < length) {
        throw new IllegalArgumentException("bytes's length minus " + offset + " (" +
          (bytes.length - offset) + ") is less than " + length);
      }
      return DecodeToString(enc, DataIO.ToByteReader(bytes, offset, length));
    }

    /**
     * Reads Unicode characters from a character input and writes them to a byte
     * array encoded using the given character encoder. When writing to the
     * byte array, any characters that can't be encoded are replaced with
     * the byte 0x3f (the question mark character). <p>In the .NET
     * implementation, this method is implemented as an extension method to
     * any object implementing ICharacterInput and can be called as follows:
     * <code>input.EncodeToBytes(encoding)</code>. If the object's class already
     * has an EncodeToBytes method with the same parameters, that method
     * takes precedence over this extension method.</p>
     * @param input An object that implements a stream of universal code points.
     * @param encoding An object that implements a given character encoding.
     * @return A byte array containing the encoded text.
     * @throws NullPointerException The parameter {@code encoding} is null.
     */
    public static byte[] EncodeToBytes(
      ICharacterInput input,
      ICharacterEncoding encoding) {
      if (encoding == null) {
        throw new NullPointerException("encoding");
      }
      return EncodeToBytes(input, encoding.GetEncoder());
    }

    /**
     * Reads Unicode characters from a character input and writes them to a byte
     * array encoded using a given character encoding. When writing to the
     * byte array, any characters that can't be encoded are replaced with
     * the byte 0x3f (the question mark character). <p>In the .NET
     * implementation, this method is implemented as an extension method to
     * any object implementing ICharacterInput and can be called as follows:
     * <code>input.EncodeToBytes(encoder)</code>. If the object's class already
     * has a EncodeToBytes method with the same parameters, that method
     * takes precedence over this extension method.</p>
     * @param input An object that implements a stream of universal code points.
     * @param encoder An object that implements a character encoder.
     * @return A byte array.
     * @throws NullPointerException The parameter {@code encoder} or {@code input}
     * is null.
     */
    public static byte[] EncodeToBytes(
      ICharacterInput input,
      ICharacterEncoder encoder) {
      if (encoder == null) {
        throw new NullPointerException("encoder");
      }
      if (input == null) {
        throw new NullPointerException("input");
      }
      ArrayWriter writer = new ArrayWriter();
      while (true) {
        int cp = input.ReadChar();
        int enc = encoder.Encode(cp, writer);
        if (enc == -2) {
          // Not encodable, write a question mark instead
          writer.write((byte)0x3f);
        }
        if (enc == -1) {
          break;
        }
      }
      return writer.ToArray();
    }

    /**
     * Reads Unicode characters from a text string and writes them to a byte array
     * encoded in a given character encoding. When reading the string, any
     * unpaired surrogate characters are replaced with the replacement
     * character (U + FFFD), and when writing to the byte array, any
     * characters that can't be encoded are replaced with the byte 0x3f (the
     * question mark character). <p>In the .NET implementation, this method
     * is implemented as an extension method to any string object and can be
     * called as follows: <code>str.EncodeToBytes(enc)</code>. If the object's
     * class already has a EncodeToBytes method with the same parameters,
     * that method takes precedence over this extension method.</p>
     * @param str A string object.
     * @param enc An object implementing a character encoding (gives access to an
     * encoder and a decoder).
     * @return A byte array.
     * @throws NullPointerException The parameter {@code str} or {@code enc} is
     * null.
     */
    public static byte[] EncodeToBytes(
String str,
ICharacterEncoding enc) {
      if (str == null) {
        throw new NullPointerException("str");
      }
      if (enc == null) {
        throw new NullPointerException("enc");
      }
      return EncodeToBytes(new StringCharacterInput(str), enc);
    }

    /**
     * Reads Unicode characters from a character input and writes them to a byte
     * array encoded using the given character encoder. When writing to the
     * byte array, any characters that can't be encoded are replaced with
     * the byte 0x3f (the question mark character). <p>In the .NET
     * implementation, this method is implemented as an extension method to
     * any object implementing ICharacterInput and can be called as follows:
     * <code>input.EncodeToBytes(encoding)</code>. If the object's class already
     * has a EncodeToBytes method with the same parameters, that method
     * takes precedence over this extension method.</p>
     * @param input An object that implements a stream of universal code points.
     * @param encoding An object that implements a character encoding.
     * @param writer A byte writer to write the encoded bytes to.
     * @throws NullPointerException The parameter {@code encoding} is null.
     */
    public static void EncodeToWriter(
      ICharacterInput input,
      ICharacterEncoding encoding,
      IWriter writer) {
      if (encoding == null) {
        throw new NullPointerException("encoding");
      }
      EncodeToWriter(input, encoding.GetEncoder(), writer);
    }

    /**
     * Reads Unicode characters from a character input and writes them to a byte
     * array encoded in a given character encoding. When writing to the byte
     * array, any characters that can't be encoded are replaced with the
     * byte 0x3f (the question mark character). <p>In the .NET
     * implementation, this method is implemented as an extension method to
     * any object implementing ICharacterInput and can be called as follows:
     * <code>input.EncodeToBytes(encoder)</code>. If the object's class already
     * has a EncodeToBytes method with the same parameters, that method
     * takes precedence over this extension method.</p>
     * @param input An object that implements a stream of universal code points.
     * @param encoder An object that implements a character encoder.
     * @param writer A byte writer to write the encoded bytes to.
     * @throws NullPointerException The parameter {@code encoder} or {@code input}
     * is null.
     */
    public static void EncodeToWriter(
      ICharacterInput input,
      ICharacterEncoder encoder,
      IWriter writer) {
      if (encoder == null) {
        throw new NullPointerException("encoder");
      }
      if (input == null) {
        throw new NullPointerException("input");
      }
      if (writer == null) {
        throw new NullPointerException("writer");
      }
      while (true) {
        int cp = input.ReadChar();
        int enc = encoder.Encode(cp, writer);
        if (enc == -2) {
          // Not encodable, write a question mark instead
          writer.write((byte)0x3f);
        }
        if (enc == -1) {
          break;
        }
      }
    }

    /**
     * Converts a text string to bytes and writes the bytes to an output byte
     * writer. When reading the string, any unpaired surrogate characters
     * are replaced with the replacement character (U + FFFD), and when
     * writing to the byte stream, any characters that can't be encoded are
     * replaced with the byte 0x3f (the question mark character). <p>In the
     * .NET implementation, this method is implemented as an extension
     * method to any string object and can be called as follows:
     * <code>str.EncodeToBytes(enc, writer)</code>. If the object's class already
     * has a EncodeToBytes method with the same parameters, that method
     * takes precedence over this extension method.</p>
     * @param str A string object to encode.
     * @param enc An object implementing a character encoding (gives access to an
     * encoder and a decoder).
     * @param writer A byte writer where the encoded bytes will be written to.
     * @throws NullPointerException The parameter {@code str} or {@code enc} is
     * null.
     */
    public static void EncodeToWriter(
String str,
ICharacterEncoding enc,
IWriter writer) {
      if (str == null) {
        throw new NullPointerException("str");
      }
      if (enc == null) {
        throw new NullPointerException("enc");
      }
      EncodeToWriter(new StringCharacterInput(str), enc, writer);
    }

    /**
     * Converts a character encoding into a character input stream, given a
     * streamable source of bytes. The input stream doesn't check the first
     * few bytes for a byte-order mark indicating a Unicode encoding such as
     * UTF-8 before using the character encoding's decoder. <p>In the .NET
     * implementation, this method is implemented as an extension method to
     * any object implementing ICharacterEncoding and can be called as
     * follows: "encoding.GetDecoderInput(transform)". If the object's class
     * already has a GetDecoderInput method with the same parameters, that
     * method takes precedence over this extension method.</p>
     * @param encoding Encoding that exposes a decoder to be converted into a
     * character input stream. If the decoder returns -2 (indicating a
     * decode error), the character input stream handles the error by
     * returning a replacement character in its place.
     * @param stream Byte stream to convert into Unicode characters.
     * @return An ICharacterInput object.
     */
    public static ICharacterInput GetDecoderInput(
      ICharacterEncoding encoding,
      IByteReader stream) {
      return new DecoderToInputClass(
        encoding.GetDecoder(),
        stream);
    }

    /**
     * Converts a character encoding into a character input stream, given a
     * streamable source of bytes. But if the input stream starts with a
     * UTF-8 or UTF-16 byte order mark, the input is decoded as UTF-8 or
     * UTF-16, as the case may be, rather than the given character encoding.
     * <p>This method implements the "decode" algorithm specified in the
     * Encoding standard.</p> <p>In the .NET implementation, this method is
     * implemented as an extension method to any object implementing
     * ICharacterEncoding and can be called as follows:
     * "encoding.GetDecoderInput(transform)". If the object's class already
     * has a GetDecoderInput method with the same parameters, that method
     * takes precedence over this extension method.</p>
     * @param encoding Encoding object that exposes a decoder to be converted into
     * a character input stream. If the decoder returns -2 (indicating a
     * decode error), the character input stream handles the error by
     * returning a replacement character in its place.
     * @param stream Byte stream to convert into Unicode characters.
     * @return An ICharacterInput object.
     */
    public static ICharacterInput GetDecoderInputSkipBom(
      ICharacterEncoding encoding,
      IByteReader stream) {
      return EncoderAlgorithms.DecodeAlgorithmInput(stream, encoding);
    }

    /**
     * Returns a character encoding from the given name.
     * @param name A string naming a character encoding. See the ResolveAlias
     * method. Can be null.
     * @return An ICharacterEncoding object.
     */
    public static ICharacterEncoding GetEncoding(String name) {
      return GetEncoding(name, false, false);
    }

    /**
     * Returns a character encoding from the given name.
     * @param name A string naming a character encoding. See the ResolveAlias
     * method. Can be null.
     * @param forEmail A Boolean object.
     * @return An ICharacterEncoding object.
     */
    public static ICharacterEncoding GetEncoding(String name, boolean forEmail) {
      return GetEncoding(name, forEmail, false);
    }

    /**
     * Returns a character encoding from the given name.
     * @param name A string naming a character encoding. See the ResolveAlias
     * method. Can be null.
     * @param forEmail If false, uses the encoding resolution rules in the Encoding
     * Standard. If true, uses modified rules as described in the
     * ResolveAliasForEmail method.
     * @param allowReplacement If true, allows the label {@code replacement} to
     * return the replacement encoding.
     * @return An object that enables encoding and decoding text in the given
     * character encoding. Returns null if the name is null or empty, or if
     * it names an unrecognized or unsupported encoding.
     */
    public static ICharacterEncoding GetEncoding(
String name,
boolean forEmail,
boolean allowReplacement) {
      if (((name) == null || (name).length() == 0)) {
        return null;
      }
      if (allowReplacement) {
        name = TrimAsciiWhite(name);
        name = ToLowerCaseAscii(name);
        if (name.equals("replacement")) {
          return new EncodingReplacement();
        }
      }
      name = forEmail ? ResolveAliasForEmail(name) :
        ResolveAlias(name);
      if (name.equals("utf-8")) {
        return UTF8;
      }
      if (name.equals("us-ascii")) {
        return (ICharacterEncoding)(new EncodingAscii());
      }
      if (name.equals("iso-8859-1")) {
        return (ICharacterEncoding)(new EncodingLatinOne());
      }
      if (name.equals("utf-7")) {
        return (ICharacterEncoding)(new EncodingUtf7());
      }
      if (name.equals("windows-1252")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 8364, 129,
          8218,
    402, 8222, 8230, 8224,
    8225, 710, 8240, 352, 8249, 338, 141, 381, 143, 144, 8216, 8217, 8220, 8221,
    8226, 8211, 8212, 732, 8482, 353, 8250, 339, 157, 382, 376, 160, 161, 162,
    163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177,
    178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192,
    193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207,
    208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222,
    223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237,
    238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252,
    253, 254, 255 });
      }

      if (name.equals("ibm866")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 1040, 1041,
        1042, 1043, 1044, 1045,
    1046, 1047, 1048, 1049, 1050, 1051, 1052, 1053, 1054, 1055, 1056, 1057,
    1058, 1059, 1060, 1061, 1062, 1063, 1064, 1065, 1066, 1067, 1068, 1069,
    1070, 1071, 1072, 1073, 1074, 1075, 1076, 1077, 1078, 1079, 1080, 1081,
    1082, 1083, 1084, 1085, 1086, 1087, 9617, 9618, 9619, 9474, 9508, 9569,
    9570, 9558, 9557, 9571, 9553, 9559, 9565, 9564, 9563, 9488, 9492, 9524,
    9516, 9500, 9472, 9532, 9566, 9567, 9562, 9556, 9577, 9574, 9568, 9552,
    9580, 9575, 9576, 9572, 9573, 9561, 9560, 9554, 9555, 9579, 9578, 9496,
    9484, 9608, 9604, 9612, 9616, 9600, 1088, 1089, 1090, 1091, 1092, 1093,
    1094, 1095, 1096, 1097, 1098, 1099, 1100, 1101, 1102, 1103, 1025, 1105,
    1028, 1108, 1031, 1111, 1038, 1118, 176, 8729, 183, 8730, 8470, 164, 9632,
    160 });
      }
      if (name.equals("iso-8859-10")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 128, 129, 130,
          131,
    132, 133, 134, 135,
    136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150,
    151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 260, 274, 290, 298, 296,
    310, 167, 315, 272, 352, 358, 381, 173, 362, 330, 176, 261, 275, 291, 299,
    297, 311, 183, 316, 273, 353, 359, 382, 8213, 363, 331, 256, 193, 194, 195,
    196, 197, 198, 302, 268, 201, 280, 203, 278, 205, 206, 207, 208, 325, 332,
    211, 212, 213, 214, 360, 216, 370, 218, 219, 220, 221, 222, 223, 257, 225,
    226, 227, 228, 229, 230, 303, 269, 233, 281, 235, 279, 237, 238, 239, 240,
    326, 333, 243, 244, 245, 246, 361, 248, 371, 250, 251, 252, 253, 254, 312
    });
      }
      if (name.equals("iso-8859-13")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 128, 129, 130,
          131,
    132, 133, 134, 135,
    136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150,
    151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 8221, 162, 163, 164, 8222,
    166, 167, 216, 169, 342, 171, 172, 173, 174, 198, 176, 177, 178, 179, 8220,
    181, 182, 183, 248, 185, 343, 187, 188, 189, 190, 230, 260, 302, 256, 262,
    196, 197, 280, 274, 268, 201, 377, 278, 290, 310, 298, 315, 352, 323, 325,
    211, 332, 213, 214, 215, 370, 321, 346, 362, 220, 379, 381, 223, 261, 303,
    257, 263, 228, 229, 281, 275, 269, 233, 378, 279, 291, 311, 299, 316, 353,
    324, 326, 243, 333, 245, 246, 247, 371, 322, 347, 363, 252, 380, 382, 8217
    });
      }
      if (name.equals("iso-8859-14")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 128, 129, 130,
          131,
    132, 133, 134, 135,
    136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150,
    151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 7682, 7683, 163, 266, 267,
    7690, 167, 7808, 169, 7810, 7691, 7922, 173, 174, 376, 7710, 7711, 288, 289,
    7744, 7745, 182, 7766, 7809, 7767, 7811, 7776, 7923, 7812, 7813, 7777, 192,
    193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207,
    372, 209, 210, 211, 212, 213, 214, 7786, 216, 217, 218, 219, 220, 221, 374,
    223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237,
    238, 239, 373, 241, 242, 243, 244, 245, 246, 7787, 248, 249, 250, 251, 252,
    253, 375, 255 });
      }
      if (name.equals("iso-8859-15")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 128, 129, 130,
          131,
    132, 133, 134, 135,
    136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150,
    151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 8364, 165,
    352, 167, 353, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 381,
    181, 182, 183, 382, 185, 186, 187, 338, 339, 376, 191, 192, 193, 194, 195,
    196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210,
    211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225,
    226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240,
    241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255
    });
      }
      if (name.equals("iso-8859-16")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 128, 129, 130,
          131,
    132, 133, 134, 135,
    136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150,
    151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 260, 261, 321, 8364, 8222,
    352, 167, 353, 169, 536, 171, 377, 173, 378, 379, 176, 177, 268, 322, 381,
    8221, 182, 183, 382, 269, 537, 187, 338, 339, 376, 380, 192, 193, 194, 258,
    196, 262, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 272, 323, 210,
    211, 212, 336, 214, 346, 368, 217, 218, 219, 220, 280, 538, 223, 224, 225,
    226, 259, 228, 263, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 273,
    324, 242, 243, 244, 337, 246, 347, 369, 249, 250, 251, 252, 281, 539, 255
    });
      }
      if (name.equals("iso-8859-2")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 128, 129, 130,
          131,
    132, 133, 134, 135,
    136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150,
    151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 260, 728, 321, 164, 317,
    346, 167, 168, 352, 350, 356, 377, 173, 381, 379, 176, 261, 731, 322, 180,
    318, 347, 711, 184, 353, 351, 357, 378, 733, 382, 380, 340, 193, 194, 258,
    196, 313, 262, 199, 268, 201, 280, 203, 282, 205, 206, 270, 272, 323, 327,
    211, 212, 336, 214, 215, 344, 366, 218, 368, 220, 221, 354, 223, 341, 225,
    226, 259, 228, 314, 263, 231, 269, 233, 281, 235, 283, 237, 238, 271, 273,
    324, 328, 243, 244, 337, 246, 247, 345, 367, 250, 369, 252, 253, 355, 729
    });
      }
      if (name.equals("iso-8859-3")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 128, 129, 130,
          131,
    132, 133, 134, 135,
    136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150,
    151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 294, 728, 163, 164, -2,
    292, 167, 168, 304, 350, 286, 308, 173, -2, 379, 176, 295, 178, 179, 180,
    181, 293, 183, 184, 305, 351, 287, 309, 189, -2, 380, 192, 193, 194,
    -2, 196, 266, 264, 199, 200, 201, 202, 203, 204, 205, 206, 207, -2,
    209, 210, 211, 212, 288, 214, 215, 284, 217, 218, 219, 220, 364, 348, 223,
    224, 225, 226, -2, 228, 267, 265, 231, 232, 233, 234, 235, 236, 237, 238,
    239, -2, 241, 242, 243, 244, 289, 246, 247, 285, 249, 250, 251, 252, 365,
    349, 729 });
      }
      if (name.equals("iso-8859-4")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 128, 129, 130,
          131,
    132, 133, 134, 135,
    136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150,
    151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 260, 312, 342, 164, 296,
    315, 167, 168, 352, 274, 290, 358, 173, 381, 175, 176, 261, 731, 343, 180,
    297, 316, 711, 184, 353, 275, 291, 359, 330, 382, 331, 256, 193, 194, 195,
    196, 197, 198, 302, 268, 201, 280, 203, 278, 205, 206, 298, 272, 325, 332,
    310, 212, 213, 214, 215, 216, 370, 218, 219, 220, 360, 362, 223, 257, 225,
    226, 227, 228, 229, 230, 303, 269, 233, 281, 235, 279, 237, 238, 299, 273,
    326, 333, 311, 244, 245, 246, 247, 248, 371, 250, 251, 252, 361, 363, 729
    });
      }
      if (name.equals("iso-8859-5")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 128, 129, 130,
          131,
    132, 133, 134, 135,
    136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150,
    151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 1025, 1026, 1027, 1028,
    1029, 1030, 1031, 1032, 1033, 1034, 1035, 1036, 173, 1038, 1039, 1040, 1041,
    1042, 1043, 1044, 1045, 1046, 1047, 1048, 1049, 1050, 1051, 1052, 1053,
    1054, 1055, 1056, 1057, 1058, 1059, 1060, 1061, 1062, 1063, 1064, 1065,
    1066, 1067, 1068, 1069, 1070, 1071, 1072, 1073, 1074, 1075, 1076, 1077,
    1078, 1079, 1080, 1081, 1082, 1083, 1084, 1085, 1086, 1087, 1088, 1089,
    1090, 1091, 1092, 1093, 1094, 1095, 1096, 1097, 1098, 1099, 1100, 1101,
    1102, 1103, 8470, 1105, 1106, 1107, 1108, 1109, 1110, 1111, 1112, 1113,
    1114, 1115, 1116, 167, 1118, 1119 });
      }
      if (name.equals("iso-8859-6")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 128, 129, 130,
          131,
    132, 133, 134, 135,
    136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150,
    151, 152, 153, 154, 155, 156, 157, 158, 159, 160, -2, -2, -2, 164,
    -2, -2, -2, -2, -2, -2, -2, 1548, 173, -2, -2,
    -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,
    1563, -2, -2, -2, 1567, -2, 1569, 1570, 1571, 1572, 1573, 1574,
    1575, 1576, 1577, 1578, 1579, 1580, 1581, 1582, 1583, 1584, 1585, 1586,
    1587, 1588, 1589, 1590, 1591, 1592, 1593, 1594, -2, -2, -2, -2,
    -2, 1600, 1601, 1602, 1603, 1604, 1605, 1606, 1607, 1608, 1609, 1610,
    1611, 1612, 1613, 1614, 1615, 1616, 1617, 1618, -2, -2, -2, -2,
    -2, -2, -2, -2, -2, -2, -2, -2, -2 });
      }
      if (name.equals("iso-8859-7")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 128, 129, 130,
          131,
    132, 133, 134, 135,
    136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150,
    151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 8216, 8217, 163, 8364,
    8367, 166, 167, 168, 169, 890, 171, 172, 173, -2, 8213, 176, 177, 178,
    179, 900, 901, 902, 183, 904, 905, 906, 187, 908, 189, 910, 911, 912, 913,
    914, 915, 916, 917, 918, 919, 920, 921, 922, 923, 924, 925, 926, 927, 928,
    929, -2, 931, 932, 933, 934, 935, 936, 937, 938, 939, 940, 941, 942, 943,
    944, 945, 946, 947, 948, 949, 950, 951, 952, 953, 954, 955, 956, 957, 958,
    959, 960, 961, 962, 963, 964, 965, 966, 967, 968, 969, 970, 971, 972, 973,
    974, -2 });
      }
      if (name.equals("iso-8859-8") || name.equals("iso-8859-8-i")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 128, 129, 130,
          131,
    132, 133, 134, 135,
    136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150,
    151, 152, 153, 154, 155, 156, 157, 158, 159, 160, -2, 162, 163, 164, 165,
    166, 167, 168, 169, 215, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180,
    181, 182, 183, 184, 185, 247, 187, 188, 189, 190, -2, -2, -2,
    -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,
    -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,
    -2, -2, -2, -2, -2, -2, -2, 8215, 1488, 1489, 1490,
    1491, 1492, 1493, 1494, 1495, 1496, 1497, 1498, 1499, 1500, 1501, 1502,
    1503, 1504, 1505, 1506, 1507, 1508, 1509, 1510, 1511, 1512, 1513, 1514,
    -2, -2, 8206, 8207, -2 });
      }
      if (name.equals("koi8-r")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 9472, 9474,
        9484, 9488, 9492, 9496,
    9500, 9508, 9516, 9524, 9532, 9600, 9604, 9608, 9612, 9616, 9617, 9618,
    9619, 8992, 9632, 8729, 8730, 8776, 8804, 8805, 160, 8993, 176, 178, 183,
    247, 9552, 9553, 9554, 1105, 9555, 9556, 9557, 9558, 9559, 9560, 9561, 9562,
    9563, 9564, 9565, 9566, 9567, 9568, 9569, 1025, 9570, 9571, 9572, 9573,
    9574, 9575, 9576, 9577, 9578, 9579, 9580, 169, 1102, 1072, 1073, 1094, 1076,
    1077, 1092, 1075, 1093, 1080, 1081, 1082, 1083, 1084, 1085, 1086, 1087,
    1103, 1088, 1089, 1090, 1091, 1078, 1074, 1100, 1099, 1079, 1096, 1101,
    1097, 1095, 1098, 1070, 1040, 1041, 1062, 1044, 1045, 1060, 1043, 1061,
    1048, 1049, 1050, 1051, 1052, 1053, 1054, 1055, 1071, 1056, 1057, 1058,
    1059, 1046, 1042, 1068, 1067, 1047, 1064, 1069, 1065, 1063, 1066 });
      }
      if (name.equals("koi8-u")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 9472, 9474,
        9484, 9488, 9492, 9496,
    9500, 9508, 9516, 9524, 9532, 9600, 9604, 9608, 9612, 9616, 9617, 9618,
    9619, 8992, 9632, 8729, 8730, 8776, 8804, 8805, 160, 8993, 176, 178, 183,
    247, 9552, 9553, 9554, 1105, 1108, 9556, 1110, 1111, 9559, 9560, 9561, 9562,
    9563, 1169, 1118, 9566, 9567, 9568, 9569, 1025, 1028, 9571, 1030, 1031,
    9574, 9575, 9576, 9577, 9578, 1168, 1038, 169, 1102, 1072, 1073, 1094, 1076,
    1077, 1092, 1075, 1093, 1080, 1081, 1082, 1083, 1084, 1085, 1086, 1087,
    1103, 1088, 1089, 1090, 1091, 1078, 1074, 1100, 1099, 1079, 1096, 1101,
    1097, 1095, 1098, 1070, 1040, 1041, 1062, 1044, 1045, 1060, 1043, 1061,
    1048, 1049, 1050, 1051, 1052, 1053, 1054, 1055, 1071, 1056, 1057, 1058,
    1059, 1046, 1042, 1068, 1067, 1047, 1064, 1069, 1065, 1063, 1066 });
      }
      if (name.equals("macintosh")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 196, 197, 199,
          201,
    209, 214, 220, 225,
    224, 226, 228, 227, 229, 231, 233, 232, 234, 235, 237, 236, 238, 239, 241,
    243, 242, 244, 246, 245, 250, 249, 251, 252, 8224, 176, 162, 163, 167, 8226,
    182, 223, 174, 169, 8482, 180, 168, 8800, 198, 216, 8734, 177, 8804, 8805,
    165, 181, 8706, 8721, 8719, 960, 8747, 170, 186, 937, 230, 248, 191, 161,
    172, 8730, 402, 8776, 8710, 171, 187, 8230, 160, 192, 195, 213, 338, 339,
    8211, 8212, 8220, 8221, 8216, 8217, 247, 9674, 255, 376, 8260, 8364, 8249,
    8250, 64257, 64258, 8225, 183, 8218, 8222, 8240, 194, 202, 193, 203, 200,
    205, 206, 207, 204, 211, 212, 63743, 210, 218, 219, 217, 305, 710, 732, 175,
    728, 729, 730, 184, 733, 731, 711 });
      }
      if (name.equals("windows-1250")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 8364, 129,
          8218,
    131, 8222, 8230, 8224,
    8225, 136, 8240, 352, 8249, 346, 356, 381, 377, 144, 8216, 8217, 8220, 8221,
    8226, 8211, 8212, 152, 8482, 353, 8250, 347, 357, 382, 378, 160, 711, 728,
    321, 164, 260, 166, 167, 168, 169, 350, 171, 172, 173, 174, 379, 176, 177,
    731, 322, 180, 181, 182, 183, 184, 261, 351, 187, 317, 733, 318, 380, 340,
    193, 194, 258, 196, 313, 262, 199, 268, 201, 280, 203, 282, 205, 206, 270,
    272, 323, 327, 211, 212, 336, 214, 215, 344, 366, 218, 368, 220, 221, 354,
    223, 341, 225, 226, 259, 228, 314, 263, 231, 269, 233, 281, 235, 283, 237,
    238, 271, 273, 324, 328, 243, 244, 337, 246, 247, 345, 367, 250, 369, 252,
    253, 355, 729 });
      }
      if (name.equals("windows-1251")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 1026, 1027,
        8218, 1107, 8222, 8230,
    8224, 8225, 8364, 8240, 1033, 8249, 1034, 1036, 1035, 1039, 1106, 8216,
    8217, 8220, 8221, 8226, 8211, 8212, 152, 8482, 1113, 8250, 1114, 1116, 1115,
    1119, 160, 1038, 1118, 1032, 164, 1168, 166, 167, 1025, 169, 1028, 171, 172,
    173, 174, 1031, 176, 177, 1030, 1110, 1169, 181, 182, 183, 1105, 8470, 1108,
    187, 1112, 1029, 1109, 1111, 1040, 1041, 1042, 1043, 1044, 1045, 1046, 1047,
    1048, 1049, 1050, 1051, 1052, 1053, 1054, 1055, 1056, 1057, 1058, 1059,
    1060, 1061, 1062, 1063, 1064, 1065, 1066, 1067, 1068, 1069, 1070, 1071,
    1072, 1073, 1074, 1075, 1076, 1077, 1078, 1079, 1080, 1081, 1082, 1083,
    1084, 1085, 1086, 1087, 1088, 1089, 1090, 1091, 1092, 1093, 1094, 1095,
    1096, 1097, 1098, 1099, 1100, 1101, 1102, 1103 });
      }
      if (name.equals("windows-1253")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 8364, 129,
          8218,
    402, 8222, 8230, 8224,
    8225, 136, 8240, 138, 8249, 140, 141, 142, 143, 144, 8216, 8217, 8220, 8221,
    8226, 8211, 8212, 152, 8482, 154, 8250, 156, 157, 158, 159, 160, 901, 902,
    163, 164, 165, 166, 167, 168, 169, -2, 171, 172, 173, 174, 8213, 176,
    177, 178, 179, 900, 181, 182, 183, 904, 905, 906, 187, 908, 189, 910, 911,
    912, 913, 914, 915, 916, 917, 918, 919, 920, 921, 922, 923, 924, 925, 926,
    927, 928, 929, -2, 931, 932, 933, 934, 935, 936, 937, 938, 939, 940, 941,
    942, 943, 944, 945, 946, 947, 948, 949, 950, 951, 952, 953, 954, 955, 956,
    957, 958, 959, 960, 961, 962, 963, 964, 965, 966, 967, 968, 969, 970, 971,
    972, 973, 974, -2 });
      }
      if (name.equals("windows-1254")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 8364, 129,
          8218,
    402, 8222, 8230, 8224,
    8225, 710, 8240, 352, 8249, 338, 141, 142, 143, 144, 8216, 8217, 8220, 8221,
    8226, 8211, 8212, 732, 8482, 353, 8250, 339, 157, 158, 376, 160, 161, 162,
    163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177,
    178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192,
    193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207,
    286, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 304, 350,
    223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237,
    238, 239, 287, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252,
    305, 351, 255 });
      }
      if (name.equals("windows-1255")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 8364, 129,
          8218,
    402, 8222, 8230, 8224,
    8225, 710, 8240, 138, 8249, 140, 141, 142, 143, 144, 8216, 8217, 8220, 8221,
    8226, 8211, 8212, 732, 8482, 154, 8250, 156, 157, 158, 159, 160, 161, 162,
    163, 8362, 165, 166, 167, 168, 169, 215, 171, 172, 173, 174, 175, 176, 177,
    178, 179, 180, 181, 182, 183, 184, 185, 247, 187, 188, 189, 190, 191, 1456,
    1457, 1458, 1459, 1460, 1461, 1462, 1463, 1464, 1465, -2, 1467, 1468,
    1469, 1470, 1471, 1472, 1473, 1474, 1475, 1520, 1521, 1522, 1523, 1524,
    -2, -2, -2, -2, -2, -2, -2, 1488, 1489, 1490, 1491,
    1492, 1493, 1494, 1495, 1496, 1497, 1498, 1499, 1500, 1501, 1502, 1503,
    1504, 1505, 1506, 1507, 1508, 1509, 1510, 1511, 1512, 1513, 1514, -2,
    -2, 8206, 8207, -2 });
      }
      if (name.equals("windows-1256")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 8364, 1662,
          8218,
   402, 8222, 8230, 8224,
    8225, 710, 8240, 1657, 8249, 338, 1670, 1688, 1672, 1711, 8216, 8217, 8220,
    8221, 8226, 8211, 8212, 1705, 8482, 1681, 8250, 339, 8204, 8205, 1722, 160,
    1548, 162, 163, 164, 165, 166, 167, 168, 169, 1726, 171, 172, 173, 174, 175,
    176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 1563, 187, 188, 189, 190,
    1567, 1729, 1569, 1570, 1571, 1572, 1573, 1574, 1575, 1576, 1577, 1578,
    1579, 1580, 1581, 1582, 1583, 1584, 1585, 1586, 1587, 1588, 1589, 1590, 215,
    1591, 1592, 1593, 1594, 1600, 1601, 1602, 1603, 224, 1604, 226, 1605, 1606,
    1607, 1608, 231, 232, 233, 234, 235, 1609, 1610, 238, 239, 1611, 1612, 1613,
    1614, 244, 1615, 1616, 247, 1617, 249, 1618, 251, 252, 8206, 8207, 1746 });
      }
      if (name.equals("windows-1257")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 8364, 129,
          8218,
    131, 8222, 8230, 8224,
    8225, 136, 8240, 138, 8249, 140, 168, 711, 184, 144, 8216, 8217, 8220, 8221,
    8226, 8211, 8212, 152, 8482, 154, 8250, 156, 175, 731, 159, 160, -2, 162,
    163, 164, -2, 166, 167, 216, 169, 342, 171, 172, 173, 174, 198, 176, 177,
    178, 179, 180, 181, 182, 183, 248, 185, 343, 187, 188, 189, 190, 230, 260,
    302, 256, 262, 196, 197, 280, 274, 268, 201, 377, 278, 290, 310, 298, 315,
    352, 323, 325, 211, 332, 213, 214, 215, 370, 321, 346, 362, 220, 379, 381,
    223, 261, 303, 257, 263, 228, 229, 281, 275, 269, 233, 378, 279, 291, 311,
    299, 316, 353, 324, 326, 243, 333, 245, 246, 247, 371, 322, 347, 363, 252,
    380, 382, 729 });
      }
      if (name.equals("windows-1258")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 8364, 129,
          8218,
    402, 8222, 8230, 8224,
    8225, 710, 8240, 138, 8249, 338, 141, 142, 143, 144, 8216, 8217, 8220, 8221,
    8226, 8211, 8212, 732, 8482, 154, 8250, 339, 157, 158, 376, 160, 161, 162,
    163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177,
    178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192,
    193, 194, 258, 196, 197, 198, 199, 200, 201, 202, 203, 768, 205, 206, 207,
    272, 209, 777, 211, 212, 416, 214, 215, 216, 217, 218, 219, 220, 431, 771,
    223, 224, 225, 226, 259, 228, 229, 230, 231, 232, 233, 234, 235, 769, 237,
    238, 239, 273, 241, 803, 243, 244, 417, 246, 247, 248, 249, 250, 251, 252,
    432, 8363, 255 });
      }
      if (name.equals("windows-874")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 8364, 129,
          130,
       131, 132, 8230, 134,
    135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 8216, 8217, 8220, 8221,
    8226, 8211, 8212, 152, 153, 154, 155, 156, 157, 158, 159, 160, 3585, 3586,
    3587, 3588, 3589, 3590, 3591, 3592, 3593, 3594, 3595, 3596, 3597, 3598,
    3599, 3600, 3601, 3602, 3603, 3604, 3605, 3606, 3607, 3608, 3609, 3610,
    3611, 3612, 3613, 3614, 3615, 3616, 3617, 3618, 3619, 3620, 3621, 3622,
    3623, 3624, 3625, 3626, 3627, 3628, 3629, 3630, 3631, 3632, 3633, 3634,
    3635, 3636, 3637, 3638, 3639, 3640, 3641, 3642, -2, -2, -2, -2,
    3647, 3648, 3649, 3650, 3651, 3652, 3653, 3654, 3655, 3656, 3657, 3658,
    3659, 3660, 3661, 3662, 3663, 3664, 3665, 3666, 3667, 3668, 3669, 3670,
    3671, 3672, 3673, 3674, 3675, -2, -2, -2, -2 });
      }
      if (name.equals("x-mac-cyrillic")) {
        return (ICharacterEncoding)new EncodingSingleByte(new int[] { 1040, 1041,
        1042, 1043, 1044, 1045,
    1046, 1047, 1048, 1049, 1050, 1051, 1052, 1053, 1054, 1055, 1056, 1057,
    1058, 1059, 1060, 1061, 1062, 1063, 1064, 1065, 1066, 1067, 1068, 1069,
    1070, 1071, 8224, 176, 1168, 163, 167, 8226, 182, 1030, 174, 169, 8482,
    1026, 1106, 8800, 1027, 1107, 8734, 177, 8804, 8805, 1110, 181, 1169, 1032,
    1028, 1108, 1031, 1111, 1033, 1113, 1034, 1114, 1112, 1029, 172, 8730, 402,
    8776, 8710, 171, 187, 8230, 160, 1035, 1115, 1036, 1116, 1109, 8211, 8212,
    8220, 8221, 8216, 8217, 247, 8222, 1038, 1118, 1039, 1119, 8470, 1025, 1105,
    1103, 1072, 1073, 1074, 1075, 1076, 1077, 1078, 1079, 1080, 1081, 1082,
    1083, 1084, 1085, 1086, 1087, 1088, 1089, 1090, 1091, 1092, 1093, 1094,
    1095, 1096, 1097, 1098, 1099, 1100, 1101, 1102, 8364 });
      } else if (name.equals("euc-jp")) {
        return (ICharacterEncoding)(new EncodingEUCJP());
      } else if (name.equals("euc-kr")) {
        return (ICharacterEncoding)(new EncodingKoreanEUC());
      } else if (name.equals("big5")) {
        return (ICharacterEncoding)(new EncodingBig5());
      } else if (name.equals("shift_jis")) {
        return (ICharacterEncoding)(new EncodingShiftJIS());
      } else if (name.equals("x-user-defined")) {
        return (ICharacterEncoding)(new EncodingXUserDefined());
      } else if (name.equals("gbk")) {
        return (ICharacterEncoding)(new EncodingGBK());
      } else if (name.equals("gb18030")) {
        return (ICharacterEncoding)(new EncodingGB18030());
      } else if (name.equals("utf-16le")) {
        return (ICharacterEncoding)(new EncodingUtf16());
      } else if (name.equals("utf-16be")) {
        return (ICharacterEncoding)(new EncodingUtf16BE());
      }
      return name.equals("iso-2022-jp") ? (ICharacterEncoding)(new
        EncodingISO2022JP()) :
        (name.equals("replacement") ? (ICharacterEncoding)(new
          EncodingReplacement()) : null);
    }

    /**
     * Reads Unicode characters from a character input and converts them to a text
     * string. <p>In the .NET implementation, this method is implemented as
     * an extension method to any object implementing ICharacterInput and
     * can be called as follows: <code>reader.InputToString()</code>. If the
     * object's class already has a InputToString method with the same
     * parameters, that method takes precedence over this extension
     * method.</p>
     * @param reader A character input whose characters will be converted to a text
     * string.
     * @return A text string containing the characters read.
     */
    public static String InputToString(ICharacterInput reader) {
      StringBuilder builder = new StringBuilder();
      while (true) {
        int c = reader.ReadChar();
        if (c < 0) {
          break;
        }
        if (c <= 0xffff) {
          builder.append((char)c);
        } else if (c <= 0x10ffff) {
          builder.append((char)((((c - 0x10000) >> 10) & 0x3ff) + 0xd800));
          builder.append((char)(((c - 0x10000) & 0x3ff) + 0xdc00));
        }
      }
      return builder.toString();
    }

    /**
     * Resolves a character encoding's name to a standard form. This involves
     * changing aliases of a character encoding to a standardized name.
     * <p>In several Internet standards, this name is known as a "charset"
     * parameter. In HTML and HTTP, for example, the "charset" parameter
     * indicates the encoding used to represent text in the HTML page, text
     * file, etc.</p>
     * @param name A string that names a given character encoding. Can be null. Any
     * leading and trailing whitespace is removed and the name converted to
     * lowercase before resolving the encoding&#x27;s name. The Encoding
     * Standard supports only the following encodings (and defines aliases
     * for most of them):. <ul> <li> {@code utf-8} - UTF-8 (8-bit universal
     * character set, the encoding recommended by the Encoding Standard for
     * new data formats)</li> <li> {@code utf-16le} - UTF-16 little-endian
     * (16-bit UCS)</li> <li> {@code utf-16be} - UTF-16 big-endian (16-bit
     * UCS)</li> <li>The special-purpose encoding {@code
     * x-user-defined}</li> <li>The special-purpose encoding {@code
     * replacement}, which this function returns only if one of several
     * aliases are passed to it, as defined in the Encoding Standard.</li>
     * <li>28 legacy single-byte encodings: <ul> <li> {@code windows-1252} :
     * Western Europe (Note: The Encoding Standard aliases the names {@code
     * us-ascii} and {@code iso-8859-1} to {@code windows-1252}, which
     * specifies a different character set from either; it differs from
     * {@code iso-8859-1} by assigning different characters to some bytes
     * from 0x80 to 0x9F. The Encoding Standard does this for compatibility
     * with existing Web pages.)</li> <li> {@code iso-8859-2}, {@code
     * windows-1250} : Central Europe</li> <li> {@code iso-8859-10} :
     * Northern Europe</li> <li> {@code iso-8859-4}, {@code windows-1257} :
     * Baltic</li> <li> {@code iso-8859-13} : Estonian</li> <li> {@code
     * iso-8859-14} : Celtic</li> <li> {@code iso-8859-16} : Romanian</li>
     * <li> {@code iso-8859-5}, {@code ibm866}, {@code koi8-r}, {@code
     * windows-1251}, {@code x-mac-cyrillic} : Cyrillic</li> <li> {@code
     * koi8-u} : Ukrainian</li> <li> {@code iso-8859-7}, {@code windows-1253}
     * : Greek</li> <li> {@code iso-8859-6}, {@code windows-1256} :
     * Arabic</li> <li> {@code iso-8859-8}, {@code iso-8859-8-i}, {@code
     * windows-1255} : Hebrew</li> <li> {@code iso-8859-3} : Latin 3</li>
     * <li> {@code iso-8859-15} : Latin 9</li> <li> {@code windows-1254} :
     * Turkish</li> <li> {@code windows-874} : Thai</li> <li> {@code
     * windows-1258} : Vietnamese</li> <li> {@code macintosh} : Mac
     * Roman</li></ul></li> <li>Three legacy Japanese encodings: {@code
     * shift_jis}, {@code euc-jp}, {@code iso-2022-jp}</li> <li>Two legacy
     * simplified Chinese encodings: {@code gbk} and {@code gb18030}</li>
     * <li> {@code big5} : legacy traditional Chinese encoding</li>
     * <li> {@code euc-kr} : legacy Korean encoding</li></ul> <p>The {@code
     * utf-8}, {@code utf-16le}, and {@code utf-16be} encodings don't encode
     * a byte-order mark at the start of the text (doing so is not
     * recommended for {@code utf-8}, while in {@code utf-16le} and {@code
     * utf-16be}, the byte-order mark character U + FEFF is treated as an
     * ordinary character, unlike in to the UTF-16 encoding form). The
     * Encoding Standard aliases {@code utf-16} to {@code utf-16le} "to deal
     * with deployed content".</p> .
     * @return A standardized name for the encoding. Returns the empty string if
     * {@code name} is null or empty, or if the encoding name is
     * unsupported.
     */
    public static String ResolveAlias(String name) {
      if (((name) == null || (name).length() == 0)) {
        return "";
      }
      name = TrimAsciiWhite(name);
      name = ToLowerCaseAscii(name);
      return charsetAliases.containsKey(name) ? charsetAliases.get(name) :
             "";
    }

    /**
     * Resolves a character encoding's name to a canonical form, using rules more
     * suitable for email.
     * @param name A string naming a character encoding. Can be null. Uses a
     * modified version of the rules in the Encoding Standard to better
     * conform, in some cases, to email standards like MIME. In addition to
     * the encodings mentioned in ResolveAlias, the following additional
     * encodings are supported:. <ul> <li> {@code us-ascii} - ASCII
     * single-byte encoding, rather than an alias to {@code windows-1252},
     * as specified in the Encoding Standard.</li> <li> {@code iso-8859-1} -
     * Latin-1 single-byte encoding, rather than an alias to {@code
     * windows-1252}, as specified in the Encoding Standard.</li> <li> {@code
     * utf-7} - UTF-7 (7-bit universal character set).</li></ul>.
     * @return A standardized name for the encoding. Returns the empty string if
     * {@code name} is null or empty, or if the encoding name is
     * unsupported.
     */
    public static String ResolveAliasForEmail(String name) {
      if (((name) == null || (name).length() == 0)) {
        return "";
      }
      name = TrimAsciiWhite(name);
      name = ToLowerCaseAscii(name);
      if (name.equals("utf-8") || name.equals("iso-8859-1")) {
        return name;
      }
      if (name.equals("us-ascii") || name.equals("ascii") ||
        name.equals("ansi_x3.4-1968")) {
        // DEVIATION: "ascii" is not an IANA-registered name,
        // but occurs quite frequently
        return "us-ascii";
      }
      if (charsetAliases.containsKey(name)) {
        return charsetAliases.get(name);
      }
      if (name.equals("iso-2022-jp-2")) {
        // NOTE: Treat as the same as iso-2022-jp
        return "iso-2022-jp";
      }
      if (name.equals("utf-7") || name.equals("unicode-1-1-utf-7")) {
        return "utf-7";
      }
      if (name.length() > 9 && name.substring(0,9).equals("iso-8859-")) {
        // NOTE: For conformance to MIME, treat unknown iso-8859-* encodings
        // as ASCII
        return "us-ascii";
      }
      return "";
    }

    /**
     * Converts a text string to a byte array encoded in a given character
     * encoding. When reading the string, any unpaired surrogate characters
     * are replaced with the replacement character (U + FFFD), and when
     * writing to the byte array, any characters that can't be encoded are
     * replaced with the byte 0x3f (the question mark character). <p>In the
     * .NET implementation, this method is implemented as an extension
     * method to any object implementing ICharacterEncoding and can be
     * called as follows: <code>encoding.StringToBytes(str)</code>. If the
     * object's class already has a StringToBytes method with the same
     * parameters, that method takes precedence over this extension
     * method.</p>
     * @param encoding An object that implements a character encoding.
     * @param str A string to be encoded into a byte array.
     * @return A byte array containing the string encoded in the given text
     * encoding.
     * @throws NullPointerException The parameter {@code encoding} is null.
     */
    public static byte[] StringToBytes(
      ICharacterEncoding encoding,
      String str) {
      if (encoding == null) {
        throw new NullPointerException("encoding");
      }
      return StringToBytes(encoding.GetEncoder(), str);
    }

    /**
     * Converts a text string to a byte array using the given character encoder.
     * When reading the string, any unpaired surrogate characters are
     * replaced with the replacement character (U + FFFD), and when writing
     * to the byte array, any characters that can't be encoded are replaced
     * with the byte 0x3f (the question mark character). <p>In the .NET
     * implementation, this method is implemented as an extension method to
     * any object implementing ICharacterEncoder and can be called as
     * follows: <code>encoder.StringToBytes(str)</code>. If the object's class
     * already has a StringToBytes method with the same parameters, that
     * method takes precedence over this extension method.</p>
     * @param encoder An object that implements a character encoder.
     * @param str A text string to encode into a byte array.
     * @return A byte array.
     * @throws NullPointerException The parameter {@code encoder} or {@code str}
     * is null.
     */
    public static byte[] StringToBytes(
      ICharacterEncoder encoder,
      String str) {
      if (encoder == null) {
        throw new NullPointerException("encoder");
      }
      if (str == null) {
        throw new NullPointerException("str");
      }
      return EncodeToBytes(
          new StringCharacterInput(str),
          encoder);
    }

    /**
     * Converts a text string to a character input. The resulting input can then be
     * used to encode the text to bytes, or to read the string code point by
     * code point, among other things. When reading the string, any unpaired
     * surrogate characters are replaced with the replacement character (U +
     * FFFD). <p>In the .NET implementation, this method is implemented as
     * an extension method to any string object and can be called as
     * follows: <code>str.StringToInput(offset, length)</code>. If the object's
     * class already has a StringToInput method with the same parameters,
     * that method takes precedence over this extension method.</p>
     * @param str A string object.
     * @return An ICharacterInput object.
     * @throws NullPointerException The parameter {@code str} is null.
     */
    public static ICharacterInput StringToInput(String str) {
      if (str == null) {
        throw new NullPointerException("str");
      }
      return StringToInput(str, 0, str.length());
    }

    /**
     * Converts a portion of a text string to a character input. The resulting
     * input can then be used to encode the text to bytes, or to read the
     * string code point by code point, among other things. When reading the
     * string, any unpaired surrogate characters are replaced with the
     * replacement character (U + FFFD). <p>In the .NET implementation, this
     * method is implemented as an extension method to any string object and
     * can be called as follows: <code>str.StringToInput(offset, length)</code>.
     * If the object's class already has a StringToInput method with the
     * same parameters, that method takes precedence over this extension
     * method.</p>
     * @param str A string object.
     * @param offset A zero-based index showing where the desired portion of {@code
     * str} begins.
     * @param length The length, in code units, of the desired portion of {@code
     * str} (but not more than {@code str} 's length).
     * @return An ICharacterInput object.
     * @throws NullPointerException The parameter {@code str} is null.
     * @throws IllegalArgumentException Either {@code offset} or {@code length} is less
     * than 0 or greater than {@code str} 's length, or {@code str} 's
     * length minus {@code offset} is less than {@code length}.
     */
    public static ICharacterInput StringToInput(
String str,
int offset,
int length) {
      if (str == null) {
        throw new NullPointerException("str");
      }
      if (offset < 0) {
        throw new IllegalArgumentException("offset (" + offset +
          ") is less than " + 0);
      }
      if (offset > str.length()) {
        throw new IllegalArgumentException("offset (" + offset +
          ") is more than " + str.length());
      }
      if (length < 0) {
        throw new IllegalArgumentException("length (" + length +
          ") is less than " + 0);
      }
      if (length > str.length()) {
        throw new IllegalArgumentException("length (" + length +
          ") is more than " + str.length());
      }
      if (str.length() - offset < length) {
        throw new IllegalArgumentException("str's length minus " + offset + " (" +
          (str.length() - offset) + ") is less than " + length);
      }
      return new StringCharacterInput(str, offset, length);
    }

    private static Map<String, String> CreateAliasMap() {
      HashMap<String, String> aliases = new HashMap<String, String>();
      aliases.put("unicode-1-1-utf-8","utf-8");
      aliases.put("utf-8","utf-8");
      aliases.put("utf8","utf-8");
      aliases.put("866","ibm866");
      aliases.put("cp866","ibm866");
      aliases.put("csibm866","ibm866");
      aliases.put("ibm866","ibm866");
      aliases.put("csisolatin2","iso-8859-2");
      aliases.put("iso-8859-2","iso-8859-2");
      aliases.put("iso-ir-101","iso-8859-2");
      aliases.put("iso8859-2","iso-8859-2");
      aliases.put("iso88592","iso-8859-2");
      aliases.put("iso_8859-2","iso-8859-2");
      aliases.put("iso_8859-2:1987","iso-8859-2");
      aliases.put("l2","iso-8859-2");
      aliases.put("latin2","iso-8859-2");
      aliases.put("csisolatin3","iso-8859-3");
      aliases.put("iso-8859-3","iso-8859-3");
      aliases.put("iso-ir-109","iso-8859-3");
      aliases.put("iso8859-3","iso-8859-3");
      aliases.put("iso88593","iso-8859-3");
      aliases.put("iso_8859-3","iso-8859-3");
      aliases.put("iso_8859-3:1988","iso-8859-3");
      aliases.put("l3","iso-8859-3");
      aliases.put("latin3","iso-8859-3");
      aliases.put("csisolatin4","iso-8859-4");
      aliases.put("iso-8859-4","iso-8859-4");
      aliases.put("iso-ir-110","iso-8859-4");
      aliases.put("iso8859-4","iso-8859-4");
      aliases.put("iso88594","iso-8859-4");
      aliases.put("iso_8859-4","iso-8859-4");
      aliases.put("iso_8859-4:1988","iso-8859-4");
      aliases.put("l4","iso-8859-4");
      aliases.put("latin4","iso-8859-4");
      aliases.put("csisolatincyrillic","iso-8859-5");
      aliases.put("cyrillic","iso-8859-5");
      aliases.put("iso-8859-5","iso-8859-5");
      aliases.put("iso-ir-144","iso-8859-5");
      aliases.put("iso8859-5","iso-8859-5");
      aliases.put("iso88595","iso-8859-5");
      aliases.put("iso_8859-5","iso-8859-5");
      aliases.put("iso_8859-5:1988","iso-8859-5");
      aliases.put("arabic","iso-8859-6");
      aliases.put("asmo-708","iso-8859-6");
      aliases.put("csiso88596e","iso-8859-6");
      aliases.put("csiso88596i","iso-8859-6");
      aliases.put("csisolatinarabic","iso-8859-6");
      aliases.put("ecma-114","iso-8859-6");
      aliases.put("iso-8859-6","iso-8859-6");
      aliases.put("iso-8859-6-e","iso-8859-6");
      aliases.put("iso-8859-6-i","iso-8859-6");
      aliases.put("iso-ir-127","iso-8859-6");
      aliases.put("iso8859-6","iso-8859-6");
      aliases.put("iso88596","iso-8859-6");
      aliases.put("iso_8859-6","iso-8859-6");
      aliases.put("iso_8859-6:1987","iso-8859-6");
      aliases.put("csisolatingreek","iso-8859-7");
      aliases.put("ecma-118","iso-8859-7");
      aliases.put("elot_928","iso-8859-7");
      aliases.put("greek","iso-8859-7");
      aliases.put("greek8","iso-8859-7");
      aliases.put("iso-8859-7","iso-8859-7");
      aliases.put("iso-ir-126","iso-8859-7");
      aliases.put("iso8859-7","iso-8859-7");
      aliases.put("iso88597","iso-8859-7");
      aliases.put("iso_8859-7","iso-8859-7");
      aliases.put("iso_8859-7:1987","iso-8859-7");
      aliases.put("sun_eu_greek","iso-8859-7");
      aliases.put("csiso88598e","iso-8859-8");
      aliases.put("csisolatinhebrew","iso-8859-8");
      aliases.put("hebrew","iso-8859-8");
      aliases.put("iso-8859-8","iso-8859-8");
      aliases.put("iso-8859-8-e","iso-8859-8");
      aliases.put("iso-ir-138","iso-8859-8");
      aliases.put("iso8859-8","iso-8859-8");
      aliases.put("iso88598","iso-8859-8");
      aliases.put("iso_8859-8","iso-8859-8");
      aliases.put("iso_8859-8:1988","iso-8859-8");
      aliases.put("visual","iso-8859-8");
      aliases.put("csiso88598i","iso-8859-8-i");
      aliases.put("iso-8859-8-i","iso-8859-8-i");
      aliases.put("logical","iso-8859-8-i");
      aliases.put("csisolatin6","iso-8859-10");
      aliases.put("iso-8859-10","iso-8859-10");
      aliases.put("iso-ir-157","iso-8859-10");
      aliases.put("iso8859-10","iso-8859-10");
      aliases.put("iso885910","iso-8859-10");
      aliases.put("l6","iso-8859-10");
      aliases.put("latin6","iso-8859-10");
      aliases.put("iso-8859-13","iso-8859-13");
      aliases.put("iso8859-13","iso-8859-13");
      aliases.put("iso885913","iso-8859-13");
      aliases.put("iso-8859-14","iso-8859-14");
      aliases.put("iso8859-14","iso-8859-14");
      aliases.put("iso885914","iso-8859-14");
      aliases.put("csisolatin9","iso-8859-15");
      aliases.put("iso-8859-15","iso-8859-15");
      aliases.put("iso8859-15","iso-8859-15");
      aliases.put("iso885915","iso-8859-15");
      aliases.put("iso_8859-15","iso-8859-15");
      aliases.put("l9","iso-8859-15");
      aliases.put("iso-8859-16","iso-8859-16");
      aliases.put("cskoi8r","koi8-r");
      aliases.put("koi","koi8-r");
      aliases.put("koi8","koi8-r");
      aliases.put("koi8-r","koi8-r");
      aliases.put("koi8_r","koi8-r");
      aliases.put("koi8-ru","koi8-u");
      aliases.put("koi8-u","koi8-u");
      aliases.put("csmacintosh","macintosh");
      aliases.put("mac","macintosh");
      aliases.put("macintosh","macintosh");
      aliases.put("x-mac-roman","macintosh");
      aliases.put("dos-874","windows-874");
      aliases.put("iso-8859-11","windows-874");
      aliases.put("iso8859-11","windows-874");
      aliases.put("iso885911","windows-874");
      aliases.put("tis-620","windows-874");
      aliases.put("windows-874","windows-874");
      aliases.put("cp1250","windows-1250");
      aliases.put("windows-1250","windows-1250");
      aliases.put("x-cp1250","windows-1250");
      aliases.put("cp1251","windows-1251");
      aliases.put("windows-1251","windows-1251");
      aliases.put("x-cp1251","windows-1251");
      aliases.put("ansi_x3.4-1968","windows-1252");
      aliases.put("ascii","windows-1252");
      aliases.put("cp1252","windows-1252");
      aliases.put("cp819","windows-1252");
      aliases.put("csisolatin1","windows-1252");
      aliases.put("ibm819","windows-1252");
      aliases.put("iso-8859-1","windows-1252");
      aliases.put("iso-ir-100","windows-1252");
      aliases.put("iso8859-1","windows-1252");
      aliases.put("iso88591","windows-1252");
      aliases.put("iso_8859-1","windows-1252");
      aliases.put("iso_8859-1:1987","windows-1252");
      aliases.put("l1","windows-1252");
      aliases.put("latin1","windows-1252");
      aliases.put("us-ascii","windows-1252");
      aliases.put("windows-1252","windows-1252");
      aliases.put("x-cp1252","windows-1252");
      aliases.put("cp1253","windows-1253");
      aliases.put("windows-1253","windows-1253");
      aliases.put("x-cp1253","windows-1253");
      aliases.put("cp1254","windows-1254");
      aliases.put("csisolatin5","windows-1254");
      aliases.put("iso-8859-9","windows-1254");
      aliases.put("iso-ir-148","windows-1254");
      aliases.put("iso8859-9","windows-1254");
      aliases.put("iso88599","windows-1254");
      aliases.put("iso_8859-9","windows-1254");
      aliases.put("iso_8859-9:1989","windows-1254");
      aliases.put("l5","windows-1254");
      aliases.put("latin5","windows-1254");
      aliases.put("windows-1254","windows-1254");
      aliases.put("x-cp1254","windows-1254");
      aliases.put("cp1255","windows-1255");
      aliases.put("windows-1255","windows-1255");
      aliases.put("x-cp1255","windows-1255");
      aliases.put("cp1256","windows-1256");
      aliases.put("windows-1256","windows-1256");
      aliases.put("x-cp1256","windows-1256");
      aliases.put("cp1257","windows-1257");
      aliases.put("windows-1257","windows-1257");
      aliases.put("x-cp1257","windows-1257");
      aliases.put("cp1258","windows-1258");
      aliases.put("windows-1258","windows-1258");
      aliases.put("x-cp1258","windows-1258");
      aliases.put("x-mac-cyrillic","x-mac-cyrillic");
      aliases.put("x-mac-ukrainian","x-mac-cyrillic");
      aliases.put("chinese","gbk");
      aliases.put("csgb2312","gbk");
      aliases.put("csiso58gb231280","gbk");
      aliases.put("gb2312","gbk");
      aliases.put("gb_2312","gbk");
      aliases.put("gb_2312-80","gbk");
      aliases.put("gbk","gbk");
      aliases.put("iso-ir-58","gbk");
      aliases.put("x-gbk","gbk");
      aliases.put("gb18030","gb18030");
      aliases.put("big5","big5");
      aliases.put("big5-hkscs","big5");
      aliases.put("cn-big5","big5");
      aliases.put("csbig5","big5");
      aliases.put("x-x-big5","big5");
      aliases.put("cseucpkdfmtjapanese","euc-jp");
      aliases.put("euc-jp","euc-jp");
      aliases.put("x-euc-jp","euc-jp");
      aliases.put("csiso2022jp","iso-2022-jp");
      aliases.put("iso-2022-jp","iso-2022-jp");
      aliases.put("csshiftjis","shift_jis");
      aliases.put("ms932","shift_jis");
      aliases.put("ms_kanji","shift_jis");
      aliases.put("shift-jis","shift_jis");
      aliases.put("shift_jis","shift_jis");
      aliases.put("sjis","shift_jis");
      aliases.put("windows-31j","shift_jis");
      aliases.put("x-sjis","shift_jis");
      aliases.put("cseuckr","euc-kr");
      aliases.put("csksc56011987","euc-kr");
      aliases.put("euc-kr","euc-kr");
      aliases.put("iso-ir-149","euc-kr");
      aliases.put("korean","euc-kr");
      aliases.put("ks_c_5601-1987","euc-kr");
      aliases.put("ks_c_5601-1989","euc-kr");
      aliases.put("ksc5601","euc-kr");
      aliases.put("ksc_5601","euc-kr");
      aliases.put("windows-949","euc-kr");
      aliases.put("csiso2022kr","replacement");
      aliases.put("hz-gb-2312","replacement");
      aliases.put("iso-2022-cn","replacement");
      aliases.put("iso-2022-cn-ext","replacement");
      aliases.put("iso-2022-kr","replacement");
      aliases.put("utf-16be","utf-16be");
      aliases.put("utf-16","utf-16le");
      aliases.put("utf-16le","utf-16le");
      aliases.put("x-user-defined","x-user-defined");
      return aliases;
    }

    /**
     * Returns a string with the basic upper-case letters A to Z (U + 0041 to U +
     * 005A) converted to lower-case. Other characters remain unchanged.
     * @param str A string.
     * @return The converted string, or null if {@code str} is null.
     */
    private static String ToLowerCaseAscii(String str) {
      if (str == null) {
        return null;
      }
      int len = str.length();
      char c = (char)0;
      boolean hasUpperCase = false;
      for (int i = 0; i < len; ++i) {
        c = str.charAt(i);
        if (c >= 'A' && c <= 'Z') {
          hasUpperCase = true;
          break;
        }
      }
      if (!hasUpperCase) {
        return str;
      }
      StringBuilder builder = new StringBuilder();
      for (int i = 0; i < len; ++i) {
        c = str.charAt(i);
        if (c >= 'A' && c <= 'Z') {
          builder.append((char)(c + 0x20));
        } else {
          builder.append(c);
        }
      }
      return builder.toString();
    }

    private static String TrimAsciiWhite(String str) {
      return ((str) == null || (str).length() == 0) ? str :
        TrimAsciiWhiteLeft(TrimAsciiWhiteRight(str));
    }

    private static String TrimAsciiWhiteLeft(String str) {
      if (((str) == null || (str).length() == 0)) {
        return str;
      }
      int index = 0;
      int valueSLength = str.length();
      while (index < valueSLength) {
        char c = str.charAt(index);
        if (c != 0x09 && c != 0x20 && c != 0x0c && c != 0x0d && c != 0x0a) {
          break;
        }
        ++index;
      }
      return (index == valueSLength) ? "" : ((index == 0) ? str :
        str.substring(index));
    }

    private static String TrimAsciiWhiteRight(String str) {
      if (((str) == null || (str).length() == 0)) {
        return str;
      }
      int index = str.length() - 1;
      while (index >= 0) {
        char c = str.charAt(index);
        if (c != 0x09 && c != 0x20 && c != 0x0c && c != 0x0d && c != 0x0a) {
          return str.substring(0, index + 1);
        }
        --index;
      }
      return "";
    }

    private static class DecoderToInputClass implements ICharacterInput {
      private final IByteReader stream;
      private final ICharacterDecoder reader;

      public DecoderToInputClass(ICharacterDecoder reader, IByteReader stream) {
        this.reader = reader;
        this.stream = stream;
      }

    /**
     * This is an internal method.
     * @return A 32-bit signed integer.
     */
      public int ReadChar() {
        int c = this.reader.ReadChar(this.stream);
        return (c == -2) ? 0xfffd : c;
      }

    /**
     * This is an internal method.
     * @param buffer An array of 32-bit unsigned integers.
     * @param offset A zero-based index showing where the desired portion of {@code
     * buffer} begins.
     * @param length The number of elements in the desired portion of {@code
     * buffer} (but not more than {@code buffer} 's length).
     * @return A 32-bit signed integer.
     * @throws NullPointerException The parameter {@code buffer} is null.
     * @throws IllegalArgumentException Either {@code offset} or {@code length} is less
     * than 0 or greater than {@code buffer} 's length, or {@code buffer} 's
     * length minus {@code offset} is less than {@code length}.
     */
      public int Read(int[] buffer, int offset, int length) {
        if (buffer == null) {
          throw new NullPointerException("buffer");
        }
        if (offset < 0) {
          throw new IllegalArgumentException("offset (" + offset +
            ") is less than " + 0);
        }
        if (offset > buffer.length) {
          throw new IllegalArgumentException("offset (" + offset + ") is more than " +
            buffer.length);
        }
        if (length < 0) {
          throw new IllegalArgumentException("length (" + length +
            ") is less than " + 0);
        }
        if (length > buffer.length) {
          throw new IllegalArgumentException("length (" + length + ") is more than " +
            buffer.length);
        }
        if (buffer.length - offset < length) {
          throw new IllegalArgumentException("buffer's length minus " + offset + " (" +
            (buffer.length - offset) + ") is less than " + length);
        }
        int count = 0;
        for (int i = 0; i < length; ++i) {
          int c = this.ReadChar();
          if (c == -1) {
            break;
          }
          buffer[offset] = c;
          ++count;
          ++offset;
        }
        return count;
      }
    }
  }
