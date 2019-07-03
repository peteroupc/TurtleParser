using System;
using System.Text;
using PeterO;

/*
Written in 2013 by Peter Occil.
Any copyright is dedicated to the Public Domain.
http://creativecommons.org/publicdomain/zero/1.0/

If you like this, you should donate to Peter O.
at: http://peteroupc.github.io/
*/
namespace PeterO.Rdf {
  /// <include file='../../docs.xml'
  /// path='docs/doc[@name="T:PeterO.Rdf.RDFTerm"]/*'/>
  public sealed class RDFTerm {
    /// <xmlbegin id="11"/><summary>Type value for a blank node.</summary>
  ///
  ///
  ///
    public const int BLANK = 0; // type is blank node name, literal is blank

    /// <xmlbegin id="12"/><summary>Type value for an IRI (Internationalized Resource
    /// Identifier.).</summary>
  ///
  ///
  ///
    public const int IRI = 1; // type is IRI, literal is blank

    /// <xmlbegin id="13"/><summary>Type value for a string with a language tag.</summary>
  ///
  ///
  ///
    public const int LANGSTRING = 2; // literal is given

    /// <xmlbegin id="14"/><summary>Type value for a piece of data serialized to a
    /// string.</summary>
  ///
  ///
  ///
    public const int TYPEDSTRING = 3; // type is IRI, literal is given

    private static void EscapeBlankNode(string str, StringBuilder builder) {
      int length = str.Length;
      string hex = "0123456789ABCDEF";
      for (int i = 0; i < length; ++i) {
        int c = str[i];
        if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') ||
            (c > 0 && c >= '0' && c <= '9')) {
          builder.Append((char)c);
        } else if ((c & 0xfc00) == 0xd800 && i + 1 < length &&
                (str[i + 1] & 0xfc00) == 0xdc00) {
          // Get the Unicode code point for the surrogate pair
          c = 0x10000 + ((c - 0xd800) << 10) + (str[i + 1] - 0xdc00);
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

    private static void EscapeLanguageTag(string str, StringBuilder builder) {
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
          if (i + 1 < length && str[i + 1] == '-') {
            builder.Append('x');
          }
        } else {
          builder.Append('x');
        }
      }
    }

    private static void EscapeString(
  string str,
  StringBuilder builder,
  bool uri) {
      int length = str.Length;
      const string Hex = "0123456789ABCDEF";
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
                (str[i + 1] & 0xfc00) == 0xdc00) {
          // Get the Unicode code point for the surrogate pair
          c = 0x10000 + ((c - 0xd800) << 10) + (str[i + 1] - 0xdc00);
          builder.Append("\\U00");
          builder.Append(Hex[(c >> 20) & 15]);
          builder.Append(Hex[(c >> 16) & 15]);
          builder.Append(Hex[(c >> 12) & 15]);
          builder.Append(Hex[(c >> 8) & 15]);
          builder.Append(Hex[(c >> 4) & 15]);
          builder.Append(Hex[c & 15]);
          ++i;
        } else {
          builder.Append("\\u");
          builder.Append(Hex[(c >> 12) & 15]);
          builder.Append(Hex[(c >> 8) & 15]);
          builder.Append(Hex[(c >> 4) & 15]);
          builder.Append(Hex[c & 15]);
        }
      }
    }

    private readonly string typeOrLanguage = null;
    private readonly string value = null;
    private readonly int kind;

    private RDFTerm(int kind, string typeOrLanguage, string value) {
      this.kind = kind;
      this.typeOrLanguage = typeOrLanguage;
      this.value = value;
    }

    /// <xmlbegin id="15"/><summary>Predicate for RDF types.</summary>
  ///
  ///
  ///
    public static readonly RDFTerm A =
        FromIRI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");

    /// <xmlbegin id="16"/><summary>Predicate for the first object in a list.</summary>
  ///
  ///
  ///
    public static readonly RDFTerm FIRST = FromIRI(
        "http://www.w3.org/1999/02/22-rdf-syntax-ns#first");

    /// <xmlbegin id="17"/><summary>Object for nil, the end of a list, or an empty
    /// list.</summary>
  ///
  ///
  ///
    public static readonly RDFTerm NIL = FromIRI(
        "http://www.w3.org/1999/02/22-rdf-syntax-ns#nil");

    /// <xmlbegin id="18"/><summary>Predicate for the remaining objects in a list.</summary>
  ///
  ///
  ///
    public static readonly RDFTerm REST = FromIRI(
        "http://www.w3.org/1999/02/22-rdf-syntax-ns#rest");

    /// <xmlbegin id="19"/><summary>Object for false.</summary>
  ///
  ///
  ///
    public static readonly RDFTerm FALSE = FromTypedString(
        "false",
        "http://www.w3.org/2001/XMLSchema#bool");

    /// <xmlbegin id="20"/><summary>Object for true.</summary>
  ///
  ///
  ///
    public static readonly RDFTerm TRUE = FromTypedString(
        "true",
        "http://www.w3.org/2001/XMLSchema#bool");

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.FromBlankNode(System.String)"]/*'/>
    public static RDFTerm FromBlankNode(string name) {
      if (name == null) {
        throw new ArgumentNullException(nameof(name));
      }
      if (name.Length == 0) {
        throw new ArgumentException("name is empty.");
      }
      return new RDFTerm(BLANK, null, name);
    }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.FromIRI(System.String)"]/*'/>
    public static RDFTerm FromIRI(string iri) {
      if (iri == null) {
        throw new ArgumentNullException(nameof(iri));
      }
      return new RDFTerm(IRI, null, iri);
    }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.FromLangString(System.String,System.String)"]/*'/>
    public static RDFTerm FromLangString(string str, string languageTag) {
      if (str == null) {
        throw new ArgumentNullException(nameof(str));
      }
      if (languageTag == null) {
        throw new ArgumentNullException(nameof(languageTag));
      }
      if (languageTag.Length == 0) {
        throw new ArgumentException("languageTag is empty.");
      }
      return new RDFTerm(LANGSTRING, languageTag, str);
    }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.FromTypedString(System.String)"]/*'/>
    public static RDFTerm FromTypedString(string str) {
      return FromTypedString(str, "http://www.w3.org/2001/XMLSchema#string");
    }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.FromTypedString(System.String,System.String)"]/*'/>
    public static RDFTerm FromTypedString(string str, string iri) {
      if (str == null) {
        throw new ArgumentNullException(nameof(str));
      }
      if (iri == null) {
        throw new ArgumentNullException(nameof(iri));
      }
      if (iri.Length == 0) {
        throw new ArgumentException("iri is empty.");
      }
      return new RDFTerm(TYPEDSTRING, iri, str);
    }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.Equals(System.Object)"]/*'/>
    public override sealed bool Equals(object obj) {
      if (this == obj) {
        return true;
      }
      var other = obj as RDFTerm;
      if (other == null) {
        return false;
      }
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
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.GetKind"]/*'/>
    public int GetKind() {
      return this.kind;
    }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.GetTypeOrLanguage"]/*'/>
    public string GetTypeOrLanguage() {
      return this.typeOrLanguage;
    }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.GetValue"]/*'/>
    public string GetValue() {
      return this.value;
    }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.GetHashCode"]/*'/>
    public override sealed int GetHashCode() {
      unchecked {
        var prime = 31;
        int result = prime + this.kind;
        result = (prime * result) + ((this.typeOrLanguage == null) ? 0 :
                this.typeOrLanguage.GetHashCode());
        bool isnull = this.value == null;
        result = (prime * result) + (isnull ? 0 : this.value.GetHashCode());
        return result;
      }
    }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.IsBlank"]/*'/>
    public bool IsBlank() {
      return this.kind == BLANK;
    }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.IsIRI(System.String)"]/*'/>
    public bool IsIRI(string str) {
      return this.kind == IRI && str != null && str.Equals(this.value);
    }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTerm.IsOrdinaryString"]/*'/>
    public bool IsOrdinaryString() {
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
        EscapeBlankNode(this.value, builder);
      } else if (this.kind == LANGSTRING) {
        builder = new StringBuilder();
        builder.Append("\"");
        EscapeString(this.value, builder, false);
        builder.Append("\"@");
        EscapeLanguageTag(this.typeOrLanguage, builder);
      } else if (this.kind == TYPEDSTRING) {
        builder = new StringBuilder();
        builder.Append("\"");
        EscapeString(this.value, builder, false);
        builder.Append("\"");
        if (!"http://www.w3.org/2001/XMLSchema#string"
              .Equals(this.typeOrLanguage)) {
          builder.Append("^^<");
          EscapeString(this.typeOrLanguage, builder, true);
          builder.Append(">");
        }
      } else if (this.kind == IRI) {
        builder = new StringBuilder();
        builder.Append("<");
        EscapeString(this.value, builder, true);
        builder.Append(">");
      } else {
        return "<>";
      }
      return builder.ToString();
    }
  }
}
