# com.upokecenter.util.TurtleParser

    public class TurtleParser extends Object implements IRDFParser

Not documented yet.

## Methods

* `TurtleParser​(com.upokecenter.util.IByteReader stream)`<br>
 Initializes a new instance of the {@link com.upokecenter.Rdf.getTurtleParser()}
 class.
* `TurtleParser​(com.upokecenter.util.IByteReader stream,
            String baseURI)`<br>
 Initializes a new instance of the {@link com.upokecenter.Rdf.getTurtleParser()}
 class.
* `TurtleParser​(String str)`<br>
 Initializes a new instance of the {@link com.upokecenter.Rdf.getTurtleParser()}
 class.
* `TurtleParser​(String str,
            String baseURI)`<br>
 Initializes a new instance of the {@link com.upokecenter.Rdf.getTurtleParser()}
 class.
* `Set<RDFTriple> Parse()`<br>
 Not documented yet.

## Constructors

* `TurtleParser​(com.upokecenter.util.IByteReader stream)`<br>
 Initializes a new instance of the {@link com.upokecenter.Rdf.getTurtleParser()}
 class.
* `TurtleParser​(com.upokecenter.util.IByteReader stream,
            String baseURI)`<br>
 Initializes a new instance of the {@link com.upokecenter.Rdf.getTurtleParser()}
 class.
* `TurtleParser​(String str)`<br>
 Initializes a new instance of the {@link com.upokecenter.Rdf.getTurtleParser()}
 class.
* `TurtleParser​(String str,
            String baseURI)`<br>
 Initializes a new instance of the {@link com.upokecenter.Rdf.getTurtleParser()}
 class.

## Method Details

### TurtleParser
    public TurtleParser​(com.upokecenter.util.IByteReader stream)
Initializes a new instance of the {@link com.upokecenter.Rdf.getTurtleParser()}
 class.

**Parameters:**

* <code>stream</code> - A PeterO.IByteReader object.

### TurtleParser
    public TurtleParser​(com.upokecenter.util.IByteReader stream, String baseURI)
Initializes a new instance of the {@link com.upokecenter.Rdf.getTurtleParser()}
 class.

**Parameters:**

* <code>stream</code> - A PeterO.IByteReader object.

* <code>baseURI</code> - The parameter <code>baseURI</code> is a text string.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>stream</code> or <code>
 baseURI</code> is null.

* <code>IllegalArgumentException</code> - BaseURI.

### TurtleParser
    public TurtleParser​(String str)
Initializes a new instance of the {@link com.upokecenter.Rdf.getTurtleParser()}
 class.

**Parameters:**

* <code>str</code> - The parameter <code>str</code> is a text string.

### TurtleParser
    public TurtleParser​(String str, String baseURI)
Initializes a new instance of the {@link com.upokecenter.Rdf.getTurtleParser()}
 class.

**Parameters:**

* <code>str</code> - The parameter <code>str</code> is a text string.

* <code>baseURI</code> - The parameter <code>baseURI</code> is a text string.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>str</code> or <code>
 baseURI</code> is null.

* <code>IllegalArgumentException</code> - BaseURI.

### Parse
    public Set<RDFTriple> Parse()
Not documented yet.

**Specified by:**

* <code>Parse</code>&nbsp;in interface&nbsp;<code>IRDFParser</code>

**Returns:**

* An ISet(RDFTriple) object.

### Parse
    public Set<RDFTriple> Parse()
Not documented yet.

**Specified by:**

* <code>Parse</code>&nbsp;in interface&nbsp;<code>IRDFParser</code>

**Returns:**

* An ISet(RDFTriple) object.
