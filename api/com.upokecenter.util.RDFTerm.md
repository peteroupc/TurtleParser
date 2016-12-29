# com.upokecenter.util.RDFTerm

    public final class RDFTerm extends Object

Not documented yet.

## Fields

* `static RDFTerm A`<br>
 Predicate for RDF types.
* `static int BLANK`<br>
 Type value for a blank node.
* `static RDFTerm FALSE`<br>
 Object for false.
* `static RDFTerm FIRST`<br>
 Predicate for the first object in a list.
* `static int IRI`<br>
 Type value for an IRI (Internationalized Resource Identifier.).
* `static int LANGSTRING`<br>
 Type value for a string with a language tag.
* `static RDFTerm NIL`<br>
 Object for nil, the end of a list, or an empty list.
* `static RDFTerm REST`<br>
 Predicate for the remaining objects in a list.
* `static RDFTerm TRUE`<br>
 Object for true.
* `static int TYPEDSTRING`<br>
 Type value for a piece of data serialized to a string.

## Methods

* `boolean equals(Object obj)`<br>
 Not documented yet.
* `static RDFTerm fromBlankNode(String name)`<br>
 Not documented yet.
* `static RDFTerm fromIRI(String iri)`<br>
 Not documented yet.
* `static RDFTerm fromLangString(String str,
              String languageTag)`<br>
 Not documented yet.
* `static RDFTerm fromTypedString(String str)`<br>
 Not documented yet.
* `static RDFTerm fromTypedString(String str,
               String iri)`<br>
 Not documented yet.
* `int getKind()`<br>
 Not documented yet.
* `String getTypeOrLanguage()`<br>
 Gets the language tag or data type for this RDF literal.
* `String getValue()`<br>
 Gets the IRI, blank node identifier, or lexical form of an RDF literal.
* `int hashCode()`<br>
 Not documented yet.
* `boolean isBlank()`<br>
 Gets whether this term is a blank node.
* `boolean isIRI(String str)`<br>
 Not documented yet.
* `boolean isOrdinaryString()`<br>
 Not documented yet.
* `String toString()`<br>
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
Object for nil, the end of a list, or an empty list.
### REST
    public static final RDFTerm REST
Predicate for the remaining objects in a list.
### FALSE
    public static final RDFTerm FALSE
Object for false.
### TRUE
    public static final RDFTerm TRUE
Object for true.
## Method Details

### fromBlankNode
    public static RDFTerm fromBlankNode(String name)
Not documented yet.

**Parameters:**

* <code>name</code> - The parameter <code>name</code> is not documented yet.

**Returns:**

* A RDFTerm object.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>name</code> is null.

### fromIRI
    public static RDFTerm fromIRI(String iri)
Not documented yet.

**Parameters:**

* <code>iri</code> - The parameter <code>iri</code> is not documented yet.

**Returns:**

* A RDFTerm object.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>iri</code> is null.

### fromLangString
    public static RDFTerm fromLangString(String str, String languageTag)
Not documented yet.

**Parameters:**

* <code>str</code> - The parameter <code>str</code> is not documented yet.

* <code>languageTag</code> - The parameter <code>languageTag</code> is not documented yet.

**Returns:**

* A RDFTerm object.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>str</code> or <code>languageTag</code> is null.

### fromTypedString
    public static RDFTerm fromTypedString(String str)
Not documented yet.

**Parameters:**

* <code>str</code> - The parameter <code>str</code> is not documented yet.

**Returns:**

* A RDFTerm object.

### fromTypedString
    public static RDFTerm fromTypedString(String str, String iri)
Not documented yet.

**Parameters:**

* <code>str</code> - The parameter <code>str</code> is not documented yet.

* <code>iri</code> - The parameter <code>iri</code> is not documented yet.

**Returns:**

* A RDFTerm object.

**Throws:**

* <code>NullPointerException</code> - The parameter <code>str</code> or <code>iri</code> is null.

### equals
    public final boolean equals(Object obj)
Not documented yet.

**Overrides:**

* <code>equals</code>&nbsp;in class&nbsp;<code>Object</code>

**Parameters:**

* <code>obj</code> - The parameter <code>obj</code> is not documented yet.

**Returns:**

* The return value is not documented yet.

### getKind
    public int getKind()
Not documented yet.

**Returns:**

* A 32-bit signed integer.

### getTypeOrLanguage
    public String getTypeOrLanguage()
Gets the language tag or data type for this RDF literal.

**Returns:**

* A text string.

### getValue
    public String getValue()
Gets the IRI, blank node identifier, or lexical form of an RDF literal.

**Returns:**

* A text string.

### hashCode
    public final int hashCode()
Not documented yet.

**Overrides:**

* <code>hashCode</code>&nbsp;in class&nbsp;<code>Object</code>

**Returns:**

* The return value is not documented yet.

### isBlank
    public boolean isBlank()
Gets whether this term is a blank node.

**Returns:**

* A Boolean object.

### isIRI
    public boolean isIRI(String str)
Not documented yet.

**Parameters:**

* <code>str</code> - The parameter <code>str</code> is not documented yet.

**Returns:**

* A Boolean object.

### isOrdinaryString
    public boolean isOrdinaryString()
Not documented yet.

**Returns:**

* A Boolean object.

### toString
    public final String toString()
Gets a string representation of this RDF term in N-Triples format. The
 string will not end in a line break.

**Overrides:**

* <code>toString</code>&nbsp;in class&nbsp;<code>Object</code>

**Returns:**

* The return value is not documented yet.
