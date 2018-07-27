# com.upokecenter.util.NTriplesParser

    public final class NTriplesParser extends Object implements IRDFParser

Not documented yet.

## Methods

* `NTriplesParser​(com.upokecenter.util.IByteReader stream)`<br>
 Initializes a new instance of the {@link com.upokecenter.Rdf.getNTriplesParser()}
 class.
* `NTriplesParser​(String str)`<br>
 Initializes a new instance of the {@link com.upokecenter.Rdf.getNTriplesParser()}
 class.
* `static boolean isAsciiChar​(int c,
           String asciiChars)`<br>
 Not documented yet.
* `Set<RDFTriple> Parse()`<br>
 Not documented yet.

## Constructors

* `NTriplesParser​(com.upokecenter.util.IByteReader stream)`<br>
 Initializes a new instance of the {@link com.upokecenter.Rdf.getNTriplesParser()}
 class.
* `NTriplesParser​(String str)`<br>
 Initializes a new instance of the {@link com.upokecenter.Rdf.getNTriplesParser()}
 class.

## Method Details

### NTriplesParser
    public NTriplesParser​(com.upokecenter.util.IByteReader stream)
Initializes a new instance of the {@link com.upokecenter.Rdf.getNTriplesParser()}
 class.

**Parameters:**

* <code>stream</code> - A PeterO.IByteReader object.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>stream</code> is null.

### NTriplesParser
    public NTriplesParser​(String str)
Initializes a new instance of the {@link com.upokecenter.Rdf.getNTriplesParser()}
 class.

**Parameters:**

* <code>str</code> - The parameter <code>str</code> is a text string.

**Throws:**

* <code>NullPointerException</code> - The parameter "stream" is null.

### isAsciiChar
    public static boolean isAsciiChar​(int c, String asciiChars)
Not documented yet.

**Parameters:**

* <code>c</code> - The parameter <code>c</code> is not documented yet.

* <code>asciiChars</code> - The parameter <code>asciiChars</code> is not documented yet.

**Returns:**

* Either <code>true</code> or <code>false</code>.

### Parse
    public Set<RDFTriple> Parse()
Not documented yet.

**Specified by:**

* <code>Parse</code>&nbsp;in interface&nbsp;<code>IRDFParser</code>

**Returns:**

* An ISet(RDFTriple) object.
