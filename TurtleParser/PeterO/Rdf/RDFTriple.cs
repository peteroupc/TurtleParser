using System;

    /*
Written in 2013 by Peter Occil.
Any copyright is dedicated to the Public Domain.
http://creativecommons.org/publicdomain/zero/1.0/

If you like this, you should donate to Peter O.
at: http://peteroupc.github.io/
*/
namespace PeterO.Rdf {
/// <summary>Not documented yet.</summary>
  public sealed class RDFTriple {
    private readonly RDFTerm subject;
    private readonly RDFTerm predicate;
    private readonly RDFTerm objectRdf;

    /// <xmlbegin id="3"/><summary>Initializes a new instance of the <see cref='RDFTriple'/> class.</summary>
    /// <param name='subject'>The subject term.</param>
    /// <param name='predicate'>The predicate term.</param>
    /// <param name='objectRdf'>The object term.</param>
  ///
  ///
  ///
    public RDFTriple(RDFTerm subject, RDFTerm predicate, RDFTerm objectRdf) {
      if (objectRdf == null) {
        throw new ArgumentNullException(nameof(objectRdf));
      }
      this.objectRdf = objectRdf;
      if (predicate == null) {
        throw new ArgumentNullException(nameof(predicate));
      }
      if (!(predicate.GetKind() == RDFTerm.IRI)) {
        throw new ArgumentException("doesn't satisfy predicate.kind==RDFTerm.IRI");
      }
      this.predicate = predicate;
      if (subject == null) {
        throw new ArgumentNullException(nameof(subject));
      }
      if (!(subject.GetKind() == RDFTerm.IRI ||
          subject.GetKind() == RDFTerm.BLANK)) {
        throw new
         ArgumentException(
  "doesn't satisfy subject.kind==RDFTerm.IRI || subject.kind==RDFTerm.BLANK");
      }
      this.subject = subject;
    }

    /// <xmlbegin id="4"/><summary>Initializes a new instance of the <see cref='RDFTriple'/> class.</summary>
    /// <param name='triple'>The parameter <paramref name='triple'/> is a
    /// RDFTriple object.</param>
    /// <exception cref='T:System.ArgumentNullException'>The parameter
    /// <paramref name='triple'/> is null.</exception>
  ///
  ///
  ///
    public RDFTriple(RDFTriple triple)
      : this(
        Check(triple).subject,
        Check(triple).predicate,
        Check(triple).objectRdf) {
    }

    private static RDFTriple Check(RDFTriple triple) {
      if (triple == null) {
        throw new ArgumentNullException(nameof(triple));
      }
      return triple;
    }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTriple.Equals(System.Object)"]/*'/>
    public override sealed bool Equals(object obj) {
      if (this == obj) {
        return true;
      }
      var other = obj as RDFTriple;
      if (other == null) {
        return false;
      }
      if (this.objectRdf == null) {
        if (other.objectRdf != null) {
          return false;
        }
      } else if (!this.objectRdf.Equals(other.objectRdf)) {
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
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTriple.GetObject"]/*'/>
    public RDFTerm GetObject() {
      return this.objectRdf;
    }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTriple.GetPredicate"]/*'/>
    public RDFTerm GetPredicate() {
      return this.predicate;
    }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTriple.GetSubject"]/*'/>
    public RDFTerm GetSubject() {
      return this.subject;
    }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTriple.GetHashCode"]/*'/>
    public override sealed int GetHashCode() {
      unchecked {
        var prime = 31;
        int result = prime + ((this.objectRdf == null) ? 0 :
             this.objectRdf.GetHashCode());
        result = (prime * result) +
            ((this.predicate == null) ? 0 : this.predicate.GetHashCode());
        bool subjnull = this.subject == null;
        result = (prime * result) + (subjnull ? 0 :
          this.subject.GetHashCode());
        return result;
      }
    }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.RDFTriple.ToString"]/*'/>
    public override sealed string ToString() {
      return this.subject.ToString() + " " + this.predicate.ToString() + " " +
            this.objectRdf.ToString() + " .";
    }
  }
}
