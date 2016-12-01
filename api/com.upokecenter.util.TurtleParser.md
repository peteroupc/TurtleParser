# com.upokecenter.util.TurtleParser

    public class TurtleParser extends Object implements IRDFParser

Not documented yet.

## Methods

* `TurtleParser(PeterO.IByteReader stream) com.upokecenter.Rdf.getTurtleParser()`<br>
 Initializes a new instance of the com.upokecenter.Rdf.getTurtleParser()
 class.
* `TurtleParser(PeterO.IByteReader stream,
            String baseURI) com.upokecenter.Rdf.getTurtleParser()`<br>
 Initializes a new instance of the com.upokecenter.Rdf.getTurtleParser()
 class.
* `<any> Parse()`<br>
 Not documented yet.

## Constructors

* `TurtleParser(PeterO.IByteReader stream) com.upokecenter.Rdf.getTurtleParser()`<br>
 Initializes a new instance of the com.upokecenter.Rdf.getTurtleParser()
 class.
* `TurtleParser(PeterO.IByteReader stream,
            String baseURI) com.upokecenter.Rdf.getTurtleParser()`<br>
 Initializes a new instance of the com.upokecenter.Rdf.getTurtleParser()
 class.

## Method Details

### TurtleParser
    public TurtleParser(PeterO.IByteReader stream)
Initializes a new instance of the <code>com.upokecenter.Rdf.getTurtleParser()</code>
 class.

**Parameters:**

* <code>stream</code> - A PeterO.IByteReader object.

### TurtleParser
    public TurtleParser(PeterO.IByteReader stream, String baseURI)
Initializes a new instance of the <code>com.upokecenter.Rdf.getTurtleParser()</code>
 class.

**Parameters:**

* <code>stream</code> - A PeterO.IByteReader object.

* <code>baseURI</code> - A text string.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>stream</code> or <code>baseURI</code> is null.

### Parse
    public <any> Parse()
Not documented yet.

**Specified by:**

* <code>Parse</code>&nbsp;in interface&nbsp;<code>IRDFParser</code>

**Returns:**

* An ISet(RDFTriple) object.

### Parse
    public <any> Parse()
Not documented yet.

**Specified by:**

* <code>Parse</code>&nbsp;in interface&nbsp;<code>IRDFParser</code>

**Returns:**

* An ISet(RDFTriple) object.
