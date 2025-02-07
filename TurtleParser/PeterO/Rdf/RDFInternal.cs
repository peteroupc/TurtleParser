using System;
using System.Collections.Generic;
using System.Globalization;
using PeterO;
/*
Written in 2013 by Peter Occil.
Any copyright to this work is released to the Public Domain.
In case this is not possible, this work is also
licensed under the Unlicense: https://unlicense.org/

*/
namespace PeterO.Rdf {
  internal static class RDFInternal {
    /// <summary>Not documented yet.</summary>
    /// <param name='triples'>The parameter <paramref name='triples'/> is
    /// a.Collections.Generic.ISet{PeterO.Rdf.RDFTriple} object.</param>
    /// <param name='bnodeLabels'>The parameter <paramref
    /// name='bnodeLabels'/> is a.Collections.Generic.IDictionary
    /// {System.String object.</param>
    internal static void ReplaceBlankNodes(
      ISet<RDFTriple> triples,
      IDictionary<string, RDFTerm> bnodeLabels) {
      if (bnodeLabels.Count == 0) {
        return;
      }
      IDictionary<string, RDFTerm> newBlankNodes = new
      Dictionary<string, RDFTerm>();
      IList<RDFTriple[]> changedTriples = new List<RDFTriple[]>();
      var nodeindex = new int[] { 0 };
      foreach (RDFTriple triple in triples) {
        var changed = false;
        RDFTerm subj = triple.GetSubject();
        if (subj.GetKind() == RDFTerm.BLANK) {
          string oldname = subj.GetValue();
          string newname = SuggestBlankNodeName(
              oldname,
              nodeindex,
              bnodeLabels);
          if (!newname.Equals(oldname, StringComparison.Ordinal)) {
            RDFTerm newNode = newBlankNodes.ContainsKey(oldname) ?
              newBlankNodes[oldname] : null;
            if (newNode == null) {
              newNode = RDFTerm.FromBlankNode(newname);
              bnodeLabels.Add(newname, newNode);
              newBlankNodes.Add(oldname, newNode);
            }
            subj = newNode;
            changed = true;
          }
        }
        RDFTerm obj = triple.GetObject();
        if (obj.GetKind() == RDFTerm.BLANK) {
          string oldname = obj.GetValue();
          string newname = SuggestBlankNodeName(
              oldname,
              nodeindex,
              bnodeLabels);
          if (!newname.Equals(oldname, StringComparison.Ordinal)) {
            RDFTerm newNode = newBlankNodes.ContainsKey(oldname) ?
              newBlankNodes[oldname] : null;
            if (newNode == null) {
              newNode = RDFTerm.FromBlankNode(newname);
              bnodeLabels.Add(newname, newNode);
              newBlankNodes.Add(oldname, newNode);
            }
            obj = newNode;
            changed = true;
          }
        }
        if (changed) {
          var newTriple = new RDFTriple[] {
            triple,
            new RDFTriple(subj, triple.GetPredicate(), obj),
          };
          changedTriples.Add(newTriple);
        }
      }
      foreach (RDFTriple[] triple in changedTriples) {
        triples.Remove(triple[0]);
        triples.Add(triple[1]);
      }
    }

    private static string SuggestBlankNodeName(
      string node,
      int[] nodeindex,
      IDictionary<string, RDFTerm> bnodeLabels) {
      bool validnode = node.Length > 0;
      // Check if the blank node label is valid
      // under N-Triples
      for (int i = 0; i < node.Length; ++i) {
        int c = node[i];
        // NOTE: Blank nodes that start with a digit are now allowed
        // under N-Triples
        // if (i == 0 && !((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))) {
        // validnode = false;
        // break;
        // }
        if (i >= 0 && !((c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') ||
          (c >= 'a' && c <= 'z'))) {
          validnode = false;
          break;
        }
      }
      if (validnode) {
        return node;
      }
      while (true) {
        // Generate a new blank node label,
        // and ensure it's unique
        node = "b" + Convert.ToString(
            (int)nodeindex[0],
            CultureInfo.InvariantCulture);
        if (!bnodeLabels.ContainsKey(node)) {
          return node;
        }
        ++nodeindex[0];
      }
    }
  }
}
