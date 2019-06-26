# com.upokecenter.rdf.TurtleParser

    public class TurtleParser extends java.lang.Object implements IRDFParser

Not documented yet.

## Methods

* `TurtleParser​(com.upokecenter.util.IByteReader stream) TurtleParser`<br>
 Initializes a new instance of the TurtleParser
 class.
* `TurtleParser​(com.upokecenter.util.IByteReader stream,
            java.lang.String baseURI) TurtleParser`<br>
 Initializes a new instance of the TurtleParser
 class.
* `TurtleParser​(java.lang.String str) TurtleParser`<br>
 Initializes a new instance of the TurtleParser
 class.
* `TurtleParser​(java.lang.String str,
            java.lang.String baseURI) TurtleParser`<br>
 Initializes a new instance of the TurtleParser
 class.
* `java.util.Set<RDFTriple> Parse()`<br>
 Not documented yet.

## Constructors

* `TurtleParser​(com.upokecenter.util.IByteReader stream) TurtleParser`<br>
 Initializes a new instance of the TurtleParser
 class.
* `TurtleParser​(com.upokecenter.util.IByteReader stream,
            java.lang.String baseURI) TurtleParser`<br>
 Initializes a new instance of the TurtleParser
 class.
* `TurtleParser​(java.lang.String str) TurtleParser`<br>
 Initializes a new instance of the TurtleParser
 class.
* `TurtleParser​(java.lang.String str,
            java.lang.String baseURI) TurtleParser`<br>
 Initializes a new instance of the TurtleParser
 class.

## Method Details

### TurtleParser
    public TurtleParser​(com.upokecenter.util.IByteReader stream)
Initializes a new instance of the <code>TurtleParser</code>
 class.

**Parameters:**

* <code>stream</code> - A PeterO.IByteReader object.

### TurtleParser
    public TurtleParser​(com.upokecenter.util.IByteReader stream, java.lang.String baseURI)
Initializes a new instance of the <code>TurtleParser</code>
 class.

**Parameters:**

* <code>stream</code> - A PeterO.IByteReader object.

* <code>baseURI</code> - The parameter <code>baseURI</code> is a text string.

**Throws:**

* <code>java.lang.NullPointerException</code> - The parameter <code>stream</code> or <code>
 baseURI</code> is null.

* <code>java.lang.IllegalArgumentException</code> - BaseURI.

### TurtleParser
    public TurtleParser​(java.lang.String str)
Initializes a new instance of the <code>TurtleParser</code>
 class.

**Parameters:**

* <code>str</code> - The parameter <code>str</code> is a text string.

### TurtleParser
    public TurtleParser​(java.lang.String str, java.lang.String baseURI)
Initializes a new instance of the <code>TurtleParser</code>
 class.

**Parameters:**

* <code>str</code> - The parameter <code>str</code> is a text string.

* <code>baseURI</code> - The parameter <code>baseURI</code> is a text string.

**Throws:**

* <code>java.lang.NullPointerException</code> - The parameter <code>str</code> or <code>
 baseURI</code> is null.

* <code>java.lang.IllegalArgumentException</code> - BaseURI.

### Parse
    public java.util.Set<RDFTriple> Parse()
Not documented yet.

**Specified by:**

* <code>Parse</code> in interface <code>IRDFParser</code>

**Returns:**

* An ISet(RDFTriple) object.

### Parse
    public java.util.Set<RDFTriple> Parse()
Not documented yet.

**Specified by:**

* <code>Parse</code> in interface <code>IRDFParser</code>

**Returns:**

* An ISet(RDFTriple) object.
