/*
Written in 2013 by Peter Occil.
Any copyright is dedicated to the Public Domain.
http://creativecommons.org/publicdomain/zero/1.0/

If you like this, you should donate to Peter O.
at: http://upokecenter.dreamhosters.com/articles/donate-now-2/
*/
namespace PeterO.Rdf {
using System;

public sealed class RDFTriple {
  private RDFTerm subject, predicate, _object;

  public RDFTriple(RDFTerm subject, RDFTerm predicate, RDFTerm _object) {
    setSubject(subject);
    setPredicate(predicate);
    setObject(_object);
  }

  public RDFTriple(RDFTriple triple) {
    if (triple == null) {
 throw new ArgumentNullException("triple");
}
    setSubject(triple.subject);
    setPredicate(triple.predicate);
    setObject(triple._object);
  }

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
    var other = (RDFTriple) obj;
    if (_object == null) {
      if (other._object != null) {
 return false;
}
    } else if (!_object.Equals(other._object)) {
 return false;
}
    if (predicate == null) {
      if (other.predicate != null) {
 return false;
}
    } else if (!predicate.Equals(other.predicate)) {
 return false;
}
    if (subject == null) {
      return (other.subject != null);
    } else {
 return (!subject.Equals(other.subject));
}
  }

  public RDFTerm getObject() {
    return _object;
  }

  public RDFTerm getPredicate() {
    return predicate;
  }

  public RDFTerm getSubject() {
    return subject;
  }

  public override sealed int GetHashCode() {unchecked {
     var prime = 31;
 int result = prime + ((_object == null) ? 0 :
      _object.GetHashCode());
    result = prime * result+
        ((predicate == null) ? 0 : predicate.GetHashCode());
    result = prime * result + ((subject == null) ? 0 : subject.GetHashCode());
    return result;
  }}

  private void setObject(RDFTerm _object) {
    if ((_object) == null) {
 throw new ArgumentNullException("object");
}
    this._object = _object;
  }

  private void setPredicate(RDFTerm predicate) {
    if ((predicate) == null) {
 throw new ArgumentNullException("predicate");
}
    if (!(predicate.getKind() == RDFTerm.IRI)) {
 throw new ArgumentException("doesn't satisfy predicate.kind==RDFTerm.IRI");
}
    this.predicate = predicate;
  }

  private void setSubject(RDFTerm subject) {
    if ((subject) == null) {
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

  public override sealed string ToString() {
return subject.ToString()+" " +predicate.ToString()+" "
      +_object.ToString()+" ." ;
  }
}
}
