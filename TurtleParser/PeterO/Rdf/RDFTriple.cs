/*
Written in 2013 by Peter Occil.
Any copyright is dedicated to the Public Domain.
http://creativecommons.org/publicdomain/zero/1.0/

If you like this, you should donate to Peter O.
at: http://peteroupc.github.io/
*/
namespace PeterO.Rdf {
  using System;

    /// <summary>Not documented yet.</summary>
  ///
  public sealed class RDFTriple {
    private volatile RDFTerm subject, predicate, objectRdf;

    /// <summary>Initializes a new instance of the
    /// <see cref='T:PeterO.Rdf.RDFTriple'/> class.</summary>
    /// <param name='subject'>The subject term.</param>
    /// <param name='predicate'>The predicate term.</param>
    /// <param name='objectRdf'>The object term.</param>
  ///
    public RDFTriple(RDFTerm subject, RDFTerm predicate, RDFTerm objectRdf) {
      this.setSubject(subject);
      this.setPredicate(predicate);
      this.setObject(objectRdf);
    }

    /// <summary>Initializes a new instance of the
    /// <see cref='T:PeterO.Rdf.RDFTriple'/> class.</summary>
    /// <param name='triple'>The parameter <paramref name='triple'/> is a
    /// RDFTriple object.</param>
    /// <exception cref='T:System.ArgumentNullException'>The parameter
    /// <paramref name='triple'/> is null.</exception>
  ///
    public RDFTriple(RDFTriple triple) {
      if (triple == null) {
        throw new ArgumentNullException(nameof(triple));
      }
      this.setSubject(triple.subject);
      this.setPredicate(triple.predicate);
      this.setObject(triple.objectRdf);
    }

    /// <summary>Not documented yet.</summary>
    /// <param name='obj'>The parameter <paramref name='obj'/> is not
    /// documented yet.</param>
    /// <param name='obj'>The parameter <paramref name='obj'/> is not
    /// documented yet.</param>
    /// <returns>The return value is not documented yet.</returns>
  ///
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

    /// <summary>Not documented yet.</summary>
    /// <returns>A RDFTerm object.</returns>
  ///
    public RDFTerm getObject() {
      return this.objectRdf;
    }

    /// <summary>Not documented yet.</summary>
    /// <returns>A RDFTerm object.</returns>
  ///
    public RDFTerm getPredicate() {
      return this.predicate;
    }

    /// <summary>Not documented yet.</summary>
    /// <returns>A RDFTerm object.</returns>
  ///
    public RDFTerm getSubject() {
      return this.subject;
    }

    /// <summary>Not documented yet.</summary>
    /// <returns>The return value is not documented yet.</returns>
  ///
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

    private void setObject(RDFTerm rdfObject) {
      if (rdfObject == null) {
        throw new ArgumentNullException(nameof(rdfObject));
      }
      this.objectRdf = rdfObject;
    }

    private void setPredicate(RDFTerm predicate) {
      if (predicate == null) {
        throw new ArgumentNullException(nameof(predicate));
      }
      if (!(predicate.getKind() == RDFTerm.IRI)) {
    throw new ArgumentException("doesn't satisfy predicate.kind==RDFTerm.IRI");
      }
      this.predicate = predicate;
    }

    private void setSubject(RDFTerm subject) {
      if (subject == null) {
        throw new ArgumentNullException(nameof(subject));
      }
      if (!(subject.getKind() == RDFTerm.IRI ||
          subject.getKind() == RDFTerm.BLANK)) {
        throw new
         ArgumentException(
  "doesn't satisfy subject.kind==RDFTerm.IRI || subject.kind==RDFTerm.BLANK");
      }
      this.subject = subject;
    }

    /// <summary>Not documented yet.</summary>
    /// <returns>The return value is not documented yet.</returns>
  ///
    public override sealed string ToString() {
      return this.subject.ToString() + " " + this.predicate.ToString() + " " +
            this.objectRdf.ToString() + " .";
    }
  }
}
