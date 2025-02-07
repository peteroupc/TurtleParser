using System;
using System.Text;
using PeterO;

/*
Written in 2013 by Peter Occil.
Any copyright to this work is released to the Public Domain.
In case this is not possible, this work is also
licensed under the Unlicense: https://unlicense.org/

*/
namespace PeterO.Rdf {
  /// <summary>Not documented yet.</summary>
  public sealed class RDFTerm {
    /// <summary>Type value for a blank node.</summary>
    public const int BLANK = 0; // type is blank node name, literal is blank

    /// <summary>Type value for an IRI (Internationalized Resource
    /// Identifier.).</summary>
    public const int IRI = 1; // type is IRI, literal is blank

    /// <summary>Type value for a string with a language tag.</summary>
    public const int LANGSTRING = 2; // literal is given

    /// <summary>Type value for a piece of data serialized to a
    /// string.</summary>
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
          c = 0x10000 + ((c & 0x3ff) << 10) + (str[i + 1] & 0x3ff);
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
          c = 0x10000 + ((c & 0x3ff) << 10) + (str[i + 1] & 0x3ff);
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

    private readonly string typeOrLanguage;
    private readonly string value;
    private readonly int kind;

    private RDFTerm(int kind, string typeOrLanguage, string value) {
      this.kind = kind;
      this.typeOrLanguage = typeOrLanguage;
      this.value = value;
    }

    /// <summary>Predicate for RDF types.</summary>
    public static readonly RDFTerm A =
      FromIRI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");

    /// <summary>Predicate for the first object in a list.</summary>
    public static readonly RDFTerm FIRST = FromIRI(
        "http://www.w3.org/1999/02/22-rdf-syntax-ns#first");

    /// <summary>Object for nil, the end of a list, or an empty
    /// list.</summary>
    public static readonly RDFTerm NIL = FromIRI(
        "http://www.w3.org/1999/02/22-rdf-syntax-ns#nil");

    /// <summary>Predicate for the remaining objects in a list.</summary>
    public static readonly RDFTerm REST = FromIRI(
        "http://www.w3.org/1999/02/22-rdf-syntax-ns#rest");

    /// <summary>Object for false.</summary>
    public static readonly RDFTerm FALSE = FromTypedString(
        "false",
        "http://www.w3.org/2001/XMLSchema#boolean");

    /// <summary>Object for true.</summary>
    public static readonly RDFTerm TRUE = FromTypedString(
        "true",
        "http://www.w3.org/2001/XMLSchema#boolean");

    /// <summary>Not documented yet.</summary>
    /// <param name='name'>The parameter <paramref name='name'/> is a text
    /// string.</param>
    /// <returns>A RDFTerm object.</returns>
    /// <exception cref='ArgumentException'>Name is empty.</exception>
    /// <exception cref='ArgumentNullException'>The parameter <paramref
    /// name='name'/> is null.</exception>
    public static RDFTerm FromBlankNode(string name) {
      if (name == null) {
        throw new ArgumentNullException(nameof(name));
      }
      if (name.Length == 0) {
        throw new ArgumentException("name is empty.");
      }
      return new RDFTerm(BLANK, null, name);
    }

    /// <summary>Not documented yet.</summary>
    /// <param name='iri'>The parameter <paramref name='iri'/> is a text
    /// string.</param>
    /// <returns>A RDFTerm object.</returns>
    /// <exception cref='ArgumentNullException'>The parameter <paramref
    /// name='iri'/> is null.</exception>
    public static RDFTerm FromIRI(string iri) {
      if (iri == null) {
        throw new ArgumentNullException(nameof(iri));
      }
      return new RDFTerm(IRI, null, iri);
    }

    /// <summary>Not documented yet.</summary>
    /// <param name='str'>The parameter <paramref name='str'/> is a text
    /// string.</param>
    /// <param name='languageTag'>The parameter <paramref
    /// name='languageTag'/> is a text string.</param>
    /// <returns>A RDFTerm object.</returns>
    /// <exception cref='ArgumentNullException'>The parameter <paramref
    /// name='str'/> or <paramref name='languageTag'/> is null.</exception>
    /// <exception cref='ArgumentException'>LanguageTag is
    /// empty.</exception>
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

    /// <summary>Not documented yet.</summary>
    /// <param name='str'>The parameter <paramref name='str'/> is a text
    /// string.</param>
    /// <returns>A RDFTerm object.</returns>
    public static RDFTerm FromTypedString(string str) {
      return FromTypedString(str, "http://www.w3.org/2001/XMLSchema#string");
    }

    /// <summary>Not documented yet.</summary>
    /// <param name='str'>The parameter <paramref name='str'/> is a text
    /// string.</param>
    /// <param name='iri'>The parameter <paramref name='iri'/> is a text
    /// string.</param>
    /// <returns>A RDFTerm object.</returns>
    /// <exception cref='ArgumentNullException'>The parameter <paramref
    /// name='str'/> or <paramref name='iri'/> is null.</exception>
    /// <exception cref='ArgumentException'>Iri is empty.</exception>
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

    /// <summary>Not documented yet.</summary>
    /// <param name='obj'>The parameter <paramref name='obj'/> is a Object
    /// object.</param>
    /// <returns>The return value is not documented yet.</returns>
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
      } else if (!this.typeOrLanguage.Equals(other.typeOrLanguage,
        StringComparison.Ordinal)) {
        return false;
      }
      if (this.value == null) {
        return other.value == null;
      } else {
        return this.value.Equals(other.value, StringComparison.Ordinal);
      }
    }

    /// <summary>Not documented yet.</summary>
    /// <returns>A 32-bit signed integer.</returns>
    public int GetKind() {
      return this.kind;
    }

    /// <summary>Gets the language tag or data type for this RDF
    /// literal.</summary>
    /// <returns>A text string.</returns>
    public string GetTypeOrLanguage() {
      return this.typeOrLanguage;
    }

    /// <summary>Gets the IRI, blank node identifier, or lexical form of an
    /// RDF literal.</summary>
    /// <returns>A text string.</returns>
    public string GetValue() {
      return this.value;
    }

    /// <summary>Not documented yet.</summary>
    /// <returns>The return value is not documented yet.</returns>
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

    /// <summary>Gets a value indicating whether this term is a blank
    /// node.</summary>
    /// <returns>Either <c>true</c> or <c>false</c>.</returns>
    public bool IsBlank() {
      return this.kind == BLANK;
    }

    /// <summary>Not documented yet.</summary>
    /// <param name='str'>The parameter <paramref name='str'/> is a text
    /// string.</param>
    /// <returns>Either <c>true</c> or <c>false</c>.</returns>
    public bool IsIRI(string str) {
      return this.kind == IRI && str != null && str.Equals(this.value,
          StringComparison.Ordinal);
    }

    private const string XmlSchemaString =
      "http://www.w3.org/2001/XMLSchema#string";

    /// <summary>Not documented yet.</summary>
    /// <returns>Either <c>true</c> or <c>false</c>.</returns>
    public bool IsOrdinaryString() {
      return this.kind == TYPEDSTRING &&
        XmlSchemaString.Equals(this.typeOrLanguage, StringComparison.Ordinal);
    }

    /// <summary>Gets a string representation of this RDF term in N-Triples
    /// format. The string will not end in a line break.</summary>
    /// <returns>A string representation of this object.</returns>
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
        if (!XmlSchemaString.Equals(this.typeOrLanguage,
          StringComparison.Ordinal)) {
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
