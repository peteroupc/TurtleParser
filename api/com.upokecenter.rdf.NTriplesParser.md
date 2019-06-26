# com.upokecenter.rdf.NTriplesParser

    public final class NTriplesParser extends java.lang.Object implements IRDFParser

Not documented yet.

## Methods

* `NTriplesParser​(com.upokecenter.util.IByteReader stream) NTriplesParser`<br>
 Initializes a new instance of the NTriplesParser
 class.
* `NTriplesParser​(java.lang.String str) NTriplesParser`<br>
 Initializes a new instance of the NTriplesParser
 class.
* `static boolean IsAsciiChar​(int c,
           java.lang.String asciiChars)`<br>
 Not documented yet.
* `java.util.Set<RDFTriple> Parse()`<br>
 Not documented yet.

## Constructors

* `NTriplesParser​(com.upokecenter.util.IByteReader stream) NTriplesParser`<br>
 Initializes a new instance of the NTriplesParser
 class.
* `NTriplesParser​(java.lang.String str) NTriplesParser`<br>
 Initializes a new instance of the NTriplesParser
 class.

## Method Details

### NTriplesParser
    public NTriplesParser​(com.upokecenter.util.IByteReader stream)
Initializes a new instance of the <code>NTriplesParser</code>
 class.

**Parameters:**

* <code>stream</code> - A PeterO.IByteReader object.

**Throws:**

* <code>java.lang.NullPointerException</code> - The parameter <code>stream</code> is null.

### NTriplesParser
    public NTriplesParser​(java.lang.String str)
Initializes a new instance of the <code>NTriplesParser</code>
 class.

**Parameters:**

* <code>str</code> - The parameter <code>str</code> is a text string.

**Throws:**

* <code>java.lang.NullPointerException</code> - The parameter "stream" is null.

### IsAsciiChar
    public static boolean IsAsciiChar​(int c, java.lang.String asciiChars)
Not documented yet.

**Parameters:**

* <code>c</code> - The parameter <code>c</code> is not documented yet.

* <code>asciiChars</code> - The parameter <code>asciiChars</code> is not documented yet.

**Returns:**

* Either <code>true</code> or <code>false</code>.

### Parse
    public java.util.Set<RDFTriple> Parse()
Not documented yet.

**Specified by:**

* <code>Parse</code> in interface <code>IRDFParser</code>

**Returns:**

* An ISet(RDFTriple) object.
