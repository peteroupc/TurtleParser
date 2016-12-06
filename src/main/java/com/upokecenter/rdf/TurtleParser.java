package com.upokecenter.util;
/*
Written in 2013 by Peter Occil.
Any copyright is dedicated to the Public Domain.
http://creativecommons.org/publicdomain/zero/1.0/

If you like this, you should donate to Peter O.
at: http://peteroupc.github.io/
*/

  import java.util.*;

  import com.upokecenter.util.*;
  import com.upokecenter.text.*;

    /**
     * Not documented yet.
     */
  public class TurtleParser implements IRDFParser {
    private static final class TurtleObject {
      public static final int SIMPLE = 0;
      public static final int COLLECTION = 1;
      public static final int PROPERTIES = 2;

      public static TurtleObject fromTerm(RDFTerm term) {
        TurtleObject tobj = new TurtleObject();
        tobj.setTerm(term);
        tobj.setKind(TurtleObject.SIMPLE);
        return tobj;
      }

      public static TurtleObject newCollection() {
        TurtleObject tobj = new TurtleObject();
        tobj.objects = new ArrayList<TurtleObject>();
        tobj.setKind(TurtleObject.COLLECTION);
        return tobj;
      }

      public static TurtleObject newPropertyList() {
        TurtleObject tobj = new TurtleObject();
        tobj.properties = new ArrayList<TurtleProperty>();
        tobj.setKind(TurtleObject.PROPERTIES);
        return tobj;
      }

      private RDFTerm term;

      private int kind;
      private List<TurtleObject> objects;

      private List<TurtleProperty> properties;

      public final RDFTerm getTerm() {
          return this.term;
        }
public final void setTerm(RDFTerm value) {
          this.term = value;
        }

      public final int getKind() {
          return this.kind;
        }
public final void setKind(int value) {
          this.kind = value;
        }

      public List<TurtleObject> getObjects() {
        return this.objects;
      }

      public List<TurtleProperty> getProperties() {
        return this.properties;
      }
    }

    private static final class TurtleProperty {
      private RDFTerm _pred;
      private TurtleObject _obj;

      public final RDFTerm getPred() {
          return this._pred;
        }
public final void setPred(RDFTerm value) {
          this._pred = value;
        }

      public final TurtleObject getObj() {
          return this._obj;
        }
public final void setObj(TurtleObject value) {
          this._obj = value;
        }
    }

    private Map<String, RDFTerm> bnodeLabels;
    private Map<String, String> namespaces;

    private String baseURI;

    private TurtleObject curSubject;

    private RDFTerm curPredicate;

    private StackableCharacterInput input;
    private int curBlankNode = 0;

    /**
     *
     */
    public TurtleParser(com.upokecenter.util.IByteReader stream) {
 this(stream,"about:blank");
    }

    /**
     *
     */
    public TurtleParser(com.upokecenter.util.IByteReader stream, String baseURI) {
      if (stream == null) {
        throw new NullPointerException("stream");
      }
      if (baseURI == null) {
        throw new NullPointerException("baseURI");
      }
      if (!URIUtility.hasScheme(baseURI)) {
        throw new IllegalArgumentException("baseURI");
      }
      this.input = new StackableCharacterInput(
          Encodings.GetDecoderInput(Encodings.UTF8, stream));
      this.baseURI = baseURI;
      this.bnodeLabels = new HashMap<String, RDFTerm>();
      this.namespaces = new HashMap<String, String>();
    }

    /**
     * Initializes a new instance of the {@link com.upokecenter.Rdf.getTurtleParser()}
     * class.
     * @param str A text string.
     */
    public TurtleParser(String str) {
 this(str,"about:blank");
    }

    /**
     * Initializes a new instance of the {@link com.upokecenter.Rdf.getTurtleParser()}
     * class.
     * @param str A text string.
     * @param baseURI Another string object.
     * @throws java.lang.NullPointerException The parameter {@code str} or {@code
     * baseURI} is null.
     */
    public TurtleParser(String str, String baseURI) {
      if (str == null) {
        throw new NullPointerException("str");
      }
      if (baseURI == null) {
        throw new NullPointerException("baseURI");
      }
      if (!URIUtility.hasScheme(baseURI)) {
        throw new IllegalArgumentException("baseURI");
      }
      this.input = new StackableCharacterInput(
          Encodings.StringToInput(str));
      this.baseURI = baseURI;
      this.bnodeLabels = new HashMap<String, RDFTerm>();
      this.namespaces = new HashMap<String, String>();
    }

    private RDFTerm AllocateBlankNode() {
      ++this.curBlankNode;
      // A period is included so as not to conflict
      // with user-defined blank node labels (this is allowed
      // because the syntax for blank node identifiers is
      // not concretely defined)
      String label = "." +
          (this.curBlankNode).toString();
      RDFTerm node = RDFTerm.fromBlankNode(label);
      this.bnodeLabels.put(label, node);
      return node;
    }

    private void emitRDFTriple(
  RDFTerm subj,
  RDFTerm pred,
  RDFTerm obj,
  Set<RDFTriple> triples) {
      RDFTriple triple = new RDFTriple(subj, pred, obj);
      triples.Add(triple);
    }

    private void emitRDFTriple(
  RDFTerm subj,
  RDFTerm pred,
  TurtleObject obj,
  Set<RDFTriple> triples) {
      if (obj.getKind() == TurtleObject.SIMPLE) {
        this.emitRDFTriple(subj, pred, obj.getTerm(), triples);
      } else if (obj.getKind() == TurtleObject.PROPERTIES) {
        List<TurtleProperty> props = obj.getProperties();
        if (props.size() == 0) {
          this.emitRDFTriple(subj, pred, this.AllocateBlankNode(), triples);
        } else {
          RDFTerm blank = this.AllocateBlankNode();
          this.emitRDFTriple(subj, pred, blank, triples);
          for (int i = 0; i < props.size(); ++i) {
            this.emitRDFTriple(blank, props.get(i).getPred(), props.get(i).getObj(), triples);
          }
        }
      } else if (obj.getKind() == TurtleObject.COLLECTION) {
        List<TurtleObject> objs = obj.getObjects();
        if (objs.size() == 0) {
          this.emitRDFTriple(subj, pred, RDFTerm.NIL, triples);
        } else {
          RDFTerm curBlank = this.AllocateBlankNode();
          RDFTerm firstBlank = curBlank;
          this.emitRDFTriple(curBlank, RDFTerm.FIRST, objs.get(0), triples);
          for (int i = 1; i <= objs.size(); ++i) {
            if (i == objs.size()) {
              this.emitRDFTriple(curBlank, RDFTerm.REST, RDFTerm.NIL, triples);
            } else {
              RDFTerm nextBlank = this.AllocateBlankNode();
              this.emitRDFTriple(curBlank, RDFTerm.REST, nextBlank, triples);
              this.emitRDFTriple(nextBlank, RDFTerm.FIRST, objs.get(i), triples);
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
  Set<RDFTriple> triples) {
      if (subj.getKind() == TurtleObject.SIMPLE) {
        this.emitRDFTriple(subj.getTerm(), pred, obj, triples);
      } else if (subj.getKind() == TurtleObject.PROPERTIES) {
        List<TurtleProperty> props = subj.getProperties();
        if (props.size() == 0) {
          this.emitRDFTriple(this.AllocateBlankNode(), pred, obj, triples);
        } else {
          RDFTerm blank = this.AllocateBlankNode();
          this.emitRDFTriple(blank, pred, obj, triples);
          for (int i = 0; i < props.size(); ++i) {
            this.emitRDFTriple(blank, props.get(i).getPred(), props.get(i).getObj(), triples);
          }
        }
      } else if (subj.getKind() == TurtleObject.COLLECTION) {
        List<TurtleObject> objs = subj.getObjects();
        if (objs.size() == 0) {
          this.emitRDFTriple(RDFTerm.NIL, pred, obj, triples);
        } else {
          RDFTerm curBlank = this.AllocateBlankNode();
          RDFTerm firstBlank = curBlank;
          this.emitRDFTriple(curBlank, RDFTerm.FIRST, objs.get(0), triples);
          for (int i = 1; i <= objs.size(); ++i) {
            if (i == objs.size()) {
              this.emitRDFTriple(curBlank, RDFTerm.REST, RDFTerm.NIL, triples);
            } else {
              RDFTerm nextBlank = this.AllocateBlankNode();
              this.emitRDFTriple(curBlank, RDFTerm.REST, nextBlank, triples);
              this.emitRDFTriple(nextBlank, RDFTerm.FIRST, objs.get(i), triples);
              curBlank = nextBlank;
            }
          }
          this.emitRDFTriple(firstBlank, pred, obj, triples);
        }
      }
    }

    private RDFTerm finishStringLiteral(String str) {
      int mark = this.input.setHardMark();
      int ch = this.input.ReadChar();
      if (ch == '@') {
        return RDFTerm.fromLangString(str, this.readLanguageTag());
      } else if (ch == '^' && this.input.ReadChar() == '^') {
        ch = this.input.ReadChar();
        if (ch == '<') {
          return RDFTerm.fromTypedString(str, this.readIriReference());
        } else if (ch == ':') { // prefixed name with current prefix
          String scope = this.namespaces.get("");
          if (scope == null) {
            throw new ParserException();
          }
          return RDFTerm.fromTypedString(
     str,
     scope + this.readOptionalLocalName());
        } else if (this.isNameStartChar(ch)) {  // prefix
          String prefix = this.readPrefix(ch);
          String scope = this.namespaces.get(prefix);
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

    private boolean isNameChar(int ch) {
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

    private boolean isNameStartChar(int ch) {
      return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') ||
          (ch >= 0xc0 && ch <= 0xd6) || (ch >= 0xd8 && ch <= 0xf6) ||
          (ch >= 0xf8 && ch <= 0x2ff) || (ch >= 0x370 && ch <= 0x37d) ||
          (ch >= 0x37f && ch <= 0x1fff) || (ch >= 0x200c && ch <= 0x200d) ||
          (ch >= 0x2070 && ch <= 0x218f) || (ch >= 0x2c00 && ch <= 0x2fef) ||
          (ch >= 0x3001 && ch <= 0xd7ff) || (ch >= 0xf900 && ch <= 0xfdcf) ||
          (ch >= 0xfdf0 && ch <= 0xfffd) || (ch >= 0x10000 && ch <= 0xeffff);
    }

    private boolean isNameStartCharU(int ch) {
      return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch ==
          '_' || (ch >= 0xc0 && ch <= 0xd6) || (ch >= 0xd8 && ch <= 0xf6) ||
            (ch >= 0xf8 && ch <= 0x2ff) || (ch >= 0x370 && ch <= 0x37d) ||
            (ch >= 0x37f && ch <= 0x1fff) || (ch >= 0x200c && ch <= 0x200d) ||
            (ch >= 0x2070 && ch <= 0x218f) || (ch >= 0x2c00 && ch <= 0x2fef) ||
            (ch >= 0x3001 && ch <= 0xd7ff) || (ch >= 0xf900 && ch <= 0xfdcf) ||
            (ch >= 0xfdf0 && ch <= 0xfffd) || (ch >= 0x10000 && ch <= 0xeffff);
    }

    /**
     * Not documented yet.
     * @return An ISet(RDFTriple) object.
     */
    public Set<RDFTriple> Parse() {
      Set<RDFTriple> triples = new HashSet<RDFTriple>();
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
          int c2 = 0;
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
          int c2 = 0;
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

    private void readBase(boolean sparql) {
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

    private String readBlankNodeLabel() {
      StringBuilder ilist = new StringBuilder();
      int startChar = this.input.ReadChar();
      if (!this.isNameStartCharU(startChar) &&
           (startChar < '0' || startChar > '9')) {
        throw new ParserException();
      }
      if (startChar <= 0xffff) {
        {
          ilist.append((char)startChar);
        }
      } else if (startChar <= 0x10ffff) {
        ilist.append((char)((((startChar - 0x10000) >> 10) & 0x3ff) + 0xd800));
        ilist.append((char)(((startChar - 0x10000) & 0x3ff) + 0xdc00));
      }
      boolean lastIsPeriod = false;
      this.input.setSoftMark();
      while (true) {
        int ch = this.input.ReadChar();
        if (ch == '.') {
          int position = this.input.getMarkPosition();
          int ch2 = this.input.ReadChar();
          if (!this.isNameChar(ch2) && ch2 != ':' && ch2 != '.') {
            this.input.setMarkPosition(position - 1);
            return ilist.toString();
          } else {
            this.input.moveBack(1);
          }
          if (ch <= 0xffff) {
            {
              ilist.append((char)ch);
            }
          } else if (ch <= 0x10ffff) {
            ilist.append((char)((((ch - 0x10000) >> 10) & 0x3ff) + 0xd800));
            ilist.append((char)(((ch - 0x10000) & 0x3ff) + 0xdc00));
          }
          lastIsPeriod = true;
        } else if (this.isNameChar(ch)) {
          if (ch <= 0xffff) {
            {
              ilist.append((char)ch);
            }
          } else if (ch <= 0x10ffff) {
            ilist.append((char)((((ch - 0x10000) >> 10) & 0x3ff) + 0xd800));
            ilist.append((char)(((ch - 0x10000) & 0x3ff) + 0xdc00));
          }
          lastIsPeriod = false;
        } else {
          if (ch >= 0) {
            this.input.moveBack(1);
          }
          if (lastIsPeriod) {
            throw new ParserException();
          }
          return ilist.toString();
        }
      }
    }

    private TurtleObject readBlankNodePropertyList() {
      TurtleObject obj = TurtleObject.newPropertyList();
      boolean havePredObject = false;
      while (true) {
        this.skipWhitespace();
        int ch;
        if (havePredObject) {
          boolean haveSemicolon = false;
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

    private String readIriReference() {
      StringBuilder ilist = new StringBuilder();
      while (true) {
        int ch = this.input.ReadChar();
        if (ch < 0) {
          throw new ParserException();
        }
        if (ch == '>') {
          String iriref = ilist.toString();
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
                "><\\\"{}|^`".indexOf((char)ch) >= 0)) {
          throw new ParserException();
        }
        if (ch <= 0xffff) {
          {
            ilist.append((char)ch);
          }
        } else if (ch <= 0x10ffff) {
          ilist.append((char)((((ch - 0x10000) >> 10) & 0x3ff) + 0xd800));
          ilist.append((char)(((ch - 0x10000) & 0x3ff) + 0xdc00));
        }
      }
    }

    private String readLanguageTag() {
      StringBuilder ilist = new StringBuilder();
      boolean hyphen = false;
      boolean haveHyphen = false;
      boolean haveString = false;
      this.input.setSoftMark();
      while (true) {
        int c2 = this.input.ReadChar();
        if (c2 >= 'A' && c2 <= 'Z') {
          if (c2 <= 0xffff) {
            {
              ilist.append((char)c2);
            }
          } else if (c2 <= 0x10ffff) {
            ilist.append((char)((((c2 - 0x10000) >> 10) & 0x3ff) + 0xd800));
            ilist.append((char)(((c2 - 0x10000) & 0x3ff) + 0xdc00));
          }
          haveString = true;
          hyphen = false;
        } else if (c2 >= 'a' && c2 <= 'z') {
              ilist.append((char)c2);
          haveString = true;
          hyphen = false;
        } else if (haveHyphen && (c2 >= '0' && c2 <= '9')) {
              ilist.append((char)c2);
          haveString = true;
          hyphen = false;
        } else if (c2 == '-') {
          if (hyphen || !haveString) {
            throw new ParserException();
          }
          if (c2 <= 0xffff) {
            {
              ilist.append((char)c2);
            }
          } else if (c2 <= 0x10ffff) {
            ilist.append((char)((((c2 - 0x10000) >> 10) & 0x3ff) + 0xd800));
            ilist.append((char)(((c2 - 0x10000) & 0x3ff) + 0xdc00));
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
          return ilist.toString();
        }
      }
    }

    // Reads a number literal starting with
    // the given character (assumes it's plus, minus,
    // a dot, or a digit)
    private RDFTerm readNumberLiteral(int ch) {
      // buffer to hold the literal
      StringBuilder ilist = new StringBuilder();
      // include the first character
      if (ch <= 0xffff) {
        {
          ilist.append((char)ch);
        }
      } else if (ch <= 0x10ffff) {
        ilist.append((char)((((ch - 0x10000) >> 10) & 0x3ff) + 0xd800));
        ilist.append((char)(((ch - 0x10000) & 0x3ff) + 0xdc00));
      }
      boolean haveDigits = ch >= '0' && ch <= '9';
      boolean haveDot = ch == '.';
      this.input.setHardMark();
      while (true) {
        int ch1 = this.input.ReadChar();
        if (haveDigits && (ch1 == 'e' || ch1 == 'E')) {
          // Parse exponent
          if (ch1 <= 0xffff) {
            {
              ilist.append((char)ch1);
            }
          } else if (ch1 <= 0x10ffff) {
            ilist.append((char)((((ch1 - 0x10000) >> 10) & 0x3ff) + 0xd800));
            ilist.append((char)(((ch1 - 0x10000) & 0x3ff) + 0xdc00));
          }
          ch1 = this.input.ReadChar();
          haveDigits = false;
          if (ch1 == '+' || ch1 == '-' || (ch1 >= '0' && ch1 <= '9')) {
            if (ch1 <= 0xffff) {
              {
                ilist.append((char)ch1);
              }
            } else if (ch1 <= 0x10ffff) {
              ilist.append((char)((((ch1 - 0x10000) >> 10) & 0x3ff) + 0xd800));
              ilist.append((char)(((ch1 - 0x10000) & 0x3ff) + 0xdc00));
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
                  ilist.append((char)ch1);
                }
              } else if (ch1 <= 0x10ffff) {
                ilist.append((char)((((ch1 - 0x10000) >> 10) & 0x3ff) +
                    0xd800));
                ilist.append((char)(((ch1 - 0x10000) & 0x3ff) + 0xdc00));
              }
            } else {
              if (ch1 >= 0) {
                this.input.moveBack(1);
              }
              if (!haveDigits) {
                throw new ParserException();
              }
              return RDFTerm.fromTypedString(
  ilist.toString(),
  "http://www.w3.org/2001/XMLSchema#double");
            }
          }
        } else if (ch1 >= '0' && ch1 <= '9') {
          haveDigits = true;
          if (ch1 <= 0xffff) {
            {
              ilist.append((char)ch1);
            }
          } else if (ch1 <= 0x10ffff) {
            ilist.append((char)((((ch1 - 0x10000) >> 10) & 0x3ff) + 0xd800));
            ilist.append((char)(((ch1 - 0x10000) & 0x3ff) + 0xdc00));
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
            String ns = haveDot ? "http://www.w3.org/2001/XMLSchema#decimal" :
                "http://www.w3.org/2001/XMLSchema#integer";
            return RDFTerm.fromTypedString(
  ilist.toString(),
  ns);
          } else {
            this.input.moveBack(1);
          }
          if (ch1 <= 0xffff) {
            {
              ilist.append((char)ch1);
            }
          } else if (ch1 <= 0x10ffff) {
            ilist.append((char)((((ch1 - 0x10000) >> 10) & 0x3ff) + 0xd800));
            ilist.append((char)(((ch1 - 0x10000) & 0x3ff) + 0xdc00));
          }
        } else {  // no more digits
          if (ch1 >= 0) {
            this.input.moveBack(1);
          }
          if (!haveDigits) {
            throw new ParserException();
          }
          String ns = haveDot ? "http://www.w3.org/2001/XMLSchema#decimal" :
              "http://www.w3.org/2001/XMLSchema#integer";
          return RDFTerm.fromTypedString(
  ilist.toString(),
  ns);
        }
      }
    }

    private TurtleObject readObject(boolean acceptLiteral) {
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
        String str = this.readStringLiteral(ch);
        return TurtleObject.fromTerm(this.finishStringLiteral(str));
      } else if (ch == '_') { // Blank Node Label
        if (this.input.ReadChar() != ':') {
          throw new ParserException();
        }
        String label = this.readBlankNodeLabel();
        RDFTerm term = this.bnodeLabels.containsKey(label) ?
                    this.bnodeLabels.get(label) : null;
        if (term == null) {
          term = RDFTerm.fromBlankNode(label);
          this.bnodeLabels.put(label, term);
        }
        return TurtleObject.fromTerm(term);
      } else if (ch == '[') {
        return this.readBlankNodePropertyList();
      } else if (ch == '(') {
        return this.readCollection();
      } else if (ch == ':') { // prefixed name with current prefix
        String scope = this.namespaces.get("");
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
        String prefix = this.readPrefix(ch);
        String scope = this.namespaces.containsKey(prefix) ?
                    this.namespaces.get(prefix) : null;
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

    private void readObjectList(Set<RDFTriple> triples) {
      boolean haveObject = false;
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
        // Read Object
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
      boolean haveObject = false;
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
        // Read Object
        TurtleObject obj = this.readObject(true);
        if (obj == null) {
          if (!haveObject) {
            throw new ParserException();
          } else {
            return;
          }
        }
        TurtleProperty prop = new TurtleProperty();
        prop.setPred(predicate);
        prop.setObj(obj);
        propertyList.getProperties().Add(prop);
        this.skipWhitespace();
        haveObject = true;
      }
      if (!haveObject) {
        throw new ParserException();
      }
      return;
    }

    private String readOptionalLocalName() {
      StringBuilder ilist = new StringBuilder();
      boolean lastIsPeriod = false;
      boolean first = true;
      this.input.setSoftMark();
      while (true) {
        int ch = this.input.ReadChar();
        if (ch < 0) {
          return ilist.toString();
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
              ilist.append((char)ch);
            }
          } else if (ch <= 0x10ffff) {
            ilist.append((char)((((ch - 0x10000) >> 10) & 0x3ff) + 0xd800));
            ilist.append((char)(((ch - 0x10000) & 0x3ff) + 0xdc00));
          }
          if (a <= 0xffff) {
            {
              ilist.append((char)a);
            }
          } else if (a <= 0x10ffff) {
            ilist.append((char)((((a - 0x10000) >> 10) & 0x3ff) + 0xd800));
            ilist.append((char)(((a - 0x10000) & 0x3ff) + 0xdc00));
          }
          if (b <= 0xffff) {
            {
              ilist.append((char)b);
            }
          } else if (b <= 0x10ffff) {
            ilist.append((char)((((b - 0x10000) >> 10) & 0x3ff) + 0xd800));
            ilist.append((char)(((b - 0x10000) & 0x3ff) + 0xdc00));
          }
          lastIsPeriod = false;
          first = false;
          continue;
        } else if (ch == '\\') {
          ch = this.input.ReadChar();
          if ((ch & 0x7f) == ch &&
                  "_~.-!$&'()*+,;=/?#@%".indexOf((char)ch) >= 0) {
            if (ch <= 0xffff) {
              {
                ilist.append((char)ch);
              }
            } else if (ch <= 0x10ffff) {
              ilist.append((char)((((ch - 0x10000) >> 10) & 0x3ff) + 0xd800));
              ilist.append((char)(((ch - 0x10000) & 0x3ff) + 0xdc00));
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
            return ilist.toString();
          }
        } else {
          if (!this.isNameChar(ch) && ch != ':' && ch != '.') {
            this.input.moveBack(1);
            if (lastIsPeriod) {
              throw new ParserException();
            }
            return ilist.toString();
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
            return ilist.toString();
          } else {
            this.input.moveBack(1);
          }
        }
        first = false;
        if (ch <= 0xffff) {
          {
            ilist.append((char)ch);
          }
        } else if (ch <= 0x10ffff) {
          ilist.append((char)((((ch - 0x10000) >> 10) & 0x3ff) + 0xd800));
          ilist.append((char)(((ch - 0x10000) & 0x3ff) + 0xdc00));
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
          String prefix = this.readPrefix('a');
          String scope = this.namespaces.get(prefix);
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
      } else if (ch == ':') { // prefixed name with current prefix
        String scope = this.namespaces.get("");
        if (scope == null) {
          throw new ParserException();
        }
        predicate = RDFTerm.fromIRI(scope + this.readOptionalLocalName());
        this.skipWhitespace();
        return predicate;
      } else if (this.isNameStartChar(ch)) {  // prefix
        String prefix = this.readPrefix(ch);
        String scope = this.namespaces.get(prefix);
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

    private void readPredicateObjectList(Set<RDFTriple> triples) {
      boolean havePredObject = false;
      while (true) {
        int ch;
        this.skipWhitespace();
        if (havePredObject) {
          boolean haveSemicolon = false;
          while (true) {
            this.input.setSoftMark();
            ch = this.input.ReadChar();
            // System.out.println("nextchar %c",(char)ch);
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
        // System.out.println("predobjlist %s",curPredicate);
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

    private boolean isBooleanLiteralEnd() {
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

    private String readPrefix(int startChar) {
      StringBuilder ilist = new StringBuilder();
      boolean lastIsPeriod = false;
      boolean first = true;
      if (startChar >= 0) {
        if (startChar <= 0xffff) {
          {
            ilist.append((char)startChar);
          }
        } else if (startChar <= 0x10ffff) {
          ilist.append((char)((((startChar - 0x10000) >> 10) & 0x3ff) +
              0xd800));
          ilist.append((char)(((startChar - 0x10000) & 0x3ff) + 0xdc00));
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
          return ilist.toString();
        } else if (first && !this.isNameStartChar(ch)) {
          throw new ParserException();
        } else if (ch != '.' && !this.isNameChar(ch)) {
          throw new ParserException();
        }
        first = false;
        if (ch <= 0xffff) {
          {
            ilist.append((char)ch);
          }
        } else if (ch <= 0x10ffff) {
          ilist.append((char)((((ch - 0x10000) >> 10) & 0x3ff) + 0xd800));
          ilist.append((char)(((ch - 0x10000) & 0x3ff) + 0xdc00));
        }
        lastIsPeriod = ch == '.';
      }
    }

    private void readPrefixStatement(boolean sparql) {
      String prefix = this.readPrefix(-1);
      this.skipWhitespace();
      if (this.input.ReadChar() != '<') {
        throw new ParserException();
      }
      String iri = this.readIriReference();
      this.namespaces.put(prefix, iri);
      if (!sparql) {
        this.skipWhitespace();
        if (this.input.ReadChar() != '.') {
          throw new ParserException();
        }
      } else {
        this.skipWhitespace();
      }
    }

    private String readStringLiteral(int ch) {
      StringBuilder ilist = new StringBuilder();
      boolean first = true;
      boolean longQuote = false;
      int quotecount = 0;
      while (true) {
        int c2 = this.input.ReadChar();
        if (first && c2 == ch) {
          this.input.setHardMark();
          c2 = this.input.ReadChar();
          if (c2 != ch) {
            if (c2 >= 0) {
              this.input.moveBack(1);
            }
            return "";
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
                ilist.append((char)ch);
              }
            } else if (ch <= 0x10ffff) {
              ilist.append((char)((((ch - 0x10000) >> 10) & 0x3ff) + 0xd800));
              ilist.append((char)(((ch - 0x10000) & 0x3ff) + 0xdc00));
            }
          }
          if (quotecount >= 1) {
            if (ch <= 0xffff) {
              {
                ilist.append((char)ch);
              }
            } else if (ch <= 0x10ffff) {
              ilist.append((char)((((ch - 0x10000) >> 10) & 0x3ff) + 0xd800));
              ilist.append((char)(((ch - 0x10000) & 0x3ff) + 0xdc00));
            }
          }
          if (c2 <= 0xffff) {
            {
              ilist.append((char)c2);
            }
          } else if (c2 <= 0x10ffff) {
            ilist.append((char)((((c2 - 0x10000) >> 10) & 0x3ff) + 0xd800));
            ilist.append((char)(((c2 - 0x10000) & 0x3ff) + 0xdc00));
          }
          quotecount = 0;
        } else if (c2 == ch) {
          if (!longQuote) {
            return ilist.toString();
          }
          ++quotecount;
          if (quotecount >= 3) {
            return ilist.toString();
          }
        } else {
          if (c2 < 0) {
            throw new ParserException();
          }
          if (quotecount >= 2) {
            if (ch <= 0xffff) {
              {
                ilist.append((char)ch);
              }
            } else if (ch <= 0x10ffff) {
              ilist.append((char)((((ch - 0x10000) >> 10) & 0x3ff) + 0xd800));
              ilist.append((char)(((ch - 0x10000) & 0x3ff) + 0xdc00));
            }
          }
          if (quotecount >= 1) {
            if (ch <= 0xffff) {
              {
                ilist.append((char)ch);
              }
            } else if (ch <= 0x10ffff) {
              ilist.append((char)((((ch - 0x10000) >> 10) & 0x3ff) + 0xd800));
              ilist.append((char)(((ch - 0x10000) & 0x3ff) + 0xdc00));
            }
          }
          if (c2 <= 0xffff) {
            {
              ilist.append((char)c2);
            }
          } else if (c2 <= 0x10ffff) {
            ilist.append((char)((((c2 - 0x10000) >> 10) & 0x3ff) + 0xd800));
            ilist.append((char)(((c2 - 0x10000) & 0x3ff) + 0xdc00));
          }
          quotecount = 0;
        }
      }
    }

    private void readTriples(Set<RDFTriple> triples) {
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
      if (!(subject.getKind() == TurtleObject.PROPERTIES &&
          subject.getProperties().size() > 0)) {
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
          for (Object prop : subject.getProperties()) {
            this.emitRDFTriple(blankNode, prop.getPred(), prop.getObj(), triples);
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

    private int readUnicodeEscape(boolean extended) {
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

    private boolean skipWhitespace() {
      boolean haveWhitespace = false;
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
