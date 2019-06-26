using System.Collections.Generic;

    /*
Written in 2013 by Peter Occil.
Any copyright is dedicated to the Public Domain.
http://creativecommons.org/publicdomain/zero/1.0/

If you like this, you should donate to Peter O.
at: http://peteroupc.github.io/
*/
namespace PeterO.Rdf {
/// <summary>Not documented yet.</summary>
  public interface IRDFParser {
    /// <include file='../../docs.xml'
    /// path='docs/doc[@name="M:PeterO.Rdf.IRDFParser.Parse"]/*'/>
   ISet<RDFTriple> Parse();
}
}
