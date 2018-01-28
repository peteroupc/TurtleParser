/*
Written in 2013 by Peter Occil.
Any copyright is dedicated to the Public Domain.
http://creativecommons.org/publicdomain/zero/1.0/

If you like this, you should donate to Peter O.
at: http://peteroupc.github.io/
*/
namespace PeterO.Rdf {
using System;
using System.IO;

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="T:PeterO.Rdf.ParserException"]/*'/>
public class ParserException : IOException {
    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.ParserException.#ctor"]/*'/>
        public ParserException() : base() {
        }

    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.ParserException.#ctor(System.String)"]/*'/>
        public ParserException(string str) : base(str) {
    }
}
}
