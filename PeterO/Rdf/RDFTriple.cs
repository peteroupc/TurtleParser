/*
Written in 2013 by Peter Occil.
Any copyright is dedicated to the Public Domain.
http://creativecommons.org/publicdomain/zero/1.0/

If you like this, you should donate to Peter O.
at: http://upokecenter.dreamhosters.com/articles/donate-now-2/
*/
namespace PeterO.Rdf {
  using System;

    /// <include file='../../docs.xml'
  /// path='docs/doc[@name="T:PeterO.Rdf.RDFTriple"]/*'/>
  public sealed class RDFTriple {
    private RDFTerm subject, predicate, _object;

    /// <include file='../../docs.xml'
  /// path='docs/doc[@name="M:PeterO.Rdf.RDFTriple.#ctor(PeterO.Rdf.RDFTerm,PeterO.Rdf.RDFTerm,PeterO.Rdf.RDFTerm)"]/*'/>
    public RDFTriple(RDFTerm subject, RDFTerm predicate, RDFTerm _object) {
      this.setSubject(subject);
      this.setPredicate(predicate);
      this.setObject(_object);
    }

    /// <include file='../../docs.xml'
  /// path='docs/doc[@name="M:PeterO.Rdf.RDFTriple.#ctor(PeterO.Rdf.RDFTriple)"]/*'/>
    public RDFTriple(RDFTriple triple) {
      if (triple == null) {
        throw new ArgumentNullException("triple");
      }
      this.setSubject(triple.subject);
      this.setPredicate(triple.predicate);
      this.setObject(triple._object);
    }

    /// <include file='../../docs.xml'
  /// path='docs/doc[@name="M:PeterO.Rdf.RDFTriple.Equals(System.Object)"]/*'/>
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
      var other = (RDFTriple)obj;
      if (this._object == null) {
        if (other._object != null) {
          return false;
        }
      } else if (!this._object.Equals(other._object)) {
        return false;
      }
      if (this.predicate == null) {
        if (other.predicate != null) {
          return false;
        }
      } else if (!this.predicate.Equals(other.predicate)) {
        return false;
      }
      if (this.subject == null) {
        return other.subject != null;
      } else {
        return !this.subject.Equals(other.subject);
      }
    }

    /// <include file='../../docs.xml'
  /// path='docs/doc[@name="M:PeterO.Rdf.RDFTriple.getObject"]/*'/>
    public RDFTerm getObject() {
      return this._object;
    }

    /// <include file='../../docs.xml'
  /// path='docs/doc[@name="M:PeterO.Rdf.RDFTriple.getPredicate"]/*'/>
    public RDFTerm getPredicate() {
      return this.predicate;
    }

    /// <include file='../../docs.xml'
  /// path='docs/doc[@name="M:PeterO.Rdf.RDFTriple.getSubject"]/*'/>
    public RDFTerm getSubject() {
      return this.subject;
    }

    /// <include file='../../docs.xml'
  /// path='docs/doc[@name="M:PeterO.Rdf.RDFTriple.GetHashCode"]/*'/>
    public override sealed int GetHashCode() {
      unchecked {
        var prime = 31;
        int result = prime + ((this._object == null) ? 0 :
             this._object.GetHashCode());
        result = (prime * result) +
            ((this.predicate == null) ? 0 : this.predicate.GetHashCode());
        result = prime * result + ((this.subject == null) ? 0 :
          this.subject.GetHashCode());
        return result;
      }
    }

    private void setObject(RDFTerm _object) {
      if (_object == null) {
        throw new ArgumentNullException("object");
      }
      this._object = _object;
    }

    private void setPredicate(RDFTerm predicate) {
      if (predicate == null) {
        throw new ArgumentNullException("predicate");
      }
      if (!(predicate.getKind() == RDFTerm.IRI)) {
    throw new ArgumentException("doesn't satisfy predicate.kind==RDFTerm.IRI");
      }
      this.predicate = predicate;
    }

    private void setSubject(RDFTerm subject) {
      if (subject == null) {
        throw new ArgumentNullException("subject");
      }
      if (!(subject.getKind() == RDFTerm.IRI ||
          subject.getKind() == RDFTerm.BLANK)) {
        throw new
         ArgumentException(
  "doesn't satisfy subject.kind==RDFTerm.IRI || subject.kind==RDFTerm.BLANK");
      }
      this.subject = subject;
    }

    /// <include file='../../docs.xml'
  /// path='docs/doc[@name="M:PeterO.Rdf.RDFTriple.ToString"]/*'/>
    public override sealed string ToString() {
      return this.subject.ToString() + " " + this.predicate.ToString() + " " +
            this._object.ToString() + " .";
    }
  }
}
