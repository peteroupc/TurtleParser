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
using System.Globalization;
using System.Collections.Generic;
using PeterO;
  using PeterO.Text;
public class TurtleParser : IRDFParser {
  private class TurtleObject {
    public const int SIMPLE = 0;
    public const int COLLECTION = 1;
    public const int PROPERTIES = 2;
    public static TurtleObject fromTerm(RDFTerm term) {
      var tobj = new TurtleObject();
      tobj.term = term;
      tobj.kind = TurtleObject.SIMPLE;
      return tobj;
    }
    public static TurtleObject newCollection() {
      var tobj = new TurtleObject();
      tobj.objects = new List<TurtleObject>();
      tobj.kind = TurtleObject.COLLECTION;
      return tobj;
    }
    public static TurtleObject newPropertyList() {
      var tobj = new TurtleObject();
      tobj.properties = new List<TurtleProperty>();
      tobj.kind = TurtleObject.PROPERTIES;
      return tobj;
    }
    public RDFTerm term;

    public int kind;
    IList<TurtleObject> objects;

    IList<TurtleProperty> properties;
    public IList<TurtleObject> getObjects() {
      return objects;
    }
    public IList<TurtleProperty> getProperties() {
      return properties;
    }
  }
  private class TurtleProperty {
    public RDFTerm pred;
    public TurtleObject obj;
  }
  private IDictionary<string, RDFTerm> bnodeLabels;
  private IDictionary<string, string> namespaces;

  private string baseURI;

  private TurtleObject curSubject;

  private RDFTerm curPredicate;

  private StackableCharacterInput input;
  private int curBlankNode = 0;

  public TurtleParser(PeterO.IByteReader stream) :
    this(stream,"about:blank") {
  }

  public TurtleParser(PeterO.IByteReader stream, string baseURI) {
    if ((stream) == null) {
 throw new ArgumentNullException("stream");
}
    if (baseURI == null) {
 throw new ArgumentNullException("baseURI");
}
    if (!URIUtility.hasScheme(baseURI)) {
 throw new ArgumentException("baseURI");
}
    this.input = new StackableCharacterInput(
        Encodings.GetDecoderInput(Encodings.UTF8, stream));
    this.baseURI = baseURI;
    bnodeLabels = new Dictionary<string, RDFTerm>();
    namespaces = new Dictionary<string, string>();
  }

  public TurtleParser(string str) : this(str,"about:blank") {
  }

  public TurtleParser(string str, string baseURI) {
    if ((str) == null) {
 throw new ArgumentNullException("str");
}
    if (baseURI == null) {
 throw new ArgumentNullException("baseURI");
}
    if (!URIUtility.hasScheme(baseURI)) {
 throw new ArgumentException("baseURI");
}
    this.input = new StackableCharacterInput(
        Encodings.StringToInput(str));
    this.baseURI = baseURI;
    bnodeLabels = new Dictionary<string, RDFTerm>();
    namespaces = new Dictionary<string, string>();
  }
  private RDFTerm allocateBlankNode() {
    ++curBlankNode;
    // A period is included so as not to conflict
    // with user-defined blank node labels (this is allowed
    // because the syntax for blank node identifiers is
    // not concretely defined)
  string label="."
      +Convert.ToString(curBlankNode, CultureInfo.InvariantCulture);
    RDFTerm node = RDFTerm.fromBlankNode(label);
    bnodeLabels.Add(label, node);
    return node;
  }

  private void emitRDFTriple(RDFTerm subj, RDFTerm pred, RDFTerm obj,
    ISet<RDFTriple> triples) {
    var triple = new RDFTriple(subj, pred, obj);
    triples.Add(triple);
  }

  private void emitRDFTriple(RDFTerm subj, RDFTerm pred,
      TurtleObject obj, ISet<RDFTriple> triples) {
    if (obj.kind == TurtleObject.SIMPLE) {
      emitRDFTriple(subj, pred, obj.term, triples);
    } else if (obj.kind == TurtleObject.PROPERTIES) {
      IList<TurtleProperty> props = obj.getProperties();
      if (props.Count == 0) {
        emitRDFTriple(subj, pred, allocateBlankNode(), triples);
      } else {
        RDFTerm blank = allocateBlankNode();
        emitRDFTriple(subj, pred, blank, triples);
        for (int i = 0;i<props.Count; ++i) {
          emitRDFTriple(blank, props[i].pred, props[i].obj, triples);
        }
      }
    } else if (obj.kind == TurtleObject.COLLECTION) {
      IList<TurtleObject> objs = obj.getObjects();
      if (objs.Count == 0) {
        emitRDFTriple(subj, pred, RDFTerm.NIL, triples);
      } else {
        RDFTerm curBlank = allocateBlankNode();
        RDFTerm firstBlank = curBlank;
        emitRDFTriple(curBlank, RDFTerm.FIRST, objs[0], triples);
        for (int i = 1;i <= objs.Count; ++i) {
          if (i == objs.Count) {
            emitRDFTriple(curBlank, RDFTerm.REST, RDFTerm.NIL, triples);
          } else {
            RDFTerm nextBlank = allocateBlankNode();
            emitRDFTriple(curBlank, RDFTerm.REST, nextBlank, triples);
            emitRDFTriple(nextBlank, RDFTerm.FIRST, objs[i], triples);
            curBlank = nextBlank;
          }
        }
        emitRDFTriple(subj, pred, firstBlank, triples);
      }
    }
  }

  private void emitRDFTriple(TurtleObject subj, RDFTerm pred,
      TurtleObject obj, ISet<RDFTriple> triples) {
    if (subj.kind == TurtleObject.SIMPLE) {
      emitRDFTriple(subj.term, pred, obj, triples);
    } else if (subj.kind == TurtleObject.PROPERTIES) {
      IList<TurtleProperty> props = subj.getProperties();
      if (props.Count == 0) {
        emitRDFTriple(allocateBlankNode(), pred, obj, triples);
      } else {
        RDFTerm blank = allocateBlankNode();
        emitRDFTriple(blank, pred, obj, triples);
        for (int i = 0;i<props.Count; ++i) {
          emitRDFTriple(blank, props[i].pred, props[i].obj, triples);
        }
      }
    } else if (subj.kind == TurtleObject.COLLECTION) {
      IList<TurtleObject> objs = subj.getObjects();
      if (objs.Count == 0) {
        emitRDFTriple(RDFTerm.NIL, pred, obj, triples);
      } else {
        RDFTerm curBlank = allocateBlankNode();
        RDFTerm firstBlank = curBlank;
        emitRDFTriple(curBlank, RDFTerm.FIRST, objs[0], triples);
        for (int i = 1;i <= objs.Count; ++i) {
          if (i == objs.Count) {
            emitRDFTriple(curBlank, RDFTerm.REST, RDFTerm.NIL, triples);
          } else {
            RDFTerm nextBlank = allocateBlankNode();
            emitRDFTriple(curBlank, RDFTerm.REST, nextBlank, triples);
            emitRDFTriple(nextBlank, RDFTerm.FIRST, objs[i], triples);
            curBlank = nextBlank;
          }
        }
        emitRDFTriple(firstBlank, pred, obj, triples);
      }
    }
  }

  private RDFTerm finishStringLiteral(string str) {
    int mark = input.setHardMark();
    int ch = input.ReadChar();
    if (ch=='@') {
 return RDFTerm.fromLangString(str, readLanguageTag());
  } else if (ch=='^' && input.ReadChar()=='^') {
      ch = input.ReadChar();
      if (ch=='<') {
 return RDFTerm.fromTypedString(str, readIriReference());
  } else if (ch==':') { // prefixed name with current prefix
        string scope=namespaces[""];
        if (scope == null) {
 throw new ParserException();
}
        return RDFTerm.fromTypedString(str, scope + readOptionalLocalName());
      } else if (isNameStartChar(ch)) {  // prefix
        string prefix = readPrefix(ch);
        string scope = namespaces[prefix];
        if (scope == null) {
 throw new ParserException();
}
        return RDFTerm.fromTypedString(str, scope + readOptionalLocalName());
      } else {
 throw new ParserException();
}
    } else {
      input.setMarkPosition(mark);
      return RDFTerm.fromTypedString(str);
    }
  }

  private bool isNameChar(int ch) {
    return (ch>= 'a' && ch<= 'z') || (ch>= '0' && ch<= '9') ||
        (ch>= 'A' && ch<= 'Z') || ch=='_' || ch=='-' ||
        ch == 0xb7 || (ch >= 0xc0 && ch <= 0xd6) ||
        (ch >= 0xd8 && ch <= 0xf6) || (ch >= 0xf8 && ch <= 0x37d) ||
        (ch >= 0x37f && ch <= 0x1fff) || (ch >= 0x200c && ch <= 0x200d) ||
        ch == 0x203f || ch == 0x2040 || (ch >= 0x2070 && ch <= 0x218f) ||
        (ch >= 0x2c00 && ch <= 0x2fef) || (ch >= 0x3001 && ch <= 0xd7ff) ||
        (ch >= 0xf900 && ch <= 0xfdcf) || (ch >= 0xfdf0 && ch <= 0xfffd) ||
        (ch >= 0x10000 && ch <= 0xeffff);
  }

  private bool isNameStartChar(int ch) {
    return (ch>= 'a' && ch<= 'z') || (ch>= 'A' && ch<= 'Z') ||
        (ch >= 0xc0 && ch <= 0xd6) || (ch >= 0xd8 && ch <= 0xf6) ||
        (ch >= 0xf8 && ch <= 0x2ff) || (ch >= 0x370 && ch <= 0x37d) ||
        (ch >= 0x37f && ch <= 0x1fff) || (ch >= 0x200c && ch <= 0x200d) ||
        (ch >= 0x2070 && ch <= 0x218f) || (ch >= 0x2c00 && ch <= 0x2fef) ||
        (ch >= 0x3001 && ch <= 0xd7ff) || (ch >= 0xf900 && ch <= 0xfdcf) ||
        (ch >= 0xfdf0 && ch <= 0xfffd) || (ch >= 0x10000 && ch <= 0xeffff);
  }

  private bool isNameStartCharU(int ch) {
    return (ch>= 'a' && ch<= 'z') || (ch>= 'A' && ch<= 'Z') || ch=='_' ||
        (ch >= 0xc0 && ch <= 0xd6) || (ch >= 0xd8 && ch <= 0xf6) ||
        (ch >= 0xf8 && ch <= 0x2ff) || (ch >= 0x370 && ch <= 0x37d) ||
        (ch >= 0x37f && ch <= 0x1fff) || (ch >= 0x200c && ch <= 0x200d) ||
        (ch >= 0x2070 && ch <= 0x218f) || (ch >= 0x2c00 && ch <= 0x2fef) ||
        (ch >= 0x3001 && ch <= 0xd7ff) || (ch >= 0xf900 && ch <= 0xfdcf) ||
        (ch >= 0xfdf0 && ch <= 0xfffd) || (ch >= 0x10000 && ch <= 0xeffff);
  }
  public ISet<RDFTriple> parse() {
    ISet<RDFTriple> triples = new HashSet<RDFTriple>();
    while (true) {
      skipWhitespace();
      int mark = input.setHardMark();
      int ch = input.ReadChar();
      if (ch< 0) {
        RDFInternal.replaceBlankNodes(triples, bnodeLabels);
        return triples;
      }
      if (ch=='@') {
        ch = input.ReadChar();
        if (ch=='p' && input.ReadChar()=='r' && input.ReadChar()=='e' &&
            input.ReadChar()=='f' && input.ReadChar()=='i' &&
            input.ReadChar()=='x' && skipWhitespace()) {
          readPrefixStatement(false);
          continue;
  } else if (ch=='b' && input.ReadChar()=='a' && input.ReadChar()=='s' &&
            input.ReadChar()=='e' && skipWhitespace()) {
          readBase(false);
          continue;
        } else {
 throw new ParserException();
}
      } else if (ch=='b' || ch=='B') {
        var c2 = 0;
        if (((c2=input.ReadChar())=='A' || c2=='a') &&
            ((c2=input.ReadChar())=='S' || c2=='s') &&
            ((c2=input.ReadChar())=='E' || c2=='e') && skipWhitespace()) {
          readBase(true);
          continue;
        } else {
          input.setMarkPosition(mark);
        }
      } else if (ch=='p' || ch=='P') {
        var c2 = 0;
        if (((c2=input.ReadChar())=='R' || c2=='r') &&
            ((c2=input.ReadChar())=='E' || c2=='e') &&
            ((c2=input.ReadChar())=='F' || c2=='f') &&
            ((c2=input.ReadChar())=='I' || c2=='i') &&
            ((c2=input.ReadChar())=='X' || c2=='x') && skipWhitespace()) {
          readPrefixStatement(true);
          continue;
        } else {
          input.setMarkPosition(mark);
        }
      } else {
        input.setMarkPosition(mark);
      }
      readTriples(triples);
    }
  }

  private void readBase(bool sparql) {
    if (input.ReadChar()!='<') {
 throw new ParserException();
}
    baseURI = readIriReference();
    if (!sparql) {
      skipWhitespace();
      if (input.ReadChar()!='.') {
 throw new ParserException();
}
    } else {
      skipWhitespace();
    }
  }

  private string readBlankNodeLabel() {
    var ilist = new StringBuilder();
    int startChar = input.ReadChar();
    if (!isNameStartCharU(startChar) && (startChar<'0' || startChar>'9')) {
 throw new ParserException();
}
    if (startChar <= 0xffff) {
  { ilist.Append((char)(startChar));
}
  } else if (startChar <= 0x10ffff) {
ilist.Append((char)((((startChar-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((startChar-0x10000) & 0x3ff)+0xdc00));
}
    var lastIsPeriod = false;
    input.setSoftMark();
    while (true) {
      int ch = input.ReadChar();
      if (ch=='.') {
        int position = input.getMarkPosition();
        int ch2 = input.ReadChar();
        if (!isNameChar(ch2) && ch2!=':' && ch2!='.') {
          input.setMarkPosition(position-1);
          return ilist.ToString();
        } else {
          input.moveBack(1);
        }
        if (ch <= 0xffff) {
  { ilist.Append((char)(ch));
}
  } else if (ch <= 0x10ffff) {
ilist.Append((char)((((ch-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((ch-0x10000) & 0x3ff)+0xdc00));
}
        lastIsPeriod = true;
      } else if (isNameChar(ch)) {
        if (ch <= 0xffff) {
  { ilist.Append((char)(ch));
}
  } else if (ch <= 0x10ffff) {
ilist.Append((char)((((ch-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((ch-0x10000) & 0x3ff)+0xdc00));
}
        lastIsPeriod = false;
      } else {
        if (ch >= 0) {
          input.moveBack(1);
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
      skipWhitespace();
      int ch;
      if (havePredObject) {
        var haveSemicolon = false;
        while (true) {
          input.setSoftMark();
          ch = input.ReadChar();
          if (ch==';') {
            skipWhitespace();
            haveSemicolon = true;
          } else {
            if (ch >= 0) {
              input.moveBack(1);
            }
            break;
          }
        }
        if (!haveSemicolon) {
          break;
        }
      }
      RDFTerm pred = readPredicate();
      if (pred == null) {
        break;
      }
      havePredObject = true;
      readObjectListToProperties(pred, obj);
    }
    if (input.ReadChar()!=']') {
 throw new ParserException();
}
    return obj;
  }
  private TurtleObject readCollection() {
    TurtleObject obj = TurtleObject.newCollection();
    while (true) {
      skipWhitespace();
      input.setHardMark();
      int ch = input.ReadChar();
      if (ch==')') {
        break;
      } else {
        if (ch >= 0) {
          input.moveBack(1);
        }
        TurtleObject subobj = readObject(true);
        obj.getObjects().Add(subobj);
      }
    }
    return obj;
  }

  private string readIriReference() {
    var ilist = new StringBuilder();
    while (true) {
      int ch = input.ReadChar();
      if (ch< 0) {
 throw new ParserException();
}
      if (ch=='>') {
        string iriref = ilist.ToString();
        // Resolve the IRI reference relative
        // to the _base URI
        iriref = URIUtility.relativeResolve(iriref, baseURI);
        if (iriref == null) {
 throw new ParserException();
}
        return iriref;
  } else if (ch=='\\') {
        ch = readUnicodeEscape(false);
      }
      if (ch<= 0x20 || ((ch&0x7F)==ch && "><\\\"{}|^`".IndexOf((char)ch)>= 0))
        throw new ParserException();
      if (ch <= 0xffff) {
  { ilist.Append((char)(ch));
}
  } else if (ch <= 0x10ffff) {
ilist.Append((char)((((ch-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((ch-0x10000) & 0x3ff)+0xdc00));
}
    }
  }

  private string readLanguageTag() {
    var ilist = new StringBuilder();
    var hyphen = false;
    var haveHyphen = false;
    var haveString = false;
    input.setSoftMark();
    while (true) {
      int c2 = input.ReadChar();
      if (c2>= 'A' && c2<= 'Z') {
        if (c2 <= 0xffff) {
  { ilist.Append((char)(c2));
}
  } else if (c2 <= 0x10ffff) {
ilist.Append((char)((((c2-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((c2-0x10000) & 0x3ff)+0xdc00));
}
        haveString = true;
        hyphen = false;
      } else if (c2>= 'a' && c2<= 'z') {
        if (c2 <= 0xffff) {
  { ilist.Append((char)(c2));
}
  } else if (c2 <= 0x10ffff) {
ilist.Append((char)((((c2-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((c2-0x10000) & 0x3ff)+0xdc00));
}
        haveString = true;
        hyphen = false;
      } else if (haveHyphen && (c2>= '0' && c2<= '9')) {
        if (c2 <= 0xffff) {
  { ilist.Append((char)(c2));
}
  } else if (c2 <= 0x10ffff) {
ilist.Append((char)((((c2-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((c2-0x10000) & 0x3ff)+0xdc00));
}
        haveString = true;
        hyphen = false;
      } else if (c2=='-') {
        if (hyphen||!haveString) {
 throw new ParserException();
}
        if (c2 <= 0xffff) {
  { ilist.Append((char)(c2));
}
  } else if (c2 <= 0x10ffff) {
ilist.Append((char)((((c2-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((c2-0x10000) & 0x3ff)+0xdc00));
}
        hyphen = true;
        haveHyphen = true;
        haveString = true;
      } else {
        if (c2 >= 0) {
          input.moveBack(1);
        }
        if (hyphen||!haveString) {
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
  { ilist.Append((char)(ch));
}
  } else if (ch <= 0x10ffff) {
ilist.Append((char)((((ch-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((ch-0x10000) & 0x3ff)+0xdc00));
}
    bool haveDigits=(ch>= '0' && ch<= '9');
    bool haveDot=(ch=='.');
    input.setHardMark();
    while (true) {
      int ch1 = input.ReadChar();
      if (haveDigits && (ch1=='e' || ch1=='E')) {
        // Parse exponent
        if (ch1 <= 0xffff) {
  { ilist.Append((char)(ch1));
}
  } else if (ch1 <= 0x10ffff) {
ilist.Append((char)((((ch1-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((ch1-0x10000) & 0x3ff)+0xdc00));
}
        ch1 = input.ReadChar();
        haveDigits = false;
        if (ch1=='+' || ch1=='-' || (ch1>= '0' && ch1<= '9')) {
          if (ch1 <= 0xffff) {
  { ilist.Append((char)(ch1));
}
  } else if (ch1 <= 0x10ffff) {
ilist.Append((char)((((ch1-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((ch1-0x10000) & 0x3ff)+0xdc00));
}
          if (ch1>= '0' && ch1<= '9') {
            haveDigits = true;
          }
        } else {
 throw new ParserException();
}
        input.setHardMark();
        while (true) {
          ch1 = input.ReadChar();
          if (ch1>= '0' && ch1<= '9') {
            haveDigits = true;
            if (ch1 <= 0xffff) {
  { ilist.Append((char)(ch1));
}
  } else if (ch1 <= 0x10ffff) {
ilist.Append((char)((((ch1-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((ch1-0x10000) & 0x3ff)+0xdc00));
}
          } else {
            if (ch1 >= 0) {
              input.moveBack(1);
            }
            if (!haveDigits) {
 throw new ParserException();
}
            return RDFTerm.fromTypedString(ilist.ToString(),
                "http://www.w3.org/2001/XMLSchema#double");
          }
        }
      } else if (ch1>= '0' && ch1<= '9') {
        haveDigits = true;
        if (ch1 <= 0xffff) {
  { ilist.Append((char)(ch1));
}
  } else if (ch1 <= 0x10ffff) {
ilist.Append((char)((((ch1-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((ch1-0x10000) & 0x3ff)+0xdc00));
}
      } else if (!haveDot && ch1=='.') {
        haveDot = true;
        // check for non-digit and non-E
        int markpos = input.getMarkPosition();
        int ch2 = input.ReadChar();
        if (ch2!='e' && ch2!='E' && (ch2<'0' || ch2>'9')) {
          // move to just at the period and return
          input.setMarkPosition(markpos-1);
          if (!haveDigits) {
 throw new ParserException();
}
          return RDFTerm.fromTypedString(ilist.ToString(),
              haveDot ? "http://www.w3.org/2001/XMLSchema#decimal" :
              "http://www.w3.org/2001/XMLSchema#integer");
        } else {
          input.moveBack(1);
        }
        if (ch1 <= 0xffff) {
  { ilist.Append((char)(ch1));
}
  } else if (ch1 <= 0x10ffff) {
ilist.Append((char)((((ch1-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((ch1-0x10000) & 0x3ff)+0xdc00));
}
      } else {  // no more digits
        if (ch1 >= 0) {
          input.moveBack(1);
        }
        if (!haveDigits) {
 throw new ParserException();
}
        return RDFTerm.fromTypedString(ilist.ToString(),
            haveDot ? "http://www.w3.org/2001/XMLSchema#decimal" :
            "http://www.w3.org/2001/XMLSchema#integer");
      }
    }
  }

  private TurtleObject readObject(bool acceptLiteral) {
    int ch = input.ReadChar();
    int mark = input.setSoftMark();
    if (ch< 0) {
 throw new ParserException();
  } else if (ch=='<') return TurtleObject.fromTerm(
          RDFTerm.fromIRI(readIriReference()));
    else if (acceptLiteral && (ch=='-' || ch=='+' || ch=='.' || (ch>= '0' &&
      ch<= '9'))) {
 return TurtleObject.fromTerm(readNumberLiteral(ch));
} else if (acceptLiteral && (ch=='\'' || ch=='\"')) {
        // start of quote literal
      string str = readStringLiteral(ch);
      return TurtleObject.fromTerm(finishStringLiteral(str));
    } else if (ch=='_') { // Blank Node Label
      if (input.ReadChar()!=':') {
 throw new ParserException();
}
      string label = readBlankNodeLabel();
      RDFTerm term = bnodeLabels[label];
      if (term == null) {
        term = RDFTerm.fromBlankNode(label);
        bnodeLabels.Add(label, term);
      }
      return TurtleObject.fromTerm(term);
    } else if (ch=='[') {
 return readBlankNodePropertyList();
  } else if (ch=='(') return readCollection();
    else if (ch==':') { // prefixed name with current prefix
      string scope=namespaces[""];
      if (scope == null) {
 throw new ParserException();
}
      return TurtleObject.fromTerm(
          RDFTerm.fromIRI(scope + readOptionalLocalName()));
    } else if (isNameStartChar(ch)) {  // prefix
      if (acceptLiteral && (ch=='t' || ch=='f')) {
        mark = input.setHardMark();
        if (ch=='t' && input.ReadChar()=='r' && input.ReadChar()=='u' &&
            input.ReadChar()=='e' && skipWhitespace()) {
 return TurtleObject.fromTerm(RDFTerm.TRUE);
  } else if (ch=='f' && input.ReadChar()=='a' && input.ReadChar()=='l' &&
          input.ReadChar()=='s' && input.ReadChar()=='e' &&
              skipWhitespace()) {
 return TurtleObject.fromTerm(RDFTerm.FALSE);
} else {
          input.setMarkPosition(mark);
        }
      }
      string prefix = readPrefix(ch);
      string scope = namespaces[prefix];
      if (scope == null) {
 throw new ParserException();
}
      return TurtleObject.fromTerm(
          RDFTerm.fromIRI(scope + readOptionalLocalName()));
    } else {
      input.setMarkPosition(mark);
      return null;
    }
  }

  private void readObjectList(ISet<RDFTriple> triples) {
    var haveObject = false;
    while (true) {
      input.setSoftMark();
      int ch;
      if (haveObject) {
        ch = input.ReadChar();
        if (ch!=',') {
          if (ch >= 0) {
            input.moveBack(1);
          }
          break;
        }
        skipWhitespace();
      }
      // Read _object
      TurtleObject obj = readObject(true);
      if (obj == null) {
        if (!haveObject) {
 throw new ParserException();
} else {
 return;
}
      }
      haveObject = true;
      emitRDFTriple(curSubject, curPredicate, obj, triples);
      skipWhitespace();
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
      input.setSoftMark();
      int ch;
      if (haveObject) {
        ch = input.ReadChar();
        if (ch!=',') {
          if (ch >= 0) {
            input.moveBack(1);
          }
          break;
        }
        skipWhitespace();
      }
      // Read _object
      TurtleObject obj = readObject(true);
      if (obj == null) {
        if (!haveObject) {
 throw new ParserException();
} else {
 return;
}
      }
      var prop = new TurtleProperty();
      prop.pred = predicate;
      prop.obj = obj;
      propertyList.getProperties().Add(prop);
      skipWhitespace();
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
    input.setSoftMark();
    while (true) {
      int ch = input.ReadChar();
      if (ch< 0) {
 return ilist.ToString();
}
      if (ch=='%') {
        int a = input.ReadChar();
        int b = input.ReadChar();
        if (toHexValue(a)<0 ||
            toHexValue(b)< 0) {
 throw new ParserException();
}
        if (ch <= 0xffff) {
  { ilist.Append((char)(ch));
}
  } else if (ch <= 0x10ffff) {
ilist.Append((char)((((ch-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((ch-0x10000) & 0x3ff)+0xdc00));
}
        if (a <= 0xffff) {
  { ilist.Append((char)(a));
}
  } else if (a <= 0x10ffff) {
ilist.Append((char)((((a-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((a-0x10000) & 0x3ff)+0xdc00));
}
        if (b <= 0xffff) {
  { ilist.Append((char)(b));
}
  } else if (b <= 0x10ffff) {
ilist.Append((char)((((b-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((b-0x10000) & 0x3ff)+0xdc00));
}
        lastIsPeriod = false;
        first = false;
        continue;
      } else if (ch=='\\') {
        ch = input.ReadChar();
        if (((ch&0x7F)==ch && "_~.-!$&'()*+,;=/?#@%".IndexOf((char)ch)>= 0)) {
          if (ch <= 0xffff) {
  { ilist.Append((char)(ch));
}
  } else if (ch <= 0x10ffff) {
ilist.Append((char)((((ch-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((ch-0x10000) & 0x3ff)+0xdc00));
}
        } else {
 throw new ParserException();
}
        lastIsPeriod = false;
        first = false;
        continue;
      }
      if (first) {
        if (!isNameStartCharU(ch) && ch!=':' && (ch<'0' || ch>'9')) {
          input.moveBack(1);
          return ilist.ToString();
        }
      } else {
        if (!isNameChar(ch) && ch!=':' && ch!='.') {
          input.moveBack(1);
          if (lastIsPeriod) {
 throw new ParserException();
}
          return ilist.ToString();
        }
      }
      lastIsPeriod=(ch=='.');
      if (lastIsPeriod && !first) {
        // if a period was just read, check
        // if the next character is valid before
        // adding the period.
        int position = input.getMarkPosition();
        int ch2 = input.ReadChar();
        if (!isNameChar(ch2) && ch2!=':' && ch2!='.') {
          input.setMarkPosition(position-1);
          return ilist.ToString();
        } else {
          input.moveBack(1);
        }
      }
      first = false;
      if (ch <= 0xffff) {
  { ilist.Append((char)(ch));
}
  } else if (ch <= 0x10ffff) {
ilist.Append((char)((((ch-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((ch-0x10000) & 0x3ff)+0xdc00));
}
    }
  }

  private RDFTerm readPredicate() {
    int mark = input.setHardMark();
    int ch = input.ReadChar();
    RDFTerm predicate = null;
    if (ch=='a') {
      mark = input.setHardMark();
      if (skipWhitespace()) {
 return RDFTerm.A;
} else {
        input.setMarkPosition(mark);
        string prefix=readPrefix('a');
        string scope = namespaces[prefix];
        if (scope == null) {
 throw new ParserException();
}
        predicate = RDFTerm.fromIRI(scope + readOptionalLocalName());
        skipWhitespace();
        return predicate;
      }
    } else if (ch=='<') {
      predicate = RDFTerm.fromIRI(readIriReference());
      skipWhitespace();
      return predicate;
    } else if (ch==':') { // prefixed name with current prefix
      string scope=namespaces[""];
      if (scope == null) {
 throw new ParserException();
}
      predicate = RDFTerm.fromIRI(scope + readOptionalLocalName());
      skipWhitespace();
      return predicate;
    } else if (isNameStartChar(ch)) {  // prefix
      string prefix = readPrefix(ch);
      string scope = namespaces[prefix];
      if (scope == null) {
 throw new ParserException();
}
      predicate = RDFTerm.fromIRI(scope + readOptionalLocalName());
      skipWhitespace();
      return predicate;
    } else {
      input.setMarkPosition(mark);
      return null;
    }
  }

  private void readPredicateObjectList(ISet<RDFTriple> triples) {
    var havePredObject = false;
    while (true) {
      int ch;
      skipWhitespace();
      if (havePredObject) {
        var haveSemicolon = false;
        while (true) {
          input.setSoftMark();
          ch = input.ReadChar();
          //Console.WriteLine("nextchar %c",(char)ch);
          if (ch==';') {
            skipWhitespace();
            haveSemicolon = true;
          } else {
            if (ch >= 0) {
              input.moveBack(1);
            }
            break;
          }
        }
        if (!haveSemicolon) {
          break;
        }
      }
      curPredicate = readPredicate();
      //Console.WriteLine("predobjlist %s",curPredicate);
      if (curPredicate == null) {
        if (!havePredObject) {
 throw new ParserException();
} else {
          break;
        }
      }
      // Read _object
      havePredObject = true;
      readObjectList(triples);
    }
    if (!havePredObject) {
 throw new ParserException();
}
    return;
  }

  private string readPrefix(int startChar) {
    var ilist = new StringBuilder();
    var lastIsPeriod = false;
    var first = true;
    if (startChar >= 0) {
      if (startChar <= 0xffff) {
  { ilist.Append((char)(startChar));
}
  } else if (startChar <= 0x10ffff) {
ilist.Append((char)((((startChar-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((startChar-0x10000) & 0x3ff)+0xdc00));
}
      first = false;
    }
    while (true) {
      int ch = input.ReadChar();
      if (ch< 0) {
 throw new ParserException();
}
      if (ch==':') {
        if (lastIsPeriod) {
 throw new ParserException();
}
        return ilist.ToString();
  } else if (first && !isNameStartChar(ch)) {
 throw new ParserException();
  } else if (ch!='.' && !isNameChar(ch)) {
 throw new ParserException();
}
      first = false;
      if (ch <= 0xffff) {
  { ilist.Append((char)(ch));
}
  } else if (ch <= 0x10ffff) {
ilist.Append((char)((((ch-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((ch-0x10000) & 0x3ff)+0xdc00));
}
      lastIsPeriod=(ch=='.');
    }
  }

  private void readPrefixStatement(bool sparql) {
    string prefix = readPrefix(-1);
    skipWhitespace();
    if (input.ReadChar()!='<') {
 throw new ParserException();
}
    string iri = readIriReference();
    namespaces.Add(prefix, iri);
    if (!sparql) {
      skipWhitespace();
      if (input.ReadChar()!='.') {
 throw new ParserException();
}
    } else {
      skipWhitespace();
    }
  }
  private string readStringLiteral(int ch) {
    var ilist = new StringBuilder();
    var first = true;
    var longQuote = false;
    var quotecount = 0;
    while (true) {
      int c2 = input.ReadChar();
      if (first && c2 == ch) {
        input.setHardMark();
        c2 = input.ReadChar();
        if (c2 != ch) {
          if (c2 >= 0) {
            input.moveBack(1);
          }
          return "";
        }
        longQuote = true;
        c2 = input.ReadChar();
      }
      first = false;
      if (!longQuote && (c2 == 0x0a || c2 == 0x0d)) {
 throw new ParserException();
  } else if (c2=='\\') {
        c2 = readUnicodeEscape(true);
        if (quotecount >= 2) {
          if (ch <= 0xffff) {
  { ilist.Append((char)(ch));
}
  } else if (ch <= 0x10ffff) {
ilist.Append((char)((((ch-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((ch-0x10000) & 0x3ff)+0xdc00));
}
        }
        if (quotecount >= 1) {
          if (ch <= 0xffff) {
  { ilist.Append((char)(ch));
}
  } else if (ch <= 0x10ffff) {
ilist.Append((char)((((ch-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((ch-0x10000) & 0x3ff)+0xdc00));
}
        }
        if (c2 <= 0xffff) {
  { ilist.Append((char)(c2));
}
  } else if (c2 <= 0x10ffff) {
ilist.Append((char)((((c2-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((c2-0x10000) & 0x3ff)+0xdc00));
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
        if (c2< 0) {
 throw new ParserException();
}
        if (quotecount >= 2) {
          if (ch <= 0xffff) {
  { ilist.Append((char)(ch));
}
  } else if (ch <= 0x10ffff) {
ilist.Append((char)((((ch-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((ch-0x10000) & 0x3ff)+0xdc00));
}
        }
        if (quotecount >= 1) {
          if (ch <= 0xffff) {
  { ilist.Append((char)(ch));
}
  } else if (ch <= 0x10ffff) {
ilist.Append((char)((((ch-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((ch-0x10000) & 0x3ff)+0xdc00));
}
        }
        if (c2 <= 0xffff) {
  { ilist.Append((char)(c2));
}
  } else if (c2 <= 0x10ffff) {
ilist.Append((char)((((c2-0x10000) >> 10) & 0x3ff)+0xd800));
ilist.Append((char)(((c2-0x10000) & 0x3ff)+0xdc00));
}
        quotecount = 0;
      }
    }
  }
  private void readTriples(ISet<RDFTriple> triples) {
    int mark = input.setHardMark();
    int ch = input.ReadChar();
    if (ch< 0) {
 return;
}
    input.setMarkPosition(mark);
    TurtleObject subject = readObject(false);
    if (subject == null) {
 throw new ParserException();
}
    curSubject = subject;
    if (!(subject.kind == TurtleObject.PROPERTIES &&
        subject.getProperties().Count>0)) {
      skipWhitespace();
      readPredicateObjectList(triples);
    } else {
      skipWhitespace();
      input.setHardMark();
      ch = input.ReadChar();
      if (ch=='.') {
        // just a blank node property list;
        // generate a blank node as the subject
        RDFTerm blankNode = allocateBlankNode();
        foreach(var prop in subject.getProperties()) {
          emitRDFTriple(blankNode, prop.pred, prop.obj, triples);
        }
        return;
      } else if (ch< 0) {
 throw new ParserException();
}
      input.moveBack(1);
      readPredicateObjectList(triples);
    }
    skipWhitespace();
    if (input.ReadChar()!='.') {
 throw new ParserException();
}
  }

  private int readUnicodeEscape(bool extended) {
    int ch = input.ReadChar();
    if (ch=='U') {
      if (input.ReadChar()!='0') {
 throw new ParserException();
}
      if (input.ReadChar()!='0') {
 throw new ParserException();
}
      int a = toHexValue(input.ReadChar());
      int b = toHexValue(input.ReadChar());
      int c = toHexValue(input.ReadChar());
      int d = toHexValue(input.ReadChar());
      int e = toHexValue(input.ReadChar());
      int f = toHexValue(input.ReadChar());
      if (a<0||b<0||c<0||d<0||e<0||f< 0) {
 throw new ParserException();
}
      ch=(a << 20)|(b << 16)|(c << 12)|(d << 8)|(e << 4)|(f);
    } else if (ch=='u') {
      int a = toHexValue(input.ReadChar());
      int b = toHexValue(input.ReadChar());
      int c = toHexValue(input.ReadChar());
      int d = toHexValue(input.ReadChar());
      if (a<0||b<0||c<0||d< 0) {
 throw new ParserException();
}
      ch=(a << 12)|(b << 8)|(c << 4)|(d);
    } else if (extended && ch=='t') {
 return '\t';
  } else if (extended && ch=='b') {
 return '\b';
  } else if (extended && ch=='n') {
 return '\n';
  } else if (extended && ch=='r') {
 return '\r';
  } else if (extended && ch=='f') {
 return '\f';
  } else if (extended && ch=='\'') {
 return '\'';
  } else if (extended && ch=='\\') {
 return '\\';
  } else if (extended && ch=='"') {
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
    input.setSoftMark();
    while (true) {
      int ch = input.ReadChar();
      if (ch=='#') {
        while (true) {
          ch = input.ReadChar();
          if (ch< 0) {
 return true;
}
          if (ch == 0x0d || ch == 0x0a) {
            break;
          }
        }
      } else if (ch != 0x09 && ch != 0x0a && ch != 0x0d && ch != 0x20) {
        if (ch >= 0) {
          input.moveBack(1);
        }
        return haveWhitespace;
      }
      haveWhitespace = true;
    }
  }

  private int toHexValue(int a) {
    if (a>= '0' && a<= '9') {
 return a-'0';
}
    return (a>= 'a' && a<= 'f') ? (a+10-'a') : ((a>= 'A' && a<= 'F') ?
      (a+10-'A') : (-1));
  }
}
}
