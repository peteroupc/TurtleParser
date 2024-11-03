using System.Collections.Generic;

/*
Written in 2013 by Peter Occil.
Any copyright to this work is released to the Public Domain.
In case this is not possible, this work is also
licensed under the Unlicense: https://unlicense.org/

*/
namespace PeterO.Rdf {
  /// <summary>Not documented yet.</summary>
  public interface IRDFParser {
    /// <summary>Not documented yet.</summary>
    /// <returns>The return value is not documented yet.</returns>
    ISet<RDFTriple> Parse();
  }
}
