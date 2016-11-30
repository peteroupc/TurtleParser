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
    /// <summary>Initializes a new instance of the ParserException
    /// class.</summary>
        public ParserException() : base() {
        }

    /// <summary>Initializes a new instance of the ParserException
    /// class.</summary>
    /// <param name='str'>A string object.</param>
        public ParserException(string str) : base(str) {
    }
}
}
