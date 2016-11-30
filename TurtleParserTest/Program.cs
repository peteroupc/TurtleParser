using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using PeterO;
using PeterO.Rdf;

namespace EncodingTest {
  class Program {
    static void Main () {
      foreach (var f in Directory.GetFiles (
        ".",
        "*.ttl")) {
        using (var fs = new FileStream (f, FileMode.Open)) {
                    Console.WriteLine (f);
          var bs = DataIO.ToReader (fs);
          var ttlp = new TurtleParser (bs);
          var sets = ttlp.Parse();
          foreach (var rdf in sets) {
            Console.WriteLine (rdf);
          }
        }
      }
    }
  }
}
