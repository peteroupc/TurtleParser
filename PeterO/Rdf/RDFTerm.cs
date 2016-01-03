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

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="T:PeterO.Rdf.RDFTerm"]/*'/>
public sealed class RDFTerm {
    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="F:PeterO.Rdf.RDFTerm.BLANK"]/*'/>
  public const int BLANK = 0;  // type is blank node name, literal is blank

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="F:PeterO.Rdf.RDFTerm.IRI"]/*'/>
  public const int IRI = 1;  // type is IRI, literal is blank

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="F:PeterO.Rdf.RDFTerm.LANGSTRING"]/*'/>
  public const int LANGSTRING = 2;  // literal is given

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="F:PeterO.Rdf.RDFTerm.TYPEDSTRING"]/*'/>
  public const int TYPEDSTRING = 3;  // type is IRI, literal is given

  private static void escapeBlankNode(string str, StringBuilder builder) {
    int length = str.Length;
    string hex = "0123456789ABCDEF";
    for (int i = 0; i < length; ++i) {
      int c = str[i];
      if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c<= 'z') ||
          (c > 0 && c >= '0' && c <= '9')) {
        builder.Append((char)c);
  } else if ((c & 0xfc00) == 0xd800 && i + 1 < length &&
          str[i + 1] >= 0xdc00 && str[i + 1] <= 0xdfff) {
        // Get the Unicode code point for the surrogate pair
        c = 0x10000 + (c - 0xd800)* 0x400+(str[i + 1]-0xdc00);
        builder.Append("U00");
        builder.Append(hex[(c >> 20) & 15]);
        builder.Append(hex[(c >> 16) & 15]);
        builder.Append(hex[(c >> 12) & 15]);
        builder.Append(hex[(c >> 8) & 15]);
        builder.Append(hex[(c >> 4) & 15]);
        builder.Append(hex[c & 15]);
        ++i;
      } else {
        builder.Append("u");
        builder.Append(hex[(c >> 12) & 15]);
        builder.Append(hex[(c >> 8) & 15]);
        builder.Append(hex[(c >> 4) & 15]);
        builder.Append(hex[c & 15]);
      }
    }
  }

  private static void escapeLanguageTag(string str, StringBuilder builder) {
    int length = str.Length;
    var hyphen = false;
    for (int i = 0; i < length; ++i) {
      int c = str[i];
      if (c >= 'A' && c <= 'Z') {
        builder.Append((char)(c + 0x20));
      } else if (c >= 'a' && c <= 'z') {
        builder.Append((char)c);
      } else if (hyphen && c >= '0' && c <= '9') {
        builder.Append((char)c);
      } else if (c == '-') {
        builder.Append((char)c);
        hyphen = true;
        if (i + 1 < length && str[i + 1]=='-') {
          builder.Append('x');
        }
      } else {
        builder.Append('x');
      }
    }
  }

  private static void escapeString(
string str,
StringBuilder builder,
bool uri) {
    int length = str.Length;
    string hex = "0123456789ABCDEF";
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
      } else if (uri && c == '>') {
        builder.Append("%3E");
      } else if (c >= 0x20 && c <= 0x7e) {
        builder.Append((char)c);
  } else if ((c & 0xfc00) == 0xd800 && i + 1 < length &&
          str[i + 1] >= 0xdc00 && str[i + 1] <= 0xdfff) {
        // Get the Unicode code point for the surrogate pair
        c = 0x10000 + (c - 0xd800)* 0x400+(str[i + 1]-0xdc00);
        builder.Append("\\U00");
        builder.Append(hex[(c >> 20) & 15]);
        builder.Append(hex[(c >> 16) & 15]);
        builder.Append(hex[(c >> 12) & 15]);
        builder.Append(hex[(c >> 8) & 15]);
        builder.Append(hex[(c >> 4) & 15]);
        builder.Append(hex[c & 15]);
        ++i;
      } else {
        builder.Append("\\u");
        builder.Append(hex[(c >> 12) & 15]);
        builder.Append(hex[(c >> 8) & 15]);
        builder.Append(hex[(c >> 4) & 15]);
        builder.Append(hex[c & 15]);
      }
    }
  }

  private string typeOrLanguage = null;
  private string value = null;
  private int kind;

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="F:PeterO.Rdf.RDFTerm.A"]/*'/>
  public static readonly RDFTerm A =
      fromIRI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="F:PeterO.Rdf.RDFTerm.FIRST"]/*'/>
  public static readonly RDFTerm FIRST = fromIRI(
      "http://www.w3.org/1999/02/22-rdf-syntax-ns#first");

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="F:PeterO.Rdf.RDFTerm.NIL"]/*'/>
  public static readonly RDFTerm NIL = fromIRI(
      "http://www.w3.org/1999/02/22-rdf-syntax-ns#nil");

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="F:PeterO.Rdf.RDFTerm.REST"]/*'/>
  public static readonly RDFTerm REST = fromIRI(
      "http://www.w3.org/1999/02/22-rdf-syntax-ns#rest");

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="F:PeterO.Rdf.RDFTerm.FALSE"]/*'/>
  public static readonly RDFTerm FALSE = fromTypedString(
      "false",
      "http://www.w3.org/2001/XMLSchema#bool");

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="F:PeterO.Rdf.RDFTerm.TRUE"]/*'/>
  public static readonly RDFTerm TRUE = fromTypedString(
      "true",
      "http://www.w3.org/2001/XMLSchema#bool");

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.fromBlankNode(System.String)"]/*'/>
  public static RDFTerm fromBlankNode(string name) {
    if (name == null) {
 throw new ArgumentNullException("name");
}
    if (name.Length == 0) {
 throw new ArgumentException("name is empty.");
}
    var ret = new RDFTerm();
    ret.kind = BLANK;
    ret.typeOrLanguage = null;
    ret.value = name;
    return ret;
  }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.fromIRI(System.String)"]/*'/>
  public static RDFTerm fromIRI(string iri) {
    if (iri == null) {
 throw new ArgumentNullException("iri");
}
    var ret = new RDFTerm();
    ret.kind = IRI;
    ret.typeOrLanguage = null;
    ret.value = iri;
    return ret;
  }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.fromLangString(System.String,System.String)"]/*'/>
  public static RDFTerm fromLangString(string str, string languageTag) {
    if (str == null) {
 throw new ArgumentNullException("str");
}
    if (languageTag == null) {
 throw new ArgumentNullException("languageTag");
}
    if (languageTag.Length == 0) {
 throw new ArgumentException("languageTag is empty.");
}
    var ret = new RDFTerm();
    ret.kind = LANGSTRING;
    ret.typeOrLanguage = languageTag;
    ret.value = str;
    return ret;
  }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.fromTypedString(System.String)"]/*'/>
  public static RDFTerm fromTypedString(string str) {
    return fromTypedString(str, "http://www.w3.org/2001/XMLSchema#string");
  }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.fromTypedString(System.String,System.String)"]/*'/>
  public static RDFTerm fromTypedString(string str, string iri) {
    if (str == null) {
 throw new ArgumentNullException("str");
}
    if (iri == null) {
 throw new ArgumentNullException("iri");
}
    if (iri.Length == 0) {
 throw new ArgumentException("iri is empty.");
}
    var ret = new RDFTerm();
    ret.kind = TYPEDSTRING;
    ret.typeOrLanguage = iri;
    ret.value = str;
    return ret;
  }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.Equals(System.Object)"]/*'/>
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
    if (this.kind != other.kind) {
 return false;
}
    if (this.typeOrLanguage == null) {
      if (other.typeOrLanguage != null) {
 return false;
}
    } else if (!this.typeOrLanguage.Equals(other.typeOrLanguage)) {
 return false;
}
    if (this.value == null) {
      return other.value != null;
    } else {
 return !this.value.Equals(other.value);
}
  }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.getKind"]/*'/>
  public int getKind() {
    return this.kind;
  }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.getTypeOrLanguage"]/*'/>
  public string getTypeOrLanguage() {
    return this.typeOrLanguage;
  }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.getValue"]/*'/>
  public string getValue() {
    return this.value;
  }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.GetHashCode"]/*'/>
  public override sealed int GetHashCode() {unchecked {
     var prime = 31;
    int result = prime + this.kind;
    result = (prime * result) + ((this.typeOrLanguage == null) ? 0 :
            this.typeOrLanguage.GetHashCode());
    result = prime * result + ((this.value = = null) ? 0 :
      this.value.GetHashCode());
    return result;
  }}

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.isBlank"]/*'/>
  public bool isBlank() {
    return this.kind == BLANK;
  }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.isIRI(System.String)"]/*'/>
  public bool isIRI(string str) {
    return this.kind == IRI && str != null && str.Equals(this.value);
  }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.isOrdinaryString"]/*'/>
  public bool isOrdinaryString() {
    return this.kind == TYPEDSTRING && "http://www.w3.org/2001/XMLSchema#string"
      .Equals(this.typeOrLanguage);
  }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.ToString"]/*'/>
  public override sealed string ToString() {
    StringBuilder builder = null;
    if (this.kind == BLANK) {
      builder = new StringBuilder();
      builder.Append("_:");
      escapeBlankNode(this.value, builder);
    } else if (this.kind == LANGSTRING) {
      builder = new StringBuilder();
      builder.Append("\"");
      escapeString(this.value, builder, false);
      builder.Append("\"@");
      escapeLanguageTag(this.typeOrLanguage, builder);
    } else if (this.kind == TYPEDSTRING) {
      builder = new StringBuilder();
      builder.Append("\"");
      escapeString(this.value, builder, false);
      builder.Append("\"");
  if (!"http://www.w3.org/2001/XMLSchema#string"
        .Equals(this.typeOrLanguage)) {
        builder.Append("^^<");
        escapeString(this.typeOrLanguage, builder, true);
        builder.Append(">");
      }
    } else if (this.kind == IRI) {
      builder = new StringBuilder();
      builder.Append("<");
      escapeString(this.value, builder, true);
      builder.Append(">");
    } else {
 return "<about:blank>";
}
    return builder.ToString();
  }
}
}
