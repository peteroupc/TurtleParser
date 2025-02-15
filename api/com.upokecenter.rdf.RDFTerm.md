# com.upokecenter.rdf.RDFTerm

    public final class RDFTerm extends Object

Not documented yet.

## Fields

* `static final RDFTerm A`<br>
 Predicate for RDF types.

* `static final int BLANK`<br>
 Type value for a blank node.

* `static final RDFTerm FALSE`<br>
 object for false.

* `static final RDFTerm FIRST`<br>
 Predicate for the first object in a list.

* `static final int IRI`<br>
 Type value for an IRI (Internationalized Resource Identifier.).

* `static final int LANGSTRING`<br>
 Type value for a string with a language tag.

* `static final RDFTerm NIL`<br>
 object for nil, the end of a list, or an empty list.

* `static final RDFTerm REST`<br>
 Predicate for the remaining objects in a list.

* `static final RDFTerm TRUE`<br>
 object for true.

* `static final int TYPEDSTRING`<br>
 Type value for a piece of data serialized to a string.

## Methods

* `final boolean equals(Object obj)`<br>
 Not documented yet.

* `static RDFTerm FromBlankNode(String name)`<br>
 Not documented yet.

* `static RDFTerm FromIRI(String iri)`<br>
 Not documented yet.

* `static RDFTerm FromLangString(String str,
 String languageTag)`<br>
 Not documented yet.

* `static RDFTerm FromTypedString(String str)`<br>
 Not documented yet.

* `static RDFTerm FromTypedString(String str,
 String iri)`<br>
 Not documented yet.

* `int GetKind()`<br>
 Not documented yet.

* `String GetTypeOrLanguage()`<br>
 Gets the language tag or data type for this RDF literal.

* `String GetValue()`<br>
 Gets the IRI, blank node identifier, or lexical form of an RDF literal.

* `final int hashCode()`<br>
 Not documented yet.

* `boolean IsBlank()`<br>
 Gets a value indicating whether this term is a blank node.

* `boolean IsIRI(String str)`<br>
 Not documented yet.

* `boolean IsOrdinaryString()`<br>
 Not documented yet.

* `final String toString()`<br>
 Gets a string representation of this RDF term in N-Triples format.

## Field Details

### BLANK

    public static final int BLANK

Type value for a blank node.

### IRI

    public static final int IRI

Type value for an IRI (Internationalized Resource Identifier.).

### LANGSTRING

    public static final int LANGSTRING

Type value for a string with a language tag.

### TYPEDSTRING

    public static final int TYPEDSTRING

Type value for a piece of data serialized to a string.

### A

    public static final RDFTerm A

Predicate for RDF types.

### FIRST

    public static final RDFTerm FIRST

Predicate for the first object in a list.

### NIL

    public static final RDFTerm NIL

object for nil, the end of a list, or an empty list.

### REST

    public static final RDFTerm REST

Predicate for the remaining objects in a list.

### FALSE

    public static final RDFTerm FALSE

object for false.

### TRUE

    public static final RDFTerm TRUE

object for true.

## Method Details

### FromBlankNode

    public static RDFTerm FromBlankNode(String name)

Not documented yet.

**Parameters:**

* <code>name</code> - The parameter <code>name</code> is a text string.

**Returns:**

* A RDFTerm object.

**Throws:**

* <code>IllegalArgumentException</code> - Name is empty.

* <code>NullPointerException</code> - The parameter <code>name</code> is null.

### FromIRI

    public static RDFTerm FromIRI(String iri)

Not documented yet.

**Parameters:**

* <code>iri</code> - The parameter <code>iri</code> is a text string.

**Returns:**

* A RDFTerm object.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>iri</code> is null.

### FromLangString

    public static RDFTerm FromLangString(String str, String languageTag)

Not documented yet.

**Parameters:**

* <code>str</code> - The parameter <code>str</code> is a text string.

* <code>languageTag</code> - The parameter <code>languageTag</code> is a text string.

**Returns:**

* A RDFTerm object.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>str</code> or <code>
 languageTag</code> is null.

* <code>IllegalArgumentException</code> - LanguageTag is empty.

### FromTypedString

    public static RDFTerm FromTypedString(String str)

Not documented yet.

**Parameters:**

* <code>str</code> - The parameter <code>str</code> is a text string.

**Returns:**

* A RDFTerm object.

### FromTypedString

    public static RDFTerm FromTypedString(String str, String iri)

Not documented yet.

**Parameters:**

* <code>str</code> - The parameter <code>str</code> is a text string.

* <code>iri</code> - The parameter <code>iri</code> is a text string.

**Returns:**

* A RDFTerm object.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>str</code> or <code>iri</code> is
 null.

* <code>IllegalArgumentException</code> - Iri is empty.

### equals

    public final boolean equals(Object obj)

Not documented yet.

**Overrides:**

* <code>equals</code> in class <code>Object</code>

**Parameters:**

* <code>obj</code> - The parameter <code>obj</code> is a object object.

**Returns:**

* The return value is not documented yet.

### GetKind

    public int GetKind()

Not documented yet.

**Returns:**

* A 32-bit signed integer.

### GetTypeOrLanguage

    public String GetTypeOrLanguage()

Gets the language tag or data type for this RDF literal.

**Returns:**

* A text string.

### GetValue

    public String GetValue()

Gets the IRI, blank node identifier, or lexical form of an RDF literal.

**Returns:**

* A text string.

### hashCode

    public final int hashCode()

Not documented yet.

**Overrides:**

* <code>hashCode</code> in class <code>Object</code>

**Returns:**

* The return value is not documented yet.

### IsBlank

    public boolean IsBlank()

Gets a value indicating whether this term is a blank node.

**Returns:**

* Either <code>true</code> or <code>false</code>.

### IsIRI

    public boolean IsIRI(String str)

Not documented yet.

**Parameters:**

* <code>str</code> - The parameter <code>str</code> is a text string.

**Returns:**

* Either <code>true</code> or <code>false</code>.

### IsOrdinaryString

    public boolean IsOrdinaryString()

Not documented yet.

**Returns:**

* Either <code>true</code> or <code>false</code>.

### toString

    public final String toString()

Gets a string representation of this RDF term in N-Triples format. The
 string will not end in a line break.

**Overrides:**

* <code>toString</code> in class <code>Object</code>

**Returns:**

* A string representation of this object.
