package com.upokecenter.util;
/*
Written in 2013 by Peter Occil.
Any copyright is dedicated to the Public Domain.
http://creativecommons.org/publicdomain/zero/1.0/

If you like this, you should donate to Peter O.
at: http://peteroupc.github.io/
*/

    /**
     * Not documented yet.
     */
  public final class RDFTriple {
    private RDFTerm subject, predicate, objectRdf;

    /**
     * Initializes a new instance of the {@link com.upokecenter.Rdf.getRDFTriple()}
     * class.
     * @param subject A RDFTerm object.
     * @param predicate Another RDFTerm object.
     * @param objectRdf A RDFTerm object. (3).
     */
    public RDFTriple(RDFTerm subject, RDFTerm predicate, RDFTerm objectRdf) {
      this.setSubject(subject);
      this.setPredicate(predicate);
      this.setObject(objectRdf);
    }

    /**
     * Initializes a new instance of the {@link com.upokecenter.Rdf.getRDFTriple()}
     * class.
     * @param triple A RDFTriple object.
     * @throws java.lang.NullPointerException The parameter {@code triple} is null.
     */
    public RDFTriple(RDFTriple triple) {
      if (triple == null) {
        throw new NullPointerException("triple");
      }
      this.setSubject(triple.subject);
      this.setPredicate(triple.predicate);
      this.setObject(triple.objectRdf);
    }

    /**
     * Not documented yet.
     * @return The return value is not documented yet.
     */
    @Override public final boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      RDFTriple other = ((obj instanceof RDFTriple) ? (RDFTriple)obj : null);
      if (other == null) {
        return false;
      }
      if (this.objectRdf == null) {
        if (other.objectRdf != null) {
          return false;
        }
      } else if (!this.objectRdf.equals(other.objectRdf)) {
        return false;
      }
      if (this.predicate == null) {
        if (other.predicate != null) {
          return false;
        }
      } else if (!this.predicate.equals(other.predicate)) {
        return false;
      }
      if (this.subject == null) {
        return other.subject != null;
      } else {
        return !this.subject.equals(other.subject);
      }
    }

    /**
     * Not documented yet.
     * @return A RDFTerm object.
     */
    public RDFTerm getObject() {
      return this.objectRdf;
    }

    /**
     * Not documented yet.
     * @return A RDFTerm object.
     */
    public RDFTerm getPredicate() {
      return this.predicate;
    }

    /**
     * Not documented yet.
     * @return A RDFTerm object.
     */
    public RDFTerm getSubject() {
      return this.subject;
    }

    /**
     * Not documented yet.
     * @return The return value is not documented yet.
     */
    @Override public final int hashCode() {
      {
        int prime = 31;
        int result = prime + ((this.objectRdf == null) ? 0 :
             this.objectRdf.hashCode());
        result = (prime * result) +
            ((this.predicate == null) ? 0 : this.predicate.hashCode());
        boolean subjnull = this.subject == null;
        result = (prime * result) + (subjnull ? 0 :
          this.subject.hashCode());
        return result;
      }
    }

    private void setObject(RDFTerm objectRdf) {
      if (objectRdf == null) {
        throw new NullPointerException("Object");
      }
      this.objectRdf = objectRdf;
    }

    private void setPredicate(RDFTerm predicate) {
      if (predicate == null) {
        throw new NullPointerException("predicate");
      }
      if (!(predicate.getKind() == RDFTerm.IRI)) {
    throw new IllegalArgumentException("doesn't satisfy predicate.kind==RDFTerm.IRI");
      }
      this.predicate = predicate;
    }

    private void setSubject(RDFTerm subject) {
      if (subject == null) {
        throw new NullPointerException("subject");
      }
      if (!(subject.getKind() == RDFTerm.IRI ||
          subject.getKind() == RDFTerm.BLANK)) {
        throw new
         IllegalArgumentException(
  "doesn't satisfy subject.kind==RDFTerm.IRI || subject.kind==RDFTerm.BLANK");
      }
      this.subject = subject;
    }

    /**
     * Not documented yet.
     * @return The return value is not documented yet.
     */
    @Override public final String toString() {
      return this.subject.toString() + " " + this.predicate.toString() + " " +
            this.objectRdf.toString() + " .";
    }
  }
