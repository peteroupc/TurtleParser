/*
Written in 2013 by Peter Occil.
Any copyright is dedicated to the Public Domain.
http://creativecommons.org/publicdomain/zero/1.0/

If you like this, you should donate to Peter O.
at: http://peteroupc.github.io/
*/
namespace PeterO.Rdf {
  using System;
using System.Collections.Generic;
using System.Globalization;
using System.Text;
using PeterO;
using PeterO.Text;

    /// <summary>Not documented yet.</summary>
  ///
  ///
  public class TurtleParser : IRDFParser {
    private sealed class TurtleObject {
      public const int SIMPLE = 0;
      public const int COLLECTION = 1;
      public const int PROPERTIES = 2;

      public static TurtleObject fromTerm(RDFTerm term) {
        var tobj = new TurtleObject();
        tobj.Term = term;
        tobj.Kind = TurtleObject.SIMPLE;
        return tobj;
      }

      public static TurtleObject newCollection() {
        var tobj = new TurtleObject();
        tobj.objects = new List<TurtleObject>();
        tobj.Kind = TurtleObject.COLLECTION;
        return tobj;
      }

      public static TurtleObject newPropertyList() {
        var tobj = new TurtleObject();
        tobj.properties = new List<TurtleProperty>();
        tobj.Kind = TurtleObject.PROPERTIES;
        return tobj;
      }

      private RDFTerm term;

      private int kind;
      private IList<TurtleObject> objects;

      private IList<TurtleProperty> properties;

      public RDFTerm Term {
        get {
          return this.term;
        }

        set {
          this.term = value;
        }
      }

      public int Kind {
        get {
          return this.kind;
        }

        set {
          this.kind = value;
        }
      }

      public IList<TurtleObject> getObjects() {
        return this.objects;
      }

      public IList<TurtleProperty> getProperties() {
        return this.properties;
      }
    }

    private sealed class TurtleProperty {
      private RDFTerm predValue;
      private TurtleObject objValue;

      public RDFTerm Pred {
        get {
          return this.predValue;
        }

        set {
          this.predValue = value;
        }
      }

      public TurtleObject Obj {
        get {
          return this.objValue;
        }

        set {
          this.objValue = value;
        }
      }
    }

    private IDictionary<string, RDFTerm> bnodeLabels;
    private IDictionary<string, string> namespaces;

    private string baseURI;

    private TurtleObject curSubject;

    private RDFTerm curPredicate;

    private StackableCharacterInput input;
    private int curBlankNode = 0;

    /// <summary>Initializes a new instance of the
    /// <see cref='T:PeterO.Rdf.TurtleParser'/> class.</summary>
    /// <param name='stream'>A PeterO.IByteReader object.</param>
  ///
  ///
    public TurtleParser(IByteReader stream) : this(stream, "about:blank") {
    }

    /// <summary>Initializes a new instance of the
    /// <see cref='T:PeterO.Rdf.TurtleParser'/> class.</summary>
    /// <param name='stream'>A PeterO.IByteReader object.</param>
    /// <param name='baseURI'>The parameter <paramref name='baseURI'/> is a
    /// text string.</param>
    /// <exception cref='T:System.ArgumentNullException'>The parameter
    /// <paramref name='stream'/> or <paramref name='baseURI'/> is
    /// null.</exception>
    /// <exception cref='ArgumentException'>BaseURI.</exception>
  ///
  ///
    public TurtleParser(IByteReader stream, string baseURI) {
      if (stream == null) {
        throw new ArgumentNullException(nameof(stream));
      }
      if (baseURI == null) {
        throw new ArgumentNullException(nameof(baseURI));
      }
      if (!URIUtility.hasScheme(baseURI)) {
        throw new ArgumentException("baseURI");
      }
      this.input = new StackableCharacterInput(
          Encodings.GetDecoderInput(Encodings.UTF8, stream));
      this.baseURI = baseURI;
      this.bnodeLabels = new Dictionary<string, RDFTerm>();
      this.namespaces = new Dictionary<string, string>();
    }

    /// <summary>Initializes a new instance of the
    /// <see cref='T:PeterO.Rdf.TurtleParser'/> class.</summary>
    /// <param name='str'>The parameter <paramref name='str'/> is a text
    /// string.</param>
  ///
  ///
    public TurtleParser(string str) : this(str, "about:blank") {
    }

    /// <summary>Initializes a new instance of the
    /// <see cref='T:PeterO.Rdf.TurtleParser'/> class.</summary>
    /// <param name='str'>The parameter <paramref name='str'/> is a text
    /// string.</param>
    /// <param name='baseURI'>The parameter <paramref name='baseURI'/> is a
    /// text string.</param>
    /// <exception cref='T:System.ArgumentNullException'>The parameter
    /// <paramref name='str'/> or <paramref name='baseURI'/> is
    /// null.</exception>
    /// <exception cref='ArgumentException'>BaseURI.</exception>
  ///
  ///
    public TurtleParser(string str, string baseURI) {
      if (str == null) {
        throw new ArgumentNullException(nameof(str));
      }
      if (baseURI == null) {
        throw new ArgumentNullException(nameof(baseURI));
      }
      if (!URIUtility.hasScheme(baseURI)) {
        throw new ArgumentException("baseURI");
      }
      this.input = new StackableCharacterInput(
          Encodings.StringToInput(str));
      this.baseURI = baseURI;
      this.bnodeLabels = new Dictionary<string, RDFTerm>();
      this.namespaces = new Dictionary<string, string>();
    }

    private RDFTerm AllocateBlankNode() {
      ++this.curBlankNode;
      // A period is included so as not to conflict
      // with user-defined blank node labels (this is allowed
      // because the syntax for blank node identifiers is
      // not concretely defined)
      string label = "." +
          Convert.ToString(this.curBlankNode, CultureInfo.InvariantCulture);
      RDFTerm node = RDFTerm.fromBlankNode(label);
      this.bnodeLabels.Add(label, node);
      return node;
    }

    private void emitRDFTriple(
  RDFTerm subj,
  RDFTerm pred,
  RDFTerm obj,
  ISet<RDFTriple> triples) {
      var triple = new RDFTriple(subj, pred, obj);
      triples.Add(triple);
    }

    private void emitRDFTriple(
  RDFTerm subj,
  RDFTerm pred,
  TurtleObject obj,
  ISet<RDFTriple> triples) {
      if (obj.Kind == TurtleObject.SIMPLE) {
        this.emitRDFTriple(subj, pred, obj.Term, triples);
      } else if (obj.Kind == TurtleObject.PROPERTIES) {
        IList<TurtleProperty> props = obj.getProperties();
        if (props.Count == 0) {
          this.emitRDFTriple(subj, pred, this.AllocateBlankNode(), triples);
        } else {
          RDFTerm blank = this.AllocateBlankNode();
          this.emitRDFTriple(subj, pred, blank, triples);
          for (int i = 0; i < props.Count; ++i) {
            this.emitRDFTriple(blank, props[i].Pred, props[i].Obj, triples);
          }
        }
      } else if (obj.Kind == TurtleObject.COLLECTION) {
        IList<TurtleObject> objs = obj.getObjects();
        if (objs.Count == 0) {
          this.emitRDFTriple(subj, pred, RDFTerm.NIL, triples);
        } else {
          RDFTerm curBlank = this.AllocateBlankNode();
          RDFTerm firstBlank = curBlank;
          this.emitRDFTriple(curBlank, RDFTerm.FIRST, objs[0], triples);
          for (int i = 1; i <= objs.Count; ++i) {
            if (i == objs.Count) {
              this.emitRDFTriple(curBlank, RDFTerm.REST, RDFTerm.NIL, triples);
            } else {
              RDFTerm nextBlank = this.AllocateBlankNode();
              this.emitRDFTriple(curBlank, RDFTerm.REST, nextBlank, triples);
              this.emitRDFTriple(nextBlank, RDFTerm.FIRST, objs[i], triples);
              curBlank = nextBlank;
            }
          }
          this.emitRDFTriple(subj, pred, firstBlank, triples);
        }
      }
    }

    private void emitRDFTriple(
  TurtleObject subj,
  RDFTerm pred,
  TurtleObject obj,
  ISet<RDFTriple> triples) {
      if (subj.Kind == TurtleObject.SIMPLE) {
        this.emitRDFTriple(subj.Term, pred, obj, triples);
      } else if (subj.Kind == TurtleObject.PROPERTIES) {
        IList<TurtleProperty> props = subj.getProperties();
        if (props.Count == 0) {
          this.emitRDFTriple(this.AllocateBlankNode(), pred, obj, triples);
        } else {
          RDFTerm blank = this.AllocateBlankNode();
          this.emitRDFTriple(blank, pred, obj, triples);
          for (int i = 0; i < props.Count; ++i) {
            this.emitRDFTriple(blank, props[i].Pred, props[i].Obj, triples);
          }
        }
      } else if (subj.Kind == TurtleObject.COLLECTION) {
        IList<TurtleObject> objs = subj.getObjects();
        if (objs.Count == 0) {
          this.emitRDFTriple(RDFTerm.NIL, pred, obj, triples);
        } else {
          RDFTerm curBlank = this.AllocateBlankNode();
          RDFTerm firstBlank = curBlank;
          this.emitRDFTriple(curBlank, RDFTerm.FIRST, objs[0], triples);
          for (int i = 1; i <= objs.Count; ++i) {
            if (i == objs.Count) {
              this.emitRDFTriple(curBlank, RDFTerm.REST, RDFTerm.NIL, triples);
            } else {
              RDFTerm nextBlank = this.AllocateBlankNode();
              this.emitRDFTriple(curBlank, RDFTerm.REST, nextBlank, triples);
              this.emitRDFTriple(nextBlank, RDFTerm.FIRST, objs[i], triples);
              curBlank = nextBlank;
            }
          }
          this.emitRDFTriple(firstBlank, pred, obj, triples);
        }
      }
    }

    private RDFTerm finishStringLiteral(string str) {
      int mark = this.input.setHardMark();
      int ch = this.input.ReadChar();
      if (ch == '@') {
        return RDFTerm.fromLangString(str, this.readLanguageTag());
      } else if (ch == '^' && this.input.ReadChar() == '^') {
        ch = this.input.ReadChar();
        if (ch == '<') {
          return RDFTerm.fromTypedString(str, this.readIriReference());
        } else if (ch == ':') {  // prefixed name with current prefix
          string scope = this.namespaces[String.Empty];
          if (scope == null) {
            throw new ParserException();
          }
          return RDFTerm.fromTypedString(
     str,
     scope + this.readOptionalLocalName());
        } else if (this.isNameStartChar(ch)) {  // prefix
          string prefix = this.readPrefix(ch);
          string scope = this.namespaces[prefix];
          if (scope == null) {
            throw new ParserException();
          }
          return RDFTerm.fromTypedString(
     str,
     scope + this.readOptionalLocalName());
        } else {
          throw new ParserException();
        }
      } else {
        this.input.setMarkPosition(mark);
        return RDFTerm.fromTypedString(str);
      }
    }

    private bool isNameChar(int ch) {
      return (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9') ||
          (ch >= 'A' && ch <= 'Z') || ch == '_' || ch == '-' ||
          ch == 0xb7 || (ch >= 0xc0 && ch <= 0xd6) ||
          (ch >= 0xd8 && ch <= 0xf6) || (ch >= 0xf8 && ch <= 0x37d) ||
          (ch >= 0x37f && ch <= 0x1fff) || (ch >= 0x200c && ch <= 0x200d) ||
          ch == 0x203f || ch == 0x2040 || (ch >= 0x2070 && ch <= 0x218f) ||
          (ch >= 0x2c00 && ch <= 0x2fef) || (ch >= 0x3001 && ch <= 0xd7ff) ||
          (ch >= 0xf900 && ch <= 0xfdcf) || (ch >= 0xfdf0 && ch <= 0xfffd) ||
          (ch >= 0x10000 && ch <= 0xeffff);
    }

    private bool isNameStartChar(int ch) {
      return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') ||
          (ch >= 0xc0 && ch <= 0xd6) || (ch >= 0xd8 && ch <= 0xf6) ||
          (ch >= 0xf8 && ch <= 0x2ff) || (ch >= 0x370 && ch <= 0x37d) ||
          (ch >= 0x37f && ch <= 0x1fff) || (ch >= 0x200c && ch <= 0x200d) ||
          (ch >= 0x2070 && ch <= 0x218f) || (ch >= 0x2c00 && ch <= 0x2fef) ||
          (ch >= 0x3001 && ch <= 0xd7ff) || (ch >= 0xf900 && ch <= 0xfdcf) ||
          (ch >= 0xfdf0 && ch <= 0xfffd) || (ch >= 0x10000 && ch <= 0xeffff);
    }

    private bool isNameStartCharU(int ch) {
      return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch ==
          '_' || (ch >= 0xc0 && ch <= 0xd6) || (ch >= 0xd8 && ch <= 0xf6) ||
            (ch >= 0xf8 && ch <= 0x2ff) || (ch >= 0x370 && ch <= 0x37d) ||
            (ch >= 0x37f && ch <= 0x1fff) || (ch >= 0x200c && ch <= 0x200d) ||
            (ch >= 0x2070 && ch <= 0x218f) || (ch >= 0x2c00 && ch <= 0x2fef) ||
            (ch >= 0x3001 && ch <= 0xd7ff) || (ch >= 0xf900 && ch <= 0xfdcf) ||
            (ch >= 0xfdf0 && ch <= 0xfffd) || (ch >= 0x10000 && ch <= 0xeffff);
    }

    /// <summary>Not documented yet.</summary>
    /// <returns>An ISet(RDFTriple) object.</returns>
  ///
  ///
    public ISet<RDFTriple> Parse() {
      ISet<RDFTriple> triples = new HashSet<RDFTriple>();
      while (true) {
        this.skipWhitespace();
        int mark = this.input.setHardMark();
        int ch = this.input.ReadChar();
        if (ch < 0) {
          RDFInternal.replaceBlankNodes(triples, this.bnodeLabels);
          return triples;
        }
        if (ch == '@') {
          ch = this.input.ReadChar();
          if (ch == 'p' && this.input.ReadChar() == 'r' &&
            this.input.ReadChar() == 'e' &&
              this.input.ReadChar() == 'f' && this.input.ReadChar() == 'i' &&
              this.input.ReadChar() == 'x' && this.skipWhitespace()) {
            this.readPrefixStatement(false);
            continue;
          } else if (ch == 'b' && this.input.ReadChar() == 'a' &&
            this.input.ReadChar() == 's' &&
                    this.input.ReadChar() == 'e' && this.skipWhitespace()) {
            this.readBase(false);
            continue;
          } else {
            throw new ParserException();
          }
        } else if (ch == 'b' || ch == 'B') {
          var c2 = 0;
          if (((c2 = this.input.ReadChar()) == 'A' || c2 == 'a') &&
              ((c2 = this.input.ReadChar()) == 'S' || c2 == 's') &&
           ((c2 = this.input.ReadChar()) == 'E' || c2 == 'e') &&
                this.skipWhitespace()) {
            this.readBase(true);
            continue;
          } else {
            this.input.setMarkPosition(mark);
          }
        } else if (ch == 'p' || ch == 'P') {
          var c2 = 0;
          if (((c2 = this.input.ReadChar()) == 'R' || c2 == 'r') &&
              ((c2 = this.input.ReadChar()) == 'E' || c2 == 'e') &&
              ((c2 = this.input.ReadChar()) == 'F' || c2 == 'f') &&
              ((c2 = this.input.ReadChar()) == 'I' || c2 == 'i') &&
           ((c2 = this.input.ReadChar()) == 'X' || c2 == 'x') &&
                this.skipWhitespace()) {
            this.readPrefixStatement(true);
            continue;
          } else {
            this.input.setMarkPosition(mark);
          }
        } else {
          this.input.setMarkPosition(mark);
        }
        this.readTriples(triples);
      }
    }

    private void readBase(bool sparql) {
      if (this.input.ReadChar() != '<') {
        throw new ParserException();
      }
      this.baseURI = this.readIriReference();
      if (!sparql) {
        this.skipWhitespace();
        if (this.input.ReadChar() != '.') {
          throw new ParserException();
        }
      } else {
        this.skipWhitespace();
      }
    }

    private string readBlankNodeLabel() {
      var ilist = new StringBuilder();
      int startChar = this.input.ReadChar();
      if (!this.isNameStartCharU(startChar) &&
           (startChar < '0' || startChar > '9')) {
        throw new ParserException();
      }
      if (startChar <= 0xffff) {
        {
          ilist.Append((char)startChar);
        }
      } else if (startChar <= 0x10ffff) {
        ilist.Append((char)((((startChar - 0x10000) >> 10) & 0x3ff) + 0xd800));
        ilist.Append((char)(((startChar - 0x10000) & 0x3ff) + 0xdc00));
      }
      var lastIsPeriod = false;
      this.input.setSoftMark();
      while (true) {
        int ch = this.input.ReadChar();
        if (ch == '.') {
          int position = this.input.getMarkPosition();
          int ch2 = this.input.ReadChar();
          if (!this.isNameChar(ch2) && ch2 != ':' && ch2 != '.') {
            this.input.setMarkPosition(position - 1);
            return ilist.ToString();
          } else {
            this.input.moveBack(1);
          }
          if (ch <= 0xffff) {
            {
              ilist.Append((char)ch);
            }
          } else if (ch <= 0x10ffff) {
            ilist.Append((char)((((ch - 0x10000) >> 10) & 0x3ff) + 0xd800));
            ilist.Append((char)(((ch - 0x10000) & 0x3ff) + 0xdc00));
          }
          lastIsPeriod = true;
        } else if (this.isNameChar(ch)) {
          if (ch <= 0xffff) {
            {
              ilist.Append((char)ch);
            }
          } else if (ch <= 0x10ffff) {
            ilist.Append((char)((((ch - 0x10000) >> 10) & 0x3ff) + 0xd800));
            ilist.Append((char)(((ch - 0x10000) & 0x3ff) + 0xdc00));
          }
          lastIsPeriod = false;
        } else {
          if (ch >= 0) {
            this.input.moveBack(1);
          }
          if (lastIsPeriod) {
            throw new ParserException();
          }
          return ilist.ToString();
        }
      }
    }

    private TurtleObject readBlankNodePropertyList() {
      TurtleObject obj = TurtleObject.newPropertyList();
      var havePredObject = false;
      while (true) {
        this.skipWhitespace();
        int ch;
        if (havePredObject) {
          var haveSemicolon = false;
          while (true) {
            this.input.setSoftMark();
            ch = this.input.ReadChar();
            if (ch == ';') {
              this.skipWhitespace();
              haveSemicolon = true;
            } else {
              if (ch >= 0) {
                this.input.moveBack(1);
              }
              break;
            }
          }
          if (!haveSemicolon) {
            break;
          }
        }
        RDFTerm pred = this.readPredicate();
        if (pred == null) {
          break;
        }
        havePredObject = true;
        this.readObjectListToProperties(pred, obj);
      }
      if (this.input.ReadChar() != ']') {
        throw new ParserException();
      }
      return obj;
    }

    private TurtleObject readCollection() {
      TurtleObject obj = TurtleObject.newCollection();
      while (true) {
        this.skipWhitespace();
        this.input.setHardMark();
        int ch = this.input.ReadChar();
        if (ch == ')') {
          break;
        } else {
          if (ch >= 0) {
            this.input.moveBack(1);
          }
          TurtleObject subobj = this.readObject(true);
          obj.getObjects().Add(subobj);
        }
      }
      return obj;
    }

    private string readIriReference() {
      var ilist = new StringBuilder();
      while (true) {
        int ch = this.input.ReadChar();
        if (ch < 0) {
          throw new ParserException();
        }
        if (ch == '>') {
          string iriref = ilist.ToString();
          // Resolve the IRI reference relative
          // to the _base URI
          iriref = URIUtility.relativeResolve(iriref, this.baseURI);
          if (iriref == null) {
            throw new ParserException();
          }
          return iriref;
        } else if (ch == '\\') {
          ch = this.readUnicodeEscape(false);
        }
        if (ch <= 0x20 || ((ch & 0x7f) == ch &&
                "><\\\"{}|^`".IndexOf((char)ch) >= 0)) {
          throw new ParserException();
        }
        if (ch <= 0xffff) {
          {
            ilist.Append((char)ch);
          }
        } else if (ch <= 0x10ffff) {
          ilist.Append((char)((((ch - 0x10000) >> 10) & 0x3ff) + 0xd800));
          ilist.Append((char)(((ch - 0x10000) & 0x3ff) + 0xdc00));
        }
      }
    }

    private string readLanguageTag() {
      var ilist = new StringBuilder();
      var hyphen = false;
      var haveHyphen = false;
      var haveString = false;
      this.input.setSoftMark();
      while (true) {
        int c2 = this.input.ReadChar();
        if (c2 >= 'A' && c2 <= 'Z') {
          if (c2 <= 0xffff) {
            {
              ilist.Append((char)c2);
            }
          } else if (c2 <= 0x10ffff) {
            ilist.Append((char)((((c2 - 0x10000) >> 10) & 0x3ff) + 0xd800));
            ilist.Append((char)(((c2 - 0x10000) & 0x3ff) + 0xdc00));
          }
          haveString = true;
          hyphen = false;
        } else if (c2 >= 'a' && c2 <= 'z') {
              ilist.Append((char)c2);
          haveString = true;
          hyphen = false;
        } else if (haveHyphen && (c2 >= '0' && c2 <= '9')) {
              ilist.Append((char)c2);
          haveString = true;
          hyphen = false;
        } else if (c2 == '-') {
          if (hyphen || !haveString) {
            throw new ParserException();
          }
          if (c2 <= 0xffff) {
            {
              ilist.Append((char)c2);
            }
          } else if (c2 <= 0x10ffff) {
            ilist.Append((char)((((c2 - 0x10000) >> 10) & 0x3ff) + 0xd800));
            ilist.Append((char)(((c2 - 0x10000) & 0x3ff) + 0xdc00));
          }
          hyphen = true;
          haveHyphen = true;
          haveString = true;
        } else {
          if (c2 >= 0) {
            this.input.moveBack(1);
          }
          if (hyphen || !haveString) {
            throw new ParserException();
          }
          return ilist.ToString();
        }
      }
    }

    // Reads a number literal starting with
    // the given character (assumes it's plus, minus,
    // a dot, or a digit)
    private RDFTerm readNumberLiteral(int ch) {
      // buffer to hold the literal
      var ilist = new StringBuilder();
      // include the first character
      if (ch <= 0xffff) {
        {
          ilist.Append((char)ch);
        }
      } else if (ch <= 0x10ffff) {
        ilist.Append((char)((((ch - 0x10000) >> 10) & 0x3ff) + 0xd800));
        ilist.Append((char)(((ch - 0x10000) & 0x3ff) + 0xdc00));
      }
      bool haveDigits = ch >= '0' && ch <= '9';
      bool haveDot = ch == '.';
      this.input.setHardMark();
      while (true) {
        int ch1 = this.input.ReadChar();
        if (haveDigits && (ch1 == 'e' || ch1 == 'E')) {
          // Parse exponent
          if (ch1 <= 0xffff) {
            {
              ilist.Append((char)ch1);
            }
          } else if (ch1 <= 0x10ffff) {
            ilist.Append((char)((((ch1 - 0x10000) >> 10) & 0x3ff) + 0xd800));
            ilist.Append((char)(((ch1 - 0x10000) & 0x3ff) + 0xdc00));
          }
          ch1 = this.input.ReadChar();
          haveDigits = false;
          if (ch1 == '+' || ch1 == '-' || (ch1 >= '0' && ch1 <= '9')) {
            if (ch1 <= 0xffff) {
              {
                ilist.Append((char)ch1);
              }
            } else if (ch1 <= 0x10ffff) {
              ilist.Append((char)((((ch1 - 0x10000) >> 10) & 0x3ff) + 0xd800));
              ilist.Append((char)(((ch1 - 0x10000) & 0x3ff) + 0xdc00));
            }
            if (ch1 >= '0' && ch1 <= '9') {
              haveDigits = true;
            }
          } else {
            throw new ParserException();
          }
          this.input.setHardMark();
          while (true) {
            ch1 = this.input.ReadChar();
            if (ch1 >= '0' && ch1 <= '9') {
              haveDigits = true;
              if (ch1 <= 0xffff) {
                {
                  ilist.Append((char)ch1);
                }
              } else if (ch1 <= 0x10ffff) {
                ilist.Append((char)((((ch1 - 0x10000) >> 10) & 0x3ff) +
                    0xd800));
                ilist.Append((char)(((ch1 - 0x10000) & 0x3ff) + 0xdc00));
              }
            } else {
              if (ch1 >= 0) {
                this.input.moveBack(1);
              }
              if (!haveDigits) {
                throw new ParserException();
              }
              return RDFTerm.fromTypedString(
  ilist.ToString(),
  "http://www.w3.org/2001/XMLSchema#double");
            }
          }
        } else if (ch1 >= '0' && ch1 <= '9') {
          haveDigits = true;
          if (ch1 <= 0xffff) {
            {
              ilist.Append((char)ch1);
            }
          } else if (ch1 <= 0x10ffff) {
            ilist.Append((char)((((ch1 - 0x10000) >> 10) & 0x3ff) + 0xd800));
            ilist.Append((char)(((ch1 - 0x10000) & 0x3ff) + 0xdc00));
          }
        } else if (!haveDot && ch1 == '.') {
          haveDot = true;
          // check for non-digit and non-E
          int markpos = this.input.getMarkPosition();
          int ch2 = this.input.ReadChar();
          if (ch2 != 'e' && ch2 != 'E' && (ch2 < '0' || ch2 > '9')) {
            // move to just at the period and return
            this.input.setMarkPosition(markpos - 1);
            if (!haveDigits) {
              throw new ParserException();
            }
            string ns = haveDot ? "http://www.w3.org/2001/XMLSchema#decimal" :
                "http://www.w3.org/2001/XMLSchema#integer";
            return RDFTerm.fromTypedString(
  ilist.ToString(),
  ns);
          } else {
            this.input.moveBack(1);
          }
          if (ch1 <= 0xffff) {
            {
              ilist.Append((char)ch1);
            }
          } else if (ch1 <= 0x10ffff) {
            ilist.Append((char)((((ch1 - 0x10000) >> 10) & 0x3ff) + 0xd800));
            ilist.Append((char)(((ch1 - 0x10000) & 0x3ff) + 0xdc00));
          }
        } else {  // no more digits
          if (ch1 >= 0) {
            this.input.moveBack(1);
          }
          if (!haveDigits) {
            throw new ParserException();
          }
          string ns = haveDot ? "http://www.w3.org/2001/XMLSchema#decimal" :
              "http://www.w3.org/2001/XMLSchema#integer";
          return RDFTerm.fromTypedString(
  ilist.ToString(),
  ns);
        }
      }
    }

    private TurtleObject readObject(bool acceptLiteral) {
      int ch = this.input.ReadChar();
      int mark = this.input.setSoftMark();
      if (ch < 0) {
        throw new ParserException();
      } else if (ch == '<') {
        return TurtleObject.fromTerm(
  RDFTerm.fromIRI(this.readIriReference()));
      } else if (acceptLiteral && (ch == '-' || ch == '+' || ch == '.' ||
        (ch >= '0' && ch <= '9'))) {
        return TurtleObject.fromTerm(this.readNumberLiteral(ch));
      } else if (acceptLiteral && (ch == '\'' || ch == '\"')) {
        // start of quote literal
        string str = this.readStringLiteral(ch);
        return TurtleObject.fromTerm(this.finishStringLiteral(str));
      } else if (ch == '_') {  // Blank Node Label
        if (this.input.ReadChar() != ':') {
          throw new ParserException();
        }
        string label = this.readBlankNodeLabel();
        RDFTerm term = this.bnodeLabels.ContainsKey(label) ?
                    this.bnodeLabels[label] : null;
        if (term == null) {
          term = RDFTerm.fromBlankNode(label);
          this.bnodeLabels.Add(label, term);
        }
        return TurtleObject.fromTerm(term);
      } else if (ch == '[') {
        return this.readBlankNodePropertyList();
      } else if (ch == '(') {
        return this.readCollection();
      } else if (ch == ':') {  // prefixed name with current prefix
        string scope = this.namespaces[String.Empty];
        if (scope == null) {
          throw new ParserException();
        }
        return TurtleObject.fromTerm(
            RDFTerm.fromIRI(scope + this.readOptionalLocalName()));
      } else if (this.isNameStartChar(ch)) {  // prefix
        if (acceptLiteral && (ch == 't' || ch == 'f')) {
          mark = this.input.setHardMark();
          if (ch == 't' && this.input.ReadChar() == 'r' &&
            this.input.ReadChar() == 'u' &&
              this.input.ReadChar() == 'e' && this.isBooleanLiteralEnd()) {
            return TurtleObject.fromTerm(RDFTerm.TRUE);
          } else if (ch == 'f' && this.input.ReadChar() == 'a' &&
            this.input.ReadChar() == 'l' && this.input.ReadChar() == 's' &&
                this.input.ReadChar() == 'e' && this.isBooleanLiteralEnd()) {
            return TurtleObject.fromTerm(RDFTerm.FALSE);
          } else {
            this.input.setMarkPosition(mark);
          }
        }
        string prefix = this.readPrefix(ch);
        string scope = this.namespaces.ContainsKey(prefix) ?
                    this.namespaces[prefix] : null;
        if (scope == null) {
          throw new ParserException();
        }
        return TurtleObject.fromTerm(
            RDFTerm.fromIRI(scope + this.readOptionalLocalName()));
      } else {
        this.input.setMarkPosition(mark);
        return null;
      }
    }

    private void readObjectList(ISet<RDFTriple> triples) {
      var haveObject = false;
      while (true) {
        this.input.setSoftMark();
        int ch;
        if (haveObject) {
          ch = this.input.ReadChar();
          if (ch != ',') {
            if (ch >= 0) {
              this.input.moveBack(1);
            }
            break;
          }
          this.skipWhitespace();
        }
        // Read object
        TurtleObject obj = this.readObject(true);
        if (obj == null) {
          if (!haveObject) {
            throw new ParserException();
          } else {
            return;
          }
        }
        haveObject = true;
        this.emitRDFTriple(this.curSubject, this.curPredicate, obj, triples);
        this.skipWhitespace();
      }
      if (!haveObject) {
        throw new ParserException();
      }
      return;
    }

    private void readObjectListToProperties(
        RDFTerm predicate,
        TurtleObject propertyList) {
      var haveObject = false;
      while (true) {
        this.input.setSoftMark();
        int ch;
        if (haveObject) {
          ch = this.input.ReadChar();
          if (ch != ',') {
            if (ch >= 0) {
              this.input.moveBack(1);
            }
            break;
          }
          this.skipWhitespace();
        }
        // Read object
        TurtleObject obj = this.readObject(true);
        if (obj == null) {
          if (!haveObject) {
            throw new ParserException();
          } else {
            return;
          }
        }
        var prop = new TurtleProperty();
        prop.Pred = predicate;
        prop.Obj = obj;
        propertyList.getProperties().Add(prop);
        this.skipWhitespace();
        haveObject = true;
      }
      if (!haveObject) {
        throw new ParserException();
      }
      return;
    }

    private string readOptionalLocalName() {
      var ilist = new StringBuilder();
      var lastIsPeriod = false;
      var first = true;
      this.input.setSoftMark();
      while (true) {
        int ch = this.input.ReadChar();
        if (ch < 0) {
          return ilist.ToString();
        }
        if (ch == '%') {
          int a = this.input.ReadChar();
          int b = this.input.ReadChar();
          if (this.toHexValue(a) < 0 ||
              this.toHexValue(b) < 0) {
            throw new ParserException();
          }
          if (ch <= 0xffff) {
            {
              ilist.Append((char)ch);
            }
          } else if (ch <= 0x10ffff) {
            ilist.Append((char)((((ch - 0x10000) >> 10) & 0x3ff) + 0xd800));
            ilist.Append((char)(((ch - 0x10000) & 0x3ff) + 0xdc00));
          }
          if (a <= 0xffff) {
            {
              ilist.Append((char)a);
            }
          } else if (a <= 0x10ffff) {
            ilist.Append((char)((((a - 0x10000) >> 10) & 0x3ff) + 0xd800));
            ilist.Append((char)(((a - 0x10000) & 0x3ff) + 0xdc00));
          }
          if (b <= 0xffff) {
            {
              ilist.Append((char)b);
            }
          } else if (b <= 0x10ffff) {
            ilist.Append((char)((((b - 0x10000) >> 10) & 0x3ff) + 0xd800));
            ilist.Append((char)(((b - 0x10000) & 0x3ff) + 0xdc00));
          }
          lastIsPeriod = false;
          first = false;
          continue;
        } else if (ch == '\\') {
          ch = this.input.ReadChar();
          if ((ch & 0x7f) == ch &&
                  "_~.-!$&'()*+,;=/?#@%".IndexOf((char)ch) >= 0) {
            if (ch <= 0xffff) {
              {
                ilist.Append((char)ch);
              }
            } else if (ch <= 0x10ffff) {
              ilist.Append((char)((((ch - 0x10000) >> 10) & 0x3ff) + 0xd800));
              ilist.Append((char)(((ch - 0x10000) & 0x3ff) + 0xdc00));
            }
          } else {
            throw new ParserException();
          }
          lastIsPeriod = false;
          first = false;
          continue;
        }
        if (first) {
          if (!this.isNameStartCharU(ch) && ch != ':' &&
               (ch < '0' || ch > '9')) {
            this.input.moveBack(1);
            return ilist.ToString();
          }
        } else {
          if (!this.isNameChar(ch) && ch != ':' && ch != '.') {
            this.input.moveBack(1);
            if (lastIsPeriod) {
              throw new ParserException();
            }
            return ilist.ToString();
          }
        }
        lastIsPeriod = ch == '.';
        if (lastIsPeriod && !first) {
          // if a period was just read, check
          // if the next character is valid before
          // adding the period.
          int position = this.input.getMarkPosition();
          int ch2 = this.input.ReadChar();
          if (!this.isNameChar(ch2) && ch2 != ':' && ch2 != '.') {
            this.input.setMarkPosition(position - 1);
            return ilist.ToString();
          } else {
            this.input.moveBack(1);
          }
        }
        first = false;
        if (ch <= 0xffff) {
          {
            ilist.Append((char)ch);
          }
        } else if (ch <= 0x10ffff) {
          ilist.Append((char)((((ch - 0x10000) >> 10) & 0x3ff) + 0xd800));
          ilist.Append((char)(((ch - 0x10000) & 0x3ff) + 0xdc00));
        }
      }
    }

    private RDFTerm readPredicate() {
      int mark = this.input.setHardMark();
      int ch = this.input.ReadChar();
      RDFTerm predicate = null;
      if (ch == 'a') {
        mark = this.input.setHardMark();
        if (this.skipWhitespace()) {
          return RDFTerm.A;
        } else {
          this.input.setMarkPosition(mark);
          string prefix = this.readPrefix('a');
          string scope = this.namespaces[prefix];
          if (scope == null) {
            throw new ParserException();
          }
          predicate = RDFTerm.fromIRI(scope + this.readOptionalLocalName());
          this.skipWhitespace();
          return predicate;
        }
      } else if (ch == '<') {
        predicate = RDFTerm.fromIRI(this.readIriReference());
        this.skipWhitespace();
        return predicate;
      } else if (ch == ':') {  // prefixed name with current prefix
        string scope = this.namespaces[String.Empty];
        if (scope == null) {
          throw new ParserException();
        }
        predicate = RDFTerm.fromIRI(scope + this.readOptionalLocalName());
        this.skipWhitespace();
        return predicate;
      } else if (this.isNameStartChar(ch)) {  // prefix
        string prefix = this.readPrefix(ch);
        string scope = this.namespaces[prefix];
        if (scope == null) {
          throw new ParserException();
        }
        predicate = RDFTerm.fromIRI(scope + this.readOptionalLocalName());
        this.skipWhitespace();
        return predicate;
      } else {
        this.input.setMarkPosition(mark);
        return null;
      }
    }

    private void readPredicateObjectList(ISet<RDFTriple> triples) {
      var havePredObject = false;
      while (true) {
        int ch;
        this.skipWhitespace();
        if (havePredObject) {
          var haveSemicolon = false;
          while (true) {
            this.input.setSoftMark();
            ch = this.input.ReadChar();
            // Console.WriteLine("nextchar %c",(char)ch);
            if (ch == ';') {
              this.skipWhitespace();
              haveSemicolon = true;
            } else {
              if (ch >= 0) {
                this.input.moveBack(1);
              }
              break;
            }
          }
          if (!haveSemicolon) {
            break;
          }
        }
        this.curPredicate = this.readPredicate();
        // Console.WriteLine("predobjlist %s",curPredicate);
        if (this.curPredicate == null) {
          if (!havePredObject) {
            throw new ParserException();
          } else {
            break;
          }
        }
        // Read _object
        havePredObject = true;
        this.readObjectList(triples);
      }
      if (!havePredObject) {
        throw new ParserException();
      }
      return;
    }

    private bool isBooleanLiteralEnd() {
      if (this.skipWhitespace()) {
        return true;
      }
      this.input.setSoftMark();
      int ch = this.input.ReadChar();
      if (ch < 0) {
        return true;
      }
      this.input.moveBack(1);
       return this.isNameChar(ch);
    }

    private string readPrefix(int startChar) {
      var ilist = new StringBuilder();
      var lastIsPeriod = false;
      var first = true;
      if (startChar >= 0) {
        if (startChar <= 0xffff) {
          {
            ilist.Append((char)startChar);
          }
        } else if (startChar <= 0x10ffff) {
          ilist.Append((char)((((startChar - 0x10000) >> 10) & 0x3ff) +
              0xd800));
          ilist.Append((char)(((startChar - 0x10000) & 0x3ff) + 0xdc00));
        }
        first = false;
      }
      while (true) {
        int ch = this.input.ReadChar();
        if (ch < 0) {
          throw new ParserException();
        }
        if (ch == ':') {
          if (lastIsPeriod) {
            throw new ParserException();
          }
          return ilist.ToString();
        } else if (first && !this.isNameStartChar(ch)) {
          throw new ParserException();
        } else if (ch != '.' && !this.isNameChar(ch)) {
          throw new ParserException();
        }
        first = false;
        if (ch <= 0xffff) {
          {
            ilist.Append((char)ch);
          }
        } else if (ch <= 0x10ffff) {
          ilist.Append((char)((((ch - 0x10000) >> 10) & 0x3ff) + 0xd800));
          ilist.Append((char)(((ch - 0x10000) & 0x3ff) + 0xdc00));
        }
        lastIsPeriod = ch == '.';
      }
    }

    private void readPrefixStatement(bool sparql) {
      string prefix = this.readPrefix(-1);
      this.skipWhitespace();
      if (this.input.ReadChar() != '<') {
        throw new ParserException();
      }
      string iri = this.readIriReference();
      this.namespaces.Add(prefix, iri);
      if (!sparql) {
        this.skipWhitespace();
        if (this.input.ReadChar() != '.') {
          throw new ParserException();
        }
      } else {
        this.skipWhitespace();
      }
    }

    private string readStringLiteral(int ch) {
      var ilist = new StringBuilder();
      var first = true;
      var longQuote = false;
      var quotecount = 0;
      while (true) {
        int c2 = this.input.ReadChar();
        if (first && c2 == ch) {
          this.input.setHardMark();
          c2 = this.input.ReadChar();
          if (c2 != ch) {
            if (c2 >= 0) {
              this.input.moveBack(1);
            }
            return String.Empty;
          }
          longQuote = true;
          c2 = this.input.ReadChar();
        }
        first = false;
        if (!longQuote && (c2 == 0x0a || c2 == 0x0d)) {
          throw new ParserException();
        } else if (c2 == '\\') {
          c2 = this.readUnicodeEscape(true);
          if (quotecount >= 2) {
            if (ch <= 0xffff) {
              {
                ilist.Append((char)ch);
              }
            } else if (ch <= 0x10ffff) {
              ilist.Append((char)((((ch - 0x10000) >> 10) & 0x3ff) + 0xd800));
              ilist.Append((char)(((ch - 0x10000) & 0x3ff) + 0xdc00));
            }
          }
          if (quotecount >= 1) {
            if (ch <= 0xffff) {
              {
                ilist.Append((char)ch);
              }
            } else if (ch <= 0x10ffff) {
              ilist.Append((char)((((ch - 0x10000) >> 10) & 0x3ff) + 0xd800));
              ilist.Append((char)(((ch - 0x10000) & 0x3ff) + 0xdc00));
            }
          }
          if (c2 <= 0xffff) {
            {
              ilist.Append((char)c2);
            }
          } else if (c2 <= 0x10ffff) {
            ilist.Append((char)((((c2 - 0x10000) >> 10) & 0x3ff) + 0xd800));
            ilist.Append((char)(((c2 - 0x10000) & 0x3ff) + 0xdc00));
          }
          quotecount = 0;
        } else if (c2 == ch) {
          if (!longQuote) {
            return ilist.ToString();
          }
          ++quotecount;
          if (quotecount >= 3) {
            return ilist.ToString();
          }
        } else {
          if (c2 < 0) {
            throw new ParserException();
          }
          if (quotecount >= 2) {
            if (ch <= 0xffff) {
              {
                ilist.Append((char)ch);
              }
            } else if (ch <= 0x10ffff) {
              ilist.Append((char)((((ch - 0x10000) >> 10) & 0x3ff) + 0xd800));
              ilist.Append((char)(((ch - 0x10000) & 0x3ff) + 0xdc00));
            }
          }
          if (quotecount >= 1) {
            if (ch <= 0xffff) {
              {
                ilist.Append((char)ch);
              }
            } else if (ch <= 0x10ffff) {
              ilist.Append((char)((((ch - 0x10000) >> 10) & 0x3ff) + 0xd800));
              ilist.Append((char)(((ch - 0x10000) & 0x3ff) + 0xdc00));
            }
          }
          if (c2 <= 0xffff) {
            {
              ilist.Append((char)c2);
            }
          } else if (c2 <= 0x10ffff) {
            ilist.Append((char)((((c2 - 0x10000) >> 10) & 0x3ff) + 0xd800));
            ilist.Append((char)(((c2 - 0x10000) & 0x3ff) + 0xdc00));
          }
          quotecount = 0;
        }
      }
    }

    private void readTriples(ISet<RDFTriple> triples) {
      int mark = this.input.setHardMark();
      int ch = this.input.ReadChar();
      if (ch < 0) {
        return;
      }
      this.input.setMarkPosition(mark);
      TurtleObject subject = this.readObject(false);
      if (subject == null) {
        throw new ParserException();
      }
      this.curSubject = subject;
      if (!(subject.Kind == TurtleObject.PROPERTIES &&
          subject.getProperties().Count > 0)) {
        this.skipWhitespace();
        this.readPredicateObjectList(triples);
      } else {
        this.skipWhitespace();
        this.input.setHardMark();
        ch = this.input.ReadChar();
        if (ch == '.') {
          // just a blank node property list;
          // generate a blank node as the subject
          RDFTerm blankNode = this.AllocateBlankNode();
          foreach (var prop in subject.getProperties()) {
            this.emitRDFTriple(blankNode, prop.Pred, prop.Obj, triples);
          }
          return;
        } else if (ch < 0) {
          throw new ParserException();
        }
        this.input.moveBack(1);
        this.readPredicateObjectList(triples);
      }
      this.skipWhitespace();
      if (this.input.ReadChar() != '.') {
        throw new ParserException();
      }
    }

    private int readUnicodeEscape(bool extended) {
      int ch = this.input.ReadChar();
      if (ch == 'U') {
        if (this.input.ReadChar() != '0') {
          throw new ParserException();
        }
        if (this.input.ReadChar() != '0') {
          throw new ParserException();
        }
        int a = this.toHexValue(this.input.ReadChar());
        int b = this.toHexValue(this.input.ReadChar());
        int c = this.toHexValue(this.input.ReadChar());
        int d = this.toHexValue(this.input.ReadChar());
        int e = this.toHexValue(this.input.ReadChar());
        int f = this.toHexValue(this.input.ReadChar());
        if (a < 0 || b < 0 || c < 0 || d < 0 || e < 0 || f < 0) {
          throw new ParserException();
        }
        ch = (a << 20) | (b << 16) | (c << 12) | (d << 8) | (e << 4) | f;
      } else if (ch == 'u') {
        int a = this.toHexValue(this.input.ReadChar());
        int b = this.toHexValue(this.input.ReadChar());
        int c = this.toHexValue(this.input.ReadChar());
        int d = this.toHexValue(this.input.ReadChar());
        if (a < 0 || b < 0 || c < 0 || d < 0) {
          throw new ParserException();
        }
        ch = (a << 12) | (b << 8) | (c << 4) | d;
      } else if (extended && ch == 't') {
        return '\t';
      } else if (extended && ch == 'b') {
        return '\b';
      } else if (extended && ch == 'n') {
        return '\n';
      } else if (extended && ch == 'r') {
        return '\r';
      } else if (extended && ch == 'f') {
        return '\f';
      } else if (extended && ch == '\'') {
        return '\'';
      } else if (extended && ch == '\\') {
        return '\\';
      } else if (extended && ch == '"') {
        return '\"';
      } else {
        throw new ParserException();
      }
      // Reject surrogate code points
      // as Unicode escapes
      if ((ch & 0xf800) == 0xd800) {
        throw new ParserException();
      }
      return ch;
    }

    private bool skipWhitespace() {
      var haveWhitespace = false;
      this.input.setSoftMark();
      while (true) {
        int ch = this.input.ReadChar();
        if (ch == '#') {
          while (true) {
            ch = this.input.ReadChar();
            if (ch < 0) {
              return true;
            }
            if (ch == 0x0d || ch == 0x0a) {
              break;
            }
          }
        } else if (ch != 0x09 && ch != 0x0a && ch != 0x0d && ch != 0x20) {
          if (ch >= 0) {
            this.input.moveBack(1);
          }
          return haveWhitespace;
        }
        haveWhitespace = true;
      }
    }

    private int toHexValue(int a) {
      if (a >= '0' && a <= '9') {
        return a - '0';
      }
      return (a >= 'a' && a <= 'f') ? (a + 10 - 'a') : ((a >= 'A' && a <= 'F') ?
        (a + 10 - 'A') : (-1));
    }
  }
}
