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

    /// <summary>Not documented yet.</summary>
  ///
public class ParserException : IOException {
    /// <summary>Initializes a new instance of the
    /// <see cref='ParserException'/> class.</summary>
  ///
        public ParserException() : base() {
        }

    /// <summary>Initializes a new instance of the
    /// <see cref='ParserException'/> class.</summary>
    /// <param name='str'>The parameter <paramref name='str'/> is a text
    /// string.</param>
  ///
        public ParserException(string str) : base(str) {
    }
}
}
