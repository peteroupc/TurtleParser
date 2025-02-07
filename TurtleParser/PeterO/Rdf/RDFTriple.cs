using System;

/*
Written in 2013 by Peter Occil.
Any copyright to this work is released to the Public Domain.
In case this is not possible, this work is also
licensed under the Unlicense: https://unlicense.org/

*/
namespace PeterO.Rdf {
  /// <summary>Not documented yet.</summary>
  public sealed class RDFTriple {
    private readonly RDFTerm subject;
    private readonly RDFTerm predicate;
    private readonly RDFTerm objectRdf;

    /// <summary>Initializes a new instance of the
    /// <see cref='PeterO.Rdf.RDFTriple'/> class.</summary>
    /// <param name='subject'>The subject term.</param>
    /// <param name='predicate'>The predicate term.</param>
    /// <param name='objectRdf'>The object term.</param>
    /// <exception cref='ArgumentNullException'>The parameter <paramref
    /// name='objectRdf'/> or <paramref name='predicate'/> or <paramref
    /// name='subject'/> is null.</exception>
    public RDFTriple(RDFTerm subject, RDFTerm predicate, RDFTerm objectRdf) {
      if (objectRdf == null) {
        throw new ArgumentNullException(nameof(objectRdf));
      }
      this.objectRdf = objectRdf;
      if (predicate == null) {
        throw new ArgumentNullException(nameof(predicate));
      }
      if (!(predicate.GetKind() == RDFTerm.IRI)) {
        throw new ArgumentException("doesn't satisfy" +
          "\u0020predicate.kind==RDFTerm.IRI");
      }
      this.predicate = predicate;
      if (subject == null) {
        throw new ArgumentNullException(nameof(subject));
      }
      if (!(subject.GetKind() == RDFTerm.IRI ||
        subject.GetKind() == RDFTerm.BLANK)) {
        throw new
        ArgumentException(
          "doesn't satisfy subject.kind==RDFTerm.IRI ||" +
          "\u0020subject.kind==RDFTerm.BLANK");
      }
      this.subject = subject;
    }

    /// <summary>Initializes a new instance of the
    /// <see cref='PeterO.Rdf.RDFTriple'/> class.</summary>
    /// <param name='triple'>The parameter <paramref name='triple'/> is a
    /// RDFTriple object.</param>
    public RDFTriple(RDFTriple triple) : this(
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

    /// <summary>Not documented yet.</summary>
    /// <param name='obj'>The parameter <paramref name='obj'/> is a Object
    /// object.</param>
    /// <returns>The return value is not documented yet.</returns>
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
        return other.subject == null;
      } else {
        return this.subject.Equals(other.subject);
      }
    }

    /// <summary>Not documented yet.</summary>
    /// <returns>A RDFTerm object.</returns>
    public RDFTerm GetObject() {
      return this.objectRdf;
    }

    /// <summary>Not documented yet.</summary>
    /// <returns>A RDFTerm object.</returns>
    public RDFTerm GetPredicate() {
      return this.predicate;
    }

    /// <summary>Not documented yet.</summary>
    /// <returns>A RDFTerm object.</returns>
    public RDFTerm GetSubject() {
      return this.subject;
    }

    /// <summary>Not documented yet.</summary>
    /// <returns>The return value is not documented yet.</returns>
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

    /// <summary>Not documented yet.</summary>
    /// <returns>The return value is not documented yet.</returns>
    public override sealed string ToString() {
      return this.subject.ToString() + " " + this.predicate.ToString() + " " +
        this.objectRdf.ToString() + " .";
    }
  }
}
