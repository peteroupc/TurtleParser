/*
Written in 2013 by Peter Occil.
Any copyright is dedicated to the Public Domain.
http://creativecommons.org/publicdomain/zero/1.0/

If you like this, you should donate to Peter O.
at: http://upokecenter.dreamhosters.com/articles/donate-now-2/
*/
namespace PeterO.Rdf {
using System;
using System.Text;

public sealed class RDFTerm {
    /// <summary>Type value for a blank node.</summary>
  public const int BLANK = 0;  // type is blank node name, literal is blank

    /// <summary>Type value for an IRI (Internationalized Resource
    /// Identifier.).</summary>
  public const int IRI = 1;  // type is IRI, literal is blank

    /// <summary>Type value for a _string with a language tag.</summary>
  public const int LANGSTRING = 2;  // literal is given

    /// <summary>Type value for a piece of data serialized to a
    /// _string.</summary>
  public const int TYPEDSTRING = 3;  // type is IRI, literal is given

  private static void escapeBlankNode(string str, StringBuilder builder) {
    int length = str.Length;
    string hex="0123456789ABCDEF";
    for (int i = 0; i < length; ++i) {
      int c = str[i];
      if ((c>= 'A' && c<= 'Z') || (c>= 'a' && c<= 'z') ||
          (c>0 && c>= '0' && c<= '9')) {
        builder.Append((char)c);
  } else if ((c & 0xfc00) == 0xd800 && i + 1<length &&
          str[i + 1]>= 0xdc00 && str[i + 1]<= 0xdfff) {
        // Get the Unicode code point for the surrogate pair
        c = 0x10000+(c-0xd800)*0x400+(str[i + 1]-0xdc00);
        builder.Append("U00");
        builder.Append(hex[(c >> 20) & 15]);
        builder.Append(hex[(c >> 16) & 15]);
        builder.Append(hex[(c >> 12) & 15]);
        builder.Append(hex[(c >> 8) & 15]);
        builder.Append(hex[(c >> 4) & 15]);
        builder.Append(hex[(c) & 15]);
        ++i;
      } else {
        builder.Append("u");
        builder.Append(hex[(c >> 12) & 15]);
        builder.Append(hex[(c >> 8) & 15]);
        builder.Append(hex[(c >> 4) & 15]);
        builder.Append(hex[(c) & 15]);
      }
    }
  }

  private static void escapeLanguageTag(string str, StringBuilder builder) {
    int length = str.Length;
    var hyphen = false;
    for (int i = 0; i < length; ++i) {
      int c = str[i];
      if (c>= 'A' && c<= 'Z') {
        builder.Append((char)(c + 0x20));
      } else if (c>= 'a' && c<= 'z') {
        builder.Append((char)c);
      } else if (hyphen && c>= '0' && c<= '9') {
        builder.Append((char)c);
      } else if (c=='-') {
        builder.Append((char)c);
        hyphen = true;
        if (i+1<length && str[i+1]=='-') {
          builder.Append('x');
        }
      } else {
        builder.Append('x');
      }
    }
  }
  private static void escapeString(string str,
      StringBuilder builder, bool uri) {
    int length = str.Length;
    string hex="0123456789ABCDEF";
    for (int i = 0; i < length; ++i) {
      int c = str[i];
      if (c == 0x09) {
        builder.Append("\\t");
      } else if (c == 0x0a) {
        builder.Append("\\n");
      } else if (c == 0x0d) {
        builder.Append("\\r");
      } else if (c == 0x22) {
        builder.Append("\\\"");
      } else if (c == 0x5c) {
        builder.Append("\\\\");
      } else if (uri && c=='>') {
        builder.Append("%3E");
      } else if (c >= 0x20 && c <= 0x7e) {
        builder.Append((char)c);
  } else if ((c & 0xfc00) == 0xd800 && i + 1<length &&
          str[i + 1]>= 0xdc00 && str[i + 1]<= 0xdfff) {
        // Get the Unicode code point for the surrogate pair
        c = 0x10000+(c-0xd800)*0x400+(str[i + 1]-0xdc00);
        builder.Append("\\U00");
        builder.Append(hex[(c >> 20) & 15]);
        builder.Append(hex[(c >> 16) & 15]);
        builder.Append(hex[(c >> 12) & 15]);
        builder.Append(hex[(c >> 8) & 15]);
        builder.Append(hex[(c >> 4) & 15]);
        builder.Append(hex[(c) & 15]);
        ++i;
      } else {
        builder.Append("\\u");
        builder.Append(hex[(c >> 12) & 15]);
        builder.Append(hex[(c >> 8) & 15]);
        builder.Append(hex[(c >> 4) & 15]);
        builder.Append(hex[(c) & 15]);
      }
    }
  }
  private string typeOrLanguage = null;
  private string value = null;
  private int kind;

    /// <summary>Predicate for RDF types.</summary>
  public static readonly RDFTerm A=
      fromIRI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");

    /// <summary>Predicate for the first object in a list.</summary>
  public static readonly RDFTerm FIRST = fromIRI(
      "http://www.w3.org/1999/02/22-rdf-syntax-ns#first");

    /// <summary>Object for nil, the end of a list, or an empty
    /// list.</summary>
  public static readonly RDFTerm NIL = fromIRI(
      "http://www.w3.org/1999/02/22-rdf-syntax-ns#nil");

    /// <summary>Predicate for the remaining objects in a list.</summary>
  public static readonly RDFTerm REST = fromIRI(
      "http://www.w3.org/1999/02/22-rdf-syntax-ns#rest");

    /// <summary>Object for false.</summary>
  public static readonly RDFTerm FALSE = fromTypedString(
      "false",
      "http://www.w3.org/2001/XMLSchema#bool");

    /// <summary>Object for true.</summary>
  public static readonly RDFTerm TRUE = fromTypedString(
      "true",
      "http://www.w3.org/2001/XMLSchema#bool");

  public static RDFTerm fromBlankNode(string name) {
    if ((name) == null) {
 throw new ArgumentNullException("name");
}
    if ((name).Length == 0) {
 throw new ArgumentException("name is empty.");
}
    var ret = new RDFTerm();
    ret.kind = BLANK;
    ret.typeOrLanguage = null;
    ret.value = name;
    return ret;
  }

  public static RDFTerm fromIRI(string iri) {
    if ((iri) == null) {
 throw new ArgumentNullException("iri");
}
    var ret = new RDFTerm();
    ret.kind = IRI;
    ret.typeOrLanguage = null;
    ret.value = iri;
    return ret;
  }

  public static RDFTerm fromLangString(string str, string languageTag) {
    if ((str) == null) {
 throw new ArgumentNullException("str");
}
    if ((languageTag) == null) {
 throw new ArgumentNullException("languageTag");
}
    if ((languageTag).Length == 0) {
 throw new ArgumentException("languageTag is empty.");
}
    var ret = new RDFTerm();
    ret.kind = LANGSTRING;
    ret.typeOrLanguage = languageTag;
    ret.value = str;
    return ret;
  }
  public static RDFTerm fromTypedString(string str) {
    return fromTypedString(str,"http://www.w3.org/2001/XMLSchema#string");
  }
  public static RDFTerm fromTypedString(string str, string iri) {
    if ((str) == null) {
 throw new ArgumentNullException("str");
}
    if ((iri) == null) {
 throw new ArgumentNullException("iri");
}
    if ((iri).Length == 0) {
 throw new ArgumentException("iri is empty.");
}
    var ret = new RDFTerm();
    ret.kind = TYPEDSTRING;
    ret.typeOrLanguage = iri;
    ret.value = str;
    return ret;
  }
  public override sealed bool Equals(object obj) {
    if (this == obj) {
 return true;
}
    if (obj == null) {
 return false;
}
    if (GetType() != obj.GetType()) {
 return false;
}
    var other = (RDFTerm) obj;
    if (kind != other.kind) {
 return false;
}
    if (typeOrLanguage == null) {
      if (other.typeOrLanguage != null) {
 return false;
}
    } else if (!typeOrLanguage.Equals(other.typeOrLanguage)) {
 return false;
}
    if (value == null) {
      return (other.value != null);
    } else {
 return (!value.Equals(other.value));
}
  }
  public int getKind() {
    return kind;
  }

    /// <summary>Gets the language tag or data type for this RDF
    /// literal.</summary>
    /// <returns>A string object.</returns>
  public string getTypeOrLanguage() {
    return typeOrLanguage;
  }

    /// <summary>Gets the IRI, blank node identifier, or lexical form of an
    /// RDF literal.</summary>
    /// <returns>A string object.</returns>
  public string getValue() {
    return value;
  }
  public override sealed int GetHashCode() {unchecked {
     var prime = 31;
    int result = prime + kind;
    result = prime * result+
        ((typeOrLanguage == null) ? 0 : typeOrLanguage.GetHashCode());
    result = prime * result + ((value == null) ? 0 : value.GetHashCode());
    return result;
  }}
  public bool isBlank() {
    return kind == BLANK;
  }
  public bool isIRI(string str) {
    return kind == IRI && str != null && str.Equals(value);
  }
  public bool isOrdinaryString() {
    return kind==TYPEDSTRING && "http://www.w3.org/2001/XMLSchema#string"
      .Equals(typeOrLanguage);
  }

    /// <summary>* Gets a _string representation of this RDF term in
    /// N-Triples format. The _string will not end in a line
    /// break.</summary>
  public override sealed string ToString() {
    StringBuilder builder = null;
    if (this.kind == BLANK) {
      builder = new StringBuilder();
      builder.Append("_:");
      escapeBlankNode(value, builder);
    } else if (this.kind == LANGSTRING) {
      builder = new StringBuilder();
      builder.Append("\"");
      escapeString(value, builder, false);
      builder.Append("\"@");
      escapeLanguageTag(typeOrLanguage, builder);
    } else if (this.kind == TYPEDSTRING) {
      builder = new StringBuilder();
      builder.Append("\"");
      escapeString(value, builder, false);
      builder.Append("\"");
      if (!"http://www.w3.org/2001/XMLSchema#string".Equals(typeOrLanguage)) {
        builder.Append("^^<");
        escapeString(typeOrLanguage, builder, true);
        builder.Append(">");
      }
    } else if (this.kind == IRI) {
      builder = new StringBuilder();
      builder.Append("<");
      escapeString(value, builder, true);
      builder.Append(">");
    } else {
 return "<about:blank>";
}
    return builder.ToString();
  }
}
}
