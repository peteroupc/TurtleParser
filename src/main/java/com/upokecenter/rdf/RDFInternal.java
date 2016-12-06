package com.upokecenter.util;
/*
Written in 2013 by Peter Occil.
Any copyright is dedicated to the Public Domain.
http://creativecommons.org/publicdomain/zero/1.0/

If you like this, you should donate to Peter O.
at: http://peteroupc.github.io/
*/

import java.util.*;

  final class RDFInternal {
    /**
     *
     */
  static void replaceBlankNodes(
  Set<RDFTriple> triples,
      Map<String, RDFTerm> bnodeLabels) {
    if (bnodeLabels.size() == 0) {
 return;
}
    Map<String, RDFTerm> newBlankNodes = new
      HashMap<String, RDFTerm>();
    List<RDFTriple[]> changedTriples = new ArrayList<RDFTriple[]>();
    int[] nodeindex = new int[] { 0 };
     for (RDFTriple triple : triples) {
      boolean changed = false;
      RDFTerm subj = triple.getSubject();
      if (subj.getKind() == RDFTerm.BLANK) {
        String oldname = subj.getValue();
        String newname = suggestBlankNodeName(oldname, nodeindex, bnodeLabels);
        if (!newname.equals(oldname)) {
            RDFTerm newNode = newBlankNodes.containsKey(oldname) ?
                    newBlankNodes.get(oldname) : null;
          if (newNode == null) {
            newNode = RDFTerm.fromBlankNode(newname);
            bnodeLabels.put(newname, newNode);
            newBlankNodes.put(oldname, newNode);
          }
          subj = newNode;
          changed = true;
        }
      }
      RDFTerm obj = triple.getObject();
      if (obj.getKind() == RDFTerm.BLANK) {
        String oldname = obj.getValue();
        String newname = suggestBlankNodeName(oldname, nodeindex, bnodeLabels);
        if (!newname.equals(oldname)) {
                    RDFTerm newNode = newBlankNodes.containsKey(oldname) ?
                    newBlankNodes.get(oldname) : null;
          if (newNode == null) {
            newNode = RDFTerm.fromBlankNode(newname);
            bnodeLabels.put(newname, newNode);
            newBlankNodes.put(oldname, newNode);
          }
          obj = newNode;
          changed = true;
        }
      }
      if (changed) {
        RDFTriple[] newTriple = new RDFTriple[] { triple,
            new RDFTriple(subj, triple.getPredicate(), obj)
        };
        changedTriples.add(newTriple);
      }
    }
     for (RDFTriple[] triple : changedTriples) {
      triples.Remove(triple[0]);
      triples.Add(triple[1]);
    }
  }

  private static String suggestBlankNodeName(
      String node, int[] nodeindex, Map<String, RDFTerm> bnodeLabels) {
    boolean validnode = node.length() > 0;
    // Check if the blank node label is valid
    // under N-Triples
    for (int i = 0; i < node.length(); ++i) {
      int c = node.charAt(i);
      if (i == 0 && !((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))) {
        validnode = false;
        break;
      }
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
      node = "b" + (nodeindex[0]).toString();
      if (!bnodeLabels.containsKey(node)) {
 return node;
}
      ++nodeindex[0];
    }
  }

  private RDFInternal() {
}
}
