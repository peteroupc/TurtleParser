# com.upokecenter.util.TurtleParser

    public class TurtleParser extends Object implements IRDFParser

Not documented yet.

## Methods

* `TurtleParser(com.upokecenter.util.IByteReader stream)`<br>
* `TurtleParser(com.upokecenter.util.IByteReader stream,
            String baseURI)`<br>
* `TurtleParser(String str) com.upokecenter.Rdf.getTurtleParser()`<br>
 Initializes a new instance of the com.upokecenter.Rdf.getTurtleParser()
 class.
* `TurtleParser(String str,
            String baseURI) com.upokecenter.Rdf.getTurtleParser()`<br>
 Initializes a new instance of the com.upokecenter.Rdf.getTurtleParser()
 class.
* `Set<RDFTriple> Parse()`<br>
 Not documented yet.

## Constructors

* `TurtleParser(com.upokecenter.util.IByteReader stream)`<br>
* `TurtleParser(com.upokecenter.util.IByteReader stream,
            String baseURI)`<br>
* `TurtleParser(String str) com.upokecenter.Rdf.getTurtleParser()`<br>
 Initializes a new instance of the com.upokecenter.Rdf.getTurtleParser()
 class.
* `TurtleParser(String str,
            String baseURI) com.upokecenter.Rdf.getTurtleParser()`<br>
 Initializes a new instance of the com.upokecenter.Rdf.getTurtleParser()
 class.

## Method Details

### TurtleParser
    public TurtleParser(com.upokecenter.util.IByteReader stream)
### TurtleParser
    public TurtleParser(com.upokecenter.util.IByteReader stream, String baseURI)
### TurtleParser
    public TurtleParser(String str)
Initializes a new instance of the <code>com.upokecenter.Rdf.getTurtleParser()</code>
 class.

**Parameters:**

* <code>str</code> - A text string.

### TurtleParser
    public TurtleParser(String str, String baseURI)
Initializes a new instance of the <code>com.upokecenter.Rdf.getTurtleParser()</code>
 class.

**Parameters:**

* <code>str</code> - A text string.

* <code>baseURI</code> - Another string object.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>str</code> or <code>baseURI</code> is null.

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
